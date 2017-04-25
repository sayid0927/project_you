package com.zxly.o2o.request;

import android.os.Build;
import android.util.Log;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.model.CircleForumVO;
import com.zxly.o2o.model.ShopArticle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/21.
 */
public class ForumGuideRequest extends BaseRequest {
    private List<CircleForumVO> circleForumVOs = new ArrayList<CircleForumVO>();

    public ForumGuideRequest(String areaName) {
        addParams("areaName", areaName);
        addParams("brandName", Build.BRAND);
    }

    @Override
    protected boolean isShowLoadingDialog() {
       return true;
    }

    @Override
    protected String method() {
        return "/user/circle/recommend";
    }

    @Override
    protected void fire(String data) throws AppException {
        JSONObject jo;
        try {
            jo = new JSONObject(data);
            data = jo.getString("circles");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        TypeToken<List<CircleForumVO>> token = new TypeToken<List<CircleForumVO>>() {
        };
        circleForumVOs = GsonParser.getInstance().fromJson(data, token);

    }

    public List<CircleForumVO> getObjects(){
        if (circleForumVOs == null) {
            circleForumVOs = new ArrayList<CircleForumVO>();
        }
        return circleForumVOs;
    }
}
