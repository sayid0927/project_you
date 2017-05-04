package com.zxly.o2o.service;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.provider.ContactsContract;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.RadioButton;

import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.shyz.downloadutil.DownLoadListAct;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.ArticlePlatformVipAct;
import com.zxly.o2o.activity.BigturntableAct;
import com.zxly.o2o.activity.CommonQuestionAct;
import com.zxly.o2o.activity.DiscountProductAct;
import com.zxly.o2o.activity.DiscoverInfoAct;
import com.zxly.o2o.activity.H5DetailAct;
import com.zxly.o2o.activity.HomeAct;
import com.zxly.o2o.activity.InsuranceListAct;
import com.zxly.o2o.activity.LoginAct;
import com.zxly.o2o.activity.MakecommissionAct;
import com.zxly.o2o.activity.MobileDataAct;
import com.zxly.o2o.activity.MyCircleSecondAct;
import com.zxly.o2o.activity.MyCircleThirdAct;
import com.zxly.o2o.activity.PayAct;
import com.zxly.o2o.activity.ProductInfoAct;
import com.zxly.o2o.activity.ShopArticleAct;
import com.zxly.o2o.activity.ShopHotArticleAct;
import com.zxly.o2o.activity.ToStorePrivilegeAct;
import com.zxly.o2o.activity.WebViewAct;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.dialog.LoadingDialog;
import com.zxly.o2o.dialog.ShareDialog;
import com.zxly.o2o.fragment.DiscoveryFragment;
import com.zxly.o2o.model.Banners;
import com.zxly.o2o.model.ShareInfo;
import com.zxly.o2o.model.ShopArticle;
import com.zxly.o2o.model.ShopTopic;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BigturntableCountRequest;
import com.zxly.o2o.util.ApkInfoUtil;
import com.zxly.o2o.util.BitmapUtil;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.CallBackWithParam;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.ShareListener;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.Util;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import static com.zxly.o2o.config.Config.shopId;

public class RemoteInvokeService {
    private Activity context;
    private WebView webView;
    private LoadingDialog dialog;
    private CallBack callBack;
    private ParameCallBack mCallback;
    private Object data;
    private LoadingView loadingView;

    public static final int LOADING_FINISH=1;
    public static final int LOADING_FAIL=-1;
    public static final int LOGIN_SUCCESS=2;
    public RemoteInvokeService(){

    }

    public RemoteInvokeService(Activity paramActivity, WebView webView, ParameCallBack callBack) {
        this.context = paramActivity;
        this.webView = webView;
        this.mCallback = callBack;
    }


    public RemoteInvokeService(Activity paramActivity, WebView webView, CallBack callBack) {
        this.context = paramActivity;
        this.webView = webView;
        this.callBack = callBack;
    }

    public RemoteInvokeService(Activity paramActivity, Object data, CallBack callBack) {
        this.context = paramActivity;
        this.callBack = callBack;
        this.data=data;
    }

    @JavascriptInterface
    public void toLogin() {
        AppController.getInstance().deleteFile("user");
        Account.user = null;
        PreferUtil.getInstance().setLoginToken("");
        LoginAct.start(context, new CallBack() {
            @Override
            public void onCall() {
                if(callBack!=null){
                    callBack.onCall();
                }else if(mCallback!=null){
                    mCallback.onCall(LOGIN_SUCCESS);
                }
            }
        });

    }

    @JavascriptInterface
    public String getTel() {
        if(Account.user!=null)
        {
            return Account.user.getMobilePhone();
        }else
        {
            toLogin();
        }
        return "null";
    }

    @JavascriptInterface
    public void openContacts() {
        this.context.startActivityForResult(new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI), 0);
    }

    @JavascriptInterface
    public void doRecharge(String orderNo) {
        ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String topActName = cn.getClassName();
        if (Account.hasLogin() && !topActName.equals("com.zxly.o2o.activity.PayAct")) {
            PayAct.start(this.context, orderNo, Constants.TYPE_FLOW_PAY);
        }
    }

    @JavascriptInterface
    public void showLoading() {
        if (dialog == null) {
            dialog = new LoadingDialog();
        }
        dialog.show();
    }

    private ShareDialog shareDialog;
    @JavascriptInterface
    public void toShare(final String title, final String desc, final String targetUrl, final String iconUrl) {
        if(webView!=null){
            webView.post(new Runnable() {
                @Override
                public void run() {
                    if(shareDialog==null)
                        shareDialog=new ShareDialog();

                    shareDialog.show(title, desc, targetUrl, iconUrl, new ShareListener() {
                        @Override
                        public void onComplete(Object var1) {
                            if (webView!=null)
                                webView.loadUrl("javascript:submitTransmit()");
                        }

                        @Override
                        public void onFail(int errorCode) {

                        }
                    });
                }
            });
        }

    }


    @JavascriptInterface
    public void cancelLoading() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }


    public void setLoadingView(LoadingView loadingView){
        this.loadingView=loadingView;
    }

    @JavascriptInterface
    public void showLoadingView() {
        if (loadingView != null) {
            loadingView.startLoading();
        }
    }

    @JavascriptInterface
    public void stopLoadingView() {
        if (mCallback!=null)
            mCallback.onCall(LOADING_FINISH);
    }

    @JavascriptInterface
    public void doShare() {
        Banners banner= (Banners) data;
        ViewUtils.share(context, Config.dataBaseUrl + banner.getUrl() + "&DeviceID=" + Config.imei + "&Authorization=null&type=" + Constants.OPEN_FROM_SHARE + "&shopId=" + Config.shopId + "&baseUrl=" + Config.dataBaseUrl);
        new BigturntableCountRequest(Account.user.getId(), (long) banner.getId()).start();
    }

    @JavascriptInterface
    public String getUserId() {

        if(Account.user!=null)
            return Account.user.getId()+"";

        return null;
    }

    @JavascriptInterface
    public String updateNetworkData() {
        String data = null;
        try {
            JSONObject jsonObject = new JSONObject();
            if (Account.user != null) {
                jsonObject.put("userId", Account.user.getId());
            } else {
                jsonObject.put("userId", "");
            }
            String token = PreferUtil.getInstance().getLoginToken();
            if (!StringUtil.isNull(token)) {
                jsonObject.put("Authorization", token);
            } else {
                jsonObject.put("Authorization", "");
            }
            data = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    @JavascriptInterface
    public String getToken() {
        return PreferUtil.getInstance().getLoginToken();
    }


    @JavascriptInterface
    public void toCommonQuestions() {
        CommonQuestionAct.start(context);
    }

    @JavascriptInterface
    public void showToast(String msg){
        ViewUtils.showToast(msg);
    }


    @JavascriptInterface
    public void openView(String jsData){
        // indexArticle type: 1.帖子 2.文章 3.软文
        try {
            JSONObject jo = new JSONObject(jsData);
            JSONObject result=null;
            if(!DataUtil.stringIsNull(jo.optString("data"))&&!"null".equals(jo.optString("data")))
              result = new JSONObject(jo.getString("data"));
            int pageId = jo.getInt("pageId");
            if(jo.has("newpageId")){
                pageId=jo.optInt("newpageId");
            }
            switch (pageId){

                case Constants.PAGE_BANNER:
                     final Banners banner= GsonParser.getInstance().getBean(jo.getString("data"),Banners.class);
                    if(banner.getType()==Banners.BANNER_PRODUCT){
                        ProductInfoAct.start(context, banner.getProductId(), banner.getId(), null);
                    }else if(banner.getType()==Banners.BANNER_H5){
                        String activityUrl="";
                        if(Account.user != null){
                            activityUrl = banner.getUrl().replace("{userId}", Account.user.getId() + "");
                        } else {
                            activityUrl = banner.getUrl().replace("{userId}", "0");
                        }
                        ShareInfo shareInfo=new ShareInfo();
                        shareInfo.setTitle(banner.getTitle());
                        shareInfo.setDesc("");
                        shareInfo.setUrl(banner.getUrl().replace("{userId}", "0"));
                        shareInfo.setShareIcon(banner.getImageUrl());
                        WebViewAct.start(WebViewAct.TYPE_DEFAULT,"活动详情",activityUrl,shareInfo);
                    //    H5DetailAct.start(AppController.getInstance().getTopAct(),banner.getUrl(),"活动详情");
                    }else if(banner.getType()==Banners.BANNER_DEFAULT){
                        H5DetailAct.start(H5DetailAct.TYPE_DEFAULT,context,"详情",banner);
                    }else if(banner.getType()==Banners.BANNER_TURN_TABLE){
                        BigturntableAct.start(context, banner);
                    }else if(banner.getType()==Banners.TYPE_GAME) {
                        //appType=2 代表用户app
                        if(Account.user==null){
                            LoginAct.start(AppController.getInstance().getTopAct(), new CallBack() {
                                @Override
                                public void onCall() {
                                    banner.setShareUrl(banner.getShareUrl()+"&appType=2"+"&userId="+Account.user.getId());
                                    H5DetailAct.start(H5DetailAct.TYPE_H5_GAME,context,"剁手党活动",banner);
                                }
                            });
                        }else {
                            banner.setShareUrl(banner.getShareUrl()+"&appType=2"+"&userId="+Account.user.getId());
                            H5DetailAct.start(H5DetailAct.TYPE_H5_GAME,context,"剁手党活动",banner);
                        }
                    }else if(banner.getType()==Banners.TYPE_EXTERNAL_LINK){
                        H5DetailAct.start(H5DetailAct.TYPE_DEFAULT,context,banner.getTitle(),banner);
                    }else if(banner.getType()==Banners.TYPE_DDYH){
                        ToStorePrivilegeAct.start(context);
                    }else if(banner.getType()==Banners.TYPE_ARTICLE_AD){
                        ShopArticle shopArticle = new ShopArticle();
                        shopArticle.setId(banner.getProductId());
                        shopArticle.setTitle(banner.getTitle());
                        ShopHotArticleAct.start(context, shopArticle, 1, "详情", false, null);
                    }
                    break;

                case Constants.PAGE_PRODUCT_ARTICLE:
                    DiscoverInfoAct.start(context, Integer.parseInt(result.getString("id")));
                    break;


                case Constants.PAGE_MAKE_COMMISSION:
                    ViewUtils.startActivity(new Intent(context, MakecommissionAct.class), context);
                    break;


                case Constants.PAGE_APP_DOWNLOAD:
                    ViewUtils.startActivity(new Intent(context, DownLoadListAct.class), context);
                    break;

                case Constants.PAGE_FLOW_RECHARGE:
                    ViewUtils.startActivity(new Intent(context, MobileDataAct.class), context);
                    break;
                case Constants.PAGE_DDYH:
                    ToStorePrivilegeAct.start(context);
                    break;

                case Constants.PAGE_DISCOUNT_PRODUCT:
                    ViewUtils.startActivity(new Intent(context, DiscountProductAct.class), context);
                    break;


                case Constants.PAGE_ARTICLE_H5:
                    ShopArticle article=new ShopArticle();
                    article.setId(result.optLong("id"));
                    article.setTitle(result.optString("title"));
                    article.setUrl(result.optString("url"));
                    article.setCreateTime(result.optLong("createTime"));  //文章创建时间
                    article.setPraiseAmount(result.optInt("praiseAmount"));

                    if("platform_article".equals(result.optString("tableName"))){

                        ShareInfo shareInfo=new ShareInfo();
                        shareInfo.setTitle(result.optString("title"));
                        shareInfo.setUrl(article.getUrl());
                        shareInfo.setShareIcon(BitmapUtil.drawableToBitmap(context.getResources().getDrawable(R.drawable.icon_youanmi)));
                        WebViewAct.start(WebViewAct.TYPE_DEFAULT,"详情",article.getUrl(),shareInfo);
                        //   H5DetailAct.start(context,article.getUrl(),"详情",article.getTitle()); //平台文章
                        return;
                    }

                    //   MyCircleRequest.shopArticle = article;
                    //   MyCircleSecondAct.MyCircleLunch(context, Constants.ARTICLE_DETAIL,"详情");  //普通文章
                    MyCircleThirdAct.start(context, article, "详情", new CallBackWithParam() {
                        @Override
                        public void onCall(int param) {
                            if (param == 0) {
                                //      ViewUtils.showToast("文章点赞");
                            } else {
                                if (webView != null)
                                    webView.loadUrl("javascript:refreshComment('" + param + "')");

                                //         ViewUtils.showToast("文章评论"+param);
                            }
                        }
                    });

                    break;

                case Constants.PAGE_ARTICLE_DETAIL:
                    ShopTopic sp=new ShopTopic();
                    sp.setId(result.getLong("id"));
                    sp.setContent(result.optString("content"));
                    sp.setPraiseAmout(result.optInt("praiseAmount"));
                    sp.setCreateTime(result.optLong("createTime"));  //帖子创建时间

                    // publishMan : 1.匿名 2.柚安米 0.普通用户
                    if(result.optInt("publishMan")==0){
                        sp.getPublishUser().setId(result.optLong("publishUserId")); //设置发布人的id
                    }else if (result.optInt("publishMan")==1){
                        sp.getPublishUser().setId(-1);                              //发布人为匿名
                    }else if (result.optInt("publishMan")==2){
                        sp.getPublishUser().setId(0);                                //发布人为平台
                    }

                    sp.getPublishUser().setNickname(result.optString("publishName"));   //发帖用户昵称
                    sp.getPublishUser().setThumHeadUrl(result.optString("publishHeadUrl"));   //发帖用户头像

                    TypeToken<List<String>> stringTyppe = new TypeToken<List<String>>() {};
                    sp.setThumImageList(GsonParser.getInstance().fromJson(result.optString("thumImageUrlAry"), stringTyppe));  //帖子图片列表
                    sp.setOriginImageList(GsonParser.getInstance().fromJson(result.optString("originImageUrlAry"), stringTyppe));  //帖子图片列表

                    byte circleType=0; //普通圈子
                    if("platform_topic".equals(result.get("tableName"))){
                        circleType=1; //平台圈子

                    }

                    MyCircleThirdAct.start(context,circleType,sp,result.optString("articleFrom"),false,new CallBackWithParam(){
                        @Override
                        public void onCall(final int param) {

                            if(param==0){
                              //  ViewUtils.showToast("帖子点赞");
                            }else {
                                if(webView!=null){
                                    webView.loadUrl("javascript:refreshComment('" + param + "')");
                                }
                            }
                        }

                    });

                    break;

                case Constants.PAGE_CIRCLE_DETAIL:
                    // 后台设计此处只拉取平台圈子的内容
                    Intent intent = new Intent();
                    intent.putExtra("isShop",(byte)1);  //否
                    intent.putExtra("mycircle_second_title", result.optString("name"));
                    intent.putExtra("circleId", Long.valueOf(result.getString("id")));
                    if(result.has("brandId")){
                        intent.putExtra("circleType", "brandCircle");
                    }
                    intent.setClass(context, MyCircleSecondAct.class);
                    context.startActivity(intent);
                    break;

                case Constants.PAGE_DISCOVERY:
                    if(context instanceof HomeAct&&webView!=null){
                        webView.post(new Runnable() {
                            @Override
                            public void run() {
                                DiscoveryFragment.turnToProductList=true;
                                ((RadioButton)context.findViewById(R.id.btn_discoveryPage)).setChecked(true);
                            }
                        });
                    }

                    break;

                case Constants.PAGE_CIRCLE:
                    if(context instanceof HomeAct&&webView!=null){
                        webView.post(new Runnable() {
                            @Override
                            public void run() {
//                                ((RadioButton)context.findViewById(R.id.btn_circlePage)).setChecked(true);
                            }
                        });
                    }

                    break;

                //用户app微调新增
                case Constants.PAGE_INSURANCE:
                    if(Account.user!=null) {
                        InsuranceListAct.start(context);
                    } else {
                        LoginAct.start(context);
                    }
                    break;
                case Constants.PAGE_DPRW://店铺热文
                    ShopArticleAct.start(context, (byte) 1);
                    break;
                case Constants.PAGE_BDJL://本店交流
                    Intent i = new Intent();
                    i.putExtra("isShop", (byte) 1);  //否
                    i.putExtra("mycircle_second_title", result.optString("name"));
                    i.putExtra("circleId", Long.valueOf(result.getString("id")));
                    if (result.has("brandId")) {
                        i.putExtra("circleType", "brandCircle");
                    }
                    i.setClass(context, MyCircleSecondAct.class);
                    context.startActivity(i);
                    break;
                case Constants.PAGE_MDTJ://
                    ShopArticleAct.start(context, (byte) 1);
                    break;
                case Constants.PAGE_PTZX://平台专享
                    ShopArticleAct.start(context, (byte) 2);
                    break;
                case Constants.PAGE_RWZX:
                    //articleType  1:店铺热文  2 平台专享
                    ShopArticle articleInfo=new ShopArticle();
                    articleInfo.setId(result.optLong("id"));
                    articleInfo.setTitle(result.optString("title"));
                    articleInfo.setUrl(result.optString("url"));
                    articleInfo.setIsPraise(result.optInt("isPraise"));
                    articleInfo.setIsPraise(result.optInt("isCollect"));
                    articleInfo.setContent(result.optString("content"));
                    articleInfo.setCreateTime(result.optLong("createTime"));  //文章创建时间
                    articleInfo.setPraiseAmount(result.optInt("praiseAmount"));
                    articleInfo.setArticleFrom(result.optString("articleFrom"));//文章类别


                    if(jo.has("articleType")){
                        int articleType = jo.optInt("articleType");
                        if(articleType==1){
                            ShopHotArticleAct.start(context,articleInfo,articleType,"详情", false,new CallBackWithParam() {
                                @Override
                                public void onCall(int param) {
                                    if (param == 0) {
                                    } else {
                                        if (webView != null)
                                            webView.loadUrl("javascript:refreshComment('" + param + "')");
                                    }
                                }
                            });
                        }else if(articleType==2){
                            ShareInfo shareInfo=new ShareInfo();
                            shareInfo.setTitle(result.optString("title"));
                            shareInfo.setUrl(articleInfo.getUrl());
                            ArticlePlatformVipAct.start("详情",shareInfo,articleType,articleInfo,false,null);
                        }
                    }
                    break;

                case Constants.PAGE_TOTAL:

                    String homeUrl="";

                    if ((PreferUtil.getInstance().getH5VersionCode() > ApkInfoUtil.getDefaultH5Version(context) ||
                            PreferUtil.getInstance().getH5StyleId() != ApkInfoUtil.getDefaultH5StyleId(context)) &&
                            new File(Constants.H5_PROJECT_PATH).exists()) {

                        homeUrl = "file://" + Constants.H5_PROJECT_PATH + "/moreFunction.html?shopId=" + shopId +
                                "&baseUrl=" + DataUtil.encodeBase64(Config.dataBaseUrl)
                                +"&appVersion="+ Util.getVersion(context)
                                +"&brandName=" + Build.BRAND ;

                    } else {
                        homeUrl = "file://" + Config.getH5CachePath(context) + "/moreFunction.html?shopId=" + shopId +
                                "&baseUrl=" + DataUtil.encodeBase64(Config.dataBaseUrl)
                                +"&appVersion="+ Util.getVersion(context)
                                +"&brandName=" + Build.BRAND ;
                    }

//                    String  Url = "file://" + Constants.H5_PROJECT_PATH + "/moreFunction.html?shopId=" + shopId +
//                            "&baseUrl=" + DataUtil.encodeBase64(Config.dataBaseUrl)
//                            +"&appVersion="+ Util.getVersion(context)
//                            + "&brandName=" + Build.BRAND ;

                    //String browserUrl1="http://192.168.1.66/app-multi-temp/moreFunction.html?shopId=100&baseUrl=aHR0cDovLzE5Mi4xNjguMS4xMDoyODAwNS8==&brandName=Xiaomi&DeviceID=867451021080988&DeviceType=1&userId=27570&appVersion=20500";

                    WebViewAct.start("全部",homeUrl);

                    break;

                case Constants.PAGE_MDZX:

                    String browserUrl=result.optString("bannerUrl");
                    String name=result.optString("name");
                   WebViewAct.start(name,browserUrl);

                    break;

                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @JavascriptInterface
    public String getPageData(){
        return (String) data;
    }


    public void setPageData(String data){
        this.data=data;
    }

    @JavascriptInterface
    public void loadingFail(){
        if(mCallback!=null)
            mCallback.onCall(LOADING_FAIL);
    }


    /**
     * 店铺热文详情页面关联商品点击事件
     * @param id
     */
    @JavascriptInterface
    public void clickProductFromArticle(String id){
        try {
            JSONObject jsonObject=new JSONObject(id);
            ProductInfoAct.start(AppController.getInstance().getTopAct(),jsonObject.getLong("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}