package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.model.RefundmentDetail;

/**
 * Created by Benjamin on 2015/5/29.
 */
public class RefundmentDetailRequest extends BaseRequest {
    public RefundmentDetail refundmentDetail;

    public RefundmentDetailRequest(long id) {
        addParams("id", id);
    }


    @Override
    protected String method() {
        return "/order/applyDetail";
    }

    @Override
    protected void fire(String data) throws AppException {
        TypeToken<RefundmentDetail> d = new TypeToken<RefundmentDetail>() {
        };
        refundmentDetail = GsonParser.getInstance().fromJson(data, d);
        if (refundmentDetail == null) {
            refundmentDetail = new RefundmentDetail();
        } else {
            for (int i = 0; i < refundmentDetail.getBuyItem().getProducts().size(); i++) {
                refundmentDetail.getBuyItem().getProducts().get(i).setPcs(refundmentDetail.getBuyItem().getPcs());
            }
        }

    }
}
