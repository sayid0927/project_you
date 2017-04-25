package com.zxly.o2o.request;


import com.easemob.easeui.AppException;
import com.zxly.o2o.util.EncryptionUtils;

/**
 * @author fengrongjian 2015-7-20
 * @description 修改支付密码网络请求
 */
public class PayChangePwdRequest extends BaseRequest {

	public PayChangePwdRequest(String code, String tradingPwd) {
		addParams("code", code);
		addParams("tradingPwd", EncryptionUtils.MD5Swap(tradingPwd));
	}

	@Override
	protected void fire(String data) throws AppException {
	}

	@Override
	protected String method() {
		return "pay/setTransPaw";
	}

	@Override
	protected boolean isShowLoadingDialog() {
		return true;
	}

}
