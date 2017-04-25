package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.NewProduct;
import com.zxly.o2o.model.Pakage;
import com.zxly.o2o.model.ProductParam;
import com.zxly.o2o.model.ProductSKUParam;
import com.zxly.o2o.model.ProductSKUValue;
import com.zxly.o2o.model.Skus;
import com.zxly.o2o.model.User;
import com.zxly.o2o.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *     @author dsnx  @version 创建时间：2015-1-19 下午4:49:15    类说明: 
 */
	public class ProductInfoRequest extends BaseRequest {

	private String[] imageUrls;
	private List<ProductParam> productParamList = new ArrayList<ProductParam>();
	private List<Pakage> packageList = new ArrayList<Pakage>();
	private List<String> labelList = new ArrayList<String>();
	private  String[] shopDiscount;
	private int isCollect;
	private int isLike;//1是0否
	private int isShare;//1是0否
	private String selDesc;
	private NewProduct product;
	private float minPrice;
	private float maxPrice;
	public String content;
	private int collectAmount;
	private int likeAmount;
	private int  shareAmount;
	private List<User> listUser =new ArrayList<User>();

	public ProductInfoRequest(NewProduct product,int bannerId) {
		this.product = product;
		addParams("id", product.getId());
		addParams("shopId", Config.shopId);
		if(bannerId>0)
		{
			addParams("bannerId",bannerId);
		}
			
	}

	public String[] getImageUrls() {
		return imageUrls;
	}


	public List<ProductParam> getProductParamList() {
		return productParamList;
	}

	public List<String> getLabelList() {
		return labelList;
	}

	public List<Pakage> getPackageList() {
		return packageList;
	}

	public String[] getShopDiscount() {
		return shopDiscount;
	}

	public int getIsCollect() {
		return isCollect;
	}

	public int getIsLike() {
		return isLike;
	}

	public String getSelDesc() {
		return selDesc;
	}

	public List<User> getListUser() {
		return listUser;
	}

	public int getShareAmount() {
		return shareAmount;
	}

	public int getEnjoyAmount() {
		return likeAmount;
	}

	public int getCollectAmount() {
		return collectAmount;
	}

	@Override
	protected String method() {
		return "/product/me";
	}

	@Override
	protected void fire(String data) throws AppException {
		try {
			JSONObject jo = new JSONObject(data);
			product.getProductSKUParamList().clear();
			product.getSkuList().clear();
			product.setSelSku(null);
		
			isCollect = jo.getInt("isCollect");
			product.setCollect(isCollect);
			isLike=jo.optInt("isLike");
			isShare=jo.optInt("isShare");
			product.setName(jo.getString("name"));
			product.setHeadUrl(jo.getString("headUrl"));
			product.setAmount(jo.optInt("amount"));

			collectAmount=jo.optInt("collectAmount");
			likeAmount=jo.optInt("likeAmount");
			shareAmount=jo.optInt("shareAmount");
			if(jo.has("shopDiscount"))
			{
				JSONArray jsonArray=jo.getJSONArray("shopDiscount");
				int length=jsonArray.length();
				if(length>0)
				{
					shopDiscount=new String[length>=2?2:1];
					for(int i=0;i<length;i++)
					{
						if(i>=2)
						{
							return ;
						}
						JSONObject jsonObject=jsonArray.getJSONObject(i);
						int discountType=jsonObject.getInt("discountType");
						String title=jsonObject.getString("title");
						StringBuilder sb=new StringBuilder();
						sb.append(discountType).append(",").append(title);
						shopDiscount[i]=sb.toString();
					}
				}


			}
			if(jo.has("enjoyMens"))
			{
				JSONArray userArray=jo.getJSONArray("enjoyMens");
				int length=userArray.length();
				for(int i=0;i<length;i++)
				{
					JSONObject joUser=userArray.getJSONObject(i);
					User user=new User();
					user.setId(joUser.getInt("userId"));
					user.setName(joUser.optString("name"));
					user.setOprateTime(joUser.optLong("opTime"));
					user.setType((byte) joUser.getInt("type"));
					user.setSignature(joUser.optString("sign"));
					user.setThumHeadUrl(joUser.optString("imageUrl"));
					listUser.add(user);
				}

			}
			if(jo.has("shareUrl"))
			{
				product.setShareUrl(jo.getString("shareUrl"));
			}
			if(jo.has("desc"))
			{
				product.setDesc(jo.getString("desc"));
			}
			if(jo.has("content"))
			{
				content=jo.getString("content");
			}
			float price = (float) jo.getDouble("price");
			if (jo.has("preference")) {
				float preference = (float) jo.getDouble("preference");
				product.setPreference(preference);
			}
			product.setPrice(price);
			if (jo.has("typeCode")) {
				int type_code = jo.getInt("typeCode");
				product.setTypeCode(type_code);
				if (type_code == 1)// 限时抢
				{
					product.setResidueTime(jo.getLong("residueTime"));
				}
			}else{
				product.setTypeCode(0);
				product.setPreference(0);
				product.setResidueTime(0);
			}

			product.setIsParamType(jo.getInt("isParamTye"));
			String imgArray = jo.getString("imageUrls");
			imageUrls =imgArray.split(",");
			if (jo.has("labels")) {
				JSONArray lables = jo.getJSONArray("labels");
				int lableLength = lables.length();
				for (int i = 0; i < lableLength; i++) {
					JSONObject lablesJo = lables.getJSONObject(i);
					labelList.add(lablesJo.getString("name"));
				}
			}

			JSONArray paramsArray;
			switch (product.getParamType()) {
			case 1:
				if (jo.has("complexParams")) {
					paramsArray = jo.getJSONArray("complexParams");
					int paramLength = paramsArray.length();
					for (int i = 0; i < paramLength; i++) {
						JSONObject paramJo = paramsArray.getJSONObject(i);
						int orderId = paramJo.optInt("orderId");
						JSONArray childParamArray = paramJo
								.getJSONArray("params");
						parserParam(childParamArray, orderId);

					}
				}

				break;
			case 2:
				if (jo.has("productParam")) {
					paramsArray = jo.getJSONArray("productParam");
					parserParam(paramsArray, 1);
				}
				break;
			}

			if (jo.has("packages")) {
				JSONArray packArray = jo.getJSONArray("packages");
				int packLength = packArray.length();
				for (int k = 0; k < packLength; k++) {
					Pakage productCombo = new Pakage();
					JSONObject _jo=packArray.getJSONObject(k);
					productCombo.setPakageId(_jo.getInt("id"));
					productCombo
							.setName("套餐" + StringUtil.foematInteger(k + 1));
					productCombo.setNameId(k + 1);
					if(_jo.has("maxPreferece"))
					{
						float _maxPrefPrice=(float) _jo.getDouble("maxPreferece");
						productCombo.setMaxPrefPrice(_maxPrefPrice);
					}
					packageList.add(productCombo);
				}
			}

			JSONArray skuParamArray = jo.getJSONArray("skuParams");
			int skuParamLength = skuParamArray.length();
			for (int i = 0; i < skuParamLength; i++) {
				JSONObject pspJson = skuParamArray.getJSONObject(i);
				ProductSKUParam productSkuParam = new ProductSKUParam();
				productSkuParam.setId(pspJson.getInt("id"));
				productSkuParam
						.setDisplayName(pspJson.getString("displayName"));
				JSONArray productSkuValueArray = pspJson
						.getJSONArray("productSKUValueDTO");
				int skuVlength = productSkuValueArray.length();
				ProductSKUValue[] productSKUValue = new ProductSKUValue[skuVlength];
				for (int j = 0; j < skuVlength; j++) {
					JSONObject skuValueJson = productSkuValueArray
							.getJSONObject(j);
					ProductSKUValue pskuValue = new ProductSKUValue();
					pskuValue.setId(skuValueJson.getInt("id"));
					pskuValue.setDisplyName(skuValueJson
							.getString("displyName"));
					pskuValue.setParamValue(skuValueJson
							.getString("paramValue"));
					productSKUValue[j] = pskuValue;
				}
				productSkuParam.setProductSKUValue(productSKUValue);
				product.addProductSKUParam(productSkuParam);
			}

			StringBuilder str = new StringBuilder();
			for (int i = 0; i < skuParamLength; i++) {
				ProductSKUParam productSKUParam = product.getProductSKUParamList().get(i);
				str.append(productSKUParam.getDisplayName());
				if (i < skuParamLength - 1) {
					str.append("，");
				}
			}
			selDesc = str.toString();
			JSONArray skusArray = jo.getJSONArray("skus");
			int skusLength = skusArray.length();
			for (int i = 0; i < skusLength; i++) {
				JSONObject skuJo = skusArray.getJSONObject(i);
				Skus skus = new Skus();
				skus.setId(skuJo.getLong("skuId"));
				skus.setPrice((float) skuJo.getDouble("price"));
				skus.setParamComValues(skuJo.getString("paramComValues"));
				product.addSku(skus);
				if(i==0)
				{
					minPrice=skus.getPrice();
					maxPrice=minPrice;
				}else
				{
					if(skus.getPrice()<minPrice)
					{
						minPrice=skus.getPrice();
					}
					if(skus.getPrice()>maxPrice)
					{
						maxPrice=skus.getPrice();
					}
				}
			}
			//此段代码只能放在这里 请不要移动 要不然会导致价格区间不正确
			product.setPrice(minPrice);
			product.setMaxPrice(maxPrice);
		} catch (org.json.JSONException e) {
			throw JSONException(e);
		}
	}

	public void parserParam(JSONArray array, int orderId)
			throws org.json.JSONException {
		if (array == null)
			return;
		int cpLength = array.length();
		for (int j = 0; j < cpLength; j++) {
			JSONObject childParam = array.getJSONObject(j);
			ProductParam pParam = new ProductParam();
			pParam.setDisplayName(childParam.getString("displayName"));
			pParam.setIsMust(childParam.getInt("isMust"));
			pParam.setParamType(orderId);
			pParam.setParamValue(childParam.optString("paramValue",""));
			productParamList.add(pParam);
		}
	}


}
