package com.zxly.o2o.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.HXHelper;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.ViewUtils;

/**
 * Created by dsnx on 2016/1/30.
 */
public class AppInstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        PackageManager manager = context.getPackageManager();
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            if(packageName.equals("com.zxly.o2o.o2o_user"))
            {
                HXConstant.isLoginSuccess = false;
                HXHelper.getInstance().logout(true, null);//退出环信
                HXHelper.getInstance().deleteDBContactCache();  //清空本地联系人
                HXHelper.getInstance().cleanYAMContactList();  //清空本地缓存
                PreferUtil.getInstance().setHXAccount(1); //设置为游客状态
                AppController.getInstance().loginHxVisitorAccount();//登录游客账号

                Account.orderCount = 0;
                context.deleteFile("user");
                Account.user = null;
                PreferUtil.getInstance().setLoginToken("");
            }

        }else if(intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED))
        {
            String packageName = intent.getData().getSchemeSpecificPart();
            if(!packageName.equals("com.zxly.o2o.o2o_user")&&packageName.contains("com.zxly.o2o.o2o_user"))
            {
                PreferUtil.getInstance().setFlagOldAppDel(true);
            }
        }

    }
    }
