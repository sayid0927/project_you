package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.ActivityVO;

import java.util.List;

/**
 * Created by Administrator on 2016/1/11.
 */
public class ActivityListRequest extends BaseRequest {
    private List<ActivityVO> activityVOs;

    public ActivityListRequest(int pageIndex) {
        addParams("pageIndex", pageIndex);
        addParams("userId", Account.user.getId());
        addParams("shopId",  Account.user==null?0:Account.user.getShopId());
    }
    public ActivityListRequest(String key,int pageIndex) {
        addParams("pageIndex", pageIndex);
        addParams("userId", Account.user.getId());
        addParams("shopId",  Account.user==null?0:Account.user.getShopId());
        addParams("key",key);
    }


    @Override
    protected String method() {
        return "/makeFans/shopActivityList";
//        return "/makeFans/popuArticle";
    }

    @Override
    protected void fire(String data) throws AppException {
        TypeToken<List<ActivityVO>> types = new TypeToken<List<ActivityVO>>() {};
        activityVOs= GsonParser.getInstance().fromJson(data, types);
    }

    public List<ActivityVO> getActivityVOs() {
        return activityVOs;
    }
}
