package com.example.normalbluetoothdemo.UI;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afaya.toolslib.LogUtil;
import com.afaya.toolslib.OTHERTOOLS;
import com.example.normalbluetoothdemo.R;
import com.example.normalbluetoothdemo.Server.BlueToothState;
import com.example.normalbluetoothdemo.Server.BluetoothChatService;
import com.example.normalbluetoothdemo.Server.MessageNews;
import com.example.normalbluetoothdemo.tools.Crc16;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private final static int ALLLENG = 4 * 1024 * 1024;
    private final static int OFF_PER = 512;


    public static int CUSTUM_CODE = 1;
    private String devName;
    private String devAddress;

    private TextView displayNews;
    private Button button1;
    private Button button2;
    private Button button3;
    private BluetoothChatService mChatService = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private StringBuilder sb = null;
    private int lineNum = 0;
    private int strLen = 0;
    private MainActivity.MyHandler mHandler = new MainActivity.MyHandler(this);

    private ScheduledExecutorService mScheduledExecutorService = null;
    private byte[] dataByte = null;
    private int curOff = 0;
    private int allLeng = 0;
    private short curCrc = 0;
    private int curSendLen = 0;
    private long begingClick = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        if (sb == null) {
            sb = new StringBuilder();
        }
//        if (dataByte == null) {
//            dataByte = new Byte[ALLLENG];
//        }
//        readAssetsFile();


        if (mScheduledExecutorService == null) {
            mScheduledExecutorService = Executors.newScheduledThreadPool(1);
        }
//        displayNews = (TextView) findViewById(R.id.textView);
//        bleNews.setText(devName + "----" + devAddress);

        displayNews = (TextView) findViewById(R.id.textView);

        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtil.E(TAG, "bt OnClick!");
                Intent intent = new Intent(MainActivity.this, BlueToothBleChoiceActivity.class);
                startActivity(intent);
            }
        });

        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtil.E(TAG, "BUTTON1");
//                String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//                String str = "123456";
//                mHandler.obtainMessage(MessageNews.MESSAGE_WRITE, str.length(), 0, str.getBytes());
//                blueTootheWrite(str.getBytes());
                readAssetsFile1();
                LogUtil.E(TAG, "fileLeng = " + dataByte.length);
                curOff = 0;
                mHandler.obtainMessage(MessageNews.MESSAGE_WRITE).sendToTarget();
                begingClick = System.currentTimeMillis();

            }
        });


        button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.E(TAG, "BUTTON3");
//                String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//                String str = "123456";
//                mHandler.obtainMessage(MessageNews.MESSAGE_WRITE, str.length(), 0, str.getBytes());
//                blueTootheWrite(str.getBytes());
                readAssetsFile2();
                LogUtil.E(TAG, "fileLeng = " + dataByte.length);
                curOff = 0;
                mHandler.obtainMessage(MessageNews.MESSAGE_WRITE).sendToTarget();
                begingClick = System.currentTimeMillis();
            }
        });


        if (!readBlueToothNews()) {
            LogUtil.E(TAG, "goto BleScan");
            Intent intent = new Intent(MainActivity.this, BlueToothBleChoiceActivity.class);
//            intent.setAction("com.afaya.BleScan");
//            intent.set
//            intent.setClass("com.afaya.blebluetoothdemo", "com.afaya.blebluetoothdemo.BlueToothChoiceActivity");
//            intent.setComponent(new ComponentName(this, DeviceScanActivity.class));
            startActivity(intent);
        }
    }

    private void readAssetsFile1() {
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
//        InputStreamReader isr = new InputStreamReader(this.getClass().getClassLoader()
//                .getResourceAsStream("assets/" + "2"));
        InputStream resourceAsStream = this.getClass().getClassLoader()
                .getResourceAsStream("assets/" + "1");
        try {
            allLeng = resourceAsStream.available();
            dataByte = new byte[allLeng];
            resourceAsStream.read(dataByte);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void readAssetsFile2() {
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
//        InputStreamReader isr = new InputStreamReader(this.getClass().getClassLoader()
//                .getResourceAsStream("assets/" + "2"));
        InputStream resourceAsStream = this.getClass().getClassLoader()
                .getResourceAsStream("assets/" + "2");
        try {
            allLeng = resourceAsStream.available();
            dataByte = new byte[allLeng];
            resourceAsStream.read(dataByte);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private boolean readBlueToothNews() {
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
        boolean bleFlag;
        SharedPreferences pref = getSharedPreferences("bluetoothdata", MODE_PRIVATE);
        devName = pref.getString("name", null);
        devAddress = pref.getString("address", null);
        bleFlag = pref.getBoolean("ble", false);
        LogUtil.E(TAG, "name = " + devName + "address = " + devAddress);
        if (devName == null) {
            return false;
        }


        if (devAddress == null) {
            return false;
        }
        if (bleFlag != false) {
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
    }

    @Override
    protected void onPause() {
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
        super.onPause();
        blueTootheStop();
        mScheduledExecutorService.shutdown();
    }

    @Override
    protected void onResume() {
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
        super.onResume();
        if (devAddress != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("name: ").append(devName).append("\r\n")
                    .append("address: ").append(devAddress);
//            bleNews.setText(stringBuilder);
//            SingleServceProx.getInstance().setDevAddress(devAddress);
//            mainProx.setDevName(devName);
//            mainProx.setDevAddress(devAddress);
//            mainProx.startService();
//            startService();
        }
        blueTootheConnect();
//        mScheduledExecutorService.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
////                String str = "123456";
////                mHandler.obtainMessage(MessageNews.MESSAGE_WRITE, str.length(), 0, str.getBytes());
//                blueTootheWrite(str.getBytes());
//            }
//        }, 10, 10, TimeUnit.SECONDS);
    }
//    private Messenger mMessenger = new Messenger(mHandler);

    public void blueTootheConnect() {

        LogUtil.E(TAG, "BlueToothe connect! ");
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BlueToothState.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        } else {
            setupChat();
            connectDevice(new Intent(), false);
//                 connectDevice(false);
        }

    }

    /**
     * 普通蓝牙处理
     */
    private void setupChat() {
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(MainActivity.this, mHandler);
    }

    private void connectDevice(Intent data, boolean secure) {
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
        // Get the device MAC address
//        String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BLuetoothDevice object
        final BluetoothManager bluetoothManager = (BluetoothManager) MainActivity.this.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(devAddress);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
    }

    public void blueTootheStop() {
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
        if (mChatService != null) mChatService.stop();
        mChatService = null;


        //mConnected = false;
        //cancleCheckBlueTooth(aHandler);
    }

    public void saveBlueToothNews() {
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
        SharedPreferences.Editor editor = getSharedPreferences("bluetoothdata", MODE_PRIVATE).edit();
        editor.clear();
        editor.putString("name", devName);
        editor.putString("address", devAddress);
        editor.putBoolean("ble", false);
//        editor.putLong("allNum", AfayaApp.allNum);
        editor.commit();
        LogUtil.E(TAG, "saveBlueToothNews ok!");
    }

    public void handleMsg(Message msg) {
        LogUtil.E(TAG, "MSG:what = " + msg.what);
        switch (msg.what) {
            case MessageNews.MESSAGE_DEVICE_NAME:
                break;
            case MessageNews.MESSAGE_READ:
//                LogUtil.E(TAG, "arg1 = " + msg.arg1);
                recvMesToDo(msg);
                break;
            case MessageNews.MESSAGE_WRITE:
                sendDataToDo();

                break;
            case MessageNews.MESSAGE_TOAST:
                OTHERTOOLS.longShow(MainActivity.this, msg.obj.toString());
                break;
            default:
                break;
        }
    }

    private void recvMesToDo(Message msg) {
//        LogUtil.E(TAG, "msg.arg1 = " + msg.arg1);
        if(testMode(msg))return;

//
//        int crcNum = Crc16.chechRecData((byte[]) msg.obj,(msg.arg1-2));
//        LogUtil.E(TAG,"temp:crc = " + t + " : " + crcNum);
//        if(t != crcNum){
//            return;
//        }

//        String str = new String((byte[]) msg.obj, 0, 1);
//                int i = Crc16.toInt((byte[]) msg.obj);
//        Integer ack = Integer.parseInt(String.valueOf(temp[0]));
//        LogUtil.E(TAG, "ack = " + ack);
//        int crcNum = Crc16.chechRecData((byte[]) msg.obj,(msg.arg1-2));
//        LogUtil.E(TAG,"crc = " + crcNum);
//        if (!crcCheck((byte[]) msg.obj,msg.arg1)) return;
        byte[] temp = (byte[]) msg.obj;
//        if (msg.arg1 == 3) {
        if (true) {
            //下载数据应答
            String str = new String((byte[]) msg.obj, 0, msg.arg1);
//                int i = Crc16.toInt((byte[]) msg.obj);
            Integer ack = Integer.parseInt(String.valueOf(temp[0]));
            LogUtil.E(TAG, "ack = " + ack);


            //下载正常处理
            if (MessageNews.MESSAGE_WRITE_ACK_OK == ack) {
                curOff = curOff + curSendLen;
                LogUtil.E(TAG, "cur:all = " + curOff + " : " + allLeng);
                if (curOff >= allLeng) {
                    curOff = 0;
                    curSendLen = 0;
                    long endClick = System.currentTimeMillis() - begingClick;
                    float f = (endClick / 1000);
                    LogUtil.E(TAG, "send over!");
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("长度 = ").append(allLeng).append("\r\n")
                            .append("时间(秒) = ").append(f).append("\r\n")
                            .append("每秒字节数 = ").append((float) allLeng / f).append("\r\n");
                    stringBuilder.append("\r\n--------------------------------\r\n");
                    LogUtil.E(TAG, stringBuilder.toString());
                    sb.insert(0, stringBuilder.toString());
                    displayNews.setText(sb);

//                    LogUtil.E(TAG, "长度:时间:平均 = " + allLeng + " : " + endClick + " : " + (float) allLeng / f);

                } else {

                    mHandler.obtainMessage(MessageNews.MESSAGE_WRITE).sendToTarget();
                }
            } else {
                //下载失败，重新再发
                mHandler.obtainMessage(MessageNews.MESSAGE_WRITE).sendToTarget();
            }
        } else {
            //上传数据
        }

    }

    private void sendDataToDo() {
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
        curSendLen = allLeng - curOff;
        if (curSendLen >= OFF_PER) {
            curSendLen = OFF_PER;
        }
        byte[] data = new byte[curSendLen + 2];
        System.arraycopy(dataByte, curOff, data, 0, curSendLen);
        curCrc = Crc16.chechRecData(data, curSendLen);
        LogUtil.E(TAG, "curLen:curCrc = " + curSendLen + ":" + curCrc);
        data[curSendLen] = (byte) (curCrc >> 8);
        data[curSendLen + 1] = (byte) curCrc;
        blueTootheWrite(data);
    }

    private boolean testMode(Message msg) {
        LogUtil.E(TAG, "msg.arg1 = " + msg.arg1);
        byte[] temp = (byte[]) msg.obj;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < msg.arg1; i++) {
//                readBuf[i] = ((byte[]) msg.obj)[i];
            stringBuilder.append(String.format("%02X ", temp[i]));
        }
        LogUtil.E(TAG, stringBuilder.toString());
//        int t = ((int)temp[msg.arg1 -2])<<8;
//
//        t = t | (int)(temp[msg.arg1 - 1]);
//
//        int crcNum = Crc16.chechRecData((byte[]) msg.obj,(msg.arg1-2));
//        LogUtil.E(TAG,"temp:crc = " + t + " : " + crcNum);
//        if(t != crcNum){
//            return;
//        }
        return true;
    }

    public void blueTootheWrite(byte[] data) {
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
//        LogUtil.E(TAG,"str = " + new String(data,0,data.length) );
//        LogUtil.E(TAG,"strLen = " + data.length);
        if (mChatService == null) return;
        mChatService.write(data);
        //mConnected = false;
        //cancleCheckBlueTooth(aHandler);
    }

    private boolean crcCheck(byte[] data, int leng) {

//        msg.arg1 = " + msg.arg1);
//        byte[] temp = (byte[]) msg.obj;
//                StringBuilder stringBuilder = new StringBuilder();
//                for (int i = 0; i < msg.arg1; i++) {
////                readBuf[i] = ((byte[]) msg.obj)[i];
//                    stringBuilder.append(String.format("%02X ", temp[i]));
//                }
//          LogUtil.E(TAG,stringBuilder.toString());
        if (leng <= 2) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < leng; i++) {
//                readBuf[i] = ((byte[]) msg.obj)[i];
                stringBuilder.append(String.format("%02X ", data[i]));
            }
            LogUtil.E(TAG, stringBuilder.toString());
            return false;
        }

        int t = ((int) data[leng - 2]) << 8;

        t = t | (int) (data[leng - 1]);

        int crcNum = Crc16.chechRecData((byte[]) data, (leng - 2));
        LogUtil.E(TAG, "temp:crc = " + t + " : " + crcNum);
        if (t == crcNum) {
            return true;
        } else {
            return false;

        }

    }

    private static class MyHandler extends Handler {
        private final WeakReference<MainActivity> main3ActivityWeakReference;

        public MyHandler(MainActivity activity) {
            main3ActivityWeakReference = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = main3ActivityWeakReference.get();
            if (activity == null) {
                return;
            }
//            super.handleMessage(msg);
            activity.handleMsg(msg);
        }
    }
}
