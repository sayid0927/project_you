package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.User;
import com.zxly.o2o.model.UserOrder;

import org.json.JSONObject;

/**
 * @author fengrongjian 2015-12-16
 * @description 我主页相关信息网络请求
 */
public class PersonalInitRequest extends BaseRequest {
    private String userTrans;
    private User user;
    private UserOrder userOrder;

    public PersonalInitRequest(long userId, long shopId) {
        addParams("id", userId);
        addParams("shopId", shopId);
    }



    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject json = new JSONObject(data);
            if (json.has("userTrans")) {
                userTrans = json.getString("userTrans");
            }
            if (json.has("userOrder")) {
                userOrder = GsonParser.getInstance().getBean(json.getString("userOrder"), UserOrder.class);
            }
            if (json.has("userInfo")) {
                user = GsonParser.getInstance().getBean(json.getString("userInfo"), User.class);
                long belongId = Account.user.getBelongId();
                if(belongId == 0 || belongId != user.getBelongId()){
                    User newUser = Account.readLoginUser(AppController.getInstance().getTopAct());
                    newUser.setBelongId(user.getBelongId());
                    Account.user = newUser;
                    Account.saveLoginUser(AppController.getInstance().getTopAct(), newUser);
                }
            }
        } catch (Exception e) {
            throw new AppException("数据解析异常");
        }
    }

    @Override
    protected String method() {
        return "me/init";
    }

    public float getAccountBalance() {
        if (userTrans != null) {
            try {
                return Float.parseFloat(userTrans);
            } catch (Exception e) {
                return 0.0f;
            }
        }
        return 0.0f;
    }

    public User getUser() {
        return user;
    }

    public UserOrder getUserOrder() {
        return userOrder;
    }
}
