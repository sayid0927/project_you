package com.zxly.o2o.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.easemob.chatuidemo.HXApplication;
import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.HXHelper;
import com.easemob.chatuidemo.ui.ChatActivity;
import com.easemob.easeui.AppException;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.domain.EaseYAMUser;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.H5DetailAct;
import com.zxly.o2o.activity.LoginAct;
import com.zxly.o2o.activity.ShopCartAct;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.dialog.SelectChatDialog;
import com.zxly.o2o.model.GetuiMsg;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.Mode;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.zxly.o2o.pullrefresh.PullToRefreshWebView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.GetuiMsgClickRequest;
import com.zxly.o2o.request.LatestActivityRequest;
import com.zxly.o2o.request.PersonalInitRequest;
import com.zxly.o2o.service.RemoteInvokeService;
import com.zxly.o2o.util.NetUtils;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

@SuppressLint("InflateParams")
public class HomeFragment extends BaseFragment implements OnClickListener {

    private  final  static  String TAG=HomeFragment.class.getName();

    private DataRequest request;
    private PullToRefreshWebView mWebview;
    private LoadingView loadingView;
    private String appName;
    String browserUrl, mJsData;
    private Dialog dialog;
    RemoteInvokeService jsObj;
    private boolean isInitWebPage;//暂时没有想到其他判断webView加载完的方法,先用这个
    private SelectChatDialog selectChatDialog;
    private GetuiMsg getuiMsg;


    public static HomeFragment newInstance(String browserUrl) {
        HomeFragment f = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("browserUrl", browserUrl);
        f.setArguments(args);
        return f;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void initView(Bundle bundle) {
        browserUrl = bundle.getString("browserUrl");
      //browserUrl="http://192.168.1.10:28005/app-multi-temp/index.html?shopId=1&baseUrl=aHR0cDovL3Vpcy55b3Vhbm1pLm5ldA==&brandName=Xiaomi&DeviceID=867451021080988&DeviceType=1&userId=27570&appVersion=2.8";

        appName = getActivity().getResources().getString(R.string.app_name);
        TextView txtTitle = (TextView) findViewById(R.id.txt_title);
        ViewUtils.setText(txtTitle, appName);

        View btnShopCart = findViewById(R.id.btn_shopCar);
        btnShopCart.setOnClickListener(this);

        View btnMessage = findViewById(R.id.btn_message);
        btnMessage.setOnClickListener(this);
        dialog = new Dialog(this.getActivity(), R.style.dialog);

        mWebview = (PullToRefreshWebView) findViewById(R.id.pull_refresh_webview);
        mWebview.setVisibility(View.GONE);
        ViewUtils.setRefreshText(mWebview);
        mWebview.setMode(Mode.PULL_FROM_START);


        if(NetUtils.isNetworkAvailable(getActivity()))
            mWebview.getRefreshableView().getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
         else
            mWebview.getRefreshableView().getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        // webView.getSettings().setBlockNetworkImage(true);// 把图片加载放在最后来加载渲染
        mWebview.getRefreshableView().getSettings().setJavaScriptEnabled(true);
        mWebview.getRefreshableView().getSettings().setDisplayZoomControls(false);
        mWebview.getRefreshableView().getSettings().setBuiltInZoomControls(false);
        mWebview.getRefreshableView().getSettings().setSupportZoom(false);
        mWebview.getRefreshableView().getSettings().setUseWideViewPort(true);
        mWebview.getRefreshableView().getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebview.getRefreshableView().getSettings().setLoadWithOverviewMode(true);
        mWebview.getRefreshableView().getSettings().setSaveFormData(false); // 支持保存数据
        //mWebview.getRefreshableView().clearCache(true); // 清除缓存
        //mWebview.getRefreshableView().clearHistory(); // 清除历史记录
        mWebview.getRefreshableView().getSettings().setAppCacheEnabled(true);//是否使用缓存
        mWebview.getRefreshableView().getSettings().setDomStorageEnabled(true);//DOM Storage

        String  cacheDomPath =  getActivity().getApplicationContext().getDir("HomecacheDomPath", Context.MODE_PRIVATE).getPath();
        String cacheAppPath = getActivity().getApplicationContext().getDir("HomecacheAppPath", Context.MODE_PRIVATE).getPath();

        mWebview.getRefreshableView().getSettings().setAppCachePath(cacheAppPath);
        mWebview.getRefreshableView().getSettings().setDatabasePath(cacheDomPath);
        mWebview.getRefreshableView().getSettings().setAppCacheMaxSize(8*1024*1024);
        mWebview.getRefreshableView().setHorizontalScrollBarEnabled(false);//水平不显示
        mWebview.getRefreshableView().setVerticalScrollBarEnabled(false); //垂直不显示


        jsObj = new RemoteInvokeService(getActivity(), mWebview.getRefreshableView(), new ParameCallBack() {
            @Override
            public void onCall(final Object object) {
                loadingView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (object instanceof Integer) {
                            int result = (Integer) object;
                            switch (result) {
                                case RemoteInvokeService.LOADING_FINISH:

                                    mWebview.setVisibility(View.VISIBLE);
                                    loadingView.onLoadingComplete();
                                    mWebview.onRefreshComplete();

                                    break;
                                case RemoteInvokeService.LOADING_FAIL:

//                                    mWebview.setVisibility(View.GONE);
//                                    loadingView.onLoadingFail();
                                    //    mWebview.onRefreshComplete();

                                    break;

                                case RemoteInvokeService.LOGIN_SUCCESS:
                                    mWebview.getRefreshableView().loadUrl("javascript:setLoginUserId('" + Account.user.getId() + "')");
                                    break;
                            }
                        }
                    }
                });
            }
        });

        mWebview.getRefreshableView().addJavascriptInterface(jsObj, "js_invoke");
        mWebview.getRefreshableView().setWebViewClient(new WebViewClient() {

            int errorCode;

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            /** 开始载入页面 */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //	setProgressBarIndeterminateVisibility(true);// 设置标题栏的滚动条开始
                this.errorCode = 0;
                super.onPageStarted(view, url, favicon);
            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
              // 	view.loadUrl(url);
                return true;
            }

            /** 错误返回 */
            @Override
            public void onReceivedError(WebView view, int errorCode,String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);

                this.errorCode = errorCode;
                loadingView.onLoadingFail();

            }

            /** 页面载入完毕 */
            @Override
            public void onPageFinished(WebView view, String url) {
                //	setProgressBarIndeterminateVisibility(false);// 设置标题栏的滚动条停止
                super.onPageFinished(view, url);
                //页面加载完询问是否有最新活动
                //由于请求最新活动需要clientId参数，而获取clientId的请求是有很大几率在这个请求后发生故延迟半秒
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getLatestActivity();
                    }
                }, 600);
            }
        });


        mWebview.getRefreshableView().setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });


        mWebview.setOnRefreshListener(new OnRefreshListener<WebView>() {
            @Override
            public void onRefresh(PullToRefreshBase<WebView> refreshView) {
                //		mWebview.getRefreshableView().loadUrl("javascript:wave1()");
                mWebview.getRefreshableView().loadUrl("javascript:refreshView()");

                // mWebview.getRefreshableView().loadUrl("javascript:refreshComment('5')");
                // mWebview.getRefreshableView().loadUrl("javascript:setLoginUserId('666666')");
                //JS常常加载数据太慢，没办法
                //mWebview.getRefreshableView().reload();

                mWebview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mWebview.onRefreshComplete();
                    }
                }, 1000);
            }
        });


        loadingView = (LoadingView) findViewById(R.id.view_loading);

        loadingView.startLoading();

        loadingView.setOnAgainListener(new LoadingView.OnAgainListener() {
            @Override
            public void onLoading() {
                loadingView.startLoading();
              mWebview.getRefreshableView().reload();
            }
        });

     mWebview.getRefreshableView().loadUrl(browserUrl);
    }

    @Override
    protected void initView() {
    }


    @Override
    protected int layoutId() {
        return R.layout.wim_home;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_shopCar:
                ShopCartAct.start(this.getActivity());
                break;

            case R.id.btn_message:

                if (selectChatDialog == null) {
                    selectChatDialog = new SelectChatDialog();
                }

                if (Account.user != null) {
                    HXHelper.getInstance().getYAMContactList();

                    if (Account.user.getBelongId() != 0) {
                        goToChat();

                    } else {
                        PersonalInitRequest personalInitRequest = new PersonalInitRequest(Account.user
                                .getId(), Config.shopId);
                        personalInitRequest.setOnResponseStateListener(
                                new BaseRequest.ResponseStateListener() {
                                    @Override
                                    public void onOK() {
                                        if (Account.user.getBelongId() != 0) {
                                            goToChat();

                                        } else {
                                            selectChatDialog.show();
                                        }
                                    }

                                    @Override
                                    public void onFail(int code) {

                                    }
                                });
                        personalInitRequest.start();

                    }
                } else {
                    LoginAct.start(getActivity());
                }

                break;

            case R.id.btn_shopInfo:

                break;

            default:
                break;
        }
    }

    private void goToChat() {

        String chatUserId =
                HXApplication.getInstance().parseUserFromID(Account.user.getBelongId(),
                        HXConstant.TAG_SHOP);
        EaseYAMUser user = HXHelper.getInstance().getUserInfo(chatUserId);

        if (user != null) {

            String name = user.getFirendsUserInfo().getNickname();
            if (TextUtils.isEmpty(name)) {
                name = user.getFirendsUserInfo().getUserName();
            }
            chatUserId = new StringBuffer("").append(name).append("#")
                    .append(Account.user.getBelongId()).append("#").append(chatUserId)
                    .toString();
            EaseConstant.startActivityNormalWithStringForResult(ChatActivity.class, getActivity(),
                    chatUserId,
                    EaseConstant.EXTRA_USER_ID);
        }
    }


    static class DataRequest extends BaseRequest {

        String jsData;

        public DataRequest() {
            addParams("shopId", Config.shopId);
            addParams("brandName", Build.BRAND);
        }

        public String getJsData() {
            return jsData;
        }

        @Override
        protected void fire(String data) throws AppException {
            jsData = data;
        }

        @Override
        protected String method() {
            return "shop/shopIndex";
        }
    }

    private void getLatestActivity() {
        final LatestActivityRequest request = new LatestActivityRequest();
        request.start();
        request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {


            @Override
            public void onOK() {
                getuiMsg = request.getGetuiMsg();
                if (!TextUtils.isEmpty(getuiMsg.getHeadUrl())) {
                    showLatextActivityDialog();
                }
            }

            @Override
            public void onFail(int code) {

            }
        });
    }

    /**
     * 有最新活动则显示活动详情弹出框
     */
    private void showLatextActivityDialog() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog.show();
        dialog.setContentView(R.layout.dialog_presentation_activity);
        NetworkImageView iv = (NetworkImageView) dialog.findViewById(R.id.activity_image);
        iv.setImageUrl(getuiMsg.getHeadUrl(), AppController.imageLoader);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        ((TextView) dialog.findViewById(R.id.activity_title)).setText(getuiMsg.getTitle());
        ((TextView) dialog.findViewById(R.id.activity_price)).setText(Html.fromHtml("活动价格  " + "<font color='#ff0000'>" + getuiMsg.getContent() + "</font>"));
        dialog.findViewById(R.id.content)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        gotoActivityDetail();
                    }
                });
        dialog.findViewById(R.id.close)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                });
    }

    /**
     * 点击活动浮层调转至活动详情页面
     */
    private void gotoActivityDetail() {
        markGetuiMsgHasRead();
        H5DetailAct.start(this.getActivity(), getuiMsg.getH5Url(), "商品详情");
    }

    /**
     * 标记此活动已阅
     */
    private void markGetuiMsgHasRead() {
        GetuiMsgClickRequest gmcr = new GetuiMsgClickRequest(getuiMsg.getDataId());
        gmcr.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
            }

            @Override
            public void onFail(int code) {
            }
        });
        gmcr.start();
    }
}
