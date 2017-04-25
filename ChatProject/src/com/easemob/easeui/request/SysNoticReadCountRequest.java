package com.easemob.easeui.request;


import com.easemob.easeui.AppException;
import com.easemob.easeui.EaseConstant;

/**
 * Created by Administrator on 2015/10/13.
 */
public class SysNoticReadCountRequest extends HXNormalRequest{

    public SysNoticReadCountRequest(long noticeId) {
        addParams("noticeId", noticeId);
    }

    @Override
    protected void fire(String data) throws AppException {
    }

    @Override
    protected String method() {
        return EaseConstant.shopID<0?"/sysNotice/shop/readNotice":"/sysNotice/readNotice";
    }
}
