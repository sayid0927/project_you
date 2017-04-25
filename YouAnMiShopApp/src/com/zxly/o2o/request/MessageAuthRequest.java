package com.zxly.o2o.request;


import com.easemob.easeui.AppException;

import org.json.JSONException;
import org.json.JSONObject;

public class MessageAuthRequest extends BaseRequest {
    public String authCode;

    /**
     * @param userName 账号
     * @param mobile   手机号
     */
    public MessageAuthRequest(int type, String userName, String mobile) {
        addParams("type", type);
        addParams("userName", userName);
        addParams("mobile", mobile);
    }

    @Override
    protected void fire(String data)
            throws AppException {
        try {
            JSONObject jo = new JSONObject(data);
            authCode = jo.getString("authCode");
        } catch (JSONException e) {
            throw new AppException("数据解析异常");
        }
    }

    @Override
    protected String method() {
        return "shopApp/get_security_code";
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

}
