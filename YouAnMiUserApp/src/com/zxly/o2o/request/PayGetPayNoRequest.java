package com.zxly.o2o.request;


import com.easemob.easeui.AppException;

import org.json.JSONObject;

/**
 * @author fengrongjian 2015-10-10
 * @description 获取流水号
 */
public class PayGetPayNoRequest extends BaseRequest {
    private String payNo;

    public PayGetPayNoRequest(String orderNo, String payType, String businessType, String channel, String device, long shopId) {
        addParams("orderNo", orderNo);
        addParams("payType", payType);
        addParams("businessType", businessType);
        addParams("channel", channel);
        addParams("device", device);
        addParams("shopId", shopId);
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject json = new JSONObject(data);
            if (json.has("payNo")) {
                payNo = json.getString("payNo");
            }
        } catch (Exception e) {
            throw new AppException("数据解析异常");
        }
    }

    @Override
    protected String method() {
        return "/pay/jy/rechargeAmount";
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

    public String getPayNo() {
        return payNo;
    }

}
