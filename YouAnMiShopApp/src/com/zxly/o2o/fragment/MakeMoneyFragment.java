package com.zxly.o2o.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.zxing.CaptureActivity;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.FragmentListAct;
import com.zxly.o2o.activity.MainActivity;
import com.zxly.o2o.activity.MakeCommissionAct;
import com.zxly.o2o.activity.MobileDataAct;
import com.zxly.o2o.activity.SystemMsgAct;
import com.zxly.o2o.activity.YieldDetailAct;
import com.zxly.o2o.adapter.DataSetAdapter;
import com.zxly.o2o.adapter.MakeMoneyAdapter;
import com.zxly.o2o.application.Config;
import com.zxly.o2o.model.GiftGetInfo;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.DisCountSystemMsgDeTailRequest;
import com.zxly.o2o.request.MakeMoneyInitRequest;
import com.zxly.o2o.request.MakeMoneyListRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;
import com.zxly.o2o.view.VerticalRollingTextView;

/**
 * Created by dsnx on 2015/12/14.
 */
public class MakeMoneyFragment extends BaseFragment implements View.OnClickListener, PullToRefreshBase.OnRefreshListener {

    private PullToRefreshListView mListView;
    private MakeMoneyAdapter makeMoneyAdapter;
    private View btnSymx, btnZyj, btnScanErweima, btnMsg;
    private TextView txtYbglNumber;
    private LoadingView loadingview;
    private TextView txtTotalYield, txtCurYield, txtTitle;
    private int pageIndex = 1;
    private boolean isLastData;
    private int refreshCount;
    private VerticalRollingTextView mVerticalRollingView;
    private LoadingView footerView;
    private  ViewGroup discoverHead;

    @Override
    protected void initView() {
        mListView = (PullToRefreshListView) findViewById(R.id.zqgl_listview);
        makeMoneyAdapter = new MakeMoneyAdapter(this.getActivity());
        if(Config.screenWidth < 720){
            discoverHead = (ViewGroup) LayoutInflater.from(this.getActivity()).inflate(R.layout.head_make_money_720, null);
        } else {
            discoverHead = (ViewGroup) LayoutInflater.from(this.getActivity()).inflate(R.layout.head_make_money, null);
        }

        btnSymx = discoverHead.findViewById(R.id.btn_symx);
        btnScanErweima = findViewById(R.id.btn_scanErweima);
        txtTotalYield = (TextView) discoverHead.findViewById(R.id.txt_total_yield);
        txtCurYield = (TextView) discoverHead.findViewById(R.id.txt_cur_yield);
        txtTitle = (TextView) findViewById(R.id.txt_title);
        btnMsg = discoverHead.findViewById(R.id.btn_msg);
        mVerticalRollingView = (VerticalRollingTextView) discoverHead.findViewById(R.id.txt_sysMsg);

        mListView.setAdapter(makeMoneyAdapter);
        loadingview = (LoadingView) findViewById(R.id.view_loading);
        ViewUtils.setRefreshText(mListView);
        mListView.setDivideHeight(0);
        mListView.addH(discoverHead);
        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mListView.setOnRefreshListener(this);
        mListView.setIntercept(true);
//        if (Account.user != null) {
//            ViewUtils.setText(txtTitle, Account.user.getShopName());
//        }
        //11月运维2  新需求：为保持与tab文字一致
        ViewUtils.setText(txtTitle, "赚钱");
        btnZyj = discoverHead.findViewById(R.id.btn_zyj);
        btnSymx.setOnClickListener(this);
        btnZyj.setOnClickListener(this);
        btnMsg.setOnClickListener(MakeMoneyFragment.this);
        discoverHead.findViewById(R.id.btn_llcz).setOnClickListener(this);
        discoverHead.findViewById(R.id.btn_ybgl).setOnClickListener(this);
        txtYbglNumber = (TextView) discoverHead.findViewById(R.id.txt_yanbao_number);
        btnScanErweima.setOnClickListener(this);


    }

    public void onResume() {
        super.onResume();
        if (Account.user != null && 2 == ((MainActivity) getActivity()).fragmentController.getCurrentTab()) {
            initData();
        }
    }

    protected void initData() {
        refreshCount++;
        final MakeMoneyInitRequest moneyInitRequest = new MakeMoneyInitRequest();
        moneyInitRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                loadingview.onLoadingComplete();
                ViewUtils.setTextPrice(txtTotalYield, moneyInitRequest.getAllRevenue());
                txtCurYield.setText("本月收益：" + "￥" + StringUtil.getFormatPrice(moneyInitRequest.getCurrentMonthRevenue()));

                if(moneyInitRequest.getInsuranceOrderCount()>0){
                    ((MainActivity) getActivity()).setRedPointVisible(true);
                    ViewUtils.setVisible(txtYbglNumber);
                    String count=moneyInitRequest.getInsuranceOrderCount()>99 ? "99+" : (moneyInitRequest.getInsuranceOrderCount()+"");
                    ViewUtils.setText(txtYbglNumber,count);
                }else {
                    ((MainActivity) getActivity()).setRedPointVisible(false);
                    ViewUtils.setGone(txtYbglNumber);
                }

                if (moneyInitRequest.getArticleList().isEmpty()) {
                    LoadState("未发布攻略!");
                } else {
                    makeMoneyAdapter.clear();
                    makeMoneyAdapter.addItem(moneyInitRequest.getArticleList(),true);
                }

            }

            @Override
            public void onFail(int code) {
                loadingview.onLoadingComplete();
                if (refreshCount == 1) {
                    LoadState("加载失败!");
                    ViewUtils.showToast("刷新失败");
                }

            }
        });
        loadingview.startLoading();
        moneyInitRequest.start(this);
        final DisCountSystemMsgDeTailRequest disCountSystemMsgDeTailRequest = new DisCountSystemMsgDeTailRequest(1, 5);
        disCountSystemMsgDeTailRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                if(disCountSystemMsgDeTailRequest.getIsDiscount()==0)
                {
                    ViewUtils.setVisible(btnMsg);
                    ViewUtils.setVisible(discoverHead,R.id.line_msg_buttom);
                    if(!disCountSystemMsgDeTailRequest.getGiftGetInfoList().isEmpty())
                    {

                        mVerticalRollingView.setDataSetAdapter(new DataSetAdapter(disCountSystemMsgDeTailRequest.getGiftGetInfoList()) {
                            @Override
                            protected String text(Object o) {
                                GiftGetInfo giftGetInfo= (GiftGetInfo) o;
                                StringBuffer buffer=new StringBuffer(giftGetInfo.getUserName());
                                buffer.append("领取了");
                                if(giftGetInfo.getDiscountType()==1)
                                {
                                    buffer.append("现金折扣");
                                    buffer.append("#");
                                    buffer.append(giftGetInfo.getDiscountInfo()).append("元");
                                }else
                                {
                                    buffer.append("礼品赠送");
                                    buffer.append("#");
                                    buffer.append(giftGetInfo.getDiscountInfo());
                                }
                                buffer.append("！");
                                return buffer.toString();
                            }

                        });
                        mVerticalRollingView.run();
                    }
                }


            }

            @Override
            public void onFail(int code) {

            }
        });
        disCountSystemMsgDeTailRequest.start(this);
    }

    private void loadData(final int _pageIndex) {
        final MakeMoneyListRequest request = new MakeMoneyListRequest(_pageIndex);
        request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                boolean isEmpty = request.getArticleList().isEmpty();
                mListView.onRefreshComplete();
                if (isEmpty) {
                    if (_pageIndex > 1) {
                        ViewUtils.showToast("亲! 没有更多了");
                        pageIndex--;
                    }
                } else {
                    if (_pageIndex == 1) {
                        makeMoneyAdapter.clear();
                    }
                    makeMoneyAdapter.addItem(request.getArticleList());
                    makeMoneyAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFail(int code) {
                ViewUtils.showToast("加载失败!");
            }
        });
        request.start(this);
    }

    private void LoadState(String msg) {
        if (footerView == null) {
            footerView = new LoadingView(this.getActivity());
            mListView.addF(footerView);

        }
        footerView.onDataEmpty(msg);

    }

    @Override
    protected int layoutId() {
        return R.layout.win_make_money;
    }

    @Override
    public void onClick(View v) {
        if (v == btnSymx) {
            YieldDetailAct.start(this.getActivity());
        } else if (v.getId() == R.id.btn_llcz) {
            Intent intent = new Intent(getActivity(), MobileDataAct.class);
            startActivity(intent);
        } else if (v == btnZyj) {
            ViewUtils.startActivity(new Intent(getActivity(), MakeCommissionAct.class), getActivity());
        } else if (v == btnScanErweima) {
            ViewUtils.startActivity(new Intent(getActivity(), CaptureActivity.class), getActivity());
        } else if (v == btnMsg) {
            SystemMsgAct.start(this.getActivity());
        } else if (v.getId() == R.id.btn_ybgl) {
            FragmentListAct.start("延保管理", FragmentListAct.PAGE_GUARANTEE_MANAGE);
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        {
            if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
                pageIndex = 1;

                loadData(pageIndex);

            } else if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
                // 加载上拉数据
                    pageIndex++;
                    loadData(pageIndex);

            }

        }
    }
}
