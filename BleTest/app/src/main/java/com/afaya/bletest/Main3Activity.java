package com.afaya.bletest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main3Activity extends AppCompatActivity {

    private static final String TAG = "Main3Activity";
    private TextView bleNews;
    private TextView displayNews;
    private Button bt;
    private Button bt2;

    private StringBuilder sb = null;

    private int runCount = 0;

    private String devName;
    private String devAddress;
//        private SendDataProx bleServiceProxy = null;

    private BleSendNewsToServer bleSendNewsToServer = null;
    private Messenger bleMessengerProx = null;

    private ScheduledExecutorService scheduledExecutorService = null;
    private StringBuilder sbDisplay = null;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();


    }


    private void init() {
        sb = new StringBuilder();
        bleNews = (TextView) findViewById(R.id.textView);
//        bleNews.setText(devName + "----" + devAddress);

        displayNews = (TextView) findViewById(R.id.displaynews);

        bt2 = (Button) findViewById(R.id.button);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtil.E(TAG, "TEST BEGIN");

            }
        });

        bt = (Button) findViewById(R.id.button3);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtil.E(TAG, "bt OnClick!");
                Intent intent = new Intent(Main3Activity.this, BlueToothBleChoiceActivity.class);
                startActivity(intent);
            }
        });

        if (!readBlueToothNews()) {
            LogUtil.E(TAG, "goto BleScan");
            Intent intent = new Intent();
//            intent.setAction("com.afaya.BleScan");
//            intent.set
//            intent.setClass("com.afaya.blebluetoothdemo", "com.afaya.blebluetoothdemo.BlueToothChoiceActivity");
            intent.setComponent(new ComponentName(this, BlueToothBleChoiceActivity.class));
            startActivity(intent);
        }


        if (scheduledExecutorService == null) {
            scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
        }


    }

    @Override
    protected void onStart() {
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
//        if (bleServiceProx == null) {
//            bleServiceProx = new SendDataProx(mMessenger);
//        }
        if (sbDisplay == null) {
            sbDisplay = new StringBuilder();
        }
//        if (mainProx == null) {
//            mainProx = new MainProx(this, mMessenger, mHandler);
//        }
//        mainProx = MainProx.getIntance();
        super.onStart();
    }

    @Override
    protected void onResume() {
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
        LogUtil.E(TAG, "name = " + devName);
        LogUtil.E(TAG, "address = " + devAddress);
        SingleServceProx.getInstance().setContext(getApplicationContext());
        if (devAddress != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("name: ").append(devName).append("\r\n")
                    .append("address: ").append(devAddress);
            bleNews.setText(stringBuilder);
            SingleServceProx.getInstance().setDevAddress(devAddress);
//            mainProx.setDevName(devName);
//            mainProx.setDevAddress(devAddress);
//            mainProx.startService();
//            startService();
        }
        SingleServceProx.getInstance().bindHandler(mHandler);
        if (scheduledExecutorService != null) {
//            runCount = PenNews.ANDROD_DELETE_PEN_STORAGE
// ;
            runCount = 3;
            scheduledExecutorService.scheduleAtFixedRate(new Main3Activity.MyRun(), 5, 5, TimeUnit.SECONDS);
        }
//        mainProx.setaHandler(mHandler);
        super.onResume();
    }

    @Override
    protected void onPause() {
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();
            scheduledExecutorService = null;
        }
//        mainProx.stopService();
        SingleServceProx.getInstance().unBindHandler();
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
        super.onActivityResult(requestCode, resultCode, data);
    }


//
//    private boolean mConnected = false;
//    private BleBroadService mBLEService;
//
//    // Code to manage Service lifecycle.
//    private final ServiceConnection mServiceConnection = new ServiceConnection() {
//
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder service) {
//            LogUtil.E(TAG, "onServiceConnected");
////            rMessenger = new Messenger(service);
//            mBLEService = ((BleBroadService.LocalBinder) service).getService();
//            if (!mBLEService.initialize()) {
//                LogUtil.E(TAG, "Unable to initialize Bluetooth");
////                finish();
//            }
//            // Automatically connects to the device upon successful start-up initialization.
//            mConnected = mBLEService.connect(devAddress);
//            if (mConnected) {
//                LogUtil.E(TAG, "Ble connected OK!");
//
//            } else {
//                LogUtil.E(TAG, "ble connected fail!");
//            }
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//            LogUtil.E(TAG, "onServiceDisconnected: ");
////            mBLEService = null;
////            mConnected = false;
//        }
//    };
//
//
//    // Handles various events fired by the Service.
//    // ACTION_GATT_CONNECTED: connected to a GATT server.
//    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
//    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
//    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
//    //                        or notification operations.
//    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
////            final String action = intent.getAction();
////            if (SOMESTRING.ACTION_GATT_CONNECTED.equals(action)) {
////                LogUtil.E(TAG, "GATT_CONNECTED");
////                mConnectionState = BluetoothProfile.STATE_CONNECTED;
//////                aHandler.sendMessage(aHandler.obtainMessage(MESSAGE_STATE_CHANGE, STATE_CONNECTED, 0));
////            } else if (SOMESTRING.ACTION_GATT_DISCONNECTED.equals(action)) {
////                LogUtil.E(TAG, "GATT_DISCONNECTED");
//////                aHandler.sendMessage(aHandler.obtainMessage(MESSAGE_STATE_CHANGE, STATE_DISCONNECTED, 0));
//////                OTHERTOOLS.shortShow(mContext,"DISCONNECTED");
////                mConnectionState = BluetoothProfile.STATE_DISCONNECTED;
////                mConnected = false;
//////                mBluetoothLeService = null;
//////                final boolean result = mBluetoothLeService.connect(mDeviceAddress);
//////                LogUtil.E(TAG, "Connect request result=" + result);
////            } else if (SOMESTRING.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
//////                LogUtil.E(TAG, "SERVICES_DISCONNECTED");
////                LogUtil.E(TAG, "SERVICES_DISCOVERED");
////                // Show all the supported services and characteristics on the user interface.
////                displayGattServices(mBLEService.getSupportedGattServices());
//////                OTHERTOOLS.shortShow(mContext,"Connected");
////                mConnected = true;
////                mConnectionState = BluetoothProfile.STATE_DISCONNECTED;
//////                setCheckBlueTooth(aHandler);
////                //               aHandler.sendMessage(aHandler.obtainMessage(MESSAGE_STATE_CHANGE, STATE_DISCONNECTED, 0));
////                //mConnected = false;
//////                if (!blueToothIsRuning) {
//////                    OTHERTOOLS.shortShow(mContext, "Connected");
//////                    blueToothIsRuning = true;
//////                }
////            } else if (SOMESTRING.ACTION_DATA_AVAILABLE.equals(action)) {
//////                aHandler.obtainMessage(BlueToothRec.MESSAGE_READ, intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA).length, -1, intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA)).sendToTarget();
////                int tempLen = intent.getByteArrayExtra(SOMESTRING.EXTRA_DATA).length;
//////                byte[] tempData = intent.getByteArrayExtra(SOMESTRING.EXTRA_DATA);
////                Bundle bundle = new Bundle();
////                bundle.putByteArray(SOMESTRING.BUNDLE_NEWS, intent.getByteArrayExtra(SOMESTRING.EXTRA_DATA));
////                Message msg = Message.obtain();
////                msg.what = SOMESTRING.BLE_DATA_SEND;
////                msg.setData(bundle);
////                msg.sendToTarget();
////
//////                LogUtil.E(TAG, "len = " + tempLen);
//////                StringBuilder stringBuilder = new StringBuilder();
//////                for (int i = 0; i < tempLen; i++) {
//////                    recbuf[i] = tempData[i];
//////                    stringBuilder.append(String.format("%02X ", recbuf[i]));
//////                }
//////                LogUtil.E(TAG, "recBuf =  " + stringBuilder);
//////                System.arraycopy(recbuf,0,tempData,0,tempLen);
//////                System.arraycopy(tempData,0,recbuf,0,tempLen);
//////                DataUtil.printByte(tempData,tempLen);
////
////            }
//        }
//    };
//
//    public void startService() {
//        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
//
//        if (SOMESTRING.BLE) {
//            if (devAddress == null) {
//                OTHERTOOLS.shortShow(this, "Please set BleAddress first");
//                return;
//            }
////            if (mBLEService != null) {
////                LogUtil.E(TAG, "ble has connectted!");
////                return;
////            }
//            LogUtil.E(TAG, "ready bindServices");
//            Intent gattServiceIntent = new Intent(this,BleBroadService.class);
//            bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
////            mContext.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
//        } else {
//
//        }
//    }


//    public Messenger getMainActivityMessenger() {
//        return mMessenger;
//    }
//    @Override
//    protected void onNewIntent(Intent intent) {
//        LogUtil.E(TAG,OTHERTOOLS.getCurrentMethod());
//        super.onNewIntent(intent);
//    }


//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        LogUtil.E(TAG,OTHERTOOLS.getCurrentMethod());
//        super.onWindowFocusChanged(hasFocus);
//    }

    private boolean readBlueToothNews() {
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
        boolean bleFlag;
        SharedPreferences pref = getSharedPreferences("bluetoothdata", MODE_PRIVATE);
        devName = pref.getString("name", null);
        devAddress = pref.getString("address", null);
        bleFlag = pref.getBoolean("ble", false);
        LogUtil.E(TAG, "name = " + devName + "address = " + devAddress);
        if (devName == null) return false;
        if (devAddress == null) return false;
        if (bleFlag != false) return false;
        return true;
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
            case SOMESTRING.BLE_DATA_SEND:
//                Bundle bundle = msg.getData();
//                byte[] data = bundle.getByteArray(SOMESTRING.BUNDLE_NEWS);
                byte[] data = (byte[]) msg.obj;
                final StringBuilder stringBuilder = new StringBuilder(msg.arg1);
                for (byte byteChar : data) {
                    stringBuilder.append(String.format("%02X ", byteChar));
                }
                LogUtil.E(TAG, "data = " + stringBuilder);
                if (sbDisplay.length() > 1024) {
                    sbDisplay.delete(0, sbDisplay.length());
                }
                sbDisplay.append(stringBuilder).append("\r\n");
                displayNews.setText(sbDisplay);
                break;
            case SOMESTRING.BLE_STATE_SEND:
                LogUtil.E(TAG, "state = " + msg.arg1);
                break;
            default:
                break;
        }
    }

    private Main3Activity.MyHandler mHandler = new Main3Activity.MyHandler(this);
//    private Messenger mMessenger = new Messenger(mHandler);

    private static class MyHandler extends Handler {
        private final WeakReference<Main3Activity> main3ActivityWeakReference;

        public MyHandler(Main3Activity activity) {
            main3ActivityWeakReference = new WeakReference<Main3Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            Main3Activity activity = main3ActivityWeakReference.get();
            if (activity == null) {
                return;
            }
//            super.handleMessage(msg);
            activity.handleMsg(msg);
        }
    }


//    private ServiceConnection mServiceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
//            bleMessengerProx = new Messenger(service);
//            Message msg = Message.obtain(null, SOMESTRING.BLE_REPLO_MESSENGER);
//            msg.replyTo = mMessenger;
//            Bundle bundle = new Bundle();
//            bundle.putString(SOMESTRING.EXTRAS_DEVICE_NAME, devName);
//            bundle.putString(SOMESTRING.EXTRAS_DEVICE_ADDRESS, devAddress);
//            msg.setData(bundle);
//            try {
//                bleMessengerProx.send(msg);
//
//                scheduledExecutorService.scheduleAtFixedRate(new MyRun(), 0, 4, TimeUnit.SECONDS);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
//            sendMessengerToService(SOMESTRING.BLE_DISCONNECT, 0, 0, null);
//            bleMessengerProx = null;
//            if (!scheduledExecutorService.isShutdown()) {
//                scheduledExecutorService.shutdown();
//            }
//        }
//    };
//
//    public void startService() {
//        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
//
//        if (SOMESTRING.BLE) {
//            Intent gattServiceIntent = new Intent(this, BLEService.class);
//            bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
//        } else {
//
//        }
//    }
//
//    public void stopService() {
//        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
//        if (SOMESTRING.BLE) {
////            Intent gattServiceIntent = new Intent(mContext, BluetoothLeService.class);
////            bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
//            unbindService(mServiceConnection);
//        } else {
//
//        }
//    }
//
//    private void sendMessengerToService(int what, int arg1, int arg2, Bundle bundle) {
//        if (bleMessengerProx == null) {
//            return;
//        }
//        Message msg = Message.obtain();
//        msg.what = what;
//        msg.arg1 = arg1;
//        msg.arg2 = arg2;
////            bundle.putByteArray(SOMESTRING.BUNDLE_NEWS, data);
//        if (bundle != null) {
//            msg.setData(bundle);
//        }
//
//
//        try {
//            bleMessengerProx.send(msg);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//    }
//

    class MyRun implements Runnable {

        @Override
        public void run() {
            LogUtil.E(TAG, "runCount = " + runCount);
            switch (runCount) {
                case PenNews.ANDROD_REDA_PENID:
//                    askBleID();
//                    mainProx.askBle(PenNews.ANDROD_REDA_PENID, null);
                    SingleServceProx.getInstance().getServNews(PenNews.ANDROD_REDA_PENID);
                    break;
                case PenNews.ANDROD_WRITE_PENID:
//                    mainProx.askBle(PenNews.ANDROD_WRITE_PENID, new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a});
//                    mainProx.upgradeID("123456789101221");
//                    mainProx.upgradeID("ABcdedg");
//                    mainProx.upgradeID("778899");
                    SingleServceProx.getInstance().writeServId("789987");
                    break;
                case PenNews.ANDROD_READ_VERSION:
//                    mainProx.askBle(PenNews.ANDROD_READ_VERSION, null);
                    SingleServceProx.getInstance().getServNews(PenNews.ANDROD_READ_VERSION);
                    break;
                case PenNews.ANDROD_READ_PEN_BATTERY:
//                    mainProx.askBle(PenNews.ANDROD_READ_PEN_BATTERY, null);
                    SingleServceProx.getInstance().getServNews(PenNews.ANDROD_READ_PEN_BATTERY);
                    break;
                case PenNews.ANDROD_READ_PEN_STORAGE:
//                    mainProx.askBle(PenNews.ANDROD_READ_PEN_STORAGE, null);
                    SingleServceProx.getInstance().getServNews(PenNews.ANDROD_READ_PEN_STORAGE);
                    break;
                case PenNews.ANDROD_DELETE_PEN_STORAGE:
//                    mainProx.askBle(PenNews.ANDROD_DELETE_PEN_STORAGE, null);
                    //runCount = PenNews.ANDROD_SUM_PEN_STORAGE;
                    SingleServceProx.getInstance().getServNews(PenNews.ANDROD_DELETE_PEN_STORAGE);
                    break;
                case PenNews.ANDROD_SUM_PEN_STORAGE:
//                    mainProx.askBle(PenNews.ANDROD_SUM_PEN_STORAGE, null);
                    SingleServceProx.getInstance().getServNews(PenNews.ANDROD_SUM_PEN_STORAGE);
//                    runCount = PenNews.ANDROD_READ_NEED_NEWS;
                    break;
                case PenNews.ANDROD_READ_NEED_NEWS:
//                    mainProx.askBle(PenNews.ANDROD_READ_NEED_NEWS, new byte[]{0x00, 0x00, 0x00, (byte) 0x96, 0x00, 0x6});
//                    mainProx.askBle(PenNews.ANDROD_READ_NEED_NEWS, new byte[]{0x00, 0x00, 0x00, (byte) 0x96, 0x00, 0x6});
//                    mainProx.askSomeData(0x00000096, 0x06);
                    SingleServceProx.getInstance().getServSomeData(0x00000096,0x06);
                    //runCount = PenNews.ANDROD_DELETE_PEN_STORAGE;
                    break;
                default:
                    if (runCount > 9) {
                        runCount = 1;
                    }
                    break;
            }
//            mainProx.askBleState();
            SingleServceProx.getInstance().askServState();
            runCount++;
//            runCount = 3;
//            runCount = 9;
        }
    }
//
//
//    private void askBle(int command,byte[] byteIS) {
//        if (bleSendNewsToServer == null) {
//            bleSendNewsToServer = new BleSendNewsToServer();
//        }
//        int len = 0;
//        switch (command) {
//            case PenNews.ANDROD_REDA_PENID:
////                askBleID();
//                len = bleSendNewsToServer.readIdCmd();
//                break;
//            case PenNews.ANDROD_WRITE_PENID:
////                byte[] idByte = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a};
//                len = bleSendNewsToServer.writeIdCmd(byteIS);
//                break;
//            case PenNews.ANDROD_READ_VERSION:
////                askBleVersion();
//                len = bleSendNewsToServer.readVersion();
//                break;
//            case PenNews.ANDROD_READ_PEN_BATTERY:
//                len = bleSendNewsToServer.readPenBattary();
//                break;
//            case PenNews.ANDROD_READ_PEN_STORAGE:
//                len = bleSendNewsToServer.readPenStorage();
//                break;
//            case PenNews.ANDROD_DELETE_PEN_STORAGE:
////                comBleDelNews();
//                len = bleSendNewsToServer.deletePenAllNews();
//                break;
//            case PenNews.ANDROD_SUM_PEN_STORAGE:
////                askBleNewsNum();
//                len = bleSendNewsToServer.readPenRecordNum();
//                break;
//            case PenNews.ANDROD_READ_NEED_NEWS:
//                len = bleSendNewsToServer.readPenSomeRecord(byteIS);
//                LogUtil.E(TAG,"ANDROD_READ_NEED_NEWS = " + len);
//
////                askBleSomeNews(0, 2);
//                break;
//            default:
//                break;
//        }
//        if(len == 0)return;
////        int len = bleSendNewsToServer.readPenBattary();
//        byte[] data = new byte[len];
////        LogUtil.E(TAG, "cmd = " + Arrays.toString(bleSendNewsToServer.cmdByteArray));
//        System.arraycopy(bleSendNewsToServer.cmdByteArray, 0, data, 0, len);
////        LogUtil.E(TAG, "data = " + Arrays.toString(data));
////        DataUtil.printByte(data,data.length);
//
//        Bundle bundle = new Bundle();
//        bundle.putByteArray(SOMESTRING.EXTRAS_NEWS,data);
//        bundle.putAll(bundle);
//        sendMessengerToService(SOMESTRING.BLE_DATA_RECV, 0, 0, bundle);
//    }
//    private void askBleID() {
//
//
//        sendMessengerToService(SOMESTRING.BLE_DATA_RECV, PenNews.ANDROD_REDA_PENID, 0, null);
//    }
//
//    private void writeBleID(byte[] data) {
//        Bundle bundle = new Bundle();
//        sendMessengerToService(SOMESTRING.BLE_DATA_RECV, PenNews.ANDROD_WRITE_PENID, 0, bundle);
//    }
//
//    private void askBleVersion() {
//        sendMessengerToService(SOMESTRING.BLE_DATA_RECV, PenNews.ANDROD_READ_VERSION, 0, null);
//    }
//
//    private void askBleBattery() {
//        if (bleSendNewsToServer == null) {
//            bleSendNewsToServer = new BleSendNewsToServer();
//        }
//        int len = bleSendNewsToServer.readPenBattary();
//        byte[] data = new byte[len];
//        System.arraycopy(bleSendNewsToServer.cmdByteArray, 0, data, 0, len);
//        Bundle bundle = new Bundle();
//        bundle.putByteArray(SOMESTRING.EXTRAS_NEWS,data);
//        bundle.putAll(bundle);
//        sendMessengerToService(SOMESTRING.BLE_DATA_RECV, 0, 0, bundle);
//    }
//
//
//    private void askBleStroge() {
//
//        sendMessengerToService(SOMESTRING.BLE_DATA_RECV, PenNews.ANDROD_READ_PEN_STORAGE, 0, null);
//    }
//
//    private void comBleDelNews() {
//        sendMessengerToService(SOMESTRING.BLE_DATA_RECV, PenNews.ANDROD_DELETE_PEN_STORAGE, 0, null);
//    }
//
//    private void askBleNewsNum() {
//        sendMessengerToService(SOMESTRING.BLE_DATA_RECV, PenNews.ANDROD_SUM_PEN_STORAGE, 0, null);
//    }
//
//    private void askBleSomeNews(int off, int len) {
//        byte[] data = new byte[6];
//        Bundle bundle = new Bundle();
//        sendMessengerToService(SOMESTRING.BLE_DATA_RECV, PenNews.ANDROD_READ_NEED_NEWS, 0, bundle);
//    }
}
