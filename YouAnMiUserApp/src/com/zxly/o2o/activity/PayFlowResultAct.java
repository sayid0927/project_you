package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.FlowRechargeRequest;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.ViewUtils;

/**
 * @author fengrongjian 2015-5-22
 * @description 充值提现成功界面
 */
public class PayFlowResultAct extends BasicAct implements View.OnClickListener {
    private Context context;
    private String payNo;
    private TextView btnGoAhead;
    private View viewRoot;
    private String payType;
    private String result;
    private ImageView imgPayResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_flow_result);
        context = this;
        payNo = getIntent().getStringExtra("payNo");
        payType = getIntent().getStringExtra("payType");
        result = getIntent().getStringExtra("result");
        initViews();
        if (Constants.PAY_TYPE_USER_BALANCE.equals(payType)) {
            if ("success".equals(result)) {
                showSuccess();
            } else if("fail".equals(result)){
                showFail();
            }
        } else {
            if ("success".equals(result)) {
                flowRecharge(payNo);
            } else if("fail".equals(result)){
                showFail();
            }
        }
    }


    /**
     * @param payNo 流水号
     */
    public static void start(Activity curAct, String payNo, String payType, String result) {
        Intent intent = new Intent(curAct, PayFlowResultAct.class);
        intent.putExtra("payNo", payNo);
        intent.putExtra("payType", payType);
        intent.putExtra("result", result);
        ViewUtils.startActivity(intent, curAct);
    }

    private void initViews() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        ViewUtils.setText(findViewById(R.id.txt_title), "流量充值");
        viewRoot = findViewById(R.id.view_root);
//        ViewUtils.setGone(viewRoot);
        imgPayResult = (ImageView) findViewById(R.id.img_pay_result);
        findViewById(R.id.btn_to_home).setOnClickListener(this);
        btnGoAhead = (TextView) findViewById(R.id.btn_go_ahead);
        btnGoAhead.setOnClickListener(this);
    }

    private void showSuccess() {
        ViewUtils.setVisible(viewRoot);
        imgPayResult.setBackgroundResource(R.drawable.icon_zf_success);
        ((TextView) findViewById(R.id.txt_pay_result)).setText("付款成功");
        ((TextView) findViewById(R.id.txt_pay_desc)).setText("您已成功在柚安米平台提交流量充值申请。流量将在24小时内到帐，如有疑问请咨询客服热线：4008077067。");
        ((TextView) findViewById(R.id.btn_to_home)).setText("回到主页");
        ViewUtils.setText(btnGoAhead, "继续充值");
    }

    private void showFail() {
        ViewUtils.setVisible(viewRoot);
        imgPayResult.setBackgroundResource(R.drawable.icon_zf_fail);
        ((TextView) findViewById(R.id.txt_pay_result)).setText("付款失败");
        ((TextView) findViewById(R.id.txt_pay_result)).setTextColor(getResources().getColor(R.color.gray_999999));
        ((TextView) findViewById(R.id.txt_pay_desc)).setText(" ");
        ((TextView) findViewById(R.id.btn_to_home)).setText("回到主页");
        ViewUtils.setText(btnGoAhead, "重新充值");
    }

    private void flowRecharge(String numberNo) {
        FlowRechargeRequest flowRechargeRequest = new FlowRechargeRequest(numberNo);
        flowRechargeRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                showSuccess();
            }

            @Override
            public void onFail(int code) {
                showFail();
            }
        });
        flowRechargeRequest.start(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            RechargeRecordAct.start(PayFlowResultAct.this);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                RechargeRecordAct.start(PayFlowResultAct.this);
                finish();
                break;
            case R.id.btn_to_home:
                Intent intent = new Intent(PayFlowResultAct.this, HomeAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_go_ahead:
                finish();
                break;
        }
    }

}
