package com.zxly.o2o.request;

import com.easemob.easeui.AppException;

/**
 * Created by Administrator on 2016/5/13.
 */
public class GetMonthOrderPayRequest extends BaseRequest {

    public GetMonthOrderPayRequest(long orderId,  long shopId) {
        addParams("orderId", orderId);
        addParams("shopId", shopId);

    }

    @Override
    protected void fire(String data) throws AppException {

        super.fire(data);

    }

    @Override
    protected String method() {
        return "/insurance/order/monthOrderPay";
    }
}
