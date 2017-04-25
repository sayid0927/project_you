package com.zxly.o2o.request;


import com.easemob.easeui.AppException;

/**
 * @author fengrongjian 2015-8-26
 * @description 流量充值网络请求
 */
public class FlowRechargeRequest extends BaseRequest {

    public FlowRechargeRequest(String numberNo) {
        addParams("numberNo", numberNo);
    }

    @Override
    protected void fire(String data) throws AppException {
    }

    @Override
    protected String method() {
        return "flow/chongzhi";
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

}
