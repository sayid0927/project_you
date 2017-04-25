package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.model.IMUserInfoVO;
import com.easemob.easeui.utils.GsonParser;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.model.OperationInfo;
import com.zxly.o2o.model.ShopStatistic;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengrongjian 2015-12-16
 * @description 我主页相关信息网络请求
 */
public class PersonalInitRequest extends BaseRequest {
    private float accountBalance;
    private int insuranceOrderCount;
    private IMUserInfoVO user;
    private OperationInfo operationInfo = new OperationInfo();
    private List<ShopStatistic> operationInfoList = new ArrayList<ShopStatistic>();
    private boolean isShowOperationData = false;
    private String serialNum;

    public PersonalInitRequest(long userId, long shopId, long year, long month) {
        addParams("userId", userId);
        addParams("shopId", shopId);
        if (year != 0) {
            addParams("year", year);
        }
        if (month != 0) {
            addParams("month", month);
        }
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject json = new JSONObject(data);

            if (json.has("insuranceOrderCount")) {
                insuranceOrderCount = json.getInt("insuranceOrderCount");
            }

            if (json.has("accountBalance")) {
                accountBalance = (float) json.getDouble("accountBalance");
            }
            if (json.has("clickAmount")) {
                operationInfo.setClickAmount(json.getInt("clickAmount"));
                isShowOperationData = true;
            }
            if (json.has("installAmount")) {
                operationInfo.setInstallAmount(json.getInt("installAmount"));
            }
            if (json.has("registerAmount")) {
                operationInfo.setRegisterAmount(json.getInt("registerAmount"));
            }
            if (json.has("visitAmount")) {
                operationInfo.setVisitAmount(json.getInt("visitAmount"));
            }
            if (json.has("flowTurnover")) {
                operationInfo.setFlowTurnover((float) json.getDouble("flowTurnover"));
            }
            if (json.has("productTurnover")) {
                operationInfo.setProductTurnover((float) json.getDouble("productTurnover"));
            }
            getOperationData(operationInfo);
            if (json.has("userInfo")) {
                user = GsonParser.getInstance().getBean(json.getString("userInfo"), IMUserInfoVO.class);
                if(Account.user != null) {
                    IMUserInfoVO newUser = Account.readLoginUser(AppController.getInstance().getTopAct());
                    newUser.setMobilePhone(user.getMobilePhone());
                    newUser.setName(user.getName());
                    newUser.setUserName(user.getUserName());
                    newUser.setThumHeadUrl(user.getThumHeadUrl());
                    newUser.setOriginHeadUrl(user.getOriginHeadUrl());
                    newUser.setCardNo(user.getCardNo());
                    newUser.setSerialNum(user.getSerialNum());
//                    newUser.setPassword(user.getPassword());
                    Account.user = newUser;
                    Account.saveLoginUser(AppController.getInstance().getTopAct(), newUser);
                }
            }
        } catch (Exception e) {
            throw new AppException("数据解析异常");
        }
    }

    private List<ShopStatistic> getOperationData(OperationInfo operationInfo) {
        operationInfoList.add(new ShopStatistic("APP安装量", operationInfo.getInstallAmount()));
        operationInfoList.add(new ShopStatistic("APP注册量", operationInfo.getRegisterAmount()));
        operationInfoList.add(new ShopStatistic("店铺访客量", operationInfo.getVisitAmount()));
        operationInfoList.add(new ShopStatistic("商品浏览量", operationInfo.getClickAmount()));
        operationInfoList.add(new ShopStatistic("商品成交额", operationInfo.getProductTurnover()));
        operationInfoList.add(new ShopStatistic("流量成交额", operationInfo.getFlowTurnover()));
        return operationInfoList;
    }

    @Override
    protected String method() {
        return "shopApp/meIndex";
    }

    public IMUserInfoVO getUser() {
        return user;
    }

    public float getAccountBalance() {
        return accountBalance;
    }

    public List<ShopStatistic> getOperationInfoList() {
        return operationInfoList;
    }

    public boolean isShowOperationData() {
        return isShowOperationData;
    }

    public int getInsuranceOrderCount() {
        return insuranceOrderCount;
    }
}
