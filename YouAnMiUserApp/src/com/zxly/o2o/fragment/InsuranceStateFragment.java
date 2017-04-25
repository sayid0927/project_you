package com.zxly.o2o.fragment;

import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxly.o2o.activity.InsuranceAct;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest.ResponseStateListener;
import com.zxly.o2o.request.InsuranceCancelRequest;
import com.zxly.o2o.util.ViewUtils;

public class InsuranceStateFragment extends BaseFragment implements OnClickListener {
    private Dialog dialog;
    private long orderId;
    private int orderStatus;//1:已申购, 2:已取消, 3:已拒单, 5:支付超时, 8:已退单
    private String name;
    private ImageView imgResult;
    private TextView txtResult, txtInfo;
    private View viewStatus, viewChecking;

    @Override
    protected void initView() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_service).setOnClickListener(this);
        viewStatus = findViewById(R.id.view_status);
        viewChecking = findViewById(R.id.view_checking);

        orderId = ((InsuranceAct) getActivity()).orderId;
        name = ((InsuranceAct) getActivity()).name;
        orderStatus = ((InsuranceAct) getActivity()).orderStatus;
        initViewByState(orderStatus);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {//重新显示出现
            orderId = ((InsuranceAct) getActivity()).orderId;
            orderStatus = ((InsuranceAct) getActivity()).orderStatus;
            initViewByState(orderStatus);
        }
    }

    private void initViewByState(int orderStatus){
        ViewUtils.setText(findViewById(R.id.txt_title), name);
        switch (orderStatus) {
            case 1:
                ViewUtils.setGone(viewStatus);
                ViewUtils.setVisible(viewChecking);
                dialog = new Dialog(getActivity(), R.style.dialog);
                findViewById(R.id.btn_cancel).setOnClickListener(this);
                break;
            case 2:
            case 3:
            case 5:
            case 8:
                ViewUtils.setGone(viewChecking);
                ViewUtils.setVisible(viewStatus);
                findViewById(R.id.btn_ok).setOnClickListener(this);
                imgResult = (ImageView) findViewById(R.id.img_result);
                txtResult = (TextView) findViewById(R.id.txt_result);
                txtInfo = (TextView) findViewById(R.id.txt_info);
                setViewByStatus(orderStatus);
                break;
        }
    }

    private void setViewByStatus(int orderStatus) {
        switch (orderStatus) {
            case 2:
                ViewUtils.setText(txtResult, "您已取消");
                ViewUtils.setGone(txtInfo);
                break;
            case 3:
                ViewUtils.setText(txtResult, "已被业务员拒单！");
                ViewUtils.setText(txtInfo, "您提交的申请不符合要求，或业务员未能为您补充资料");
                break;
            case 5:
                ViewUtils.setText(txtResult, "您已超过支付期限");
                ViewUtils.setGone(txtInfo);
                break;
            case 8:
                ViewUtils.setText(txtResult, "退单成功");
                txtResult.setTextColor(getResources().getColor(R.color.green_3fb837));
                imgResult.setBackgroundResource(R.drawable.icon_insurance_success);
                ViewUtils.setText(txtInfo, "您的退款将在24小时内原路返还至支付账户。");
                break;
        }

    }

    @Override
    protected int layoutId() {
        return R.layout.win_insurance_status;
    }

    private void loadData() {
        final InsuranceCancelRequest request = new InsuranceCancelRequest(orderId);
        request.setOnResponseStateListener(new ResponseStateListener() {

            @Override
            public void onOK() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                ((InsuranceAct) getActivity()).changeCurrentState(2);//状态变为已取消
                ((InsuranceAct) getActivity()).setPageShow(0, null);
            }

            @Override
            public void onFail(int code) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
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
            case R.id.btn_cancel:
                showDialog();
                break;
            case R.id.btn_ok:
                if (((InsuranceAct) getActivity()).type == 1) {
                    getActivity().finish();
                } else {
                    ((InsuranceAct) getActivity()).setPageShow(0, null);
                }
                break;
        }
    }

    private void showDialog() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog.show();
        dialog.setContentView(R.layout.dialog_insurance_cancel);
        dialog.findViewById(R.id.btn_cancel)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        loadData();
                    }
                });
        dialog.findViewById(R.id.btn_close)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                });
    }

}
