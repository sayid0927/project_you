package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.model.TradingRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengrongjian 2015-5-22
 * @description 用户资金记录网络请求
 */
public class PayAccountRecordRequest extends BaseRequest {
	private ArrayList<TradingRecord> mTradingRecordList = new ArrayList<TradingRecord>();

	public PayAccountRecordRequest(int type, int pageIndex, int pageSize) {
		addParams("type", type);
		addParams("pageIndex", pageIndex);
		addParams("pageSize", pageSize);
	}
	
	@Override
	protected void fire(String data) throws AppException {
		try {
			TypeToken<List<TradingRecord>> type = new TypeToken<List<TradingRecord>>() {
			};
			List<TradingRecord> tradingRecord = GsonParser.getInstance()
					.fromJson(data, type);
			if (!listIsEmpty(tradingRecord)) {
				mTradingRecordList.addAll(tradingRecord);
			}
		} catch (Exception e) {
			throw new AppException("数据解析异常");
		}
	}

	@Override
	protected String method() {
		return "pay/record";
	}

	public ArrayList<TradingRecord> getTradingRecordList() {
		return mTradingRecordList;
	}

}
