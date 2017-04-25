package com.zxly.o2o.request;

import com.zxly.o2o.util.DataUtil;

import java.util.List;

/**
 * Created by hejun on 2016/9/21.
 */
public class PushArticleRequest extends BaseRequest {
    public PushArticleRequest(List<String> fansImeis,long articleId,long userId,List<Long> userIds){
        if(!DataUtil.listIsNull(fansImeis)) {
            addParams("fansImeis", fansImeis);
        }
        addParams("articleId",articleId);
        addParams("userId",userId);
        if(!DataUtil.listIsNull(userIds)) {
            addParams("userIds", userIds);
        }
    }
    @Override
    protected String method() {
        return "/keduoduo/push/pushArticle";
    }
}
