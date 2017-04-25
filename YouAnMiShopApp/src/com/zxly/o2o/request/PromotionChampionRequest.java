package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.PromotionChampion;
import com.zxly.o2o.model.PromotionRanking;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengrongjian 2016-8-31
 * @description 获取, 商品，文章，app推广的第一名
 */
public class PromotionChampionRequest extends BaseRequest {
    private List<PromotionChampion> promotionChampionList = new ArrayList<PromotionChampion>();
    private List<PromotionRanking> promotionRankingList = new ArrayList<PromotionRanking>();

    public PromotionChampionRequest(int year, int month) {
        addParams("year", year);
        addParams("month", month);
        addParams("shopId", Account.user.getShopId());
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject jsonObject = new JSONObject(data);
            String list = jsonObject.getString("list");
            TypeToken<List<PromotionChampion>> championType = new TypeToken<List<PromotionChampion>>() {
            };
            promotionChampionList = GsonParser.getInstance().fromJson(list, championType);
            String myList = jsonObject.getString("myList");
            TypeToken<List<PromotionRanking>> rankingType = new TypeToken<List<PromotionRanking>>() {
            };
            promotionRankingList = GsonParser.getInstance().fromJson(myList, rankingType);

            for (PromotionRanking promotionRanking : promotionRankingList) {
                for (PromotionChampion promotionChampion : promotionChampionList) {
                    if (promotionChampion.getType() == promotionRanking.getType()) {
                        promotionChampion.setRanking(promotionRanking.getRanking());
                    }
                }
            }
        } catch (Exception e) {
            throw new AppException("数据解析异常");
        }
    }

    @Override
    protected String method() {
        return "task/getPushFirsts";
    }

    public List<PromotionChampion> getPromotionChampionList() {
        return promotionChampionList;
    }

    public List<PromotionRanking> getPromotionRankingList() {
        return promotionRankingList;
    }
}
