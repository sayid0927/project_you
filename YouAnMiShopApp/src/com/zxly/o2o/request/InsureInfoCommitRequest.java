package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.util.ViewUtils;

/**
 * Created by Administrator on 2016/5/13.
 */
public class InsureInfoCommitRequest extends BaseRequest {

    public InsureInfoCommitRequest(long id, String imageUrl, String phonePrice, long shopId, String
            thumImageUrl) {
        addParams("thumImageUrl", thumImageUrl);
        addParams("shopId", shopId);
        addParams("phonePrice", phonePrice);
        addParams("imageUrl", imageUrl);
        addParams("id", id);
    }



    @Override
    protected void fire(String data) throws AppException {

        super.fire(data);

    }

    @Override
    protected String method() {
        return "/insurance/order/supplement";
    }
}
