package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.config.Config;

public class PushUnbindRequest extends BaseRequest {

    public PushUnbindRequest() {
        addParams("clientId", Config.getuiClientId);
        addParams("type", 1);
        addParams("shopId",Config.shopId);
        addParams("userName", Account.user.getUserName());
    }

    @Override
    protected void fire(String data) throws AppException {
    }

    @Override
    protected String method() {
        return "/getui/uis/unBind";
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }
}
