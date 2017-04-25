package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.model.RenewDeadline;
import com.zxly.o2o.model.RenewScope;
import com.zxly.o2o.util.AppLog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengrongjian 2015-3-18
 * @description 保修单续保初始化网络请求
 */
public class RenewInitRequest extends BaseRequest {

	private List<RenewScope> scopeList = new ArrayList<RenewScope>();
	private List<RenewDeadline> deadlineList = new ArrayList<RenewDeadline>();

	public RenewInitRequest(Long shopId, Float price) {
		addParams("shopId", shopId);
		addParams("price", price);
	}

	@Override
	protected void fire(String data) throws AppException {
		AppLog.e("---renew init data---" + data);
		try {
			JSONObject json = new JSONObject(data);
			String scopes = json.getString("scopes");
			TypeToken<List<RenewScope>> scopeType = new TypeToken<List<RenewScope>>() {
			};
			List<RenewScope> scopeLists = GsonParser.getInstance().fromJson(scopes,
					scopeType);
			if (!listIsEmpty(scopeLists)) {
				scopeList.addAll(scopeLists);
			}
			String deadlines = json.getString("deadlines");
			TypeToken<List<RenewDeadline>> deadlineType = new TypeToken<List<RenewDeadline>>() {
			};
			List<RenewDeadline> deadlineLists = GsonParser.getInstance().fromJson(deadlines,
					deadlineType);
			if (!listIsEmpty(deadlineLists)) {
				for(RenewDeadline renewDeadline : deadlineLists){
					if(renewDeadline.getPrice() != null){
						deadlineList.add(renewDeadline);
					}
				}
			}
//			if (!listIsEmpty(deadlineLists)) {
//				deadlineList.addAll(deadlineLists);
//			}
		} catch (Exception e) {
			throw new AppException("数据解析异常");
		}
	}

	@Override
	protected String method() {
		return "renew/desc";
	}

	public List<RenewScope> getScopeList() {
		return this.scopeList;
	}

	public List<RenewDeadline> getDeadlineList() {
		return this.deadlineList;
	}
}
