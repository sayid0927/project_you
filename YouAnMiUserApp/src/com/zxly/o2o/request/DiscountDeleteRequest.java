package com.zxly.o2o.request;

import com.easemob.easeui.AppException;

public class DiscountDeleteRequest extends BaseRequest {

    public DiscountDeleteRequest(long id) {
        addParams("id", id);
    }

    @Override
    protected void fire(String data) throws AppException {
    }

    @Override
    protected String method() {
        return "discount/delete";
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }
}
