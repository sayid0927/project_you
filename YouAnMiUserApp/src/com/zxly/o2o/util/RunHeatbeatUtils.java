/**
 * Copyright(C)2012-2013 深圳市壹捌无限科技有限公司版权所有
 * 创 建 人:Jacky
 * 修 改 人:
 * 创 建日期:2013-12-3
 * 描	   述:
 * 版 本 号:
 */
package com.zxly.o2o.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * @author Jacky
 *         定时器工具
 */
public class RunHeatbeatUtils {

    //开启轮询服务  
    public static void startPollingService(Context context, Class<?> cls,long intervalMillis) {
        //获取AlarmManager系统服务
        AlarmManager manager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);

        //包装需要执行Service的Intent  
        Intent intent = new Intent(context, cls);
        PendingIntent pendingIntent =
                PendingIntent.getService(context, 0,
                        intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //触发服务的起始时间  
//        long triggerAtTime = SystemClock.elapsedRealtime(); 
        Calendar calendar = Calendar.getInstance();
        /*calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
		calendar.add(Calendar.MINUTE, 60);*/


        //使用AlarmManger的setRepeating方法设置定期执行的时间间隔（seconds秒）和需要执行的Service  
        manager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                intervalMillis, pendingIntent);
    }

    //停止轮询服务  
    public static void stopPollingService(Context context, Class<?> cls) {
        AlarmManager manager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //取消正在执行的服务  
        manager.cancel(pendingIntent);
    }
}  
