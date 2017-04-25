package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.util.AppLog;

/**
 * @author fengrongjian 2015-1-22
 * @description 删除地址网络请求
 */
public class AddressRemoveRequest extends BaseRequest {

	public AddressRemoveRequest(long addressId) {
		addParams("addressId", addressId);
	}

	@Override
	protected void fire(String data) throws AppException {
		AppLog.e("---address remove fire data---" + data);
	}

	@Override
	protected String method() {
		return "address/remove";
	}
	
	@Override
	protected boolean isShowLoadingDialog() {
		// TODO Auto-generated method stub
		return true;
	}

}
