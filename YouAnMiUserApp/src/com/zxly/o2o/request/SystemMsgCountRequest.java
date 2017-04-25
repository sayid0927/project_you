package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.util.AppLog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author fengrongjian 2015-2-13
 * @description 系统消息未读数目请求
 */
public class SystemMsgCountRequest extends BaseRequest {

	private long count;
	
	public SystemMsgCountRequest(long shopId) {
		addParams("shopId", shopId);
	}

	@Override
	protected void fire(String data) throws AppException {
		AppLog.e("---user init data---" + data);
		try {
			JSONObject jo = new JSONObject(data);
			count = jo.getLong("msgAmount");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected String method() {
		return "user/init";
	}

	public long getMsgCount(){
		return this.count;
	}
}
