package com.zxly.o2o.dialog;

import android.view.Gravity;
import android.view.View;

import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.ParameCallBackById;

/**
 * Created by Benjamin on 2015/6/11.
 */
public class TwoBtnActionDialog extends BaseDialog implements View.OnClickListener {
    private ParameCallBackById callBack;

    @Override
    protected void initView() {
        findViewById(R.id.dialog_btn_bottom).setOnClickListener(this);
        findViewById(R.id.dialog_btn_right).setOnClickListener(this);
        findViewById(R.id.dialog_btn_left).setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_two_btn_layout;
    }

    @Override
    public void onClick(View v) {
        this.callBack.onCall(v.getId(), null);
        dismiss();

    }

    @Override
    protected boolean isLimitHeight() {
        return true;
    }

    public void show(ParameCallBackById callBack) {
        super.show();
        this.callBack = callBack;
    }

    @Override
    public int getGravity() {

        return Gravity.BOTTOM;
    }
}
