package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.model.UserAddress;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.AddressListRequest;
import com.zxly.o2o.request.BaseRequest.ResponseStateListener;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

import java.util.List;

/**
 * @author fengrongjian 2016-2-21
 * @description 选择收货地址界面
 */
public class SelectAddressAct extends BasicAct implements
        View.OnClickListener, OnItemClickListener {
    private Context context;
    private ListView listView;
    private PersonalAddressAdapter addressAdapter;
    private static ParameCallBack callBack;
    private LoadingView loadingview = null;
    private TextView btnAddressManage;
    private List<UserAddress> addressList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_personal_address_select);
        context = this;
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAddressList();
    }

    private void initViews() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        ViewUtils.setText(findViewById(R.id.txt_title), "选择收货地址");
        btnAddressManage = (TextView) findViewById(R.id.btn_title_right);
        ViewUtils.setVisible(btnAddressManage);
        ViewUtils.setText(btnAddressManage, "管理");
        btnAddressManage.setOnClickListener(this);

        loadingview = (LoadingView) findViewById(R.id.view_loading);
        listView = (ListView) findViewById(R.id.address_list);
        addressAdapter = new PersonalAddressAdapter(context);
        listView.setAdapter(addressAdapter);
        listView.setOnItemClickListener(this);
    }

    private void getAddressList() {
        final AddressListRequest request = new AddressListRequest();
        request.setOnResponseStateListener(new ResponseStateListener() {

            @Override
            public void onOK() {
                addressList = request.getAddressList();
                if (addressList.size() == 0) {
                    ViewUtils.setGone(listView);
                    loadingview.onDataEmpty("请在右上角管理收货信息！");
                    addressAdapter.resetDefUserAddress();
                } else {
                    ViewUtils.setVisible(listView);
                    addressAdapter.resetDefUserAddress();
                    addressAdapter.clear();
                    addressAdapter.addItem(addressList, true);
                    loadingview.onLoadingComplete();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_title_right:
                SettingAddressAct.start(SelectAddressAct.this, "manage");
                break;
        }
    }

    /**
     * @param curAct
     * @param _callBack 确认订单页面回调
     */
    public static void start(Activity curAct, ParameCallBack _callBack) {
        callBack = _callBack;
        Intent intent = new Intent(curAct, SelectAddressAct.class);
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
        UserAddress selectedAddress = (UserAddress) addressAdapter.getItem(position);
        addressAdapter.setSelectedAddress(selectedAddress);
        finish();
    }

    class PersonalAddressAdapter extends ObjectAdapter {
        private UserAddress defUserAddress = null;
        private UserAddress selectedAddress = null;

        public PersonalAddressAdapter(Context context) {
            super(context);
        }

        public void setSelectedAddress(UserAddress selectedAddress) {
            this.selectedAddress = selectedAddress;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            AddressViewHolder holder;
            if (convertView == null) {
                convertView = inflateConvertView();
                holder = new AddressViewHolder();
                holder.viewDefault = convertView.findViewById(R.id.view_default);
                holder.txtDefault = (TextView) convertView.findViewById(R.id.txt_default);
                holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
                holder.txtMobile = (TextView) convertView.findViewById(R.id.txt_mobile);
                holder.txtAddress = (TextView) convertView.findViewById(R.id.txt_address);
                holder.imgCurrentSelect = (ImageView) convertView.findViewById(R.id.img_select);
                convertView.setTag(holder);
            } else {
                holder = (AddressViewHolder) convertView.getTag();
            }
            final UserAddress address = (UserAddress) getItem(position);
            holder.txtName.setText(address.getName());
            holder.txtMobile.setText(address.getMobilePhone());
            holder.txtAddress.setText(address.getAddress());
            if (1 == address.getIsDefault()) {
                ViewUtils.setVisible(holder.viewDefault);
                ViewUtils.setVisible(holder.txtDefault);
                defUserAddress = address;
            } else {
                ViewUtils.setGone(holder.viewDefault);
                ViewUtils.setGone(holder.txtDefault);
            }
            if (Account.user.getTakeoutDeliveryId() == address.getId()) {
                holder.imgCurrentSelect.setBackgroundResource(R.drawable.checkbox_press);
            } else {
                holder.imgCurrentSelect.setBackgroundColor(getResources().getColor(R.color.white));
            }
            return convertView;
        }

        public UserAddress getDeliveryAddress() {
            if (selectedAddress != null) {
                return selectedAddress;
            } else {
                return defUserAddress;
            }
        }

        public void resetDefUserAddress() {
            defUserAddress = null;
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_select_address;
        }

        class AddressViewHolder {
            View viewDefault;
            TextView txtDefault;
            TextView txtName;
            TextView txtMobile;
            TextView txtAddress;
            ImageView imgCurrentSelect;
        }

    }

}
