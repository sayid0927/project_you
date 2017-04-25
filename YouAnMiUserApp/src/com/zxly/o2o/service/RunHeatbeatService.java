
package com.zxly.o2o.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.Paging;
import com.zxly.o2o.model.SystemMessage;
import com.zxly.o2o.request.AppRunHeatbeatRequest;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.SystemMsgListRequest;
import com.zxly.o2o.request.SystemMsgListUnloginRequest;
import com.zxly.o2o.util.NetworkUtil;
import com.zxly.o2o.util.NotifyUtil;


public class RunHeatbeatService extends Service {
	private Context context;
	private long id;
	private long createTime;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		if(NetworkUtil.isBleasantWifiNet(context))
		{
			AppRunHeatbeatRequest appRunHeatbeatRequest = new AppRunHeatbeatRequest();
			appRunHeatbeatRequest.start();
		}

	}


}
