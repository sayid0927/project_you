package com.zxly.o2o.request;

import com.easemob.easeui.AppException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author fengrongjian 2016-4-28
 * @description 申请购买延保网络请求
 */
public class InsuranceBuyRequest extends BaseRequest {
    private long orderId;

    public InsuranceBuyRequest(long shopId, long productId, String phoneBrand, String phoneModel, String userName, String userPhone, String serialNum, int isNeedInvoice) {
        addParams("shopId", shopId);
        addParams("productId", productId);
        addParams("isNeedInvoice", isNeedInvoice);
        addParams("phoneBrand", phoneBrand);
        addParams("phoneModel", phoneModel);
        addParams("userName", userName);
        addParams("userPhone", userPhone);
        addParams("serialNum", serialNum);
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject jsonObject = new JSONObject(data);
            if(jsonObject.has("orderId")){
                this.orderId = jsonObject.getLong("orderId");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String method() {
        return "insurance/product/buy";
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

    public long getOrderId() {
        return orderId;
    }
}
