package com.httht.lenovo.pfu.GPS;

import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by lenovo on 2015/8/31.
 */
public class MyGPS {
    private LocationListener ll;
    private LocationManager lm;
//    private GpsStatus gs;
    private String provider;
    private Handler handler;
    public MyGPS(LocationManager lm, final Handler handler){
        this.handler=handler;
        this.lm=lm;
        provider=lm.getProvider(LocationManager.GPS_PROVIDER).getName();
        if(!lm.isProviderEnabled(provider)){
            handler.sendMessage(Message.obtain(null, GpsActivity.GPS_REQUEST_ENABLE));
        }else {
            handler.sendMessage(Message.obtain(null, GpsActivity.LOCATION_CHANGED, lm.getLastKnownLocation(provider)));
        }
        ll=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                handler.sendMessage(Message.obtain(null, GpsActivity.LOCATION_CHANGED,location));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.e("gps","打开");
            }

            @Override
            public void onProviderDisabled(String provider) {
                handler.sendMessage(Message.obtain(null, GpsActivity.GPS_CLOSED));
            }
        };
        lm.requestLocationUpdates(provider,1000,0,ll);
    }

    public void addGpsStatusListener(){
        final GpsStatus gpsStatus=lm.getGpsStatus(null);
        GpsStatus.Listener listener=new GpsStatus.Listener() {
            @Override
            public void onGpsStatusChanged(int event) {
                switch (event){
                    //发送GPS卫星状态事件
                    case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                        lm.getGpsStatus(gpsStatus);
                        Iterable<GpsSatellite> allSatellites = gpsStatus.getSatellites();
                        Iterator<GpsSatellite> it=allSatellites.iterator();
                        ArrayList<GpsSatellite> gss=new ArrayList<>();
                        while(it.hasNext()){
                            gss.add(it.next());
                        }
                        handler.sendMessage(Message.obtain(null, GpsActivity.GPS_UPDATE_SATELLITE_STATUS,gss));
                        break;
                    //停止定位事件
                    case GpsStatus.GPS_EVENT_STOPPED:
                        Log.d("Location", "GPS_EVENT_STOPPED");
                        break;
                }
            }
        };
        lm.addGpsStatusListener(listener);
    }
}
