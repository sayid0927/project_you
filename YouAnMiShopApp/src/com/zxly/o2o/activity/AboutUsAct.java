package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zxly.o2o.application.AppController;
import com.zxly.o2o.application.Config;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.UmengUtil;
import com.zxly.o2o.util.ViewUtils;

/**
 * @author fengrongjian 2015-6-24
 * @description 关于柚安米
 */
public class AboutUsAct extends BasicAct implements
        View.OnClickListener {
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_about_us);
        context = this;
        initViews();
    }

    public static void start(Activity curAct) {
        Intent intent = new Intent(curAct, AboutUsAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

    private void initViews() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.img_aboutUs).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                ViewUtils.showToast("当前版本编号:"+Config.appSerialNo +"\n"+"接口地址:"+Config.dataBaseUrl);
//                +"\n"+"个推id:"+Config.id+"\n"+"个推key:"+Config.key+"\n"+"个推secret:"+Config.secret
                return false;
            }
        });
        ((TextView) findViewById(R.id.txt_title)).setText("关于柚安米");
        findViewById(R.id.btn_update_now).setOnClickListener(this);
        ViewUtils.setText(findViewById(R.id.txt_shop_version), "当前版本: V" + AppController.mVersionName);
        if (Config.serVerCode> AppController.mVersionCode) {
            ViewUtils.setText(findViewById(R.id.txt_check_version), "检测到新版本: V" + Config.serVerName);
            ViewUtils.setVisible(findViewById(R.id.btn_update_now));
        } else {
            ViewUtils.setText(findViewById(R.id.txt_check_version), "没有检测到新版本");
            ViewUtils.setGone(findViewById(R.id.btn_update_now));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
//            case R.id.btn_feedback:
//                FeedbackAct.start(AboutUsAct.this);
//                break;
            case R.id.btn_update_now:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(Config.appUpdateUrl));
                startActivity(intent);
                UmengUtil.onEvent(AboutUsAct.this,new UmengUtil().ABOUT_UPDATE_CLICK,null);
                break;
        }
    }

}
