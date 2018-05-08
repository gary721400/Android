package com.afaya.a30udemo;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements AfayaLog {

    private static final String TAG = "MainActivity";
    private Handler sHandler = null;

    public volatile static boolean BindFlag = false;
    public volatile static boolean BindSendFlag = false;

    private Button bt1 = null;
    private TextView decTv = null;
    private TextView hexTv = null;
    private TextView stateTv = null;
    private Timer timer = null;
    private MyConnection myConnection = null;

    private volatile boolean connected = false;
    private Messenger sMessenger = null;
    private Messenger mMessenger = null;

    private USBT12 usbT12 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myLog("onCreate");
        setContentView(R.layout.activity_main);

        bt1 = (Button) findViewById(R.id.connectButton);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bt1.isEnabled()) {
                    myLog("ready bind");
                    bt1.setEnabled(false);
                    MainActivity.BindFlag = true;
                    MainActivity.BindSendFlag = true;
//                    sendServerNews(SOMETHING.READY_BIND,0);
//                    Message msg = mHandler.obtainMessage();
//                    msg.what = SOMETHING.READY_BIND;
//                    msg.sendToTarget();

                }
            }
        });

        decTv = (TextView) findViewById(R.id.textDec);
        hexTv = (TextView) findViewById(R.id.textHex);
        stateTv = (TextView) findViewById(R.id.textState);
        regesiterServicer();
    }

    class MyConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            myLog("onServiceConnected");
            sMessenger = new Messenger(iBinder);
            mMessenger = new Messenger(mHandler);

            Message msg = Message.obtain();
            msg.what = SOMETHING.BACK_HANDLER;
            msg.replyTo = mMessenger;
            try {
                sMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            myLog("onServiceDisconnected");
        }
    }

    void regesiterServicer() {
        timer = new Timer();
        timer.schedule(new MyTimerTask(0), 0, 1000);
//        Intent intent = new Intent(MainActivity.this,UsbService.class);
//        myConnection = new MyConnection();
//        bindService(intent,myConnection,BIND_AUTO_CREATE);
        if (usbT12 == null) {
            usbT12 = new USBT12(this, mHandler);
            usbT12.startFindTimer();
//              mHandler.postDelayed(usbT12.searchThread,SOMETHING.SCAN_USB);
        }
    }

    void unregesterServicer() {
//        timer.cancel();
//        unbindService(myConnection);
//        mHandler.removeCallbacks(usbT12.searchThread);
//        mHandler.removeCallbacks(usbT12.readThread);
        if (usbT12 != null) {
            usbT12.stopAllTimer();
            usbT12 = null;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            myLog("msg.what = " + msg.what);
            switch (msg.what) {
                case SOMETHING.RECEVIE_INDEX:
//                    if(msg.arg1 == SOMETHING.NEW_INDEX) {
                    int i = msg.arg1;
                    myLog("index = " + msg.arg1);

                    hexTv.setText(Integer.toHexString(i).toString());
                    decTv.setText(Integer.toString(i).toString());
//                        if(i == 11){
//                            MainActivity.BindFlag = true;
//                        }
//                    }
//                    mHandler.postDelayed(usbT12.readThread,SOMETHING.READ_USB);
//                    reposeServier();
                    break;
                case SOMETHING.DISCONNECTED:
                case SOMETHING.NOUSBDEVICE:
                    connected = false;
//                    stateTv.setText("Disconnected");
                    stateTv.setText(getString(R.string.disconnected));
                    bt1.setEnabled(false);
                    break;
                case SOMETHING.CONNECTED:
//                    stateTv.setText("Connected");
                    stateTv.setText(getString(R.string.connected));
                    bt1.setEnabled(true);
//                    mHandler.postDelayed(usbT12.readThread,SOMETHING.READ_USB);
                    break;

                case SOMETHING.BIND_END:
                    bt1.setEnabled(true);
                    MainActivity.BindFlag = false;
//                    mHandler.postDelayed(usbT12.readThread,SOMETHING.READ_USB);
                    break;
                case SOMETHING.NO_USBPER:
                    finish();
                    break;


            }
            super.handleMessage(msg);
        }
    };

//    Messenger mMessenger = new Messenger(mHandler);


    @Override
    protected void onStart() {
        super.onStart();
        myLog("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        myLog("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        myLog("onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myLog("onDestroy");
        unregesterServicer();
    }

    void reposeServier() {
        Message msg = new Message();
        msg.what = SOMETHING.REPOSE_SERVER;
        try {
            sMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void myLog(String str) {
        if (AfayaLog.D) {
            Log.d(TAG, str);
        }
    }


    private void sendServerNews(int what, int data) {
        Message msg = new Message();
        msg.what = what;
        msg.arg1 = data;
        try {
            sMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    class MyTimerTask extends TimerTask {
        private int data = 0;

        public MyTimerTask(int data) {
            this.data = data;
        }

        @Override
        public void run() {
            if (MainActivity.BindFlag) {
                data++;
                if (data > 5) {
//                    Message msg = mHandler.obtainMessage();
//                    msg.what = SOMETHING.BIND_END;
//                    msg.sendToTarget();
                    sendMessage(SOMETHING.BIND_END, 0, 0);
                }

            } else {
                data = 0;
            }

//            myLog("mytimertask");
//            data++;
//            Message msg = mHandler.obtainMessage();
//            msg.what = SOMETHING.RECEVIE_INDEX;
//            msg.arg1 = data;
//            msg.sendToTarget();

        }
    }

    private void sendMessage(int what, int arg1, int arg2) {
        Message msg = mHandler.obtainMessage();
        msg.what = what;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        msg.sendToTarget();
    }

}
