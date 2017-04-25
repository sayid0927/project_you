package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.model.Filters;
import com.zxly.o2o.model.Paging;
import com.zxly.o2o.model.ShopArticle;

import java.util.ArrayList;
import java.util.List;

/**
 *     @author dsnx  @version 创建时间：2015-1-19 上午11:18:52    类说明: 
 */
public class ArticleListRequest extends MyCircleRequest {

	public ArticleListRequest(int page, String value, long shopId) {
		addParams("paging", new Paging(page, 0));
		Filters[] filter = null;
		if (value != null) {
			filter = new Filters[2];
			// 文章类型
			filter[1] = new Filters(5, value);
		} else {
			filter = new Filters[1];
		}
		// shopid
		filter[0] = new Filters(2, shopId);
		addParams("filters", filter);
	}

	@Override
	protected void fire(String data) throws AppException {
		TypeToken<List<ShopArticle>> token = new TypeToken<List<ShopArticle>>() {
		};
		articles = GsonParser.getInstance().fromJson(data, token);

		if (articles == null) {
			articles = new ArrayList<ShopArticle>();
		}
	}

	@Override
	protected String method() {
		// TODO Auto-generated method stub
		return "article/list";
	}

}
