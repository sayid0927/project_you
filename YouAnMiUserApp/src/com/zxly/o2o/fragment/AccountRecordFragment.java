package com.zxly.o2o.fragment;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.model.TradingRecord;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.PayAccountRecordRequest;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.DesityUtil;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

public class AccountRecordFragment extends BaseFragment {
    public static final int ACCOUNT_RECORD_ALL = 0;
    public static final int ACCOUNT_RECORD_SHOPPING = 5;
    public static final int ACCOUNT_RECORD_COMMISSION = 1;
    public static final int ACCOUNT_RECORD_FLOW = 7;
    public static final int ACCOUNT_RECORD_TAKEOUT = 2;
    public static final int ACCOUNT_RECORD_REFUND = 4;
    public static final int ACCOUNT_RECORD_INSURANCE = 12;

    private PullToRefreshListView mListView;
    private ObjectAdapter adapter;
    private LoadingView loadingView;
    private int page = 1;
    private boolean isInitData = false;
    private int status = -1;
    private PayAccountRecordRequest payAccountRecordRequest;

    @Override
    protected void initView() {
        loadingView = (LoadingView) findViewById(R.id.view_loading);
        mListView = (PullToRefreshListView) findViewById(R.id.list_view);
        mListView.setDivideHeight(DesityUtil.dp2px(getActivity(), 10));
        mListView.setIntercept(true);
        ViewUtils.setRefreshText(mListView);

        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {

                if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
                    page = 1;
                    loadData(page);
                }
                if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
                    loadData(page);
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
            adapter = new TradingRecordAdapter(getActivity());
        }

        mListView.setAdapter(adapter);

        if (isInitData)
            loadData(1);
    }

    public void init(int status) {
        this.status = status;
        isInitData = true;
        if (adapter != null) {
            adapter.clear();
            loadData(1);
        }
    }

    @Override
    protected int layoutId() {
        return R.layout.win_pay_accountrecord;
    }

    private void loadData(final int pageIndex) {
        if (DataUtil.listIsNull(adapter.getContent()))
            loadingView.startLoading();

        payAccountRecordRequest = new PayAccountRecordRequest(status, pageIndex, 10);
        payAccountRecordRequest
                .setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

                    @Override
                    public void onOK() {
                        if (!DataUtil.listIsNull(payAccountRecordRequest.getTradingRecordList())) {
                            if (pageIndex == 1)
                                adapter.clear();

                            adapter.addItem(payAccountRecordRequest.getTradingRecordList(), true);
                            page++;
                            loadingView.onLoadingComplete();
                        } else {
                            //下拉刷新的时候发现数据为空，清空list
                            if (pageIndex == 1) {
                                adapter.clear();
                                adapter.notifyDataSetChanged();
                                loadingView.onDataEmpty();
                            } else {
                                //最后一页
                            }
                        }

                        if (mListView.isRefreshing())
                            mListView.onRefreshComplete();

                    }

                    @Override
                    public void onFail(int code) {
                        if (DataUtil.listIsNull(adapter.getContent()))
                            loadingView.onLoadingFail();

                        if (mListView.isRefreshing())
                            mListView.onRefreshComplete();

                    }
                });
        payAccountRecordRequest.start(getActivity());
    }

    class TradingRecordAdapter extends ObjectAdapter {

        public TradingRecordAdapter(Context _context) {
            super(_context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflateConvertView();
                holder = new ViewHolder();
                holder.viewHead = convertView
                        .findViewById(R.id.view_header);
                holder.txtType = (TextView) convertView
                        .findViewById(R.id.txt_type);
                holder.txtDesc = (TextView) convertView
                        .findViewById(R.id.txt_desc);
                holder.txtDescHead = (TextView) convertView
                        .findViewById(R.id.txt_desc_head);
                holder.txtMoney = (TextView) convertView
                        .findViewById(R.id.txt_money);
                holder.txtPayTime = (TextView) convertView
                        .findViewById(R.id.txt_pay_time);
                holder.txtBankName = (TextView) convertView
                        .findViewById(R.id.txt_bank);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            TradingRecord tradingRecord = (TradingRecord) getItem(position);
            float money = tradingRecord.getMoney();
            int source = tradingRecord.getSource();
            int type = tradingRecord.getType();
            if (source == 1 || source == 2 || source == 16) {
                ViewUtils.setText(holder.txtType, "佣金");
                holder.txtType.setBackgroundResource(R.drawable.bg_bill_type_orange);
                ViewUtils.setText(holder.txtDescHead, "流水号：");
                ViewUtils.setText(holder.txtDesc, tradingRecord.getNumberNo());
                holder.txtMoney.setTextColor(context.getResources().getColor(R.color.green_53ac45));
                ViewUtils.setText(holder.txtMoney, "+" + StringUtil.getFormatPrice(money));
                ViewUtils.setText(holder.txtBankName, tradingRecord.getPayName());
            } else {
                if (type == 5) {
                    ViewUtils.setText(holder.txtType, "购物");
                    holder.txtType.setBackgroundResource(R.drawable.bg_bill_type_orange);
                    ViewUtils.setText(holder.txtDescHead, "订单号：");
                    ViewUtils.setText(holder.txtDesc, tradingRecord.getOrderNo());
                    holder.txtMoney.setTextColor(context.getResources().getColor(R.color.gray_333333));
                    ViewUtils.setText(holder.txtMoney, "-" + StringUtil.getFormatPrice(money));
                    ViewUtils.setText(holder.txtBankName, tradingRecord.getPayName());
                } else if (type == 7) {
                    ViewUtils.setText(holder.txtType, "流量");
                    holder.txtType.setBackgroundResource(R.drawable.bg_bill_type_yellow);
                    ViewUtils.setText(holder.txtDescHead, "手机号：");
                    ViewUtils.setText(holder.txtDesc, tradingRecord.getMobile());
                    holder.txtMoney.setTextColor(context.getResources().getColor(R.color.gray_333333));
                    ViewUtils.setText(holder.txtMoney, "-" + StringUtil.getFormatPrice(money));
                    ViewUtils.setText(holder.txtBankName, tradingRecord.getPayName());
                } else if (type == 2) {
                    if (source == 12) {
                        ViewUtils.setText(holder.txtType, "退款");
                        ViewUtils.setText(holder.txtMoney, "+" + StringUtil.getFormatPrice(money));
                        holder.txtMoney.setTextColor(context.getResources().getColor(R.color.green_53ac45));
                    } else {
                        ViewUtils.setText(holder.txtType, "提现");
                        ViewUtils.setText(holder.txtMoney, "-" + StringUtil.getFormatPrice(money));
                        holder.txtMoney.setTextColor(context.getResources().getColor(R.color.gray_333333));
                    }
                    holder.txtType.setBackgroundResource(R.drawable.bg_bill_type_sky);
                    ViewUtils.setText(holder.txtDescHead, "流水号：");
                    ViewUtils.setText(holder.txtDesc, tradingRecord.getNumberNo());
                    ViewUtils.setText(holder.txtBankName, tradingRecord.getBankName() + "(尾号" + tradingRecord.getAcceptBankNumber() + ")");
                } else if (type == 4) {
                    if (source == 17) {
                        ViewUtils.setText(holder.txtType, "延保");
                        holder.txtType.setBackgroundResource(R.drawable.bg_bill_type_orange);
                    } else {
                        ViewUtils.setText(holder.txtType, "退款");
                        holder.txtType.setBackgroundResource(R.drawable.bg_bill_type_blue);
                    }
                    ViewUtils.setText(holder.txtDescHead, "订单号：");
                    ViewUtils.setText(holder.txtDesc, tradingRecord.getOrderNo());
                    holder.txtMoney.setTextColor(context.getResources().getColor(R.color.green_53ac45));
                    ViewUtils.setText(holder.txtMoney, "+" + StringUtil.getFormatPrice(money));
                    ViewUtils.setText(holder.txtBankName, tradingRecord.getPayName());
                } else if (type == 8) {
                    ViewUtils.setText(holder.txtType, "验证");
                    holder.txtType.setBackgroundResource(R.drawable.bg_bill_type_orange);
                    ViewUtils.setText(holder.txtDescHead, "流水号：");
                    ViewUtils.setText(holder.txtDesc, tradingRecord.getNumberNo());
                    holder.txtMoney.setTextColor(context.getResources().getColor(R.color.gray_333333));
                    ViewUtils.setText(holder.txtMoney, "-" + StringUtil.getFormatPrice(money));
                    ViewUtils.setText(holder.txtBankName, tradingRecord.getPayName());
                } else if (type == 12) {
                    ViewUtils.setText(holder.txtType, "延保");
                    holder.txtType.setBackgroundResource(R.drawable.bg_bill_type_orange);
                    ViewUtils.setText(holder.txtDescHead, "订单号：");
                    ViewUtils.setText(holder.txtDesc, tradingRecord.getOrderNo());
                    holder.txtMoney.setTextColor(context.getResources().getColor(R.color.gray_333333));
                    ViewUtils.setText(holder.txtMoney, "-" + StringUtil.getFormatPrice(money));
                    ViewUtils.setText(holder.txtBankName, tradingRecord.getPayName());
                }
            }
            ViewUtils.setText(holder.txtPayTime, StringUtil.getDateByMillis(tradingRecord.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            return convertView;
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_account_record;
        }

        class ViewHolder {
            View viewHead;
            TextView txtType;
            TextView txtDesc;
            TextView txtDescHead;
            TextView txtMoney;
            TextView txtPayTime;
            TextView txtBankName;
        }
    }

}
