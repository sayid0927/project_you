package com.zxly.o2o.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.GetFavorableStatisticsAct;
import com.zxly.o2o.activity.MainActivity;
import com.zxly.o2o.activity.MyOrderAct;
import com.zxly.o2o.activity.PayAccountRecordAct;
import com.zxly.o2o.activity.PayMyAccountAct;
import com.zxly.o2o.activity.PayTakeoutAct;
import com.zxly.o2o.activity.SalesmanRankingAct;
import com.zxly.o2o.activity.ShippingListAct;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.model.ShopStatistic;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.PersonalInitRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.FragmentTabHandler;
import com.zxly.o2o.util.UmengUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;
import com.zxly.o2o.view.MGridView;

import java.util.List;
import java.util.Map;

public class ManageFragment extends DateListFragment implements View.OnClickListener {
    private FragmentTabHandler fragmentTabHandler;
    private MGridView gridView;
    private View btnTakeout;
    private TextView txtTakeout;
    private TextView txtUserBalance;
    private int year, month;
    private OperationAdapter adapter;
    private LoadingView loadingView;
    private PersonalInitRequest personalInitRequest;
    private List<ShopStatistic> operationDataList;

    @Override
    protected void initView() {
        UmengUtil.onEvent(getActivity(), "home_manage_enter",null);
        super.initView(null);
        ViewUtils.setGone(findViewById(R.id.btn_back));
        ViewUtils.setText(findViewById(R.id.txt_title), "管理");
        loadingView = (LoadingView) findViewById(R.id.view_loading);

        gridView = (MGridView) findViewById(R.id.grid_view);
        adapter = new OperationAdapter(getActivity());
        gridView.setAdapter(adapter);

        findViewById(R.id.btn_user_balance).setOnClickListener(this);
        txtUserBalance = (TextView) findViewById(R.id.txt_account_balance);
        btnTakeout = findViewById(R.id.btn_takeout);
        btnTakeout.setOnClickListener(this);
        txtTakeout = (TextView) findViewById(R.id.txt_takeout);
        findViewById(R.id.btn_payment_detail).setOnClickListener(this);
        findViewById(R.id.btn_order_all).setOnClickListener(this);
        findViewById(R.id.btn_order_daifahuo).setOnClickListener(this);
        findViewById(R.id.btn_order_daishouhuo).setOnClickListener(this);
        findViewById(R.id.btn_order_refund).setOnClickListener(this);
        findViewById(R.id.btn_dybd).setOnClickListener(this);
        findViewById(R.id.btn_shqd).setOnClickListener(this);
        findViewById(R.id.btn_yhlqtj).setOnClickListener(this);
        if (Account.user != null) {
            if (Account.user.getIsBoss() == 1) {
                ViewUtils.setVisible(findViewById(R.id.btn_yhlqtj));
            } else {
                ViewUtils.setGone(findViewById(R.id.btn_yhlqtj));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentTabHandler = ((MainActivity) getActivity()).fragmentController;
        if (1 == fragmentTabHandler.getCurrentTab()) {
            refreshUI();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (personalInitRequest != null) {
            personalInitRequest.cancel();
        }
    }

    public void refreshUI() {
        if (Account.user != null) {
            if (Account.user.getIsBoss() == 1) {
                ViewUtils.setVisible(findViewById(R.id.btn_yhlqtj));
            } else {
                ViewUtils.setGone(findViewById(R.id.btn_yhlqtj));
            }

            loadData(year, month);
        }
    }

    private void loadData(long year, long month) {
        personalInitRequest = new PersonalInitRequest(Account.user.getId(), Account.user.getShopId(), year, month);
        personalInitRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                float userBalance = personalInitRequest.getAccountBalance();
                ViewUtils.setTextPrice(txtUserBalance, userBalance);
                Drawable drawable;
                if (userBalance < 10) {
                    ViewUtils.setVisible(findViewById(R.id.view_warning));
                    drawable = getActivity().getResources().getDrawable(R.drawable.icon_takeout_disable);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    txtTakeout.setCompoundDrawables(drawable, null, null, null);
                    txtTakeout.setTextColor(getResources().getColor(R.color.gray_999999));
                    btnTakeout.setEnabled(false);
                } else {
                    ViewUtils.setGone(findViewById(R.id.view_warning));
                    drawable = getActivity().getResources().getDrawable(R.drawable.icon_takeout);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    txtTakeout.setCompoundDrawables(drawable, null, null, null);
                    txtTakeout.setTextColor(getResources().getColor(R.color.orange_ff5f19));
                    btnTakeout.setEnabled(true);
                }
                if (personalInitRequest.isShowOperationData()) {
                    ViewUtils.setVisible(findViewById(R.id.view_operation_data));
                    operationDataList = personalInitRequest.getOperationInfoList();
                    adapter.clear();
                    adapter.addItem(operationDataList, true);
                } else {
                    ViewUtils.setGone(findViewById(R.id.view_operation_data));
                }

                loadingView.onLoadingComplete();
            }

            @Override
            public void onFail(int code) {
                loadingView.onLoadingComplete();
                ViewUtils.showToast("加载失败，稍后重试");
            }
        });
        loadingView.startLoading();
        personalInitRequest.start(this);



    }

    @Override
    protected int layoutId() {
        return R.layout.win_manage_center;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_user_balance:

                PayMyAccountAct.start(getActivity());
                UmengUtil.onEvent(getActivity(),new UmengUtil().MANAGE_BALANCE_CLICK,null);

                break;
            case R.id.btn_takeout:

                PayTakeoutAct.start(getActivity());
                UmengUtil.onEvent(getActivity(),new UmengUtil().MANAGE_WITHDRAW_CLICK,null);

                break;
            case R.id.btn_payment_detail:

                PayAccountRecordAct.start(0, getActivity());
                UmengUtil.onEvent(getActivity(),new UmengUtil().MANAGE_BILL_CLICK,null);

                break;
            case R.id.btn_order_all://全部订单
                MyOrderAct.startMyorderAct(getActivity(), MyOrderAct.ORDER_REQUEST_ALL);
                UmengUtil.onEvent(getActivity(),new UmengUtil().MANAGE_ALLORDER_CLICK,null);
                break;
            case R.id.btn_order_daifahuo://待发货
                MyOrderAct.startMyorderAct(getActivity(), MyOrderAct.ORDER_REQUEST_WAIT_FOR_SEND);
                UmengUtil.onEvent(getActivity(),new UmengUtil().MANAGE_CHECKING_CLICK,null);

                break;
            case R.id.btn_order_daishouhuo://待收货
                MyOrderAct.startMyorderAct(getActivity(), MyOrderAct.ORDER_REQUEST_SENDED);
                UmengUtil.onEvent(getActivity(),new UmengUtil().MANAGE_DELIVERING_CLICK,null);

                break;
            case R.id.btn_order_refund://退款
                MyOrderAct.startMyorderAct(getActivity(), MyOrderAct.ORDER_REQUEST_REFUND);
                UmengUtil.onEvent(getActivity(),new UmengUtil().MANAGE_REFUND_CLICK,null);

                break;
            case R.id.btn_dybd://店员榜单
                SalesmanRankingAct.start(getActivity());
                UmengUtil.onEvent(getActivity(),new UmengUtil().MANAGE_SALESRANKING_CLICK,null);

                break;
            case R.id.btn_shqd://送货清单
                ShippingListAct.start(getActivity());
                break;
            case R.id.btn_yhlqtj:
                GetFavorableStatisticsAct.start(getActivity());
                UmengUtil.onEvent(getActivity(),new UmengUtil().MANAGE_DISCOUNTRECEIVE_CLICK,null);

                break;
        }
    }

    @Override
    public void onCall(Object data) {
        super.onCall(data);
        Map<String, Integer> result = (Map<String, Integer>) data;
        year = result.get("year");
        month = result.get("month");
        loadData(year, month);
    }

    class OperationAdapter extends ObjectAdapter {
        private Context context;

        public OperationAdapter(Context _context) {
            super(_context);
            this.context = _context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ShopStatistic statistic = (ShopStatistic) getItem(position);
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflateConvertView();
                holder = new ViewHolder();
                holder.txtName = (TextView) convertView.findViewById(R.id.txt_type_name);
                holder.txtCount = (TextView) convertView.findViewById(R.id.txt_count);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ViewUtils.setText(holder.txtName, statistic.getName());
            if (position > 3) {
                ViewUtils.setTextPrice(holder.txtCount, statistic.getCount());
            } else {
                ViewUtils.setText(holder.txtCount, (int) statistic.getCount());
            }
            return convertView;
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_count_info;
        }

        class ViewHolder {
            TextView txtName, txtCount;
        }
    }
}
