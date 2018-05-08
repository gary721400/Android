package com.afaya.mp3playerdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.afaya.toolslib.InCardUtil;
import com.afaya.toolslib.LogUtil;
import com.afaya.toolslib.OTHERTOOLS;

import java.io.File;
import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Mp3PlayerStub mp3PlayerStub = null;
    private Button btBegin;
    private Button btPause;
    private Button btEnd;
    private Button btReset;

    private String dirPath;
    private Handler sHandler = null;
    private PlayerSerProx playerSerProx = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @Override
    protected void onStart() {
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
        super.onStart();
//        if (mp3PlayerStub == null) {
//            mp3PlayerStub = new Mp3PlayerStub(this, mHandler);
//        }
//
//        sHandler = mp3PlayerStub.getMp3PlayerHandler();


        if (playerSerProx == null) {
            playerSerProx = new PlayerSerProx(this, mMessenger, mHandler);
        }
    }

    @Override
    protected void onResume() {
        LogUtil.E(TAG,OTHERTOOLS.getCurrentMethod());
        super.onResume();
        playerSerProx.start();
    }

    @Override
    protected void onPause() {
        LogUtil.E(TAG,OTHERTOOLS.getCurrentMethod());
        super.onPause();
        playerSerProx.stop();
    }

    private void init() {

        dirPath = InCardUtil.getInRootDir(this);

        LogUtil.E(TAG,"dirPath = " + dirPath.toString());

        btBegin = findViewById(R.id.begin);
        btBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = dirPath + File.separator+"1.mp3";
//                String str = dirPath + File.separator+"1.mp4";

//                LogUtil.E(TAG,"btBegin onClick = " + str );
//                Message msg = Message.obtain();
//                msg.what = MP3COMMAND.MP3_BEGIN;
//                msg.obj = str;
//
//                if (sHandler != null) {
//                    sHandler.sendMessage(msg);
//                }
                Message msg = Message.obtain();
                msg.what = MP3COMMAND.MP3_BEGIN;
                msg.obj = str;
                playerSerProx.sendMsgToSer(msg);
            }
        });

        btEnd = findViewById(R.id.end);
        btEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.E(TAG,"btEnd onClick");

            }
        });

        btPause = findViewById(R.id.pause);
        btPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.E(TAG,"btPause onClick");
                Message msg = Message.obtain();
                msg.what = MP3COMMAND.MP3_PAUSE;
                playerSerProx.sendMsgToSer(msg);
            }
        });

        btReset = findViewById(R.id.reset);
        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.E(TAG,"btReset onClick");
                Message msg  = Message.obtain();
                msg.what = MP3COMMAND.MP3_RESET;
                playerSerProx.sendMsgToSer(msg);
            }
        });

    }


    private void handleMsg(Message msg) {
        LogUtil.E(TAG,"msg.what = " + msg.what);
        switch (msg.what) {

            default:
                break;

        }
    }


    private Handler mHandler = new MyHandler(this);
    private static class MyHandler extends Handler{
        private final WeakReference<MainActivity> mainActivityWeakReference;

        public MyHandler(MainActivity mainActivity) {
            this.mainActivityWeakReference = new WeakReference<MainActivity>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity mainActivity = mainActivityWeakReference.get();
            if (mainActivity == null) {
                return;
            }
//            super.handleMessage(msg);
            mainActivity.handleMsg(msg);
        }
    }
    private Messenger mMessenger = new Messenger(mHandler);
}
