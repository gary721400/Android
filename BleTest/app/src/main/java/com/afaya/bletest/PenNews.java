package com.afaya.bletest;

/**
 * Created by Administrator on 2017/11/9 0009.
 */

public class PenNews {


    /**
     * 笔发送给apk当前写的轨迹
     */
    public static final int PEN_SEND_DATANEWS = 0x0001;

    /**
     * 读取当前笔的ID
     */
    public static final int ANDROD_REDA_PENID = 0x0002;

    /**
     * 写当前笔的ID
     */
    public static final int ANDROD_WRITE_PENID = 0x0003;


    /**
     * 读取当前笔的固件，硬件版本信息
     */
    public static final int ANDROD_READ_VERSION = 0x0004;

    /**
     * 读取当前笔的电量
     */
    public static final int ANDROD_READ_PEN_BATTERY = 0x0005;

    /**
     * 读取当前笔的存储容量
     */
    public static final int ANDROD_READ_PEN_STORAGE = 0x0006;


    /**
     * 删除当前笔的所有轨迹记录
     */
    public static final int ANDROD_DELETE_PEN_STORAGE = 0x0007;

    /**
     * 读取笔当前记录的总轨迹数量
     */
    public static final int ANDROD_SUM_PEN_STORAGE = 0x0008;

    /**
     * 读取笔对应轨迹的数据
     */
    public static final int ANDROD_READ_NEED_NEWS = 0x0009;
}
