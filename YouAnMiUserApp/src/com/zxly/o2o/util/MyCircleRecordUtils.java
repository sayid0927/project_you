package com.zxly.o2o.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.zxly.o2o.model.MyCircleArticleBV;
import com.zxly.o2o.model.MyCircleArticleReplyBV;
import com.zxly.o2o.model.MyCircleTopicBV;

import java.util.ArrayList;
import java.util.List;

/**
 * 包含操作 {@code JSON} 数据的常用方法的工具类。
 * <p />
 * 该工具类使用的 {@code JSON} 转换引擎是 {@code Google Gson}</a>。 下面是工具类的使用案例：
 * 
 */
public class MyCircleRecordUtils {

	private static Gson gson = null;

	static {
		if (gson == null) {
			gson = new Gson();
		}
	}

	/** 将对象转换成json格式 */
	public  String objectToJson(Object ts) {
		String jsonStr = null;
		if (gson != null) {
			jsonStr = gson.toJson(ts);
		}
		return jsonStr;
	}

	/** 将json格式转换成list对象 */
	public  List<MyCircleTopicBV> jsonToList(String jsonStr) {
		List<MyCircleTopicBV> objList = null;
		if (gson != null) {
			java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<List<MyCircleTopicBV>>() {
			}.getType();
			objList = gson.fromJson(jsonStr, type);
		}
		return objList;
	}

	public  List<MyCircleArticleBV> jsonToList2(String jsonStr) {
		List<MyCircleArticleBV> objList = null;
		if (gson != null) {
			java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<List<MyCircleArticleBV>>() {
			}.getType();
			objList = gson.fromJson(jsonStr, type);
		}
		return objList;
	}
	
	public  List<MyCircleArticleReplyBV> jsonToList3(String jsonStr) {
		List<MyCircleArticleReplyBV> objList = null;
		if (gson != null) {
			java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<List<MyCircleArticleReplyBV>>() {
			}.getType();
			objList = gson.fromJson(jsonStr, type);
		}
		return objList;
	}

	/**
	 * 设置TopicBVRecord
	 */
	public  void setTopicBVRecord(long id, int isDo) {
		MyCircleTopicBV actionBV = new MyCircleTopicBV();
		actionBV.setId(id);
		actionBV.setUp(isDo == 0 ? false : true);

		List<MyCircleTopicBV> list = new ArrayList<MyCircleTopicBV>();
		String record = PreferUtil.getInstance().getMyCircleTopicBV();
		// Log.d("==", "====MyCircleTopicBV record is====" + record);
		if (!TextUtils.isEmpty(record))
			list = (List<MyCircleTopicBV>) jsonToList(record);
		for (MyCircleTopicBV item : list) {
			if (item.getId() == actionBV.getId()) {
				list.remove(item);
				break;
			}
		}
		list.add(actionBV);
		String jsonString = objectToJson(list);
		PreferUtil.getInstance().setMyCircleTopicBV(jsonString);
		// Log.e("==", "====MyCircleTopicBV record is====" + jsonString);
	}

	public  boolean getTopicBV(long topicId) {
		List<MyCircleTopicBV> list = new ArrayList<MyCircleTopicBV>();
		String record = PreferUtil.getInstance().getMyCircleTopicBV();
		if (!TextUtils.isEmpty(record))
			list = (List<MyCircleTopicBV>) jsonToList(record);

		for (MyCircleTopicBV item : list) {
			if (item.getId() == topicId) {
				return item.getUp();
			}
		}
		return false;
	}

	/**
	 * 设置文章bv
	 */
	public  void setArticleBVRecord(long id, int isUp,int isDown,int isCollected) {
		MyCircleArticleBV actionBV = new MyCircleArticleBV();
		actionBV.setId(id);
		actionBV.setUp(isUp == 0 ? false : true);
		actionBV.setDown(isDown == 0 ? false : true);
		actionBV.setCollected(isCollected == 0 ? false : true);

		List<MyCircleArticleBV> list = new ArrayList<MyCircleArticleBV>();
		String record = PreferUtil.getInstance().getMyCircleArticleBV();
		 AppLog.e("==", "====MyCircleTopicBV record is====" + record);
		if (!TextUtils.isEmpty(record))
			list = (List<MyCircleArticleBV>) jsonToList2(record);
		for (MyCircleArticleBV item : list) {
			if (item.getId() == actionBV.getId()) {
				list.remove(item);
				break;
			}
		}
		list.add(actionBV);
		String jsonString = objectToJson(list);
		PreferUtil.getInstance().setMyCircleArticleBV(jsonString);
		 AppLog.e("==", "====MyCircleTopicBV record is====" + jsonString);
	}

	public  MyCircleArticleBV getArticleBV(long articleId) {
		List<MyCircleArticleBV> list = new ArrayList<MyCircleArticleBV>();
		String record = PreferUtil.getInstance().getMyCircleArticleBV();
		if (!TextUtils.isEmpty(record))
			list = (List<MyCircleArticleBV>) jsonToList2(record);

		for (MyCircleArticleBV item : list) {
			if (item.getId() == articleId) {
				return item;
			}
		}
		return null;
	}
	
}