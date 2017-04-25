package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.util.AppLog;

/**
 * @author fengrongjian 2015-3-18
 * @description 系统消息删除网络请求
 */
public class SystemMsgRemoveRequest extends BaseRequest {
	
	public SystemMsgRemoveRequest(long id) {
		addParams("id", id);
	}

	@Override
	protected void fire(String data) throws AppException {
		AppLog.e("---msg remove data---" + data);
	}

	@Override
	protected String method() {
		return "msg/remove";
	}

}
