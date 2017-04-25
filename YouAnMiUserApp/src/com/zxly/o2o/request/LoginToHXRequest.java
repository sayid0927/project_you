package com.zxly.o2o.request;

import com.zxly.o2o.config.Config;

/**
 * Created by Benjamin on 2015/7/10.
 */
public class LoginToHXRequest extends BaseRequest{
    @Override
    protected String method() {
        return new StringBuffer("/auth/deviceno/regist?shopId=").append(Config.shopId).toString();
    }

    public LoginToHXRequest() {
    }
}
