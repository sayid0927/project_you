package com.zxly.o2o.fragment;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.activity.InsuranceAct;
import com.zxly.o2o.activity.InsuranceClauseAct;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.InsuranceOrder;
import com.zxly.o2o.model.InsuranceProduct;
import com.zxly.o2o.model.InsuranceSupplier;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest.ResponseStateListener;
import com.zxly.o2o.request.InsuranceOrderRequest;
import com.zxly.o2o.util.PhoneUtil;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

public class InsuranceFragment extends BaseFragment implements OnClickListener {
    private InsuranceProduct insuranceProduct;
    private InsuranceOrder insuranceOrder;
    private InsuranceSupplier insuranceSupplier;
    private long id, orderId;
    /**
     * 保单状态
     * -1:没有购买,1:已申购,2:已取消,3:已拒单,4:待支付,5:支付超时,6:审核中,7:保障中,8:已退单,9:已过期
     */
    private int orderStatus;
    private NetworkImageView imgLogo;
    private ImageView imgState;
    private TextView txtInsuranceTelephone;
    private String insuranceTelephone;

    @Override
    protected void initView() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        ViewUtils.setText(findViewById(R.id.txt_title), "我的保障单");
        findViewById(R.id.btn_service).setOnClickListener(this);
        findViewById(R.id.btn_insurance_province).setOnClickListener(this);

        orderId = ((InsuranceAct) getActivity()).orderId;
        orderStatus = ((InsuranceAct) getActivity()).orderStatus;
        imgLogo = (NetworkImageView) findViewById(R.id.img_insurance_logo);
        imgState = (ImageView) findViewById(R.id.img_insurance_state);
        txtInsuranceTelephone = (TextView) findViewById(R.id.txt_insurance_phone);
        txtInsuranceTelephone.setOnClickListener(this);
        switch (orderStatus) {
            case 6:
                imgState.setBackgroundResource(R.drawable.icon_insurance_checking);
                ViewUtils.setGone(findViewById(R.id.view_insurance_info));
                break;
            case 7:
                ViewUtils.setGone(imgState);
                break;
            case 9:
                imgState.setBackgroundResource(R.drawable.icon_insurance_expired);
                ViewUtils.setGone(findViewById(R.id.view_insurance_info));
                break;
        }
        loadData();
    }

    @Override
    protected int layoutId() {
        return R.layout.win_insurance_detail;
    }

    private void loadData() {
        final InsuranceOrderRequest request = new InsuranceOrderRequest(orderId);
        request.setOnResponseStateListener(new ResponseStateListener() {

            @Override
            public void onOK() {
                insuranceProduct = request.getInsuranceProduct();
                id = insuranceProduct.getId();
                insuranceOrder = request.getInsuranceOrder();
                insuranceSupplier = request.getInsuranceSupplier();

                if (insuranceOrder != null) {
                    ((TextView) findViewById(R.id.txt_insurance_no)).setText("合同号：" + insuranceOrder.getContractNum());
                    ViewUtils.setText(findViewById(R.id.txt_insurance_user), insuranceOrder.getUserName());
                    ViewUtils.setText(findViewById(R.id.txt_insurance_mobile), insuranceOrder.getUserPhone());
                    ViewUtils.setText(findViewById(R.id.txt_insurance_brand), insuranceOrder.getPhoneModel());
                    ViewUtils.setText(findViewById(R.id.txt_insurance_imei), insuranceOrder.getPhoneImei());
                    ((TextView) findViewById(R.id.txt_insurance_price)).setText(insuranceOrder.getPrice() + "元");
                    ViewUtils.setText(findViewById(R.id.txt_insurance_start), StringUtil.getDateByMillis(insuranceOrder.getEffectTime()));
                }
                if (insuranceProduct != null) {
                    ViewUtils.setText(findViewById(R.id.txt_insurance_name), insuranceProduct.getName());
                    int compensateNum = insuranceProduct.getCompensateNum();
                    if (compensateNum == -1) {
                        ((TextView) findViewById(R.id.txt_insurance_count)).setText("不限次数");
                    } else {
                        ((TextView) findViewById(R.id.txt_insurance_count)).setText(compensateNum + "次");
                    }
                    ViewUtils.setText(findViewById(R.id.txt_insurance_end), StringUtil.getDateByMonthAdd(insuranceOrder.getEffectTime(), insuranceProduct.getServicePeriod()));
                }
                if (insuranceSupplier != null) {
//                    imgLogo.setDefaultImageResId(R.drawable.icon_insurance_logo);
//                    imgLogo.setDefaultImageResId(R.drawable.icon_default);

                    imgLogo.setImageUrl(insuranceSupplier.getLogoUrl(), AppController.imageLoader);
                    insuranceTelephone = insuranceSupplier.getContactPhone();
                    ViewUtils.setText(txtInsuranceTelephone, insuranceTelephone);
                    ((TextView) findViewById(R.id.txt_insurance_service_time)).setText("服务时间：" + insuranceSupplier.getServiceTime());
                }
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
                getActivity().finish();
                break;
            case R.id.btn_service:
                ((InsuranceAct) getActivity()).insuranceService();
                break;
            case R.id.btn_insurance_province:
                InsuranceClauseAct.start(getActivity(), id);
                break;
            case R.id.txt_insurance_phone:
                PhoneUtil.openPhoneKeyBord(insuranceTelephone, getActivity());
                break;
        }
    }

}
