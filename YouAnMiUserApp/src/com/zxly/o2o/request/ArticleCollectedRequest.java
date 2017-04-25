package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.model.CollectTopic;

import java.util.ArrayList;
import java.util.List;

/**
 *     @author huangbin  @version 创建时间：2015-2-4 上午11:21:49    类说明: 
 */
public class ArticleCollectedRequest extends BaseRequest {
    private List<CollectTopic> collectedTopicList = new ArrayList<CollectTopic>();
    public ArticleCollectedRequest(long page) {
        addParams("pageIndex", page);
    }

    @Override
    protected void fire(String data) throws AppException {
        TypeToken<List<CollectTopic>> token = new TypeToken<List<CollectTopic>>() {
        };
        collectedTopicList = GsonParser.getInstance().fromJson(data, token);
    }

    @Override
    protected String method() {
        return "me/collectTopic";
    }

    public List<CollectTopic> getCollectedTopicList() {
        return collectedTopicList;
    }
}
