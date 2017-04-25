package com.zxly.o2o.request;

/**
 *     @author huangbin  @version 创建时间：2015-1-26 下午4:21:40    类说明: 
 */
public class ArticleOperateRequest extends BaseRequest {

	public ArticleOperateRequest(long articleId, long platformArticleId, int command) {
		addParams("articleId", articleId);
		if (platformArticleId != -1)
			addParams("platformArticleId", platformArticleId);
		addParams("operType", command);
	}

	@Override
	protected String method() {
		return "article/operate";
	}

}
