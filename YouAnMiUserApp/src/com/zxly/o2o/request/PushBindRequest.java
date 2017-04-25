package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.config.Config;

public class PushBindRequest extends BaseRequest {

    public PushBindRequest() {
        addParams("clientId", Config.getuiClientId);
        addParams("shopId", Config.shopId);
        if(Account.user!=null){
            addParams("userId",Account.user.getId());
        }
    }

    @Override
    protected void fire(String data) throws AppException {
    }

    @Override
    protected String method() {
        return "getui/cid/register";
    }

}
