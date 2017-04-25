package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.config.Config;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fengrongjian on 2016/10/26
 */
public class GetPromotionCodeRequest extends MyCircleRequest {

    private String promotionCode;

    public GetPromotionCodeRequest(String phone) {
        addParams("mobilePhone", phone);
        addParams("shopId", Config.shopId);
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject jsonObject = new JSONObject(data);
            promotionCode = jsonObject.getString("promotionCode");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

    @Override
    protected String method() {
        return "newBind/getUserPromotionCode";
    }

    public String getPromotionCode() {
        return promotionCode;
    }
}
