package com.zxly.o2o.request;

import com.easemob.easeui.AppException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author fengrongjian  2016-5-31
 */
public class IdentityQueryRequest extends BaseRequest {
    private String certName;
    private String certNo;

    public IdentityQueryRequest() {
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject jsonObject = new JSONObject(data);
            certName = jsonObject.getString("certName");
            certNo = jsonObject.getString("certNo");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String method() {
        return "/user/acct/cert/query";
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

    public String getCertName() {
        return certName;
    }

    public void setCertName(String certName) {
        this.certName = certName;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }
}
