package com.zxly.o2o.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.GetuiMsgClickRequest;

/**
 * Created by Administrator on 2016/7/19.
 * 通知栏消息点击事件监听
 */
public class NoticeMsgClickService extends IntentService{
    public NoticeMsgClickService() {
        super("NoticeMsgClickService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        long dataId = intent.getLongExtra("dataId", 0);
        GetuiMsgClickRequest gmcr = new GetuiMsgClickRequest(dataId);
        gmcr.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
            }

            @Override
            public void onFail(int code) {
            }
        });
        gmcr.start();
    }


}
