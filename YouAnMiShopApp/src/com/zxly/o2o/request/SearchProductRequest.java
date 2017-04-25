package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.CommissionProduct;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsnx on 2016/1/19.
 */
public class SearchProductRequest extends BaseRequest {

    private List<CommissionProduct> products=new ArrayList<CommissionProduct>();
    public SearchProductRequest(String key,int pageIndex)
    {
        addParams("shopId", Account.user.getShopId());
        addParams("userId", Account.user.getId());
        addParams("key",key);
        addParams("pageIndex",pageIndex);
    }
    @Override
    protected void fire(String data) throws AppException {
        TypeToken<List<CommissionProduct>> types = new TypeToken<List<CommissionProduct>>() {};
        products.addAll(GsonParser.getInstance().fromJson(data, types));

    }

    public List<CommissionProduct> getProducts() {
        return products;
    }

    @Override
    protected String method() {
        return "/makeFans/popuProduct";
    }
}
