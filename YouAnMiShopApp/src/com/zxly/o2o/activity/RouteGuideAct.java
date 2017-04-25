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
public class RouteGuideAct extends BasicAct {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_route_guide);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public static void start(Activity curAct)
    {
        Intent intent=new Intent();
        intent.setClass(curAct, RouteGuideAct.class);
        ViewUtils.startActivity(intent, curAct);
    }
}
