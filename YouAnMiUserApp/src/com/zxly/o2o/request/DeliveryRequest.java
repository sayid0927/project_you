package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.zxly.o2o.model.UserAddress;

import org.json.JSONObject;

public class DeliveryRequest extends BaseRequest {


	private int delivery;
	private  float freight;
	private  UserAddress userAddress;
	
	@Override
	protected void fire(String data) throws AppException {
		try {
			JSONObject address=new JSONObject(data);
			String strUser=address.getString("address");
			userAddress= GsonParser.getInstance().getBean(strUser, UserAddress.class);
			delivery=address.getInt("isDelivery");
			freight=(float) address.getDouble("freight");
		} catch (org.json.JSONException e) {
			throw JSONException(e);
		}
	}

	@Override
	protected String method() {
		return "sub/confirm";
	}


    /**
     *
     * @return 1:支持送货上门  2:不支持送货上门
     */
	public int getDelivery() {
		return delivery;
	}

	public UserAddress getUserAddress() {
		return userAddress;
	}

	public float getFreight() {
		return freight;
	}

	

	
}
