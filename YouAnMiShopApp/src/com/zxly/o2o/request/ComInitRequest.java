package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.Product;
import com.zxly.o2o.model.ProductBrand;
import com.zxly.o2o.model.ProductType;
import com.zxly.o2o.model.SortItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsnx on 2015/7/8.
 */
public class ComInitRequest extends BaseRequest{

    private List<Product> productList = new ArrayList<Product>();
    private List<ProductType> productTypeList = new ArrayList<ProductType>();
    private List<ProductBrand> productBrandList = new ArrayList<ProductBrand>();
    private List<SortItem> sortItemList=new ArrayList<SortItem>();
    public ComInitRequest(int type) {
        addParams("type",type);
        addParams("shopId", Account.user.getShopId());
    }

    @Override
    protected void fire(String data) throws AppException {
        {

            try {
                JSONObject json = new JSONObject(data);

                if(json.has("products"))
                {
                    String products = json.getString("products");
                    JSONArray jsonArray=new JSONArray(products);
                    int len=jsonArray.length();
                    for(int i=0;i<len;i++)
                    {
                        JSONObject jProduct=jsonArray.getJSONObject(i);
                        Product product=new Product();
                        product.setId(jProduct.getLong("id"));
                        if(jProduct.has("rate"))
                        {
                            product.setRate((float) jProduct.getDouble("rate"));
                        }

                        product.setPrice((float) jProduct.getDouble("price"));
                        product.setHeadUrl(jProduct.getString("headUrl"));
                        if(jProduct.has("typeCode"))
                        {
                            product.setTypeCode(jProduct.getInt("typeCode"));
                        }
                        if(jProduct.has("preference"))
                        {
                            product.setPreference((float) jProduct.getDouble("preference"));
                        }
                        if(jProduct.has("comission"))
                        {
                            product.setComission((float) jProduct.getDouble("comission"));
                        }

                        product.setName(jProduct.getString("name"));
                        productList.add(product);
                    }
                }
                if(json.has("classes"))
                {
                    String classes = json.getString("classes");
                    TypeToken<List<ProductType>> type2 = new TypeToken<List<ProductType>>() {
                    };
                    List<ProductType> ptList = GsonParser.getInstance().fromJson(
                            classes, type2);
                    if (!listIsEmpty(ptList)) {
                        productTypeList.addAll(ptList);
                    }

                }
                if(json.has("brands"))
                {
                    String brands = json.getString("brands");
                    TypeToken<List<ProductBrand>> type3 = new TypeToken<List<ProductBrand>>() {
                    };
                    List<ProductBrand> pbList = GsonParser.getInstance().fromJson(
                            brands, type3);

                    if (!listIsEmpty(pbList)) {
                        productBrandList.addAll(pbList);
                    }
                }
                if(json.has("sorts"))
                {
                    String sorts=json.getString("sorts");
                    TypeToken<List<SortItem>> typeSort = new TypeToken<List<SortItem>>() {
                    };
                    List<SortItem> sList=GsonParser.getInstance().fromJson(sorts, typeSort);
                    if(!listIsEmpty(sList))
                    {
                        sortItemList.addAll(sList);
                    }
                }



            } catch (JSONException e) {
                throw JSONException(e);
            }


        }
    }
    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public List<ProductType> getProductTypeList() {
        return productTypeList;
    }

    public void setProductTypeList(List<ProductType> productTypeList) {
        this.productTypeList = productTypeList;
    }

    public List<ProductBrand> getProductBrandList() {
        return productBrandList;
    }

    public void setProductBrandList(List<ProductBrand> productBrandList) {
        this.productBrandList = productBrandList;
    }

    public List<SortItem> getSortItemList() {
        return sortItemList;
    }

    public void setSortItemList(List<SortItem> sortItemList) {
        this.sortItemList = sortItemList;
    }
    @Override
    protected String method() {
        return "/promote/comInit";
    }
}
