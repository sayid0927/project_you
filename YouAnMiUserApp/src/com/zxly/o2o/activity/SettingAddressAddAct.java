package com.zxly.o2o.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxly.o2o.model.AddressVillage;
import com.zxly.o2o.model.UserAddress;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.AddressAddRequest;
import com.zxly.o2o.request.AddressEditRequest;
import com.zxly.o2o.request.BaseRequest.ResponseStateListener;
import com.zxly.o2o.util.AppLog;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.ViewUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author fengrongjian 2015-1-28
 * @description 新增收货地址界面
 */
public class SettingAddressAddAct extends BasicAct implements
        View.OnClickListener {
    private EditText editName, editMobile, editDetail;
    private ImageView btnCleanName, btnCleanPhone, btnCleanDetail;
    private TextView txtArea, txtVillage;
    private TextView btnDefault;
    private static final int GET_AREA = 10;
    private static final int GET_VILLAGE = 11;
    private Integer isDefault = 2;
    private String countryId = null;
    private String provinceId = null;
    private String cityId = null;
    private String districtId = null;
    private String provinceName = null;
    private String cityName = null;
    private String districtName = null;
    private String source = null;
    private UserAddress userAddress = null;
    private Long villageId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_personal_address_add);
        source = getIntent().getStringExtra("source");
        initViews();
    }

    private void initViews() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        ((TextView) findViewById(R.id.txt_title)).setText("新增收货地址");
        findViewById(R.id.btn_address_save).setOnClickListener(this);
        btnCleanName = (ImageView) findViewById(R.id.btn_clean_name);
        btnCleanName.setOnClickListener(this);
        btnCleanPhone = (ImageView) findViewById(R.id.btn_clean_phone);
        btnCleanPhone.setOnClickListener(this);
        btnCleanDetail = (ImageView) findViewById(R.id.btn_clean_detail);
        btnCleanDetail.setOnClickListener(this);

        editName = (EditText) findViewById(R.id.edit_name);
        editName.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    btnCleanName.setVisibility(View.GONE);
                } else {
                    btnCleanName.setVisibility(View.VISIBLE);
                }
            }
        });
        editMobile = (EditText) findViewById(R.id.edit_mobile);
        editMobile.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    btnCleanPhone.setVisibility(View.GONE);
                } else {
                    btnCleanPhone.setVisibility(View.VISIBLE);
                }
            }
        });
        editDetail = (EditText) findViewById(R.id.detail_address);
        editDetail.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    btnCleanDetail.setVisibility(View.GONE);
                } else {
                    btnCleanDetail.setVisibility(View.VISIBLE);
                }
            }
        });
        findViewById(R.id.btn_area).setOnClickListener(this);
        findViewById(R.id.btn_village).setOnClickListener(this);
        txtArea = (TextView) findViewById(R.id.address_area);
        txtVillage = (TextView) findViewById(R.id.address_village);
        btnDefault = (TextView) findViewById(R.id.personal_set_default_address);
        btnDefault.setOnClickListener(this);
        if ("edit".equals(source)) {
            findViewById(R.id.view_default_layout).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.txt_title)).setText("编辑收货地址");
            userAddress = (UserAddress) getIntent().getSerializableExtra(
                    "address");
            countryId = userAddress.getCountryId() + "";
            provinceId = userAddress.getProvinceId() + "";
            if (userAddress.getCityId() != null) {
                cityId = userAddress.getCityId() + "";
            }
            if (userAddress.getAreaId() != null) {
                districtId = userAddress.getAreaId() + "";
            }
            villageId = userAddress.getVillageId();

            editName.setText(userAddress.getName());
            editName.setSelection(editName.getText().toString().length());
            editMobile.setText(userAddress.getMobilePhone());
            editDetail.setText(userAddress.getDetailedAddress());
            txtArea.setText(userAddress.getPcaName());
            txtArea.setTextColor(getResources().getColor(
                    R.color.address_area));
            if (userAddress.getVillageName() != null) {
                txtVillage.setText(userAddress.getVillageName());
                txtVillage.setTextColor(getResources().getColor(
                        R.color.address_area));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == GET_AREA) {
                provinceName = data.getStringExtra("provinceName");
                cityName = data.getStringExtra("cityName");
                districtName = data.getStringExtra("districtName");
                countryId = data.getStringExtra("countryId");
                provinceId = data.getStringExtra("provinceId");
                cityId = data.getStringExtra("cityId");
                districtId = data.getStringExtra("districtId");
                txtArea.setText(provinceName + cityName + districtName);
                txtArea.setTextColor(getResources().getColor(
                        R.color.address_area));
                txtVillage.setText("请选择街道");
                txtVillage.setTextColor(getResources().getColor(
                        R.color.address_default));
                villageId = null;
            } else if (requestCode == GET_VILLAGE) {
                AddressVillage village = (AddressVillage) data.getSerializableExtra("village");
                txtVillage.setText(village.getName());
                txtVillage.setTextColor(getResources().getColor(
                        R.color.address_area));
                villageId = village.getId();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_address_save:
                if ("".equals(editName.getText().toString().trim())) {
                    ViewUtils.showToast("姓名不能为空");
                    return;
                }
                String phoneNumber = editMobile.getText().toString();
                Pattern p = Pattern.compile(Constants.PHONE_PATTERN);
                Matcher m = p.matcher(phoneNumber);
                if (phoneNumber.length() < 11 || !m.matches()) {
                    ViewUtils.showToast("请输入正确的电话号码");
                    return;
                }
                if ("请选择区域".equals(txtArea.getText().toString().trim())) {
                    ViewUtils.showToast("区域不能为空");
                    return;
                }
                if ("".equals(editDetail.getText().toString().trim())) {
                    ViewUtils.showToast("详细地址不能为空");
                    return;
                }
                UserAddress address = new UserAddress();
                address.setCountryId(Long.parseLong(countryId));
                address.setProvinceId(Long.parseLong(provinceId));
                if (cityId != null) {
                    address.setCityId(Long.parseLong(cityId));
                } else {
                    address.setCityId(null);
                }
                if (districtId != null) {
                    address.setAreaId(Long.parseLong(districtId));
                } else {
                    address.setAreaId(null);
                }
                address.setVillageId(villageId);
                address.setDetailedAddress(editDetail.getText().toString()
                        .trim());
                address.setIsDefault(isDefault);
                address.setMobilePhone(editMobile.getText().toString().trim());
                address.setName(editName.getText().toString().trim());

                if ("edit".equals(source)) {
                    address.setId(userAddress.getId());
                    address.setIsDefault(userAddress.getIsDefault());
                    AppLog.e("---address edit to String---" + address.toString());
                    AddressEditRequest request = new AddressEditRequest(address);
                    request.setOnResponseStateListener(new ResponseStateListener() {

                        @Override
                        public void onOK() {
                            setResult(RESULT_OK);
                            SettingAddressAddAct.this.finish();
                        }

                        @Override
                        public void onFail(int code) {
                        }
                    });
                    request.start(this);
                } else if ("add".equals(source)) {
                    AppLog.e("---address add to String---" + address.toString());
                    AddressAddRequest request = new AddressAddRequest(address);
                    request.setOnResponseStateListener(new ResponseStateListener() {

                        @Override
                        public void onOK() {
                            setResult(RESULT_OK);
                            SettingAddressAddAct.this.finish();
                        }

                        @Override
                        public void onFail(int code) {
                        }
                    });
                    request.start(this);
                }
                break;
            case R.id.personal_set_default_address:
                if (btnDefault.isSelected()) {
                    btnDefault.setSelected(false);
                    isDefault = 2;
                } else {
                    btnDefault.setSelected(true);
                    isDefault = 1;
                }
                break;
            case R.id.btn_area:
                intent = new Intent(SettingAddressAddAct.this,
                        SettingAddressAreaAct.class);
                if ("edit".equals(source)) {
                    intent.putExtra("address", userAddress);
                }
                startActivityForResult(intent, GET_AREA);
                break;
            case R.id.btn_village:
                if (provinceId != null) {
                    intent = new Intent(SettingAddressAddAct.this,
                            SettingAddressVillageAct.class);
                    if ("edit".equals(source)) {
                        intent.putExtra("address", userAddress);
                    }
                    if (cityId != null) {
                        intent.putExtra("cityId", cityId);
                    }
                    if (districtId != null) {
                        intent.putExtra("areaId", districtId);
                    }
                    startActivityForResult(intent, GET_VILLAGE);
                } else {
                    ViewUtils.showToast("请先选择区域");
                }
                break;
            case R.id.btn_clean_name:
                editName.setText("");
                break;
            case R.id.btn_clean_phone:
                editMobile.setText("");
                break;
            case R.id.btn_clean_detail:
                editDetail.setText("");
                break;
        }
    }
}
