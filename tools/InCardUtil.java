package com.afaya.afayabluetooth.FileUtil;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;


/**
 * Created by Administrator on 2016/8/8.
 */
public class InCardUtil {
    private static final String TAG = "InCardUtil";
//    private static String packageName = "com.afaya.afayabluetooth";


    private InCardUtil() {
        throw new UnsupportedOperationException("InCardUtil can not be instructor!");
    }

    public static String getCurPackageName(Context context) {
//       return context.getClass().getPackage().getName();
        return context.getClass().getSimpleName();
    }


    public static String getInRootDir(Context context) {
        String str1 = Environment.getExternalStorageDirectory().getAbsolutePath();
        LogUtil.E(TAG,"str1 = " + str1);
        String str2 = Environment.getDataDirectory().getAbsolutePath();
        LogUtil.E(TAG, "str2 = " + str2);
//        String str = Environment.getExternalStoragePublicDirectory(Environment.MEDIA_SHARED).getAbsolutePath();
//        LogUtil.E(TAG, "insdShared = " + str);
        String str = Environment.getExternalStoragePublicDirectory(Environment.MEDIA_SHARED).getAbsolutePath();

        LogUtil.E(TAG, "insdShared = " + str);

        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append(str).append(File.separator).append("Android").append(File.separator).append("data").append(File.separator).append(getCurPackageName(context)).append(File.separator).append("file");
//        stringBuilder.append(str).append(File.separator).append("Android").append(File.separator).append("data").append(File.separator).append(getCurPackageName(context)).append(File.separator).append("file");
//        stringBuilder.append(str).append(File.separator).append("Android").append(File.separator).append("data").append(File.separator).append(getCurPackageName(context));
        stringBuilder.append(str1).append(File.separator).append(getCurPackageName(context));
        LogUtil.E(TAG, "recDir = " + stringBuilder);
        File dir = new File(stringBuilder.toString());
        if (!dir.exists()) {
            LogUtil.E(TAG, "create new dir");
            if (!dir.mkdirs()) return null;
        }
        LogUtil.E(TAG, "create dir ok!");
        return stringBuilder.toString();
    }

    public static Boolean createMyDir(Context context,String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            LogUtil.E(TAG, "create new dir");
            if (!dir.mkdirs()) return false;
        }
        LogUtil.E(TAG, "create dir ok!");
        return true;
    }

    public static Boolean createMyFile(Context context,String path) {
        File file = new File(path);
        if (!file.exists()) {
            LogUtil.E(TAG, "create new file");
            if (!file.mkdirs()) return false;
        }
        LogUtil.E(TAG, "create file ok!");
        return true;
    }
    public static void writeData(String filePath, byte[] data) {
        LogUtil.E(TAG, "filePath = " + filePath);
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    LogUtil.E(TAG, "Create file fail!");
                    return;
                }
//                FileOutputStream fileOutputStream = new FileOutputStream(file);
//                fileOutputStream.write(data, 0, data.length);
//                fileOutputStream.flush();
//                fileOutputStream.close();
                PrintStream printStream = new PrintStream(file);
                printStream.write(data,0,data.length);
                printStream.flush();
                printStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void appendData(String fileName,byte[] buf,int len) {
        if(len <= 0)return;
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(fileName,"rw");
            long length = randomAccessFile.length();
            randomAccessFile.seek(length);
            randomAccessFile.write(buf,0,len);
            randomAccessFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
