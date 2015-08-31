package com.httht.lenovo.pfu.CCamera;

import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.httht.lenovo.pfu.R;

import java.io.File;

public class CameraActivity extends AppCompatActivity {
    private CameraManager cm;
    private CameraCharacteristics cc;

    private CameraSurfaceView surfaceView;
    private Button button;
    private Button button2;
    private Button button3;
    private Button button4;

    private String cacheStr=null;
//    private boolean isCacheUsed=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        surfaceView=(CameraSurfaceView)findViewById(R.id.view2);
        button=(Button)findViewById(R.id.button);
        button2=(Button)findViewById(R.id.button2);
        button3=(Button)findViewById(R.id.button3);
        button4=(Button)findViewById(R.id.button4);
        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.button:
                        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            String path=Environment.getExternalStorageDirectory()+"/PFU";
                            File file=new File(path);
                            if(!file.exists()){
                                file.mkdir();
                            }
                            String str=surfaceView.takePicture(path);
                            if(str!=null) {
                                Toast.makeText(CameraActivity.this, "存储为:" + str, Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                    case R.id.button2:
                        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                            String path1=Environment.getExternalStorageDirectory()+"/PFU";
                            File file1=new File(path1);
                            if(!file1.exists()){
                                file1.mkdir();
                            }
                            cacheStr=surfaceView.startMediaRecord(path1);
                        }
                        break;
                    case R.id.button3:
                        surfaceView.stopMediaRecord();
                        if(cacheStr!=null){
                            Toast.makeText(CameraActivity.this,"存储为："+cacheStr,Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.button4:
                        surfaceView.changeCamera();
                        break;
                }
            }
        };
        button.setOnClickListener(listener);
        button2.setOnClickListener(listener);
        button3.setOnClickListener(listener);
        button4.setOnClickListener(listener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camera, menu);
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
