package com.zxly.o2o.request;


import com.easemob.easeui.AppException;

import org.json.JSONObject;

/**
 * @author fengrongjian 2015-5-22
 * @description 充值网络请求
 */
public class PayRechargeRequest extends BaseRequest {
	private String payNo;
	private String desc;
	private String money;

	public PayRechargeRequest(String money, Long userCardId) {
		addParams("money", money);
		addParams("userCardId", userCardId);
	}

	@Override
	protected void fire(String data) throws AppException {
		try {
			JSONObject json = new JSONObject(data);
			if (json.has("payNo")) {
				payNo = json.getString("payNo");
			}
			if (json.has("deac")) {
				desc = json.getString("deac");
			}
			if (json.has("money")) {
				money = json.getString("money");
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
		return "pay/renewal";
	}

	public String getPayNo() {
		return payNo;
	}

	public String getDesc() {
		return desc;
	}

	public String getMoney() {
		return money;
	}

}
