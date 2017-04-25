package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;

/**
 * Created by Administrator on 2015/12/21.
 */
public class ForumToConcernRequest extends BaseRequest {

    public ForumToConcernRequest(long id, int operate) {
        addParams("id", id);
        addParams("operate", operate);  //1.表示关注，2.表示取消关注
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

    @Override
    protected String method() {
        return "/user/circle/concern";
    }

}
