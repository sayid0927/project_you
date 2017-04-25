/*
 * 文件名：ServerTimeRequest.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： ServerTimeRequest.java
 * 修改人：wuchenhui
 * 修改时间：2015-4-24
 * 修改内容：新增
 */
package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.util.AppLog;
import com.zxly.o2o.util.TimeCutdownUtil;

import org.json.JSONObject;

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
 * @version    YIBA-O2O 2015-4-24
 * @since      YIBA-O2O
 */
public class ServerTimeRequest extends BaseRequest {

	private long serverTime;
	@Override
	protected String method() {
		return "/sys/now";
	}
	
	@Override
	protected void fire(String data) throws AppException {
		try {
			JSONObject	json = new JSONObject(data);		
			if (json.has("data")) {
				long time = json.optLong("data",0);	
				TimeCutdownUtil.serverTime=time;
				TimeCutdownUtil.changeTime=time-System.currentTimeMillis();
//				AppLog.d("serverTime", "time-->" + time + " 本地时间：" + TimeCutdownUtil.getCurrentTime(System.currentTimeMillis())
//						+ "  服务器时间 ：" + TimeCutdownUtil.getCurrentTime(time) + " 时间差 ：" + TimeCutdownUtil.changeTime);

			}	
		} catch (Exception e) {
            throw new AppException("系统时间解析出错", e);
		}
		

	}
	
	@Override
	public String getUrl() {
		String url = super.getUrl();
		AppLog.d("url", "-->" + url + "  imei=" + Config.imei);
		return url;
	}

}
