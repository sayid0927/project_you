package com.zxly.o2o.request;

import com.easemob.easeui.AppException;

import org.json.JSONObject;

/**
 * @author fengrongjian 2016-4-28
 * @description 产品服务条款网络请求
 */
public class InsuranceClauseRequest extends BaseRequest {
    private String serviceTerm;

    public InsuranceClauseRequest(long id) {
        addParams("id", id);
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject jsonObject = new JSONObject(data);
            if(jsonObject.has("serviceTerm")) {
                serviceTerm = jsonObject.getString("serviceTerm");
            }
        } catch (Exception e) {
            throw new AppException("数据解析异常");
        }
    }

    @Override
    protected String method() {
        return "insurance/product/serviceTerm";
    }

    public String getServiceTerm() {
        return serviceTerm;
    }
}
