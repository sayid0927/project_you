package com.zxly.o2o.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by kenwu on 2015/9/9.
 */
public class PhoneUtil {

    /**
     * 调出拨号键盘
    * */
    public static void openPhoneKeyBord(String phoneNumber,Context context){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phoneNumber));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


}
