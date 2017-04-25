package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.Product;
import com.zxly.o2o.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsnx on 2015/7/8.
 */
public class CommissionRequest extends BaseRequest {
    private List<Product> productList=new ArrayList<Product>();

    public CommissionRequest(int type, long brandId, long clazzId, String sortCode, int pageIndex) {
        addParams("shopId", Account.user.getShopId());
        addParams("type",type);
        if(brandId>0)
        {
            addParams("brandId",brandId);
        }
       if(clazzId>0)
       {
           addParams("clazzId",clazzId);
       }
        if(!StringUtil.isNull(sortCode))
        {
            addParams("sortCode",sortCode);
        }
        addParams("pageIndex",pageIndex);
    }

    @Override
    protected void fire(String data) throws AppException {

        TypeToken<List<Product>> type1 = new TypeToken<List<Product>>() {
        };
        List<Product> pList = GsonParser.getInstance().fromJson(data,
                type1);
        if (!listIsEmpty(pList)) {
            productList.addAll(pList);
        }

    }

    public List<Product> getProductList() {
        return productList;
    }

    @Override
    protected String method() {
        return "/promote/shopProduct";
    }
}
