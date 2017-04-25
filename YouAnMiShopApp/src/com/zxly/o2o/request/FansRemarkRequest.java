package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.model.FansRemark;
import com.zxly.o2o.model.RemarkBaseInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengrongjian on 2016/9/20.
 * 获取粉丝备注列表
 */
public class FansRemarkRequest extends BaseRequest {
    private final boolean showDialog;
    private List<RemarkBaseInfo> fansRemarkList = new ArrayList<RemarkBaseInfo>();

    public FansRemarkRequest(long fansId, int pageSize, int pageIndex,boolean showDialog) {
        this.showDialog=showDialog;
        addParams("fansId", fansId);
        addParams("pageIndex", pageIndex);
        addParams("pageSize", pageSize);
//        addParams("shopId", Account.user.getShopId());
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            TypeToken<List<RemarkBaseInfo>> type = new TypeToken<List<RemarkBaseInfo>>() {
            };
            fansRemarkList = GsonParser.getInstance().fromJson(data, type);
        } catch (Exception e) {
            throw new AppException("数据解析异常");
        }
    }

    public List<RemarkBaseInfo> getFansRemarkList() {
        return fansRemarkList;
    }

    @Override
    protected String method() {
        return "keduoduo/fans/remarks";
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return showDialog;
    }
}
