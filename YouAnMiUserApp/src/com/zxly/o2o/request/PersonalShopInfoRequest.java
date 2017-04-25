package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.zxly.o2o.model.PersonalShopInfo;

/**
 * @author fengrongjian 2015-12-25
 * @description 门店相关信息网络请求
 */
public class PersonalShopInfoRequest extends BaseRequest {
    private PersonalShopInfo personalShopInfo;

    public PersonalShopInfoRequest(long shopId) {
        addParams("shopId", shopId);
    }

    @Override
    protected void fire(String data) throws AppException {
        personalShopInfo = GsonParser.getInstance().getBean(data, PersonalShopInfo.class);
    }

    @Override
    protected String method() {
        return "shop/info";
    }

    public PersonalShopInfo getPersonalShopInfo() {
        return personalShopInfo;
    }
}
