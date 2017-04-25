package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.fragment.CircleMainPageFragment;

/**
 * Created by Administrator on 2015/12/21.
 */
public class ForumToAllConcernsRequest extends BaseRequest {
    public ForumToAllConcernsRequest(String ids) {
        addParams("ids", ids);
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

    @Override
    protected String method() {
        return "/user/circle/conerns";
    }
}
