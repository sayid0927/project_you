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

import com.zxly.o2o.account.Account;
import com.zxly.o2o.adapter.AddressAdapter;
import com.zxly.o2o.adapter.AddressAdapter.OnAddressItemClickListener;
import com.zxly.o2o.model.AddressCity;
import com.zxly.o2o.model.AddressCountry;
import com.zxly.o2o.model.AddressDistrict;
import com.zxly.o2o.model.AddressProvince;
import com.zxly.o2o.model.UserAddress;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.AreaUtil;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fengrongjian 2015-2-12
 * @description 收货地址区域选择界面
 */
public class SettingAddressAreaAct extends BasicAct implements
        View.OnClickListener, OnAddressItemClickListener {
    private Context context;
    private List<AddressCountry> areaList = null;
    private ArrayList<AddressProvince> provinceList = new ArrayList<AddressProvince>();
    private ArrayList<AddressCity> cityList = new ArrayList<AddressCity>();
    private ArrayList<AddressDistrict> districtList = new ArrayList<AddressDistrict>();
    private ArrayList<String> dataList = new ArrayList<String>();
    private String countryId = null;
    private String provinceId = null;
    private String provinceName = null;
    private String cityId = null;
    private String cityName = null;
    private String districtId = null;
    private ListView listView = null;
    private TextView txtPrvName = null;
    private TextView txtCityName = null;
    private AddressAdapter adapter = null;
    private String source = null;
    private int currentState = 0;
    private LoadingView loadingview = null;
    private final int LOAD_SUCCEED = 10;
    private static ParameCallBack parameCallBack;

    private ArrayList<String> getPrvData() {
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

    private ArrayList<String> getAreaNoCityData() {
        if (areaList != null) {
            districtList = (ArrayList<AddressDistrict>) cityList.get(0).getDistricts();
            dataList.clear();
            for (AddressDistrict ad : districtList) {
                dataList.add(ad.getDistrictName());
            }
        }
        return dataList;
    }

    private ArrayList<String> getAreaData() {
        if (areaList != null) {
            dataList.clear();
            for (AddressDistrict ad : districtList) {
                dataList.add(ad.getDistrictName());
            }
        }
        return dataList;
    }

    @Override
    protected void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.what == LOAD_SUCCEED) {
            loadingview.onLoadingComplete();
            initViews();
        }
    }

    public static void start(Activity curAct, ParameCallBack _parameCallBack){
        parameCallBack = _parameCallBack;
        Intent intent = new Intent(curAct, SettingAddressAreaAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_personal_address_area_new);
        context = this;
        source = getIntent().getStringExtra("source");
        findViewById(R.id.btn_back).setOnClickListener(this);
        ViewUtils.setText(findViewById(R.id.txt_title), "区域选择");
        loadingview = (LoadingView) findViewById(R.id.view_loading);
        areaList = Account.areaList;
        if (areaList != null && !areaList.isEmpty()) {
            initViews();
        } else {
            loadingview.startLoading();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Account.areaList = AreaUtil.getAreaFromFile();
                    areaList = Account.areaList;
                    if (areaList != null && !areaList.isEmpty()) {
                        mMainHandler.obtainMessage(LOAD_SUCCEED).sendToTarget();
                    }
                }
            }).start();
        }
    }

    private void initViews() {
        countryId = areaList.get(0).getCountryId();
        txtPrvName = (TextView) findViewById(R.id.prv_name);
        txtCityName = (TextView) findViewById(R.id.city_name);
        txtPrvName.setOnClickListener(this);
        txtCityName.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.list_view);
        adapter = new AddressAdapter(context, this);
        if ("edit".equals(source)) {
//			adapter.setDataList(areaList.get(0).getPrvs());
            adapter.setCurrentState(3);
        } else {
            txtPrvName.setVisibility(View.GONE);
            txtCityName.setVisibility(View.GONE);
            currentState = 1;
            adapter.clear();
            adapter.addItem(getPrvData());
            adapter.setCurrentState(1);
        }
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                switch (currentState) {
                    case 1:
                        txtPrvName.setVisibility(View.VISIBLE);
                        provinceName = provinceList.get(position).getProvinceName();
                        txtPrvName.setText(provinceName);
                        provinceId = provinceList.get(position).getProvinceId();
                        cityList = (ArrayList<AddressCity>) provinceList.get(position).getCitys();
                        if (cityList.get(0).getCityId() != null) {
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
                            } else {
                                currentState = 3;
                                adapter.clear();
                                adapter.addItem(getAreaNoCityData(), true);
                                adapter.setCurrentState(3);
                            }
                        }
                        break;
                    case 2:
                        txtCityName.setVisibility(View.VISIBLE);
                        cityName = cityList.get(position).getCityName();
                        txtCityName.setText(cityName);
                        cityId = cityList.get(position).getCityId();
                        districtList = (ArrayList<AddressDistrict>) cityList.get(position).getDistricts();
                        if (parameCallBack != null) {
                            Map<String, String> result = new HashMap<String, String>();
                            result.put("provinceId", provinceId);
                            result.put("cityId", cityId);
                            result.put("provinceName", provinceName);
                            result.put("cityName", cityName);
                            parameCallBack.onCall(result);
                            finish();
                        } else {
                            if (districtList.size() != 0) {
                                currentState = 3;
                                adapter.clear();
                                adapter.addItem(getAreaData(), true);
                                adapter.setCurrentState(3);
                            } else {
                                Intent intent = new Intent();
                                intent.putExtra("provinceName", txtPrvName.getText().toString());
                                intent.putExtra("cityName", txtCityName.getText().toString());
                                intent.putExtra("districtName", "");
                                intent.putExtra("countryId", countryId);
                                intent.putExtra("provinceId", provinceId);
                                intent.putExtra("cityId", cityId);
                                intent.putExtra("districtId", districtId);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        }
                        break;
                    case 3:
                        districtId = districtList.get(position).getDistrictId();
                        Intent intent = new Intent();
                        intent.putExtra("provinceName", txtPrvName.getText().toString());
                        intent.putExtra("cityName", txtCityName.getText().toString());
                        intent.putExtra("districtName", districtList.get(position).getDistrictName());
                        intent.putExtra("countryId", countryId);
                        intent.putExtra("provinceId", provinceId);
                        intent.putExtra("cityId", cityId);
                        intent.putExtra("districtId", districtId);
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                }
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        if(parameCallBack != null){
            parameCallBack =null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.prv_name:
                showPrvs();
                break;
            case R.id.city_name:
                showCitys();
                break;
        }
    }

    @Override
    public void onClicked(UserAddress address) {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (currentState == 2) {
                showPrvs();
                return true;
            } else if (currentState == 3) {
                if (cityList.get(0).getCityId() != null) {
                    showCitys();
                } else {
                    showPrvs();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showPrvs() {
        txtPrvName.setVisibility(View.GONE);
        txtPrvName.setText("");
        txtCityName.setVisibility(View.GONE);
        txtCityName.setText("");
        currentState = 1;
        adapter.clear();
        adapter.addItem(getPrvData());
        adapter.setCurrentState(1);
    }

    private void showCitys() {
        txtCityName.setVisibility(View.GONE);
        txtCityName.setText("");
        currentState = 2;
        adapter.clear();
        adapter.addItem(getCityData());
        adapter.setCurrentState(2);
    }

}
