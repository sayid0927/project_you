package com.zxly.o2o.request;

/**
 * Created by hejun on 2016/9/30.
 * 文章详情页面分享记录
 */
public class MarkShareRequest extends BaseRequest{

    public MarkShareRequest(int articleType,long id){
        addParams("articleType",articleType);
        addParams("id",id);

    }
    @Override
    protected String method() {
        return "/article/new/share";
    }
}
