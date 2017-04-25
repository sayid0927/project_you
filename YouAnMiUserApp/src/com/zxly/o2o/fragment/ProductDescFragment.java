package com.zxly.o2o.fragment;

import android.annotation.SuppressLint;
import android.util.DisplayMetrics;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ScrollView;

import com.easemob.easeui.widget.MyWebView;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

/**
 * Created by dsnx on 2015/9/10.
 */
public class ProductDescFragment  extends BaseFragment{
    private MyWebView webView;
    private String htmlContent="";
    private LoadingView loadingview;
    private ScrollView scrollview;
    public ProductDescFragment(){

    }
    @SuppressLint("ValidFragment")
    public ProductDescFragment(String htmlContent)
    {
        if(htmlContent!=null)
        {
            this.htmlContent=htmlContent;
        }

    }
    public ScrollView getScrollView()
    {
        return  scrollview;
    }
    @Override
    protected void initView() {
        webView=(MyWebView)findViewById(R.id.web_viewt);
        loadingview = (LoadingView) findViewById(R.id.view_loading);
        scrollview= (ScrollView) findViewById(R.id.scrollview);
        if(!StringUtil.isNull(this.htmlContent))
        {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setSupportZoom(false);
            webView.getSettings().setBuiltInZoomControls(false);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.loadData(htmlContent, "text/html; charset=UTF-8", null);
        }else
        {
            ViewUtils.setVisible(loadingview);
            loadingview.onDataEmpty("暂时没有商品描述");
        }

    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_product_desc;
    }
}
