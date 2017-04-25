package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.PhonePrice;
import com.zxly.o2o.model.ProductBrand;

import java.util.ArrayList;
import java.util.List;

public class PhoneBrandRequest extends BaseRequest {

    private List<ProductBrand> phoneBrands = new ArrayList<ProductBrand>();
    private List<PhonePrice> phonePrices = new ArrayList<PhonePrice>();
    private String level[] = {"0", "0-600", "600-1000", "1000-2000", "2000-3000", "3000-5000", "5000-1000000"};
    private String prices[] = {"不限", "0-599.99", "600-999.99", "1000-1999.99", "2000-2999.99", "3000-4999.99", "5000以上"};

    public PhoneBrandRequest() {
        addParams("shopId", Config.shopId);
    }

    @Override
    protected void fire(String data) throws AppException {

        phonePrices.clear();
        for(int i = 0; i< level.length; i++){
            PhonePrice phonePrice = new PhonePrice(level[i], prices[i]);
            phonePrices.add(phonePrice);
        }

        try {
            TypeToken<List<ProductBrand>> token = new TypeToken<List<ProductBrand>>() {
            };
            phoneBrands = GsonParser.getInstance().fromJson(data, token);
            ProductBrand phoneBrand = new ProductBrand(0, "全部");
            phoneBrands.add(0, phoneBrand);
        } catch (Exception e) {
            throw new AppException("数据解析异常");
        }
    }

    public List<ProductBrand> getPhoneBrands() {
        return phoneBrands;
    }

    public List<PhonePrice> getPhonePrices() {
        return phonePrices;
    }

    @Override
    protected String method() {
        return "appFound/phoneBrands";
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }
}
