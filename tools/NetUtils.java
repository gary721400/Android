package com.afaya.viewdemo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 2017/3/29.
 */

public class NetUtils {

    private NetUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null != connectivity) {

            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null)
            return false;
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

    }

    /**
     * 打开网络设置界面
     */
    public static void openSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings",
                "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }
    
    
    
     private String getInfo(Context context)
    {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String maxText = info.getMacAddress();
        String ipText = intToIp(info.getIpAddress());
        String status = "";
        if (wifi.getWifiState() == WifiManager.WIFI_STATE_ENABLED)
        {
            status = "WIFI_STATE_ENABLED";
        }
        String ssid = info.getSSID();
        int networkID = info.getNetworkId();
        int speed = info.getLinkSpeed();
        return "mac：" + maxText + "\n\r"
                + "ip：" + ipText + "\n\r"
                + "wifi status :" + status + "\n\r"
                + "ssid :" + ssid + "\n\r"
                + "net work id :" + networkID + "\n\r"
                + "connection speed:" + speed + "\n\r"
                ;
    }
    private String intToIp(int ip)
    {
        return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "."
                + ((ip >> 24) & 0xFF);
    }





    private String init(Context context) {
        StringBuffer sInfo = new StringBuffer();
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {

            NetworkInfo activeNetInfo = connectivity.getActiveNetworkInfo();
            NetworkInfo mobNetInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (activeNetInfo != null) {
                Log.d("", "XYZ_NetState_|TypeName:" + activeNetInfo.getTypeName() + "|Type:" + activeNetInfo.getType() + "|State:" + activeNetInfo.getState() + "|ExtraInfo:" + activeNetInfo.getExtraInfo()
                        + "|Reason:" + activeNetInfo.getReason() + "|SubtypeName:" + activeNetInfo.getSubtypeName() + "|Subtype:" + activeNetInfo.getSubtype() + "|DetailedState:" + activeNetInfo.getDetailedState());
                sInfo.append("\nDetailedState:" + activeNetInfo.getDetailedState());
                sInfo.append("\nState:" + activeNetInfo.getState());
                sInfo.append("\nType:" + activeNetInfo.getType());
                sInfo.append("\nTypeName:" + activeNetInfo.getTypeName());
                sInfo.append("\nExtraInfo:" + activeNetInfo.getExtraInfo());
                sInfo.append("\nReason:" + activeNetInfo.getReason());
                sInfo.append("\nSubtype:" + activeNetInfo.getSubtype());
                sInfo.append("\nSubtypeName:" + activeNetInfo.getSubtypeName());
                sInfo.append("\n\n\n\n");
            }
            if (mobNetInfo != null) {
                Log.d("", "XYZ_NetState_" + mobNetInfo.getTypeName() + ":" + mobNetInfo.getExtraInfo());
            }

            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        Log.d("", "XYZ_isconnect");

                    } else {
                        sInfo.append("\nDetailedState:" + info[i].getDetailedState());
                        sInfo.append("\nState:" + info[i].getState());
                        sInfo.append("\nType:" + info[i].getType());
                        sInfo.append("\nTypeName:" + info[i].getTypeName());
                        sInfo.append("\nExtraInfo:" + info[i].getExtraInfo());
                        sInfo.append("\nReason:" + info[i].getReason());
                        sInfo.append("\nSubtype:" + info[i].getSubtype());
                        sInfo.append("\nSubtypeName:" + info[i].getSubtypeName());
                        sInfo.append("\n");
                    }

                }
            }
        }

        return sInfo.toString();
    }

}
