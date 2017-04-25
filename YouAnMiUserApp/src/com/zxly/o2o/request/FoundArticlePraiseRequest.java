package com.zxly.o2o.request;

/**
 * Created by dsnx on 2015/12/30.
 */
public class FoundArticlePraiseRequest extends BaseRequest {

    public FoundArticlePraiseRequest(long id)
    {
        addParams("id",id);
    }
    @Override
    protected String method() {
        return "/appFound/articlePraise";
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }
}
