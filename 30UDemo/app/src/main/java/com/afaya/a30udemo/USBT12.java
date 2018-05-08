package com.afaya.a30udemo;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/3/21.
 */

public class USBT12 implements AfayaLog {
    private static final String TAG = "USBT12";


    final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    UsbManager manager = null;
    HashMap<String, UsbDevice> deviceList = null;
    UsbInterface mInterface = null;
    boolean hasDevice = false;
    UsbDevice findDevice = null;
    Iterator<UsbDevice> deviceIterator = null;
    public boolean readIndex = false;
    private Context context;
    private Handler mHandler;

    private Timer findTimer = null;
    private Timer readTimer = null;

    public USBT12(Context context, Handler mHandler) {
        this.context = context;
        this.mHandler = mHandler;
    }

    public void stopAllTimer(){
        stopReadTimer();
        stopFindTimer();
    }
    public void startFindTimer() {
        if (findTimer == null) {
            findTimer = new Timer();
            findTimer.schedule(new FindTimerTask(),0,SOMETHING.SCAN_USB_TIMER);
        }
    }

    private void startReadTimer() {
        if (readTimer == null) {
            readTimer = new Timer();
            readTimer.schedule(new ReadTimerTask(), 0, SOMETHING.READ_USB_TIMER);
        }
    }

    private void stopFindTimer() {
        if (findTimer != null) {
            findTimer.cancel();
            findTimer = null;
        }
    }

    private void stopReadTimer() {
        if (readTimer != null) {
            readTimer.cancel();
            readTimer = null;
        }
    }

    private void sendMessage(int what ,int arg1 ,int arg2){
        Message msg = mHandler.obtainMessage();
        msg.what = what;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        msg.sendToTarget();
    }

    class ReadTimerTask extends TimerTask {
        @Override
        public void run() {
            UsbDeviceConnection connection = manager.openDevice(findDevice);
//            myLog("connection = " + connection);
            if (connection == null) {
                return;
            }
            connection.claimInterface(mInterface, true);
            byte[] inputbuf = new byte[64];
            byte bindcmd[] = {(byte) 0xFE, (byte) 0xCA, (byte) 0xCA, (byte) 0xFE, 0x03, 0x01};
//            byte READ_COMD[] = {(byte) 0xFE, (byte) 0xCA, (byte) 0xCA, (byte) 0xFE, 0x10, 0x19};
            byte READ_COMD[] = {(byte) 0xEF, (byte) 0xBC, (byte) 0xCB, (byte) 0xEF, 0x10, 0x19};
            byte BIND_COMD[] = {(byte) 0xEF, (byte) 0xBC, (byte) 0xCB, (byte) 0xEF, 0x09, 0x19};
//            myLog("start read");
            int wLen = 0;
            int rLen = 0;
            if (MainActivity.BindSendFlag) {
                wLen = connection.controlTransfer(0x21, 9, 200, 0, BIND_COMD, BIND_COMD.length, 200);
                MainActivity.BindSendFlag = false;
            } else {
                wLen = connection.controlTransfer(0x21, 9, 200, 0, READ_COMD, READ_COMD.length, 200);
            }
            if (wLen < 0) return;
//                myLog("send b= " + b);
//                int b = connection.controlTransfer(0x21, 9, 200, 0, inputbuf, inputbuf.length, 200);
            rLen = connection.controlTransfer(0xa1, 1, 256, 0, inputbuf, inputbuf.length, 200);
            if (rLen < 0) return;
//            Message msg = mHandler.obtainMessage();
//            msg.what = SOMETHING.RECEVIE_INDEX;
//            msg.arg1 = SOMETHING.NO_INDEX;
            if (inputbuf[24] != 0) {
                int index = (0xFF & inputbuf[20]) << 24 | (0xFF & inputbuf[21]) << 16 | (0xFF & inputbuf[22]) << 8 | (0xFF & inputbuf[23]);
//                msg.arg1 = SOMETHING.NEW_INDEX;
//                msg.arg2 = index;
                sendMessage(SOMETHING.RECEVIE_INDEX,index,0);
            }
//            msg.sendToTarget();
        }
    }


    class FindTimerTask extends TimerTask {

        @Override
        public void run() {
//            Message msg = mHandler.obtainMessage();
//            msg.what = SOMETHING.NOUSBDEVICE;
//            hasDevice = false;
            UsbDevice device = null;
            myLog("get usb service");
            manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
            deviceList = manager.getDeviceList();
            deviceIterator = deviceList.values().iterator();
            myLog("device count = " + deviceList.size());
            if(deviceList.size() == 0){
                findDevice = null;
                stopReadTimer();
//                    msg.sendToTarget();
                sendMessage(SOMETHING.DISCONNECTED,0,0);
                return;
            }
            while (deviceIterator.hasNext()) {
                device = deviceIterator.next();
                myLog("device = " + device);
                myLog("device vid = " + device.getVendorId());
                myLog("device pid = " + device.getProductId());
                myLog("device interfaceCount = " + device.getInterfaceCount());
                if (device.getVendorId() != SOMETHING.T12VID && device.getProductId() != SOMETHING.T12PID)continue;
                for (int j = 0; j < device.getInterfaceCount(); j++) {
                    mInterface = device.getInterface(j);
                    if (mInterface.getInterfaceClass() == 3
                            && mInterface.getInterfaceSubclass() == 0
                            && (mInterface.getInterfaceProtocol() == 0
                            || mInterface.getInterfaceProtocol() == 1)
                            ) {
                        myLog("find devices");
                        hasDevice = true;
                        findDevice = device;
                        if (!manager.hasPermission(device)) {
                            myLog("noPermission");
                            PendingIntent mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
                            IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
                            context.registerReceiver(mUsbReceiver, filter);
                            manager.requestPermission(device, mPermissionIntent);
                            return;
//                            msg.what = SOMETHING.WAIT_USBPER;
//                            sendMessage(SOMETHING.WAIT_USBPER,0,0);
                        } else {
//                            msg.what = SOMETHING.CONNECTED;
//                            readThread.start();
                            startReadTimer();
                            sendMessage(SOMETHING.CONNECTED,0,0);
                            return;
                        }
                    }
                }
            }
            findDevice = null;
            stopReadTimer();
            sendMessage(SOMETHING.NOUSBDEVICE,0,0);
        }
    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
//            Message msg = mHandler.obtainMessage();
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        //readThread.start();
//                        readIndex = true;
//                        msg.what = SOMETHING.CONNECTED;
                        startReadTimer();
                        sendMessage(SOMETHING.CONNECTED,0,0);
                    } else {
                        //context.finish();
//                        msg.what = SOMETHING.NO_USBPER;
                        sendMessage(SOMETHING.NO_USBPER,0,0);
                    }
//                    msg.sendToTarget();
                }
            }
        }
    };


    @Override
    public void myLog(String str) {
        if (AfayaLog.D) {
            Log.d(TAG, str);
        }

    }
}
