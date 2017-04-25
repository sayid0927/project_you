package com.zxly.o2o.request;

import android.util.Log;


import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.SalesmanRankingAct;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.model.CommissionRecord;
import com.zxly.o2o.model.RankingInfo;

import java.util.List;

/**
 * Created by kenwu on 2015/12/15.
 */
public class SalesmanRankingRequest extends BaseRequest {

    private List<RankingInfo> list;
    private int type;
    public SalesmanRankingRequest(int type,int year,int month) {
        this.type=type;
        addParams("shopId", Account.user.getShopId());
        addParams("year",year);
        addParams("month",month);
    }

    public List<RankingInfo> getList() {
        return list;
    }


    @Override
    protected void fire(String data) throws AppException {
        TypeToken<List<RankingInfo>> types = new TypeToken<List<RankingInfo>>() {};
        list = GsonParser.getInstance().fromJson(data, types);
    }



    @Override
    protected String method() {

        if(type== SalesmanRankingAct.PROMOTION_TYPE_PRODUCT){
            return "/shopRank/productRank";
        }else if(type== SalesmanRankingAct.PROMOTION_TYPE_ARTICLE){
            return "/shopRank/staffRank";
        }else {
            return "/shopRank/articleRank";
        }


    }

    @Override
    public String getUrl() {
        String url = super.getUrl();
        Log.d("url", "-->" + url + "  imei=" + AppController.imei);
        return url;
    }
}
