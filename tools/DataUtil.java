package com.afaya.afayabluetooth.FileUtil;

/**
 * Created by Administrator on 2017/4/13.
 */

public class DataUtil {
    private static final String TAG = "DataUtil";
    private DataUtil() {
        throw new UnsupportedOperationException("DataUtis can not instructor！");
    }


    public static byte[] combine(byte[] first, byte[] second)
    {
        byte[] ret = new byte[first.length + second.length];
        System.arraycopy(first,0,ret,0,first.length);
        System.arraycopy(second,0,ret,first.length,second.length);
        return ret;
    }

    public static int mkCrc16(byte[] buf, int beginOff,int length) {
//        DataUtil.printByte(buf,length);
        short[] data = new short[length];
//        short_data = byteToshort(recData,recData.length);

        for (int i = 0; i < length; i++) {
            data[i] = (short) (((short) buf[beginOff + i]) & 0x00FF);
        }
        int[] crc_ta = new int[]{
                0x0000, 0x1021, 0x2042, 0x3063, 0x4084, 0x50a5, 0x60c6, 0x70e7, 0x8108, 0x9129, 0xa14a, 0xb16b, 0xc18c, 0xd1ad, 0xe1ce, 0xf1ef,
        };

        int ptr_point = 0;
        int short_len = length;

        int CRC = 0;
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
        return  (CRC & 0x0000FFFF);
    }

    public static void printByte(byte[] data,int len){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0;i < len; i++){
            stringBuilder.append(String.format("%02X ", data[i]));
        }
        LogUtil.E(TAG,"data = " + stringBuilder);
    }


    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        int len = src.length;
        if (src == null || len <= 0) {
            return null;
        }
//        char[] buffer = new char[2];
//        for (int i = 0; i < src.length; i++) {
//            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
//            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
//            System.out.println(buffer);
//            stringBuilder.append(buffer);
//        }
        for(int i = 0; i < len; i++){
            stringBuilder.append(String.format("%02X", src[i]));
        }
        return stringBuilder.toString();
    }

    public static String bytesLenToHexString(byte[] src,int off,int len) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || len <= 0) {
            return null;
        }
        int range = off + len;
        for(int i = off; i < range; i++){
            stringBuilder.append(String.format("%02X", src[i]));
        }
//        char[] buffer = new char[2];
//        for (int i = off; i < range; i++) {
//            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
//            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
//            System.out.println(buffer);
//            stringBuilder.append(buffer);
//        }
        return stringBuilder.toString();
    }
    public static byte[] hexString2Bytes(String s) {
        byte[] bytes;
        bytes = new byte[s.length() / 2];


        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2),
                    16);
        }


        return bytes;
    }

    public static void printOffByte(byte[] data,int off,int len){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0;i < len; i++){
            stringBuilder.append(String.format("%02X ", data[i+off]));
        }
        LogUtil.E(TAG,"data = " + stringBuilder);
    }
    public static int twobyteFrom(byte[] data,int off){
        int Mask = (((0xFF & data[off]) << 8) | (0xFF & data[ off+1 ]));
        return Mask;
    }

    public static int fourbyteFrom(byte[] data,int off){
        int Mask = (((0xFF & data[off]) << 24) | ((0xFF & data[off+1]) << 16) | ((0xFF & data[off+2]) << 8) | (0xFF & data[ off+3 ]));
        return Mask;
    }

    public static long longFrom(byte[] data,int off){
        int m1 = (((0xFF & data[off]) << 24) | ((0xFF & data[off+1]) << 16) | ((0xFF & data[off+2]) << 8) | (0xFF & data[ off+3 ]));
        int m2 = (((0xFF & data[off+4]) << 24) | ((0xFF & data[off+5]) << 16) | ((0xFF & data[off+6]) << 8) | (0xFF & data[ off+7 ]));
        return (((0xFFFF & m1) << 8) | (0xFF & m2));
    }
}
