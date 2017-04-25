package com.zxly.o2o.request;

import android.util.Log;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.CollectArticleModel;
import com.zxly.o2o.model.NewProduct;
import com.zxly.o2o.model.ShopArticle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejun on 2016/9/14.
 * 收藏文章列表
 */
public class CollectPaperRequest extends BaseRequest{
    private List<ShopArticle> paperList = new ArrayList<ShopArticle>();

    public CollectPaperRequest(int pageIndex) {
        addParams("shopId", Config.shopId);
        addParams("pageIndex", pageIndex);
    }

    @Override
    protected void fire(String data) throws AppException {
        TypeToken<List<ShopArticle>> type = new TypeToken<List<ShopArticle>>() {
        };
        List<ShopArticle> pList = GsonParser.getInstance().fromJson(data,
                type);
        if (!listIsEmpty(pList)) {
            paperList.addAll(pList);
        }
    }


    public List<ShopArticle> getPaperList() {
        return paperList;
    }

    @Override
    protected String method() {
        return "/article/new/collect/list";
    }
}
