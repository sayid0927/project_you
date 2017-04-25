package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.TextView;

import com.easemob.easeui.widget.MyWebView;
import com.zxly.o2o.adapter.ArticleDetailAdapter;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.ArticleReplyListRequest;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.MyHotArticleDetailRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

/**
 * TODO 三级界面。
 * <p>
 * TODO 详细描述
 * <p>
 * TODO 示例代码
 * <p>
 * <pre>
 * </pre>
 *
 * @author huangbin
 * @version YIBA-O2O 2015-1-10
 * @since YIBA-O2O
 */
public class HotArticleDetailAct extends HotArticleDetailActAssi
        implements OnRefreshListener, OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInflater.inflate(R.layout.pull_to_refresh_layout, rootLayout);
        detailId = getIntent().getLongExtra("articleId", -1);
        shareUrl = getIntent().getStringExtra("shareUrl");
        initArticle();
        setFlipper();
        loadData();
    }

    public static void start(Activity curAct, String shareUrl, long articleId) {
        Intent intent = new Intent(curAct, HotArticleDetailAct.class);
        intent.putExtra("shareUrl", shareUrl);
        intent.putExtra("articleId", articleId);
        curAct.startActivity(intent);
    }

    private void setArticleTopData() {
        viewContainer.setDisplayedChild(3);

//        StringUtil.setTextSpan(0, myArticleDetailRequest.shopArticle.getTitle().length(), 20,
//                Color.BLACK,
        ((TextView) HeaderView.findViewById(R.id.article_detail_title))
                .setText(Html.fromHtml(String.format(AppController.getInstance().getResources()
                                .getString(R.string.article_detail_title_text),
                        myArticleDetailRequest.shopArticle.getArticleFrom(),
                        StringUtil.getShortTime(myArticleDetailRequest.shopArticle.getCreateTime())
                )));

        ((TextView) HeaderView.findViewById(R.id.re_amount)).setText(new StringBuffer("已被浏览").append(myArticleDetailRequest.shopArticle.getRead_amount()).append("次"));
        HeaderView.findViewById(R.id.reply_refresh_btn).setOnClickListener(this);
        MyWebView mWebView = ((MyWebView) HeaderView.findViewById(R.id.article_detail_maimcontent));
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.loadUrl(myArticleDetailRequest.shopArticle.getUrl());
    }

    BaseRequest.ResponseStateListener responseStateListener = new BaseRequest.ResponseStateListener() {
        public void onFail(int code) {
            viewContainer.setDisplayedChild(2);
            mListView.onRefreshComplete();
        }

        public void onOK() {
            if (page > 0) {
                if (articleReplyListRequest.articleReplys == null || articleReplyListRequest.articleReplys
                        .size() == 0) {
                    isLastPage = 1;
                } else {
                    setNoDate(0x00000004);
                    if (page == 1) {
                        objectAdapter.clear();
                    }
                    objectAdapter.addItem(articleReplyListRequest.articleReplys, true);
                }
            } else {
                page++;
                if (myArticleDetailRequest.shopArticle == null) {
                    isLastPage = 1;
                } else {
                    setArticleTopData();
                    if (myArticleDetailRequest.shopArticle
                            .getReplys() == null || myArticleDetailRequest.shopArticle
                            .getReplys()
                            .size() == 0) {
                        isLastPage = 1;
                        setNoDate(0x00000000);
                    } else {
                        setNoDate(0x00000004);
                        if (page == 1) {
                            objectAdapter.clear();
                        }
                        objectAdapter.addItem(myArticleDetailRequest.shopArticle.getReplys(), true);
                    }
                }
            }
            ((TextView) HeaderView.findViewById(R.id.reply_refresh_btn))
                    .setText(
                            new StringBuffer("全部评论(")
                                    .append(myArticleDetailRequest.shopArticle.getReplyAmount())
                                    .append(")"));
            mListView.onRefreshComplete();
        }
    };

    private void setNoDate(int isVisible) {
        if (page == 1) {
            footView.findViewById(R.id.no_review).setVisibility(isVisible);
        }
    }


    private void setListViewAvailable() {
        animation = AnimationUtils.loadAnimation(this, R.anim.text_operate_anim);
        mListView = (PullToRefreshListView) findViewById(R.id.pull_to_refreshlistview);
        HeaderView = this.getLayoutInflater().inflate(R.layout.article_detail_include, null);
        footView = this.getLayoutInflater().inflate(R.layout.article_detail_foot_view, null);
        mListView.addH(HeaderView);
        mListView.addF(footView);
        footView.setOnClickListener(this);
        ViewUtils.setRefreshText(mListView);
        mListView.setOnRefreshListener(this);
        mListView.setIntercept(true);
    }

    private void initArticle() {
        setListViewAvailable();
        objectAdapter = new ArticleDetailAdapter(this, animation, mMainHandler);
        mListView.setAdapter(objectAdapter);
        findViewById(R.id.mycircle_third_article_detail_top_title).setVisibility(View.VISIBLE);
        findViewById(R.id.mycircle_third_top_title_share_btn).setOnClickListener(this);

    }

    @Override
    protected void loadData() {
        if (page == 0) {
            myArticleDetailRequest = new MyHotArticleDetailRequest(detailId);
            myArticleDetailRequest.setOnResponseStateListener(responseStateListener);
            myArticleDetailRequest.start(this);
        }  else {
            articleReplyListRequest = new ArticleReplyListRequest(page, detailId);
            articleReplyListRequest.setOnResponseStateListener(responseStateListener);
            articleReplyListRequest.start(this);
        }
    }
}
