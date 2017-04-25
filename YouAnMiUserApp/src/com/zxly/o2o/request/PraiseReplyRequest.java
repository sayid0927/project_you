package com.zxly.o2o.request;

/**
 * Created by hejun on 2016/9/19.
 * 点赞评论
 */
public class PraiseReplyRequest extends BaseRequest{
    public PraiseReplyRequest(long id){
        addParams("id",id);
    }
    @Override
    protected String method() {
        return "/article/new/praiseReply";
    }
}
