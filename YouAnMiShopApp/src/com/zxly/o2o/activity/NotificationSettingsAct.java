package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.ViewUtils;

/**
 * @author fengrongjian 2015-6-24
 * @description 通知设置
 */
public class NotificationSettingsAct extends BasicAct implements View.OnClickListener {
    private CheckBox cbxAll, cbxLogout, cbxSleep, cbxOrder, cbxMoney, cbxSystem, cbxFeedback;
    private TextView viewCoverLogout, viewCoverSleep, viewCoverOrder, viewCoverMoney, viewCoverSystem, viewCoverFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_notification_settings);
        initViews();
    }

    public static void start(Activity curAct) {
        Intent intent = new Intent(curAct, NotificationSettingsAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

    private void initViews() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        ((TextView) findViewById(R.id.txt_title)).setText("通知设置");
        viewCoverLogout = (TextView) findViewById(R.id.view_cover_logout);
        viewCoverSleep = (TextView) findViewById(R.id.view_cover_sleep);
        viewCoverOrder = (TextView) findViewById(R.id.view_cover_order);
        viewCoverMoney = (TextView) findViewById(R.id.view_cover_money);
        viewCoverSystem = (TextView) findViewById(R.id.view_cover_system);
        viewCoverFeedback = (TextView) findViewById(R.id.view_cover_feedback);

        cbxAll = (CheckBox) findViewById(R.id.cbx_all);
        cbxLogout = (CheckBox) findViewById(R.id.cbx_logout);
        cbxSleep = (CheckBox) findViewById(R.id.cbx_sleep);
        cbxOrder = (CheckBox) findViewById(R.id.cbx_order);
        cbxMoney = (CheckBox) findViewById(R.id.cbx_money);
        cbxSystem = (CheckBox) findViewById(R.id.cbx_system);
        cbxFeedback = (CheckBox) findViewById(R.id.cbx_feedback);

        cbxAll.setOnCheckedChangeListener(checkedChangeListener);
        cbxLogout.setOnCheckedChangeListener(checkedChangeListener);
        cbxSleep.setOnCheckedChangeListener(checkedChangeListener);
        cbxOrder.setOnCheckedChangeListener(checkedChangeListener);
        cbxMoney.setOnCheckedChangeListener(checkedChangeListener);
        cbxSystem.setOnCheckedChangeListener(checkedChangeListener);
        cbxFeedback.setOnCheckedChangeListener(checkedChangeListener);

        boolean isAllChecked = PreferUtil.getInstance().getNotifyAll();
        cbxAll.setChecked(isAllChecked);
        cbxLogout.setChecked(PreferUtil.getInstance().getNotifyLogout());
        cbxSleep.setChecked(PreferUtil.getInstance().getNotifySleep());
        cbxOrder.setChecked(PreferUtil.getInstance().getNotifyOrder());
        cbxMoney.setChecked(PreferUtil.getInstance().getNotifyMoney());
        cbxSystem.setChecked(PreferUtil.getInstance().getNotifySystem());
        cbxFeedback.setChecked(PreferUtil.getInstance().getNotifyFeedback());

        if (isAllChecked) {
            enableOthers();
        } else {
            disableOthers();
        }
    }

    private void enableOthers() {
        ViewUtils.setGone(viewCoverLogout);
        ViewUtils.setGone(viewCoverSleep);
        ViewUtils.setGone(viewCoverOrder);
        ViewUtils.setGone(viewCoverMoney);
        ViewUtils.setGone(viewCoverSystem);
        ViewUtils.setGone(viewCoverFeedback);
        cbxLogout.setEnabled(true);
        cbxSleep.setEnabled(true);
        cbxOrder.setEnabled(true);
        cbxMoney.setEnabled(true);
        cbxSystem.setEnabled(true);
        cbxFeedback.setEnabled(true);
    }

    private void disableOthers() {
        ViewUtils.setVisible(viewCoverLogout);
        ViewUtils.setVisible(viewCoverSleep);
        ViewUtils.setVisible(viewCoverOrder);
        ViewUtils.setVisible(viewCoverMoney);
        ViewUtils.setVisible(viewCoverSystem);
        ViewUtils.setVisible(viewCoverFeedback);
        cbxLogout.setEnabled(false);
        cbxSleep.setEnabled(false);
        cbxOrder.setEnabled(false);
        cbxMoney.setEnabled(false);
        cbxSystem.setEnabled(false);
        cbxFeedback.setEnabled(false);
    }

    CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.cbx_all:
                    PreferUtil.getInstance().setNotifyAll(isChecked);
                    if (isChecked) {
                        enableOthers();
                    } else {
                        disableOthers();
                    }
                    break;
                case R.id.cbx_logout:
                    PreferUtil.getInstance().setNotifyLogout(isChecked);
                    break;
                case R.id.cbx_sleep:
                    PreferUtil.getInstance().setNotifySleep(isChecked);
                    break;
                case R.id.cbx_order:
                    PreferUtil.getInstance().setNotifyOrder(isChecked);
                    break;
                case R.id.cbx_money:
                    PreferUtil.getInstance().setNotifyMoney(isChecked);
                    break;
                case R.id.cbx_system:
                    PreferUtil.getInstance().setNotifySystem(isChecked);
                    break;
                case R.id.cbx_feedback:
                    PreferUtil.getInstance().setNotifyFeedback(isChecked);
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }
}
