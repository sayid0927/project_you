package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.zxly.o2o.model.InsuranceOrder;
import com.zxly.o2o.model.InsuranceProduct;
import com.zxly.o2o.model.InsuranceSupplier;

import org.json.JSONObject;

/**
 * @author fengrongjian 2016-4-28
 * @description 延保产品订单信息网络请求
 */
public class InsuranceOrderRequest extends BaseRequest {
    private InsuranceProduct insuranceProduct;
    private InsuranceOrder insuranceOrder;
    private InsuranceSupplier insuranceSupplier;
    private boolean isShow;

    public InsuranceOrderRequest(long orderId) {
        addParams("orderId", orderId);
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.has("order")) {
                String order = jsonObject.getString("order");
                insuranceOrder = GsonParser.getInstance().getBean(order, InsuranceOrder.class);
            }
            if (jsonObject.has("product")) {
                String product = jsonObject.getString("product");
                insuranceProduct = GsonParser.getInstance().getBean(product, InsuranceProduct.class);
            }
            if (jsonObject.has("supplier")) {
                String supplier = jsonObject.getString("supplier");
                insuranceSupplier = GsonParser.getInstance().getBean(supplier, InsuranceSupplier.class);
            }
        } catch (Exception e) {
            throw new AppException("数据解析异常");
        }
    }

    @Override
    protected String method() {
        return "insurance/order";
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return this.isShow;
    }

    public void setLoadingShow(){
        this.isShow = true;
    }

    public InsuranceProduct getInsuranceProduct() {
        return insuranceProduct;
    }

    public InsuranceOrder getInsuranceOrder() {
        return insuranceOrder;
    }

    public InsuranceSupplier getInsuranceSupplier() {
        return insuranceSupplier;
    }
}
