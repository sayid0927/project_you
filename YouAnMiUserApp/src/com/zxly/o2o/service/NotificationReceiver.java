package com.zxly.o2o.service;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.easemob.chatuidemo.HXConstant;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.GetuiUserH5DetailAct;
import com.zxly.o2o.activity.HomeAct;
import com.zxly.o2o.activity.ProductInfoAct;
import com.zxly.o2o.activity.ShowDefineGetuiAct;
import com.zxly.o2o.util.Util;

/**
 * Created by Administrator on 2016/7/19.
 */
public class NotificationReceiver extends BroadcastReceiver{
    private final static String EXTRA_BUNDLE="extra_bundle";
    private boolean goto_h5;
    private String sys_msg_url;
    private String title;
    private long id;
    private Intent detailIntent;
    private long notifyId;
    private long produceId;
    private NotificationManager notificationManager ;
    private int what;
    private boolean goto_native;

    @Override
    public void onReceive(Context context, Intent intent) {
        goto_native = intent.getBooleanExtra("goto_native", false);
        goto_h5 = intent.getBooleanExtra("goto_h5", false);
            what = intent.getIntExtra("what",0);
            sys_msg_url = intent.getStringExtra("sys_msg_url");
            title = intent.getStringExtra("title");
            id =Long.parseLong(String.valueOf(intent.getIntExtra("id",0)));
            produceId = intent.getLongExtra("produceId", 0);
            notifyId = intent.getLongExtra("notifyId", 0);
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel((int) notifyId);
            //开启服务  通知服务有点击（网络请求）事件
            Intent intentService = new Intent(context, NoticeMsgClickService.class);
            intentService.putExtra("dataId",notifyId);
            context.startService(intentService);
            //判断app进程是否存活
            if(Util.isAppAlive(context, "com.zxly.o2o.o2o_user")){
                Intent detailIntent;
                if(goto_h5){
                    if(goto_native){
                        detailIntent = new Intent(context, ShowDefineGetuiAct.class);
                        detailIntent.putExtra("id",(int) id);
                        detailIntent.putExtra("what",what);
                        detailIntent.putExtra("title", title);
                    }else{
                        detailIntent = new Intent(context, GetuiUserH5DetailAct.class);
                        //传此参数的目的：需求--如果是从通知栏点击通知进入详情显示消息已被撤回  那么点击返回的时候就得直接跳到首页0.0，所以此为标记值
                        detailIntent.putExtra("isFromNotification",true);
                        if(sys_msg_url != null && !"".equals(sys_msg_url)&&sys_msg_url.contains("{userId}")&&what== HXConstant.SYS_ACTIVITY_KEY_USER){
                            //将链接中参数userid替换成获取的userid
                            long userId = Account.user.getId();
                            sys_msg_url=sys_msg_url.replace("{userId}", "" + userId);
                        }
                        detailIntent.putExtra("sys_msg_url", sys_msg_url);
                        detailIntent.putExtra("id",id);
                        detailIntent.putExtra("what",what);
                        detailIntent.putExtra("title", title);
                    }

                }else{
                    detailIntent = new Intent(context, ProductInfoAct.class);
                    detailIntent.putExtra("bannerId", -1);
                    detailIntent.putExtra("produceId", produceId);
                }
                detailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                detailIntent.putExtra("notifyId",notifyId);

                context.startActivity(detailIntent);
            }else {
                Intent launchIntent = context.getPackageManager().
                        getLaunchIntentForPackage("com.zxly.o2o.o2o_user");
                launchIntent.setFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                Bundle args = new Bundle();
                args.putBoolean("goto_h5", goto_h5);
                if(goto_h5){
                    detailIntent.putExtra("what",what);
                    args.putString("sys_msg_url", sys_msg_url);
                    args.putLong("id",id);
                    args.putString("title", "文章详情");
                }else{
                    args.putInt("bannerId",-1);
                    args.putLong("produceId",produceId);
                }
                args.putLong("notifyId",notifyId);
                launchIntent.putExtra(EXTRA_BUNDLE, args);
                context.startActivity(launchIntent);
            }
        }

}
