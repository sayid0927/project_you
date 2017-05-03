package com.zxly.o2o.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.zxly.o2o.adapter.CommissionListAdapter;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.CommissionInitRequest;
import com.zxly.o2o.request.CommissionListRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

/**
 * Created by kenwu on 2015/12/7.
 */
public class CommissionRecordAct extends BasicAct implements View.OnClickListener {

        TextView txtAllCommission,txtWillArrive;
        PullToRefreshListView mListView;
        ObjectAdapter adapter;
        View contentView;
        LoadingView loadingView;
        CommissionInitRequest initRequest;

        private int pageIndex=1;
    private boolean isEmpty;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.win_commission_record);


            View btnBack=findViewById(R.id.btn_back);
            btnBack.setOnClickListener(this);

            contentView=findViewById(R.id.layout_content);
            contentView.setVisibility(View.GONE);

            txtAllCommission= (TextView) findViewById(R.id.txt_allCommission);
            txtWillArrive= (TextView) findViewById(R.id.txt_willArrive);

            mListView= (PullToRefreshListView) findViewById(R.id.listview);
            mListView.setIntercept(true);
            adapter=new CommissionListAdapter(this);
            mListView.setAdapter(adapter);
            ViewUtils.setRefreshText(mListView);
            mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
            mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
                @Override
                public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//                    if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
//                        // 加载下啦数据
//                        loadData(1);
//                    }
                    if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
                        // 加载上拉数据
                        loadData(pageIndex);
                    }
                }
            });

            loadingView= (LoadingView) findViewById(R.id.view_loading);
            loadingView.setOnAgainListener(new LoadingView.OnAgainListener() {
                @Override
                public void onLoading() {
                    loadingView.startLoading();
                    initRequest.start(CommissionRecordAct.this);
                }
            });


            initRequest=new CommissionInitRequest();

            initRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                @Override
                public void onOK() {
                    contentView.setVisibility(View.VISIBLE);
                    ViewUtils.setTextPrice(txtAllCommission, initRequest.getTotalCommission());
                    ViewUtils.setTextPrice(txtWillArrive, initRequest.getWillArrive());
                    if (DataUtil.listIsNull(initRequest.getOrderComms())) {
                        isEmpty=true;
                        loadingView.onDataEmpty("一条记录都没有,要加油哦!",true,R.drawable.img_default_happy);
                        loadingView.setBtnText("去赚佣金");
                    } else {
                        pageIndex = 2;
                        loadingView.onLoadingComplete();
                        adapter.addItem(initRequest.getOrderComms(),true);
                    }
                    if(initRequest.hasNextPage){
                        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                    } else {
                        mListView.setMode(PullToRefreshBase.Mode.DISABLED);
                    }

                }

                @Override
                public void onFail(int code) {
                    contentView.setVisibility(View.VISIBLE);
                    loadingView.onLoadingFail();
                }
            });

            initRequest.start(this);
            loadingView.startLoading();
        loadingView.setOnAgainListener(new LoadingView.OnAgainListener() {
            @Override
            public void onLoading() {
                if(isEmpty){
                    finish();
                }else {
                    loadingView.startLoading();
                    initRequest.start(this);
                }
            }
        });
        }




        public void loadData(final int pageId) {
//            if(DataUtil.listIsNull(adapter.getContent()))
//                loadingView.startLoading();

            final CommissionListRequest request = new CommissionListRequest(pageId);
            request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                @Override
                public void onOK() {
                    if (!DataUtil.listIsNull(request.getOrderComms())) {
                        if(pageId==1)
                            adapter.clear();

                        adapter.addItem(request.getOrderComms(), true);
                        pageIndex++;
                        loadingView.onLoadingComplete();
                    } else {
                        //下拉刷新的时候发现数据为空，清空list
                        if(pageId==1){
                            isEmpty =true;
                            adapter.clear();
                            adapter.notifyDataSetChanged();
                            loadingView.onDataEmpty("一条记录都没有,要加油哦!",true,R.drawable.img_default_happy);
                            loadingView.setBtnText("去赚佣金");
                        }else{
                            //最后一页
                        }
                    }

                    if (mListView.isRefreshing())
                        mListView.onRefreshComplete();
                }

                @Override
                public void onFail(int code) {
                    if(DataUtil.listIsNull(adapter.getContent()))
                        loadingView.onLoadingFail();

                    if (mListView.isRefreshing())
                        mListView.onRefreshComplete();
                }

            });

            request.start(this);

            loadingView.setOnAgainListener(new LoadingView.OnAgainListener() {
                @Override
                public void onLoading() {
                    if(isEmpty){
                        finish();
                    }else {
                        loadingView.startLoading();
                        request.start(this);
                    }
                }
            });
        }



        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.btn_back:
                    finish();
                    break;

                case R.id.btn_commissionRecord:

                    break;
            }

        }


}
