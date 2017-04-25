package com.zxly.o2o.request;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.Product;
import com.zxly.o2o.util.ViewUtils;

import java.util.List;

/**
 * Created by dsnx on 2015/7/9.
 */
public class CustomSetComissionRequest extends BaseRequest  {

    public CustomSetComissionRequest(int type,List<Product> productList) {

        addParams("shopId", Account.user.getShopId());
        addParams("type",type);
        addParams("products",productList);
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }
    @Override
    protected String method() {
        return "/promote/setCommission/self";
    }


}
