package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.model.NewProduct;

import java.util.List;

/**
 * Created by kenwu on 2015/12/7.
 */
public class DiscountProductRequest extends  BaseRequest {

    private List<NewProduct> products;

    public List<NewProduct> getProducts() {
        return products;
    }

    public DiscountProductRequest(int pageId){
        addParams("pageId",pageId);
    }

    @Override
    protected void fire(String data) throws AppException {
        super.fire(data);
    }

    @Override
    protected String method() {
        return "";
    }
}
