package com.zxly.o2o.controller;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
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
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chatuidemo.HXApplication;
import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.HXHelper;
import com.easemob.chatuidemo.utils.PreferenceManager;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.controller.EaseUI;
import com.igexin.sdk.PushManager;
import com.shyz.downloadutil.BaseApplication;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.cache.BitmapLruCache;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.User;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.IMGetShopContactsRequest;
import com.zxly.o2o.request.MultiPartStack;
import com.zxly.o2o.service.BootBroadcastReceiver;
import com.zxly.o2o.service.RemovedBroadcastReceiver;
import com.zxly.o2o.thread.CrashHandler;
import com.zxly.o2o.util.AppLog;
import com.zxly.o2o.util.ImeiUtil;
import com.zxly.o2o.util.MD5Util;
import com.zxly.o2o.util.PackageInfoUtil;
import com.zxly.o2o.util.PreferUtil;

import java.util.Stack;

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
    private int loginToHXCount;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        PushManager.getInstance().initialize(this.getApplicationContext());

        Intent completed = new Intent(Intent.ACTION_BOOT_COMPLETED);
        BootBroadcastReceiver bootBroadcastReceiver= new BootBroadcastReceiver();
        bootBroadcastReceiver.onReceive(this,completed);

        Intent remove = new Intent(Intent.ACTION_PACKAGE_REMOVED);
        RemovedBroadcastReceiver removedBroadcastReceiver= new RemovedBroadcastReceiver();
        removedBroadcastReceiver.onReceive(this,remove);

        imei = ImeiUtil.getImei(instance);
        mImageUploadQueue = Volley.newRequestQueue(this, new MultiPartStack(this));
        lruImageCache = BitmapLruCache.instance();
        PreferUtil.getInstance().init(this);
        BaseApplication.getInstance(this).init();


        HXApplication.getInstance().onCreate(this, R.raw.uishttps);  //环信初始化

        setPkgVersion();//不要把这段代码注释了会影响升级

        displayMetrics = EaseUI.displayMetrics;
        mRequestQueue = EaseUI.requestQueue;
        imageLoader = new ImageLoader(mRequestQueue, lruImageCache);
        EaseUI.imageLoader = imageLoader;
        Config.init(this);

        //其他门店覆盖安装当前门店时，清空登录用户数据
        long curShopId = PreferUtil.getInstance().getShopId();
        if (Config.shopId != curShopId) {
            if (curShopId != 0 && !"".equals(PreferUtil.getInstance().getLoginToken())) {
                clearUserData();
            }
            PreferUtil.getInstance().setShopId(Config.shopId);
        }

        //判断当前的包是release还是debug包，release包供外部测试，崩溃信息打印到内存卡
        if (!PackageInfoUtil.isDebuggable(this)) {
            CrashHandler.getInstance().init(this);
        }

        initLogger();
    }
    
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    
    private void clearUserData(){
        HXConstant.isLoginSuccess = false;
        HXHelper.getInstance().logout(true, null);//退出环信
        HXHelper.getInstance().deleteDBContactCache();  //清空本地联系人
        HXHelper.getInstance().cleanYAMContactList();  //清空本地缓存
        PreferUtil.getInstance().setHXAccount(-1); //设置为游客未注册状态

        Account.orderCount = 0;
        AppController.getInstance().getApplicationContext().deleteFile("user");
        Account.user = null;
        PreferUtil.getInstance().setLoginToken("");
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
            AppLog.p(e);
        }
    }

    public void savePromotionCode(String promotionCode) {
        try {
            Config.promotionCode = promotionCode;
            ApplicationInfo appInfo = this.getPackageManager()
                    .getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            appInfo.metaData.putString("promotionCode", promotionCode);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
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

    public void isLoginHX(final String userName, final String userPassword) {
        if (!HXHelper.getInstance().isLoggedIn()) {
            Login(userName, userPassword);
        } else {
            HXHelper.getInstance().logout(true, new EMCallBack() {
                @Override
                public void onSuccess() {
                    Login(userName, userPassword);
                }

                @Override
                public void onError(int i, String s) {
                }

                @Override
                public void onProgress(int i, String s) {
                }
            });
        }
    }

    private void Login(final String userName, final String userPassword) {
        if (userName == null || userPassword == null || userName.isEmpty() || userPassword.isEmpty()) {
            return;
        }
        final long start = System.currentTimeMillis();
        // 调用sdk登陆方法登陆聊天服务器
        EMChatManager.getInstance().login(userName, userPassword, new EMCallBack() {

            @Override
            public void onSuccess() {
                loginToHXCount = 0;
                if (Account.user != null) {
                    HXConstant.isLoginSuccess = true; //标识登录hx成功
                } else {
                    HXConstant.isLoginSuccess = false; //标识游客登录hx成功
                }
                // 登陆成功，保存用户名密码
                HXHelper.getInstance().setCurrentUserName(userName);
                android.util.Log.e("login--", "环信登录成功=========:" + userName);
                try {
                    // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                    EMChatManager.getInstance().loadAllConversations();
                } catch (Exception e) {
                    e.printStackTrace();
                    // 取好友或者群聊失败，不让进入主页面
                    HXHelper.getInstance().logout(true, null);
                }
                // 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
                boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(
                        HXApplication.currentUserNick.trim());
                if (!updatenick) {
                    android.util.Log.e("LoginActivity", "update current user nick fail");
                }
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(final int code, final String message) {
                if (Account.user != null && loginToHXCount < 3) {  //登录失败重新登陆
                    AppController.getInstance().doLoginHX(Account.user, true);
                    loginToHXCount++;
                } else if (loginToHXCount >= 3) {
                    loginToHXCount = 0;
                }else if(Account.user != null &&loginToHXCount==0){
                    if(message.contains("No respnse from server")){
                        AppController.getInstance().doLoginHX(Account.user, true);
                        loginToHXCount++;
                    }
                }
                android.util.Log.e("login--", "环信登录失败=========:" + message + "====" + userName);
            }
        });
    }


    public void loginHxVisitorAccount() {
        AppController.getInstance().checkIsNeedUpdateContact(true);
        String userName = new StringBuffer(String.valueOf(Config.shopId)).append("_")
                .append(AppController.imei).toString();
        String userPassword = MD5Util
                .GetMD5Code(AppController.imei)
                .substring(0, 10);
        isLoginHX(userName, userPassword);
    }

    public void loginHxRegularAccount(User user) {
        if(user!=null) {
            AppController.getInstance().checkIsNeedUpdateContact(false);
            EaseConstant.currentUser.getFirendsUserInfo().setId(user.getId());
            EaseConstant.currentUser.getFirendsUserInfo().setEid(HXApplication.getInstance().parseUserFromID(
                    user.getId(),HXConstant.TAG_USER));
            EaseConstant.currentUser.getFirendsUserInfo().setNickname(user.getNickName());
            EaseConstant.currentUser.getFirendsUserInfo().setThumHeadUrl(user.getThumHeadUrl());
            String userName = EaseConstant.currentUser.getFirendsUserInfo().getEid();
            String userPassword = MD5Util
                    .GetMD5Code(new StringBuffer(
                            String.valueOf(user.getId())).append("_")
                            .append(user
                                    .getUserName()).toString())
                    .substring(0, 10);
            Log.e("loging to hx user:", userName + "=========");
            isLoginHX(userName, userPassword);
        }
    }

    public void doLoginHX(User user, boolean isRelogin) {
        /*环信正式帐号先退出游客帐号再登录*/
        if (isRelogin) {
            HXHelper.getInstance().logout(true, null);
        }

        AppController.getInstance().loginHxRegularAccount(user);
        PreferUtil.getInstance().setHXAccount(2);

    }

    private void reLoadContact(boolean isOnlyLoadShopUsers) {
        if (!IMGetShopContactsRequest.isLoadingContact) {
            IMGetShopContactsRequest imGetShopContactsRequest = new IMGetShopContactsRequest(isOnlyLoadShopUsers);
            imGetShopContactsRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                @Override
                public void onOK() {
                    IMGetShopContactsRequest.isLoadingContact = false;
                }

                @Override
                public void onFail(int code) {
                    IMGetShopContactsRequest.isLoadingContact = false;
                }
            });
            imGetShopContactsRequest.start();
        }
    }

    public  void exitAndGoToMainPage() {
        for(int i=0;i<actList.size()-1;i++){
            actList.pop().finish();
        }
    }

    public void checkIsNeedUpdateContact(boolean isOnlyLoadShopUsers) {
        // do this if mistiming last over one day
        if (true||System.currentTimeMillis() - PreferenceManager.getInstance().getMistiming() >
                1000 * 60 * 60 * 24) {
            //set system time
            PreferenceManager.getInstance().setMistiming(System.currentTimeMillis());
            reLoadContact(isOnlyLoadShopUsers); //reloading contact from server
        }
    }

    private void initLogger() {
     //   Logger.init("YouAnMiUsderApp").methodCount(2).methodOffset(0).logLevel(LogLevel.FULL).hideThreadInfo();
    }

}
