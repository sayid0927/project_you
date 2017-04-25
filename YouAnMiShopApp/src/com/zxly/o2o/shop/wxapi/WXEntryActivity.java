package com.zxly.o2o.shop.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.ShareListener;

/**
 * Created by kenwu on 2015/9/19.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;

    public static ShareListener shareListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, false);
        api.handleIntent(getIntent(), this);
    }


    protected final void onNewIntent(Intent paramIntent) {
        super.onNewIntent(paramIntent);
        setIntent(paramIntent);
        api.handleIntent(paramIntent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        // 微信发送请求到第三方应用时，会回调到该方法 (暂时不做处理)
        this.finish();
    }


    /**
     * 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
     * @param resp
     */
    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if(shareListener!=null)
                    shareListener.onComplete("");
                break;

            case BaseResp.ErrCode.ERR_USER_CANCEL:
                if(shareListener!=null)
                    shareListener.onFail(-1);
                break;

            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                if(shareListener!=null)
                    shareListener.onFail(-1);
                break;

            default:
                if(shareListener!=null)
                    shareListener.onFail(-1);
                break;
        }

        this.finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        shareListener=null;
    }





}
