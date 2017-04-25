package com.zxly.o2o.request;

import com.android.volley.VolleyError;
import com.easemob.easeui.AppException;
import com.zxly.o2o.config.Config;

/**
 * Created by Administrator on 2015/4/8.
 */
public class AppRunHeatbeatRequest extends BaseRequest {

    public AppRunHeatbeatRequest() {
        addParams("mac", Config.mac);
        addParams("imei",Config.imei);
    }

    @Override
    protected void fire(String data) throws AppException {
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
        return "http://youanmi.cn/cgi-bin/appRunHeatbeat";
    }
}
