package com.zxly.o2o.request;

/**
 * Created by dsnx on 2016/1/22.
 */
public class ShopAppArticleRequest extends BaseRequest {

    public ShopAppArticleRequest(long id)
    {
        addParams("id",id);
    }
    @Override
    protected String method() {
        return "/shopApp/article";
    }
}
