package com.zxly.o2o.request;

/**
 * Created by Administrator on 2016/1/25.
 */
public class TopicReplyPraiseRequest extends BaseRequest {

    public TopicReplyPraiseRequest(long topicId,long parentId,byte isShopTopic) {
        addParams("topicId", topicId);
        addParams("parentId", parentId);
        addParams("isShopTopic", isShopTopic);
    }

    @Override
    protected String method() {
        return "/user/circle/praiseReply";
    }

}
