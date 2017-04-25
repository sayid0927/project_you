package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.util.AppLog;

public class ShopCollectRequest extends BaseRequest {

	public String respData="null";
	public ShopCollectRequest()
	{
		addParams("shopId", Account.shopInfo.getId());
		byte collectType=Account.shopInfo.getIsCollect();
		if(collectType==1)
		{
			addParams("command", 2);
		}else
		{
			addParams("command", 1);
		}
		
	}
	public ShopCollectRequest(long shopId,int cmdType)
	{
		addParams("shopId", shopId);
		addParams("command", cmdType);
	}
	
	@Override
	protected void fire(String data) throws AppException {
		AppLog.d("data", "collect--->" + data);
		respData=data;
	//	storePageBean = GsonParser.getInstance().getBean(data,StorePage.class);


	}
	
	@Override
	protected String method() {
		return "/shop/collect";
	}

}
