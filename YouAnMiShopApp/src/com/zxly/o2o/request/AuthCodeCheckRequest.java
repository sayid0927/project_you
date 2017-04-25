package com.zxly.o2o.request;

import com.easemob.easeui.AppException;

/**
 * @author fengrongjian  2016-5-31
 */
public class AuthCodeCheckRequest extends BaseRequest {

    public AuthCodeCheckRequest(String mobilePhone, String code, int command) {
        addParams("mobilePhone", mobilePhone);
        addParams("code", code);
        addParams("command", command);
    }

    @Override
    protected void fire(String data) throws AppException {
    }

    @Override
    protected String method() {
        return "/check/auth/code";
    }

}
