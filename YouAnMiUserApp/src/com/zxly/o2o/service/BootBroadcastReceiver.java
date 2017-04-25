package com.zxly.o2o.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dx.libagent2.Agent2Yam;

/**
 * Created by zl on 2017/4/7.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "BootBroadcastReceiver";

    private static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_BOOT)) {
          //  Logger.t(TAG).e("开机广播    " + "启用Agent2Yam服务");
            /**启用Agent2Yam服务*/
            context.startService(new Intent(context, Agent2Yam.class));
        }
    }
}
