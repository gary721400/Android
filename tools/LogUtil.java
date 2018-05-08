package com.afaya.afayabluetooth.FileUtil;

import android.util.Log;

/**
 * Created by Administrator on 2017/4/6.
 */

public final  class LogUtil {

    private final static int VERBOSE = 0;
    private final static int DEBUG = 1;
    private final static int INFO = 2;
    private final static int WARNING = 3;
    private final static int ERROR = 4;
    private final static int NOTHING = 5;

    private final static int LEVEL = ERROR;


//    private enum LEVEL{
//        VERBOSE(0),DEBUG(1),INFO(2),WARNING(3),ERROR(4),NOTHTING(5);
//
//        private int num = 0;
//
//        LEVEL(int num) {
//            this.num = num;
//        }
////
////        public int getNum(){
////            return num;
////        }
//
//    }

    public static void V(String tag,String str){
        if(LEVEL <= VERBOSE){
            Log.v(tag,str);
        }
    }

    public static void D(String tag,String str){
        if(LEVEL <= DEBUG){
            Log.d(tag,str);
        }
    }

    public static void I(String tag,String str){
        if(LEVEL <= INFO){
            Log.i(tag,str);
        }
    }

    public static void W(String tag,String str){
        if(LEVEL <= WARNING){
            Log.w(tag,str);
        }
    }

    public static void E(String tag,String str){
        if(LEVEL <= ERROR){
            Log.e(tag,str);
        }
    }
}
