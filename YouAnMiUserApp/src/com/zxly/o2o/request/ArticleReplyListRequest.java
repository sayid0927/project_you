package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.model.ArticleReply;
import com.zxly.o2o.model.Paging;

import java.util.ArrayList;
import java.util.List;

/**
 *     @author huangbin  @version 创建时间：2015-1-21 下午2:59:05    类说明: 
 */
public class ArticleReplyListRequest extends MyCircleRequest {
	

	public ArticleReplyListRequest(int page, long articleId) {
		addParams("paging", new Paging(page, 0));
		addParams("id", articleId);
	}

	@Override
	protected void fire(String data) throws AppException {
		TypeToken<List<ArticleReply>> token = new TypeToken<List<ArticleReply>>() {
		};

		articleReplys = GsonParser.getInstance().fromJson(data, token);

		if (articleReplys == null) {
			articleReplys = new ArrayList<ArticleReply>();
			return;
		}
		
	}

	@Override
	protected String method() {
		return "article/reply/list";
	}
}
