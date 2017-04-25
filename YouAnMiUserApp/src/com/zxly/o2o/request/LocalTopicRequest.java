package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.ShopTopic;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *     @author fengrongjian  @version 创建时间：2017-7-28
 *     类说明: 包含本地圈子和店圈的帖子（用户app微调新增接口）
 */
public class LocalTopicRequest extends MyCircleRequest {
	public String circleName="";
	public String title="";
	public String imageUrl="";
	public int isConcern;

	public LocalTopicRequest(int pageIndex, long circleId) {
		addParams("pageIndex", pageIndex);
		addParams("circleId", circleId);
		addParams("shopId", Config.shopId);
		if(Account.user != null) {
			addParams("userId", Account.user.getId());
		}
	}

	@Override
	protected void fire(String data) throws AppException {
		JSONObject jo;
		try {
			jo = new JSONObject(data);
			if(jo.has("circleName")) {
				circleName = jo.getString("circleName");
			}
			if(jo.has("imageUrl")) {
				imageUrl = jo.getString("imageUrl");
			}
			if(jo.has("title")) {
				title = jo.getString("title");
			}
			if(jo.has("isConcern")) {
				isConcern = jo.getInt("isConcern");
			}
			if(jo.has("topics")) {
				data = jo.getString("topics");
			}else{
				return;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		TypeToken<List<ShopTopic>> token = new TypeToken<List<ShopTopic>>() {
		};

		shopTopics = GsonParser.getInstance().fromJson(data, token);

		if (shopTopics == null) {
			shopTopics = new ArrayList<ShopTopic>();
		}
	}

	@Override
	protected String method() {
		return "shop/circle/topic";
	}

}
