package com.easemob.easeui.request;

import com.easemob.easeui.EaseConstant;

/**
 * Created by dsnx on 2016/7/21.
 */
public class SendMsgStatisticsRequest extends HXNormalRequest {
    public SendMsgStatisticsRequest(String commission, boolean registerProperty, String registerTime, int sendType) {
        addParams("commission", commission);
        addParams("registerProperty", registerProperty?"已注册":"未注册");
        addParams("registerTime", registerTime);
        addParams("sendType", sendType);
        addParams("userId", EaseConstant.currentUser.getFirendsUserInfo().getId() );
    }



    @Override
    protected String method() {
        return "/shop/sendMsgStatistics";
    }
}
