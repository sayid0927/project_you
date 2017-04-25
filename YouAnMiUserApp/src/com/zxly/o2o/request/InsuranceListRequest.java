package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.model.InsuranceProduct;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengrongjian 2016-4-28
 * @description 保修产品列表网络请求
 */
public class InsuranceListRequest extends BaseRequest {

    private List<InsuranceProduct> insuranceProductList = new ArrayList<InsuranceProduct>();

    public InsuranceListRequest(long shopId) {
        addParams("shopId", shopId);
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            TypeToken<List<InsuranceProduct>> type = new TypeToken<List<InsuranceProduct>>() {
            };
            List<InsuranceProduct> list = GsonParser.getInstance().fromJson(data,
                    type);
            if (!listIsEmpty(list)) {
                insuranceProductList.addAll(list);
            }
        } catch (Exception e) {
            throw new AppException("数据解析异常");
        }
    }

    @Override
    protected String method() {
        return "insurance/products/list";
    }

    public List<InsuranceProduct> getInsuranceProductList() {
        return insuranceProductList;
    }
}
