package com.zxly.o2o.request;

import com.zxly.o2o.model.Skus;
import com.zxly.o2o.util.ViewUtils;


public class BargainRequest extends BaseRequest implements BaseRequest.ResponseStateListener {

	public BargainRequest(float price,long productId,Skus selSkus)
	{
		addParams("productId", productId);
		addParams("price", price);
		addParams("skuParam", selSkus);
		setOnResponseStateListener(this);
	}
	@Override
	protected String method() {
		return "/product/askprice";
	}
	@Override
	public void onOK() {
		ViewUtils.showToast("议价消息已发送!");
	}
	@Override
	public void onFail(int code) {
		
	}
	

}
