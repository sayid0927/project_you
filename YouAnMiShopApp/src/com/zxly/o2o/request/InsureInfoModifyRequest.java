package com.zxly.o2o.request;

import com.easemob.easeui.AppException;

/**
 * Created by fengrongjian on 2016/9/5.
 */
public class InsureInfoModifyRequest extends BaseRequest {

    public InsureInfoModifyRequest(long id, String imageUrl, long shopId, String
            thumImageUrl, String userName, String iosPhoneImei) {
        addParams("thumImageUrl", thumImageUrl);
        addParams("shopId", shopId);
        addParams("imageUrl", imageUrl);
        addParams("id", id);
        addParams("userName", userName);
        addParams("iosPhoneImei", iosPhoneImei);
    }

    @Override
    protected void fire(String data) throws AppException {
        super.fire(data);

    }

    @Override
    protected String method() {
        return "/insurance/order/update";
    }
}
