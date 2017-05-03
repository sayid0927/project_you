package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.model.MakeMoneyArticle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsnx on 2015/12/23.
 */
public class MakeMoneyListRequest extends BaseRequest {

    private List<MakeMoneyArticle> articleList=new ArrayList<MakeMoneyArticle>();
    public boolean hasNextPage;

    public MakeMoneyListRequest(int pageIndex)
    {
        addParams("pageIndex",pageIndex);
    }

    public List<MakeMoneyArticle> getArticleList() {
        return articleList;
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONArray jsonArray=new JSONArray(data);
            int length=jsonArray.length();
            for(int i=0;i<length;i++)
            {
                JSONObject articleJson=jsonArray.getJSONObject(i);
                MakeMoneyArticle makeMoneyArticle=new MakeMoneyArticle();
                makeMoneyArticle.setId(articleJson.getInt("id"));
                makeMoneyArticle.setIncomeTitle(articleJson.optString("incomeTitle",""));
                makeMoneyArticle.setContent(articleJson.optString("content",""));
                makeMoneyArticle.setTitle(articleJson.optString("title",""));
                articleList.add(makeMoneyArticle);

            }
            if(articleList.size()<5){
                hasNextPage = false;
            } else {
                hasNextPage = true;
            }
        } catch (JSONException e) {
            throw  JSONException(e);
        }
    }

    @Override
    protected String method() {
        return "shopApp/makeMoneyArticleList";
    }
}
