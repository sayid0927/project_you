package com.zxly.o2o.request;

import com.easemob.easeui.AppException;

/**
 * @author fengrongjian 2015-12-28
 * @description 用户信息编辑网络请求
 */
public class PersonalUserEditRequest extends BaseRequest {

    public PersonalUserEditRequest(long birthday) {
        addParams("birthday", birthday);
    }

    public PersonalUserEditRequest(byte gender) {
        addParams("gender", gender);
    }

    public PersonalUserEditRequest(String nickName, String signature, String provinceId, String cityId) {
        if (nickName != null) {
            addParams("nickname", nickName);
        }
        if (signature != null) {
            addParams("signature", signature);
        }
        if (provinceId != null) {
            addParams("provinceId", provinceId);
        }
        if (cityId != null) {
            addParams("cityId", cityId);
        }
    }

    @Override
    protected void fire(String data) throws AppException {
    }

    @Override
    protected String method() {
        return "shop/shopUser/edit";
    }

}
