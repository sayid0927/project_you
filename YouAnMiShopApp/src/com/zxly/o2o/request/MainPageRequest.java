/*
 * 文件名：MainPageRequest.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： MainPageRequest.java
 * 修改人：wuchenhui
 * 修改时间：2015-7-1
 * 修改内容：新增
 */
package com.zxly.o2o.request;

import android.util.Log;

import java.util.List;

import org.json.JSONObject;

import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.TaskInfo;
import com.zxly.o2o.model.Product;
import com.zxly.o2o.model.ShopArticle;
import com.zxly.o2o.model.ShopStatistic;


/**
 * TODO 添加类的一句话简单描述。
 * <p>
 * TODO 详细描述
 * <p>
 * TODO 示例代码
 * <pre>
 * </pre>
 * 
 * @author     wuchenhui
 * @version    YIBA-O2O 2015-7-1
 * @since      YIBA-O2O
 */
public class MainPageRequest extends BaseRequest {
	String shopName="";
	List<ShopStatistic> shopStatistics;
	List<TaskInfo> myTasks;
	
	private List<ShopArticle> articles;
	
	private List<Product> goods;
	
	private List<ShopArticle> industryNews;
	private List<ShopArticle> trains;

	public MainPageRequest(long userId,int userType) {
		addParams("shopId",Account.user.getShopId());
		addParams("userId",userId);
		addParams("userType",userType);
	}
	
	public String getShopName() {
		return shopName;
	}

	public List<TaskInfo> getMyTasks() {
		return myTasks;
	}

	public List<ShopStatistic> getShopStatistics() {
		return shopStatistics;
	}

	public List<ShopArticle> getArticles() {
		return articles;
	}

	public List<Product> getGoods() {
		return goods;
	}

	public List<ShopArticle> getIndustryNews() {
		return industryNews;
	}

	public List<ShopArticle> getTrains() {
		return trains;
	}

	@Override
	protected void fire(String data) {
				
		try {
			JSONObject json = new JSONObject(data);	
			
			shopName=json.optString("shopName","");
			
			try {
					
				if (json.has("shopStatistics")) {
					String str = json.getString("shopStatistics");	
					TypeToken<List<ShopStatistic>> types = new TypeToken<List<ShopStatistic>>() {};
					shopStatistics= GsonParser.getInstance().fromJson(str, types);
				}	
			} catch (Exception e) {

				Log.d("tagShop", "xx-->" + e.toString());
			}

			if (json.has("myTasks")) {
				String str = json.getString("myTasks");				
				TypeToken<List<TaskInfo>> types = new TypeToken<List<TaskInfo>>() {};
				myTasks=GsonParser.getInstance().fromJson(str,types);  
			}
			
			
			if (json.has("articles")) {
				String str = json.getString("articles");				
				TypeToken<List<ShopArticle>> types = new TypeToken<List<ShopArticle>>() {};
				articles=GsonParser.getInstance().fromJson(str,types);  
			}
			
			if (json.has("goods")) {
				String str = json.getString("goods");
				TypeToken<List<Product>> types = new TypeToken<List<Product>>() {};
				goods=GsonParser.getInstance().fromJson(str,types);
			}
			
			if (json.has("industryNews")) {
				String str = json.getString("industryNews");				
				TypeToken<List<ShopArticle>> types = new TypeToken<List<ShopArticle>>() {};
				industryNews=GsonParser.getInstance().fromJson(str,types);  
			}
			
			if (json.has("trains")) {
				String str = json.getString("trains");				
				TypeToken<List<ShopArticle>> types = new TypeToken<List<ShopArticle>>() {};
				trains=GsonParser.getInstance().fromJson(str,types);  
			}

		} catch (Exception e) {

		}

	}

	@Override
	public String getUrl() {
		String url = super.getUrl();
		Log.d("url", "-->" + url);
		//http://192.168.1.8/mockjs/3/shopIndex?shopId=&areaName=&brandName=
		return "http://192.168.1.8/mockjs/3/shopIndex?shopId=&areaName=&brandName=";
	}
	
	@Override
	protected String method() {
		return "/shopApp/index";
	}

}
