package com.afaya.mp3playerdemo.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.afaya.toolslib.LogUtil;

public class DrawSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "DrawSurfaceView";
    private static final long DELAY = 1000;
    private final SurfaceHolder mHolder;
    private Paint mPaint;
    private int mCount;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mCount++;
            Canvas canvas = mHolder.lockCanvas();
            canvas.drawColor(0xFFFFFFFF);//擦除原来的内容
            canvas.drawText("Hello World" + mCount, getWidth() >> 1, getHeight() >> 1, mPaint);
            mHolder.unlockCanvasAndPost(canvas);
            sendEmptyMessageDelayed(0x0001, DELAY);
        }
    };

    public DrawSurfaceView(Context context) {
        this(context, null);
    }

    public DrawSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(0xFFFF0000);
        mPaint.setTextSize(50);

        mHolder = getHolder();          //获取SurfaceView的ViewHolder对象
        mHolder.addCallback(this);    //为ViewHolder添加事件监听器
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mHandler.sendEmptyMessageDelayed(0x0001, DELAY);
        LogUtil.E(TAG,"surfaceCreated = " + Thread.currentThread().getName());//打印当前线程的名字
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        LogUtil.E(TAG,"surfaceChanged = " + Thread.currentThread().getName());//打印当前线程的名字
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mHandler.removeCallbacksAndMessages(null);
        LogUtil.E(TAG,"surfaceDestroyed = " +  Thread.currentThread().getName());//打印当前线程的名字
    }
}
