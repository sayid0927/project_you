package com.zxly.o2o.request;


import com.easemob.easeui.AppException;

/**
 * @author fengrongjian 2015-12-2
 * @description 是否绑定推广人网络请求
 */
public class PromotionCheckRequest extends BaseRequest {

    public PromotionCheckRequest(long userId) {
        addParams("userId", userId);
    }

    @Override
    protected void fire(String data) throws AppException {
    }

    @Override
    protected String method() {
        return "bind/checkIsBind";
    }

}
