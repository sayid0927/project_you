package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.util.AppLog;

/**
 * @author fengrongjian 2015-4-21
 * @description 未登录状态下上报系统消息被阅读数网络请求
 */
public class SystemMsgReadUnloginRequest extends BaseRequest {
	
	public SystemMsgReadUnloginRequest(long id, long shopId) {
		if(id == 0){
			addParams("shopId", shopId);
		} else if(shopId == 0){
			addParams("id", id);
		}
	}

	@Override
	protected void fire(String data) throws AppException {
		AppLog.e("---msg read unlogin data---" + data);
	}

	@Override
	protected String method() {
		return "msg/read/unlogin";
	}

}
