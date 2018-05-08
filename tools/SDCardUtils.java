package com.afaya.afayabluetooth.FileUtil;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created by Administrator on 2017/3/29.
 */

public class SDCardUtils {

    private SDCardUtils() {
        throw new UnsupportedOperationException("SDCardUtils cannot be instantiated");
    }


    /**
     * 检查SD卡是否存在
     * @return
     */
    public static boolean isSDCardEnable(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡路径
     * @return
     */
    public static String getSDCardPath(){
        return Environment.getDownloadCacheDirectory().getAbsolutePath() + File.separator;
    }

    /**
     * 获取SD卡的剩余流量
     * @return
     */
    public static long getSDCardAllSize(){
        if(isSDCardEnable()){
            StatFs statFs = new StatFs(getSDCardPath());
//            long availableBlocks = statFs.getAvailableBytes();
        }
        return 0;
    }

    /**
     * 获取系统存储路径
     * @return
     */
    public static String getRootDirectoryPath(){
        return Environment.getRootDirectory().getAbsolutePath();
    }

}
