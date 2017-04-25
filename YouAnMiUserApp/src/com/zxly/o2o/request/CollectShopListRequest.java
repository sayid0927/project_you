package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.model.Paging;
import com.zxly.o2o.model.ShopInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dsnx
 * @version YIBA-O2O 2014-12-29
 * @since YIBA-O2O
 */
public class CollectShopListRequest extends BaseRequest {
	private List<ShopInfo> shopList = new ArrayList<ShopInfo>();

	private int total;

	public CollectShopListRequest(int index, int size) {
		Paging paging = new Paging(index, size);
		addParams("paging", paging);
	}

	@Override
	protected void fire(String data) throws AppException {
		try {
			TypeToken<List<ShopInfo>> type = new TypeToken<List<ShopInfo>>() {
			};
			List<ShopInfo> list = GsonParser.getInstance().fromJson(data, type);
			if (list != null && !list.isEmpty()) {
				shopList.addAll(list);
			}

		} catch (Exception e) {
			throw new AppException("数据解析异常");
		}

	}

	@Override
	protected String method() {
		return "shop/collect/list";
	}

	public List<ShopInfo> getProductList() {
		return shopList;
	}

	public int getTotal() {
		return total;
	}

}
