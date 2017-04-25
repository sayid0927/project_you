
package com.zxly.o2o.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.NetWork;

/**
 * @author adama 描述：
 */
public class NetworkUtil {

    /**
     * 获取网络状态
     *
     * @param context
     * @return 1为wifi连接，2为2g网络，3为3g网络，-1为无网络连接
     */
    public static int getNetworkerStatus(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conMan.getActiveNetworkInfo();
        if (null != info && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                switch (info.getSubtype()) {
                    case 1:
                    case 2:
                    case 4:
                        AppLog.e("2G网络");
                        return 2;
                    default:
                        AppLog.e("3G及其以上网络");
                        return 3;
                }
            } else {
                AppLog.e("wifi网络");
                return 1;
            }
        } else {
            // 无网络
            return -1;
        }
    }

    /**
     * 判断当前网络是否是wifi网络
     */
    public static boolean isWifi(Context context) {
        return getNetworkerStatus(context) == 1;
    }

    /**
     * 判断当前网络是否是3G网络
     *
     * @param context
     * @return boolean
     */
    public static boolean is3G(Context context) {
        return getNetworkerStatus(context) == 3;
    }


    /**
     * 判断当前网络是否是手机网络
     *
     * @param context
     * @return boolean
     */
    public static boolean is2G(Context context) {
        int status = getNetworkerStatus(context);
        return (status == 2) || (status == 3);
    }

    /**
     * 判断网络连接状态
     *
     * @return 返回true为连接
     */
    public static boolean isNetworkerConnect(Context context) {
        return getNetworkerStatus(context) != -1;
    }

    public static WifiInfo getWifiInfo(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo;
    }






    /**
     * 在wifi网络下，判断是否为BleasantWifi网络
     */
    public static boolean isBleasantWifiNet(Context context) {
        WifiInfo wifiInfo = NetworkUtil.getWifiInfo(context);
        int ipAddress = wifiInfo.getIpAddress();
        String ssid = wifiInfo.getSSID();
        if(ssid.contains("Bleasant_"))
        {
            return true;
        }
        return false;

    }



    public static NetWork getMacAndIp() {
        NetWork netWork = new NetWork();
        WifiManager wifiMgr = (WifiManager) AppController.getInstance().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());


        if (null != info) {
            netWork.mac = info.getMacAddress();

            int ipInt = info.getIpAddress();
            StringBuilder sb = new StringBuilder();
            sb.append(ipInt & 0xFF).append(".");
            sb.append((ipInt >> 8) & 0xFF).append(".");
            sb.append((ipInt >> 16) & 0xFF).append(".");
            sb.append((ipInt >> 24) & 0xFF);

            netWork.ip = sb.toString();
        }
        System.out.println("mac:" + netWork.mac + ",ip:" + netWork.ip);
        return netWork;
    }


}
