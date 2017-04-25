package com.zxly.o2o.request;


import com.easemob.easeui.AppException;

import org.json.JSONObject;

/**
 * Created by kenwu on 2015/9/10.
 */
public class ConfirmReceiveRequest extends BaseRequest {

    private int wrongCount ;
    private String wrongTime;

    public ConfirmReceiveRequest(String orderUnmber,String pwd){
        addParams("orderNo",orderUnmber);
        addParams("tradingPwd",pwd);
    }

    public String getWrongTime() {
        return wrongTime;
    }

    public int getWrongCount() {
        return wrongCount;
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

    @Override
    protected String method() {
        return "/pay/confirmReceipt";
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject json = new JSONObject(data);
            wrongCount=json.optInt("wrongCount",0);
            wrongTime=json.optString("wrongTime", "");
        } catch (Exception e) {
            throw new AppException("确认提货解析错误",e);
        }
    }

}
