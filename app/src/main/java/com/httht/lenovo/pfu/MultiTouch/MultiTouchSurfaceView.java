package com.httht.lenovo.pfu.MultiTouch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

/**
 * Created by lenovo on 2015/8/17.
 */
public class MultiTouchSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private int width=0;
    private int height=0;

    private final String START_TEXT="触摸屏幕进行测试";

    private int MAX_TOUCHPOINTS=10;
    private Paint[] touchPaints=new Paint[MAX_TOUCHPOINTS];
    private int[] colors;

    private Paint textPaint=new Paint();

    private SurfaceHolder holder;

    public MultiTouchSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initPaint();
        holder=getHolder();
        holder.addCallback(this);
    }

    private void initPaint() {
        // 初始化10个不同颜色的画笔
        textPaint.setColor(Color.WHITE);
        colors=new int[10];
        colors[0] = Color.BLUE;
        colors[1] = Color.RED;
        colors[2] = Color.GREEN;
        colors[3] = Color.YELLOW;
        colors[4] = Color.CYAN;
        colors[5] = Color.MAGENTA;
        colors[6] = Color.DKGRAY;
        colors[7] = Color.WHITE;
        colors[8] = Color.LTGRAY;
        colors[9] = Color.GRAY;
        for (int i = 0; i < MAX_TOUCHPOINTS; i++) {
            touchPaints[i] = new Paint();
            touchPaints[i].setColor(colors[i]);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int touchPoints=event.getPointerCount();
        if(touchPoints>MAX_TOUCHPOINTS){
            touchPaints=new Paint[touchPoints];
            for(Paint paint:touchPaints){
                paint.setColor(new Random().nextInt(0xffffff));
            }
            MAX_TOUCHPOINTS=touchPoints;
        }

        Canvas canvas=holder.lockCanvas();
        if(canvas!=null){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    canvas.drawColor(Color.BLACK);
                    break;
                case MotionEvent.ACTION_UP:
                    canvas.drawColor(Color.BLACK);
                    float tWidth = textPaint.measureText(START_TEXT);
                    canvas.drawText(START_TEXT, width / 2 - tWidth / 2, height / 2,
                            textPaint);
                    break;
                case MotionEvent.ACTION_MOVE:
                    for(int i=0;i<touchPoints;i++){
                        int id=event.getPointerId(i);
                        float X=  event.getX();
                        float Y=  event.getY();
                        canvas.drawCircle(X,Y,20,touchPaints[id]);
                    }
                    break;
            }
            holder.unlockCanvasAndPost(canvas);
        }
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.width=width;
        this.height=height;
        Canvas c=holder.lockCanvas();
        if(c!=null){
            c.drawColor(Color.BLACK);
            float tWidth = textPaint.measureText(START_TEXT);
            c.drawText(START_TEXT, width / 2 - tWidth / 2, height / 2,
                    textPaint);
            getHolder().unlockCanvasAndPost(c);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
