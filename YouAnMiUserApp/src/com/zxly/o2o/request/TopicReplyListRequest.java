package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.model.Paging;
import com.zxly.o2o.model.TopicReply;

import java.util.ArrayList;
import java.util.List;

/**
 *     @author huangbin  @version 创建时间：2015-1-24 下午6:08:04    类说明: 
 */
public class TopicReplyListRequest extends MyCircleRequest {
	public TopicReplyListRequest(int page, long articleId) {
		addParams("paging", new Paging(page, 0));
		addParams("id", articleId);
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
		return "topic/reply/list";
	}
}