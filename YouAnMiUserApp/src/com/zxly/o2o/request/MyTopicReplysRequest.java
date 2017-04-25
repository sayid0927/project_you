package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.model.Paging;
import com.zxly.o2o.model.TopicReply;

import java.util.ArrayList;
import java.util.List;

/**
 *     @author huangbin  @version 创建时间：2015-2-4 下午2:14:18    类说明: 
 */
public class MyTopicReplysRequest extends MyCircleRequest {

	public MyTopicReplysRequest(int page) {
		addParams("paging", new Paging(page, 0));
	}

	@Override
	protected void fire(String data) throws AppException {
		TypeToken<List<TopicReply>> token = new TypeToken<List<TopicReply>>() {
		};
		topicReplys = GsonParser.getInstance().fromJson(data, token);

		if (topicReplys == null) {
			topicReplys = new ArrayList<TopicReply>();
		}
	}

	@Override
	protected String method() {
		// TODO Auto-generated method stub
		return "topic/my/replys";
	}

}
