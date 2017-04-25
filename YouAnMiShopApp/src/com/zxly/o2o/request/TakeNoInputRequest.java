/*
 * 文件名：TakeNoInputRequest.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： TakeNoInputRequest.java
 * 修改人：wuchenhui
 * 修改时间：2015-7-10
 * 修改内容：新增
 */
package com.zxly.o2o.request;

import android.util.Log;

import com.easemob.easeui.AppException;
import com.zxly.o2o.application.AppController;

/**
 * TODO 添加类的一句话简单描述。
 * <p>
 * TODO 详细描述
 * <p>
 * TODO 示例代码
 * <pre>
 * </pre>
 * 
 * @author     wuchenhui
 * @version    YIBA-O2O 2015-7-10
 * @since      YIBA-O2O
 */
public class TakeNoInputRequest extends BaseRequest {

    public TakeNoInputRequest(String orderNo,String code){
        addParams("orderNo",orderNo);
        addParams("code",code);
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

    @Override
    protected void fire(String data) throws AppException {

    }

    @Override
    protected String method() {
        return "/shop/delivery";
    }
    
	@Override
	public String getUrl() {
		String url = super.getUrl();
		Log.d("url", "-->" + url + "  imei=" + AppController.imei);
		return url;
	}
}
