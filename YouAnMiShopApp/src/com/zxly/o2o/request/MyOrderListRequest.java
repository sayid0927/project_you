package com.zxly.o2o.request;

import android.util.Log;

import java.util.List;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.MyOrderAct;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.model.OrderInfo;
import com.zxly.o2o.model.OrderStatistics;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Administrator on 2015/5/25.
 */
public class MyOrderListRequest extends BaseRequest {

    private List<OrderInfo> orderList;
    private OrderStatistics statics;
    private int type;
    public MyOrderListRequest(int status,int pageIndex,int type){
     //   addParams("shopId",Account.shopInfo.getId());
    	this.type=type;
        addParams("shopId", 1);
        addParams("status",status);
        addParams("pageIndex",pageIndex);
    }

    public List<OrderInfo> getOrderList() {
        return orderList;
    }

    public OrderStatistics getStatics() {
        return statics;
    }

    @Override
    protected void fire(String data) throws AppException {
        if(type!=MyOrderAct.TYPE_ORDER_LIST){
            TypeToken<List<OrderInfo>> types = new TypeToken<List<OrderInfo>>() {};
            orderList= GsonParser.getInstance().fromJson(data, types);
            return;
        }
        JSONObject json = null;
        try {
            json = new JSONObject(data);

            if (json.has("statics")) {
                String str = json.getString("statics");
                statics= GsonParser.getInstance().getBean(str,OrderStatistics.class);
            }

            if (json.has("orderList")) {
                String str = json.getString("orderList");
                TypeToken<List<OrderInfo>> types = new TypeToken<List<OrderInfo>>() {};
                orderList= GsonParser.getInstance().fromJson(str, types);
            }

            if(type!=MyOrderAct.TYPE_ORDER_LIST){
                TypeToken<List<OrderInfo>> types = new TypeToken<List<OrderInfo>>() {};
                orderList= GsonParser.getInstance().fromJson(data, types);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected String method() {
        Log.d("loadType", "lll->" + type);
    	if(type==MyOrderAct.TYPE_ORDER_LIST){
            return "/shop/order/list";
    	}
		return "/shop/delivery/list";
    }
    
	@Override
	public String getUrl() {
		String url = super.getUrl();
		Log.d("url", "-->" + url + "  imei=" + AppController.imei);
		return url;
	}
}
