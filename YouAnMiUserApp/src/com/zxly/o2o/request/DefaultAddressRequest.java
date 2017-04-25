package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.zxly.o2o.model.UserAddress;

/**
 * Created by dsnx on 2015/12/29.
 */
public class DefaultAddressRequest extends BaseRequest {

    private UserAddress userAddress;
    public DefaultAddressRequest(){

    }


    @Override
    protected void fire(String data) throws AppException {
        userAddress= GsonParser.getInstance().getBean(data, UserAddress.class);
    }

    public UserAddress getUserAddress() {
        return userAddress;
    }
    @Override
    protected String method() {
        return "address/defaultAddress";
    }
}
