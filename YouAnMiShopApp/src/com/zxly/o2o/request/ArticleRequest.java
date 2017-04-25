package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.ShopArticle;

import java.util.ArrayList;
import java.util.List;


/**
 *     @author huangbin  @version 创建时间：2015-2-4 上午11:21:49    类说明: 
 */
public class ArticleRequest extends BaseRequest {
    private int type;
    public List<ShopArticle> articles=new ArrayList<ShopArticle>();

    public ArticleRequest(int pageIndex, int type) {  //1:最热 2:最新 3:行业新闻 4:业界培训
        this.type = type;
        addParams("shopId",Account.user.getShopId());
        addParams("pageIndex", pageIndex);
        if (type == 1 || type == 2) {   //文章推广需要userId
            addParams("userId", Account.user.getId());
        } else {
            type = type - 2;
        }
        addParams("type", type);
    }

    @Override
    protected void fire(String data) throws AppException {
        TypeToken<List<ShopArticle>> token = new TypeToken<List<ShopArticle>>() {
        };
        articles = GsonParser.getInstance().fromJson(data, token);

        if (articles == null) {
            articles = new ArrayList<ShopArticle>();
        }
    }

    @Override
    protected String method() {
        if (type == 1 || type == 2) {
            return "/promote/articleList";
        } else {
            return "/promote/article/list";
        }
    }

}
