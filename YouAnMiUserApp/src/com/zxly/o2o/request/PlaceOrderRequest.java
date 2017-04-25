package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.NewProduct;
import com.zxly.o2o.model.PakageDTO;
import com.zxly.o2o.model.ProductDTO;
import com.zxly.o2o.util.DataUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dsnx on 2015/5/27.
 */
public class PlaceOrderRequest extends BaseRequest {

	private List<ProductDTO> prodList=new ArrayList<ProductDTO>();
	private Map<String, PakageDTO> pakageMap=new HashMap<String,PakageDTO>();
	private String orderNo;

	/**
	 * @param deliveryType 1:送货上门   2:到店自提
	 * @param addressId
	 * @param isShopingCart
	 * @param productList
	 */
	public PlaceOrderRequest(int deliveryType, long addressId,
			int isShopingCart, List<NewProduct> productList) {
		addParams("userId", Account.user.getId());
		addParams("deliveryType", deliveryType);
		if(deliveryType==1)
		{
			addParams("addressId", addressId);
		}else
		{
			addParams("branchOrg", addressId);
		}

		addParams("isShopingCart", isShopingCart);
		splitProduct(productList);
		if(!prodList.isEmpty())
		{
			addParams("products", prodList);
		}
		if(!pakageMap.isEmpty())
		{
			addParams("pakages", pakageMap.values().toArray());
		}
		addParams("shopId", Config.shopId);

	}


	@Override
	protected boolean isSign() {
		return true;
	}
	public void splitProduct(List<NewProduct> list)
	{
		int size=list.size();
		for(int i=0;i<size;i++)
		{
			NewProduct np=list.get(i);
			if(np.getPakageId()>0)
			{
				if(pakageMap.containsKey(np.getBuyItemId()))
				{
					pakageMap.get(np.getBuyItemId()).addProduct(copyProduct(np));
				}else
				{
					PakageDTO pakage=new PakageDTO();
					pakage.setPakageId(np.getPakageId());
					pakage.setPcs(np.getPcs());
					pakage.addProduct(copyProduct(np));
					pakageMap.put(np.getBuyItemId(), pakage);
					
				}
			}else
			{
				prodList.add(copyProduct(np));
			}
		}
	}

	private List<ProductDTO> copy(List<NewProduct> list) {
		List<ProductDTO> productList = new ArrayList<ProductDTO>();
		for (NewProduct product : list) {
			ProductDTO pro =copyProduct(product);
			productList.add(pro);
		}
		return productList;
	}
	private ProductDTO copyProduct(NewProduct np)
	{
		ProductDTO pro = new ProductDTO();
		pro.setId(np.getId());
		pro.setSkuId(np.getSkuId());
		pro.setPreprice(np.getSelSku().getPrice());
		pro.setPrice(DataUtil.subtractFloat(np.getSelSku().getPrice(),np.getPreference()));
		pro.setPcs(np.getPcs());
		pro.setType(np.getTypeCode());
		if(np.getPakageId()>0)
		{
			pro.setPakageId(np.getPakageId());
		}
		return pro;
	}

	@Override
	protected void fire(String data) throws AppException {
		try {
			JSONObject jo = new JSONObject(data);
			orderNo = jo.getString("orderNo");

		} catch (org.json.JSONException e) {
			throw JSONException(e);
		}
	}

	public String getOrderNo() {
		return orderNo;
	}

	
	@Override
	protected boolean isShowLoadingDialog() {
		return true;
	}

	@Override
	protected String method() {
		return "/order/placeOrder";
	}
}
