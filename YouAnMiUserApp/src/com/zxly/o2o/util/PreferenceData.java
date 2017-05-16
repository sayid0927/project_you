package com.zxly.o2o.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.Serializable;
import java.util.TimeZone;

/**
 * Created by dell on 2016/5/4.
 */
public class PreferenceData implements Serializable {
    private static final long serialVersionUID = 1019621703138147697L;

    private  static TimeZone tz = TimeZone.getDefault();


    /** Preference Config **/
    public static final String SHARED_PREFERENCE = "YanmiUserConfig";

    public static final String SAVE_MESSAGENUM_VALUE = "SAVE_MESSAGENUM_VALUE";



    public static void clearAllSharePreferences(Context context) {
        clearSharePreferences(context, SHARED_PREFERENCE);
    }

    public static void clearSharePreferences(Context context, String spName) {
        SharedPreferences sharedPreferences = context.getApplicationContext()
                .getSharedPreferences(spName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit().clear();
        editor.commit();
    }

    /** 保存我的消息数量*/
    public static void setMessageNumValue(Context context, int account) {
        SharedPreferences sharedPreferences = context.getApplicationContext()
                .getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SAVE_MESSAGENUM_VALUE, account);
        editor.commit();
    }

    /** 获取我的消息数量**/
    public static int getMessageNumValue(Context context) {
        SharedPreferences sharedPreferences = context.getApplicationContext()
                .getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(SAVE_MESSAGENUM_VALUE, 0);
    }

}
