package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.model.Paging;
import com.zxly.o2o.model.ShopTopic;

import java.util.ArrayList;
import java.util.List;

/**
 *     @author huangbin  @version 创建时间：2015-2-4 下午2:10:22    类说明: 
 */
public class MyTopicsRequest extends MyCircleRequest {

	public MyTopicsRequest(int page) {
		addParams("paging", new Paging(page, 0));
	}

	@Override
	protected void fire(String data) throws AppException {
		TypeToken<List<ShopTopic>> token = new TypeToken<List<ShopTopic>>() {
		};
		shopTopics = GsonParser.getInstance().fromJson(data, token);

		if (shopTopics == null) {
			shopTopics = new ArrayList<ShopTopic>();
		}
	}

	@Override
	protected String method() {
		// TODO Auto-generated method stub
		return "topic/my/sends";
	}

}
