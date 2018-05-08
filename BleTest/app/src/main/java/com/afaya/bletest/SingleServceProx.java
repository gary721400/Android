package com.afaya.bletest;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by Administrator on 2017/11/13 0013.
 */


public class SingleServceProx {
    private static final String TAG = "SingleServceProx";
    //    private static Handler aHandler = null;
    private static Context sContext = null;
    private static MyHanlerThread myHanlerThread = null;
    private static Handler serHanler = null;

    private static String HEART_RATE_MEASUREMENT = "0783b03e-8535-b5a0-7140-a304d2495cb8";
    private static String WRITEDATA = "0783b03e-8535-b5a0-7140-a304d2495cba";
    // SBM 14580 UUID
    private final static UUID RECEIVE_UUID = UUID.fromString(HEART_RATE_MEASUREMENT);
    // SBM 14580 UUID
    private final static UUID SEND_UUID = UUID.fromString(WRITEDATA);

    private SingleServceProx() {
    }

    private static class SingleHolder {
        private static final SingleServceProx INSTANCE = new SingleServceProx();
    }

    public static final SingleServceProx getInstance() {
        return SingleHolder.INSTANCE;
    }

    public void bindHandler(Handler handler) {
//        aHandler = handler;
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
        if (myHanlerThread != null) {
            myHanlerThread.setMHandler(handler);
        }
    }

    public void unBindHandler() {
//        aHandler = null;
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
        if (myHanlerThread != null) {
            myHanlerThread.setMHandler(null);
        }
    }


    public void setContext(Context context) {
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
        if (context != null) {
            if (sContext == null) {
                LogUtil.E(TAG, "HandlerThread begin!");
                myHanlerThread = new MyHanlerThread("serverThread");

                myHanlerThread.start();
                myHanlerThread.setmHandlerThreadContext(context);
                myHanlerThread.myHandlerThreadInitAfter();
                serHanler.obtainMessage(HandleThreadEnum.HANDLERTHREAD_INIT).sendToTarget();
            }
        }
    }

    public void setDevAddress(String devAddress) {
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
//        devAddress = devAddress;

        if (devAddress != null) {
            myHanlerThread.setDevAddress(devAddress);
        }
    }


    public void askServState() {
        myHanlerThread.askBleState();
    }

    public void getServSomeData(long off, int len) {
        myHanlerThread.askSomeData(off, len);
    }

    public void writeServId(String str) {
        myHanlerThread.upgradeID(str);
    }

    public void getServNews(int id) {
        myHanlerThread.askBle(id, null);
    }

    private class MyHanlerThread extends HandlerThread {
        private Context mContext = null;

        private Handler mHandler = null;
        //    private String devName;
        private String sDevAddress;
        //    private ScheduledExecutorService scheduledExecutorService = null;
        private BleSendNewsToServer bleSendNewsToServer = null;
        private int runCount = 0;
        private BlockingDeque<byte[]> deque = null;
        //    private Timer sendTimer;
        private ScheduledExecutorService scheduledExecutorService = null;
        //    private BluetoothAdapter mBluetoothAdapter = null;


        private BleBroadService mBLEService = null;
        private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
        private boolean mConnected = false;
        private BluetoothGattCharacteristic mNotifyCharacteristic;
        private BluetoothGattCharacteristic mSendDataCharacteristic;
        private int mConnectionState = BluetoothProfile.STATE_DISCONNECTED;

        //    private LocalBroadcastManager localBroadcastManager = null;
//        private static SingleServceProx instance = null;

        private HandlerThread serverThread = null;
        private Handler serverHandler = null;
//        private String address;

        public MyHanlerThread(String name) {
            super(name);
        }

        private void myHandlerThreadInitAfter() {
            serHanler = new SingleHandler(this.getLooper());
//
//            serHanler = new Handler(this.getLooper()) {
//                @Override
//                public void handleMessage(Message msg) {
////                super.handleMessage(msg);
//                    handlerMsg(msg);
//                }
//            };
        }

        private void setmHandlerThreadContext(Context mContext) {
            this.mContext = mContext;
        }

        private void setMHandler(Handler mHandler) {
            this.mHandler = mHandler;
        }

        private void setDevAddress(String devAddress) {
//            this.de = devAddress;
            if (sDevAddress == null) {
                this.sDevAddress = devAddress;
                serHanler.obtainMessage(HandleThreadEnum.HANDLERTHREAD_SETADDRESS).sendToTarget();
            }else if(!devAddress.equalsIgnoreCase(sDevAddress)){
                this.sDevAddress = devAddress;
                serverHandler.obtainMessage(HandleThreadEnum.HANDLERTHREAD_RECONNECT).sendToTarget();
            }
        }


        private void initClass() {
            LogUtil.E(TAG, "SingleProx initClass");
            if (bleSendNewsToServer == null) {
                bleSendNewsToServer = new BleSendNewsToServer();
            }
            if (deque == null) {

                deque = new LinkedBlockingDeque<>();
            }
            if (scheduledExecutorService == null) {
                scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
                scheduledExecutorService.execute(new Runnable() {
                    @Override
                    public void run() {

                        while (true) {
                            byte[] data = getPackage();
                            if (mHandler != null) {
                                Message msg = Message.obtain();
                                msg.what = SOMESTRING.BLE_DATA_SEND;
                                msg.arg1 = data.length;
                                msg.obj = data;
                                mHandler.sendMessage(msg);
                            }
                        }
                    }
                });
            }
        }

        private void handlerMsg(Message msg) {
            LogUtil.E(TAG, "msg:what = " + msg.what);
            switch (msg.what) {
                case HandleThreadEnum.HANDLERTHREAD_INIT:
                    initClass();
                    break;

                case HandleThreadEnum.HANDLERTHREAD_SETADDRESS:
                    startService();
                    break;
                case HandleThreadEnum.HANDLERTHREAD_RECONNECT:
                    //reConnect();
                    stopService();
                    startService();
                    break;
                default:
                    try {
                        throw new Throwable("unknow msg!");
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                    break;
            }
        }

        private class SingleHandler extends Handler{

            public SingleHandler(Looper looper) {
                super(looper);
            }

            @Override
            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
                handlerMsg(msg);
            }
        }
        // Code to manage Service lifecycle.
        private final MyServiceConnection myServiceConnection = new MyServiceConnection();

        private class MyServiceConnection implements ServiceConnection {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                LogUtil.E(TAG, "onServiceConnected");
//            rMessenger = new Messenger(service);
                mBLEService = ((BleBroadService.LocalBinder) service).getService();
                if (!mBLEService.initialize()) {
                    LogUtil.E(TAG, "Unable to initialize Bluetooth");
//                finish();
                }
                // Automatically connects to the device upon successful start-up initialization.
                mConnected = mBLEService.connect(sDevAddress);
                if (mConnected) {
                    LogUtil.E(TAG, "Ble connected OK!");

                } else {
                    LogUtil.E(TAG, "ble connected fail!");
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                LogUtil.E(TAG, "onServiceDisconnected: ");
                mBLEService = null;
                mConnected = false;
            }
        }

        private class MyBroadcastReceiver extends BroadcastReceiver {
            @Override
            public void onReceive(Context context, Intent intent) {

                final String action = intent.getAction();
                if (SOMESTRING.ACTION_BLE_STATE.equals(action)) {
                    mConnectionState = intent.getIntExtra(SOMESTRING.ACTION_BLE_STATE, BluetoothProfile.STATE_DISCONNECTED);
                    LogUtil.E(TAG, "mConnectionState = " + mConnectionState);
                    if (mConnectionState == BluetoothProfile.STATE_DISCONNECTED) {
                        mConnected = false;
                        LogUtil.E(TAG, "GATT_DISCONNECTED");
                        reConnect();
                    } else if (mConnectionState == BluetoothProfile.STATE_CONNECTING) {

                    } else if (mConnectionState == BluetoothProfile.STATE_CONNECTED) {
                        LogUtil.E(TAG, "GATT_CONNECTED");
                        displayGattServices(mBLEService.getSupportedGattServices());
                        mConnected = true;
                        askBleState();
                    } else if (mConnectionState == BluetoothProfile.STATE_DISCONNECTING) {
                        LogUtil.E(TAG, "STATE_DISCONNECTING");
                    }
                } else if (SOMESTRING.ACTION_BLE_DATA.equals(action)) {
                    byte[] tempData = intent.getByteArrayExtra(SOMESTRING.EXTRA_DATA);
                    int tempLen = tempData.length;
                    final StringBuilder stringBuilder = new StringBuilder(tempData.length);
                    for (byte byteChar : tempData) {
                        stringBuilder.append(String.format("%02X ", byteChar));
                    }
                    LogUtil.E(TAG, "recv = " + stringBuilder);
                    addPackage(tempLen, tempData);
//                Bundle bundle = new Bundle();
//                bundle.putByteArray(SOMESTRING.BUNDLE_NEWS, tempData);
//                Message msg = Message.obtain();
//                msg.what = SOMESTRING.BLE_DATA_SEND;
////                msg.setData(bundle);
//                msg.arg1 = tempLen;
//                msg.obj = tempData;
//                msg.sendToTarget();
                }
            }
        }

        // Handles various events fired by the Service.
        // ACTION_GATT_CONNECTED: connected to a GATT server.
        // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
        // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
        // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
        //                        or notification operations.
        private final BroadcastReceiver mGattUpdateReceiver = new MyBroadcastReceiver();

        private void startService() {
            LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());

            if (SOMESTRING.BLE) {
                if (sDevAddress == null) {
//                OTHERTOOLS.shortShow(mContext, "Please set BleAddress first");
                    LogUtil.E(TAG, "Please set BleAddress first");
                    return;
                }
                if (mBLEService != null) {
                    LogUtil.E(TAG, "ble has connectted!");
                    return;
                }
                LogUtil.E(TAG, "ready bindServices");
                IntentFilter intentFilter = new IntentFilter();

                intentFilter.addAction(SOMESTRING.ACTION_BLE_DATA);
                intentFilter.addAction(SOMESTRING.ACTION_BLE_STATE);

                Intent gattServiceIntent = new Intent(mContext, BleBroadService.class);
                mContext.bindService(gattServiceIntent, myServiceConnection, Context.BIND_AUTO_CREATE);
//            mContext.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
                LocalBroadcastManager.getInstance(mContext).registerReceiver(mGattUpdateReceiver, intentFilter);
            } else {

            }
        }

        private void stopService() {
            LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
            if (SOMESTRING.BLE) {
//            Intent gattServiceIntent = new Intent(mContext, BluetoothLeService.class);
//            bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
                if (mBLEService != null) {
                    LogUtil.E(TAG, "unbindService");
//                mContext.unregisterReceiver(mGattUpdateReceiver);
                    LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mGattUpdateReceiver);
                    mContext.unbindService(myServiceConnection);
                }
            } else {

            }
        }

        private void reConnect() {
            // Automatically connects to the device upon successful start-up initialization.
            mConnected = mBLEService.connect(sDevAddress);
            if (mConnected) {
                LogUtil.E(TAG, "Ble connected OK!");

            } else {
                LogUtil.E(TAG, "ble connected fail!");
            }
        }

        private void uninit() {
            if (bleSendNewsToServer != null) {
                bleSendNewsToServer = null;
            }
            if (deque != null) {
                deque = null;
            }
        }

        private void displayGattServices(List<BluetoothGattService> gattServices) {
            if (gattServices == null) {
                return;
            }

            // Loops through available GATT Services.
            for (BluetoothGattService gattService : gattServices) {

                LogUtil.E(TAG, "service_uuid : " + gattService.getUuid().toString());

                List<BluetoothGattCharacteristic> characteristics = gattService.getCharacteristics();
                for (BluetoothGattCharacteristic characteristic : characteristics) {

                    //      		Log.d("service_uuid", "service_uuid"+characteristic.getUuid().toString());


                    //      	 boolean result_notify = RECEIVE_UUID.equals(Receive_Chara.getUuid());
                    if (characteristic.getUuid().equals(RECEIVE_UUID)) {
                        if ((characteristic.getProperties() | (BluetoothGattCharacteristic.PROPERTY_NOTIFY)) > 0) {
                            mNotifyCharacteristic = characteristic;
                            mBLEService.setCharacteristicNotification(characteristic, true);
                        }
                    }

                    //       boolean result_notify = RECEIVE_UUID.equals(Receive_Chara.getUuid());
                    if (characteristic.getUuid().equals(SEND_UUID)) {
                        if ((characteristic.getProperties() | (BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE)) > 0) {
                            characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
                            mSendDataCharacteristic = characteristic;
                        }
                    }

                }

            }

        }


//    private void regeisterSer() {
//        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
//            mContext.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
//            if (mBLEService != null) {
//                if (devAddress != null) {
//                final boolean result = mBLEService.connect(devAddress);
//                LogUtil.E(TAG, "Connect request result=" + result);
//                }
//            }
//    }
//
//    private void unregeisterSer() {
//        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
//        mContext.unregisterReceiver(mGattUpdateReceiver);
//    }

//        private void IntentFilter makeGattUpdateIntentFilter() {
//            final IntentFilter intentFilter = new IntentFilter();
//            intentFilter.addAction(SOMESTRING.ACTION_BLE_DATA);
//            intentFilter.addAction(SOMESTRING.ACTION_BLE_STATE);
//            return intentFilter;
//        }


        private void askBleState() {
            if (bleSendNewsToServer == null) {
                bleSendNewsToServer = new BleSendNewsToServer();
            }
            if (mBLEService == null) {
                return;
            }
            LogUtil.E(TAG, "askState = " + mConnectionState);
            if (mHandler != null) {
                mHandler.obtainMessage(SOMESTRING.BLE_STATE_SEND, mConnectionState, 0).sendToTarget();
            }
        }

        private void askBle(int command, byte[] byteIS) {
            if (bleSendNewsToServer == null) {
                bleSendNewsToServer = new BleSendNewsToServer();
            }
            int len = 0;
            switch (command) {
                case PenNews.ANDROD_REDA_PENID:
//                askBleID();
                    len = bleSendNewsToServer.readIdCmd();
                    break;
//            case PenNews.ANDROD_WRITE_PENID:
////                byte[] idByte = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a};
//                len = bleSendNewsToServer.writeIdCmd(byteIS);
//                break;
                case PenNews.ANDROD_READ_VERSION:
//                askBleVersion();
                    len = bleSendNewsToServer.readVersion();
                    break;
                case PenNews.ANDROD_READ_PEN_BATTERY:
                    len = bleSendNewsToServer.readPenBattary();
                    break;
                case PenNews.ANDROD_READ_PEN_STORAGE:
                    len = bleSendNewsToServer.readPenStorage();
                    break;
                case PenNews.ANDROD_DELETE_PEN_STORAGE:
//                comBleDelNews();
                    len = bleSendNewsToServer.deletePenAllNews();
                    break;
                case PenNews.ANDROD_SUM_PEN_STORAGE:
//                askBleNewsNum();
                    len = bleSendNewsToServer.readPenRecordNum();
                    break;
//            case PenNews.ANDROD_READ_NEED_NEWS:
//                len = bleSendNewsToServer.readPenSomeRecord(byteIS);
//                LogUtil.E(TAG, "ANDROD_READ_NEED_NEWS = " + len);
//
////                askBleSomeNews(0, 2);
//                break;
                default:
                    break;
            }
            if (len == 0) {
                return;
            }
//        int len = bleSendNewsToServer.readPenBattary();
            byte[] data = new byte[len];
//        LogUtil.E(TAG, "cmd = " + Arrays.toString(bleSendNewsToServer.cmdByteArray));
            System.arraycopy(bleSendNewsToServer.cmdByteArray, 0, data, 0, len);
            LogUtil.E(TAG, "data = " + Arrays.toString(data));
//        DataUtil.printByte(data,data.length);

//        Bundle bundle = new Bundle();
//        bundle.putByteArray(SOMESTRING.EXTRAS_NEWS, data);
//        bundle.putAll(bundle);
//        sendMessengerToService(SOMESTRING.BLE_DATA_RECV, 0, 0, bundle);
//
            if (mBLEService == null) {
                return;
            }
//        Message msg = Message.obtain();
//
//        msg.what = what;
//        msg.arg1 = arg1;
//        msg.arg2 = arg2;
////            bundle.putByteArray(SOMESTRING.BUNDLE_NEWS, data);
//        if (bundle != null) {
//            msg.setData(bundle);
//        }
            boolean sendflag = mBLEService.writeData(mSendDataCharacteristic, data);
            LogUtil.E(TAG, "sendFlag = " + sendflag);
        }

        private void upgradeID(String strID) {
            if (bleSendNewsToServer == null) {
                bleSendNewsToServer = new BleSendNewsToServer();
            }
            LogUtil.E(TAG, "upgradeID = " + strID);
//        int len = 0;
//        String tempStr = Arrays.toString((new byte[10]));
//
//        if (tempStr.length() > strID.length()) {
//            tempStr = strID.substring(0, strID.length());
//
//        } else {
//            tempStr = strID.substring(0, tempStr.length());
//        }
//
//        LogUtil.E(TAG, "tempStr = " + tempStr);
            LogUtil.E(TAG, "tempLen = " + strID.length());
            int len = bleSendNewsToServer.writeIdCmd(strID);

            if (len == 0) {
                return;
            }
//        int len = bleSendNewsToServer.readPenBattary();
            byte[] data = new byte[len];
//        LogUtil.E(TAG, "cmd = " + Arrays.toString(bleSendNewsToServer.cmdByteArray));
            System.arraycopy(bleSendNewsToServer.cmdByteArray, 0, data, 0, len);
            LogUtil.E(TAG, "data = " + Arrays.toString(data));
//        DataUtil.printByte(data,data.length);
            if (mBLEService == null) {
                return;
            }
            boolean sendflag = mBLEService.writeData(mSendDataCharacteristic, data);
            LogUtil.E(TAG, "sendFlag = " + sendflag);

        }


        private void askSomeData(long offBegin, int len) {
            if (bleSendNewsToServer == null) {
                bleSendNewsToServer = new BleSendNewsToServer();
            }
            LogUtil.E(TAG, "ANDROD_READ_NEED_NEWS = " + offBegin + " : " + len);
            if (offBegin >= 0x0100000000L) {
                LogUtil.E(TAG, "offBegin too big!");
                return;
            }
            len = bleSendNewsToServer.readPenSomeRecord(offBegin, len);
            if (len == 0) return;
//        int len = bleSendNewsToServer.readPenBattary();
            byte[] data = new byte[len];
//        LogUtil.E(TAG, "cmd = " + Arrays.toString(bleSendNewsToServer.cmdByteArray));
            System.arraycopy(bleSendNewsToServer.cmdByteArray, 0, data, 0, len);
            LogUtil.E(TAG, "data = " + Arrays.toString(data));
//        DataUtil.printByte(data,data.length);

//        Bundle bundle = new Bundle();
//        bundle.putByteArray(SOMESTRING.EXTRAS_NEWS, data);
//        bundle.putAll(bundle);
//        sendMessengerToService(SOMESTRING.BLE_DATA_RECV, 0, 0, bundle);
//
            if (mBLEService == null) {
                return;
            }
//        Message msg = Message.obtain();
//
//        msg.what = what;
//        msg.arg1 = arg1;
//        msg.arg2 = arg2;
////            bundle.putByteArray(SOMESTRING.BUNDLE_NEWS, data);
//        if (bundle != null) {
//            msg.setData(bundle);
//        }
            boolean sendflag = mBLEService.writeData(mSendDataCharacteristic, data);
            LogUtil.E(TAG, "sendFlag = " + sendflag);
        }

        private void addPackage(int len, byte[] buf) {
//        String str = DataUtil.bytesToHexString(buf);
            LogUtil.E(TAG, "addLen = " + len);
//        DataUtil.printByte(buf,len);

            byte[] data = new byte[len];
            System.arraycopy(buf, 0, data, 0, len);


            boolean b = false;
            try {
                deque.put(data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }

        private byte[] getPackage() {
            byte[] data = null;
            try {
                data = deque.take();
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
            return data;
        }


    }
}
