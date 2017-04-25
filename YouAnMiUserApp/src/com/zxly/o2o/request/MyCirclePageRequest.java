/*
 * 文件名：MyCirclePageRequest.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： MyCirclePageRequest.java
 * 修改人：Administrator
 * 修改时间：2015-1-4
 * 修改内容：新增
 */
package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.zxly.o2o.model.MyCirclePage;

public class MyCirclePageRequest extends MyCircleRequest {

	

	public MyCirclePageRequest(long shopId) {
		addParams("id", shopId);
	}

	@Override
	protected void fire(String data) throws AppException {
		myCirclePageData = GsonParser.getInstance().getBean(data, MyCirclePage.class);
		if(myCirclePageData==null){
			myCirclePageData = new MyCirclePage();
		}
	}

	@Override
	protected String method() {
		return "/circle/init";
	}
}
