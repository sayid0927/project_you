package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.zxly.o2o.model.ArticleInfo;
import com.zxly.o2o.model.ShopArticle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejun on 2016/9/21.
 * 店铺热文
 */
public class NewShopInfoRequest extends BaseRequest{

    private ShopArticle myShopArticle=new ShopArticle();

    public NewShopInfoRequest(long id){
        addParams("id",id);
        addParams("articleFrom",2);
    }

    @Override
    protected void fire(String data) throws AppException {
        myShopArticle = GsonParser.getInstance().getBean(data, ShopArticle.class);

    }

    @Override
    protected String method() {
        return "/article/new/shop/info";
    }

    public ShopArticle getShopArticle(){
        return myShopArticle;
    }
}
