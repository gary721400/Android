package com.afaya.mp3playerdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.afaya.toolslib.LogUtil;
import com.afaya.toolslib.OTHERTOOLS;

/**
 * Created by Administrator on 2017/11/17 0017.
 */

public class PlayerSerProx {
    private static final String TAG = "PlayerSerProx";
    private Context mContext;
    private Messenger mMessenger;
    private Handler mHandler;

    private Messenger sMessenger;
//    private PlayerSer playerSer;

    public PlayerSerProx(Context mContext, Messenger mMessenger, Handler mHandler) {
        LogUtil.E(TAG,"PlayerSerProx init");
        this.mContext = mContext;
        this.mMessenger = mMessenger;
        this.mHandler = mHandler;
    }


    public void start(){
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
        Intent intent = new Intent(mContext, PlayerSer.class);
        mContext.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void stop(){
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
        mContext.unbindService(serviceConnection);
    }

    private ServiceConnection serviceConnection = new PlayerConnection();
    private class PlayerConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
            sMessenger = new Messenger(service);
            Message msg = Message.obtain();
            msg.what = MP3COMMAND.MESSENGER_REPLY;
            msg.replyTo = mMessenger;
            try {
                sMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
            sMessenger = null;
        }
    }


    public void sendMsgToSer(Message msg) {
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
        if (sMessenger != null) {
            try {
                sMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
