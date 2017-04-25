package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.NewProduct;
import com.zxly.o2o.model.ProductArticles;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsnx on 2015/12/25.
 */
public class SearchReqeust extends BaseRequest {


    private List<ProductArticles> listArticle=new ArrayList<ProductArticles>();
    private List<NewProduct>  listProduct=new ArrayList<NewProduct>();

    /**
     *
     * @param type  1:商品 2:配件 3:文章
     * @param key
     * @param pageIndex
     */
    public SearchReqeust(int type,String key,int pageIndex)
    {
        addParams("shopId", Config.shopId);
        addParams("type",type);
        addParams("key",key);
        addParams("pageIndex",pageIndex);
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject jsonObject=new JSONObject(data);
            if(jsonObject.has("productArticles"))
            {
                JSONArray jsonArray=jsonObject.getJSONArray("productArticles");
                int length=jsonArray.length();
                for(int i=0;i<length;i++)
                {
                    JSONObject articleJson=jsonArray.getJSONObject(i);
                    ProductArticles pa=new ProductArticles();
                    pa.setContent(articleJson.optString("content"));
                    pa.setEnjoyAmount(articleJson.optInt("enjoyAmount"));
                    pa.setId(articleJson.optInt("id"));
                    pa.setImageUrl(articleJson.optString("imageUrl"));
                    pa.setTitle(articleJson.optString("title"));
                    listArticle.add(pa);
                }
            }
            if(jsonObject.has("products"))
            {
                JSONArray jsonArray=jsonObject.getJSONArray("products");
                int length=jsonArray.length();
                for(int i=0;i<length;i++)
                {
                    JSONObject productJson=jsonArray.getJSONObject(i);
                    NewProduct product=new NewProduct();
                    product.setEnjoyAmount(productJson.optInt("enjoyAmount"));
                    product.setHeadUrl(productJson.optString("headUrl"));
                    product.setId(productJson.optLong("id"));
                    product.setName(productJson.optString("name"));
                    product.setPreference((float) productJson.optDouble("preference",0.0));
                    product.setPrice((float) productJson.getDouble("price"));
                    product.setTypeCode(productJson.optInt("typeCode"));
                    listProduct.add(product);
                }
            }
        } catch (JSONException e) {
            throw  JSONException(e);
        }
    }

    public List<ProductArticles> getListArticle() {
        return listArticle;
    }

    public List<NewProduct> getListProduct() {
        return listProduct;
    }

    @Override
    protected String method() {
        return "appFound/query";
    }
}
