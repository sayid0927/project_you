package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.util.AppLog;

/**
 * @author fengrongjian 2015-1-27
 * @description 系统消息已读网络请求
 */
public class SystemMsgReadRequest extends BaseRequest {
	
	public SystemMsgReadRequest(long msgId) {
			addParams("shopId", Config.shopId);
			addParams("id", msgId);
	}

	@Override
	protected void fire(String data) throws AppException {
		AppLog.e("---msg read data---" + data);
	}

	@Override
	protected String method() {
		return "msg/read";
	}

}
