package com.zxly.o2o.request;

/**
 *     @author huangbin  @version 创建时间：2015-1-28 下午3:17:47    类说明: 
 */
public class TopicPraiseRequest extends BaseRequest {
	public TopicPraiseRequest(long topicId, int command) {
		addParams("topicId", topicId);
		addParams("operType", command / 2);
	}

	@Override
	protected String method() {
		return "topic/praise";
	}
}
