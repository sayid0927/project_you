package com.zxly.o2o.fragment;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.zxly.o2o.activity.InsuranceAct;
import com.zxly.o2o.activity.PayAct;
import com.zxly.o2o.model.InsuranceOrder;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.InsuranceOrderRequest;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;



    //2017.4.13

public class InsuranceSuccessFragment extends BaseFragment implements OnClickListener {
    private long orderId;
    private String name;
    private String orderNo;
    private float price;
    private  byte  payType;
    /**
     * 保单状态
     * -1:没有购买,1:已申购,2:已取消,3:已拒单,4:待支付,5:支付超时,6:审核中,7:保障中,8:已退单,9:已过期
     */
    private int orderStatus;

    @Override
    protected void initView() {

        name = ((InsuranceAct) getActivity()).name;
        orderNo = ((InsuranceAct) getActivity()).orderNo;
        orderId = ((InsuranceAct) getActivity()).orderId;
//        loadData(orderId);
        final InsuranceOrderRequest payTyperequest = new InsuranceOrderRequest(orderId);
        payTyperequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                InsuranceOrder insuranceOrder = payTyperequest.getInsuranceOrder();
                payType = insuranceOrder.getPayType();
                if(payType==0x02){
                    //线上支付
                    findViewById(R.id.btn_pay_now).setVisibility(View.GONE);
                    findViewById(R.id.layout_head).setVisibility(View.GONE);
                    findViewById(R.id.txt_price_symbol).setVisibility(View.GONE);
                    findViewById(R.id.txt_info).setVisibility(View.GONE);
                    findViewById(R.id.line_bottom).setVisibility(View.GONE);

                }else {

                    //线下支付
                    price = ((InsuranceAct) getActivity()).price;
                    ViewUtils.setText(findViewById(R.id.txt_title), name);
                    findViewById(R.id.btn_pay_now).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.txt_price_price)).setText(price + "");

                }
            }

            @Override
            public void onFail(int code) {

            }
        });

        payTyperequest.start(this);
        findViewById(R.id.btn_pay_now).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_service).setOnClickListener(this);

    }

    @Override
    protected int layoutId() {
        return R.layout.win_insurance_success;
    }

    private void loadData(final long orderId) {

        final InsuranceOrderRequest request = new InsuranceOrderRequest(orderId);
        request.setLoadingShow();
        request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                InsuranceOrder insuranceOrder = request.getInsuranceOrder();
                orderNo = insuranceOrder.getOrderNo();
                orderStatus = insuranceOrder.getOrderStatus();
                if (orderStatus == 4) {  //确定保单状态是待支付状态
                    if (!StringUtil.isNull(orderNo)) {
                        PayAct.start(getActivity(), orderNo, orderId, Constants.TYPE_INSURANCE_PAY);
                        getActivity().finish();
                    }
                } else if (orderStatus == 5) {
                    ViewUtils.showToast("支付已超时");
                } else if (orderStatus == 6 || orderStatus == 7){
                    ViewUtils.showToast("支付已完成");
                }
            }

            @Override
            public void onFail(int code) {
            }
        });
        request.start(this);
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
            case R.id.btn_pay_now:
                loadData(orderId);
                break;
        }
    }
}
