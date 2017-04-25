package com.zxly.o2o.shop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.ShareListener;

/**
 * 用于接收新浪登陆回调
 * Created by kenwu on 2015/9/24.
 */
public class WBAuthActivity extends Activity {

    private AuthInfo mAuthInfo;

    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
    private Oauth2AccessToken mAccessToken;

    /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
    private SsoHandler mSsoHandler;

    /**
     * @see {@link Activity#onCreate}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
        mAuthInfo = new AuthInfo(this, Constants.XLWB_APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        mSsoHandler = new SsoHandler(this, mAuthInfo);

        // SSO 授权, 仅客户端
       // mSsoHandler.authorizeClientSso(new AuthListener());

        // SSO 授权, ALL IN ONE   如果手机安装了微博客户端则使用客户端授权,没有则进行网页授权
        mSsoHandler.authorize(new AuthListener());

        /**
         *    其他登陆方式
         *
         *   手机短信授权 ：
         *   mSsoHandler.registerOrLoginByMobile("验证码登录",new AuthListener());
         *   title 短信注册页面title，可选，不传时默认为""验证码登录""。此处WeiboAuthListener 对象 listener
         *   可以是和sso 同一个 listener   回调对象 也可以是不同的。开发者根据需要选择
         *
         *   SSO 授权, 仅Web：
         *   mSsoHandler.authorizeWeb(new AuthListener());
         *
         *   用户登出：
         *        AccessTokenKeeper.clear(getApplicationContext());
         *        mAccessToken = new Oauth2AccessToken();
         *
         *        从 SharedPreferences 中读取上次已保存好 AccessToken 等信息，
         *        第一次启动本应用，AccessToken 不可用
         *        mAccessToken = AccessTokenKeeper.readAccessToken(this);
         *        mAccessToken.isSessionValid()
         *
         * **/
    }

    /**
     * 当 SSO 授权 Activity 退出时，该函数被调用。
     *
     * @see {@link Activity#onActivityResult}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }

    }

    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     *    该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);

            if (mAccessToken.isSessionValid()) {
                // 保存 Token 到 SharedPreferences
                AccessTokenKeeper.writeAccessToken(WBAuthActivity.this, mAccessToken);
                if(loginListener!=null)
                    loginListener.onComplete("");
            } else {
                if(loginListener!=null)
                    loginListener.onFail(-1);
                //失败
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
            }

            finish();
        }

        @Override
        public void onCancel() {
            //取消登陆
            if(loginListener!=null)
                loginListener.onFail(-1);

            finish();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            //登陆出错 可能签名或者包名有问题
            if(loginListener!=null)
                loginListener.onFail(-1);

            finish();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginListener=null;
    }

    private static ShareListener loginListener;
    public static void start(Context context,ShareListener loginListener){
        WBAuthActivity.loginListener=loginListener;
        Intent it=new Intent();
        it.setClass(context,WBAuthActivity.class);
        context.startActivity(it);
    }

}
