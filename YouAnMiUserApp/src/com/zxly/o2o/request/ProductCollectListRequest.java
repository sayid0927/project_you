package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.NewProduct;

import java.util.ArrayList;
import java.util.List;

public class ProductCollectListRequest extends BaseRequest {

    private List<NewProduct> productList = new ArrayList<NewProduct>();

    public ProductCollectListRequest(int pageIndex) {
        addParams("shopId", Config.shopId);
        addParams("pageIndex", pageIndex);
    }

    @Override
    protected void fire(String data) throws AppException {
        TypeToken<List<NewProduct>> type1 = new TypeToken<List<NewProduct>>() {
        };
        List<NewProduct> pList = GsonParser.getInstance().fromJson(data,
                type1);
        if (!listIsEmpty(pList)) {
            productList.addAll(pList);
        }
    }

    @Override
    protected String method() {
//		return "/product/collect/list";
        return "me/collectProduct";
    }

    public List<NewProduct> getProductList() {
        return productList;
    }

}
