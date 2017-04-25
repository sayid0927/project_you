package com.zxly.o2o.request;

import com.easemob.easeui.AppException;

/**
 * @author fengrongjian  2016-5-31
 */
public class UpdateMobileRequest extends BaseRequest {
    public String authCode;

    /**
     * @param code   短信验证码
     * @param mobile 新手机号码
     * @param type   短信模板类型(16:为修改手机号码模板类型)
     * @param userId 用户id
     */
    public UpdateMobileRequest(String code, String mobile, int type, long userId) {
        addParams("code", code);
        addParams("mobile", mobile);
        addParams("type", type);
        addParams("user_id", userId);
    }

    @Override
    protected void fire(String data) throws AppException {
    }

    @Override
    protected String method() {
        return "/update/mobile";
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

}
