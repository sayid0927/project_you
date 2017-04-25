package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.request.HXNormalRequest;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.model.TopicDetailHeads;
import com.zxly.o2o.model.TopicDetailHeadsUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/28.
 */
public class ShareAndUpStatusRequest extends HXNormalRequest{
    public List<TopicDetailHeads> viewHolders=new ArrayList<TopicDetailHeads>();

    public ShareAndUpStatusRequest(byte isShopTopic,long topicId,int pageIndex){
        addParams("isShopTopic",isShopTopic);
        addParams("topicId",topicId);
        addParams("pageIndex",pageIndex);
    }

    @Override
    protected void fire(String data) throws AppException {
//        JSONObject jo ;
//        try {
//            jo = new JSONObject(data);
//            data = jo.getString("user");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


        TypeToken<List<TopicDetailHeads>> token = new TypeToken<List<TopicDetailHeads>>() {};
        viewHolders = GsonParser.getInstance().fromJson(data, token);
    }

    @Override
    protected String method() {
        return "/user/circle/record";
    }

}
