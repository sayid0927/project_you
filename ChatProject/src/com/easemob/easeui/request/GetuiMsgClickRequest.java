package com.easemob.easeui.request;

import com.easemob.chatuidemo.HXApplication;
import com.easemob.easeui.AppException;
import com.easemob.easeui.EaseConstant;

/**
 * Created by hejun on 2016/7/16.
 * 个推消息已被点击请求事件
 */
public class GetuiMsgClickRequest extends HXNormalRequest{

    public GetuiMsgClickRequest(long dataId){
        if(EaseConstant.currentUser.getFirendsUserInfo()!=null&&EaseConstant.currentUser.getFirendsUserInfo().getId()!=0)
        {//仅登录时候传
            addParams("userId", EaseConstant.currentUser.getFirendsUserInfo().getId());
        }
        addParams("dataId", dataId);
        if(EaseConstant.shopID>0){
            addParams("clientId",EaseConstant.getuiUserClientID);
        }else{
            addParams("clientId",EaseConstant.getuiShopClientID);
        }
    }

    @Override
    protected String method() {
        return "/getui/data/click";
    }

    @Override
    protected void fire(String data) throws AppException {
        super.fire(data);
    }
}
