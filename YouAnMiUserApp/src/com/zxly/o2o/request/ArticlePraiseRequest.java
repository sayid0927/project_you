package com.zxly.o2o.request;

import com.easemob.easeui.AppException;

/**
 * @author fengrongjian 2016-8-9
 * @description 文章列表点赞
 */
public class ArticlePraiseRequest extends BaseRequest {

    public ArticlePraiseRequest(long id, byte articleType) {
        addParams("id", id);
        addParams("articleType", articleType);
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
        return "article/new/praise";
    }

}
