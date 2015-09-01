package com.httht.lenovo.pfu.GPS;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.httht.lenovo.pfu.R;

public class GpsActivity extends AppCompatActivity {
    public static final int LOCATION_CHANGED=1;
//    public static final int GPS_ENABLED=2;
    public static final int GPS_UPDATE_SATELLITE_STATUS=3;
    public static final int GPS_REQUEST_ENABLE=4;
    public static final int GPS_CLOSED=5;
    private TextView textView;
    private double latitude=0.0;
    private double longitude=0.0;
    private LocationManager lm;
    private Handler handler;
    private MyGPS myGPS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        textView=(TextView)findViewById(R.id.textView8);
        lm=(LocationManager)getSystemService(LOCATION_SERVICE);
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case LOCATION_CHANGED:
                        Location location=(Location)msg.obj;
                        if(location!=null){
                            textView.setText("经度："+location.getLongitude()+"; 纬度:"+location.getLatitude());
                        }
                        break;
                    case GPS_REQUEST_ENABLE:
                        new AlertDialog.Builder(GpsActivity.this)
                                .setMessage("需要打开GPS")
                                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("取消",null)
                                .show();
                        break;
                    case GPS_CLOSED:
                        Toast.makeText(GpsActivity.this,"GPS被关闭",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        myGPS=new MyGPS(lm,handler);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_g, menu);
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
}
