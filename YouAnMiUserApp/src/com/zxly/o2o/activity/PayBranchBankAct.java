package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.model.BranchBank;
import com.zxly.o2o.model.UserBankCard;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.PayQueryBankRequest;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fengrongjian 2016-3-8
 * @description 银行卡开户行支行所在地选择界面
 */
public class PayBranchBankAct extends BasicAct implements
        View.OnClickListener, OnItemClickListener {
    private Context context;
    private String branchBankName;
    private String prcptcd;
    private ListView listView;
    private EditText editSearch;
    private AddressAdapter adapter = null;
    private LoadingView loadingview = null;
    private static ParameCallBack parameCallBack;
    private ArrayList<BranchBank> branchBankList = new ArrayList<BranchBank>();
    private String cityCode;
    private String bankNo;
    private UserBankCard userBankCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_branch_bank);
        context = this;
        cityCode = getIntent().getStringExtra("cityCode");
        bankNo = getIntent().getStringExtra("bankNo");
        userBankCard = (UserBankCard) getIntent().getSerializableExtra("userBankCard");
        initViews();
        loadData(null);
    }

    public static void start(Activity curAct, String cityCode, String bankNo, UserBankCard userBankCard, ParameCallBack _parameCallBack) {
        parameCallBack = _parameCallBack;
        Intent intent = new Intent(curAct, PayBranchBankAct.class);
        intent.putExtra("cityCode", cityCode);
        intent.putExtra("bankNo", bankNo);
        intent.putExtra("userBankCard", userBankCard);
        ViewUtils.startActivity(intent, curAct);
    }

    private void initViews() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        ViewUtils.setText(findViewById(R.id.txt_title), "支行选择");
        loadingview = (LoadingView) findViewById(R.id.view_loading);
        editSearch = (EditText) findViewById(R.id.edit_search);
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    loadData(null);
                } else {
                    loadData(s.toString());
                }
            }
        });
        listView = (ListView) findViewById(R.id.list_view);
        adapter = new AddressAdapter(context);
        adapter.addItem(branchBankList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    private void loadData(String brabankName) {
        final PayQueryBankRequest payQueryBankRequest = new PayQueryBankRequest(brabankName, cityCode, userBankCard.getBankNumber(), bankNo);
        payQueryBankRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                loadingview.onLoadingComplete();
                branchBankList = payQueryBankRequest.getBranchBankList();
                adapter.clear();
                adapter.addItem(branchBankList, true);
            }

            @Override
            public void onFail(int code) {
                loadingview.onLoadingFail();
            }
        });
        loadingview.startLoading();
        payQueryBankRequest.start(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        branchBankName = branchBankList.get(position).getBrabank_name();
        prcptcd = branchBankList.get(position).getPrcptcd();
        if (parameCallBack != null) {
            Map<String, String> result = new HashMap<String, String>();
            result.put("branchBankName", branchBankName);
            result.put("prcptcd", prcptcd);
            parameCallBack.onCall(result);
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (parameCallBack != null) {
            parameCallBack = null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }

    class AddressAdapter extends ObjectAdapter {
        private ArrayList<String> dataList = null;

        public AddressAdapter(Context context) {
            super(context);
        }

        public void setDataList(ArrayList<String> dataList) {
            this.dataList = dataList;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            AddressItemHolder holder;
            if (convertView == null) {
                convertView = inflateConvertView();
                holder = new AddressItemHolder();
                holder.txtName = (TextView) convertView.findViewById(R.id.txt_bank);
                convertView.setTag(holder);
            } else {
                holder = (AddressItemHolder) convertView.getTag();
            }
            BranchBank branchBank = (BranchBank) getItem(position);
            holder.txtName.setText(branchBank.getBrabank_name());
            return convertView;
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_branch_bank;
        }

        class AddressItemHolder {
            TextView txtName;
        }

    }

}
