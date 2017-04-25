package com.zxly.o2o.request;

import com.easemob.easeui.AppException;

public class MyTopicDeleteRequest extends BaseRequest {

    public MyTopicDeleteRequest(String shopIds, String platIds) {
        addParams("shopIds", shopIds);
        addParams("platIds", platIds);
    }

    @Override
    protected void fire(String data) throws AppException {
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

    @Override
    protected String method() {
        return "user/circle/delete";
    }

}
