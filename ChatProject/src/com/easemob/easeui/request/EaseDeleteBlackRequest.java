package com.easemob.easeui.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.EaseConstant;

/**
 * Created by Administrator on 2015/12/30.
 */
public class EaseDeleteBlackRequest extends HXNormalRequest{
    public EaseDeleteBlackRequest(long friendsUserId){
        addParams("friendsUserId",friendsUserId);
        addParams("shopId",Math.abs(EaseConstant.shopID));
    }

    @Override
    protected String method() {
        return "/friends/black/delete";
    }

    @Override
    protected void fire(String data) throws AppException {
    }
}
