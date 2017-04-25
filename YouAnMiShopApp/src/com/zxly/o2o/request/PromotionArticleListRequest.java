package com.zxly.o2o.request;


import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.PromotionArticle;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2015/5/25.
 */
public class PromotionArticleListRequest extends BaseRequest {

    private List<PromotionArticle> articles=new ArrayList<PromotionArticle>();
    public PromotionArticleListRequest(){
        addParams("shopId", Account.user==null?0:Account.user.getShopId());
        addParams("userId",Account.user==null?0:Account.user.getId());
    }

    public PromotionArticleListRequest(int pageIndex){
        addParams("shopId", Account.user==null?0:Account.user.getShopId());
        addParams("userId",Account.user==null?0:Account.user.getId());
        addParams("pageIndex",pageIndex);
    }
    public PromotionArticleListRequest(String key,int pageIndex){
        addParams("shopId", Account.user==null?0:Account.user.getShopId());
        addParams("userId",Account.user==null?0:Account.user.getId());
        addParams("pageIndex",pageIndex);
        addParams("key",key);
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
    }


    @Override
    protected String method() {
        // /makeFans/popuProduct
		return "/makeFans/popuArticle";
    }

}
