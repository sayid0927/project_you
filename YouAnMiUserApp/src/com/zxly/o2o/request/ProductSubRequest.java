package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.NewProduct;
import com.zxly.o2o.model.Pakage;
import com.zxly.o2o.model.ProductDTO;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductSubRequest extends BaseRequest {


	/**
	 * 送货上门 有套餐
	 * @param addressId
	 * @param freight
	 * @param productId
	 * @param packageId
	 */
	public  ProductSubRequest(long addressId,float freight,long productId,Pakage productCombo)
	{
		addParams("deliveryType", 2);
		addParams("addressId", addressId);
		addParams("productId", productId);
		addParams("freight", freight);
		addParams("packageId", productCombo.getPakageId());
		addParams("shopId", Config.shopId);
		addParams("products",copy(productCombo.getProductList()));
	}
	/**
	 * 送货上门无套餐
	 * @param addressId
	 * @param freight
	 * @param productId
	 */
	public  ProductSubRequest(long addressId,float freight,NewProduct product)
	{
		addParams("deliveryType", 2);
		addParams("addressId", addressId);
		addParams("productId", product.getId());
		addParams("freight", freight);
		addParams("shopId", Config.shopId);
		List<NewProduct> list=new ArrayList<NewProduct>();
		list.add(product);
		addParams("products", copy(list));
	}
	/**
	 * 到店自取有套餐
	 * @param productId
	 * @param packageId
	 */
	public  ProductSubRequest(long productId,Pakage productCombo)
	{
		addParams("deliveryType", 1);
		addParams("productId", productId);
		addParams("shopId", Config.shopId);
		addParams("packageId", productCombo.getPakageId());
		addParams("products", copy(productCombo.getProductList()));
	}
	/**
	 * 到店自取无套餐
	 * @param productId
	 */
	public  ProductSubRequest(NewProduct product)
	{
		addParams("deliveryType", 1);
		addParams("productId", product.getId());
		addParams("shopId", Config.shopId);
		List<NewProduct> list=new ArrayList<NewProduct>();
		list.add(product);
		addParams("products", copy(list));
	}
	private List<ProductDTO> copy(List<NewProduct> list)
	{
		List<ProductDTO> productList=new ArrayList<ProductDTO>();
		for(NewProduct product:list)
		{
			ProductDTO pro=new ProductDTO();
			pro.setId(product.getId());
			pro.setSkuId(product.getSkuId());
			pro.setPcs(product.getPcs());
			productList.add(pro);
		}
		return productList;
	}
	private String subNo;
	private long createTime;
	private float price;
	

	@Override
	protected void fire(String data) throws AppException {
		try {
			JSONObject jo=new JSONObject(data);
			subNo=jo.getString("subNo");
			createTime=jo.getLong("createTime");
			price=(float) jo.getDouble("price");
		} catch (org.json.JSONException e) {
			throw JSONException(e);
		}
	}
	
	
	public String getSubNo() {
		return subNo;
	}


	public long getCreateTime() {
		return createTime;
	}


	public float getPrice() {
		return price;
	}


	@Override
	protected String method() {
		return "/sub/product";
	}

}
