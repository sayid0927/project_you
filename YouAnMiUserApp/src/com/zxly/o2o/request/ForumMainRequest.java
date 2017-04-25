package com.zxly.o2o.request;

import android.util.Log;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.CircleForumVO;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.PreferUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/21.
 */
public class ForumMainRequest extends BaseRequest {
    public List<CircleForumVO> copyList = new ArrayList<CircleForumVO>();
    /*关注的圈子*/
    public List<CircleForumVO> concernCircles = new ArrayList<CircleForumVO>();
    /*推荐的圈子*/
    public List<CircleForumVO> recommendCircles = new ArrayList<CircleForumVO>();
    /*门店的圈子*/
    public CircleForumVO shopCircle;
    public int recommendSize;


    public ForumMainRequest() {
        addParams("shopId", Config.shopId);
    }

    @Override
    protected String method() {
        return "/user/circle/index";
    }

    @Override
    protected void fire(String data) throws AppException {
        TypeToken<List<CircleForumVO>> token = new TypeToken<List<CircleForumVO>>() {
        };
        JSONObject jo;
        try {
            jo = new JSONObject(data);
            String getData;
            if (jo.has("concernCircle")) {
                getData = jo.getString("concernCircle");
                concernCircles = GsonParser.getInstance().fromJson(getData, token);
            }
            if (jo.has("recommendCircle")) {
                getData = jo.getString("recommendCircle");
                recommendCircles = GsonParser.getInstance().fromJson(getData, token);
            }
            if (jo.has("shopCircle")) {
                getData = jo.getString("shopCircle");
                shopCircle = GsonParser.getInstance().fromJson(getData, new TypeToken<CircleForumVO>() {
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        copyList.clear();
        //获取置顶列表
        String idList = PreferUtil.getInstance().getForumTopList();
        //把置顶放顶部
        if (!"".equals(idList)) {
            String[] ids = idList.split(",");
            for (String anIdList : ids) {
                for (CircleForumVO circleForumVO : concernCircles) {
                    if (circleForumVO.getId() == Long.valueOf(anIdList)) {
                        circleForumVO.setIsTop(1);
                        copyList.add(circleForumVO);
                        concernCircles.remove(circleForumVO);
                        break;
                    }
                }
            }
        }
        copyList.addAll(concernCircles);
        concernCircles.clear();

        if(shopCircle!=null&&shopCircle.getId()!=0)
        {
            shopCircle.setName(
                    AppController.getInstance().getResources().getString(R.string.app_name)+Constants
                            .FORUM_CIRCLE_TITLE);
            shopCircle.setInitialLetter("我的圈子");
            copyList.add(0, shopCircle);
        }else if(copyList.size()>0){
            copyList.get(0).setInitialLetter("我的圈子");
        }


        if (recommendCircles.size() > 0) {
            recommendCircles.get(0).setInitialLetter("推荐的圈子");
        }

        copyList.addAll(recommendCircles);
        recommendSize=recommendCircles.size();
        recommendCircles.clear();
    }
}
