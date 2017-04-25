package com.zxly.o2o.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.InsuranceAct;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest.ResponseStateListener;
import com.zxly.o2o.request.InsuranceBuyRequest;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

public class InsuranceApplyFragment extends BaseFragment implements OnClickListener {
    private String name, phone, salesmanNo, phoneBrand, phoneModel;
    private EditText editName, editPhone, editSalesmanNo;
    private CheckBox cbx;
    private int isNeedInvoice = 1;
    private long id, orderId;

    @Override
    protected void initView() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        ViewUtils.setText(findViewById(R.id.txt_title), "申请购买");
        findViewById(R.id.btn_service).setOnClickListener(this);
        findViewById(R.id.btn_submit).setOnClickListener(this);

        editName = (EditText) findViewById(R.id.edit_name);
        editPhone = (EditText) findViewById(R.id.edit_phone);
        editSalesmanNo = (EditText) findViewById(R.id.edit_salesman_no);
        editPhone.setText(Account.user.getMobilePhone());
        cbx = (CheckBox) findViewById(R.id.cbx_receipt);
        id = ((InsuranceAct) getActivity()).id;

        phoneBrand = Build.BRAND;
        phoneModel = Build.MODEL;
        ((TextView) findViewById(R.id.txt_phone_model)).setText(phoneModel + " (本机)");
        ((TextView) findViewById(R.id.txt_imei)).setText("IMEI: " + Config.imei);

        if(Account.shopInfo != null) {
            ((TextView) findViewById(R.id.txt_shop_name)).setText("办理地址: " + Account.shopInfo.getName());
            ((TextView) findViewById(R.id.txt_shop_address)).setText(Account.shopInfo.getAddress());
        }
    }

    @Override
    protected int layoutId() {
        return R.layout.win_insurance_apply;
    }

    private void loadData() {
        final InsuranceBuyRequest request = new InsuranceBuyRequest(Config.shopId, id, phoneBrand, phoneModel, name, phone, salesmanNo, isNeedInvoice);
        request.setOnResponseStateListener(new ResponseStateListener() {

            @Override
            public void onOK() {
                orderId = request.getOrderId();
                Bundle bundle = new Bundle();
                bundle.putLong("orderId", orderId);
                ((InsuranceAct) getActivity()).changeCurrentState(1);//状态变为已申购
                ((InsuranceAct) getActivity()).setPageShow(1, bundle);
            }

            @Override
            public void onFail(int code) {
            }
        });
        request.start(getActivity());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                ((InsuranceAct) getActivity()).setPageShow(0, null);
                break;
            case R.id.btn_service:
                ((InsuranceAct) getActivity()).insuranceService();
                break;
            case R.id.btn_submit:
                name = editName.getText().toString().trim();
                if ("".equals(name)) {
                    ViewUtils.showToast("姓名不能为空");
                    return;
                }
                phone = editPhone.getText().toString().trim();
                if ("".equals(phone)) {
                    ViewUtils.showToast("手机号码不能为空");
                    return;
                }
                if (!StringUtil.isMobileNO(phone)) {
                    ViewUtils.showToast("手机号码有误");
                    return;
                }
                salesmanNo = editSalesmanNo.getText().toString().trim();
                if ("".equals(salesmanNo)) {
                    ViewUtils.showToast("业务员编号不能为空");
                    return;
                }
                if (salesmanNo.length() != 4) {
                    ViewUtils.showToast("业务员编号有误");
                    return;
                }
                isNeedInvoice = cbx.isChecked() ? 2 : 1;

                loadData();
                break;
        }
    }

}
