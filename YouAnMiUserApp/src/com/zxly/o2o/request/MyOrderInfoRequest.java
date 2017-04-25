package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.OrderInfo;
import com.zxly.o2o.util.AppLog;

/**
 * Created by Administrator on 2015/5/25.
 */
public class MyOrderInfoRequest extends BaseRequest {

    private OrderInfo orderInfo;
    public MyOrderInfoRequest(String orderNumber){
        addParams("orderNo",orderNumber);
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            orderInfo= GsonParser.getInstance().getBean(data,OrderInfo.class);
        } catch (AppException e) {
            e.printStackTrace();
        }
    }

    
    @Override
    protected String method() {
        return "//order//detail";
    }
    
	@Override
	public String getUrl() {
		String url = super.getUrl();
		AppLog.d("url", "-->" + url + "  imei=" + Config.imei);
		return url;
	}
}
