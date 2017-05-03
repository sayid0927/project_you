package com.zxly.o2o.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.TradingRecord;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshExpandableListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.PayAccountRecordRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.UmengUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AccountRecordFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener {
    public static final int ACCOUNT_RECORD_ALL = 0;
    public static final int ACCOUNT_RECORD_PRODUCT = 5;
    public static final int ACCOUNT_RECORD_FLOW = 7;
    public static final int ACCOUNT_RECORD_APPLICATION = 10;
    public static final int ACCOUNT_RECORD_TAKEOUT = 2;
    public static final int ACCOUNT_RECORD_INSURANCE = 12;

    private PullToRefreshExpandableListView mListView;
    private LoadingView loadingView;
    private int page = 1;
    private boolean isInitData = false;
    private int status = -1;
    private PayAccountRecordRequest payAccountRecordRequest;
    TradingAdapter tradingAdapter;
    private boolean isLastData;

   
    @Override
    protected void initView() {
        loadingView = (LoadingView) findViewById(R.id.view_loading);
        mListView = (PullToRefreshExpandableListView) findViewById(R.id.list_view);
        mListView =  (PullToRefreshExpandableListView) findViewById(R.id.list_view);
        mListView.getRefreshableView().setGroupIndicator(null);

        ViewUtils.setRefreshListText(mListView);

        mListView.getRefreshableView().setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        mListView.setOnRefreshListener(this);
        mListView.getRefreshableView().setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return true;
            }
        });

        loadingView.setOnAgainListener(new LoadingView.OnAgainListener() {
            @Override
            public void onLoading() {
                loadingView.startLoading();
                loadData(1);
            }
        });

        if (tradingAdapter == null) {
            tradingAdapter = new TradingAdapter(getActivity());
        }
        mListView.getRefreshableView().setAdapter(tradingAdapter);

        if (isInitData) {
            loadData(1);
        }
    }

    public void init(int status) {
        this.status = status;
        isInitData = true;
        if (tradingAdapter != null) {
            page = 1;
            loadData(page);
        }
    }

    @Override
    protected int layoutId() {
        return R.layout.win_pay_accountrecord;
    }

    private void loadData(final int pageIndex) {
        if (!mListView.isRefreshing()) {
            loadingView.startLoading();
        }
        payAccountRecordRequest = new PayAccountRecordRequest(status, pageIndex, 10, Account.user.getShopId());
        payAccountRecordRequest
                .setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

                    @Override
                    public void onOK() {
                        boolean isEmpty = payAccountRecordRequest.getTradingList().isEmpty();
                        mListView.onRefreshComplete();
                        loadingView.onLoadingComplete();
                        if (isEmpty) {
                            if (pageIndex == 1) {
                                loadingView.onDataEmpty("您还没有账单呢!",R.drawable.img_default_tired);
                            } else {
                                isLastData = true;
                                ViewUtils.showToast("亲! 没有更多了");
                            }
                        } else {
                            if (pageIndex == 1) {
                                tradingAdapter.clear();
                            }
                            tradingAdapter.addContent(payAccountRecordRequest.getTradingList());
                            tradingAdapter.notifyDataSetChanged();
                            for (int i = 0; i < tradingAdapter.getGroupCount(); i++) {
                                mListView.getRefreshableView().expandGroup(i);
                            }
                            page++;
                        }

                        if(payAccountRecordRequest.hasNextPage){
                            mListView.setMode(PullToRefreshBase.Mode.BOTH);
                        } else {
                            mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        }
                    }

                    @Override
                    public void onFail(int code) {
                        mListView.onRefreshComplete();
                        loadingView.onLoadingFail();
                    }
                });
        payAccountRecordRequest.start(getActivity());
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
            page = 1;
            loadData(page);
            UmengUtil.onEvent(getActivity(),new UmengUtil().BILL_REFRESH,null);
        }
        if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
            loadData(page);
            UmengUtil.onEvent(getActivity(),new UmengUtil().BILL_UPLOAD,null);
        }
    }

    class TradingAdapter extends BaseExpandableListAdapter {
        protected Map<Object, List<Object>> content = new LinkedHashMap<Object, List<Object>>();
        protected Context context;

        public TradingAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getGroupCount() {

            return content.keySet().size();
        }

        public void clear() {
            content.clear();
        }

        public void addContent(Map<Object, List<Object>> _content) {
            if (content.isEmpty()) {
                content.putAll(_content);
            } else {
                Iterator iter = _content.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    Object key = entry.getKey();
                    List<Object> srcList = (List<Object>) entry.getValue();
                    if (content.containsKey(key)) {
                        List<Object> list = content.get(key);
                        list.addAll(srcList);
                    } else {
                        content.put(key, srcList);
                    }
                }
            }

        }

        public void addContent(Object key, Object value) {
            if (!content.containsKey(key)) {
                List<Object> list = new ArrayList<Object>();
                list.add(value);
                content.put(key, list);
            } else {
                content.get(key).add(value);
            }
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return content.get(content.keySet().toArray()[groupPosition]).size();

        }

        @Override
        public Object getGroup(int groupPosition) {
            return content.keySet().toArray()[groupPosition];
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return content.get(content.keySet().toArray()[groupPosition]).get(
                    childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflateConvertView(R.layout.item_ed_group);
            }
            TextView txtItem = (TextView) convertView.findViewById(R.id.txt_Time);
            txtItem.setText((CharSequence) getGroup(groupPosition));
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflateConvertView(R.layout.item_account_record);
                holder = new ViewHolder();
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
            TradingRecord tradingRecord = (TradingRecord) getChild(groupPosition, childPosition);
            float money = tradingRecord.getMoney();
            int type = tradingRecord.getType();
            int source = tradingRecord.getSource();
            String incomeType = "";
            if (tradingRecord.getIncomeType() == 1) {
                incomeType = "+";
                holder.txtMoney.setTextColor(context.getResources().getColor(R.color.orange_ff5f19));
            } else if (tradingRecord.getIncomeType() == 2) {
                incomeType = "-";
                holder.txtMoney.setTextColor(context.getResources().getColor(R.color.gray_333333));
            }
            ViewUtils.setText(holder.txtMoney, incomeType + StringUtil.getFormatPrice(money));
            if (type == 5) {
                ViewUtils.setText(holder.txtType, "商品");
                holder.txtType.setBackgroundResource(R.drawable.bg_bill_type_orange);
                ViewUtils.setText(holder.txtDescHead, "订单号：");
                ViewUtils.setText(holder.txtDesc, tradingRecord.getOrderNo());
                ViewUtils.setText(holder.txtBankName, tradingRecord.getPayName());
            } else if (type == 8) {
                ViewUtils.setText(holder.txtType, "验证");
                holder.txtType.setBackgroundResource(R.drawable.bg_bill_type_orange);
                ViewUtils.setText(holder.txtDescHead, "流水号：");
                ViewUtils.setText(holder.txtDesc, tradingRecord.getNumberNo());
                ViewUtils.setText(holder.txtBankName, tradingRecord.getPayName());
            } else if (type == 2) {
                if (source == 12) {
                    ViewUtils.setText(holder.txtType, "退款");
                } else {
                    ViewUtils.setText(holder.txtType, "提现");
                }
                holder.txtType.setBackgroundResource(R.drawable.bg_bill_type_sky);
                ViewUtils.setText(holder.txtDescHead, "流水号：");
                ViewUtils.setText(holder.txtDesc, tradingRecord.getNumberNo());
                ViewUtils.setText(holder.txtBankName, tradingRecord.getBankName() + "(尾号" + tradingRecord.getAcceptBankNumber() + ")");
            } else if (type == 7) {
                ViewUtils.setText(holder.txtType, "流量");
                holder.txtType.setBackgroundResource(R.drawable.bg_bill_type_yellow);
                ViewUtils.setText(holder.txtDescHead, "流水号：");
                ViewUtils.setText(holder.txtDesc, tradingRecord.getNumberNo());
                ViewUtils.setText(holder.txtBankName, tradingRecord.getPayName());
            } else if (type == 10) {
                ViewUtils.setText(holder.txtType, "应用");
                holder.txtType.setBackgroundResource(R.drawable.bg_bill_type_blue);
                ViewUtils.setText(holder.txtDescHead, "订单号：");
                ViewUtils.setText(holder.txtDesc, tradingRecord.getOrderNo());
                ViewUtils.setText(holder.txtBankName, tradingRecord.getPayName());
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
                ViewUtils.setText(holder.txtBankName, tradingRecord.getPayName());
            } else if (type == 12) {
                ViewUtils.setText(holder.txtType, "延保");
                holder.txtType.setBackgroundResource(R.drawable.bg_bill_type_orange);
                ViewUtils.setText(holder.txtDescHead, "订单号：");
                ViewUtils.setText(holder.txtDesc, tradingRecord.getOrderNo());
                ViewUtils.setText(holder.txtBankName, tradingRecord.getPayName());
            }
            ViewUtils.setText(holder.txtPayTime, StringUtil.getDateByMillis(tradingRecord.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            return convertView;
        }

        public View inflateConvertView(int layoutId) {
            return LayoutInflater.from(context).inflate(layoutId, null);
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        class ViewHolder {
            TextView txtType;
            TextView txtDesc;
            TextView txtDescHead;
            TextView txtMoney;
            TextView txtPayTime;
            TextView txtBankName;

        }
    }

}