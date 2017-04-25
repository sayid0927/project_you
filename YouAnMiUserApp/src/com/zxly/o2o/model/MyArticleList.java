package com.zxly.o2o.model;

import java.util.ArrayList;
import java.util.List;

/**
 *     @author dsnx  @version 创建时间：2015-1-19 下午2:16:10    类说明: 
 */
public class MyArticleList {

	private List<ShopArticle> articles = new ArrayList<ShopArticle>();

	public List<ShopArticle> getArticles() {
		return articles;
	}

	public void setArticles(List<ShopArticle> articles) {
		this.articles = articles;
	}

}
