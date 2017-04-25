package com.easemob.easeui.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.easemob.chatuidemo.R;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.request.HXNormalRequest;
import com.easemob.easeui.widget.EaseMyFlipperView;
import com.easemob.easeui.widget.easepullrefresh.EasePullToRefreshBase;
import com.easemob.easeui.widget.easepullrefresh.EasePullToRefreshListView;
import com.easemob.easeui.widget.easepullrefresh.EasePullToRefreshBase.Mode;
import com.easemob.easeui.widget.easepullrefresh.EasePullToRefreshBase.OnRefreshListener;

/**
 * Created by Administrator on 2015/12/8.
 */
public abstract class EaseBaseMyListPageActivity extends EaseBaseActivity implements OnRefreshListener{
    protected int page = 0;
    protected byte isLastPage;
    protected EasePullToRefreshListView mListView;
    protected Adapter objectAdapter;

    protected HXNormalRequest myRequest;
    protected LayoutInflater mInflater;
    protected RelativeLayout rootLayout;

    protected abstract void loadData();
    protected EaseMyFlipperView viewContainer;

    protected void setListView(){
        mListView = (EasePullToRefreshListView) findViewById(R.id.pull_to_refreshlistview);
        EaseConstant.setRefreshText(mListView);
        mListView.setOnRefreshListener(this);
        mListView.setIntercept(true);
    }


    @Override
    public void onRefresh(EasePullToRefreshBase refreshView) {
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
                }, 1000);
                if (isLastPage == 1)
                    Toast.makeText(this,"已经是最后一页了!",Toast.LENGTH_SHORT).show();
                isLastPage = 2;
            }
        }

    }

    protected void setFlipper() {
        viewContainer = (EaseMyFlipperView) findViewById(R.id.list_layout);
        viewContainer.getRetryBtn().setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View arg0) {
                viewContainer.setDisplayedChild(0,true);
                loadData();
            }
        });
    }
}
