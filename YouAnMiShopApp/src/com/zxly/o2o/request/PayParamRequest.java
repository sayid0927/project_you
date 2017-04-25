package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.zxly.o2o.application.Config;
import com.zxly.o2o.model.PayParams;

/**
 * @author fengrongjian 2015-10-10
 * @description 获取微信，支付宝商户参数网络请求
 */
public class PayParamRequest extends BaseRequest {

    private PayParams payParams;

    public PayParamRequest(String businessType, String channel, String device) {
        addParams("businessType", businessType);
        addParams("channel", channel);
        addParams("device", device);
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            payParams = GsonParser.getInstance().getBean(data, PayParams.class);
            Config.payParams = payParams;
        } catch (Exception e) {
            throw new AppException("数据解析异常");
        }
    }

    @Override
    protected String method() {
        return "pay/init";
    }

}
