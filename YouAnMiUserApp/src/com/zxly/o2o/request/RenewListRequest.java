package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.model.UserMaintain;
import com.zxly.o2o.util.AppLog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengrongjian 2015-3-16
 * @description 保修单列表网络请求
 */
public class RenewListRequest extends BaseRequest {

	private List<UserMaintain> renewList = new ArrayList<UserMaintain>();

	public RenewListRequest(String mobile, long shopId) {
		addParams("mobile", mobile);
		addParams("shopId", shopId);
	}

	@Override
	protected void fire(String data) throws AppException {
		AppLog.e("---renew list data---" + data);
		try {
			TypeToken<List<UserMaintain>> type = new TypeToken<List<UserMaintain>>() {
			};
			List<UserMaintain> list = GsonParser.getInstance().fromJson(data,
					type);
			if (!listIsEmpty(list)) {
				renewList.addAll(list);
			}
		} catch (Exception e) {
			throw new AppException("数据解析异常");
		}
	}

	@Override
	protected String method() {
		return "renew/list";
	}

	public List<UserMaintain> getRenewList() {
		return this.renewList;
	}
}
