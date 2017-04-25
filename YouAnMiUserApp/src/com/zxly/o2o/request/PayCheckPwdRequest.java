package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.util.EncryptionUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author fengrongjian 2015-12-3
 * @description 校验支付密码网络请求
 */
public class PayCheckPwdRequest extends BaseRequest {
    private int wrongCount = 0;
    private String wrongTime;

    public PayCheckPwdRequest(String tradingPwd) {
        addParams("tradingPwd", EncryptionUtils.MD5Swap(tradingPwd));
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject json = new JSONObject(data);
            if (json.has("wrongCount")) {
                this.wrongCount = json.getInt("wrongCount");
            }
            if (json.has("wrongTime")) {
                this.wrongTime = json.getString("wrongTime");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String method() {
        return "pay/checkPwd";
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

    public int getWrongCount() {
        return wrongCount;
    }

    public String getWrongTime() {
        return wrongTime;
    }

}
