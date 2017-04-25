package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.domain.EaseUser;
import com.easemob.easeui.model.IMUserInfoVO;
import com.easemob.easeui.request.HXNormalRequest;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.model.ShopTopic;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/31.
 */
public class IMUserDetailTopicsRequest extends BaseRequest {
    public List<ShopTopic> userTopics = new ArrayList<ShopTopic>();

    public IMUserDetailTopicsRequest(long userId, int pageIndex) {
        addParams("userId", userId);
        addParams("pageIndex", pageIndex);
    }

    @Override
    protected void fire(String data) throws AppException {
        JSONObject jo;

        try {
            jo = new JSONObject(data);
            if (jo.has("topics")) {
                data = jo.getString("topics");
            }else{
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        userTopics = GsonParser.getInstance().fromJson(data, new TypeToken<List<ShopTopic>>() {
        });
    }

    @Override
    protected String method() {
        return "/user/circle/mytopic";
    }
}
