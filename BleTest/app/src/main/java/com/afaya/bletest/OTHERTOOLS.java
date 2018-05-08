package com.afaya.bletest;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/3/29.
 */

public class OTHERTOOLS {

    public static final boolean isShow = true;


    public static String getCurrentMethod() {
        return new Throwable().getStackTrace()[1].getMethodName();
    }

    public static void shortShow(Context context, String msg) {
        if (isShow) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public static void longShow(Context context, String msg) {
        if (isShow) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
    }


}
