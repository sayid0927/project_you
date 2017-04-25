package com.zxly.o2o.request;

import com.easemob.easeui.AppException;

/**
 * @author fengrongjian 2016-1-9
 * @description 修改登录密码网络请求
 */
public class ChangeLoginPwdRequest extends BaseRequest {

	public ChangeLoginPwdRequest(String userName, String password,
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
		return "/shop/user/password2";
	}

	@Override
	protected boolean isShowLoadingDialog() {
		return true;
	}

}
