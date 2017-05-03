package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * fengrongjian 2016-10-27
 * 分享地址统一管理接口-获取下载页地址（扫码链接）
 */
public class CommonShareRequest extends BaseRequest {
    private String shareUrl;
    private String h5HomeURL;

    public CommonShareRequest() {
        addParams("userId", Account.user.getId());
        addParams("shopId", Account.user.getShopId());
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject json = new JSONObject(data);
            if (json.has("url")) {
                shareUrl = json.getString("url");
            }
            if (json.has("h5HomeURL")) {
                h5HomeURL = json.getString("h5HomeURL");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public String getH5HomeURL() {
        return h5HomeURL;
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

    @Override
    protected String method() {
        return "common/share/url";
    }

}
