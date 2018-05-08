package com.afaya.a30udemo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class UsbService extends Service implements AfayaLog{

    private static final String TAG = "UsbService";
    private Handler mHandler = null;
    private Messenger mMessenger = null;
//    private Message msg = null;
    private Timer timer = null;
    private Context sContext = null;

    public UsbService() {
        myLog("usbService constructor");
        sContext = this.getApplicationContext();

    }


    private Handler sHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            myLog("msg.what = " + msg.what);
            switch (msg.what){
                case SOMETHING.BACK_HANDLER:
                    mMessenger = msg.replyTo;
                    if(timer == null){
                        timer = new Timer();
                        timer.schedule(new ServierTimerTask(100),0,1000);
                    }
                    break;
                case SOMETHING.READY_BIND:
                    break;
            }
            super.handleMessage(msg);
        }
    };


    private Messenger sMessage = new Messenger(sHandler);

    @Override
    public void onCreate() {
        super.onCreate();
        myLog("onCreate");
//        if(msg == null){
//        msg = new Message();
//
//        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return sMessage.getBinder();
    }

    @Override
    public void onDestroy() {
        myLog("onDestroy");
        super.onDestroy();
//        if(msg != null)msg = null;
        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void myLog(String str) {
        if(AfayaLog.D){
            Log.d(TAG, str);
        }
    }


    class ServierTimerTask extends TimerTask {
        private int data = 0;
        private int count = 0;
        private boolean flag = false;

        public ServierTimerTask(int data) {
            this.data = data;
        }

        @Override
        public void run() {
            myLog("mytimertask");
            data++;
            count ++;

//            Message msg = mHandler.obtainMessage();
//            msg.what = SOMETHING.RECEVIE_INDEX;
//            msg.arg1 = data;
//            msg.sendToTarget();
            if(count > 10){
                if(flag){
                    flag = false;
                    sendMainNews(SOMETHING.DISCONNECTED,0);
                }else{
                    flag = true;
                    sendMainNews(SOMETHING.CONNECTED,0);
                }
                count = 0;
            }
            sendMainNews(SOMETHING.RECEVIE_INDEX,data);
        }
    }

    private void sendMainNews(int what,int data){
        Message msg = new Message();
        msg.what = what;
        msg.arg1 = data;
        try {
            mMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
