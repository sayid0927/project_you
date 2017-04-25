package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.dialog.ShareDialog;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.service.RemoteInvokeService;
import com.zxly.o2o.util.BitmapUtil;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

/**
 * Created by dsnx on 2016/6/6.
 */
public class ToStorePrivilegeAct extends BasicAct {

    private TextView txtTitle;
    private ImageView btnRight,btnBack;
    private WebView webView;
    private LoadingView loadingview = null;
    private float price;
    private ShareDialog shareDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_h5_detail);
        txtTitle= (TextView) findViewById(R.id.tag_title_title_name);
        btnRight= (ImageView) findViewById(R.id.btn_right);
        btnBack= (ImageView) findViewById(R.id.tag_title_btn_back);
        loadingview = (LoadingView) findViewById(R.id.view_loading);
        loadingview.startLoading();
        webView = ((WebView) findViewById(R.id.web_view));
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shareDialog == null) {
                    shareDialog = new ShareDialog();
                }
                Object shareImage = BitmapUtil.drawableToBitmap(getResources().getDrawable(R.drawable.img_ddyh));
                StringBuilder desc=new StringBuilder();
                String appName="【"+getResources().getString(R.string.app_name)+"】";
                desc.append("您的好友").append( Account.user.getNickName()).append("邀请您参加优惠活动，赶紧去参加...");
                StringBuilder url=new StringBuilder();
                url.append(Config.discountUrl).append("&from=xx").append("&shopId=").append(Config.shopId).append("&phone=null&price=null");
                url.append("&title=").append(appName).append("到店优惠享不停!").append("&desc=").append("您的好友").append(Account.user.getNickName()).append("邀请您参加优惠活动，赶紧去参加...");
                shareDialog.show(appName+"到店优惠享不停!", desc.toString(),url.toString() , shareImage,"img_ddyh.png", null);
            }
        });
        txtTitle.setText("到店优惠");
        ViewUtils.setVisible(btnRight);
        loadH5(getH5Url());

    }
    public String getH5Url()
    {
        StringBuilder sb = null;
        if ( Config.discountUrl != null) {
            sb = new StringBuilder(Config.discountUrl);
            sb.append("&from=app").append("&shopId=").append(Config.shopId).append("&TargetID=").append(Config.shopId);
            if (Account.user != null) {
                String phone = Account.user.getMobilePhone();

                sb.append("&phone=").append(phone);
                sb.append("&userId=").append(Account.user.getId());
                sb.append("&Authorization=").append(PreferUtil.getInstance().getLoginToken());
            } else {
                sb.append("&phone=null");
            }
            sb.append("&DeviceID=").append(Config.imei);
            price = getIntent().getFloatExtra("price", 0);
            if (price > 0) {
                sb.append("&price=").append(price);
            } else {
                sb.append("&price=null");
            }
            return  sb.toString();
        }else
            ViewUtils.showToast("到店优惠异常");
        return  "";

    }
    protected void goBack(){
        String webUrl=webView.getUrl();
        if(webUrl.contains("ddyh/index.html"))
        {
            finish();
            return;
        }else if(webUrl.contains("shopList.html"))
        {
            txtTitle.setText("到店优惠");
            btnRight.setVisibility(View.VISIBLE);
        }
        if(webView.canGoBack()){
            webView.goBack();
        }else {
            finish();
        }
    }
    public void loadH5(final String loadUrl) {
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(true);
        //	webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSaveFormData(true); // 支持保存数据
        webView.getSettings().setAppCacheEnabled(true);//是否使用缓存
        webView.getSettings().setDomStorageEnabled(true);//DOM Storage

        webView.setHorizontalScrollBarEnabled(false);//水平不显示
        webView.setVerticalScrollBarEnabled(false); //垂直不显示
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                ViewUtils.setVisible(webView);
                loadingview.onLoadingComplete();

            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("tel:")){
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url));
                    startActivity(intent);
                    return true;
                }else if(url.contains("shopList.html"))
                {
                    txtTitle.setText("分店地址列表");
                    btnRight.setVisibility(View.GONE);
                }
                return false;
            }
        });


        RemoteInvokeService jsObj = new RemoteInvokeService(this, webView, new ParameCallBack() {
            @Override
            public void onCall(Object object) {

            }
        });

        webView.addJavascriptInterface(jsObj, "js_invoke");
        loadingview.startLoading();
        webView.loadUrl(loadUrl);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public static void start(Activity curAct, float price) {
        Intent intent = new Intent(curAct, ToStorePrivilegeAct.class);
        intent.putExtra("price",price);
        start(curAct,intent);
    }
    public static void start(Activity curAct) {
        Intent intent = new Intent(curAct, ToStorePrivilegeAct.class);
       ;start(curAct,intent);
    }
    private static void start(Activity curAct,Intent intent)
    {
        if (Account.user == null) {
            LoginAct.start(curAct);
        }else
        {
            ViewUtils.startActivity(intent, curAct);
        }

    }
}
