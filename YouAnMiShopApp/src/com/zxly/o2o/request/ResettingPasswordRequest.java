package com.zxly.o2o.request;

import com.easemob.easeui.AppException;

public class ResettingPasswordRequest extends BaseRequest {
    /**
     * @param userName 用户账号
     * @param paw      密码
     * @param code     验证码
     */
    public ResettingPasswordRequest(String userName, String paw, String code) {
        addParams("userName", userName);
        addParams("paw", paw);
        addParams("code", code);
    }

    @Override
    protected void fire(String data)
            throws AppException {
    }

    @Override
    protected String method() {
        return "shopApp/setPassword2";
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

}
