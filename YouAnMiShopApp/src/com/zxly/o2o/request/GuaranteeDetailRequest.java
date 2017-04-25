package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.GuaranteeInfo;

import org.json.JSONObject;

/**
 * Created by kenwu on 2016/4/27.
 */
public class GuaranteeDetailRequest extends BaseRequest{

    private GuaranteeInfo guaranteeInfo;

    public GuaranteeInfo getGuaranteeInfo() {
        return guaranteeInfo;
    }

    public GuaranteeDetailRequest(String id){
        addParams("id",id);
        addParams("shopId", Account.user.getShopId());
    }


    @Override
    protected void fire(String data) throws AppException {

        try {
            JSONObject jsonObject=new JSONObject(data);

            if(jsonObject.has("order")){
                guaranteeInfo= GsonParser.getInstance().getBean(jsonObject.getString("order"),GuaranteeInfo.class);
            }

        }catch (Exception e){

        }
    }

    @Override
    protected String method() {
        return "/insurance/order/detail";
    }
}
