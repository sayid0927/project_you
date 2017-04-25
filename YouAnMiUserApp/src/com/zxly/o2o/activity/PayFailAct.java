package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.ViewUtils;

/**
 * @author fengrongjian 2015-5-22
 * @description 支付失败界面
 */
public class PayFailAct extends BasicAct implements View.OnClickListener {
    private Context context;
    private String orderNo;
    private String desc;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_pay_fail);
        context = this;
        orderNo = getIntent().getStringExtra("orderNo");
        desc = getIntent().getStringExtra("desc");
        type = getIntent().getIntExtra("type", -1);
        initViews();
    }

    /**
     * @param curAct
     * @param orderNo 订单号
     */
    public static void start(Activity curAct, String orderNo) {
        Intent intent = new Intent(curAct, PayFailAct.class);
        intent.putExtra("orderNo", orderNo);
        ViewUtils.startActivity(intent, curAct);
    }

    public static void start(Activity curAct, String orderNo, int type) {
        Intent intent = new Intent(curAct, PayFailAct.class);
        intent.putExtra("orderNo", orderNo);
        intent.putExtra("type", type);
        ViewUtils.startActivity(intent, curAct);
    }

    public static void start(Activity curAct, String orderNo, String desc) {
        Intent intent = new Intent(curAct, PayFailAct.class);
        intent.putExtra("orderNo", orderNo);
        intent.putExtra("desc", desc);
        ViewUtils.startActivity(intent, curAct);
    }

    private void initViews() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        if("takeout_fail".equals(desc)){
            ViewUtils.setText(findViewById(R.id.txt_title), "提现");
            ViewUtils.setText(findViewById(R.id.txt_pay_result), "提现失败");
            ViewUtils.setText(findViewById(R.id.txt_pay_desc), "提现失败，请稍后重试...");
            ViewUtils.setGone(findViewById(R.id.btn_check_order));
            ViewUtils.setGone(findViewById(R.id.btn_continue_shopping));
        } else {
            ViewUtils.setText(findViewById(R.id.txt_title), "订单支付");
            ViewUtils.setText(findViewById(R.id.txt_pay_result), "支付失败");
            if (Constants.TYPE_INSURANCE_PAY == type) {
                ViewUtils.setGone(findViewById(R.id.txt_pay_desc));
                ViewUtils.setGone(findViewById(R.id.btn_check_order));
                ViewUtils.setGone(findViewById(R.id.btn_continue_shopping));
            } else {
                ViewUtils.setText(findViewById(R.id.txt_pay_desc), "当前支付未成功，请重新支付...");
                findViewById(R.id.btn_check_order).setOnClickListener(this);
                findViewById(R.id.btn_continue_shopping).setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_continue_shopping:
                finish();
                break;
            case R.id.btn_check_order:
                MyOrderInfoAct.start(orderNo, PayFailAct.this, null);
                finish();
                break;
        }
    }

}
