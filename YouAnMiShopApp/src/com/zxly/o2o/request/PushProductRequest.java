package com.zxly.o2o.request;

import com.zxly.o2o.util.DataUtil;

import java.util.List;

/**
 * Created by hejun on 2016/9/21.
 */
public class PushProductRequest extends BaseRequest {
    public PushProductRequest(List<String> fansImeis,long productId,long userId,List<Long> userIds){
        if(!DataUtil.listIsNull(fansImeis)) {
            addParams("fansImeis", fansImeis);
        }
        addParams("productId",productId);
        addParams("userId",userId);
        if(!DataUtil.listIsNull(userIds)) {
            addParams("userIds", userIds);
        }
    }
    @Override
    protected String method() {
        return "/keduoduo/push/pushProduct";
    }
}
