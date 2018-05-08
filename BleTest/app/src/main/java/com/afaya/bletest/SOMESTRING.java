package com.afaya.bletest;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/7/21.
 */
public class SOMESTRING {

    public static final boolean isEventBuf = true;

    public static int CUSTUM_CODE = 1;
    public static int SCAN_TIMER = 30;


    public static final int BLE_REPLO_MESSENGER = 1;
    public static final int BLE_INIT_FAIL = 2;

    public static final int BLE_DATA_SEND  = 3;
    public static final int BLE_STATE_SEND= 4;
    public static final int BLE_DATA_RECV = 5;
    public static final int BLE_DISCONNECT = 6;
    public static final int BLE_LINK_STATE = 7;


    public static final int BLE_PACKAGENES  = 127;


    public static int MAX_POINTNEWS_ARRAY   = 1024;
    public static int MAX_PACKAGES_BUF   = 512*20;
//    public static int REPLO_MESSENGER = 1;
    public static final boolean BLE = true;
    public static final String BUNDLE_NEWS = "BUNDLE_NEWS";



    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static final String EXTRAS_NEWS = "EXTRAS_NEWS";

//    public final static String ACTION_GATT_CONNECTED = "com.afaya.bluetoothle.ACTION_GATT_CONNECTED";
//    public final static String ACTION_GATT_DISCONNECTED = "com.afaya.bluetoothle.ACTION_GATT_DISCONNECTED";
//    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.afaya.bluetoothle.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_BLE_DATA = "com.afaya.ble.data";
    public final static String EXTRA_DATA = "com.afaya.bluetoothle.EXTRA_DATA";
    public final static String ACTION_BLE_STATE = "com.afaya.ble.state";


    private static HashMap<String, String> attributes = new HashMap();
    //    public static String HEART_RATE_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb";
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";


    public static String SERVICE_UUID = "0783b03e-8535-b5a0-7140-a304d2495cb7";
    /**
     * SBM1458 硬件信息
     */
//
//    public static String HEART_RATE_MEASUREMENT = "0783b03e-8535-b5a0-7140-a304d2495cb8";
//    public static String WRITEDATA = "0783b03e-8535-b5a0-7140-a304d2495cba";
//
//    static {
//        // Sample Services.
//        attributes.put("0000180d-0000-1000-8000-00805f9b34fb", "Heart Rate Service");
//        attributes.put("0000180a-0000-1000-8000-00805f9b34fb", "Device Information Service");
//        // Sample Characteristics.
//        attributes.put(HEART_RATE_MEASUREMENT, "Heart Rate Measurement");
//        attributes.put("00002a29-0000-1000-8000-00805f9b34fb", "Manufacturer Name String");
//    }

//    public static String lookup(String uuid, String defaultName) {
//        String name = attributes.get(uuid);
//        return name == null ? defaultName : name;
//    }

}
