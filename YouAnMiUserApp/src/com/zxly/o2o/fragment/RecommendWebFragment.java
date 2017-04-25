package com.zxly.o2o.fragment;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.easemob.easeui.AppException;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshWebView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.service.RemoteInvokeService;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

/**
 * Created by kenwu on 2015/12/3.
 */
public class RecommendWebFragment extends BaseFragment implements View.OnClickListener{
    private PullToRefreshWebView mWebview;
    private LoadingView loadingView;
    private String appName;
    String browserUrl;
    RemoteInvokeService jsObj;
    private boolean isInitWebPage;//暂时没有想到其他判断webView加载完的方法,先用这个


    public static RecommendWebFragment newInstance(String browserUrl){
        RecommendWebFragment f=new RecommendWebFragment();
        Bundle args = new Bundle();
        args.putString("browserUrl", browserUrl);
        f.setArguments(args);
        return f;
    }


    @Override
    protected void initView() {
    }


    @Override
    protected void initView(Bundle bundle) {
//	new RemoteInvokeService().openView("aaaa");
        if(mWebview!=null)
            return;

        browserUrl=bundle.getString("browserUrl");
    //   browserUrl="http://192.168.1.174:3000/dist/discovery.html?shopId=30&baseUrl="+ DataUtil.encodeBase64(Config.dataBaseUrl)+"&brandName=Xiaomi&DeviceID=867451021080988&DeviceType=1";
//        browserUrl="http://192.168.1.189:8089/multiTemp/discovery.html?shopId=30&baseUrl=aHR0cDovL3Vpcy5nb3V3dWdvbmd5dWFuLmNvbS8=%20&brandName=%E5%B0%8F%E7%B1%B3&DeviceID=867451021080988&DeviceType=1&userId=28595";


        appName=getActivity().getResources().getString(R.string.app_name);
        TextView txtTitle= (TextView) findViewById(R.id.txt_title);
        ViewUtils.setText(txtTitle, appName);


        mWebview = (PullToRefreshWebView) findViewById(R.id.pull_refresh_webview);
        mWebview.setVisibility(View.GONE);
        ViewUtils.setRefreshText(mWebview);
        mWebview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);

        mWebview.getRefreshableView().getSettings().setBuiltInZoomControls(false); // 支持缩放
        mWebview.getRefreshableView().getSettings().setJavaScriptEnabled(true);
        mWebview.getRefreshableView().getSettings().setSupportZoom(true);
        mWebview.getRefreshableView().getSettings().setBuiltInZoomControls(true);
        mWebview.getRefreshableView().getSettings().setUseWideViewPort(true);
        mWebview.getRefreshableView().getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebview.getRefreshableView().getSettings().setLoadWithOverviewMode(true);
        mWebview.getRefreshableView().getSettings().setSaveFormData(false); // 支持保存数据
        mWebview.getRefreshableView().clearCache(true); // 清除缓存
        mWebview.getRefreshableView().clearHistory(); // 清除历史记录
        mWebview.getRefreshableView().getSettings().setAppCacheEnabled(true);//是否使用缓存
        mWebview.getRefreshableView().getSettings().setDomStorageEnabled(true);//DOM Storage

        mWebview.getRefreshableView().setHorizontalScrollBarEnabled(false);//水平不显示
        mWebview.getRefreshableView().setVerticalScrollBarEnabled(false); //垂直不显示

        jsObj = new RemoteInvokeService(getActivity(), null, new ParameCallBack() {
            @Override
            public void onCall(final Object object) {
                loadingView.post(new Runnable() {
                    @Override
                    public void run() {
                        if(((Integer)object)==RemoteInvokeService.LOADING_FINISH){
                            mWebview.setVisibility(View.VISIBLE);
                            loadingView.onLoadingComplete();
                            mWebview.onRefreshComplete();
                        }else if(((Integer)object)==RemoteInvokeService.LOADING_FAIL){;
                            mWebview.setVisibility(View.GONE);
                            loadingView.onLoadingFail();
                            //    mWebview.onRefreshComplete();
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
                //	view.loadUrl(url);
                return true;
            }

            /** 错误返回 */
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                this.errorCode = errorCode;
                loadingView.onLoadingFail();
            }

            /** 页面载入完毕 */
            @Override
            public void onPageFinished(WebView view, String url) {
                //	setProgressBarIndeterminateVisibility(false);// 设置标题栏的滚动条停止
                super.onPageFinished(view, url);

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


        mWebview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<WebView>() {
            @Override
            public void onRefresh(PullToRefreshBase<WebView> refreshView) {
                mWebview.getRefreshableView().loadUrl("javascript:refreshDiscoveryView()");
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
    protected int layoutId() {
        return R.layout.tab_recommend;
    }



    @Override
    public void onClick(View v) {

    }



    static class DataRequest extends BaseRequest {

        String jsData;

        public DataRequest(){
            addParams("shopId", Config.shopId);
        }

        public String getJsData() {
            return jsData;
        }

        @Override
        protected void fire(String data) throws AppException {
            jsData=data;
        }

        @Override
        protected String method() {
            return "/appFound/articles";
        }
    }
}
