package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.application.Config;

/**
 * Created by hejun on 2016/8/5.
 * 个推消息展示在通知栏时请求
 */
public class GetuiMsgShowRequest extends BaseRequest{

    public GetuiMsgShowRequest(long dataId) {
        addParams("clientId", Config.getuiClientId);
        addParams("dataId",dataId);
        if(Account.user!=null)
        {//仅登录时候传
            addParams("userId", Account.user.getId());
        }
    }

    @Override
    protected String method() {
        return "/getui/data/show";
    }

    @Override
    protected void fire(String data) throws AppException {
        super.fire(data);
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return false;
    }
}
