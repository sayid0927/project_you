/*
 * 文件名：MyOrderListFragment.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： MyOrderListFragment.java
 * 修改人：wuchenhui
 * 修改时间：2015-5-27
 * 修改内容：新增
 */
package com.zxly.o2o.fragment;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.zxly.o2o.activity.FragmentListAct;
import com.zxly.o2o.adapter.CustomArticleAdapter;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.Mode;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.BaseRequest.ResponseStateListener;
import com.zxly.o2o.request.IndustriesArticleRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

/**
 * 自定义文章
 *
 * @author dsnx
 * @version YIBA-O2O 2016-9-8
 * @since YIBA-O2O
 */
public class PromotionArticleFragment extends BaseFragment implements View.OnClickListener {

    private PullToRefreshListView mListView;
    private LoadingView loadingView;
    private CustomArticleAdapter adapter;
    private View btnPaste, viewHnArticle, btnClose;
    private TextView  txtEmptyHint, btnHnArticle;
    private IndustriesArticleRequest  industriesArticleRequest;
    private int pageIndex=1;
    private boolean noCustomArticle = false;
    public static PromotionArticleFragment newInstance() {
        PromotionArticleFragment f = new PromotionArticleFragment();
        return f;
    }

    @Override
    protected void initView() {
        loadingView = (LoadingView) findViewById(R.id.view_loading11);
        mListView = (PullToRefreshListView) findViewById(R.id.listview);
        viewHnArticle = findViewById(R.id.ll_hn_article);
        btnClose = findViewById(R.id.btn_close);
        btnHnArticle = (TextView) findViewById(R.id.btn_hn_article);
        ViewUtils.setRefreshText(mListView);
        ViewGroup headView = (ViewGroup) LayoutInflater.from(getActivity()).inflate(R.layout.item_custom_article_list, null);
        mListView.addH(headView);
        btnPaste = headView.findViewById(R.id.btn_paste);
        txtEmptyHint = (TextView) headView.findViewById(R.id.txt_empty_hint);
        btnPaste.setOnClickListener(this);
        viewHnArticle.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        mListView.setIntercept(true);
        //mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (refreshView.getCurrentMode() == Mode.PULL_FROM_START) {
                    if(noCustomArticle){
                        industriesArticle(1);
                    } else {
                        loadData(1);
                    }
                }
                if (refreshView.getCurrentMode() == Mode.PULL_FROM_END) {
                    // 加载上拉数据
                    if(noCustomArticle){
                        industriesArticle(pageIndex);
                    } else {
                        loadData(pageIndex);
                    }
                }

            }
        });

        loadingView.setOnAgainListener(new LoadingView.OnAgainListener() {
            @Override
            public void onLoading() {
                loadingView.startLoading();
                loadData(1);
            }
        });
        adapter = new CustomArticleAdapter(getActivity());
        adapter.setArticleDeleteCallBack(new ParameCallBack() {
            @Override
            public void onCall(Object object) {
                if(adapter.isEmpty())
                {
                    resetAdapter();
                    industriesArticle(1);
                }
            }
        });
        mListView.setAdapter(adapter);


    }
    private void resetAdapter()
    {
        adapter = new CustomArticleAdapter(getActivity());
        adapter.setArticleDeleteCallBack(new ParameCallBack() {
            @Override
            public void onCall(Object object) {
                if(adapter.isEmpty())
                {
                    resetAdapter();
                    industriesArticle(1);
                }
            }
        });
        mListView.setAdapter(adapter);
    }

    @Override
    protected void loadInitData() {
        if (DataUtil.listIsNull(adapter.getContent())) {
            loadData(1);
        }
    }

    public void loadData(final int _pageIndex) {
        if (DataUtil.listIsNull(adapter.getContent())) {
            loadingView.startLoading();
        }
        final ArticelListRequest request = new ArticelListRequest(_pageIndex);
        request.setOnResponseStateListener(new ResponseStateListener() {
            @Override
            public void onOK() {
                loadingView.onLoadingComplete();
                mListView.onRefreshComplete();
                if(_pageIndex==1)
                {
                    pageIndex=_pageIndex;
                    if (request.articleList.isEmpty()) {
                        noCustomArticle = true;
                        industriesArticle(1);
                    } else {
                        mListView.setMode(Mode.BOTH);
                        ViewUtils.setGone(txtEmptyHint);
                        ViewUtils.setVisible(viewHnArticle);
                        pageIndex++;
                        adapter.clear();
                        adapter.addItem(request.articleList);
                        adapter.setDelItem(true);
                        adapter.notifyDataSetChanged();
                        btnClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ViewUtils.setGone(viewHnArticle);
                            }
                        });

                    }
                }else
                {
                    if(!request.articleList.isEmpty())
                    {
                        pageIndex++;
                        adapter.addItem(request.articleList);
                        adapter.notifyDataSetChanged();
                    }
                }
                if(request.hasNext||request.articleList.isEmpty()){
                    mListView.setMode(Mode.BOTH);
                } else {
                    mListView.setMode(Mode.PULL_FROM_START);
                }
            }

            @Override
            public void onFail(int code) {
                loadingView.onLoadingComplete();
                mListView.onRefreshComplete();
                loadingView.onLoadingFail();
            }
        });
        request.start(this);


    }
    private void  industriesArticle(final int _pageIndex)
    {
        mListView.setMode(Mode.BOTH);
        industriesArticleRequest = new IndustriesArticleRequest(_pageIndex);
        industriesArticleRequest.setOnResponseStateListener(new ResponseStateListener() {
            @Override
            public void onOK() {
                ViewUtils.setVisible(txtEmptyHint);
                ViewUtils.setGone(viewHnArticle);
                mListView.onRefreshComplete();
                if(_pageIndex==1) {
                    pageIndex = _pageIndex;
                    if (!industriesArticleRequest.articleList.isEmpty()) {

                        pageIndex++;

                        adapter.clear();
                        adapter.addItem(industriesArticleRequest.articleList);
                        adapter.setDelItem(false);
                        adapter.notifyDataSetChanged();
//                        industriesArticleRequest.articleList.clear();
                    }
                }else
                {
                    if(!industriesArticleRequest.articleList.isEmpty())
                    {
                        pageIndex++;
                        adapter.addItem(industriesArticleRequest.articleList);
                        adapter.notifyDataSetChanged();
                    } else {
                        ViewUtils.showToast("没有更多了");
                    }
                }
            }

            @Override
            public void onFail(int code) {

            }
        });
        industriesArticleRequest.start();
    }

    @Override
    protected int layoutId() {
        return R.layout.win_custom_article;
    }


    @Override
    public void onClick(View v) {
        if (v == btnPaste) {
            String wxUrl = null;

            //wxUrl="http://mp.weixin.qq.com/s?__biz=MzA4NjA3NjIyNg==&mid=2652078409&idx=1&sn=e9fc98bd2130f48abc80453a826b2715&scene=4";

            // wxUrl="http://5566.net/";

            //适用3.0 之后的机器
            ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm.hasPrimaryClip()) {
                ClipData data = cm.getPrimaryClip();
                ClipData.Item item = data.getItemAt(0);
                wxUrl = item.getText().toString();
            }

            if (!TextUtils.isEmpty(wxUrl)) {
                Bundle bundle = new Bundle();
                bundle.putString("wxUrl", wxUrl);
                FragmentListAct.start("详情", FragmentListAct.PAGE_CUSTOM_ARTICLE_DETAIL, bundle, articleRefreshCallBack);
            } else {
                ViewUtils.showToast("请先去复制链接！");
            }
        } else if (v == viewHnArticle) {
            FragmentListAct.start("行内推荐文章", FragmentListAct.PAGE_INDUSTRIES_ARTICLE_LIST, null);
        }
    }

    private ParameCallBack articleRefreshCallBack = new ParameCallBack() {
        @Override
        public void onCall(Object object) {
            loadData(1);
        }
    };
}
