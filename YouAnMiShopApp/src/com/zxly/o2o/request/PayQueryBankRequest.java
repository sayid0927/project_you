package com.zxly.o2o.request;


import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.model.BranchBank;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengrongjian 2016-3-7
 * @description 支行信息网络请求
 */
public class PayQueryBankRequest extends BaseRequest {
    private ArrayList<BranchBank> branchBankList = new ArrayList<BranchBank>();
    private boolean isShowLoadingDialog;

    public PayQueryBankRequest(String brabankName, String cityCode, String cardNo, String bankCode) {
        if (brabankName != null) {
            addParams("brabankName", brabankName);
        }
        addParams("cityCode", cityCode);
        addParams("cardNo", cardNo);
        addParams("bankCode", bankCode);
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject json = new JSONObject(data);
            if (json.has("card_list")) {
                String branchBankJson = json.getString("card_list");

                TypeToken<List<BranchBank>> type = new TypeToken<List<BranchBank>>() {
                };
                List<BranchBank> branchList = GsonParser.getInstance()
                        .fromJson(branchBankJson, type);
                if (!listIsEmpty(branchList)) {
                    branchBankList.addAll(branchList);
                }
            }

        } catch (Exception e) {
            throw new AppException("数据解析异常");
        }
    }

    @Override
    protected String method() {
        return "pay/CNAPSCodeQuery";
    }

    public void setShowLoadingDialog(){
        this.isShowLoadingDialog = true;
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return isShowLoadingDialog;
    }

    public ArrayList<BranchBank> getBranchBankList() {
        return branchBankList;
    }
}
