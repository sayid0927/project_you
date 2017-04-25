package com.zxly.o2o.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.adapter.PersonalAddressAdapter;
import com.zxly.o2o.model.UserAddress;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.AddressEditRequest;
import com.zxly.o2o.request.AddressListRequest;
import com.zxly.o2o.request.AddressRemoveRequest;
import com.zxly.o2o.request.BaseRequest.ResponseStateListener;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

import java.util.List;

/**
 * @author fengrongjian 2015-1-22
 * @description 设置收货地址界面
 */
public class SettingAddressAct extends BasicAct implements
        View.OnClickListener, OnItemClickListener, PersonalAddressAdapter.OnAddressClickListener {
    private Context context;
    private ListView listView;
    private PersonalAddressAdapter addressAdapter;
    private static final int ADD_ADDRESS = 10;
    private static final int EDIT_ADDRESS = 11;
    private static ParameCallBack callBack;
    private LoadingView loadingview = null;
    private TextView btnTopRight;
    private String source;
    private Dialog dialog;
    private List<UserAddress> addressList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_personal_address);
        context = this;
        source = getIntent().getStringExtra("source");
        initViews();
        getAddressList();
    }

    private void initViews() {
        dialog = new Dialog(this, R.style.dialog);
        findViewById(R.id.btn_back).setOnClickListener(this);
        if ("manage".equals(source)) {
            ((TextView) findViewById(R.id.txt_title)).setText("管理收货地址");
        } else {
            ((TextView) findViewById(R.id.txt_title)).setText("选择收货地址");
        }
        btnTopRight = (TextView) findViewById(R.id.btn_top_right);
        ViewUtils.setVisible(btnTopRight);
        btnTopRight.setOnClickListener(this);

        loadingview = (LoadingView) findViewById(R.id.view_loading);
        listView = (ListView) findViewById(R.id.address_list);
        addressAdapter = new PersonalAddressAdapter(context, this);
        listView.setAdapter(addressAdapter);
        listView.setOnItemClickListener(this);
    }

    private boolean checkHasDefaultAddress(List<UserAddress> addressList) {
        for (int i = 0; i < addressList.size(); i++) {
            if (addressList.get(i).getIsDefault() == 1) {
                return true;
            }
        }
        return false;
    }

    private void getAddressList() {
        final AddressListRequest request = new AddressListRequest();
        request.setOnResponseStateListener(new ResponseStateListener() {

            @Override
            public void onOK() {
                addressList = request.getAddressList();
                if (addressList.size() == 0) {
                    ViewUtils.setGone(listView);
                    loadingview.onDataEmpty("请添加收货信息！");
                    addressAdapter.resetDefUserAddress();
                } else {
                    ViewUtils.setVisible(listView);
                    addressAdapter.resetDefUserAddress();
                    addressAdapter.clear();
                    addressAdapter.addItem(addressList, true);
                    loadingview.onLoadingComplete();
                    //没有默认地址时设置第一个为默认地址
                    if (!checkHasDefaultAddress(addressList)) {
                        UserAddress userAddress = addressList.get(0);
                        setDefaultAddress(userAddress);
                    }
                }
            }

            @Override
            public void onFail(int code) {
                ViewUtils.setGone(listView);
                loadingview.onLoadingFail();
            }
        });
        loadingview.setOnAgainListener(new LoadingView.OnAgainListener() {

            @Override
            public void onLoading() {
                loadingview.startLoading();
                request.start(this);
            }
        });
        loadingview.startLoading();
        request.start(this);
    }

    //设置默认地址
    private void editAddressList(UserAddress address) {
        AddressEditRequest request = new AddressEditRequest(address);
        request.setOnResponseStateListener(new ResponseStateListener() {

            @Override
            public void onOK() {
                getAddressList();
            }

            @Override
            public void onFail(int code) {
            }
        });
        request.start(this);
    }

    //删除地址
    private void delAddress(final UserAddress userAddress) {
        AddressRemoveRequest removeRequest = new AddressRemoveRequest(userAddress.getId());
        removeRequest.setOnResponseStateListener(new ResponseStateListener() {

            @Override
            public void onOK() {
                getAddressList();
            }

            @Override
            public void onFail(int code) {
            }
        });
        removeRequest.start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            getAddressList();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_top_right:
                intent = new Intent(context, SettingAddressAddAct.class);
                intent.putExtra("source", "add");
                startActivityForResult(intent, ADD_ADDRESS);
                break;
        }
    }

    /**
     * @param curAct
     * @param _callBack 确认订单页面回调
     */
    public static void start(Activity curAct, ParameCallBack _callBack) {
        callBack = _callBack;
        Intent intent = new Intent(curAct, SettingAddressAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

    /**
     * @param curAct
     * @param source "manage": 管理收货地址，"select":"选择收货地址"
     */
    public static void start(Activity curAct, String source) {
        Intent intent = new Intent(curAct, SettingAddressAct.class);
        intent.putExtra("source", source);
        ViewUtils.startActivity(intent, curAct);
    }

    @Override
    public void finish() {
        super.finish();
        Account.areaList = null;
        if (callBack != null) {
            callBack.onCall(addressAdapter.getDeliveryAddress());
        }
        callBack = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!"manage".equals(source)) {
            UserAddress selectedAddress = (UserAddress) addressAdapter.getItem(position);
            addressAdapter.setSelectedAddress(selectedAddress);
            finish();
        }
    }

    @Override
    public void onEditClicked(UserAddress address) {
        Intent intent = new Intent(context, SettingAddressAddAct.class);
        intent.putExtra("address", address);
        intent.putExtra("source", "edit");
        startActivityForResult(intent, EDIT_ADDRESS);
    }

    @Override
    public void onDelClicked(UserAddress userAddress) {
        showConfirmDialog(userAddress);
    }

    @Override
    public void onDefaultClicked(UserAddress address) {
        setDefaultAddress(address);
    }

    private void showConfirmDialog(final UserAddress userAddress) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog.show();
        dialog.setContentView(R.layout.dialog_address_del);
        dialog.findViewById(R.id.btn_confirm)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        delAddress(userAddress);
                    }
                });
        dialog.findViewById(R.id.btn_cancel)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                });
    }

    private void setDefaultAddress(UserAddress address) {
        UserAddress userAddress = new UserAddress();
        userAddress.setId(address.getId());
        userAddress.setCountryId(address.getCountryId());
        userAddress.setName(address.getName());
        userAddress.setMobilePhone(address.getMobilePhone());
        userAddress.setProvinceId(address.getProvinceId());
        userAddress.setCityId(address.getCityId());
        userAddress.setAreaId(address.getAreaId());
        userAddress.setVillageId(address.getVillageId());
        userAddress.setDetailedAddress(address.getDetailedAddress());
        Integer isDefault = 1;
        userAddress.setIsDefault(isDefault);
        editAddressList(userAddress);
    }

}
