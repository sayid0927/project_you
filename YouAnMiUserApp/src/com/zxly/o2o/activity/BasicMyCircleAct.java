package com.zxly.o2o.activity;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.zxly.o2o.adapter.BasicMyCircleAdapter;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.Mode;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.MyCircleRequest;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.MyFlipperView;

/**
 *     @author huangbin  @version 创建时间：2015-1-27 上午10:34:15    类说明: 
 */
public abstract class BasicMyCircleAct extends BasicAct implements OnRefreshListener {
    protected int page = 0;
    protected byte isLastPage;
    protected PullToRefreshListView mListView;
    protected BasicMyCircleAdapter objectAdapter;

    protected MyCircleRequest myRequest;
    protected LayoutInflater mInflater;
    protected RelativeLayout rootLayout;
    protected final int IS_TOP = 1;
    protected int pageType;

    // 公用bitMap
    public static Bitmap photoBitmap;
    protected MyFlipperView viewContainer;
    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        if (refreshView.getCurrentMode() == Mode.PULL_FROM_START) { // 加载下拉数据
            page = 1;
            MyCircleRequest.publishTopic = null;
            photoBitmap = null;
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
                if (isLastPage == 1)
                    ViewUtils.showToast(getResources().getString(R.string.last_page));
                isLastPage = 2;
            }
        }

    }

    protected abstract void loadData();

    protected void setFlipper() {
        viewContainer = (MyFlipperView) findViewById(R.id.list_layout);
        viewContainer.getRetryBtn().setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View arg0) {
                viewContainer.setDisplayedChild(0,true);
                loadData();
            }
        });
    }

}
