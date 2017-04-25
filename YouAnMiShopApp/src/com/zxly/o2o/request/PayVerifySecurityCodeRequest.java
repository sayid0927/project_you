package com.zxly.o2o.request;


import com.easemob.easeui.AppException;

/**
 * @author fengrongjian 2015-5-25
 * @description 短信验证网络请求
 */
public class PayVerifySecurityCodeRequest extends BaseRequest {

	public PayVerifySecurityCodeRequest(int type, String code, String payNo) {
		addParams("type", type);
		addParams("code", code);
		addParams("payNo", payNo);
	}

	@Override
	protected void fire(String data) throws AppException {
	}

	@Override
	protected String method() {
		return "pay/security_code";
	}

	@Override
	protected boolean isShowLoadingDialog() {
		return true;
	}

}
