package com.zxly.o2o.request;

import com.zxly.o2o.account.Account;

/**
 * Created by Administrator on 2016/4/11.
 */
public class PersonalRemarkRequest extends BaseRequest {
    public PersonalRemarkRequest(String description, String remarkName, long remarkUserId) {
        addParams("description", description);
        addParams("remarkName", remarkName);
        addParams("remarkUserId", remarkUserId);
        addParams("shopId", Account.user.getShopId());
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

    @Override
    protected String method() {
        return "/shopUserRemark/addOrUpdate";
    }
}
