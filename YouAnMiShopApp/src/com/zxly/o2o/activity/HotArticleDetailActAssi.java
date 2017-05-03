/*
 * 文件名：BasicMyCircleThirdActivity.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： BasicMyCircleThirdActivity.java
 * 修改人：Administrator
 * 修改时间：2015-1-10
 * 修改内容：新增
 */
package com.zxly.o2o.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.dialog.ShareDialog;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.Mode;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.ArticleReplyListRequest;
import com.zxly.o2o.request.MyHotArticleDetailRequest;
import com.zxly.o2o.request.PromoteCallbackConfirmRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.ShareListener;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.MyFlipperView;

/**
 * TODO 添加类的一句话简单描述。
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
public abstract class HotArticleDetailActAssi extends BasicAct
        implements OnClickListener, OnRefreshListener {
    protected View HeaderView, footView;
    protected Animation animation;
    protected long detailId;
    protected MyHotArticleDetailRequest myArticleDetailRequest;
    protected ArticleReplyListRequest articleReplyListRequest;
    protected ObjectAdapter objectAdapter;

    protected int page = 0;
    protected byte isLastPage;
    protected PullToRefreshListView mListView;

    protected LayoutInflater mInflater;
    protected RelativeLayout rootLayout;

    protected abstract void loadData();

    protected MyFlipperView viewContainer;

    protected ShareDialog shareDialog;

    protected String title;

    protected String shareUrl;

    protected String headUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_detail_basic_layout);
        mInflater = getLayoutInflater();
        rootLayout = (RelativeLayout) findViewById(R.id.mycircle_third_root_layout);
        findViewById(R.id.mycircle_third_title_back_icon).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mycircle_third_title_back_icon:
                finish();
                break;


            case R.id.mycircle_third_top_title_share_btn:
                if(shareDialog==null)
                    shareDialog=new ShareDialog();

                shareDialog.show(title, shareUrl, headUrl, new ShareListener() {
                    @Override
                    public void onComplete(Object var1) {
                        new PromoteCallbackConfirmRequest(myArticleDetailRequest.shopArticle.getId(), 2,title).start();
                    }

                    @Override
                    public void onFail(int errorCode) {

                    }
                });
                break;

            case R.id.reply_refresh_btn:
                page = 1;
                loadData();
                break;

        }
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        if (refreshView.getCurrentMode() == Mode.PULL_FROM_START) { // 加载下拉数据
            page = 1;
            loadData();
            isLastPage = 0;
        } else if (refreshView.getCurrentMode() == Mode.PULL_FROM_END) { // 加载上拉数据
            if (isLastPage == 0) {
                page += 1;
                loadData();
            } else {
                mMainHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mListView.onRefreshComplete();
                    }
                }, Constants.REFRESH_LAST_TIME);
                if (isLastPage == 1) {
                    ViewUtils.showToast(getResources().getString(R.string.last_page));
                }
                isLastPage = 2;
            }
        }

    }

    protected void setFlipper() {
        viewContainer = (MyFlipperView) findViewById(R.id.list_layout);
        viewContainer.getRetryBtn().setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View arg0) {
                viewContainer.setDisplayedChild(0, true);
                loadData();
            }
        });
    }
}
