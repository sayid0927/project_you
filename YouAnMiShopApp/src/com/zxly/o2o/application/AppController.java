package com.zxly.o2o.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.bugtags.library.Bugtags;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chatuidemo.HXApplication;
import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.HXHelper;
import com.easemob.chatuidemo.utils.PreferenceManager;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.adapter.EaseContactAdapter;
import com.easemob.easeui.controller.EaseUI;
import com.easemob.easeui.model.IMUserInfoVO;
import com.tencent.tinker.loader.app.ApplicationLike;
import com.tinkerpatch.sdk.TinkerPatch;
import com.tinkerpatch.sdk.loader.TinkerPatchApplicationLike;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.cache.BitmapLruCache;
import com.zxly.o2o.request.IMGetContactListRequest;
import com.zxly.o2o.request.MultiPartStack;
import com.zxly.o2o.shop.BuildConfig;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.AppLog;
import com.zxly.o2o.util.AppUtil;
import com.zxly.o2o.util.FetchPatchHandler;
import com.zxly.o2o.util.ImeiUtil;
import com.zxly.o2o.util.MD5Util;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.ViewUtils;

import java.util.Stack;

import static com.bugtags.library.Bugtags.BTGInvocationEventNone;

/**
 * @author dsnx
 * @version YIBA-O2O 2014-12-25
 * @since YIBA-O2O
 */
public class AppController extends Application {

    private static RequestQueue mRequestQueue;

    private static RequestQueue mImageUploadQueue;

    private static AppController instance;

    public static ImageLoader imageLoader;

    public static DisplayMetrics displayMetrics;

    public static int mVersionCode;
    public static String mVersionName;
    public static String mBuild;
    public static String imei;

    public static Stack<Activity> actList = new Stack<Activity>();
    public static BitmapLruCache lruImageCache;

    private String userName = "";
    private String userPassword = "";
    private int loginToHxCount = 0;
    private ApplicationLike tinkerApplicationLike;

    @Override
    public void onCreate() {
        super.onCreate();
        Bugtags.start("beb9b4f14e72470fe0ad088b715ec421", this, BTGInvocationEventNone);
        instance = this;

        imei = ImeiUtil.getImei(instance);
        mImageUploadQueue = Volley.newRequestQueue(this, new MultiPartStack(this));
        lruImageCache = BitmapLruCache.instance();

        PreferUtil.getInstance().init(this);

        HXApplication.getInstance().onCreate(this,R.raw.sis_server);  //环信初始化
        setPkgVersion();
        displayMetrics = EaseUI.displayMetrics;
        mRequestQueue = EaseUI.requestQueue;
        imageLoader = new ImageLoader(mRequestQueue, lruImageCache);
        EaseUI.imageLoader = imageLoader;
        Config.init(this);

        //HXInit
        //判断当前的包是release还是debug包，release包供外部测试，崩溃信息打印到内存卡
        if (!AppUtil.isDebuggable(this)) {
            CrashHandler.getInstance().init(this);
        }

        if (BuildConfig.TINKER_ENABLE) {

            // 我们可以从这里获得Tinker加载过程的信息
            tinkerApplicationLike = TinkerPatchApplicationLike.getTinkerPatchApplicationLike();

            // 初始化TinkerPatch SDK, 更多配置可参照API章节中的,初始化SDK
            TinkerPatch.init(tinkerApplicationLike)
                    .reflectPatchLibrary()
                    .setPatchRollbackOnScreenOff(true)
                    .setPatchRestartOnSrceenOff(true);

            // 每隔3个小时去访问后台时候有更新,通过handler实现轮训的效果
            new FetchPatchHandler().fetchPatchWithInterval(3);
        }
    }

    /**
     * 得到版本号、版本名
     */
    private void setPkgVersion() {
        try {
            PackageInfo pkgInfo = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_CONFIGURATIONS);
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(
                    getPackageName(), PackageManager.GET_META_DATA);
            mBuild = appInfo.metaData.getString("build");
            mVersionCode = pkgInfo.versionCode;
            mVersionName = pkgInfo.versionName;

        } catch (NameNotFoundException e) {
            AppLog.e(e);
        }
    }

    public static void addAct(Activity act) {
        if (act != null) {
            actList.add(act);
            EaseUI.activityList = actList;
        }
    }

    public static void exitAct() {
        for (Activity act : actList) {
            act.finish();
        }
    }

    public  void exitAndGoToMainPage() {
        for(int i=0;i<actList.size()-1;i++){
            actList.pop().finish();
        }
    }

    public static AppController getInstance() {
        return instance;
    }


    public static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }

    public static void cancelAll(final Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static void cancelPublishAll(final Object tag) {
        if (mImageUploadQueue != null) {
            mImageUploadQueue.cancelAll(tag);
        }
    }

    public void addRequest(Request reqeust) {
        mRequestQueue.add(reqeust);
    }

    public void addImageUploadRequest(Request reqeust) {
        mImageUploadQueue.add(reqeust);
    }

    public static void remove(Activity act) {
        if (act != null) {
            actList.remove(act);
        }
    }

    public Activity getTopAct() {
        return actList.peek();
    }

    public Drawable getDrawable(String name) {
        return getResources().getDrawable(findResId(name));
    }

    public int findResId(String name) {
        Resources res = getResources();
        String resName = name;
        int i = name.indexOf(".");
        if (i != -1) {
            resName = name.substring(0, i);
        }
        return res.getIdentifier(
                this.getPackageName() + ":drawable/" + resName, null, null);
    }

    public void initHXAccount(IMUserInfoVO user, boolean isRelogin) {
        if (user != null) {
            EaseConstant.currentUser.getFirendsUserInfo().setId(user.getId());
            EaseConstant.currentUser.getFirendsUserInfo().setEid(HXApplication.getInstance().parseUserFromID(
                    user.getId(),HXConstant.TAG_SHOP));
            EaseConstant.currentUser.getFirendsUserInfo().setNickname(user.getNickname());
            EaseConstant.currentUser.getFirendsUserInfo().setThumHeadUrl(user.getThumHeadUrl());

            userName = EaseConstant.currentUser.getFirendsUserInfo().getEid();
            userPassword = MD5Util.GetMD5Code(
                    new StringBuffer("").append(user.getId()).append("_").append(
                            user.getUserName()).toString())
                    .substring(0, 10);

            if (isRelogin) { //是否重新登陆
                EaseContactAdapter.unRegistList.clear();
                HXHelper.getInstance().logout(true, null);
                new IMGetContactListRequest(user.getShopId()).start();
            } else {
                //检查联系人有效期
                                checkIsNeedUpdateContact(user);
            }
            if (!HXHelper.getInstance().isLoggedIn()) {
                Login();
            }

        }
    }

    private void checkIsNeedUpdateContact(IMUserInfoVO user) {
        // do this if mistiming last over one day
        if (true||System.currentTimeMillis() - PreferenceManager.getInstance().getMistiming() >
                1000 * 60 * 60 * 24) {
            //set system time
            PreferenceManager.getInstance().setMistiming(System.currentTimeMillis());
            new IMGetContactListRequest(user.getShopId()).start(); //reloading contact from server
        }
    }

    private void Login() {
        final long start = System.currentTimeMillis();
        // 调用sdk登陆方法登陆聊天服务器
        if (userName == null || userPassword == null || userName.isEmpty() || userPassword.isEmpty()) {
            return;
        }
        EMChatManager.getInstance().login(userName, userPassword, new EMCallBack() {

            @Override
            public void onSuccess() {
                Log.e("login--", "环信登录成功=========:" + userName);
                HXHelper.getInstance().setCurrentUserName(userName);

                try {
                    // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                    // ** manually load all local groups and
                    //                    EMGroupManager.getInstance().loadAllGroups();
                    EMChatManager.getInstance().loadAllConversations();
                } catch (Exception e) {
                    e.printStackTrace();
                    // 取好友或者群聊失败，不让进入主页面
                    AppController.getInstance().getTopAct().runOnUiThread(new Runnable() {
                        public void run() {
                            HXHelper.getInstance().logout(true, null);
                            ViewUtils.showToast(getResources().getString(R.string.login_failure_failed));
                        }
                    });
                    return;
                }
                // 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
                boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(
                        HXApplication.currentUserNick.trim());
                if (!updatenick) {
                    Log.e("LoginActivity", "update current user nick fail");
                }
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(final int code, final String message) {
                Log.e("login--", "环信登录失败=========:" + userPassword + message + "====" + userName);
                if (loginToHxCount < 3) {
                    AppController.getInstance().initHXAccount(Account.user, false);
                    loginToHxCount++;
                } else {
                    loginToHxCount = 0;
                }
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    }

