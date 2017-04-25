package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.zxly.o2o.model.CommisionDetailVO;

/**
 * Created by Benjamin on 2015/7/21.
 */
public class CommisionDetailRequest extends BaseRequest {
    public CommisionDetailVO commisionDetailVO;

    public CommisionDetailRequest(String orderId) {
        addParams("orderId", orderId);
    }

    @Override
    protected void fire(String data) throws AppException {
        commisionDetailVO = GsonParser.getInstance()
                .getBean(data, CommisionDetailVO.class);
    }

    @Override
    protected String method() {
        return "/popu/userCommissionOrder/detail";
    }
}
