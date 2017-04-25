package com.zxly.o2o.request;

/**
 * Created by dsnx on 2015/12/31.
 */
public class ArticelShareRequest extends BaseRequest {
    public ArticelShareRequest(long id)
    {
        addParams("id",id);
    }
    @Override
    protected String method() {
        return "/appFound/articleShare";
    }
}
