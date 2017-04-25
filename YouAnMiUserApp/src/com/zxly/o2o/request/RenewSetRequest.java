package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.util.AppLog;

/**
 * @author fengrongjian 2015-3-23
 * @description 保修单首页显示网络请求
 */
public class RenewSetRequest extends BaseRequest {

	public RenewSetRequest(Long id, Long shopId) {
		addParams("id", id);
		addParams("shopId", shopId);
	}

	@Override
	protected void fire(String data) throws AppException {
		AppLog.e("---renew set data---" + data);
	}

	@Override
	protected String method() {
		return "renew/setting";
	}

	@Override
	protected boolean isShowLoadingDialog() {
		// TODO Auto-generated method stub
		return true;
	}

}
