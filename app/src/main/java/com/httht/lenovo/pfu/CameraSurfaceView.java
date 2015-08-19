package com.httht.lenovo.pfu;

import android.content.Context;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lenovo on 2015/8/18.
 */
public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder;

    private Camera camera;
    private int curCameraIndex=0;
    private MediaRecorder recorder;

    private int cameraNums=0;
    private boolean isRecord=false;

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        holder=getHolder();
        holder.addCallback(this);
    }

    public String takePicture(final String dir){
        if(Build.VERSION.SDK_INT<21){
            if(camera!=null&&!isRecord){
                int i=1;
                while(new File(dir+"/PFU_"+String.valueOf(i)+".jpeg").exists()){
                    i++;
                }
                final String path=dir+"/PFU_"+String.valueOf(i)+".jpeg";
                camera.takePicture(null, null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        try {
                            FileOutputStream fos=new FileOutputStream(path);
                            fos.write(data);
                            fos.close();
                            camera.startPreview();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
                return path;
            }
        }
        return null;
    }

    public String startMediaRecord(String dir){
        if(Build.VERSION.SDK_INT<21) {
            if(camera!=null&&!isRecord) {
                recorder = new MediaRecorder();
                camera.unlock();
                recorder.setCamera(camera);
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
//                CamcorderProfile profile=CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
//                recorder.setProfile(profile);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                recorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
                SimpleDateFormat df = new SimpleDateFormat("yy_MM_dd_HH_mm_ss");
                String cur = df.format(new Date());
                String path = dir + "/PFU" + cur + ".3gp";
                recorder.setOutputFile(path);
//                recorder.setVideoSize(getWidth(),getHeight());
//                recorder.setVideoFrameRate(4);
                recorder.setPreviewDisplay(holder.getSurface());
                try {
                    isRecord = true;
                    recorder.prepare();
                    recorder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return path;
            }
        }
        return null;
    }

    public void stopMediaRecord(){
        if(Build.VERSION.SDK_INT<21){
            if(recorder!=null&&isRecord){
                recorder.stop();
                recorder.reset();
                recorder.release();
                recorder=null;
                if(camera!=null){
                    camera.lock();
                }
                isRecord=false;
            }
        }
    }

    public void changeCamera(){
        if(Build.VERSION.SDK_INT<21) {
            if (cameraNums > 1&&!isRecord) {
                curCameraIndex = (curCameraIndex + 1) % cameraNums;
                if(camera!=null){
                    camera.stopPreview();
                    camera.release();
                }
                camera = Camera.open(curCameraIndex);
                if(camera!=null){
                    try {
                        camera.setPreviewDisplay(holder);
                        camera.startPreview();
                    } catch (IOException e) {
                        e.printStackTrace();
                        camera.release();
                        camera=null;
                    }

                }
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if(Build.VERSION.SDK_INT<21){
            cameraNums=Camera.getNumberOfCameras();
            Log.e("cameraNums", String.valueOf(cameraNums));
            if(camera==null&&cameraNums>0) {
                camera = Camera.open(curCameraIndex);
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if(Build.VERSION.SDK_INT<21) {
            if (camera != null) {
//                Camera.Parameters parameters = camera.getParameters();
//                parameters.setPreviewSize(width, height);
//                camera.setParameters(parameters);
                try {
                    camera.setPreviewDisplay(holder);
                    camera.startPreview();
                } catch (IOException e) {
                    camera.release();
                    camera = null;
                }
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(Build.VERSION.SDK_INT<21){
            if(camera!=null){
                camera.stopPreview();
                camera.release();
                camera=null;
            }
        }
    }
}
