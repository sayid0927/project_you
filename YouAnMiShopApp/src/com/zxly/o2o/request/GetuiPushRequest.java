package com.zxly.o2o.request;

import com.easemob.easeui.AppException;

/**
 * @author fengrongjian 2016-6-29
 * @description 个推推送商户或文章
 */
public class GetuiPushRequest extends BaseRequest {

    /**
     * @param objectId   	文章或商品的id
     * @param sendTarget      推送对象(0 未注册对象 1 已注册对象)
     * @param sendTotalUser        推送总人数
     * @param sendType      推送类型 0商品推送 1 文章推送
     * @param shopId      门店id
     * @param title      文章的标题或商品的名字
     * @param useIds      用户集合,逗号隔开,未注册用户返回imei,逗号隔开，已注册用户返回userId,逗号隔开
     * @param userId      	推送者id
     */
    public GetuiPushRequest(long objectId, int sendTarget, int sendTotalUser, int sendType, long shopId, String title, String useIds, long userId) {
        addParams("objectId", objectId);
        addParams("sendTarget", sendTarget);
        addParams("sendTotalUser", sendTotalUser);
        addParams("sendType", sendType);
        addParams("shopId", shopId);
        addParams("title", title);
        addParams("useIds", useIds);
        addParams("userId", userId);
    }

    @Override
    protected void fire(String data) throws AppException {
    }

    @Override
    protected String method() {
        return "getui/data/send";
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

}
