package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zxly.o2o.adapter.ShopArticleAdapter;
import com.zxly.o2o.model.ShopArticle;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.ShopArticleRequest;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengrongjian 2016-7-28
 * @description 指定类型文章列表
 */
public class ShopArticleAct extends BasicAct implements View.OnClickListener, PullToRefreshBase.OnRefreshListener {
    private Context context;
    private PullToRefreshListView listView;
    private ShopArticleAdapter shopArticleAdapter;
    private LoadingView loadingView;
    private List<ShopArticle> shopArticleList = new ArrayList<ShopArticle>();
    private int pageIndex = 1;
    private ShopArticleRequest shopArticleRequest;

    /**
     * 来源类型
     * 1：店铺热文 2：平台专享
     */
    public byte type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_shop_article_list);
        context = this;
        findViewById(R.id.btn_back).setOnClickListener(this);
        type = getIntent().getByteExtra("type", (byte) 0);
        if (type == 1) {
            ((TextView) findViewById(R.id.txt_title)).setText("店铺热文");
        } else {
            ((TextView) findViewById(R.id.txt_title)).setText("平台专享");
        }

        loadingView = (LoadingView) findViewById(R.id.view_loading);

        listView = (PullToRefreshListView) findViewById(R.id.list_view);
        listView.setIntercept(true);
        listView.setMode(PullToRefreshBase.Mode.BOTH);


        shopArticleAdapter = new ShopArticleAdapter(context, ShopArticleAct.this, false);
        shopArticleAdapter.setArticleType(type);
        listView.setAdapter(shopArticleAdapter);
        ViewUtils.setRefreshText(listView);
        listView.setOnRefreshListener(this);
        shopArticleAdapter.addItem(shopArticleList, true);
        loadData(pageIndex);
    }


    public static void start(Activity curAct, byte type) {
        Intent intent = new Intent(curAct, ShopArticleAct.class);
        intent.putExtra("type", type);
        ViewUtils.startActivity(intent, curAct);
    }

    private void loadData(final int pageId) {
        shopArticleRequest = new ShopArticleRequest(type, pageId);
        shopArticleRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                ViewUtils.setVisible(listView);
                shopArticleList = shopArticleRequest.getShopArticleList();
                if (!DataUtil.listIsNull(shopArticleList)) {
                    if (pageId == 1) {
                        shopArticleAdapter.clear();
                    }
                    shopArticleAdapter.addItem(shopArticleList, true);
                    pageIndex++;
                    loadingView.onLoadingComplete();
                } else {
                    if (pageId == 1) {
                        shopArticleAdapter.clear();
                        shopArticleAdapter.notifyDataSetChanged();
                        loadingView.onDataEmpty("没有文章");
                    } else {
                        ViewUtils.showToast("最后一页了");
                        loadingView.onLoadingComplete();
                    }
                }
                if (listView.isRefreshing()) {
                    listView.onRefreshComplete();
                }
            }

            @Override
            public void onFail(int code) {
                ViewUtils.setGone(listView);
                loadingView.onLoadingFail();
                if (listView.isRefreshing()) {
                    listView.onRefreshComplete();
                }
            }
        });
        loadingView.setOnAgainListener(new LoadingView.OnAgainListener() {
            @Override
            public void onLoading() {
                loadingView.startLoading();
                shopArticleRequest.start(this);
            }
        });
        if (!listView.isRefreshing()) {
            loadingView.startLoading();
        }
        shopArticleRequest.start(this);
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
            pageIndex = 1;
            loadData(pageIndex);
        }
        if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
            loadData(pageIndex);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }

}
