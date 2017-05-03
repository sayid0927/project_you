package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.MakeMoneyArticle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsnx on 2015/12/22.
 */
public class MakeMoneyInitRequest extends BaseRequest {
    private float allRevenue;
    private float currentMonthRevenue;
    private int insuranceOrderCount;
    private List<MakeMoneyArticle> articleList=new ArrayList<MakeMoneyArticle>();
    public boolean hasNextPage;

    public MakeMoneyInitRequest()
    {
        addParams("userId", Account.user.getId());
        addParams("shopId", Account.user.getShopId());
    }

    public int getInsuranceOrderCount() {
        return insuranceOrderCount;
    }

    public float getAllRevenue() {
        return allRevenue;
    }

    public float getCurrentMonthRevenue() {
        return currentMonthRevenue;
    }

    public List<MakeMoneyArticle> getArticleList() {
        return articleList;
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject jsonObject=new JSONObject(data);
            insuranceOrderCount = jsonObject.optInt("insuranceOrderCount");
            allRevenue= (float) jsonObject.optDouble("allRevenue", 0);
            currentMonthRevenue= (float) jsonObject.optDouble("currentMonthRevenue", 0);
            if(jsonObject.has("articles"))
            {
                JSONArray jsonArray=jsonObject.getJSONArray("articles");
                int length=jsonArray.length();
                for(int i=0;i<length;i++)
                {
                    JSONObject articleJson=jsonArray.getJSONObject(i);
                    MakeMoneyArticle makeMoneyArticle=new MakeMoneyArticle();
                    makeMoneyArticle.setId(articleJson.getInt("id"));
                    makeMoneyArticle.setIncomeTitle(articleJson.optString("incomeTitle",""));
                    makeMoneyArticle.setContent(articleJson.optString("content",""));
                    makeMoneyArticle.setTitle(articleJson.optString("title",""));
                    makeMoneyArticle.setUrl(articleJson.optString("url"));
                    makeMoneyArticle.setHeadUrl(articleJson.optString("headUrl"));
                    articleList.add(makeMoneyArticle);
                }
            }
            if(articleList.size()<5){
                hasNextPage = false;
            } else {
                hasNextPage = true;
            }
        } catch (JSONException e) {
            throw JSONException(e);
        }
    }

//    @Override
//    protected boolean isShowLoadingDialog() {
//        return true;
//    }

    @Override
    protected String method() {
        return "shopApp/jiaoyouIndex";
    }
}
