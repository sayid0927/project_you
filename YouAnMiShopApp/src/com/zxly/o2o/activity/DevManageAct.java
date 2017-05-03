package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ViewUtils;

/**
 * Created by dsnx on 2016/6/28.
 */
public class DevManageAct extends BasicAct implements View.OnClickListener{
    private View btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_dev_manage);
        findViewById(R.id.txt_route).setOnClickListener(this);
        findViewById(R.id.txt_cdb).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);

    }
    public static  void start(Activity act)
    {
        Intent intent=new Intent();
        intent.setClass(act,DevManageAct.class);
        ViewUtils.startActivity(intent,act);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.txt_route:
                RouteGuideAct.start(this);
                break;
            case R.id.txt_cdb:
                CdbGuideAct.start(this);
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }
}
