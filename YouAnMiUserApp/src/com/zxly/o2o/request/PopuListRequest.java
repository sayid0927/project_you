package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.model.Filters;
import com.zxly.o2o.model.NewProduct;
import com.zxly.o2o.model.Paging;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsnx on 2015/7/16.
 */
public class PopuListRequest extends BaseRequest {

    private List<NewProduct> productList = new ArrayList<NewProduct>();

    public PopuListRequest(Paging paging, List<Filters> filterList) {
        addParams("paging", paging);
        addParams("filters", filterList.toArray());
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
        return "popu/list";
    }

    public List<NewProduct> getProductList() {
        return productList;
    }
}
