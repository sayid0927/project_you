package com.zxly.o2o.util;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by dsnx on 2016/3/23.
 */
public class MyDeviceAdmin extends DeviceAdminReceiver {
    /**
     * 获取设备存储的数值
     *
     * @param context
     * @return
     */
    public static SharedPreferences getDevicePreference(Context context) {
        return context.getSharedPreferences(
                DeviceAdminReceiver.class.getName(), 0);
    }


    @Override
    public void onEnabled(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Log.e("--onEnabled----", "设备管理：可用");
        PreferUtil.getInstance().setDeviceAdminActive(2);
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Log.e("--onDisabled----","设备管理：不可用");
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        // TODO Auto-generated method stub
        return "取消后可能导致桌面的部分功能无法正常使用";
    }

    @Override
    public void onPasswordChanged(Context context, Intent intent) {
        // TODO Auto-generated method stub
//        showToast(context, "设备管理：密码己经改变");
    }

    @Override
    public void onPasswordFailed(Context context, Intent intent) {
        // TODO Auto-generated method stub
//        showToast(context, "设备管理：改变密码失败");
    }

    @Override
    public void onPasswordSucceeded(Context context, Intent intent) {
        // TODO Auto-generated method stub
//        showToast(context, "设备管理：改变密码成功");
    }
}
