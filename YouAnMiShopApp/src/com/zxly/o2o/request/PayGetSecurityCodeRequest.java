package com.zxly.o2o.request;


import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;

/**
 * @author fengrongjian 2015-5-25
 * @description 发送验证码网络请求
 */
public class PayGetSecurityCodeRequest extends BaseRequest {

	/**
	 * @param type
	 */
	public PayGetSecurityCodeRequest(int type) {
		addParams("shopId", Account.user.getShopId());
		addParams("type", type);
	}

	@Override
	protected void fire(String data) throws AppException {
	}

	@Override
	protected String method() {
		return "pay/get_security_code";
	}

}
