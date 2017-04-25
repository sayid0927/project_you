package com.zxly.o2o.request;

/**
 * Created by Administrator on 2015/7/3.
 */
public class RefundmentDeleteRequest extends BaseRequest {
    public RefundmentDeleteRequest(long id) {
        addParams("id", id);
    }

    @Override
    protected String method() {
        return "/order/delRefund";
    }
}
