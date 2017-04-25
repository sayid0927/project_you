package com.zxly.o2o.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * Created by kenwu on 2016/1/29.
 */
public class ApkInfoUtil {

    public static int getDefaultH5Version(Context context){
        ApplicationInfo appInfo = null;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return  appInfo.metaData.getInt("H5_VERSION_CODE");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
      return 0;
    }

    public static int getDefaultH5StyleId(Context context){
        ApplicationInfo appInfo = null;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return  appInfo.metaData.getInt("H5_STYLE_ID");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getAppName(Context context){
        PackageManager packageManager = context.getPackageManager();
        return (String) packageManager.getApplicationLabel(context.getApplicationInfo());
    }

}
