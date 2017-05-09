package com.zxly.o2o.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.Serializable;

/**
 * Created by dell on 2016/5/4.
 */
public class PreferenceData implements Serializable {
    private static final long serialVersionUID = 1019621703138147697L;


    /** Preference Config **/
    public static final String SHARED_PREFERENCE = "ShopConfig";
    public static final String SAVE_LOCALARTICLESINFO = "SAVE_LocalArticlesInfo";


    public static void clearAllSharePreferences(Context context) {
        clearSharePreferences(context, SHARED_PREFERENCE);
    }

    public static void clearSharePreferences(Context context, String spName) {
        SharedPreferences sharedPreferences = context.getApplicationContext()
                .getSharedPreferences(spName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit().clear();
        editor.commit();
    }


}
