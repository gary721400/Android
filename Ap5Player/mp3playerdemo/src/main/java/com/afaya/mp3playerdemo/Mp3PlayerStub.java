package com.afaya.mp3playerdemo;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.afaya.toolslib.LogUtil;

import java.io.IOException;

/**
 * Created by Administrator on 2017/11/17 0017.
 */

public class Mp3PlayerStub {
    private static final String TAG = "Mp3PlayerStub";
    private HandlerThread mp3Player = null;
    private Context mContext;
    private Handler mHandler;
    private Handler mp3PlayerHandler;
    private MediaPlayer mediaPlayer;
    private int offPlace;
    private String curPath;
    public Mp3PlayerStub(Context mContext, Handler mHandler) {
        this.mContext = mContext;
        this.mHandler = mHandler;

        mp3Player = new HandlerThread("mp3Player");
        mp3Player.start();

        mp3PlayerHandler = new PlayerHandler(mp3Player.getLooper());

        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (mp == mediaPlayer) {
                    mediaPlayer.start();
                }
            }
        });
    }


    public  Handler getMp3PlayerHandler() {
        return mp3PlayerHandler;
    }



    private class PlayerHandler extends Handler{
        private Looper looper;

        public PlayerHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            LogUtil.E(TAG,"player:what:arg1 = " + msg.what + " : " +  msg.arg1);
            switch (msg.what) {
                case MP3COMMAND.MP3_RESET:
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    }
                    mediaPlayer.release();
                    break;
                case MP3COMMAND.MP3_BEGIN:
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.reset();
                    }
                    curPath = (String) msg.obj;
                    try {
                        LogUtil.E(TAG,"playStr = " + curPath);
                        mediaPlayer.setDataSource(curPath);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
                case MP3COMMAND.MP3_PAUSE:
                    if (mediaPlayer.isPlaying()) {
                        offPlace = mediaPlayer.getCurrentPosition();
                    mediaPlayer.pause();
                    }
                    break;
                case MP3COMMAND.MP3_CONTINUE:
                    if (mediaPlayer.isPlaying()) {
                        return;
                    }

                    try {

                        mediaPlayer.setDataSource(curPath);
                        mediaPlayer.seekTo(offPlace);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    mediaPlayer.
                    break;
                case MP3COMMAND.MP3_END:

                    break;
                default:
                    break;
            }
//            super.handleMessage(msg);
        }
    }



}
