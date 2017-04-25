package com.zxly.o2o.request;

import com.zxly.o2o.account.Account;

/**
 * Created by dsnx on 2015/7/16.
 */
public class PromoteCallbackConfirmRequest extends BaseRequest {



    /**
     *
     * @param targetId
     * @param type 1商品 2 文章
     */
    public PromoteCallbackConfirmRequest(long targetId,int type)
    {
        addParams("userId", Account.user.getId());
        addParams("shopId",Account.user.getShopId());
        addParams("targetId",targetId);
        addParams("type",type);
    }

    @Override
    protected String method() {
        return "/popu/callbackConfirmUserApp";
    }
}
