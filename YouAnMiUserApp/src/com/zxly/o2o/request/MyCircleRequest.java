package com.zxly.o2o.request;

import com.zxly.o2o.model.ArticleReply;
import com.zxly.o2o.model.MyCirclePage;
import com.zxly.o2o.model.ShopArticle;
import com.zxly.o2o.model.ShopTopic;
import com.zxly.o2o.model.TopicReply;

import java.util.List;

/**
 *     @author huangbin  @version 创建时间：2015-1-24 下午2:07:34    类说明: 
 */
public abstract class MyCircleRequest extends BaseRequest {
	
	//文章
	public static ShopArticle shopArticle;
	public List<ShopArticle> articles;
	public List<ArticleReply> articleReplys;
	
	
	//圈子首页
	public MyCirclePage myCirclePageData;
	
	
	//论坛帖子
	public static ShopTopic shopTopic;
	public static ShopTopic publishTopic;
	public List<ShopTopic> shopTopics;
	public List<TopicReply> topicReplys;
}
