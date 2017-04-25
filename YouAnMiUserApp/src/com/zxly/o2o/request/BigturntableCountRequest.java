package com.zxly.o2o.request;

/**
 * 大转盘统计接口
 * Created by kenwu on 2015/10/22.
 */
public class BigturntableCountRequest extends BaseRequest {


    public BigturntableCountRequest(Long userID,Long drawId){
        addParams("userId",userID);
        addParams("drawId",drawId);
    }

    @Override
    protected String method() {
        return "/H5Draw/share";
    }

}
