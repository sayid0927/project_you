package com.zxly.o2o.request;

import com.easemob.easeui.AppException;

/**
 * @author fengrongjian  2016-5-31
 */
public class AuthCodeGetRequest extends BaseRequest {

    public AuthCodeGetRequest(String mobilePhone, int command) {
        addParams("mobilePhone", mobilePhone);
        addParams("command", command);
    }

    @Override
    protected void fire(String data) throws AppException {
    }

    @Override
    protected String method() {
        return "/get/auth/code";
    }

}
