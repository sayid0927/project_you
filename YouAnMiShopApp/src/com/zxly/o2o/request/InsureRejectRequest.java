package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.util.ViewUtils;

/**
 * Created by Administrator on 2016/5/13.
 */
public class InsureRejectRequest extends BaseRequest{
    public InsureRejectRequest(long id,long shopId){
        addParams("id",id);
        addParams("shopId",shopId);
    }

    @Override
    protected String method() {
        return "/insurance/order/refuse";
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

    @Override
    protected void fire(String data) throws AppException {
        super.fire(data);
    }
}
