package com.zxly.o2o.request;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.ProductInfoAct;
import com.zxly.o2o.util.CallBack;

public class RemoveCartProductRequest extends BaseRequest implements
		BaseRequest.ResponseStateListener {

	private CallBack callBack;

	
	public RemoveCartProductRequest(String itemId, CallBack callBack) {
		this.callBack=callBack;
		addParams("userId", Account.user.getId());
		addParams("itemId", itemId);
		setOnResponseStateListener(this);
	}

	@Override
	protected String method() {
		return "/order/shoppingCart/remove";
	}

	@Override
	public void onOK() {
		if(Account.orderCount>=1)
		{
			Account.orderCount--;
			ProductInfoAct.refreshCartCount(2);
		}

	   this.callBack.onCall();
	}

	@Override
	protected boolean isShowLoadingDialog() {
		return true;
	}

	@Override
	public void onFail(int code) {

	}
}