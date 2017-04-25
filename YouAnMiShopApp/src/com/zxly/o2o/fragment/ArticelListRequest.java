package com.zxly.o2o.fragment;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.application.Config;
import com.zxly.o2o.model.ArticleType;
import com.zxly.o2o.model.PromotionArticle;
import com.zxly.o2o.request.BaseRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsnx on 2016/9/8.
 */
public class ArticelListRequest extends BaseRequest {

    public  List<PromotionArticle> articleList=new ArrayList<PromotionArticle>();

    public ArticelListRequest(int pageIndex)
    {
        addParams("pageIndex",pageIndex);
        addParams("shopId", Account.user.getShopId());
        addParams("type",3);
        addParams("userId",Account.user.getId());
    }
    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONArray jsonArray=new JSONArray(data);
            int length=jsonArray.length();
            for(int i=0;i<length;i++)
            {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                PromotionArticle article=new PromotionArticle();
                article.setArticleId(jsonObject.optLong("articleId"));
                article.setUrl(jsonObject.optString("url"));
                article.setScanCount(jsonObject.optInt("scanCount"));
                article.setCreateTime(jsonObject.optLong("createTime"));
                article.setH5Url(jsonObject.optString("url"));
                article.setTitle(jsonObject.optString("title"));
                articleList.add(article);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected String method() {
        return "/keduoduo/promote/articles";
    }
}
