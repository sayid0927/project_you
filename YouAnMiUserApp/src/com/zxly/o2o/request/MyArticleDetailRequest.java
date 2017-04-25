package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.zxly.o2o.model.ArticleReply;
import com.zxly.o2o.model.ShopArticle;

import java.util.ArrayList;

/**
 *     @author dsnx  @version 创建时间：2015-1-20 上午10:57:46    类说明: 
 */
public class MyArticleDetailRequest extends MyCircleRequest {


	public MyArticleDetailRequest(long articleId) {
		addParams("id", articleId);
	}

	@Override
	protected void fire(String data) throws AppException {
		ShopArticle myShopArticle = GsonParser.getInstance().getBean(data, ShopArticle.class);
		if(shopArticle==null)
		shopArticle=new ShopArticle();
		articleReplys = new ArrayList<ArticleReply>();
		shopArticle.setIsCollected(myShopArticle.getIsCollected());
		shopArticle.setIsPraise(myShopArticle.getIsPraise());
		shopArticle.setIsOppose(myShopArticle.getIsOppose());
		articleReplys.addAll(myShopArticle.getReplys());
		myShopArticle.getReplys().clear();
	}

	@Override
	protected String method() {
		return "/article/me";
	}

}
