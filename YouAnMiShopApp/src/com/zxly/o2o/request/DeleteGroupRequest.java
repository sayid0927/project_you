package com.zxly.o2o.request;

import com.zxly.o2o.account.Account;

/**
 * Created by hejun on 2016/9/28.
 */
public class DeleteGroupRequest extends BaseRequest{

    public DeleteGroupRequest(int groupId){
        addParams("groupId",groupId);
        addParams("shopId", Account.user.getShopId());
    }
    @Override
    protected String method() {
        return "/keduoduo/member/deleteMemberGroup";
    }
}
