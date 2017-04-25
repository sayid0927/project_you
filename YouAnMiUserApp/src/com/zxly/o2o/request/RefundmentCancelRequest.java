package com.zxly.o2o.request;

/**
 * Created by Administrator on 2015/7/3.
 */
public class RefundmentCancelRequest extends BaseRequest{


    public RefundmentCancelRequest(long id){
        addParams("id", id);
    }

    @Override
    protected String method() {
        return "/order/cancelRefund";
    }
}
