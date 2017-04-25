package com.zxly.o2o.request;


import com.easemob.easeui.AppException;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.util.StringUtil;

/**
 * @author dsnx
 * @version YIBA-O2O 2014-12-26
 * @since YIBA-O2O
 */
public class RegisterRequest extends BaseRequest {
    /**
     * @param userName      用户名，目前为手机号
     * @param password      密码
     * @param promotionCode 推广码
     */
    public RegisterRequest(String userName, String password, long shopId, String authCode, int sourceType, String promotionCode) {
        addParams("userName", userName);
        addParams("password", password);
        addParams("shopId", shopId);
        addParams("authCode", authCode);
        addParams("sourceType", sourceType);
        if(Config.servieProvider != 0) {
            addParams("serviceProvider", Config.servieProvider);
        }
        if (!StringUtil.isNull(promotionCode)) {
            addParams("promotionCode", promotionCode);
        }
        addParams("clientId", Config.getuiClientId);
    }

    @Override
    protected void fire(String data) throws AppException {
    }

    @Override
    protected String method() {
        return "/auth/regist";
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

}
