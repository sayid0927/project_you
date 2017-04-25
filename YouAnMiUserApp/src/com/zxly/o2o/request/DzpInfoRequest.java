package com.zxly.o2o.request;

import com.easemob.easeui.AppException;

import org.json.JSONObject;

/**
 * @author fengrongjian 2016-7-19
 * @description 大转盘分享信息
 */
public class DzpInfoRequest extends BaseRequest {
    private String name;
    private String remark;

    public DzpInfoRequest(long productId, long shopId) {
//        addParams("imageURL", imageUrl);
        addParams("id", productId);
        addParams("shopID", shopId);
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject json = new JSONObject(data);
            if (json.has("name")) {
                name = json.getString("name");
            }
            if (json.has("remark")) {
                remark = json.getString("remark");
            }
        } catch (Exception e) {
            throw new AppException("数据解析异常");
        }
    }

    @Override
    protected String method() {
        return "shop/dazhuanpanInfo";
    }

    public String getName() {
        return name;
    }

    public String getRemark() {
        return remark;
    }

}
