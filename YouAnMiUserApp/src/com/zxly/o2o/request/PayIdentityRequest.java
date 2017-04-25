package com.zxly.o2o.request;


import com.easemob.easeui.AppException;

import org.json.JSONObject;

/**
 * @author fengrongjian 2015-12-4
 * @description 我的身份信息网络请求
 */
public class PayIdentityRequest extends BaseRequest {
    private String accountBalance;
    private int isUserPaw;
    private String idCard;
    private String userName;

    public PayIdentityRequest() {
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject json = new JSONObject(data);
            if (json.has("accountBalance")) {
                accountBalance = json.getString("accountBalance");
            }
            if (json.has("isUserPaw")) {
                isUserPaw = json.getInt("isUserPaw");
            }
            if (json.has("idCard")) {
                idCard = json.getString("idCard");
            }
            if (json.has("userName")) {
                this.userName = json.getString("userName");
            }
        } catch (Exception e) {
            throw new AppException("数据解析异常");
        }
    }

    @Override
    protected String method() {
        return "pay/identity";
    }

    public Float getAccountBalance() {
        if (accountBalance != null && !"null".equals(accountBalance)) {
            return Float.parseFloat(accountBalance);
        } else {
            return 0.00f;
        }
    }

    public int getIsUserPaw() {
        return isUserPaw;
    }

    public String getIdCard() {
        return idCard;
    }

    public String getUserName() {
        return userName;
    }
}
