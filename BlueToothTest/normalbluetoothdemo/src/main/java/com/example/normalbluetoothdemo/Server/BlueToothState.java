package com.example.normalbluetoothdemo.Server;

/**
 * Created  :   GaryLiu
 * Email    :   gary@Test.com
 * Date     :   2018/4/18
 * Desc     :
 */

public class BlueToothState {
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device
    public static final int STATE_DISCONNECTED = 4; //now disconnnected



    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
}
