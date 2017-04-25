package com.zxly.o2o.request;

/**
 * Created by hejun on 2016/9/19.
 * 评论文章
 */
public class ReplyArticleRequest extends BaseRequest{
    /**
     *
     * @param content  评论内容
     * @param replyerId 评论者id
     */
    public ReplyArticleRequest(String content,long replyerId){
        addParams("content",content);
        addParams("replyerId",replyerId);
    }
    @Override
    protected String method() {
        return null;
    }
}
