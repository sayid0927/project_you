package com.zxly.o2o.request;

import android.os.Build;
import android.util.Log;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.AppFeature;
import com.zxly.o2o.model.ShopInfo;
import com.zxly.o2o.util.AppLog;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.StringUtil;

import org.json.JSONObject;

public class AppStartRequest extends BaseRequest {

    public AppFeature appFeature;
    private String shareUrl;


    public AppStartRequest() {
        if (Account.user != null) {
            addParams("userId", Account.user.getId());
        }

        addParams("shopId", Config.shopId);
        addParams("phoneBrand", Build.BRAND);
        addParams("phoneModel", Build.MODEL);
        if(Config.servieProvider != 0) {
            addParams("serviceProvider", Config.servieProvider);
        }
        if (!StringUtil.isNull(Config.promotionCode)) {
            addParams("promotionCode", Config.promotionCode);
        }
        addParams("isFirst", PreferUtil.getInstance().getIsFirstOpen()?1:0);
    }

    @Override
    protected void fire(String data) throws AppException {
        try {

            JSONObject json = new JSONObject(data);
            Log.e("TAG",json+"==============");
            Account.user = Account.readLoginUser(AppController.getInstance().getTopAct());
            if (json.has("appFeature")) {
                String result = json.getString("appFeature");
                appFeature = GsonParser.getInstance().getBean(result, AppFeature.class);
                Account.appFeature = appFeature;
            }

            shareUrl = json.optString("shareUrl");
            Account.shareUrl = shareUrl + "sys/dll?shopId=" + Config.shopId + "&appType=" + Constants.APP_USER_TYPE;

            if (json.has("shopInfo")) {
                String result = json.getString("shopInfo");
                Account.shopInfo = GsonParser.getInstance().getBean(result, ShopInfo.class);
            }
            Config.discountUrl = json.optString("newDiscountUrl");
            Config.newDiscountDetailUrl = json.optString("newDiscountDetailUrl");
            Config.h5Url = json.optString("h5Url");
            //文章优化新增参数
            if(json.has("articleUrl")){
                Config.articleUrl=json.optString("articleUrl");
            }

        } catch (Exception e) {
            AppLog.d("errorInfo", "-->" + e.toString());
            throw new AppException("appstart 解析出错", e);
        }


    }

    @Override
    protected String method() {
        return "shop/appstart";
    }

}
