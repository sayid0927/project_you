package com.zxly.o2o.request;


import com.easemob.easeui.AppException;

/**
 * @author fengrongjian 2015-3-24
 * @description 修改密码网络请求
 */
public class ChangePasswordRequest extends BaseRequest {

	public ChangePasswordRequest(String userName, String password,
			String originalPassword) {
		addParams("userName", userName);
		addParams("password", password);
		addParams("originalPassword", originalPassword);
	}

	@Override
	protected void fire(String data) throws AppException {
	}

	@Override
	protected String method() {
		return "/user/password";
	}

	@Override
	protected boolean isShowLoadingDialog() {
		// TODO Auto-generated method stub
		return true;
	}

}
