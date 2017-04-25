package com.zxly.o2o.request;

import com.zxly.o2o.account.Account;

/**
 * Created by dsnx on 2015/7/15.
 */
public class PromoteCallbackConfirmRequest extends BaseRequest {



    /**
     *
     * @param targetId
     * @param type 1商品 2 文章
     */
    public PromoteCallbackConfirmRequest(long targetId,int type,String title)
    {
        addParams("userId", Account.user.getId());
        addParams("shopId",Account.user.getShopId());
        addParams("targetId",targetId);
        addParams("type",type);
        addParams("innerType",1);
        addParams("title",title);
    }

    public PromoteCallbackConfirmRequest(long targetId,int type,int inerType,String title)
    {
        addParams("userId", Account.user.getId());
        addParams("shopId",Account.user.getShopId());
        addParams("targetId",targetId);
        addParams("type",type);
        addParams("innerType",inerType);
        addParams("title",title);
    }


    @Override
    protected String method() {
        return "/promote/callbackConfirm";
    }
}
