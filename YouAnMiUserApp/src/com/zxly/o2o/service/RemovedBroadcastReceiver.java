package com.zxly.o2o.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dx.libagent2.Agent2Yam;

/**
 * Created by zl on 2017/4/7.
 */

public class RemovedBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "BootBroadcastReceiver";

    private static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";


    @Override
    public void onReceive(Context context, Intent intent) {

        //接收广播：设备上删除了一个应用程序包。
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
            /**启用Agent2Yam服务*/
            context.startService(new Intent(context, Agent2Yam.class));
        }
    }
}
