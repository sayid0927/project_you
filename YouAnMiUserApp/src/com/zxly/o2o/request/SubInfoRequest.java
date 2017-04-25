package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.model.ProductOrder;
import com.zxly.o2o.model.ShopInfo;

import org.json.JSONArray;
import org.json.JSONObject;

public class SubInfoRequest extends BaseRequest {

	 private ProductOrder productOrder;
	public SubInfoRequest(ProductOrder productOrder)
	{
		this.productOrder=productOrder;
		addParams("id",this.productOrder.getId());
	}
	@Override
	protected String method() {
		return "/sub/me";
	}
	@Override
	protected void fire(String data) throws AppException {
		 try {
			 JSONObject jo=new JSONObject(data);
			 if(jo.has("UserAddressDTO"))
			 {
				 JSONObject uaJO=jo.getJSONObject("UserAddressDTO");
				 productOrder.setCsPhone(uaJO.getString("mobilePhone"));
				 productOrder.setCsName(uaJO.getString("name"));
				 productOrder.setCsAddress(uaJO.getString("address"));
				 
			 }
			 if(jo.has("ShopBasicDTO"))
			 {
				 JSONObject shopJO=jo.getJSONObject("ShopBasicDTO");
				 ShopInfo shopInfo=new ShopInfo();
				 shopInfo.setId(shopJO.getLong("id"));
				 shopInfo.setMobilePhone(shopJO.getString("mobilePhone"));
				 shopInfo.setAddress(shopJO.getString("address"));
				 shopInfo.setUserId(shopJO.getLong("userId"));
				 shopInfo.setName(shopJO.getString("name"));
				 if(shopJO.has("headUrl"))
				 {
					 shopInfo.setHeadUrl(shopJO.getString("headUrl"));
				 }
				
				 shopInfo.setIsCollect((byte) shopJO.getInt("isCollect"));
                 if(shopJO.has("imageUrls"))
                 {
                     JSONArray imges=shopJO.getJSONArray("imageUrls");
                     int imgLength=imges.length();
                     String[] imgeUrls=new String[imgLength];
                     for(int i=0;i<imgLength;i++)
                     {
                         String img=imges.getString(i);
                         imgeUrls[i]=img;
                     }
                     shopInfo.setImageUrls(imgeUrls);
                 }

				 productOrder.setShopInfo(shopInfo);
			 }
			 
			
		} catch (org.json.JSONException e) {
			throw JSONException(e);
		}
	}
	

}
