package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.zxly.o2o.model.ShopArticle;

/**
 *     @author dsnx  @version 创建时间：2015-1-20 上午10:57:46    类说明: 
 */
public class MyHotArticleDetailRequest extends BaseRequest {

	public  ShopArticle shopArticle;


	public MyHotArticleDetailRequest(long articleId) {
		addParams("id", articleId);
	}

	@Override
	protected void fire(String data) throws AppException {
		shopArticle = GsonParser.getInstance().getBean(data, ShopArticle.class);
		if(shopArticle==null)
		shopArticle=new ShopArticle();
	}

	@Override
	protected String method() {
		return "/promote/article/detail";
	}

}
