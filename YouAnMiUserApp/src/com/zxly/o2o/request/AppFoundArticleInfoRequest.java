package com.zxly.o2o.request;

import android.util.Log;

import com.easemob.easeui.AppException;
import com.zxly.o2o.model.ProductArticles;
import com.zxly.o2o.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsnx on 2015/12/30.
 */
public class AppFoundArticleInfoRequest extends BaseRequest {
    private String content;
    private int praiseAmount;
    private String imageUrl;
    private int shareAmount;
    private String title;
    private int isPraise;
    private  String shareUrl;

    private List<User> listUser =new ArrayList<User>();
    private List<ProductArticles>  productArticlesList=new ArrayList<ProductArticles>();

    public AppFoundArticleInfoRequest(long id)
    {
        addParams("id",id);
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject root=new JSONObject(data);
            shareUrl=root.optString("shareUrl");
            isPraise=root.optInt("isPraise");
            JSONObject infoJson=new JSONObject(root.getString("productArticleInfo"));
            content= infoJson.optString("content");
            praiseAmount = infoJson.optInt("praiseAmount");
            imageUrl= infoJson.optString("imageUrl");
            shareAmount = infoJson.optInt("shareAmount");
            title= infoJson.optString("title");
            if(infoJson.has("enjoyMens"))
            {
                JSONArray  userArray=infoJson.getJSONArray("enjoyMens");
                int length=userArray.length();
                for(int i=0;i<length;i++)
                {
                    JSONObject joUser=userArray.getJSONObject(i);
                    User user=new User();
                    user.setId(joUser.getInt("userId"));
                    user.setThumHeadUrl(joUser.optString("imageUrl"));
                    user.setName(joUser.getString("name"));
                    user.setThumHeadUrl(joUser.optString("imageUrl"));
                    user.setType((byte) joUser.optInt("type"));
                    user.setSignature(joUser.optString("sign"));
                    user.setOprateTime(joUser.optLong("opTime"));
                    listUser.add(user);
                }
            }
            if(infoJson.has("imageArticles"))
            {
                JSONArray productArray=infoJson.getJSONArray("imageArticles");
                int length=productArray.length();
                for(int i=0;i<length;i++)
                {
                    JSONObject joProduct=productArray.getJSONObject(i);
                    ProductArticles pa=new ProductArticles();
                    pa.setId(joProduct.optLong("id"));
                    pa.setContent(joProduct.optString("content"));
                    pa.setImageUrl(joProduct.optString("imageUrl"));
                    pa.setEnjoyAmount(joProduct.optInt("enjoyAmount"));
                    pa.setProductId(joProduct.optInt("productId"));
                    pa.setPrice((float) joProduct.optDouble("price"));
                    pa.setName(joProduct.optString("name"));
                    productArticlesList.add(pa);
                }
            }
        } catch (JSONException e) {
            throw JSONException(e);

        }
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public int getIsPraise() {
        return isPraise;
    }

    public String getContent() {
        return content;
    }

    public int getPraiseAmount() {
        return praiseAmount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getShareAmount() {
        return shareAmount;
    }

    public String getTitle() {
        return title;
    }

    public List<User> getListUser() {
        return listUser;
    }

    public List<ProductArticles> getProductArticlesList() {
        return productArticlesList;
    }

    @Override
    protected String method() {
        return "appFound/articleInfo";
    }
}
