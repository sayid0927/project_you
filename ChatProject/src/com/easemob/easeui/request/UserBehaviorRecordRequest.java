package com.easemob.easeui.request;


/**
 * Created by hejun on 2016/10/19.
 */
public class UserBehaviorRecordRequest extends HXNormalRequest{
    public UserBehaviorRecordRequest(long id,String title,int type){
        addParams("id",id);
        addParams("title",title);
        addParams("type",type);
    }
    @Override
    protected String method() {
        return "/msg/shopMsgBehavior";
    }
}
