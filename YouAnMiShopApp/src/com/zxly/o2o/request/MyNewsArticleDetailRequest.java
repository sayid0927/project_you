package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.zxly.o2o.model.ShopArticle;

/**
 * Created by Benjamin on 2015/7/20.
 */
public class MyNewsArticleDetailRequest extends BaseRequest{
    public  ShopArticle shopArticle;
    public MyNewsArticleDetailRequest(long articleId){
        addParams("id", articleId);
    }

    @Override
    protected void fire(String data) throws AppException {
        shopArticle = GsonParser.getInstance().getBean(data, ShopArticle.class);
        if(shopArticle==null)
            shopArticle=new ShopArticle();
    }

    @Override
    protected String method() {
        return "/article/detail";
    }
}
