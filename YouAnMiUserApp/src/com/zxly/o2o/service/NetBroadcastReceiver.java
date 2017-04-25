package com.zxly.o2o.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.dx.libagent2.Agent2Yam;

/**
 * Created by zl on 2017/4/7.
 */

public class NetBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "NetBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkInfo.State wifiState = null;
        NetworkInfo.State mobileState = null;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if (wifiState != null && mobileState != null
                && NetworkInfo.State.CONNECTED != wifiState
                && NetworkInfo.State.CONNECTED == mobileState) {
            // 手机网络连接成功
            /**启用Agent2Yam服务*/
            context.startService(new Intent(context, Agent2Yam.class));
            //Logger.t(TAG).e("手机网络连接成功   " + "启用Agent2Yam服务");

        } else if (wifiState != null && mobileState != null
                && NetworkInfo.State.CONNECTED != wifiState
                && NetworkInfo.State.CONNECTED != mobileState) {
            // 手机没有任何的网络
          //  Logger.t(TAG).e("手机没有任何的网络   " + "手机没有任何的网络");
        } else if (wifiState != null && NetworkInfo.State.CONNECTED == wifiState) {
            // 无线网络连接成功
            context.startService(new Intent(context, Agent2Yam.class));
         //   Logger.t(TAG).e("无线网络连接成功" + "启用Agent2Yam服务");

        }
    }
}
