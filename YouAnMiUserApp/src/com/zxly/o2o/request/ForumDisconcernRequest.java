package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.CircleForumVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/23.
 */
public class ForumDisconcernRequest extends BaseRequest{
    public List<CircleForumVO> circleForumVOs = new ArrayList<CircleForumVO>();
    public ForumDisconcernRequest(int pageIndex) {
        addParams("pageIndex", pageIndex);
    }

    @Override
    protected String method() {
        return "/user/circle/list";
    }

    @Override
    protected void fire(String data) throws AppException {
        TypeToken<List<CircleForumVO>> token = new TypeToken<List<CircleForumVO>>() {
        };
        circleForumVOs = GsonParser.getInstance().fromJson(data, token);
    }
}
