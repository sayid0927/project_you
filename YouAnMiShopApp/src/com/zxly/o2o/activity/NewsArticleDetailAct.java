package com.zxly.o2o.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.easeui.widget.MyWebView;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.dialog.ShareDialog;
import com.zxly.o2o.model.ShopArticle;
import com.zxly.o2o.request.BaseRequest.ResponseStateListener;
import com.zxly.o2o.request.MyNewsArticleDetailRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.StringUtil;

/**
 * Created by Benjamin on 2015/7/20.
 */
public class NewsArticleDetailAct extends BasicAct {
    private ShareDialog shareDialog;
    private long detailId;
    private MyWebView mWebView;
    private ImageView shareBtn;
    private MyNewsArticleDetailRequest myNewsArticleDetailRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_article_detail_layout);
        shareBtn = (ImageView) findViewById(R.id.article_share);
        setUpActionBar(getActionBar());

        detailId = getIntent().getLongExtra("articleId", -1);

        initView();

        loadData();
    }

    private void loadData() {
        myNewsArticleDetailRequest = new MyNewsArticleDetailRequest(detailId);
        myNewsArticleDetailRequest.setOnResponseStateListener(new ResponseStateListener() {
            @Override
            public void onOK() {
//                mWebView.loadUrl("http://yamtest.youanmi.com/yam/M00/00/17/oYYBAFW4rVWACRVbAAAKnx1DwDk56.html");
//                Log.e("http://yamtest.youanmi.com/yam/M00/00/17/oYYBAFW4rVWACRVbAAAKnx1DwDk56.html");
                mWebView.loadUrl(myNewsArticleDetailRequest.shopArticle.getUrl());
                ((TextView) findViewById(R.id.article_detail_title)).setText(Html.fromHtml(
                        String.format(AppController.getInstance().getResources()
                                        .getString(
                                                R.string.article_detail_title_text),
                                myNewsArticleDetailRequest.shopArticle
                                        .getArticleFrom(),
                                StringUtil.getShortTime(
                                        myNewsArticleDetailRequest.shopArticle
                                                .getCreateTime())
                        )));

            }

            @Override
            public void onFail(int code) {

            }
        });
        myNewsArticleDetailRequest.start(this);
    }

    private void initView() {
        shareBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shareDialog==null)
                    shareDialog=new ShareDialog();

                ShopArticle article=myNewsArticleDetailRequest.shopArticle;
                shareDialog.show(article.getTitle(),article.getUrl(),article.getHead_url(),null);
            }
        });
        mWebView = ((MyWebView) findViewById(R.id.article_detail_maimcontent));
         mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setLoadWithOverviewMode(true);
    }

    private void setUpActionBar(final ActionBar actionBar) {
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(R.layout.tag_title);
            ((TextView) actionBar.getCustomView().findViewById(R.id.tag_title_title_name))
                    .setText("行业新闻");
            findViewById(R.id.tag_title_btn_back).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
}
