package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.FansBehavior;
import com.zxly.o2o.model.RemarkBaseInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengrongjian on 2016/9/20.
 * 获取粉丝消费/行为轨迹
 */
public class FansBehaviorRequest extends BaseRequest {
    private final boolean showLoading;
    private List<RemarkBaseInfo> fansBehaviorList = new ArrayList<RemarkBaseInfo>();

    public FansBehaviorRequest(long fansId, int recordType, int pageSize, int pageIndex,boolean showLoading) {
        this.showLoading=showLoading;
        addParams("fansId", fansId);
        addParams("shopId", Account.user.getShopId());
        addParams("recordType", recordType);
        addParams("pageIndex", pageIndex);
        addParams("pageSize", pageSize);
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            TypeToken<List<RemarkBaseInfo>> type = new TypeToken<List<RemarkBaseInfo>>() {
            };
            fansBehaviorList = GsonParser.getInstance().fromJson(data, type);
        } catch (Exception e) {
            throw new AppException("数据解析异常");
        }
    }

    public List<RemarkBaseInfo> getFansBehaviorList() {
        return fansBehaviorList;
    }

    @Override
    protected String method() {
        return "keduoduo/fans/behaviorRecords";
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return showLoading;
    }
}
