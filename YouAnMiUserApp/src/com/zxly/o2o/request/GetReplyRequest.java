package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.model.ArticleReply;
import com.zxly.o2o.model.ReplyArticleModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejun on 2016/9/19.
 */
public class GetReplyRequest extends MyCircleRequest {

    private List<ArticleReply> articleReplys=new ArrayList<ArticleReply>();
    public GetReplyRequest(long articleId,int pageIndex){
        addParams("articleId",articleId);
        addParams("pageIndex",pageIndex);
    }

    @Override
    protected void fire(String data) throws AppException {
//        try {
//            JSONArray jsonArray = new JSONArray(data);
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = jsonArray.optJSONObject(i);
//                ReplyArticleModel replyArticle = new ReplyArticleModel();
//                replyArticle.setContent(jsonObject.optString("content"));
//                replyArticle.setHeadUrl(jsonObject.optString("headUrl"));
//                replyArticle.setId(jsonObject.optLong("id"));
//                replyArticle.setIsPraise(jsonObject.optInt("isPraise"));
//                replyArticle.setNickName(jsonObject.optString("nickname"));
//                replyArticle.setPraiseAmount(jsonObject.optInt("praiseAmount"));
//                replyArticle.setReplyerId(jsonObject.optLong("replyerId"));
//                replyArticle.setStatus(jsonObject.optInt("status"));
//                replyArticle.setUpdateTime(jsonObject.optLong("updateTime"));
//                replyArticleModelList.add(replyArticle);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        TypeToken<List<ArticleReply>> token = new TypeToken<List<ArticleReply>>() {
        };

        articleReplys = GsonParser.getInstance().fromJson(data, token);


    }

    @Override
    protected String method() {
        return "/article/new/get/reply";
    }

    public List<ArticleReply> getReplyArticleModelList(){
        return articleReplys;
    }
}
