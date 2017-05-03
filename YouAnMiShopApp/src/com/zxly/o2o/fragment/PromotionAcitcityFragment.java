/*
 * 文件名：MyOrderListFragment.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： MyOrderListFragment.java
 * 修改人：wuchenhui
 * 修改时间：2015-5-27
 * 修改内容：新增
 */
package com.zxly.o2o.fragment;


import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.YamCollegeDetailAct;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.adapter.PromotionActivityAdapter;
import com.zxly.o2o.model.ActicityInfo;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.Mode;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.BaseRequest.ResponseStateListener;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.CollegeCourseView;
import com.zxly.o2o.view.LoadingView;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO 添加类的一句话简单描述。
 * <p/>
 * TODO 详细描述
 * <p/>
 * TODO 示例代码
 * <pre>
 * </pre>
 *
 * @author wuchenhui
 * @version YIBA-O2O 2015-5-27
 * @since YIBA-O2O
 */
public class PromotionAcitcityFragment extends BaseFragment implements ResponseStateListener {

    private PullToRefreshListView mListView;
    private ObjectAdapter adapter;
    private LoadingView loadingView;
    private int pageIndex;
    ActivityListRequest request;
    private CollegeCourseView collegeCourseView;

    public static PromotionAcitcityFragment newInstance() {
        PromotionAcitcityFragment f = new PromotionAcitcityFragment();
        Bundle args = new Bundle();
        args.putInt("status", 1);
        args.putInt("type", 1);
        f.setArguments(args);
        return f;
    }

    @Override
    protected void initView(Bundle bundle) {
        loadingView = (LoadingView) findViewById(R.id.view_loading11);
//        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) loadingView.getLayoutParams();
//        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//        lp.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
//        loadingView.setLayoutParams(lp);
        mListView = (PullToRefreshListView) findViewById(R.id.listview);
        mListView.setDivideHeight(0);
        mListView.setIntercept(true);
        ViewUtils.setRefreshText(mListView);
        mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {

                if (refreshView.getCurrentMode() == Mode.PULL_FROM_START) {
                    // 加载下啦数据
                    loadData(1);
                }
                if (refreshView.getCurrentMode() == Mode.PULL_FROM_END) {
                    // 加载上拉数据
                    loadData(pageIndex);
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

        if (adapter == null) {
            adapter = new PromotionActivityAdapter(getActivity());
        }

        mListView.setAdapter(adapter);
    }

    private void showCollegeCourse() {
        if (collegeCourseView == null) {
            RelativeLayout.LayoutParams courseLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            courseLp.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics());
            courseLp.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
            courseLp.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
            courseLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

            collegeCourseView = new CollegeCourseView(getActivity(),4);

            collegeCourseView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YamCollegeDetailAct.start(getActivity(),collegeCourseView.getId());
                }
            });
            content.addView(collegeCourseView, courseLp);
        }else
        {
            collegeCourseView.show();
        }

    }
    private void hideCollegeCourse()
    {
        if(collegeCourseView!=null)
        {
            collegeCourseView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void loadInitData() {
        if (DataUtil.listIsNull(adapter.getContent())) {
            loadData(1);
        }
    }

    public void loadData(final int pageId) {
        if (DataUtil.listIsNull(adapter.getContent()))
            loadingView.startLoading();

        if (request == null) {
            request = new ActivityListRequest();
            request.setOnResponseStateListener(this);
        }

        pageIndex = pageId;
        request.addParams("pageIndex", pageIndex);
        request.start(getActivity());
    }

    @Override
    protected int layoutId() {
        return R.layout.tag_listview;
    }


    @Override
    public void onOK() {
        if (!DataUtil.listIsNull(request.getDataList())) {
            if (pageIndex == 1)
                adapter.clear();

            adapter.addItem(request.getDataList(), true);
            request.setDataList(null);
            pageIndex++;
            loadingView.onLoadingComplete();
            hideCollegeCourse();
        } else {
            //下拉刷新的时候发现数据为空，
            if (pageIndex == 1) {
                adapter.clear();
                adapter.notifyDataSetChanged();
                if(Account.user.getRoleType()== Constants.USER_TYPE_SALESMAN)
                {
                    loadingView.onDataEmpty("店铺还没有活动呢，提醒老板发布活动吧。\n或者您可以去自定义文章推广您的文章", R.drawable.img_default_tired);
                }else
                {
                    loadingView.onDataEmpty("店铺还没有活动呢，赶紧去商户后台发布活动吧", R.drawable.img_default_tired);
                }
                showCollegeCourse();
            } else {
                //最后一页
            }
        }
        if (mListView.isRefreshing())
            mListView.onRefreshComplete();

        if(request.hasNext){
            mListView.setMode(Mode.BOTH);
        } else {
            mListView.setMode(Mode.PULL_FROM_START);
        }
    }


    @Override
    public void onFail(int code) {
        if (DataUtil.listIsNull(adapter.getContent()))
            loadingView.onLoadingFail();

        if (mListView.isRefreshing())
            mListView.onRefreshComplete();
    }


    @Override
    protected void initView() {
        // TODO Auto-generated method stub

    }


    class ActivityListRequest extends BaseRequest {
        private List<ActicityInfo> dataList = new ArrayList<ActicityInfo>();
        public boolean hasNext = true;

        public ActivityListRequest() {
            addParams("shopId", Account.user == null ? 0 : Account.user.getShopId());
            addParams("userId", Account.user == null ? 0 : Account.user.getId());
        }

        public List<ActicityInfo> getDataList() {
            return dataList;
        }

        public void setDataList(List<ActicityInfo> dataList) {
            this.dataList = dataList;
        }

        @Override
        protected void fire(String data) throws AppException {
            TypeToken<List<ActicityInfo>> types = new TypeToken<List<ActicityInfo>>() {
            };
            dataList = GsonParser.getInstance().fromJson(data, types);
            if(dataList.size() < 10){
                hasNext = false;
            }
        }

        @Override
        protected String method() {
            return "/makeFans/newPopuActivityList";
        }

    }


}
