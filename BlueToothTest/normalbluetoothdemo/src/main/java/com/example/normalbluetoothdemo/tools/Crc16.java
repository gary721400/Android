package com.example.normalbluetoothdemo.tools;

import com.afaya.toolslib.LogUtil;

/**
 * Created by Administrator on 2016/8/29.
 */
public class Crc16 {
    private static final String TAG = "Crc16";
    public static short chechRecData(byte[] recData, int len) {
        short[] short_data = new short[len];
        for (int i = 0; i < len; i++) {
            short_data[i] = (short) (((short) recData[i]) & 0x00FF);
        }

        short shortData = mkCrc16(short_data, len);

        return shortData;
    }


    private static short mkCrc16(short[] data, int length) {
        int[] crc_ta = new int[]{
                0x0000, 0x1021, 0x2042, 0x3063, 0x4084, 0x50a5, 0x60c6, 0x70e7, 0x8108, 0x9129, 0xa14a, 0xb16b, 0xc18c, 0xd1ad, 0xe1ce, 0xf1ef,
        };

        int ptr_point = 0;
        int short_len = length;

        int CRC = 0;
        int temp_data;
        byte da;

        while (ptr_point < short_len) {
            da = (byte) ((CRC >>> 12) & 0x0F);
            CRC <<= 4;
            CRC ^= crc_ta[da ^ (data[ptr_point] / 16)];

            da = (byte) ((CRC >>> 12) & 0x0F);
            CRC <<= 4;
            CRC ^= crc_ta[da ^ ((byte) data[ptr_point] & 0x0F)];
            ptr_point++;
            CRC = CRC & 0x0000FFFF;
        }
        return (short) (CRC & 0x0000FFFF);
    }


    private short[] intToshort(int[] data, int length) {
        short[] shortData = new short[length];
        for (int i = 0; i < length; i++) {
            shortData[i] = (short) (((short) data[i]) & 0x00FF);
        }
        return shortData;
    }

    private short[] byteToshort(byte[] data, int length) {
        short[] shortData = new short[length];
        for (int i = 0; i < length; i++) {
            shortData[i] = (short) (((short) data[i]) & 0x00FF);
        }

        for (int j = 0; j < length; j++) {
            LogUtil.E(TAG,"short" + j + " = " + shortData[j]);
        }
        return shortData;
    }

    public static int toInt(byte[] bRefArr) {
        int iOutcome = 0;
        byte bLoop;

        for (int i = 0; i < bRefArr.length; i++) {
            bLoop = bRefArr[i];
            iOutcome += (bLoop & 0xFF) << (8 * i);
        }
        return iOutcome;
    }

}
