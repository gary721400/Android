package com.afaya.mp3playerdemo;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import com.afaya.toolslib.LogUtil;

import java.io.IOException;

import static com.afaya.mp3playerdemo.MP3COMMAND.MESSENGER_REPLY;
import static com.afaya.mp3playerdemo.MP3COMMAND.MP3_BEGIN;
import static com.afaya.mp3playerdemo.MP3COMMAND.MP3_CONTINUE;
import static com.afaya.mp3playerdemo.MP3COMMAND.MP3_PAUSE;
import static com.afaya.mp3playerdemo.MP3COMMAND.MP3_RESET;

public class PlayerSer extends Service {
    private static final String TAG = "PlayerSer";

    private Messenger mMessenger = null;
    private String playStr = null;
    private static MediaPlayer mp = null;
    public PlayerSer() {
        mp = new MediaPlayer();
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
//                if (mp == mp) {
//
//                }
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return sMessenger.getBinder();
    }


    private void handlerMsg(Message msg) {
        LogUtil.E(TAG,"msg:what = " + msg.what);
        switch (msg.what) {
            case MESSENGER_REPLY:
                mMessenger = msg.replyTo;
                break;
            case MP3_BEGIN:
                playStr = (String) msg.obj;
                playBegin();
                break;
            case MP3_PAUSE:
                playOrPause();
                break;
            case MP3_CONTINUE:
                break;
            case MP3_RESET:
                stop();
                break;
            default:
                break;

        }
    }

    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            handlerMsg(msg);
        }
    }

    private Handler sHandler = new MyHandler();
    private Messenger sMessenger = new Messenger(sHandler);





//    public MusicService() {
//        try {
//            musicIndex = 1;
//            mp.setDataSource(musicDir[musicIndex]);
//            mp.prepare();
//        } catch (Exception e) {
//            Log.d("hint","can't get to the song");
//            e.printStackTrace();
//        }



    public void playBegin(){
        try {
            mp.setDataSource(playStr);
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void playOrPause() {
        if(mp.isPlaying()){
            mp.pause();
        } else {
            mp.start();
        }
    }
    public void stop() {
        if(mp != null) {
            mp.stop();
            try {
                mp.prepare();
                mp.seekTo(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
//    public void nextMusic() {
//        if(mp != null && musicIndex < 3) {
//            mp.stop();
//            try {
//                mp.reset();
//                mp.setDataSource(musicDir[musicIndex+1]);
//                musicIndex++;
//                mp.prepare();
//                mp.seekTo(0);
//                mp.start();
//            } catch (Exception e) {
//                Log.d("hint", "can't jump next music");
//                e.printStackTrace();
//            }
//        }
//    }
//    public void preMusic() {
//        if(mp != null && musicIndex > 0) {
//            mp.stop();
//            try {
//                mp.reset();
//                mp.setDataSource(musicDir[musicIndex-1]);
//                musicIndex--;
//                mp.prepare();
//                mp.seekTo(0);
//                mp.start();
//            } catch (Exception e) {
//                Log.d("hint", "can't jump pre music");
//                e.printStackTrace();
//            }
//        }
//    }
}
