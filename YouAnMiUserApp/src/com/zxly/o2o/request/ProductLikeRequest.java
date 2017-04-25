package com.zxly.o2o.request;

import com.zxly.o2o.config.Config;

/**
 * Created by dsnx on 2015/12/25.
 */
public class ProductLikeRequest extends BaseRequest {
    public  ProductLikeRequest(long productId)
    {
        addParams("id",productId);
        addParams("shopId", Config.shopId);

    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

    @Override
    protected String method() {
        return "/product/like";
    }
}
