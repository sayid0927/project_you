package com.zxly.o2o.request;

import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.NewProduct;
import com.zxly.o2o.model.Pakage;
import com.zxly.o2o.model.ProductDTO;
import com.zxly.o2o.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsnx on 2015/6/24.
 */
public class AddPakageToCartRequest extends BaseRequest implements BaseRequest.ResponseStateListener{
    public AddPakageToCartRequest(Pakage pakage) {
        addParams("pakageId", pakage.getPakageId());
       // addParams("price", pakage.getPrice());
        //addParams("name", pakage.getName());
        addParams("shopId", Config.shopId+"");
        addParams("pcs", pakage.getPcs());
        addParams("products", copy(pakage.getProductList()));
        setOnResponseStateListener(this);
    }

    private List<ProductDTO> copy(List<NewProduct> list) {
        List<ProductDTO> productList = new ArrayList<ProductDTO>();
        for (NewProduct product : list) {
            ProductDTO pro = new ProductDTO();
            pro.setId(product.getId());
            pro.setSkuId(product.getSkuId());
          /*  pro.setPreprice(product.getSelSku().getPrice());
            pro.setPrice(product.getSelSku().getPrice()
                    - product.getPreference());*/
            pro.setPcs(product.getPcs());
            pro.setPakageId(product.getPakageId());
            productList.add(pro);
        }
        return productList;
    }

    @Override
    protected String method() {
        return "/order/shoppingCart/addPakage";
    }

    @Override
    public void onOK() {
    	ViewUtils.showToast("添加成功，在购物车等亲!");
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

    @Override
    public void onFail(int code) {
    	
    }
}
