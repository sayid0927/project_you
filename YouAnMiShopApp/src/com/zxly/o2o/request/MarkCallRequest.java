package com.zxly.o2o.request;

import com.zxly.o2o.account.Account;

/**
 * Created by hejun on 2016/9/12.
 * 记录拨打电话
 */
public class MarkCallRequest extends BaseRequest{
    /**
     *
     * @param id 会员id
     */
    public MarkCallRequest(long id){
        addParams("id",id);
        addParams("shopId", Account.user.getShopId());
    }
    @Override
    protected String method() {
        return "/keduoduo/member/recordPhoneCall";
    }
}
