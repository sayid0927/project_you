package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.model.NewProduct;
import com.zxly.o2o.model.ProductBrand;
import com.zxly.o2o.model.ProductType;
import com.zxly.o2o.model.SortItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *     @author dsnx  @version 创建时间：2015-1-6 下午7:12:20     类说明: 全部商品初始化接口
 */
public class AllProductListInitRequest extends BaseRequest {

	private List<NewProduct> productList = new ArrayList<NewProduct>();
	private List<ProductType> productTypeList = new ArrayList<ProductType>();
	private List<ProductBrand> productBrandList = new ArrayList<ProductBrand>();
	private List<SortItem> sortItemList=new ArrayList<SortItem>();
	

	public AllProductListInitRequest(long shopId) {
		addParams("id", shopId);
	}

	@Override
	protected void fire(String data) throws AppException {
		try {
			JSONObject json = new JSONObject(data);
			TypeToken<List<NewProduct>> type1 = new TypeToken<List<NewProduct>>() {
			};
			if(json.has("products"))
			{
				String products = json.getString("products");
				List<NewProduct> pList = GsonParser.getInstance().fromJson(products,
						type1);
				if (!listIsEmpty(pList)) {
					productList.addAll(pList);
				}
			}
			if(json.has("classes"))
			{
				String classes = json.getString("classes");
				TypeToken<List<ProductType>> type2 = new TypeToken<List<ProductType>>() {
				};
				List<ProductType> ptList = GsonParser.getInstance().fromJson(
						classes, type2);
				if (!listIsEmpty(ptList)) {
					productTypeList.addAll(ptList);
				}

			}
			if(json.has("brands"))
			{
				String brands = json.getString("brands");
				TypeToken<List<ProductBrand>> type3 = new TypeToken<List<ProductBrand>>() {
				};
				List<ProductBrand> pbList = GsonParser.getInstance().fromJson(
						brands, type3);
				
				if (!listIsEmpty(pbList)) {
					productBrandList.addAll(pbList);
				}
			}
			if(json.has("sorts"))
			{
				String sorts=json.getString("sorts");
				TypeToken<List<SortItem>> typeSort = new TypeToken<List<SortItem>>() {
				};
				List<SortItem> sList=GsonParser.getInstance().fromJson(sorts, typeSort);
				if(!listIsEmpty(sList))
				{
					sortItemList.addAll(sList);
				}
			}
			
			
			
		} catch (JSONException e) {
			throw JSONException(e);
		}

	}


	@Override
	protected String method() {

		return "product/list/init";
	}

	public List<NewProduct> getProductList() {
		return productList;
	}

	public void setProductList(List<NewProduct> productList) {
		this.productList = productList;
	}

	public List<ProductType> getProductTypeList() {
		return productTypeList;
	}

	public void setProductTypeList(List<ProductType> productTypeList) {
		this.productTypeList = productTypeList;
	}

	public List<ProductBrand> getProductBrandList() {
		return productBrandList;
	}

	public void setProductBrandList(List<ProductBrand> productBrandList) {
		this.productBrandList = productBrandList;
	}

	public List<SortItem> getSortItemList() {
		return sortItemList;
	}

	public void setSortItemList(List<SortItem> sortItemList) {
		this.sortItemList = sortItemList;
	}

	

}
