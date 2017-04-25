package com.zxly.o2o.request;

import com.easemob.easeui.AppException;

/**
 * @author fengrongjian 2015-12-8
 * @description 解绑银行卡网络请求
 */
public class PayDelBankcardRequest extends BaseRequest {

    public PayDelBankcardRequest(long id) {
        addParams("id", id);
    }

    @Override
    protected void fire(String data) throws AppException {
    }

    @Override
    protected String method() {
        return "pay/delBindCard";
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }


}
