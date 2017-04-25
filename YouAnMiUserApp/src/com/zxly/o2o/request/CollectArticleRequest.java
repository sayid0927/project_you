package com.zxly.o2o.request;

/**
 * Created by hejun on 2016/9/19.
 * 收藏文章
 */
public class CollectArticleRequest extends BaseRequest{

    /**
     *
     * @param articleType  文章类型  1--店铺热文 2--平台专享
     * @param id    文章id
     * @param operate 1--收藏  2--取消收藏
     */
    public CollectArticleRequest(int articleType,long id,int operate){
        addParams("articleType",articleType);
        addParams("id",id);
        addParams("operate",operate);
    }
    @Override
    protected String method() {
        return "/article/new/collect";
    }
}
