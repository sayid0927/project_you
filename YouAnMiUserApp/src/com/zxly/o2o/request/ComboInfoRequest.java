package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.model.NewProduct;
import com.zxly.o2o.model.ProductSKUParam;
import com.zxly.o2o.model.ProductSKUValue;
import com.zxly.o2o.model.Skus;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ComboInfoRequest extends BaseRequest {

	private List<NewProduct> productList = new ArrayList<NewProduct>();
	private long productId;
	private NewProduct product;
	private long pakageId;

	public ComboInfoRequest(long pakageId, long productId) {
		this.pakageId=pakageId;
		addParams("id", pakageId);
		this.productId = productId;
	}

	@Override
	protected void fire(String data) throws AppException {
		try {
			JSONArray ja = new JSONArray(data);
			int length = ja.length();
			for (int w = 0; w < length; w++) {
				JSONObject jo = ja.getJSONObject(w);
				NewProduct _product = new NewProduct();
				_product.setName(jo.getString("name"));
				_product.setId(jo.getInt("id"));
				_product.setPrice((float) jo.getDouble("price"));
				_product.setPreference((float) jo.getDouble("preference"));
				_product.setHeadUrl(jo.getString("headUrl"));
				_product.setPakageId(pakageId);
				_product.setTypeCode(3);
				if (jo.has("skuParams")) {
					JSONArray skuParamArray = jo.getJSONArray("skuParams");
					int skuParamLength = skuParamArray.length();
					for (int i = 0; i < skuParamLength; i++) {
						JSONObject pspJson = skuParamArray.getJSONObject(i);
						ProductSKUParam productSkuParam = new ProductSKUParam();
						productSkuParam.setId(pspJson.getInt("id"));
						productSkuParam.setDisplayName(pspJson
								.getString("displayName"));
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
						_product.addProductSKUParam(productSkuParam);

					}
					JSONArray skusArray = jo.getJSONArray("skus");
					int skusLength = skusArray.length();
					float minPrice = 0;
					float maxPrice = 0;
					for (int i = 0; i < skusLength; i++) {
						JSONObject skuJo = skusArray.getJSONObject(i);
						Skus skus = new Skus();
						skus.setId(skuJo.getLong("skuId"));
						skus.setPrice((float) skuJo.getDouble("price"));
						skus.setParamComValues(skuJo
								.getString("paramComValues"));
						_product.addSku(skus);
						if (i == 0) {
							minPrice = skus.getPrice();
							maxPrice = minPrice;
						} else {
							if (skus.getPrice() < minPrice) {
								minPrice = skus.getPrice();
							}
							if (skus.getPrice() > maxPrice) {
								maxPrice = skus.getPrice();
							}
						}
					}
					_product.setMaxPrice(maxPrice);
				}
				if (productId == _product.getId()) {
					product = _product;

				}else
				{
					productList.add(_product);
				}
				

			}

		} catch (Exception e) {
			throw new AppException("数据解析异常");
		}

	}

	public NewProduct getProduct() {
		return product;
	}

	public void setProduct(NewProduct product) {
		this.product = product;
	}

	@Override
	protected String method() {
		return "/package/product";
	}

	public List<NewProduct> getProductList() {
		return productList;
	}

}
