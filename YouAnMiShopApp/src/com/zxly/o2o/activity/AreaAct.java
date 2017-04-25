package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.zxly.o2o.adapter.AddressAdapter;
import com.zxly.o2o.application.Config;
import com.zxly.o2o.model.AddressCity;
import com.zxly.o2o.model.AddressCountry;
import com.zxly.o2o.model.AddressProvince;
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
 * @author fengrongjian 2016-1-5
 * @description 个人信息区域选择界面
 */
public class AreaAct extends BasicAct implements
        View.OnClickListener, OnItemClickListener {
    private Context context;
    private List<AddressCountry> areaList = null;
    private ArrayList<AddressProvince> provinceList = new ArrayList<AddressProvince>();
    private ArrayList<AddressCity> cityList = new ArrayList<AddressCity>();
    private ArrayList<String> dataList = new ArrayList<String>();
    private String provinceId = null;
    private String provinceName = null;
    private String cityId = null;
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
        setContentView(R.layout.win_area);
        context = this;
        findViewById(R.id.btn_back).setOnClickListener(this);
        ViewUtils.setText(findViewById(R.id.txt_title), "区域选择");
        loadingview = (LoadingView) findViewById(R.id.view_loading);
        areaList = Config.areaList;
        if (areaList != null && !areaList.isEmpty()) {
            initViews();
        } else {
            loadingview.startLoading();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Config.areaList = AreaUtil.getAreaFromFile();
                    areaList = Config.areaList;
                    if (areaList != null && !areaList.isEmpty()) {
                        mMainHandler.obtainMessage(LOAD_SUCCEED).sendToTarget();
                    }
                }
            }).start();
        }
    }

    public static void start(Activity curAct, ParameCallBack _parameCallBack) {
        parameCallBack = _parameCallBack;
        Intent intent = new Intent(curAct, AreaAct.class);
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
        if (areaList != null) {
            provinceList = (ArrayList<AddressProvince>) areaList.get(0).getPrvs();
            dataList.clear();
            for (AddressProvince ap : provinceList) {
                dataList.add(ap.getProvinceName());
            }
        }
        return dataList;
    }

    private ArrayList<String> getCityData() {
        if (areaList != null) {
            dataList.clear();
            for (AddressCity ac : cityList) {
                dataList.add(ac.getCityName());
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
                provinceName = provinceList.get(position).getProvinceName();
                provinceId = provinceList.get(position).getProvinceId();
                cityList = (ArrayList<AddressCity>) provinceList.get(position).getCitys();
                if (cityList.get(0).getCityId() != null) {
                    txtProvinceName.setVisibility(View.VISIBLE);
                    txtProvinceName.setText(provinceName);
                    currentState = 2;
                    adapter.clear();
                    adapter.addItem(getCityData(), true);
                    adapter.setCurrentState(2);
                } else {
                    if (parameCallBack != null) {
                        Map<String, String> result = new HashMap<String, String>();
                        result.put("provinceId", provinceId);
                        result.put("cityId", null);
                        result.put("provinceName", provinceName);
                        result.put("cityName", null);
                        parameCallBack.onCall(result);
                        finish();
                    }
                }
                break;
            case 2:
                cityName = cityList.get(position).getCityName();
                cityId = cityList.get(position).getCityId();
                if (parameCallBack != null) {
                    Map<String, String> result = new HashMap<String, String>();
                    result.put("provinceId", provinceId);
                    result.put("cityId", cityId);
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


}
