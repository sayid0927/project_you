package com.zxly.o2o.request;


import com.easemob.easeui.AppException;

/**
 * Created by Administrator on 2015/10/13.
 */
public class SysNoticReadCountRequest extends BaseRequest{

    public SysNoticReadCountRequest(long noticeId) {
        addParams("noticeId", noticeId);
    }

    @Override
    protected void fire(String data) throws AppException {
    }

    @Override
    protected String method() {
        return "/sysNotice/shop/readNotice";
    }
}
