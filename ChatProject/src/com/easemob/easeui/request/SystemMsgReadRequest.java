package com.easemob.easeui.request;


import android.util.Log;

import com.easemob.easeui.AppException;
import com.easemob.easeui.EaseConstant;

/**
 * @author fengrongjian 2015-1-27
 * @description 系统消息已读网络请求
 */
public class SystemMsgReadRequest extends HXNormalRequest {
	
	public SystemMsgReadRequest(long msgId) {
			addParams("shopId", Math.abs(EaseConstant.shopID));
			addParams("id", msgId);
	}


	@Override
	protected String method() {
		return "msg/read";
	}

}
