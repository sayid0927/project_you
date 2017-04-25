package com.zxly.o2o.request;


import com.easemob.easeui.AppException;

import org.json.JSONObject;

/**
 * @author fengrongjian 2015-5-22
 * @description 支付方式网络请求
 */
public class PayInitRequest extends BaseRequest {
	private String orderPrice;
	private String userBalance;
	private int isUserPaw;
	private int deliveryType;
	private String phoneNo;
	private String shopAddress;

	public PayInitRequest(String orderNo, String businessType) {
		addParams("orderNo", orderNo);
		addParams("businessType", businessType);
	}
	
	@Override
	protected void fire(String data) throws AppException {
		try {
			JSONObject json = new JSONObject(data);
			if (json.has("orderPrice")) {
				orderPrice = json.getString("orderPrice");
			}
			if (json.has("userBalance")) {
				userBalance = json.getString("userBalance");
			}
			if (json.has("isUserPaw")) {
				isUserPaw = json.getInt("isUserPaw");
			}
			if (json.has("deliveryType")) {
				this.deliveryType = json.getInt("deliveryType");
			}
			if (json.has("phoneNo")) {
				this.phoneNo = json.getString("phoneNo");
			}
			if (json.has("shopAddress")) {
				this.shopAddress = json.getString("shopAddress");
			}
		} catch (Exception e) {
			throw new AppException("数据解析异常");
		}
	}

	@Override
	protected String method() {
		return "pay/payment";
	}

	public Float getOrderPrice() {
		return Float.parseFloat(orderPrice);
	}

	public Float getUserBalance() {
		if(userBalance != null && !"null".equals(userBalance)){
			return Float.parseFloat(userBalance);
		} else {
			return 0.00f;
		}
	}

	public int getIsUserPaw() {
		return isUserPaw;
	}

	public int getDeliveryType() {
		return deliveryType;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public String getShopAddress() {
		return shopAddress;
	}
}
