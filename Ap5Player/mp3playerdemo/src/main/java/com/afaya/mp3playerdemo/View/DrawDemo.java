package com.afaya.mp3playerdemo.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.afaya.toolslib.LogUtil;
import com.afaya.toolslib.OTHERTOOLS;

/**
 * Created by Administrator on 2017/11/24 0024.
 */

public class DrawDemo extends View {
    private static final String TAG = "DrawDemo";
    private final Paint mPaint;


    private static final int DELAY = 1000;

    private int mCount;

    public DrawDemo(Context context) {
//        super(context);
        this(context, null, 0);
        LogUtil.E(TAG,"drawDemo one param");
    }

    public DrawDemo(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
        this(context, attrs, 0);
        LogUtil.E(TAG,"drawDemo two param");
    }

    public DrawDemo(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LogUtil.E(TAG,"drawDemo three param");
        mPaint = new Paint();
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(0xFFFF0000);
        mPaint.setTextSize(50);


    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
        handler.sendEmptyMessageDelayed(0x0001, DELAY);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LogUtil.E(TAG,OTHERTOOLS.getCurrentMethod());
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText("Hello World" + mCount, getWidth() >> 1, getHeight() >> 1, mPaint);
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mCount++;
            invalidate();
            handler.sendEmptyMessageDelayed(0x0001, DELAY);
        }
    };


}
