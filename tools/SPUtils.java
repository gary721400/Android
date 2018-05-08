package com.afaya.viewdemo;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/29.
 */

public class SPUtils {
    /**
     * 保存在手机中的文件名
     */
    public static final String FILE_NAME = "share_data";


    private SPUtils() {
        throw new UnsupportedOperationException("SPUtils cannot be instantiated");
    }

    /**
     * 返回所有的键值对
     * @param context
     * @return
     */
    public static Map<String,?> getAll(Context context){
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 查询某个值是否存在
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context,String key){
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 清除所有数据
     * @param context
     */
    public static void clear(Context context){
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
//        editor.apply();
        SharePreferencesCompat.apply(editor);
    }


    /**
     * 移除某个key值对应的值
     * @param context
     * @param key
     */
    public static void remove(Context context,String key){
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
//        editor.apply();
        SharePreferencesCompat.apply(editor);
    }


    /**
     * 得到保存数据
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */

    public static Object get(Context context,String key, Object defaultObject){
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,context.MODE_PRIVATE);

        if( defaultObject instanceof String){
            return sp.getString(key, (String) defaultObject);
        }else if(defaultObject instanceof Integer){
            return sp.getInt(key, (Integer) defaultObject);
        }else if(defaultObject instanceof Boolean){
            return sp.getBoolean(key, (Boolean) defaultObject);
        }else if(defaultObject instanceof Float){
            return sp.getFloat(key, (Float) defaultObject);
        }else if(defaultObject instanceof Long){
            return sp.getLong(key, (Long) defaultObject);
        }
        return null;
    }




    /**
     * 拿到不同类型的数据，保存数据到文件
     *
     * @param context
     * @param key
     * @param obj
     */
    public static void put(Context context, String key, Object obj) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (obj instanceof String) {
            editor.putString(key, (String) obj);
        } else if (obj instanceof Integer) {
            editor.putInt(key, (Integer) obj);
        } else if (obj instanceof Boolean) {
            editor.putBoolean(key, (Boolean) obj);
        } else if (obj instanceof Float) {
            editor.putFloat(key, (Float) obj);
        } else if (obj instanceof Long) {
            editor.putLong(key, (Long) obj);
        } else {
            editor.putString(key, obj.toString());
        }
//        editor.apply();
        SharePreferencesCompat.apply(editor);
    }

    private static class SharePreferencesCompat{

        private static final Method sApplyMethod = findApplyMethod();

        private static Method findApplyMethod(){
            Class clz = SharedPreferences.Editor.class;
            try {
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }


        public static void apply(SharedPreferences.Editor editor){
            if(sApplyMethod != null){
                try {
                    sApplyMethod.invoke(editor);
                    return;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

            editor.commit();
        }

    }


}
