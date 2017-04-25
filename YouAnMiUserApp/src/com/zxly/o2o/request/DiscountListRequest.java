package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.ShopDiscount;

import java.util.ArrayList;
import java.util.List;

public class DiscountListRequest extends BaseRequest {

    private List<ShopDiscount> shopDiscountList = new ArrayList<ShopDiscount>();

    public DiscountListRequest(long pageIndex) {
        addParams("pageIndex", pageIndex);
        addParams("shopId", Config.shopId);
    }

    @Override
    protected void fire(String data) throws AppException {
        TypeToken<List<ShopDiscount>> typeToken = new TypeToken<List<ShopDiscount>>() {
        };
        shopDiscountList = GsonParser.getInstance().fromJson(data, typeToken);
    }

    @Override
    protected String method() {
        return "discount/mine";
    }

    public List<ShopDiscount> getShopDiscountList() {
        return shopDiscountList;
    }
}
