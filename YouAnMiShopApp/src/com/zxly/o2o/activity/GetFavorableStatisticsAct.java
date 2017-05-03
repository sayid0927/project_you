package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.model.TakeStatistics;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.DiscountTakeCountRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

/**
 * Created by dsnx on 2016/6/14.
 */
public class GetFavorableStatisticsAct extends BasicAct implements PullToRefreshBase.OnRefreshListener{

    private PullToRefreshListView mListView;
    private View btnBack;
    private LoadingView loadingView;
    int pageIndex=1;
    private boolean isLastData;
    private GetFavorableStatisticsAdapter adapter;
    private boolean showBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_get_favorable_statistics);
        mListView= (PullToRefreshListView) findViewById(R.id.listview);
        btnBack=findViewById(R.id.btn_back);
        loadingView= (LoadingView) findViewById(R.id.view_loading);
        ViewUtils.setRefreshText(mListView);
        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        adapter=new GetFavorableStatisticsAdapter(this);
        mListView.setAdapter(adapter);
        mListView.setOnRefreshListener(this);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        loadingView.startLoading();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TakeStatistics ts= (TakeStatistics) adapter.getItem((int) id);
                MyBenefitsAct.start(GetFavorableStatisticsAct.this,ts.getId(),false);
            }
        });
        loadData(pageIndex);
    }
    public static void start(Activity curAct)
    {
        Intent it=new Intent();
        it.setClass(curAct,GetFavorableStatisticsAct.class);
        ViewUtils.startActivity(it,curAct);
    }
    private void loadData(final int pageIndex)
    {
        final DiscountTakeCountRequest  discountTakeCountRequest=new DiscountTakeCountRequest(pageIndex);
        discountTakeCountRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                mListView.onRefreshComplete();
                loadingView.onLoadingComplete();
                boolean isEmpty =discountTakeCountRequest.getTakeStatisticsList().isEmpty();
                if (isEmpty) {
                    if (pageIndex > 1) {
                        isLastData = true;
                        ViewUtils.showToast("亲! 没有更多了");
                    }else
                    {
                        loadingView.onDataEmpty("还没人领取优惠券哦,赶紧推广吧!",true,R.drawable.img_default_tired);
                        loadingView.setBtnText("去推广");
                        showBtn =true;
                    }
                } else {
                    if (pageIndex == 1) {
                        adapter.clear();
                    }
                    adapter.addItem(discountTakeCountRequest.getTakeStatisticsList(),true);
                }
                if(discountTakeCountRequest.hasNextPage){
                    mListView.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
            }

            @Override
            public void onFail(int code) {
                mListView.onRefreshComplete();
                loadingView.onLoadingComplete();
                loadingView.onLoadingFail();
            }
        });
        discountTakeCountRequest.start(this);
        loadingView.setOnAgainListener(new LoadingView.OnAgainListener() {
            @Override
            public void onLoading() {
                if(showBtn){
                    PromotionArticleAct.start(GetFavorableStatisticsAct.this,1);
                }else {
                    loadingView.startLoading();
                    discountTakeCountRequest.start(this);
                }
            }
        });
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
            pageIndex = 1;
            loadData(pageIndex);

        } else if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
            // 加载上拉数据
            if (!isLastData) {
                pageIndex++;
                loadData(pageIndex);
            } else {
                mMainHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mListView.onRefreshComplete();
                    }
                }, 1000);
            }

        }
    }
    class GetFavorableStatisticsAdapter extends ObjectAdapter{

        public GetFavorableStatisticsAdapter(Context _context) {
            super(_context);
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_get_favorable_staticstics;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null)
            {
                holder=new ViewHolder();
                convertView=inflateConvertView();
                holder.txtName= (TextView) convertView.findViewById(R.id.txt_name);
                holder.txtType= (TextView) convertView.findViewById(R.id.txt_type);
                holder.txtSyrs= (TextView)convertView.findViewById(R.id.txt_syrs);
                holder.txtLqrs= (TextView) convertView.findViewById(R.id.txt_lqrs);
                convertView.setTag(holder);
            }else
            {
                holder= (ViewHolder) convertView.getTag();
            }
            TakeStatistics takeStatistics= (TakeStatistics) getItem(position);
            if(takeStatistics.getDiscountType()==1)
            {
                convertView.setBackgroundResource(R.drawable.wd_xianjin_bg);
            }else {
                convertView.setBackgroundResource(R.drawable.wd_chongdianbao_bg);
            }
            ViewUtils.setText(holder.txtName,takeStatistics.getDiscountInfo());
            if(takeStatistics.getDiscountType()==1)//现金
            {
                holder.txtType.setTextColor(Color.parseColor("#06766A"));
                ViewUtils.setText(holder.txtType,"现金抵扣");

            }else{// 礼品赠送
                holder.txtType.setTextColor(Color.parseColor("#9A1355"));
                ViewUtils.setText(holder.txtType,"礼品赠送");
            }
            ViewUtils.setText(holder.txtLqrs,"领取人数："+takeStatistics.getTakePersons());
            ViewUtils.setText(holder.txtSyrs,"使用人数："+takeStatistics.getUsePersons());
            return convertView;
        }
        class ViewHolder{
            TextView txtName,txtType,txtLqrs,txtSyrs;
        }
    }
}
