package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.EaseConstant;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.application.Config;

/**
 * Created by hejun on 2016/7/16.
 * 个推消息已被点击请求事件
 */
public class GetuiMsgClickRequest extends BaseRequest{

    public GetuiMsgClickRequest(long dataId){
        if(Account.user!=null)
        {//仅登录时候传
            addParams("userId", Account.user.getId());
        }
        addParams("dataId", dataId);
        if(EaseConstant.shopID>0){
            addParams("clientId",EaseConstant.getuiUserClientID);
        }else{
            addParams("clientId",EaseConstant.getuiShopClientID);
        }
    }

    @Override
    protected String method() {
        return "/getui/data/click";
    }

    @Override
    protected void fire(String data) throws AppException {
        super.fire(data);
    }
}
