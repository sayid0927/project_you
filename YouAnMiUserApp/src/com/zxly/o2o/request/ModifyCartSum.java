package com.zxly.o2o.request;

public class ModifyCartSum extends BaseRequest {
	
	public ModifyCartSum(int id,int pcs,int type)
	{
		
	}

	@Override
	protected String method() {
		
		return "/order/shoppingCart/modify";
	}

}
