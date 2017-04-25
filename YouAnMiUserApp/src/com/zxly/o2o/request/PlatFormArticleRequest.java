package com.zxly.o2o.request;

import android.util.Log;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.zxly.o2o.model.ArticleReply;
import com.zxly.o2o.model.ShopArticle;

import java.util.ArrayList;

/**
 * Created by hejun on 2016/9/21.
 */
public class PlatFormArticleRequest extends MyCircleRequest{
    public PlatFormArticleRequest(long id){
        addParams("id",id);
        addParams("articleFrom",2);
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
        shopArticle.setStatus(myShopArticle.getStatus());
        shopArticle.setHead_url(myShopArticle.getHead_url());
    }

    @Override
    protected String method() {
        return "/article/new/store/info";
    }
}
