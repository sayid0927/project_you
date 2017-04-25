package com.zxly.o2o.shop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.ShareListener;
import com.zxly.o2o.util.ViewUtils;

/**
 * 用于接收分享回调
 * Created by kenwu on 2015/9/24.
 */
public class WBShareActivity extends Activity implements IWeiboHandler.Response {

    /** 微博微博分享接口实例 */
    private IWeiboShareAPI mWeiboShareAPI = null;

    private static ShareListener shareListener;

    private static WeiboMultiMessage weiboMessage;

    /**
     * @see {@link Activity#onCreate}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try{
            // 创建微博分享接口实例
            mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Constants.XLWB_APP_KEY);

            // 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
            // NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
            mWeiboShareAPI.registerApp();

            // 当 Activity 被重新初始化时（该 Activity 处于后台时，可能会由于内存不足被杀掉了），
            // 需要调用 {@link IWeiboShareAPI#handleWeiboResponse} 来接收微博客户端返回的数据。
            // 执行成功，返回 true，并调用 {@link IWeiboHandler.Response#onResponse}；
            // 失败返回 false，不调用上述回调
            if (savedInstanceState != null) {
                mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
            }

            send();
        }catch (Exception e){

          //  ViewUtils.showToast("垃圾新浪 -->"+e.getMessage());
        }

    }

    /**
     * @see {@link Activity#onNewIntent}
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
        mWeiboShareAPI.handleWeiboResponse(intent, this);
    }

    /**
     * 接收微客户端博请求的数据。
     * 当微博客户端唤起当前应用并进行分享时，该方法被调用。
     *
     * @see {@link IWeiboShareAPI#handleWeiboRequest}
     */
    @Override
    public void onResponse(BaseResponse baseResp) {
        switch (baseResp.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                if (shareListener != null)
                    shareListener.onComplete("");
                ViewUtils.showToast("发送成功");
                break;

            case WBConstants.ErrorCode.ERR_CANCEL:
                if (shareListener != null)
                    shareListener.onFail(-1);
                ViewUtils.showToast("quxiao ");
                break;

            case WBConstants.ErrorCode.ERR_FAIL:
                if (shareListener != null)
                    shareListener.onFail(-1);
                ViewUtils.showToast("发送shibai");
                break;
        }

      //  finish();
    }

    private void send(){
        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;

        // 3. 发送请求消息到微博，唤起微博分享界面
        AuthInfo authInfo = new AuthInfo(this, Constants.XLWB_APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(this);
        String token = accessToken.getToken();

        mWeiboShareAPI.sendRequest(this, request, authInfo, token, new WeiboAuthListener() {

            @Override
            public void onWeiboException(WeiboException arg0) {
            }

            @Override
            public void onComplete(Bundle bundle) {
                Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                AccessTokenKeeper.writeAccessToken(WBShareActivity.this, newToken);
            }

            @Override
            public void onCancel() {
            }

        });
    }


    public static void start(Context context,ShareListener shareListener,WeiboMultiMessage weiboMessage){
        WBShareActivity.shareListener=shareListener;
        WBShareActivity.weiboMessage=weiboMessage;
        Intent it=new Intent();
        it.setClass(context,WBShareActivity.class);
        context.startActivity(it);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shareListener=null;
        weiboMessage=null;
    }
}
