package com.zxly.o2o.request;

import com.easemob.easeui.AppException;

import org.json.JSONObject;

/**
 * @author fengrongjian 2015-5-22
 * @description 添加用户银行卡信息跳转页面，第二次添加返回用户的身份信息网络请求
 */
public class PayLoadShortcutRequest extends BaseRequest {
	private String userName = null;
	private String idCard = null;

	public PayLoadShortcutRequest() {
	}
	
	@Override
	protected void fire(String data) throws AppException {
		try {
			JSONObject json = new JSONObject(data);
			if(json.has("userName")){
				userName = json.getString("userName");
			}
			if(json.has("idCard")){
				idCard = json.getString("idCard");
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
		return "pay/identity";
	}

	public String getUserName() {
		return userName;
	}

	public String getIdCard() {
		return idCard;
	}

}
