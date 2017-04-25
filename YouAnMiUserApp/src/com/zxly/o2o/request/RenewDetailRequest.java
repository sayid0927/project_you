package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.model.RenewScope;
import com.zxly.o2o.util.AppLog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengrongjian 2015-3-18
 * @description 保修单详情网络请求
 */
public class RenewDetailRequest extends BaseRequest {

	private List<RenewScope> renewList = new ArrayList<RenewScope>();

	private String maintainNo = null;

	private String desc;

	public RenewDetailRequest(Long id) {
		addParams("id", id);
	}

	@Override
	protected void fire(String data) throws AppException {
		AppLog.e("---renew detail data---" + data);
		try {
			JSONObject json = new JSONObject(data);
			int renewType = json.getInt("type");
			maintainNo = json.getString("maintainNo");
			if (renewType == 1) {
				desc = json.getString("desc");
			} else if (renewType == 2) {
				String scopes = json.getString("scopes");
				TypeToken<List<RenewScope>> type = new TypeToken<List<RenewScope>>() {
				};
				List<RenewScope> list = GsonParser.getInstance().fromJson(
						scopes, type);
				if (!listIsEmpty(list)) {
					renewList.addAll(list);
				}
			}
		} catch (Exception e) {
			throw new AppException("数据解析异常");
		}
	}

	@Override
	protected String method() {
		return "renew/me";
	}

	public String getMaintainNo() {
		return this.maintainNo;
	}

	public List<RenewScope> getRenewList() {
		return this.renewList;
	}

	public String getDesc() {
		return this.desc;
	}
}
