package com.zxly.o2o.request;

/**
 * Created by dsnx on 2016/1/22.
 */
public class ProductShareRequest extends BaseRequest {

    public ProductShareRequest(long productId)
    {
        addParams("id",productId);
    }
    @Override
    protected String method() {
        return "/product/share";
    }
}
