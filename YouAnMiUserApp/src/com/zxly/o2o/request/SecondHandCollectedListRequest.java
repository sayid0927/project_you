package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.Contacts;
import com.zxly.o2o.model.Paging;
import com.zxly.o2o.model.UsedProduct;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SecondHandCollectedListRequest extends BaseRequest {
	public List<UsedProduct> usedProducts = new ArrayList<UsedProduct>();

	public SecondHandCollectedListRequest(Paging paging) {
		addParams("shopId", Config.shopId);
		addParams("paging", paging);
	}

	@Override
	protected void fire(String data) throws AppException {
		JSONArray proArray;
		try {
			proArray = new JSONArray(data);
			int proLength = proArray.length();
			for (int i = 0; i < proLength; i++) {
				JSONObject upJo = proArray.getJSONObject(i);
				UsedProduct up = new UsedProduct();
				up.setId(upJo.getLong("id"));
				up.setAssureParty(upJo.getInt("assureParty"));
				up.setPrice((float) upJo.getDouble("price"));
				up.setName(upJo.getString("name"));
				up.setDeprName(upJo.getString("deprName"));
				up.setPutawayTime(upJo.getLong("putawayTime"));
				up.setHeadUrl(upJo.getString("headUrl"));
				String contactInfo=upJo.getString("contact");
				JSONObject conJo=new JSONObject(contactInfo);
				Contacts contact=new Contacts();
				JSONObject addressJo=new JSONObject(conJo.getString("address"));
				contact.setAreaName(addressJo.getString("areaName"));
				contact.setVillageName(addressJo.getString("villageName"));
				contact.setRealName(conJo.getString("realName"));
				contact.setPhone(conJo.getString("phone"));
				//contact.setDetailedAddress(conJo.getString("detailedAddress"));
				up.setContact(contact);
				usedProducts.add(up);
			}
		} catch (org.json.JSONException e) {
			throw JSONException(e);
		}

	}

	@Override
	protected String method() {
		return "/used/collect/list";
	}

}
