package com.easemob.easeui.request;

import com.easemob.chatuidemo.HXApplication;
import com.easemob.easeui.AppException;
import com.easemob.easeui.EaseConstant;

/**
 * Created by Administrator on 2016/7/18.
 */
public class DeleteNetMsgRequest extends HXNormalRequest{

    /**
     * @param type 消息类型 0门店快讯 3门店活动 2门店公告 4柚安米公告
     */
    public DeleteNetMsgRequest(int type){
        addParams("type",type);
        if(EaseConstant.shopID>0){
            addParams("clientId",EaseConstant.getuiUserClientID);
        }else{
            addParams("clientId",EaseConstant.getuiShopClientID);
        }
        addParams("shopId", Math.abs(EaseConstant.shopID));
        if(EaseConstant.currentUser.getFirendsUserInfo()!=null){
            addParams("userId",EaseConstant.currentUser.getFirendsUserInfo().getId());
        }
    }

    @Override
    protected void fire(String data) throws AppException {
        super.fire(data);
    }

    @Override
    protected String method() {
        return "/getui/type/del";
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }
}
