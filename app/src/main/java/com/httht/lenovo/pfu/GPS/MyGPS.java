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
    private LocationListener gpsll;
    private LocationListener networkll;
    private LocationManager lm;
//    private GpsStatus gs;
//    private String gpsProvider;
//    private String networkProvider;
    private Handler handler;
    public MyGPS(LocationManager lm, final Handler handler){
        this.handler=handler;
        this.lm=lm;
//        gpsProvider=lm.getProvider(LocationManager.GPS_PROVIDER).getName();

        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            handler.sendMessage(Message.obtain(null, GpsActivity.GPS_REQUEST_ENABLE));
        }
        handler.sendMessage(Message.obtain(null, GpsActivity.GPS_LOCATION_CHANGED, lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)));
        if(!lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            handler.sendMessage(Message.obtain(null, GpsActivity.NETWORK_REQUEST_ENABLE));
        }
        handler.sendMessage(Message.obtain(null, GpsActivity.NETWORK_LOCATION_CHANGED,lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)));
        gpsll=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.e("gps","locationchanged");
                handler.sendMessage(Message.obtain(null, GpsActivity.GPS_LOCATION_CHANGED,location));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.e(provider,"打开");
            }

            @Override
            public void onProviderDisabled(String provider) {
                if(provider.equals(LocationManager.GPS_PROVIDER)) {
                    handler.sendMessage(Message.obtain(null, GpsActivity.GPS_CLOSED));
                }
            }
        };
        networkll=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.e("network","locationchanged");
                handler.sendMessage(Message.obtain(null, GpsActivity.NETWORK_LOCATION_CHANGED,location));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.e(provider,"打开");
            }

            @Override
            public void onProviderDisabled(String provider) {
                handler.sendMessage(Message.obtain(null, GpsActivity.NETWORK_LOCATION_CLOSED));
            }
        };
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,gpsll);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,networkll);
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
