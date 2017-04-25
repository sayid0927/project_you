package com.zxly.o2o.request;

import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.NewProduct;

public class ProductCollectRequest extends BaseRequest  {

	private NewProduct product;
	private int collect;//1：收藏 2：取消收藏
	public ProductCollectRequest(NewProduct product)
	{
		addParams("shopId", Config.shopId);
		addParams("productId",product.getId());
		if(product.getCollect()==1)
		{
			collect=2;
			addParams("command", collect);
		}else
		{
			collect=1;
			addParams("command", collect);
		}
		
	}

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

    @Override
	protected String method() {
		return "/product/collect";
	}

	public int getCollect() {
		return collect;
	}

	

}
