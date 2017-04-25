package com.zxly.o2o.request;


import com.zxly.o2o.config.Config;

/**
 *     @author huangbin  @version 创建时间：2015-1-26 下午3:36:44    类说明: 
 */
public class ArticleCollectRequst extends BaseRequest {

    public ArticleCollectRequst(long articleId, int command) {
        addParams("id", articleId);
        addParams("command", command);
        addParams("shopId", Config.shopId);
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

    @Override
    protected String method() {
        return "article/collect";
    }

}
