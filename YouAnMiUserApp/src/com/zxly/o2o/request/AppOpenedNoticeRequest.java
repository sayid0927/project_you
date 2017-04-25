package com.zxly.o2o.request;

import com.android.volley.VolleyError;
import com.easemob.easeui.AppException;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.util.AppLog;

/**
 * Created by Administrator on 2015/4/7.
 */
public class AppOpenedNoticeRequest extends BaseRequest {

    public AppOpenedNoticeRequest() {
        addParams("mac",Config.mac);
        addParams("ip",Config.ip);
        addParams("os","android");
    }

    @Override
    protected void fire(String data) throws AppException {
        AppLog.e("");
    }


    @Override
    public int getTimeoutMs() {
        return 1000*2;
    }

    @Override
    protected String method() {
        return null;
    }
    @Override
    protected boolean isHandlerJSONError() {
        return false;
    }

    @Override
    public void deliverError(VolleyError error) {
        if (listener != null) {
            listener.onFail(0000);
        }
    }

    @Override
    public String getUrl() {
        return "http://youanmi.cn/cgi-bin/appOpenRouter";
    }
}
