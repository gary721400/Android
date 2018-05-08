package com.afaya.bletest;

/**
 * Created by Administrator on 2016/8/29.
 */
public class BleSendNewsToServer {

    private static final String TAG = "BleSendNewsToServer";
    public static final int ZERO_LENTH = 0;
    public static final int FOUR_LENTH = 4;
    public static final int SIX_LENTH = 6;
    public static final int TEN_LENTH = 10;
    public static final int FOURRH_LENTH = 14;


    public static final int CMDINTARRAY_LENTH = 16;
    public static final int CMDBYTEARRAY_LENTH = 32;
    public static final int RECBYTEARRAY_LENTH = 128;



    public int[] cmdIntArray = null;
    public byte[] cmdByteArray = null;


    /**
     * 读取，发送信息的帧头，帧尾
     */
    public static final int FREAME_BEGIN = 0xFFFE;
    public static final int FREAME_TAIL = 0xFFFD;


    public BleSendNewsToServer() {
        if (cmdIntArray == null) {
            cmdIntArray = new int[CMDINTARRAY_LENTH];
            cmdByteArray = new byte[CMDBYTEARRAY_LENTH];
        }
    }

    public boolean chechRecData(byte[] recData, int len) {

//        myLog("len = " + len);
        int recCmd = 0;
        int recLen = 0;
        short[] short_data = new short[len];
//        short_data = byteToshort(recData,recData.length);

        for (int i = 0; i < len; i++) {
            short_data[i] = (short) (((short) recData[i]) & 0x00FF);
        }
//
//        for(int j = 0;j < len;j++) {
//            myLog("short" + j + " = " + String.format("%02X",short_data[j]));
//        }


        if (short_data[0] != 0x00FF) return false;
        if (short_data[1] != 0x00FE) return false;
        if (short_data[(len - 2)] != 0x00FF) return false;
        if (short_data[(len - 1)] != 0x00FD) return false;

        recCmd = (short_data[2]<<8)|short_data[3];
        recLen = (short_data[4]<<8)|short_data[5];
//        for(int j = 0;j < len;j++) {
//            myLog("byte" + j + " = " + recData[j]);
//        }

        short[] temp_data = new short[(len - 6)];


        for (int j = 2; j < len - 4; j++) {
            temp_data[j - 2] = short_data[j];
        }

//        for (int j = 0; j < recLen; j++) {
//            recByteData[j] = (byte)temp_data[j+4];
//        }

        int temp_i = ((int) short_data[(len - 4)]) << 8;
        temp_i |= ((int) short_data[(len - 3)]);

//        myLog("temp_i = " + String.format("%04X",temp_i));

        short shortData = mkCrc16(temp_data, (len - 6));
//        myLog("shortData = " + String.format("%4X",shortData));
        if ((short) temp_i != shortData) return false;
        return true;
    }


    public int readySendData(int lenth) {

        int temp_lenth = lenth + 6;
        short[] shortArray = new short[temp_lenth];
        short[] tempArray = new short[lenth];
        StringBuilder stringBuilder = new StringBuilder();
//        byte[] byteArray = new byte[temp_lenth];
        cmdByteArray = new byte[temp_lenth];

        shortArray[0] = (short) ((FREAME_BEGIN >> 8) & 0x00FF);
        shortArray[1] = (short) (FREAME_BEGIN & 0x00FF);
        int j = 2;
        for (int i = 0; i < lenth; i++) {
//            short s = (short) (data[i] & 0x0000FFFF);
//            shortArray[j++] = (short) ((s >> 8) & 0x00FF);
//            shortArray[j++] = (short) ((s) & 0x00FF);
//            myLog("int" + i + " = " + String.format("%04X",data[i]));
            tempArray[i] = (short) (cmdIntArray[i] & 0x0000FFFF);
            shortArray[j++] = tempArray[i];
        }


        short crc = mkCrc16(tempArray, lenth);
//        myLog("crc16 = " + String.format("%04X",crc));
        shortArray[j++] = (short) ((crc >> 8) & 0x00FF);
        shortArray[j++] = (short) ((crc) & 0x00FF);

        shortArray[j++] = (short) ((FREAME_TAIL >> 8) & 0x00FF);
        shortArray[j++] = (short) (FREAME_TAIL & 0x00FF);


        for (int i = 0; i < temp_lenth; i++) {
//            myLog("short" + i + " = " + String.format("%02X",shortArray[i]));
            cmdByteArray[i] = (byte) shortArray[i];
            stringBuilder.append(String.format(" %02X", cmdByteArray[i]));
        }

//        LogUtil.E(TAG,"send bytes = " + stringBuilder.toString());
        return temp_lenth;
    }


    private short mkCrc16(short[] data, int length) {
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

    public int readPenRecordNum() {
//        int[] data = new int[2];
        cmdIntArray[0] = (int) ((PenNews.ANDROD_SUM_PEN_STORAGE >> 8) & 0x00FF);
        cmdIntArray[1] = (int) ((PenNews.ANDROD_SUM_PEN_STORAGE) & 0x00FF);
        cmdIntArray[2] = ZERO_LENTH;
        cmdIntArray[3] = ZERO_LENTH;
//        return FOUR_LENTH;
        return readySendData(FOUR_LENTH);
    }

    public int readPenSomeRecord(long addOff,int len) {
//        int[] data = new int[2];

        cmdIntArray[0] = (int) ((PenNews.ANDROD_READ_NEED_NEWS >> 8) & 0x00FF);
        cmdIntArray[1] = (int) ((PenNews.ANDROD_READ_NEED_NEWS) & 0x00FF);
        cmdIntArray[2] =  ZERO_LENTH;
        cmdIntArray[3] =  SIX_LENTH;
//        cmdIntArray[4] = (int) (rec[0]);
//        cmdIntArray[5] = (int) (rec[1]);
//        cmdIntArray[6] = (int) (rec[2]);
//        cmdIntArray[7] = (int) (rec[3]);
//        cmdIntArray[8] = (int) (rec[4]);
//        cmdIntArray[9] = (int) (rec[5]);
        cmdIntArray[4] = (int) ((addOff >> 24) & 0x00FF);
        cmdIntArray[5] = (int) ((addOff >> 16) & 0x00FF);
        cmdIntArray[6] = (int) ((addOff >> 8) & 0x00FF);
        cmdIntArray[7] = (int) ((addOff) & 0x00FF);
        cmdIntArray[8] = (int) ((len >> 8) & 0x00FF);
        cmdIntArray[9] = (int) ((len) & 0x00FF);
//        return TEN_LENTH;
        return readySendData(TEN_LENTH);
    }

    public int deletePenAllNews() {
//        int[] data = new int[2];

        cmdIntArray[0] = (int) ((PenNews.ANDROD_DELETE_PEN_STORAGE >> 8) & 0x00FF);
        cmdIntArray[1] = (int) ((PenNews.ANDROD_DELETE_PEN_STORAGE) & 0x00FF);
        cmdIntArray[2] = ZERO_LENTH;
        cmdIntArray[3] = ZERO_LENTH;

//        return FOUR_LENTH;
        return readySendData(FOUR_LENTH);
    }

    public int readPenStorage() {
        cmdIntArray[0] = (int) ((PenNews.ANDROD_READ_PEN_STORAGE >> 8) & 0x00FF);
        cmdIntArray[1] = (int) ((PenNews.ANDROD_READ_PEN_STORAGE) & 0x00FF);
        cmdIntArray[2] = ZERO_LENTH;
        cmdIntArray[3] = ZERO_LENTH;
//        return FOUR_LENTH;
        return readySendData(FOUR_LENTH);
    }

    public  int readPenBattary() {
//        int[] data = new int[20];
        cmdIntArray[0] = (int) ((PenNews.ANDROD_READ_PEN_BATTERY >> 8) & 0x00FF);
        cmdIntArray[1] = (int) ((PenNews.ANDROD_READ_PEN_BATTERY) & 0x00FF);
        cmdIntArray[2] = ZERO_LENTH;
        cmdIntArray[3] = ZERO_LENTH;

        return readySendData(FOUR_LENTH);
//        return FOUR_LENTH;
    }

    public int readVersion() {
//        int[] data = new int[2];
        cmdIntArray[0] = (PenNews.ANDROD_READ_VERSION >> 8) & 0x00FF;
        cmdIntArray[1] = (PenNews.ANDROD_READ_VERSION) & 0x00FF;
        cmdIntArray[2] = ZERO_LENTH;
        cmdIntArray[3] = ZERO_LENTH;
//        return FOUR_LENTH;
        return readySendData(FOUR_LENTH);
    }

    public int writeIdCmd(String str) {

        cmdIntArray[0] = (PenNews.ANDROD_WRITE_PENID >> 8) & 0x00FF;
        cmdIntArray[1] = (PenNews.ANDROD_WRITE_PENID) & 0x00FF;
        cmdIntArray[2] = ZERO_LENTH;
        cmdIntArray[3] = TEN_LENTH;

        byte[] IDNum = str.getBytes();


        for (int i = 0; i < TEN_LENTH; i++) {
            cmdIntArray[i + 4] = 0;
        }

        int j = TEN_LENTH;
        if(str.length() < TEN_LENTH){
            j = str.length();
        }
        for (int i = 0; i < j; i++) {
            cmdIntArray[i + 4] = IDNum[i];
        }
//        return FOURRH_LENTH;
        return readySendData(FOURRH_LENTH);
    }

    public int readIdCmd() {
//        int[] data = new int[2];
        cmdIntArray[0] = (int) ((PenNews.ANDROD_REDA_PENID >> 8) & 0x00FF);
        cmdIntArray[1] = (int) ((PenNews.ANDROD_REDA_PENID) & 0x00FF);
        cmdIntArray[2] = ZERO_LENTH;
        cmdIntArray[3] = ZERO_LENTH;
//        return FOUR_LENTH;
        return readySendData(FOUR_LENTH);
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

}
