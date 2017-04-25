package com.zxly.o2o.request;

import com.android.volley.AuthFailureError;
import com.easemob.easeui.AppException;
import com.google.gson.Gson;
import com.zxly.o2o.model.UserAddress;
import com.zxly.o2o.util.AppLog;

import java.io.UnsupportedEncodingException;

/**
 * @author fengrongjian 2015-1-22
 * @description 新增地址网络请求
 */
public class AddressAddRequest extends BaseRequest {

	private UserAddress userAddress;

	public AddressAddRequest(UserAddress userAddress) {
		this.userAddress = userAddress;
	}

	@Override
	public byte[] getBody() throws AuthFailureError {
		Gson gons = new Gson();
		String data = gons.toJson(this.userAddress);
		try {
			return data.getBytes(getParamsEncoding());
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	@Override
	protected void fire(String data) throws AppException {
		AppLog.e("---address add fire data---" + data);
	}

	@Override
	protected String method() {
		return "address/add";
	}

	@Override
	protected boolean isShowLoadingDialog() {
		// TODO Auto-generated method stub
		return true;
	}

}
