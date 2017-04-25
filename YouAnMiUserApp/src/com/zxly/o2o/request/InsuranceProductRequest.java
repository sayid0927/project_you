package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.zxly.o2o.model.InsuranceProduct;
import com.zxly.o2o.util.AppLog;

import org.json.JSONObject;

/**
 * @author fengrongjian 2016-4-28
 * @description 保修产品信息网络请求
 */
public class InsuranceProductRequest extends BaseRequest {
    private InsuranceProduct insuranceProduct;

    public InsuranceProductRequest(long id) {
        addParams("id", id);
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            insuranceProduct = GsonParser.getInstance().getBean(data, InsuranceProduct.class);
        } catch (Exception e) {
            throw new AppException("数据解析异常");
        }
    }

    @Override
    protected String method() {
        return "insurance/product";
    }

    public InsuranceProduct getInsuranceProduct() {
        return insuranceProduct;
    }
}
