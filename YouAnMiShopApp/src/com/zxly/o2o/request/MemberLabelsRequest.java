package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.TagModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengrongjian on 2016/9/20.
 * 店铺用户标签列表
 */
public class MemberLabelsRequest extends BaseRequest {
    private List<TagModel> memberLabelList = new ArrayList<TagModel>();

    public MemberLabelsRequest() {
        addParams("shopId", Account.user.getShopId());
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            TypeToken<List<TagModel>> type = new TypeToken<List<TagModel>>() {
            };
            memberLabelList = GsonParser.getInstance().fromJson(data, type);
        } catch (Exception e) {
            throw new AppException("数据解析异常");
        }
    }

    public List<TagModel> getMemberLabelList() {
        return memberLabelList;
    }

    @Override
    protected String method() {
        return "/keduoduo/user/labels";
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return false;
    }
}
