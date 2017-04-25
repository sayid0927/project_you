package com.zxly.o2o.request;


import com.easemob.easeui.AppException;
import com.zxly.o2o.config.Config;

/**
 * @author fengrongjian 2015-5-25
 * @description 发送验证码网络请求
 */
public class PayGetSecurityCodeRequest extends BaseRequest {

	/**
	 * @param type 3：设置交易密码，4：充值，5：提现，6：交易确认
	 */
	public PayGetSecurityCodeRequest(int type) {
		addParams("shopId", Config.shopId);
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
