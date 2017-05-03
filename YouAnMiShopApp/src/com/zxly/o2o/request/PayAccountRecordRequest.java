package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.model.TradingRecord;
import com.zxly.o2o.util.TimeUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fengrongjian 2015-5-22
 * @description 用户资金记录网络请求
 */
public class PayAccountRecordRequest extends BaseRequest {
    public boolean hasNextPage;
    private ArrayList<TradingRecord> mTradingRecordList = new ArrayList<TradingRecord>();
    private Map<Object, List<Object>> tradingRecordList = new LinkedHashMap<Object, List<Object>>();

    public PayAccountRecordRequest(int type, int pageIndex, int pageSize, long shopId) {
        addParams("type", type);
        addParams("shopId", shopId);
        addParams("pageIndex", pageIndex);
        addParams("pageSize", pageSize);
    }

    public Map<Object, List<Object>> getTradingList() {
        return tradingRecordList;
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
            if(mTradingRecordList.size()<10){
                hasNextPage = false;
            } else {
                hasNextPage = true;
            }
        } catch (Exception e) {
            throw new AppException("数据解析异常");
        }
        try {
            JSONArray jsonArray = new JSONArray(data);
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                TradingRecord tradingRecord = new TradingRecord();
                if(jsonObject.has("orderNo")) {
                    tradingRecord.setOrderNo(jsonObject.getString("orderNo"));
                }
                tradingRecord.setNumberNo(jsonObject.getString("numberNo"));
                tradingRecord.setIncomeType(jsonObject.getInt("incomeType"));
                float money = Float.parseFloat(jsonObject.getString("money"));
                tradingRecord.setMoney(money);
                tradingRecord.setType(jsonObject.getInt("type"));
                tradingRecord.setSource(jsonObject.getInt("source"));
                tradingRecord.setPayName(jsonObject.getString("payName"));
                long time = jsonObject.getLong("createTime");
                tradingRecord.setCreateTime(time);
                if (jsonObject.has("bankName")) {
                    tradingRecord.setBankName(jsonObject.getString("bankName"));
                }
                if (jsonObject.has("acceptBankNumber")) {
                    tradingRecord.setAcceptBankNumber(jsonObject.getString("acceptBankNumber"));
                }
                String keyTime = TimeUtil.formatOrderTimeHHMM(time);
                if (!tradingRecordList.containsKey(keyTime)) {
                    List<Object> list = new ArrayList<Object>();
                    list.add(tradingRecord);
                    tradingRecordList.put(keyTime, list);
                } else {
                    tradingRecordList.get(keyTime).add(tradingRecord);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
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
