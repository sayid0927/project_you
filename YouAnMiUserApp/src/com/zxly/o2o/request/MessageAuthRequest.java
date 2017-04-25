package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author dsnx
 * @version YIBA-O2O 2014-12-26
 * @since YIBA-O2O
 */
public class MessageAuthRequest extends BaseRequest {
    public String authCode;

    /**
     * @param mobilePhone 手机号
     * @param command     表识 1: 注册，2：密码找回
     */
    public MessageAuthRequest(String mobilePhone, String command) {
        addParams("shopId", Config.shopId);
        addParams("mobilePhone", mobilePhone);
        addParams("command", command);
    }

    /**
     * @param mobilePhone 手机号
     * @param command     表识 1: 注册，2：密码找回
     * @param promotionCode  推广码
     */
    public MessageAuthRequest(String mobilePhone, String command, String promotionCode) {
        addParams("shopId", Config.shopId);
        addParams("mobilePhone", mobilePhone);
        addParams("command", command);
        if (!StringUtil.isNull(promotionCode)) {
            addParams("promotionCode", promotionCode);
        }
    }

    @Override
    protected void fire(String data)
            throws AppException {
        try {
            JSONObject jo = new JSONObject(data);
            authCode = jo.getString("authCode");
        } catch (JSONException e) {
            throw new AppException("数据解析异常");
        }
    }

    @Override
    protected String method() {
        return "/sms/auth";
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

}
