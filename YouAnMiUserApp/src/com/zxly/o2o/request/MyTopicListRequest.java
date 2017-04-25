package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.model.UserTopic;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyTopicListRequest extends BaseRequest {

    private List<UserTopic> myTopicList = new ArrayList<UserTopic>();

    public MyTopicListRequest(long pageIndex, long userId) {
        addParams("pageIndex", pageIndex);
        addParams("userId", userId);
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject json = new JSONObject(data);
            if (json.has("topics")){
                String topics = json.getString("topics");
                TypeToken<List<UserTopic>> typeToken = new TypeToken<List<UserTopic>>() {
                };
                List<UserTopic> topicList = GsonParser.getInstance().fromJson(topics,
                        typeToken);
                if (!listIsEmpty(topicList)) {
                    myTopicList.addAll(topicList);
                }
            }
        } catch (Exception e){
            throw new AppException("数据解析异常");
        }
    }

    @Override
    protected String method() {
        return "user/circle/mytopic";
    }

    public List<UserTopic> getMyTopicList() {
        return myTopicList;
    }

}
