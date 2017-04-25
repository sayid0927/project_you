package com.zxly.o2o.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.Paging;
import com.zxly.o2o.model.SystemMessage;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.SystemMsgListRequest;
import com.zxly.o2o.request.SystemMsgListUnloginRequest;
import com.zxly.o2o.util.NotifyUtil;
import com.zxly.o2o.util.PreferUtil;

/**
 * Created by dsnx on 2015/4/22.
 */
public class PullMessageService extends Service {
    private Context context;
    private PreferUtil preferUtil;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        preferUtil = PreferUtil.getInstance();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        /*if (Account.user != null) {
            loadData();
        } else {
            loadDataUnlogin();
        }*/
    }

    private void loadDataUnlogin() {
        // TODO Auto-generated method stub
        Paging paging = new Paging(1, 1);
        final SystemMsgListUnloginRequest request = new SystemMsgListUnloginRequest(
                paging, Config.shopId);
        request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                if (!request.getMsgList().isEmpty()) {
                    SystemMessage sysMsg = request.getMsgList().get(0);
                    if (sysMsg.getId() > preferUtil.getMsgId()
                            && sysMsg.getCreateTime() != preferUtil.getMsgCreateTime()) {
                        try {
                            NotifyUtil.showNotification(context, sysMsg);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        preferUtil.setMsgId(sysMsg.getId());
                        preferUtil.setMsgCreateTime(sysMsg.getCreateTime());
                    }
                }
            }

            @Override
            public void onFail(int code) {
            }
        });
        request.start(this);
    }

    private void loadData() {
        Paging paging = new Paging(1, 1);
        final SystemMsgListRequest request = new SystemMsgListRequest(paging,
                Config.shopId);
        request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                if (!request.getMsgList().isEmpty()) {
                    SystemMessage sysMsg = request.getMsgList().get(0);
                    if (sysMsg.getStatus() == 1
                            && sysMsg.getCreateTime() != preferUtil.getMsgCreateTime()) {
                        try {
                            NotifyUtil.showNotification(context, sysMsg);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        preferUtil.setMsgCreateTime(sysMsg.getCreateTime());
                    }
                }
            }

            @Override
            public void onFail(int code) {
            }
        });
        request.start(this);
    }

}
