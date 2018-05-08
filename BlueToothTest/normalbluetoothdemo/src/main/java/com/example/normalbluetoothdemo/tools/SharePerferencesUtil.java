package com.example.normalbluetoothdemo.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created  :   GaryLiu
 * Email    :   gary@Test.com
 * Date     :   2018/4/2
 * Desc     :   SharePerferencesUitl
 */

public class SharePerferencesUtil {

    public static boolean hasKey(Context context, final String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).contains(key);
    }

    public static void clean(Context context, final SharedPreferences p) {
            final SharedPreferences.Editor editor = p.edit();
            editor.clear();
            editor.apply();
    }
    public static String getString(Context context, String key,
                                   final String defaultVale) {

        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getString(key, defaultVale);
    }
    public static void setString(Context context, String key,
                                 final String value) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putString(key,value).apply();

    }


    public static boolean getBoolean(Context context, String key,
                                   final boolean defaultVale) {

        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getBoolean(key, defaultVale);
    }
    public static void setBoolean(Context context, String key,
                                 final boolean value) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putBoolean(key,value).apply();

    }


    public static int getInt(Context context, String key,
                                     final int defaultVale) {

        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getInt(key, defaultVale);
    }
    public static void setInt(Context context, String key,
                                  final int value) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putInt(key,value).apply();

    }

    public static long getLong(Context context, String key,
                                     final long defaultVale) {

        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getLong(key, defaultVale);
    }
    public static void setLong(Context context, String key,
                                  final long value) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putLong(key,value).apply();

    }

    public static float getFloat(Context context, String key,
                                     final float defaultVale) {

        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getFloat(key, defaultVale);
    }
    public static void setFloat(Context context, String key,
                                  final float value) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putFloat(key,value).apply();

    }



}
