package com.zxly.o2o.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.MyFlipperView;

public abstract class BaseListViewFragment extends BaseFragment implements AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener {
    protected int page = 1;
    protected boolean isLastPage;
    protected BaseRequest myRequest;
    protected BaseRequest.ResponseStateListener responseStateListener;
    protected PullToRefreshListView mListView;
    protected ObjectAdapter objectAdapter;
    protected MyFlipperView viewContainer;
    protected boolean isTokenInvaild = true;
    protected boolean isFirstLoad = true;


    @Override
    public void onResume() {
        super.onResume();
        if (isTokenInvaild) {
            isTokenInvaild = false;
            setFlipper();
            mMainHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadData();
                }
            }, 1000);

        }
    }

    private void setFlipper() {
        if (viewContainer == null) {
            viewContainer = (MyFlipperView) findViewById(R.id.list_layout);
            viewContainer.getRetryBtn().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    viewContainer.setDisplayedChild(0, true);
                    loadData();
                }
            });
        }
    }

    @Override
    protected void initView() {
        setListViewAvailable();
        initListView(null);
        mListView.setAdapter(objectAdapter);
    }

    @Override
    protected void initView(Bundle bundle) {
        super.initView(bundle);
        setListViewAvailable();
        initListView(bundle);
        mListView.setAdapter(objectAdapter);

    }

    private void setListViewAvailable() {
        mListView = (PullToRefreshListView) findViewById(R.id.pull_to_refreshlistview);
        ViewUtils.setRefreshText(mListView);
        mListView.setOnItemClickListener(this);
        mListView.setOnRefreshListener(this);
        mListView.setIntercept(true);
    }


    protected abstract void loadData();

    protected abstract void initListView(Bundle bundle);

    @Override
    protected int layoutId() {
        return R.layout.pull_to_refresh_layout;
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
            // 加载下拉数据
            page = 1;
            loadData();
        } else if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
            // 加载上拉数据
            if (!isLastPage) {
                page += 1;
                loadData();
            } else {
                mMainHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mListView.onRefreshComplete();
                    }
                }, 1000);
                ViewUtils.showToast("亲，没有新数据");
            }
        }
    }


}
