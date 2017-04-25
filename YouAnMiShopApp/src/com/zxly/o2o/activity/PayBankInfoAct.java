package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.application.Config;
import com.zxly.o2o.model.BankCity;
import com.zxly.o2o.model.BankProvince;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.AreaUtil;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fengrongjian 2016-3-8
 * @description 银行卡开户行开户地选择界面
 */
public class PayBankInfoAct extends BasicAct implements
        View.OnClickListener, OnItemClickListener {
    private Context context;
    private List<BankProvince> bankCodeList = null;
    private ArrayList<BankCity> cityList = new ArrayList<BankCity>();
    private ArrayList<String> dataList = new ArrayList<String>();
    private String provinceCode = null;
    private String provinceName = null;
    private String cityCode = null;
    private String cityName = null;
    private ListView listView;
    private TextView txtProvinceName = null;
    private AddressAdapter adapter = null;
    private int currentState = 0;
    private LoadingView loadingview = null;
    private final int LOAD_SUCCEED = 10;
    private static ParameCallBack parameCallBack;

    @Override
    protected void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.what == LOAD_SUCCEED) {
            loadingview.onLoadingComplete();
            initViews();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_bank_info);
        context = this;
        findViewById(R.id.btn_back).setOnClickListener(this);
        ViewUtils.setText(findViewById(R.id.txt_title), "区域选择");
        loadingview = (LoadingView) findViewById(R.id.view_loading);
        bankCodeList = Config.bankCodeList;
        if (bankCodeList != null && !bankCodeList.isEmpty()) {
            initViews();
        } else {
            loadingview.startLoading();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Config.bankCodeList = AreaUtil.getBankCodeFromFile();
                    bankCodeList = Config.bankCodeList;
                    if (bankCodeList != null && !bankCodeList.isEmpty()) {
                        mMainHandler.obtainMessage(LOAD_SUCCEED).sendToTarget();
                    }
                }
            }).start();
        }
    }

    public static void start(Activity curAct, ParameCallBack _parameCallBack) {
        parameCallBack = _parameCallBack;
        Intent intent = new Intent(curAct, PayBankInfoAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

    private void initViews() {
        txtProvinceName = (TextView) findViewById(R.id.txt_province);
        txtProvinceName.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.list_view);
        adapter = new AddressAdapter(context);
        txtProvinceName.setVisibility(View.GONE);
        currentState = 1;
        adapter.clear();
        adapter.addItem(getProvinceData());
        adapter.setCurrentState(1);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    private ArrayList<String> getProvinceData() {
        if (bankCodeList != null) {
            dataList.clear();
            for (BankProvince bp : bankCodeList) {
                dataList.add(bp.getProvice());
            }
        }
        return dataList;
    }

    private ArrayList<String> getCityData() {
        if (cityList != null) {
            dataList.clear();
            for (BankCity bc : cityList) {
                dataList.add(bc.getName());
            }
        }
        return dataList;
    }

    private void showProvinces() {
        txtProvinceName.setVisibility(View.GONE);
        txtProvinceName.setText("");
        currentState = 1;
        adapter.clear();
        adapter.addItem(getProvinceData());
        adapter.setCurrentState(1);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (currentState) {
            case 1:
                provinceName = bankCodeList.get(position).getProvice();
                provinceCode = bankCodeList.get(position).getProvinceCode();
                cityList = (ArrayList<BankCity>) bankCodeList.get(position).getArea();
                txtProvinceName.setVisibility(View.VISIBLE);
                txtProvinceName.setText(provinceName);
                currentState = 2;
                adapter.clear();
                adapter.addItem(getCityData(), true);
                adapter.setCurrentState(2);
                break;
            case 2:
                cityName = cityList.get(position).getName();
                cityCode = cityList.get(position).getCode();
                if (parameCallBack != null) {
                    Map<String, String> result = new HashMap<String, String>();
                    result.put("provinceCode", provinceCode);
                    result.put("cityCode", cityCode);
                    result.put("provinceName", provinceName);
                    result.put("cityName", cityName);
                    parameCallBack.onCall(result);
                    finish();
                }
                break;
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
            case R.id.txt_province:
                showProvinces();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (currentState == 2) {
                showProvinces();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    class AddressAdapter extends ObjectAdapter {
        private ArrayList<String> dataList = null;

        private int currentState = 0;

        public AddressAdapter(Context context) {
            super(context);
        }

        public void setDataList(ArrayList<String> dataList) {
            this.dataList = dataList;
        }

        public void setCurrentState(int currentState) {
            this.currentState = currentState;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            AddressItemHolder holder;
            if (convertView == null) {
                convertView = inflateConvertView();
                holder = new AddressItemHolder();
                holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
                convertView.setTag(holder);
            } else {
                holder = (AddressItemHolder) convertView.getTag();
            }

            holder.txtName.setText((String) getItem(position));
            return convertView;
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_area;
        }

        class AddressItemHolder {
            TextView txtName;
        }

    }

}
