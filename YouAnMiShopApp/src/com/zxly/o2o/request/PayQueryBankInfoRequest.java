package com.zxly.o2o.request;


import com.easemob.easeui.AppException;

import org.json.JSONObject;

/**
 * @author fengrongjian 2016-3-7
 * @description 银行卡信息网络请求
 */
public class PayQueryBankInfoRequest extends BaseRequest {
    private String bankName;
    private int bankType;
    private String bankNo;
    private int withdrawType;

    public PayQueryBankInfoRequest(String cardNo) {
        addParams("cardNo", cardNo);
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject json = new JSONObject(data);
            if (json.has("bankName")) {
                bankName = json.getString("bankName");
            }
            if (json.has("bankType")) {
                bankType = json.getInt("bankType");
            }
            if (json.has("bankNo")) {
                bankNo = json.getString("bankNo");
            }
            if (json.has("withdrawType")) {
                withdrawType = json.getInt("withdrawType");
            }
        } catch (Exception e) {
            throw new AppException("数据解析异常");
        }
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

    @Override
    protected String method() {
        return "pay/queryCardBin";
    }

    public String getBankName() {
        return bankName;
    }

    public int getBankType() {
        return bankType;
    }

    public String getBankNo() {
        return bankNo;
    }

    public int getWithdrawType() {
        return withdrawType;
    }
}
