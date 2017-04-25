package com.zxly.o2o.request;


import com.easemob.easeui.AppException;

/**
 * @author fengrongjian 2015-12-7
 * @description 绑定推广人网络请求
 */
public class PromotionBindRequest extends BaseRequest {

    public PromotionBindRequest(String promotionCode, long shopId) {
        addParams("promotionCode", promotionCode);
        addParams("shopId", shopId);
    }

    @Override
    protected void fire(String data) throws AppException {
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

    @Override
    protected String method() {
        return "bind/setPromotionCode";
    }

}
