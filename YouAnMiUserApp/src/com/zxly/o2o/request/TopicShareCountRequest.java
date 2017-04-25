package com.zxly.o2o.request;

import com.zxly.o2o.account.Account;

/**
 * Created by Administrator on 2016/1/22.
 */
public class TopicShareCountRequest extends BaseRequest{
    public TopicShareCountRequest(byte isShopTopic,long topicId){
        addParams("isShopTopic",isShopTopic);
        addParams("topicId",topicId);
        addParams("userId", Account.user.getId());

    }

    @Override
    protected String method() {
        return "/user/circle/share";
    }
}
