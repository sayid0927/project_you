package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.TakeStatistics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsnx on 2016/6/16.
 */
public class DiscountTakeCountRequest extends BaseRequest {
    private List<TakeStatistics> takeStatisticsList=new ArrayList<TakeStatistics>();
    public boolean hasNextPage;
    public DiscountTakeCountRequest (int pageIndex)
    {
        addParams("pageIndex",pageIndex);
        addParams("shopId", Account.user.getShopId());
    }

    public List<TakeStatistics> getTakeStatisticsList() {
        return takeStatisticsList;
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONArray jsonArray=new JSONArray(data);
            int len=jsonArray.length();
            for(int i=0;i<len;i++)
            {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                TakeStatistics takeStatistics=new TakeStatistics();
                takeStatistics.setDiscountInfo(jsonObject.optString("discountInfo"));
                takeStatistics.setDiscountType(jsonObject.optInt("discountType"));
                takeStatistics.setId(jsonObject.optInt("id"));
                takeStatistics.setTakePersons(jsonObject.optInt("takePersons"));
                takeStatistics.setUsePersons(jsonObject.optInt("usePersons"));
                takeStatisticsList.add(takeStatistics);
            }
            if(takeStatisticsList.size()<20){
                hasNextPage = false;
            } else {
                hasNextPage = true;
            }
        } catch (JSONException e) {
            throw JSONException(e);
        }
    }

    @Override
    protected String method() {
        return "/discount/take/count";
    }
}
