package com.zxly.o2o.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.zxly.o2o.activity.VerifyMobileAct;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ViewUtils;

public class VerifySelectFragment extends BaseFragment implements OnClickListener {

    @Override
    protected void initView(Bundle bundle) {
        initView();
    }

    @Override
    protected void initView() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        ViewUtils.setText(findViewById(R.id.txt_title), "设置手机号码");
        findViewById(R.id.btn_select_mobile)
                .setOnClickListener(this);
        findViewById(R.id.btn_select_service)
                .setOnClickListener(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.win_verify_select;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                getActivity().finish();
                break;
            case R.id.btn_select_mobile:
                ((VerifyMobileAct)getActivity()).setPageShow(1);
                break;
            case R.id.btn_select_service:
                ((VerifyMobileAct)getActivity()).setPageShow(4);
                break;
        }
    }

}
