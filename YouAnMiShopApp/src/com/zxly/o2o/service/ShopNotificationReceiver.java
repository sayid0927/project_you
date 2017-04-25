package com.zxly.o2o.service;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zxly.o2o.activity.GetuiShopH5DetailAct;
import com.zxly.o2o.activity.MainActivity;
import com.zxly.o2o.util.AppUtil;

/**
 * Created by Administrator on 2016/7/19.
 */
public class ShopNotificationReceiver extends BroadcastReceiver{
    private final static String EXTRA_BUNDLE="extra_bundle";
    private boolean isFromNotice;
    private boolean goto_h5;
    private String sys_msg_url;
    private String title;
    private long id;
    private Intent detailIntent;
    private int busId;
    private long notifyId;
    private NotificationManager notificationManager;
    private int what;

    @Override
    public void onReceive(Context context, Intent intent) {
        what =intent.getIntExtra("what",0);
        goto_h5 = intent.getBooleanExtra("goto_h5", false);
        sys_msg_url = intent.getStringExtra("sys_msg_url");
        title = intent.getStringExtra("title");
        id = intent.getIntExtra("id", 0);
        busId = intent.getIntExtra("produceId", 0);
        notifyId = intent.getLongExtra("notifyId", 0);
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel((int) notifyId);
        //开启服务  通知服务有点击事件
        Intent intentService = new Intent(context, ShopNoticeMsgClickService.class);
        intentService.putExtra("dataId",notifyId);
        context.startService(intentService);
        //判断app进程是否存活
        if(AppUtil.isAppAlive(context, "com.zxly.o2o.shop")){
            //如果是要进入h5页面则put一下数据
            detailIntent = new Intent(context, GetuiShopH5DetailAct.class);
            if(goto_h5){
                detailIntent.putExtra("sys_msg_url", sys_msg_url);
                detailIntent.putExtra("id",id);
                detailIntent.putExtra("title", title);
                detailIntent.putExtra("what",what);
            }
            detailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(detailIntent);
        }else {
            Intent launchIntent = context.getPackageManager().
                    getLaunchIntentForPackage("com.zxly.o2o.shop");
            launchIntent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            Bundle args = new Bundle();
            args.putBoolean("isFromNotice", isFromNotice);
            args.putBoolean("goto_h5", goto_h5);
            if(goto_h5){
                args.putString("sys_msg_url", sys_msg_url);
                args.putLong("id",id);
                args.putString("title", title);
                args.putInt("what",what);
            }
            args.putLong("notifyId",notifyId);
            launchIntent.putExtra(EXTRA_BUNDLE, args);
            context.startActivity(launchIntent);
        }
    }
}
