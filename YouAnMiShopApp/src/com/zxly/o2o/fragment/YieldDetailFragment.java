package com.zxly.o2o.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.zxly.o2o.activity.FlowDetailAct;
import com.zxly.o2o.activity.ProductCommissionDetailAct;
import com.zxly.o2o.activity.YaoBaoDetailAct;
import com.zxly.o2o.adapter.YieldDetailAdapter;
import com.zxly.o2o.model.YieldDetail;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshExpandableListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.YieldDetailListRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.UmengUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

/**
 * Created by dsnx on 2015/12/15.
 */
public class YieldDetailFragment extends  BaseFragment implements PullToRefreshBase.OnRefreshListener{
    private PullToRefreshExpandableListView listView;
    private YieldDetailAdapter yieldDetailAdapter;
    private int type;//0:全部，1：商品佣金，2：流量返利，3：应用收益 4:延保
    private int pageIndex=1;
    private boolean isLastData;
    private LoadingView loadingview;
    private boolean isEmpty;

    public static YieldDetailFragment getInstance(int type){
        YieldDetailFragment ydFragment = new YieldDetailFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        ydFragment.setArguments(args);
        return ydFragment;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initView(Bundle bundle) {
        type=bundle.getInt("type");
        listView= (PullToRefreshExpandableListView) findViewById(R.id.list_view);
        yieldDetailAdapter =new YieldDetailAdapter(this.getActivity());
        listView.getRefreshableView().setGroupIndicator(null);
        listView.getRefreshableView().setAdapter(yieldDetailAdapter);
        loadingview = (LoadingView) findViewById(R.id.view_loading);
        ViewUtils.setRefreshListText(listView);

        listView.getRefreshableView().setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        listView.setOnRefreshListener(this);
        listView.getRefreshableView().setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                YieldDetail yieldDetail = (YieldDetail) yieldDetailAdapter.getChild(groupPosition, childPosition);
                switch (yieldDetail.getType()) {
                    case YieldDetail.YIEL_TYPE_PROUDCT://商品
                        ProductCommissionDetailAct.start(YieldDetailFragment.this.getActivity(),yieldDetail.getSerialNumber(),yieldDetail.getId());
                        break;
                    case YieldDetail.YIEL_TYPE_FLOW://流量
                        FlowDetailAct.start(YieldDetailFragment.this.getActivity(),yieldDetail.getSerialNumber(),yieldDetail.getId());
                        break;
                    case  YieldDetail.YIEL_TYPE_YB://延保
                        YaoBaoDetailAct.start(YieldDetailFragment.this.getActivity(),yieldDetail.getSerialNumber(),yieldDetail.getId());
                        break;
                    case YieldDetail.YIEL_TYPE_YYFF://应用分发
                         ViewUtils.showToast("此处暂无内页");
                        break;
                }
                return true;
            }
        });
        loadingview.startLoading();
        loadData(pageIndex);

        UmengUtil.onEvent(getActivity(),new  UmengUtil().INCOME_ENTER,null);

    }
    private  void  loadData(final int _pageIndex)
    {
        final YieldDetailListRequest yieldDetailRequest=new YieldDetailListRequest(_pageIndex,type);
        yieldDetailRequest.setTag(this);
        yieldDetailRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                isEmpty = yieldDetailRequest.getYieldDetailList().isEmpty();
                 listView.onRefreshComplete();
                    if (isEmpty) {
                        if(_pageIndex==1)
                        {
                            loadingview.onDataEmpty("您还没有收益呢,要加油哦!",true,R.drawable.img_default_happy);
                            loadingview.setBtnText("去赚钱");

                            UmengUtil.onEvent(getActivity(),new UmengUtil().INCOME_MAKEMONEY_CLICK,null);
                        }else
                        {
                            isLastData = true;
                            ViewUtils.showToast("亲! 没有更多了");
                        }

                    } else {
                        loadingview.onLoadingComplete();
                        if (_pageIndex == 1) {
                            yieldDetailAdapter.clear();
                        }
                        yieldDetailAdapter.addContent(yieldDetailRequest.getYieldDetailList());
                        yieldDetailAdapter.notifyDataSetChanged();
                        for(int i=0;i< yieldDetailAdapter.getGroupCount();i++)
                        {
                            listView.getRefreshableView().expandGroup(i);
                        }
                    }
                if(yieldDetailRequest.hasNextPage){
                    listView.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
            }

            @Override
            public void onFail(int code) {
                listView.onRefreshComplete();
                loadingview.onLoadingFail();
            }
        });
        yieldDetailRequest.start(this);

        loadingview.setOnAgainListener(new LoadingView.OnAgainListener() {
            @Override
            public void onLoading() {
                if(isEmpty){
                    if(!getActivity().isFinishing()){
                        getActivity().finish();
                    }
                }else {
                    loadingview.startLoading();
                    yieldDetailRequest.start(this);
                }
            }
        });
    }

    @Override
    protected int layoutId() {
        return R.layout.win_yield_detail_fragment;
    }


    @Override
    public void onRefresh(PullToRefreshBase refreshView) {


        if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
            pageIndex = 1;

            loadData(pageIndex);

            UmengUtil.onEvent(getActivity(),new  UmengUtil().INCOME_REFRESH,null);

        } else if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
            UmengUtil.onEvent(getActivity(),new UmengUtil().INCOME_UPLOAD,null);
            // 加载上拉数据
            if (!isLastData) {
                pageIndex++;
                loadData(pageIndex);
            } else {
                mMainHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        listView.onRefreshComplete();
                    }
                }, 1000);
            }

        }


    }
}
