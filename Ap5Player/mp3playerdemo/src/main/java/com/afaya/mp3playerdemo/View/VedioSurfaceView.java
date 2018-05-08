package com.afaya.mp3playerdemo.View;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceView;

/**
 * Created by Administrator on 2017/11/24 0024.
 */

public class VedioSurfaceView extends SurfaceView{
    private static final String TAG = "VedioSurfaceView";

    public VedioSurfaceView(Context context) {
        super(context);
    }

    public VedioSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VedioSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


    }
}
