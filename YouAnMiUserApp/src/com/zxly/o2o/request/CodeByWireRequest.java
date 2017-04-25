package com.zxly.o2o.request;


import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.controller.AppController;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dsnx on 2016/1/5.
 */
public class CodeByWireRequest extends BaseRequest {


    public CodeByWireRequest(String sn,String serialNum)
    {
        addParams("serialNum",serialNum);
        addParams("sn",sn);
        addParams("shopId", Config.shopId);
    }
    @Override
    protected String method() {
        return "bind/getCodeByWire";
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject js=new JSONObject(data);
            String promotionCode=js.getString("promotionCode");
            AppController.getInstance().savePromotionCode(promotionCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean isShowErrorMsg() {
        return false;
    }
}
