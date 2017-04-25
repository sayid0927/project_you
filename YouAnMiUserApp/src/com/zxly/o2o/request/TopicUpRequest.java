package com.zxly.o2o.request;

import com.zxly.o2o.account.Account;

/**
 * Created by Administrator on 2015/12/28.
 */
public class TopicUpRequest extends BaseRequest{
    public TopicUpRequest( byte isShopTopic, long topicId) {
        addParams("isShopTopic", isShopTopic);
        addParams("topicId", topicId);
        addParams("userId", Account.user==null?0:Account.user.getId());
    }

    @Override
    protected String method() {
        return "/user/circle/praise";
    }
}
