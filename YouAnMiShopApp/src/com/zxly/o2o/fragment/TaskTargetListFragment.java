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
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.zxly.o2o.activity.SalesmanRankingAct;
import com.zxly.o2o.adapter.DataSetAdapter;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.adapter.TaskTargetAdapter;
import com.zxly.o2o.model.PromotionChampion;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.Mode;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.BaseRequest.ResponseStateListener;
import com.zxly.o2o.request.PromotionChampionRequest;
import com.zxly.o2o.request.TaskTargetRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.TimeUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;
import com.zxly.o2o.view.VerticalRollingTextView;

import java.util.List;
import java.util.Map;

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
public class TaskTargetListFragment extends DateListFragment implements ResponseStateListener {

    private PullToRefreshListView mListView;
    private ObjectAdapter adapter;
    private LoadingView loadingView;
    TaskTargetRequest request;
    TextView txtTaskDate;
    TextView txtPromotionTips;
    int year;
    int month;
    private VerticalRollingTextView mVerticalRollingView;
    private View viewGuide;
    private boolean firstOpen = true;

    public static TaskTargetListFragment newInstance() {
        TaskTargetListFragment f = new TaskTargetListFragment();
        Bundle args = new Bundle();
        args.putInt("type", 1);
        f.setArguments(args);
        return f;
    }


    @Override
    protected void initView(Bundle bundle) {
        super.initView(bundle);
        loadingView = (LoadingView) findViewById(R.id.view_loading);
        ViewUtils.setText(findViewById(R.id.txt_title), "任务指标");
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        viewGuide = findViewById(R.id.view_task_guide);
        viewGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstOpen = false;
                ((TaskTargetAdapter)adapter).setGuideShow(false);
                adapter.notifyDataSetChanged();
                ViewUtils.setGone(viewGuide);
            }
        });
        findViewById(R.id.btn_promotion_notice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SalesmanRankingAct.start(getActivity());
            }
        });

        mVerticalRollingView = (VerticalRollingTextView) findViewById(R.id.txt_promotion_notice);
        mListView = (PullToRefreshListView) findViewById(R.id.list_view);
        ViewUtils.setRefreshBaseText(mListView);
        mListView.setMode(Mode.PULL_FROM_START);
        // mListView.setIntercept(true);
        txtTaskDate = (TextView) findViewById(R.id.txt_taskDate);
        year = monthAdapter.getSelectYear();
        month = monthAdapter.getSelectMonth();
        txtTaskDate.setText("本月指标 ( " + year + "." + month + ".1~" + year + "." + month + "." + TimeUtil.getDayOfMonth(year, month) + " )");
        txtPromotionTips = (TextView) findViewById(R.id.txt_promotion_tips);

        mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {

                if (refreshView.getCurrentMode() == Mode.PULL_FROM_START) {
                    // 加载下啦数据
                    loadData(year, month);
                }

            }
        });

        loadingView.setOnAgainListener(new LoadingView.OnAgainListener() {
            @Override
            public void onLoading() {
                loadingView.startLoading();
                loadData(year, month);
            }
        });

        if (adapter == null) {
            adapter = new TaskTargetAdapter(getActivity(), new CallBack() {
                @Override
                public void onCall() {
                    firstOpen = false;
                    ((TaskTargetAdapter)adapter).setGuideShow(false);
                    adapter.notifyDataSetChanged();
                    ViewUtils.setGone(viewGuide);
                }
            }, getActivity());
        }
        mListView.setAdapter(adapter);
        loadData(year, month);
    }


    @Override
    public void onResume() {
        super.onResume();
//		if(((SalesmanRankingAct)getActivity()).curType==type)
//			updateData();
    }


    public void loadData(int year, int month) {
        if (DataUtil.listIsNull(adapter.getContent()))
            loadingView.startLoading();

        request = new TaskTargetRequest(year, month);
        request.setOnResponseStateListener(this);
        request.start(getActivity());
        final PromotionChampionRequest promotionChampionRequest = new PromotionChampionRequest(year, month);
        promotionChampionRequest.setOnResponseStateListener(new ResponseStateListener() {
            @Override
            public void onOK() {
                List<PromotionChampion> promotionChampionList = promotionChampionRequest.getPromotionChampionList();
                if (!promotionChampionList.isEmpty()) {

                    mVerticalRollingView.setDataSetAdapter(new DataSetAdapter(promotionChampionList) {
                        @Override
                        protected String text(Object o) {
                            PromotionChampion promotionChampion = (PromotionChampion) o;
                            StringBuffer buffer = new StringBuffer();
                            int type = promotionChampion.getType();
                            if (type == 1) {
                                buffer.append("APP推荐下载:");
                            } else if (type == 2) {
                                buffer.append("商品推广:");
                            } else if (type == 3) {
                                buffer.append("商品推广:");
                            } else if (type == 4) {
                                buffer.append("文章推广:");
                            }
                            int rank = promotionChampion.getRanking();
                            if (rank == 1) {
                                buffer.append("恭喜! 您现在排名第");
                                buffer.append("#");
                                buffer.append("1");
                                buffer.append("#");
                                buffer.append("名!");
                            } else {
                                if (rank == 0) {
                                    buffer.append("您还未做推广, 加油! ");
                                } else {
                                    buffer.append("您现在排名第");
                                    buffer.append("#");
                                    buffer.append(promotionChampion.getRanking());
                                    buffer.append("#");
                                    buffer.append("名!");
                                }
                                buffer.append("#");
                                buffer.append(promotionChampion.getNickName());
                                buffer.append("#");
                                buffer.append("推广");
                                buffer.append(promotionChampion.getCount());
                                if (type == 3) {
                                    buffer.append("件,");
                                } else {
                                    buffer.append("次,");
                                }
                                buffer.append("排名第一!");
                            }
                            return buffer.toString();
                        }
                    });
                    mVerticalRollingView.run();
                } else {
                    ViewUtils.setText(mVerticalRollingView, "早起的鸟儿有虫吃！赶紧做第一个完成任务的好同志");
                }
            }

            @Override
            public void onFail(int code) {
            }
        });
        promotionChampionRequest.start(getActivity());
    }


    @Override
    public void onOK() {
        if (!DataUtil.listIsNull(request.getTaskList())) {
            adapter.clear();
            if (!request.isHasTarget()) {
                firstOpen = false;
                ((TaskTargetAdapter) adapter).setGuideShow(false);
                ViewUtils.setGone(viewGuide);
            } else {
                if (request.isHasPromotion()) {
                    firstOpen = false;
                    ((TaskTargetAdapter) adapter).setGuideShow(false);
                    ViewUtils.setGone(viewGuide);
                } else {
                    if (firstOpen) {
                        ((TaskTargetAdapter) adapter).setGuideShow(true);
                        ViewUtils.setVisible(viewGuide);
                    } else {
                        ((TaskTargetAdapter) adapter).setGuideShow(false);
                        ViewUtils.setGone(viewGuide);
                    }
                }
            }
            if(request.isShowTips()){
                ViewUtils.setVisible(txtPromotionTips);
            } else {
                ViewUtils.setGone(txtPromotionTips);
            }
            adapter.addItem(request.getTaskList(), true);
            loadingView.onLoadingComplete();
        } else {
            adapter.clear();
            adapter.notifyDataSetChanged();
            loadingView.onDataEmpty("您还没设置指标,请在后台设置...");
        }

        mListView.onRefreshComplete();
    }


    @Override
    public void onFail(int code) {
        if (DataUtil.listIsNull(adapter.getContent()))
            loadingView.onLoadingFail();

        if (mListView.isRefreshing())
            mListView.onRefreshComplete();
    }


    @Override
    protected int layoutId() {
        return R.layout.layout_task_target_list;
    }

    @Override
    protected void initView() {
        // TODO Auto-generated method stub

    }


    @Override
    public void onCall(Object data) {
        super.onCall(data);
        Map<String, Integer> result = (Map<String, Integer>) data;
        year = result.get("year");
        month = result.get("month");

        txtTaskDate.setText("本月指标 ( " + year + "." + month + ".1~" + year + "." + month + "." + TimeUtil.getDayOfMonth(year, month) + " )");

        loadingView.startLoading();
        loadData(year, month);

    }
}
