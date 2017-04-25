package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.adapter.EaseContactAdapter;
import com.easemob.easeui.domain.EaseUser;
import com.easemob.easeui.model.Commissions;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.util.PreferUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/11.
 */
public class ContactCommissionListRequest extends BaseRequest {
    @Override
    protected String method() {
        return "/commission/user/list";
    }

    public ContactCommissionListRequest() {
        addParams("shopId", Account.user.getShopId());
    }

    @Override
    protected void fire(String data) throws AppException {
        JSONObject jo;
        try {
            jo = new JSONObject(data);

            //根据佣金列表接口返回的联系人更新时间判断是否需要更新联系人
            if (jo.has("registUserUpdateTime")) {
                long contactUpdateTime = jo.getLong("registUserUpdateTime");
                if (contactUpdateTime > PreferUtil.getInstance().getContactUpdateTime()) {
                    PreferUtil.getInstance().setContactUpdateTime(contactUpdateTime);

                    if (Account.user != null) {
                        new IMGetContactListRequest(Account.user.getShopId())
                                .start(); //reloading contact from server
                    }

                }
            }
            if (jo.has("commissions")) {
                data = jo.getString("commissions");
            } else {
                return;
            }

            TypeToken<List<Commissions>> token = new TypeToken<List<Commissions>>() {
            };

            EaseContactAdapter.commissionMap.clear();
            for (Commissions commissions : GsonParser.getInstance().fromJson(data, token)) {
                EaseContactAdapter.commissionMap.put(commissions.getContributeId(), commissions.getMoney());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
