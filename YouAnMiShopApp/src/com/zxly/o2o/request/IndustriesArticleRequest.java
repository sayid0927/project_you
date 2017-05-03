package com.zxly.o2o.request;

import android.text.TextUtils;

import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.PromotionArticle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsnx on 2016/9/29.
 * 自定义文章
 */
public class IndustriesArticleRequest extends BaseRequest {
    public List<PromotionArticle> articleList=new ArrayList<PromotionArticle>();
    public IndustriesArticleRequest(int pageIndex)
    {
        addParams("pageIndex",pageIndex);
        addParams("pageSize",20);
        addParams("shopId", Account.user.getShopId());
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
                article.setUrl(jsonObject.optString("shareUrl"));
                article.setScanCount(jsonObject.optInt("scanCount"));
                article.setCreateTime(jsonObject.optLong("createTime"));
                article.setH5Url(jsonObject.optString("shareUrl"));
                article.setTitle(jsonObject.optString("title"));
                article.setUserAppName(jsonObject.optString("userAppName"));
                article.setDescription(jsonObject.optString("description"));
                String url = jsonObject.optString("shareUrl");
                if(!TextUtils.isEmpty(url)&&url.contains("shareImage=")){
                    String substring = url.replaceAll("(?is).*?shareImage=(.*?)&.*", "$1");
                    article.setShareImageUrl(substring);
                }
                articleList.add(article);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    protected String method() {
        return "/keduoduo/fans/industriesArticle";
    }

}
