package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.model.UserBankCard;
import com.zxly.o2o.util.Constants;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengrongjian 2015-5-22
 * @description 我的帐户相关信息网络请求
 */
public class PayMyAccountRequest extends BaseRequest {
    private String accountBalance = null;
    private ArrayList<UserBankCard> bankcardsList = new ArrayList<UserBankCard>();
    private ArrayList<UserBankCard> savingsBankcardList = new ArrayList<UserBankCard>();
    private ArrayList<UserBankCard> takeoutBankcardList = new ArrayList<UserBankCard>();
    private int isUserPaw;

    public PayMyAccountRequest() {
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject json = new JSONObject(data);
            if (json.has("accountBalance")) {
                accountBalance = json.getString("accountBalance");
            }
            if (json.has("isUserPaw")) {
                isUserPaw = json.getInt("isUserPaw");
            }
            if (json.has("userBankCardList")) {
                String bankcardListJson = json.getString("userBankCardList");

                TypeToken<List<UserBankCard>> type = new TypeToken<List<UserBankCard>>() {
                };
                List<UserBankCard> bankcardList = GsonParser.getInstance()
                        .fromJson(bankcardListJson, type);
                if (!listIsEmpty(bankcardList)) {
                    bankcardsList.addAll(bankcardList);
                }
                for (UserBankCard ubc : bankcardsList) {
                    if (Constants.TYPE_BANKCARD_SAVINGS == ubc.getBankType()) {
                        if (1 == ubc.getWithdrawType()) {
                            takeoutBankcardList.add(ubc);
                        } else {
                            savingsBankcardList.add(ubc);
                        }
                    }
                }
                if (!savingsBankcardList.isEmpty()) {
                    takeoutBankcardList.addAll(savingsBankcardList);
                }
            }
        } catch (Exception e) {
            throw new AppException("数据解析异常");
        }
    }

    @Override
    protected String method() {
        return "pay/accountDetail";
    }

    public Float getAccountBalance() {
        if(accountBalance != null){
            return Float.parseFloat(accountBalance);
        }
        return 0.0f;
    }

    public ArrayList<UserBankCard> getBankcardList() {
        return bankcardsList;
    }

    public ArrayList<UserBankCard> getSavingsBankcardList() {
        return savingsBankcardList;
    }

    public ArrayList<UserBankCard> getTakeoutBankcardList() {
        return takeoutBankcardList;
    }

    public int getIsUserPaw() {
        return this.isUserPaw;
    }
}
