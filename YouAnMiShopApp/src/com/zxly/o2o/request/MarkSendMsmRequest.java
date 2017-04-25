package com.zxly.o2o.request;

import com.zxly.o2o.account.Account;

/**
 * Created by hejun on 2016/9/12.
 * 记录发送短信  会员记录短信的请求
 */
public class MarkSendMsmRequest extends BaseRequest{

    /**
     *
     * @param id 会员id
     */
    public MarkSendMsmRequest(long id){
        addParams("id",id);
        addParams("shopId", Account.user.getShopId());
    }

    @Override
    protected String method() {
        return "/keduoduo/member/recordSms";
    }
}
