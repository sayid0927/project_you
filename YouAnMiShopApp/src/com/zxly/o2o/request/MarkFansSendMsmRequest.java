package com.zxly.o2o.request;

import com.zxly.o2o.account.Account;

/**
 * Created by hejun on 2016/10/10.
 */
public class MarkFansSendMsmRequest extends BaseRequest{

    public MarkFansSendMsmRequest(long id,String content){
        addParams("fansId",id);
        addParams("shopId", Account.user.getShopId());
        addParams("content",content);
    }
    @Override
    protected String method() {
        return "/keduoduo/fans/recordSms";
    }
}
