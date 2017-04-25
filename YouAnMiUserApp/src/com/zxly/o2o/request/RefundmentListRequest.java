package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.RefundmentDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/5/26.
 */
public class RefundmentListRequest extends BaseRequest {
    public List<RefundmentDetail> refundmentList;

    public RefundmentListRequest(long userId,int status,int pageIndex) {
        addParams("pageIndex", pageIndex);
        addParams("status", status);
        addParams("userId", userId);
        addParams("shopId", Config.shopId);
    }

    @Override
    protected String method() {
        return "/order/applyRefund/list";
    }

    @Override
    protected void fire(String data) throws AppException {
        TypeToken<List<RefundmentDetail>> dataList = new TypeToken<List<RefundmentDetail>>() {
        };
        refundmentList = GsonParser.getInstance().fromJson(data, dataList);
        if (refundmentList == null) {
            refundmentList = new ArrayList<RefundmentDetail>();
        }
    }
}
