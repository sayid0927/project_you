package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.model.Paging;
import com.zxly.o2o.model.SystemMessage;
import com.zxly.o2o.util.AppLog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengrongjian 2015-1-23
 * @description 系统消息列表网络请求
 */
public class SystemMsgListRequest extends BaseRequest {

	private List<SystemMessage> msgList = new ArrayList<SystemMessage>();

	public SystemMsgListRequest(Paging paging, long shopId) {
		addParams("paging", paging);
		addParams("shopId", shopId);
	}

	@Override
	protected void fire(String data) throws AppException {
		AppLog.e("---msg list data---" + data);
		try {
			JSONObject json = new JSONObject(data);
			String messages = json.getString("messages");
			TypeToken<List<SystemMessage>> type = new TypeToken<List<SystemMessage>>() {
			};
			List<SystemMessage> list = GsonParser.getInstance().fromJson(messages,
					type);
			if (!listIsEmpty(list)) {
				msgList.addAll(list);
			}
		} catch (Exception e) {
			throw new AppException("数据解析异常");
		}
	}

	@Override
	protected String method() {
		return "msg/list";
	}

	public List<SystemMessage> getMsgList() {
		return this.msgList;
	}
}
