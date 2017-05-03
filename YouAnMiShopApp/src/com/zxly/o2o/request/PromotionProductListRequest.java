package com.zxly.o2o.request;


import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.CommissionProduct;

import java.util.List;


/**
 * Created by Administrator on 2015/5/25.
 */
public class PromotionProductListRequest extends BaseRequest {
    public boolean hasNextPage;

    private List<CommissionProduct> products;
    public PromotionProductListRequest(int pageIndex){
        addParams("shopId", Account.user==null?0:Account.user.getShopId());
        addParams("userId",Account.user==null?0:Account.user.getId());
        addParams("pageIndex",pageIndex);
    }

    public PromotionProductListRequest(){
        addParams("shopId", Account.user==null?0:Account.user.getShopId());
        addParams("userId",Account.user==null?0:Account.user.getId());
    }

    public List<CommissionProduct> getProducts() {
        return products;
    }

    public void setProducts(List<CommissionProduct> products) {
        this.products = products;
    }

    @Override
    protected void fire(String data) throws AppException {
        TypeToken<List<CommissionProduct>> types = new TypeToken<List<CommissionProduct>>() {};
        products= GsonParser.getInstance().fromJson(data, types);

        if(products.size()<10){
            hasNextPage = false;
        } else {
            hasNextPage = true;
        }
    }

    @Override
    protected String method() {
		return "/makeFans/popuProduct";
    }

}
