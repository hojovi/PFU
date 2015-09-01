package com.httht.lenovo.pfu;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.httht.lenovo.pfu.CCamera.CameraActivity;
import com.httht.lenovo.pfu.GPS.GpsActivity;
import com.httht.lenovo.pfu.MultiTouch.MultiTouchActivity;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private BaseAdapter adapter;
    private String[] listItems;
    private String[] supportedStatus;
//    private HashMap<String,String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView=(ListView)findViewById(R.id.listView);
        listItems=getResources().getStringArray(R.array.sensors);
        supportedStatus=new String[listItems.length];
        setSupportedStatus();
        try {
            adapter=new MyAdapter(listItems,supportedStatus);
        } catch (Exception e) {
            e.printStackTrace();
        }
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=null;
                switch (listItems[position]){
                    case "多点触控":
                        intent=new Intent(MainActivity.this, MultiTouchActivity.class);
                        intent.setAction(supportedStatus[position]);
                        startActivity(intent);
                        break;
                    case "摄像头":
                        intent=new Intent(MainActivity.this, CameraActivity.class);
                        startActivity(intent);
                        break;
                    case "GPS":
                        intent=new Intent(MainActivity.this, GpsActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    private void setSupportedStatus(){
        PackageManager pm=getPackageManager();
        for(int i=0;i<listItems.length;i++){
            switch (listItems[i]){
                case "多点触控":
                    if(pm.hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH_JAZZHAND)){
                        supportedStatus[i]="支持独立10点及以上";
                    }else if(pm.hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH_DISTINCT)){
                        supportedStatus[i]="支持独立两点及以上";
                    }else if(pm.hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN)){
                        supportedStatus[i]="支持两点";
                    }else{
                        supportedStatus[i]="不支持触控";
                    }
                    break;
                case "摄像头":
                    supportedStatus[i]="";
                    break;
                case "蓝牙":
                    if(pm.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)){
                        supportedStatus[i]="支持";
                    }else{
                        supportedStatus[i]="不支持";
                    }
                    break;
                case "GPS":
                    if(pm.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)){
                        supportedStatus[i]="支持";
                    }else{
                        supportedStatus[i]="不支持";
                    }
                    break;
                case "温度传感器":
                    if(pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_AMBIENT_TEMPERATURE)){
                        supportedStatus[i]="支持";
                    }else{
                        supportedStatus[i]="不支持";
                    }
                    break;
                case "湿度传感器":
                    if(pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_RELATIVE_HUMIDITY)){
                        supportedStatus[i]="支持";
                    }else{
                        supportedStatus[i]="不支持";
                    }
                    break;
                case "加速度传感器":
                    if(pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER)){
                        supportedStatus[i]="支持";
                    }else{
                        supportedStatus[i]="不支持";
                    }
                    break;
                case "距离传感器":
                    if(pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_PROXIMITY)){
                        supportedStatus[i]="支持";
                    }else{
                        supportedStatus[i]="不支持";
                    }
                    break;
                case "磁场传感器":
                    if(pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_COMPASS)){
                        supportedStatus[i]="支持";
                    }else{
                        supportedStatus[i]="不支持";
                    }
                    break;
                case "光线传感器":
                    if(pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_LIGHT)){
                        supportedStatus[i]="支持";
                    }else{
                        supportedStatus[i]="不支持";
                    }
                    break;
                case "NFC":
                    if(pm.hasSystemFeature(PackageManager.FEATURE_NFC)){
                        supportedStatus[i]="支持";
                    }else{
                        supportedStatus[i]="不支持";
                    }
                    break;
                case "心率传感器":
                    if(pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_HEART_RATE)&&pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_HEART_RATE_ECG)){
                        supportedStatus[i]="支持";
                    }else{
                        supportedStatus[i]="不支持";
                    }
                    break;
                case "计步器":
                    if(pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER)&&pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_DETECTOR)){
                        supportedStatus[i]="支持";
                    }else{
                        supportedStatus[i]="不支持";
                    }
                    break;
                default:
                    Log.e("miss",listItems[i]);
                    supportedStatus[i]="";
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class MyAdapter extends BaseAdapter{
        private String[] list1;
        private String[] list2;
        public MyAdapter(String[] list1,String[] list2) throws Exception {
            if(list1.length!=list2.length){
                throw new Exception("MainActivity.Adapter list1.length="+String.valueOf(list1.length)+" isn't equeals to list2.length="+String.valueOf(list2.length));
            }
            this.list1=list1;
            this.list2=list2;
        }
        @Override
        public int getCount() {
            return list1.length;
        }

        @Override
        public Object getItem(int position) {
            return list1[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder=null;
            if(convertView==null){
                convertView=getLayoutInflater().inflate(R.layout.main_items,null);
                holder=new Holder();
                holder.textView1=(TextView)convertView.findViewById(R.id.textView2);
                holder.textView2=(TextView)convertView.findViewById(R.id.textView3);
                convertView.setTag(holder);
            }else{
                holder= (Holder) convertView.getTag();
            }
            holder.textView1.setText(list1[position]);
            holder.textView2.setText(list2[position]);
            return convertView;
        }

        class Holder{
            TextView textView1;
            TextView textView2;
        }
    }
}
