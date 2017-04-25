package com.zxly.o2o.request;


import com.easemob.easeui.AppException;
import com.zxly.o2o.util.EncryptionUtils;

/**
 * @author fengrongjian 2015-5-25
 * @description 设置支付密码网络请求
 */
public class PaySetPwdRequest extends BaseRequest {

    public PaySetPwdRequest(String tradingPwd) {
        addParams("tradingPwd", EncryptionUtils.MD5Swap(tradingPwd));
    }

    @Override
    protected void fire(String data) throws AppException {
    }

    @Override
    protected String method() {
        return "pay/set_trading_pwd";
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

}
