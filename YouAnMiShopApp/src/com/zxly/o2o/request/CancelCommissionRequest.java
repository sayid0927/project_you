package com.zxly.o2o.request;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.Product;

import java.util.List;

/**
 * Created by dsnx on 2015/7/30.
 */
public class CancelCommissionRequest extends BaseRequest {

    public CancelCommissionRequest(List<Product> selProductList)
    {
        int size=selProductList.size();
        long[] productId=new long[size];
        for(int i=0;i<size;i++)
        {
            Product product=selProductList.get(i);
            productId[i]=product.getId();
        }
        addParams("shopId", Account.user.getShopId());
        addParams("productId",productId);
    }

    @Override
    protected String method() {
        return "/cancel/commission";
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }
}
