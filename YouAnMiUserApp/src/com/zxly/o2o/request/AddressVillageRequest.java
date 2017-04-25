package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.model.AddressVillage;
import com.zxly.o2o.util.AppLog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengrongjian 2015-3-5
 * @description 获取街道村落网络请求
 */
public class AddressVillageRequest extends BaseRequest {
	private List<AddressVillage> villageList = new ArrayList<AddressVillage>();

	public AddressVillageRequest(Integer cityId, Integer areaId) {
		if(cityId != null){
			addParams("cityId", cityId);
		}
		if(areaId != null){
			addParams("areaId", areaId);
		}
	}

	@Override
	protected void fire(String data) throws AppException {
		AppLog.e("---village fire data---" + data);
		try {
			TypeToken<List<AddressVillage>> type = new TypeToken<List<AddressVillage>>() {
			};
			List<AddressVillage> list = GsonParser.getInstance().fromJson(
					data, type);
			if (!listIsEmpty(list)) {
				villageList.addAll(list);
			}
		} catch (Exception e) {
			throw new AppException("数据解析异常");
		}
	}

	@Override
	protected String method() {
		return "address/village/list";
	}
	
	public List<AddressVillage> getVillageList(){
		return this.villageList;
	}

}
