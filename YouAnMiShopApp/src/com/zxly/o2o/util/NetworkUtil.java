/**
 * Copyright(C)2012-2013 深圳市掌星立意科技有限公司版权所有<br>
 * 创 建 人:adama<br>
 * 修 改 人:
 * 创 建日期:2013-8-1<br>
 * 描	   述:
 * 版 本 号:
 */
package com.zxly.o2o.util;

import java.io.File;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.zxly.o2o.application.AppController;
import com.zxly.o2o.model.NetWork;

/**
 * @author adama 描述：
 */
public class NetworkUtil {
    private final static String IP_18_IN = "192.168.2";
    private final static String WIFI_18Free = "18FreeWifi";
    private final static String WIFI_188Free = "8FreeWifi_";
    private static boolean mIsHasWifiSN = false;

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
     * 进入网络设置
     */
    @SuppressWarnings("deprecation")
    public static void netWorkSetting(Context context) {
        String sdkVersion = android.os.Build.VERSION.SDK;
        Intent intent = null;
        if (Integer.valueOf(sdkVersion) > 10) {
            intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
        } else {
            intent = new Intent();
            ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings");
            intent.setComponent(comp);
            intent.setAction("android.intent.action.VIEW");
        }
        context.startActivity(intent);

    }

    public static boolean hasNetWork(Context context) {
        boolean netSataus = false;
        ConnectivityManager cwjManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        cwjManager.getActiveNetworkInfo();
        if (cwjManager.getActiveNetworkInfo() != null) {
            netSataus = cwjManager.getActiveNetworkInfo().isAvailable();
        }
        return netSataus;
    }

    //	/** 弹出无网络对话框 */
//	public static void showNoNetWorkDialog(final Activity activity) {
//		Dialog dialog = null;
//		CustomDialog.Builder customBuilder = new CustomDialog.Builder(activity);
//		customBuilder.setTitle("网络提示").setMessage("您的手机暂未联网，会影响您的正常浏览")
//				.setPositiveButton("继续", new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//					}
//				}).setNegativeButton("设置", new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int which) {
//						netWorkSetting(activity);
//						dialog.dismiss();
//					}
//				});
//		dialog = customBuilder.create();
//		dialog.setCancelable(false);
//		dialog.show();
//
//	}
    public static String get18WifiMac(Context context) {

        String mac = PreferUtil.getInstance().getString("wifi_mac", null);
        if (!TextUtils.isEmpty(mac)) {
            AppLog.e("return mac from prefer:" + mac);
            return mac;
        }

        File f = new File(FileUtils.getCacheDownloadDir() + "/mac.dat");
        if (f.exists()) {
            mac = FileUtils.restoreCache(f);
            if (!TextUtils.isEmpty(mac)) {
                AppLog.e("return mac from sd:" + mac);
                return mac;
            }
        }
        if (mac != null) {
            if (NetworkUtil.isWifi(context) || NetworkUtil.is18WifiNet(context)) {
                return mac;
            }
        }

        mac = getWifiInfo(context).getSSID();
        mac = mac.substring(mac.lastIndexOf("_") + 1, mac.length());
        //if(mac.length()!=5){
        //	mac=mac.substring(mac.lastIndexOf("_")+1,mac.length()-1);
        //}
        if (null == mac && "".equals(mac)) {
            mac = PreferUtil.getInstance().getString("wifi_mac", null);
        }
        if (null != mac && !"".equals(mac)) {
            if (mac.endsWith("\"")) {
                mac = mac.replace("\"", "");
            }
            PreferUtil.getInstance().putString("wifi_mac", mac);

            File file = new File(FileUtils.getCacheDownloadDir());
            if (!file.exists()) {
                file.mkdirs();
            }
            FileUtils.saveCache(new File(file.getAbsoluteFile() + "/mac.dat"), mac);
        }
        AppLog.e("return mac from wifi:" + mac);
        return mac;

    }

    /**
     * 在wifi网络下，判断是否为18wifi网络
     */
    public static boolean is18WifiNet(Context context) {
        WifiInfo wifiInfo = NetworkUtil.getWifiInfo(context);
        int ipAddress = wifiInfo.getIpAddress();
        String ssid = wifiInfo.getSSID();
        String ipString = "";// 本机在WIFI状态下路由分配给的IP地址
        // 获得IP地址：

        if (TextUtils.isEmpty(ssid)) {
            return false;
        }

        if (ipAddress != 0) {
            ipString = ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."
                    + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));
        }
        boolean isWifiIP = false;
        if (!TextUtils.isEmpty(ipString)) {
            ipString = ipString.substring(0, ipString.lastIndexOf("."));
            isWifiIP = IP_18_IN.equals(ipString);
            AppLog.e(ipString);
        }
        AppLog.e("ssid" + ssid);

/*		setSmbHeZi(false);
		if(ssid.contains("FreeWifi")){
			if(15<ssid.length()&&ssid.length()<18 && ssid.substring(ssid.lastIndexOf("_")+1).length()==6){
				setSmbHeZi(true);
			}
			if(ssid.length()>17 && ssid.length()<20&& ssid.substring(ssid.lastIndexOf("_")+1).length()==7){
				setSmbHeZi(true);
			}
		}*/

        boolean isWifiSSID = false;
        if (!TextUtils.isEmpty(ssid) && ssid.length() >= 10) {
            //ssid=ssid.substring(1, 11);
            ssid = ssid.substring(1, 5);
            AppLog.e(ssid);
            //if(WIFI_18.contains(ssid) || WIFI_188.contains(ssid)){
            //	isWifiSSID=true;
            //}
            if (WIFI_18Free.contains(ssid) || WIFI_188Free.contains(ssid)) {
                isWifiSSID = true;
            }
        }
        boolean is18WifiNet = isWifiIP && isWifiSSID;
        AppLog.e("ip " + ipString + ",ssid " + ssid + ",isWifiMode " + isWifiSSID);
        if (isWifiSSID) {
            setmIsHasWifiSN(true);
        }
        return isWifiSSID;

    }

    /**
     * @return the mIsHasWifiSN
     */
    public static boolean ismIsHasWifiSN() {
        return mIsHasWifiSN;
    }

    /**
     * @param mIsHasWifiSN the mIsHasWifiSN to set
     */
    public static void setmIsHasWifiSN(boolean mIsHasWifiSN) {
        NetworkUtil.mIsHasWifiSN = mIsHasWifiSN;
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
        return netWork;
    }


}
