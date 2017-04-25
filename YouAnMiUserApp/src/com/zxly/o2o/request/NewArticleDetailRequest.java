package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.zxly.o2o.model.ArticleReply;
import com.zxly.o2o.model.ShopArticle;

import java.util.ArrayList;

/**
 * Created by hejun on 2016/9/20.
 */
public class NewArticleDetailRequest  extends MyCircleRequest{
    public NewArticleDetailRequest(long articleId){
        addParams("id",articleId);
    }

    @Override
    protected void fire(String data) throws AppException {
        ShopArticle myShopArticle = GsonParser.getInstance().getBean(data, ShopArticle.class);
        if(shopArticle==null)
            shopArticle=new ShopArticle();
        articleReplys = new ArrayList<ArticleReply>();
        shopArticle.setIsCollected(myShopArticle.getIsCollected());
        shopArticle.setIsPraise(myShopArticle.getIsPraise());
        shopArticle.setId(myShopArticle.getId());
        shopArticle.setPraiseAmount(myShopArticle.getPraiseAmount());
        shopArticle.setUpdateTime(myShopArticle.getUpdateTime());
    }

    @Override
    protected String method() {
        return "/article/new/list";
    }
}
