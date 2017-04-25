package com.zxly.o2o.request;

/**
 * Created by hejun on 2016/9/11.
 * 点击粉丝右侧 关注图片（就是那个眼睛）请求
 */
public class FansFocusRequest extends BaseRequest{
    /**
     *
     * @param b 取消关注--ture 关注--false
     */
    public FansFocusRequest(boolean b,long fansId){
        addParams("cancel",b);
        addParams("fansId",fansId);
    }
    @Override
    protected String method() {
        return "/keduoduo/fans/focus";
    }
}
