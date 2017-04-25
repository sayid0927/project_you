package com.zxly.o2o.request;

/**
 * Created by hejun on 2016/9/19.
 * 点赞文章
 */
public class PraiseArticleRequest extends BaseRequest {
    public PraiseArticleRequest(int articleType,long id){
        addParams("articleType",articleType);
        addParams("id",id);
    }
    @Override
    protected String method() {
        return "/article/new/praise";
    }
}
