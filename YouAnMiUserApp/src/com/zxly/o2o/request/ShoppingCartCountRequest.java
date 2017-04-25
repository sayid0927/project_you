package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.util.DataUtil;

/**
 * Created by kenwu on 2016/1/22.
 */
public class ShoppingCartCountRequest extends BaseRequest {

    private int shoppingCartCount;

    public ShoppingCartCountRequest(){
        addParams("shopId", Config.shopId);
    }

    public int getShoppingCartCount() {
        return shoppingCartCount;
    }

    @Override
    protected void fire(String data) throws AppException {
        if(!DataUtil.stringIsNull(data))
        shoppingCartCount=Integer.valueOf(data);
    }

    @Override
    protected String method() {
        return  "/order/shoppingCart/count";
    }
}
