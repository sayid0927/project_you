/*
 * 文件名：SecondhandProductIssueRequest.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： SecondhandProductIssueRequest.java
 * 修改人：wuchenhui
 * 修改时间：2015-3-17
 * 修改内容：新增
 */
package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.DataDictionary;
import com.zxly.o2o.model.ProductType;
import com.zxly.o2o.model.ShopInfo;
import com.zxly.o2o.model.UsedDescType;
import com.zxly.o2o.util.AppLog;
import com.zxly.o2o.util.AreaUtil;

import org.json.JSONObject;

import java.util.List;

/**
 * TODO 添加类的一句话简单描述。
 * <p>
 * TODO 详细描述
 * <p>
 * TODO 示例代码
 * 
 * <pre>
 * </pre>
 * 
 * @author wuchenhui
 * @version YIBA-O2O 2015-3-17
 * @since YIBA-O2O
 */
public class SecondhandIssueRequest extends BaseRequest {

	private List<ProductType> productTypes;
	private List<DataDictionary> deprs; //新旧程度类型
	private List<UsedDescType> descTypes;   //物品概况类型
	private ShopInfo shop;
	private int cityId=-1;
	private int provinceId=-1;
	
	
	public SecondhandIssueRequest(int issueType) {
		addParams("id", Config.shopId);
		addParams("type",issueType);
		
	}
	public SecondhandIssueRequest() {
		addParams("id", Config.shopId);
	}
	
	public void setIssueType(int issueType){
		addParams("type",issueType);
	}

	@Override
	protected void fire(String data) throws AppException {
		try {
			
			JSONObject json = new JSONObject(data);
			TypeToken<List<ProductType>> types1 = new TypeToken<List<ProductType>>() {};
			if (json.has("classes")) {
				String products = json.getString("classes");
				productTypes = GsonParser.getInstance().fromJson(products,types1);
				AppLog.d("classes", "classes-->" + productTypes.size());
			}

			TypeToken<List<DataDictionary>> types2 = new TypeToken<List<DataDictionary>>() {};
			if (json.has("deprs")) {
				String dataDictionary = json.getString("deprs");
				deprs = GsonParser.getInstance().fromJson(dataDictionary,types2);
				AppLog.d("classes", "deprs-->" + deprs.size());
			}

			TypeToken<List<UsedDescType>> types3= new TypeToken<List<UsedDescType>>() {};
			if (json.has("descTypes")) {
				String dataDictionary = json.getString("descTypes");
				descTypes = GsonParser.getInstance().fromJson(dataDictionary,types3);
				AppLog.d("classes", "descTypes-->" + descTypes.size());
			}
			
			cityId=json.optInt("cityId",-1);
			provinceId=json.optInt("provinceId",-1);

			if (Account.areaList == null) {
				Account.areaList = AreaUtil.getAreaFromFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	@Override
	protected boolean isShowLoadingDialog() {
		return true;
	}
	
	@Override
	protected String method() {

		return "/used/init";
	}

	public List<ProductType> getProductTypes() {
		return productTypes;
	}
	public List<DataDictionary> getDeprs() {
		return deprs;
	}
	public List<UsedDescType> getDescTypes() {
		return descTypes;
	}
	public ShopInfo getShop() {
		return shop;
	}
	public int getCityId() {
		return cityId;
	}
	public int getProvinceId() {
		return provinceId;
	}


	
	
	
}
