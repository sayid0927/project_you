package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.model.AssureScope;
import com.zxly.o2o.util.AppLog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengrongjian 2015-3-16
 * @description 二手担保类型列表网络请求
 */
public class AssureListRequest extends BaseRequest {

	private List<AssureScope> assureList = new ArrayList<AssureScope>();

	public AssureListRequest(Long id, Long classId) {
		addParams("id", id);
		addParams("classId", classId);
	}

	@Override
	protected void fire(String data) throws AppException {
		AppLog.e("---assure list data---" + data);
		try {
			TypeToken<List<AssureScope>> type = new TypeToken<List<AssureScope>>() {
			};
			List<AssureScope> list = GsonParser.getInstance().fromJson(data,
					type);
			if (!listIsEmpty(list)) {
				assureList.addAll(list);
			}
		} catch (Exception e) {
			throw new AppException("数据解析异常");
		}
	}

	@Override
	protected String method() {
		return "used/assure/list";
	}

	public List<AssureScope> getAssureList() {
		return this.assureList;
	}
}
