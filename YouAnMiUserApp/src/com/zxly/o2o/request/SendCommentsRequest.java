package com.zxly.o2o.request;

/**
 * Created by hejun on 2016/9/21.
 */
public class SendCommentsRequest extends BaseRequest{
    public SendCommentsRequest(String content,long replyerId,long shopArticleId){
        addParams("content",content);
        addParams("replyerId",replyerId);
        addParams("articleId",shopArticleId);
    }
    @Override
    protected String method() {
        return "/article/new/reply";
    }
}
