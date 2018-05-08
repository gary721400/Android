package com.afaya.mp3playerdemo;

import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;

import com.afaya.toolslib.InCardUtil;
import com.afaya.toolslib.LogUtil;
import com.afaya.toolslib.OTHERTOOLS;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class Main2Activity extends AppCompatActivity implements SurfaceHolder.Callback{

    private static final String TAG = "Main2Activity";
    private String dirPath;
    private String curPath;
    private MediaPlayer mediaPlayer = null;
    private SurfaceView ve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(Window.FEATURE_NO_TITLE,Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main2);

        ve = findViewById(R.id.vedioSur);
        ve.getHolder().addCallback(this);
        ve.getHolder().setKeepScreenOn(true);
        ve.getHolder().setFormat(PixelFormat.TRANSLUCENT);

//        ve.setRotation(90);
//        ve.setScaleX(1280f/720);
//        ve.setScaleY(720f/1280);

        init();
    }

    @Override
    protected void onResume() {
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
        super.onResume();
        mHandler.obtainMessage(MP3COMMAND.MP3_BEGIN).sendToTarget();
    }

    private void init() {

        dirPath = InCardUtil.getInRootDir(this);
//        recDir = /storage/emulated/0/Main2Activity
//        dirPath = "/storage/emulated/0/MainActivity";
        LogUtil.E(TAG, "dirPath = " + dirPath.toString());
//        curPath = dirPath + File.separator + "1.mp3";
        curPath = dirPath + File.separator + "1.mp4";
//
//        btBegin = findViewById(R.id.begin);
//        btBegin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String str = dirPath + File.separator+"1.mp3";
////                String str = dirPath + File.separator+"1.mp4";
//
////                LogUtil.E(TAG,"btBegin onClick = " + str );
////                Message msg = Message.obtain();
////                msg.what = MP3COMMAND.MP3_BEGIN;
////                msg.obj = str;
////
////                if (sHandler != null) {
////                    sHandler.sendMessage(msg);
////                }
//                Message msg = Message.obtain();
//                msg.what = MP3COMMAND.MP3_BEGIN;
//                msg.obj = str;
//                playerSerProx.sendMsgToSer(msg);
//            }
//        });
//
//        btEnd = findViewById(R.id.end);
//        btEnd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LogUtil.E(TAG,"btEnd onClick");
//
//            }
//        });
//
//        btPause = findViewById(R.id.pause);
//        btPause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LogUtil.E(TAG,"btPause onClick");
//                Message msg = Message.obtain();
//                msg.what = MP3COMMAND.MP3_PAUSE;
//                playerSerProx.sendMsgToSer(msg);
//            }
//        });
//
//        btReset = findViewById(R.id.reset);
//        btReset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LogUtil.E(TAG,"btReset onClick");
//                Message msg  = Message.obtain();
//                msg.what = MP3COMMAND.MP3_RESET;
//                playerSerProx.sendMsgToSer(msg);
//            }
//        });

    }

    private void handleMsg(Message msg) {
        LogUtil.E(TAG, "msg.what = " + msg.what);
        switch (msg.what) {
            case MP3COMMAND.MP3_RESET:
                break;
            case MP3COMMAND.MP3_BEGIN:
                playBegin();
                break;
            case MP3COMMAND.MP3_PAUSE:
                break;
            case 100:
                if (msg.obj == null) {
                    mediaPlayer.setDisplay(null);
                }else{
                    SurfaceHolder holder = (SurfaceHolder) msg.obj;
                    if (holder != null) {
                        mediaPlayer.setDisplay(holder);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                ve.requestLayout();
                            }
                        });
                    }
                }

                break;
            default:
                try {
                    throw new Throwable("unKnow command!");
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        LogUtil.E(TAG,OTHERTOOLS.getCurrentMethod());
        Message msg = new Message();
        msg.what = 100;
        msg.obj = holder;
        mHandler.sendMessage(msg);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        LogUtil.E(TAG,OTHERTOOLS.getCurrentMethod());
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        LogUtil.E(TAG,OTHERTOOLS.getCurrentMethod());
        holder.getSurface().release();
    }


    private static class MyHandler extends Handler {
        private final WeakReference<Main2Activity> main2ActivityWeakReference;

        private MyHandler(Main2Activity main2Activity) {
            this.main2ActivityWeakReference = new WeakReference<Main2Activity>(main2Activity);
        }

//        public MyHandler(Looper looper) {
//            super(looper);
//            LogUtil.E(TAG,OTHERTOOLS.getCurrentMethod());
//        }

        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            Main2Activity main2Activity = main2ActivityWeakReference.get();
            if (main2Activity == null) {
                return;
            }
            main2Activity.handleMsg(msg);
        }
    }

    private Handler mHandler = new MyHandler(this);


    private void playBegin() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        LogUtil.E(TAG, "curPath = " + curPath);
        try {
            mediaPlayer.setDataSource(curPath);
//            mediaPlayer.setSurface(new Surface());
//            mediaPlayer.setSurface(ve.getHolder().getSurface());
//            mediaPlayer.setDisplay(ve.getHolder());
            mediaPlayer.prepareAsync();

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    LogUtil.E(TAG, "load over!");
                    mediaPlayer.start();
                    OTHERTOOLS.longShow(Main2Activity.this, "开始播放");
                }
            });

            mediaPlayer.setLooping(true);

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    LogUtil.E(TAG, "play over");
                }
            });

            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    LogUtil.E(TAG, "play error");
                    return false;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playOrPause() {
        if (mediaPlayer == null) {
            return;
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }
    }

    private void stop() {
        if (mediaPlayer == null) {
            return;
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }

    }



}
