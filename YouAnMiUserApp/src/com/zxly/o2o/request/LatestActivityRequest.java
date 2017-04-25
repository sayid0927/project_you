package com.zxly.o2o.request;

import android.util.Log;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.GetuiMsg;
import com.zxly.o2o.model.OrderInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/7/13.
 */
public class LatestActivityRequest extends BaseRequest{

    private GetuiMsg getuiMsg;

    public LatestActivityRequest(){
        addParams("clientId", Config.getuiClientId);
        addParams("shopId", Config.shopId);
        if(Account.user!=null)
        {
            addParams("userId", Account.user.getId());
        }
    }
    @Override
    protected void fire(String data) throws AppException {
        try {
            if(data!=null){
                JSONObject jo = new JSONObject(data);
                getuiMsg = GsonParser.getInstance().getBean(jo.getString("expend"),GetuiMsg.class);
            }else {
                getuiMsg=new GetuiMsg();
            }
        } catch (JSONException e) {
            throw new AppException("数据解析异常");
        }
    }

    @Override
    protected String method() {
        return "activity/latest";
    }


    /**
     * 返回活动数据
     * @return
     */
    public GetuiMsg getGetuiMsg() {
        return getuiMsg;
    }

}
