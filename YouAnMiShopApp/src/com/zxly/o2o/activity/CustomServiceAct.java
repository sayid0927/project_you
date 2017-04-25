package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.AppLog;
import com.zxly.o2o.util.ViewUtils;

/**
 * @author fengrongjian 2016-3-16
 * @description 客服信息
 */
public class CustomServiceAct extends BasicAct implements
        View.OnClickListener {
    private String phone = "15013509877";
    private TextView txtPhone, txtWxPublicNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_custom_service);
        initViews();
    }

    public static void start(Activity curAct) {
        Intent intent = new Intent(curAct, CustomServiceAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

    private void initViews() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        ((TextView) findViewById(R.id.txt_title)).setText("客服信息");
        findViewById(R.id.btn_call).setOnClickListener(this);
        txtPhone = (TextView) findViewById(R.id.txt_phone);
        txtWxPublicNo = (TextView) findViewById(R.id.txt_wx_public);
        fillActiveViews(2, 0);
    }

    private View[] mActiveViews = new View[0];
    void fillActiveViews(int childCount, int firstActivePosition) {
        mActiveViews = new View[childCount];
        AppLog.e("---mActiveViews.length a---" + mActiveViews.length);
        final View[] activeViews = mActiveViews;
        for (int i = 0; i < childCount; i++) {
            View child = new View(getApplicationContext());
            activeViews[i] = child;
        }
        AppLog.e("---mActiveViews.length b---" + mActiveViews.length);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_call:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                startActivity(intent);
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

}
