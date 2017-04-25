package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.BuyItem;
import com.zxly.o2o.model.NewProduct;
import com.zxly.o2o.model.Skus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsnx on 2015/5/25.
 */
public class ChartProductListRequest extends BaseRequest {
	private List<BuyItem> productList=new ArrayList<BuyItem>();
    public ChartProductListRequest(List<BuyItem> productList)
    {
        this.productList=productList;
        addParams("userId", Account.user.getId());
        addParams("shopId", Config.shopId);
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
        	JSONObject jData=new JSONObject(data);
        	JSONArray ja=jData.getJSONArray("buyItems");
        	int length=ja.length();
			Account.orderCount=length;
        	for(int i=0;i<length;i++)
        	{
        		JSONObject jo=ja.getJSONObject(i);
        		BuyItem shopingItem=new BuyItem();
        		shopingItem.setItemId(jo.getString("itemId"));
        		shopingItem.setType(jo.optInt("type"));
        		shopingItem.setPrice((float) jo.getDouble("price"));
        		shopingItem.setPcs(jo.getInt("pcs"));
        		shopingItem.setId(jo.getInt("id"));
        		if(shopingItem.getType()==3)
        		{
        			shopingItem.setPakageId(jo.getLong("pakageId"));
        		}
        		
        		JSONArray arrayProduct=jo.getJSONArray("products");
        		int pLength=arrayProduct.length();
				float  totalPrefPrice=0;
        		for(int k=0;k<pLength;k++)
        		{
        			NewProduct newProduct=new NewProduct();
        			JSONObject jp=arrayProduct.getJSONObject(k);
        			newProduct.setId(jp.getLong("productId"));
        			newProduct.setSkuId(jp.getLong("skuId"));
        			newProduct.setPcs(shopingItem.getPcs());
        			newProduct.setBuyItemId(shopingItem.getItemId());
        			newProduct.setTypeCode(shopingItem.getType());
        			if(shopingItem.getType()==3)
        			{
        				newProduct.setPakageId(shopingItem.getPakageId());
        			}
        			Skus  skus=new Skus();
        			skus.setId(newProduct.getSkuId());
        			float price=(float) jp.getDouble("price");
        			skus.setPrice(price);
					newProduct.setPreference((float) jp.optDouble("preference",0));
					totalPrefPrice=totalPrefPrice+newProduct.getPreference();
					shopingItem.setPrefPrice(totalPrefPrice);
					newProduct.setSelSku(skus);
        			newProduct.setHeadUrl(jp.getString("headUrl"));
        			newProduct.setName(jp.getString("name"));
        			newProduct.setPrice(price);
        			newProduct.setRemark(jp.getString("remark"));
        			shopingItem.addProduct(newProduct);
        			
        		}
        		productList.add(shopingItem);
        	}
        } catch (JSONException e) {
            throw JSONException(e);
        }

    }

    @Override
    protected String method() {
        return "/order/shoppingCart/list";
    }
}
