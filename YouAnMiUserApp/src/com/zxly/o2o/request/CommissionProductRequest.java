package com.zxly.o2o.request;

import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.NewProduct;

import java.util.List;

/**
 * Created by kenwu on 2015/12/7.
 */
public class CommissionProductRequest extends  BaseRequest {

    private List<NewProduct> productlist;

    public List<NewProduct> getProductlist() {
        return productlist;
    }

    public CommissionProductRequest(int pageId){
        addParams("shopId", Config.shopId);
        addParams("pageIndex",pageId);
    }

    @Override
    protected String method() {
        return "/appFound/popuProduct";
    }
}
