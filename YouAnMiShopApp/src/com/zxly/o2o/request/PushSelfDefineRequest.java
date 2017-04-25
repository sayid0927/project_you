package com.zxly.o2o.request;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.StringUtil;

import java.util.List;

/**
 * Created by hejun on 2016/9/21.
 */
public class PushSelfDefineRequest extends BaseRequest {
    public PushSelfDefineRequest(String content,String imgUrls, List<String> fansImeis,List<Long> userIds){
        addParams("content",content);
        if(!DataUtil.listIsNull(fansImeis)){
            addParams("fansImeis",fansImeis);
        }
        if(!StringUtil.isNull(imgUrls)){
            addParams("imgUrls",imgUrls);
        }
        addParams("shopId", Account.user.getShopId());
        addParams("userId",Account.user.getId());
        if(!DataUtil.listIsNull(userIds)) {
            addParams("userIds", userIds);
        }
    }
    @Override
    protected String method() {
        return "/keduoduo/push/pushCustomContent";
    }
}
