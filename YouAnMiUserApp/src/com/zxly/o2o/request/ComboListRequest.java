package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.model.NewProduct;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ComboListRequest extends BaseRequest {

	private List<NewProduct> productList=new ArrayList<NewProduct>();
	private long curProductId;
	public ComboListRequest(long id,long productId)
	{
		addParams("id", id);
		this.curProductId=productId;
	}
	
	@Override
	protected void fire(String data) throws AppException {
		try {
			JSONArray array=new JSONArray(data);
			int length=array.length();
			for(int i=0;i<length;i++)
			{
				JSONObject jo=array.getJSONObject(i);
				long productId=jo.getLong("productId");
				if(curProductId==productId)
				{
					continue;
				}
				NewProduct product=new NewProduct();
				product.setId(productId);
				product.setName(jo.getString("productName"));
				product.setPrice((float) jo.getDouble("price"));
				product.setHeadUrl(jo.getString("headUrl"));
				productList.add(product);
			}
		} catch (org.json.JSONException e) {
			throw JSONException(e);
		}
		
	}

	
	public List<NewProduct> getProductList() {
		return productList;
	}

	@Override
	protected String method() {
		return "package/product";
	}

}
