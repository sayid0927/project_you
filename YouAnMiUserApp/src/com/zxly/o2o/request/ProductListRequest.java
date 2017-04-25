package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.model.NewProduct;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dsnx
 * @version YIBA-O2O 2014-12-29
 * @since YIBA-O2O
 */
public class ProductListRequest extends BaseRequest {
	private List<NewProduct> productList = new ArrayList<NewProduct>();

	private int total;

	public ProductListRequest(int pageIndex, int pageSize) {
		addParams("pageIndex", pageIndex + "");
		addParams("pageSize", pageSize + "");

	}

	@Override
	protected void fire(String data) throws AppException {
		try {
			JSONObject jo = new JSONObject(data);
			String products = jo.getString("products");
			total = jo.getInt("size");
			TypeToken<List<NewProduct>> type = new TypeToken<List<NewProduct>>() {
			};
			List<NewProduct> list = GsonParser.getInstance().fromJson(products,
					type);
			if (list != null && !list.isEmpty()) {
				productList.addAll(list);
			}
		} catch (JSONException e) {
			throw new AppException("数据解析异常");
		}

	}

	@Override
	protected String method() {
		return null;
	}

	public List<NewProduct> getProductList() {
		return productList;
	}

	public int getTotal() {
		return total;
	}

}
