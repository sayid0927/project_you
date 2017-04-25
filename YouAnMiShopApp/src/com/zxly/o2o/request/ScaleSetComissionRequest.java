package com.zxly.o2o.request;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.Product;
import com.zxly.o2o.util.ViewUtils;

import java.util.List;

/**
 * Created by dsnx on 2015/7/9.
 */
public class ScaleSetComissionRequest extends  BaseRequest  {


    public ScaleSetComissionRequest(List<Product> products,String rate,int type) {
        addParams("shopId", Account.user.getShopId());
        int len=products.size();
        long[] prodId=new long[len];
        for(int i=0;i<len;i++)
        {
            Product product=products.get(i);
            prodId[i]=product.getId();
        }
        addParams("products", prodId);
        addParams("rate",rate);
        addParams("type",type);

    }



    private  class ID{
        public long id;

    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

    @Override
    protected String method() {
        return "/promote/setCommission/rate";
    }
}
