package com.zxly.o2o.request;

public class MyOrderOperateRequest extends BaseRequest {

	/**
	 *
	 * @param id
	 * @param operateType 1取消，2删除
	 */
	public MyOrderOperateRequest(String orderNumber , int operateType)
	{
		addParams("orderNo", orderNumber);
		addParams("type",operateType);
	}  
	@Override
	protected String method() {
		return "/order/operateOrder";
	}
	@Override
	protected boolean isShowLoadingDialog() {
		return true;
	}

	
}
