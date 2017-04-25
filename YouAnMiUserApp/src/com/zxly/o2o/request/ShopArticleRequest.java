package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.ShopArticle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengrongjian 2016-7-28
 * @description 查询指定类型的文章
 */
public class ShopArticleRequest extends BaseRequest {

    private List<ShopArticle> shopArticleList = new ArrayList<ShopArticle>();

    public ShopArticleRequest(int articleType, int pageIndex) {
        addParams("articleType", articleType);
        addParams("pageIndex", pageIndex);
        addParams("shopId", Config.shopId);
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            TypeToken<List<ShopArticle>> type = new TypeToken<List<ShopArticle>>() {
            };
            List<ShopArticle> list = GsonParser.getInstance().fromJson(data,
                    type);
            if (!listIsEmpty(list)) {
                shopArticleList.addAll(list);
            }
        } catch (Exception e) {
            throw new AppException("数据解析异常");
        }
    }

    public List<ShopArticle> getShopArticleList() {
        return shopArticleList;
    }

    @Override
    protected String method() {
        return "article/new/list";
    }

}
