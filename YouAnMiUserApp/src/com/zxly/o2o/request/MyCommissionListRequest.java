package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.MyCommissionVO;

/**
 * Created by Benjamin on 2015/7/20.
 */
public class MyCommissionListRequest extends BaseRequest {
    public MyCommissionVO myCommissionVO = new MyCommissionVO();

    public MyCommissionListRequest(int pageIndex, int pageType) {
        addParams("pageIndex", pageIndex);
        addParams("pageType", pageType);
        addParams("userId", Account.user.getId());
        addParams("shopId", Config.shopId);
    }

    @Override
    protected void fire(String data) throws AppException {
        myCommissionVO = GsonParser.getInstance()
                .getBean(data, MyCommissionVO.class);
    }

    @Override
    protected String method() {
        return "/popu/userCommissionList/list";
    }
}
