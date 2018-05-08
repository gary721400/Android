package com.example.normalbluetoothdemo.Server;

/**Hanlder Messenger what 类型说明
 * Created  :   GaryLiu
 * Email    :   gary@Test.com
 * Date     :   2018/4/18
 * Desc     :
 */

public class MessageNews {
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    public static final int MESSAGE_WRITE_ACK = 6;


    public static final int MESSAGE_WRITE_ACK_OK = 0x55;
    public static final int MESSAGE_WRITE_ACK_KO = 0xAA;




}
