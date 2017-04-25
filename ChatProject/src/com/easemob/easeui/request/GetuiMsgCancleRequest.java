package com.easemob.easeui.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.EaseConstant;

/**
 * Created by Administrator on 2016/7/19.
 * 检查某条个推信息是否已被后台撤回，仅适用门店公告与柚安米公告类型
 */
public class GetuiMsgCancleRequest extends HXNormalRequest{
    @Override
    protected String method() {
        return "/msg/getMsgStatus";
    }

    /**
     *
     * @param what 公告类型
     * @param id  公告id
     */
    public GetuiMsgCancleRequest(int what,long id){
        addParams("id",id);
        addParams("what",what);
        addParams("shopId", Math.abs(EaseConstant.shopID));
    }

    @Override
    protected void fire(String data) throws AppException {
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return false;
    }
}
