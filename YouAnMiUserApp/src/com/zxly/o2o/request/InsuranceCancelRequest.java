package com.zxly.o2o.request;

import com.easemob.easeui.AppException;

/**
 * @author fengrongjian 2016-4-28
 * @description 取消申请网络请求
 */
public class InsuranceCancelRequest extends BaseRequest {

    public InsuranceCancelRequest(long orderId) {
        addParams("orderId", orderId);
    }

    @Override
    protected void fire(String data) throws AppException {
    }

    @Override
    protected String method() {
        return "insurance/product/cancel";
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }
}
