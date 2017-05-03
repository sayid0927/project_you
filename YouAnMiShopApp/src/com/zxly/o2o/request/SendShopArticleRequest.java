package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.PromotionArticle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejun on 2016/9/5.
 */
public class SendShopArticleRequest extends BaseRequest {
    public boolean hasNextPage;

    private List<PromotionArticle> articles=new ArrayList<PromotionArticle>();
    public SendShopArticleRequest(){
        addParams("shopId", Account.user==null?0:Account.user.getShopId());
        addParams("userId",Account.user==null?0:Account.user.getId());
    }

    public void setPageIndex(int pageIndex){
        addParams("pageIndex",pageIndex);
    }


    public List<PromotionArticle> getArticles() {
        return articles;
    }

    public void setArticles(List<PromotionArticle> articles) {
        this.articles = articles;
    }

    @Override
    protected void fire(String data) throws AppException {
        TypeToken<List<PromotionArticle>> types = new TypeToken<List<PromotionArticle>>() {};
        articles= GsonParser.getInstance().fromJson(data, types);
        if(articles.size()<10){
            hasNextPage = false;
        } else {
            hasNextPage = true;
        }
    }


    @Override
    protected String method() {
        // /makeFans/popuProduct
        return "/makeFans/popuArticle";
    }

}

