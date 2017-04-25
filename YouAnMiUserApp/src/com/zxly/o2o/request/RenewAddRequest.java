package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.zxly.o2o.model.UserMaintain;
import com.zxly.o2o.util.AppLog;
import com.zxly.o2o.util.EncodingHandler;
import com.zxly.o2o.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengrongjian 2015-3-18
 * @description 保修单续保新增网络请求
 */
public class RenewAddRequest extends BaseRequest {

	private List<UserMaintain> renewList = new ArrayList<UserMaintain>();

	public RenewAddRequest(Long shopId, Long maintainId, Long deadlineId) {
		addParams("shopId", shopId);
		addParams("maintainId", maintainId);
		addParams("deadlineId", deadlineId);
	}

	@Override
	protected void fire(String data) throws AppException {
		AppLog.e("---renew add data---" + data);
		try {
			UserMaintain maintain= GsonParser.getInstance().getBean(data, UserMaintain.class);
			if(maintain!=null)
				maintain.setBitmap(EncodingHandler.createQRImage(StringUtil.generateScanInfo(maintain), 156, 156));
		} catch (Exception e) {
			throw new AppException("数据解析异常");
		}
	}

	@Override
	protected String method() {
		return "renew/add";
	}

	public List<UserMaintain> getRenewList() {
		return this.renewList;
	}
}
