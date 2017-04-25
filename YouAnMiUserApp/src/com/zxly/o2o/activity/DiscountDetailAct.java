package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.dialog.ShareDialog;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.service.RemoteInvokeService;
import com.zxly.o2o.util.BitmapUtil;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

/**
 * @author fengrongjian 2016-6-3
 * @description 我的优惠详情
 */
public class DiscountDetailAct extends BasicAct implements View.OnClickListener {
    private Context context;
    private LoadingView loadingView;
    private View viewGuide,btnShare;
    private WebView webView;
    private ShareDialog shareDialog;
    private String h5Url;
    private long id;
    private String discount;
    private int type;
    private TextView txtTitle;
    private String title = "到店优惠享不停!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_discount_detail);
        context = this;
        id = getIntent().getLongExtra("id", 0);
        discount = getIntent().getStringExtra("discount");
        type = getIntent().getIntExtra("type", 0);
        if (id != 0 && Config.discountUrl != null) {
//            h5Url = Config.discountUrl + "/detail.html?from=app&detailId=" + id;
            StringBuilder builder = new StringBuilder(Config.discountUrl);
            builder.append("/detail.html?from=app&detailId=").append(id);
            builder.append("&shopId=").append(Config.shopId);
            builder.append("&DeviceID=").append(Config.imei);
            builder.append("&TargetID=").append(Config.shopId);
            builder.append("&userId=").append(Account.user.getId());
            builder.append("&Authorization=").append(PreferUtil.getInstance().getLoginToken());
            h5Url = builder.toString();
            initViews();
        } else {
            ViewUtils.showToast("优惠券异常");
        }
    }

    private void initViews() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_share).setOnClickListener(this);
        txtTitle= (TextView)findViewById(R.id.txt_title);
        txtTitle.setText("详情");
        loadingView = (LoadingView) findViewById(R.id.view_loading);
        btnShare=findViewById(R.id.btn_share);
        viewGuide = findViewById(R.id.view_guide);
        viewGuide.setOnClickListener(this);
        webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);// 支持JavaScript
        webView.getSettings().setBuiltInZoomControls(false);// 支持缩放
        webView.getSettings().setSaveFormData(false);// 支持保存数据
        webView.clearCache(true);// 清除缓存
        webView.clearHistory();// 清除历史记录
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                loadingView.startLoading();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loadingView.onLoadingComplete();
                ViewUtils.setVisible(webView);
                if (PreferUtil.getInstance().getIsShowDiscountGuide()) {
                    ViewUtils.setVisible(viewGuide);
                    PreferUtil.getInstance().setDiscountGuideHasOpen();
                }
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

                    btnShare.setVisibility(View.GONE);
                }
                return false;
            }
        });
        webView.loadUrl(h5Url);
        webView.addJavascriptInterface(new RemoteInvokeService(DiscountDetailAct.this, webView, new CallBack() {
            @Override
            public void onCall() {

            }
        }), "js_invoke");
    }
    protected void goBack(){
        String webUrl=webView.getUrl();
        if(webUrl.contains("detail.html"))
        {
            finish();
            return;
        }else if(webUrl.contains("shopList.html"))
        {
            txtTitle.setText("详情");
            btnShare.setVisibility(View.VISIBLE);
        }
        if(webView.canGoBack()){
            webView.goBack();
        }else {
            finish();
        }
    }
    public static void start(Activity curAct, long id) {
        Intent intent = new Intent(curAct, DiscountDetailAct.class);
        intent.putExtra("id", id);
        ViewUtils.startActivity(intent, curAct);
    }

    public static void start(Activity curAct, long id, String discount, int type) {
        Intent intent = new Intent(curAct, DiscountDetailAct.class);
        intent.putExtra("id", id);
        intent.putExtra("discount", discount);
        intent.putExtra("type", type);
        ViewUtils.startActivity(intent, curAct);
    }

    public String getH5Url() {
        StringBuilder sb = new StringBuilder(Config.newDiscountDetailUrl);
        sb.append("?from=app1").append("&shopId=").append(Config.shopId);
        sb.append("&phone=null");
        sb.append("&price=null");
        return sb.toString();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                goBack();
                break;
            case R.id.btn_share:
                ViewUtils.setGone(viewGuide);
                if (shareDialog == null) {
                    shareDialog = new ShareDialog();
                }
                String desc = null;
                if (StringUtil.isNull(discount)) {
                    if (type == 1) {
                        desc = "您的好友" + Account.user.getNickName() + "送您一张抵扣券, 赶快领取吧~";
                    } else if (type == 2) {
                        desc = "您的好友" + Account.user.getNickName() + "送您一个礼品, 赶快领取吧~";
                    } else {
                        desc = "您的好友" + Account.user.getNickName() + "送您一份大礼, 赶快领取吧~";
                    }
                } else {
                    if (type == 1) {
                        desc = "您的好友" + Account.user.getNickName() + "送您一张" + discount + "抵扣券, 赶快领取吧~";
                    } else if (type == 2) {
                        desc = "您的好友" + Account.user.getNickName() + "送您一个" + discount + ", 赶快领取吧~";
                    }
                }
                String url = getH5Url();
                Object shareImage = BitmapUtil.drawableToBitmap(context.getResources().getDrawable(R.drawable.img_ddyh));
                shareDialog.show(title, desc, url, shareImage, null);
                break;
            case R.id.view_guide:
                ViewUtils.setGone(viewGuide);
                break;
        }
    }

}
