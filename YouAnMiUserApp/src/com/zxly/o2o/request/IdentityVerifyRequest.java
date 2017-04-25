package com.zxly.o2o.request;

import com.easemob.easeui.AppException;

/**
 * @author fengrongjian  2016-5-31
 */
public class IdentityVerifyRequest extends BaseRequest {

    public IdentityVerifyRequest(String certName, String certNo, int certType) {
        addParams("certName", certName);
        addParams("certNo", certNo);
        addParams("certType", certType);
    }

    @Override
    protected void fire(String data) throws AppException {
    }

    @Override
    protected String method() {
        return "/user/acct/cert/add";
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

    @Override
    protected boolean isCheckSign() {
        return true;
    }
}
