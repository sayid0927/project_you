package com.zxly.o2o.request;

import com.zxly.o2o.account.Account;

/**
 * Created by hejun on 2016/10/8.\
 * 粉丝拨打电话记录
 */
public class FansCallRecordRequest extends BaseRequest{
    public FansCallRecordRequest(long fansId){
        addParams("fansId",fansId);
        addParams("shopId", Account.user.getShopId());
    }
    @Override
    protected String method() {
        return "/keduoduo/fans/recordPhoneCall";
    }
}
