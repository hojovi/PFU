package com.httht.lenovo.pfu.GPS;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by lenovo on 2015/9/1.
 */
public class BaseStation {
    private TelephonyManager manager;
    private Handler handler;
    private ConnectivityManager connectivityManager;
    private GsmCellLocation gsmCellLocation;
//    private double longtitude;
//    private double latitude;
    public BaseStation(TelephonyManager manager,Handler handler,ConnectivityManager connectivityManager){
        this.manager=manager;
        this.handler=handler;
        this.connectivityManager=connectivityManager;
        gsmCellLocation= (GsmCellLocation) manager.getCellLocation();
    }

    public void request(){
        int cid = gsmCellLocation.getCid();
        int lac = gsmCellLocation.getLac();
        int mnc = Integer.parseInt(manager.getNetworkOperator().substring(3, 5));
    }

    class MyThread extends Thread{
        private int cid;
        private int lac;
        private int mnc;
        public MyThread(int cid,int lac,int mnc){
            this.cid=cid;
            this.lac=lac;
            this.mnc=mnc;
        }
        @Override
        public void run() {
            NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
            StringBuilder re=new StringBuilder();
            if(networkInfo!=null&&networkInfo.isConnected()){
                String str=new StringBuilder("http://v.juhe.cn/cell/get?mnc=").append(mnc).append("&cid=").append(cid).append("&lac=").append(lac).append("&dtype=json").append("&key=07039fb6a8d8adee619432326fd60945").toString();
                Log.e("urladdress",str);
                try {
                    URL url=new URL(str);
                    HttpURLConnection connection=(HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(30000);
                    connection.setConnectTimeout(30000);
                    connection.connect();
                    BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String tmp;
                    while((tmp=br.readLine())!=null){
                        re.append(tmp);
                    }
                    br.close();
                    connection.disconnect();
                    JSONObject jsonObject=new JSONObject(re.toString());
                    if(jsonObject.getString("resultcode").equals("200")){
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        JSONObject jsonObject1=jsonArray.getJSONObject(0);
                        String longtitude=jsonObject1.getString("LNG");
                        String latitude=jsonObject1.getString("LAT");
                        handler.sendMessage(Message.obtain(null,GpsActivity.BASE_STATION_LOCATIONC_CHANGGED,new Pair<String,String>(longtitude,latitude)));
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

//    public class Location{
//        public double longtitude;
//        public double latitude;
//    }
}
