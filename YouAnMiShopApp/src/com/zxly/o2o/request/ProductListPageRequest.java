package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.Product;
import com.zxly.o2o.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsnx on 2015/7/8.
 */
public class ProductListPageRequest extends BaseRequest {
    private List<Product> productList=new ArrayList<Product>();

    public ProductListPageRequest(int type,long brandId,long classId,String sortCode,int pageIndex) {
        addParams("shopId", Account.user.getShopId());
        addParams("userId", Account.user.getId());
        addParams("type",type);
        if(brandId>0)
        {
            addParams("brandId",brandId);
        }
       if(classId>0)
       {
           addParams("classId",classId);
       }
        if(!StringUtil.isNull(sortCode))
        {
            addParams("sortCode",sortCode);
        }
        addParams("pageIndex",pageIndex);
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject json = new JSONObject(data);
            TypeToken<List<Product>> type1 = new TypeToken<List<Product>>() {
            };
            if(json.has("products"))
            {
                String products = json.getString("products");
                List<Product> pList = GsonParser.getInstance().fromJson(products,
                        type1);
                if (!listIsEmpty(pList)) {
                    productList.addAll(pList);
                }
            }

        } catch (JSONException e) {
            throw JSONException(e);
        }

    }

    public List<Product> getProductList() {
        return productList;
    }

    @Override
    protected String method() {
        return "/promote/query/list";
    }
}
