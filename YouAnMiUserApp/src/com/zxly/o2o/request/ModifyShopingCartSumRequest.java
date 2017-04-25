package com.zxly.o2o.request;

import com.zxly.o2o.account.Account;

public class ModifyShopingCartSumRequest extends BaseRequest {
	
	public ModifyShopingCartSumRequest(int pcs,String itemId)
	{
		addParams("userId",Account.user.getId());
		addParams("itemId", itemId);
		addParams("pcs", pcs);
	}

	@Override
	protected String method() {
		return "/order/shoppingCart/modify";
	}

	@Override
	protected boolean isShowLoadingDialog() {
	
		return true;
	}
	
	

}
