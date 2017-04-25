package com.zxly.o2o.request;

import java.util.List;

/**
 * Created by hejun on 2016/9/28.
 */
public class SaveFansRemarkRequest extends BaseRequest{
    public SaveFansRemarkRequest(int buyIntention, int buyOffline, long fansId, int gender,
                                 List<String> labels,String name,String phone,String productName,String productPrice,String remarkContent){
        addParams("buyIntention",buyIntention);
        addParams("fansId",fansId);
        addParams("gender",gender);
        addParams("labels",labels);
        addParams("name",name);
        addParams("phone",phone);
        addParams("buyOffline",buyOffline);
        addParams("productName",productName);
        addParams("productPrice",productPrice);
        addParams("remarkContent",remarkContent);
    }
    @Override
    protected String method() {
        return "/keduoduo/fans/addRemark";
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }
}
