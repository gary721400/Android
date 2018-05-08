package com.afaya.bletest;

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

    public static void V(String tag,String str){
        if(BuildConfig.RELEASE_DEBUG){
            return;
        }
        if(LEVEL <= VERBOSE){
            Log.v(tag,str);
        }
    }

    public static void D(String tag,String str){
        if(BuildConfig.RELEASE_DEBUG){
            return;
        }
        if(LEVEL <= DEBUG){
            Log.d(tag,str);
        }
    }

    public static void I(String tag,String str){
        if(BuildConfig.RELEASE_DEBUG){
            return;
        }
        if(LEVEL <= INFO){
            Log.i(tag,str);
        }
    }

    public static void W(String tag,String str){
        if(BuildConfig.RELEASE_DEBUG){
            return;
        }
        if(LEVEL <= WARNING){
            Log.w(tag,str);
        }
    }

    public static void E(String tag,String str){
        if(BuildConfig.RELEASE_DEBUG){
            return;
        }
        if(LEVEL <= ERROR){
            Log.e(tag,str);
        }
    }
}
