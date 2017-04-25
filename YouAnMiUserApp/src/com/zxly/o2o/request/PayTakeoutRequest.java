package com.zxly.o2o.request;


import com.easemob.easeui.AppException;
import com.zxly.o2o.util.StringUtil;

import org.json.JSONObject;

/**
 * @author fengrongjian 2015-5-22
 * @description 提现网络请求
 */
public class PayTakeoutRequest extends BaseRequest {
	private String payNo;
	private String desc;
	private String money;

	public PayTakeoutRequest(String money, Long userCardId, int type) {
		addParams("money", money);
		addParams("userCardId", userCardId);
		addParams("type", type);
	}

	public PayTakeoutRequest(String money, String userName, String idCard, String bankNumber, int type) {
		addParams("money", money);
		if(!StringUtil.isNull(userName))
		{
			addParams("userName", userName);
		}
		if(!StringUtil.isNull(idCard))
		{
			addParams("idCard", idCard);
		}
		addParams("bankNumber", bankNumber);
		addParams("type", type);
	}

	@Override
	protected void fire(String data) throws AppException {
		try {
			JSONObject json = new JSONObject(data);
			if (json.has("payNo")) {
				payNo = json.getString("payNo");
			}
			if (json.has("money")) {
				desc = json.getString("money");
			}
			if (json.has("deac")) {
				desc = json.getString("deac");
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
		return "pay/withdrawals";
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
