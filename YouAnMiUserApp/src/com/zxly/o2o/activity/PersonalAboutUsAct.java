package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.zxly.o2o.config.Config;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.ViewUtils;

/**
 * @author fengrongjian 2015-6-4
 * @description 关于我们
 */
public class PersonalAboutUsAct extends BasicAct implements
        View.OnClickListener {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_personal_about_us);
        context = this;
        initViews();
    }

    public static void start(Activity curAct) {
        Intent intent = new Intent(curAct, PersonalAboutUsAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

    private void initViews() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        ViewUtils.setText(findViewById(R.id.txt_title), "软件更新");
        findViewById(R.id.btn_update_now).setOnClickListener(this);
        findViewById(R.id.img_logo).setBackgroundResource(R.drawable.logo);
        findViewById(R.id.img_logo).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                ViewUtils.showToast("当前版本编号:" + Config.appSerialNo+"\n"+"接口地址:"+Config.dataBaseUrl);
//                +"\n"+"个推id:"+Config.id+"\n"+"个推key:"+Config.key+"\n"+"个推secret:"+Config.secret
                return false;
            }
        });
        ViewUtils.setText(findViewById(R.id.txt_shop_name), getResources().getString(R.string.app_name));

        if(Config.shopVersionCode!=-1){
            ViewUtils.setText(findViewById(R.id.txt_shop_version), "版本: V" + AppController.mVersionName + "." + Config.shopVersionCode);
        }else {
            ViewUtils.setText(findViewById(R.id.txt_shop_version), "版本: V" + AppController.mVersionName);
        }

        if (Config.versionCodeCur > AppController.mVersionCode) {
            ViewUtils.setText(findViewById(R.id.txt_check_version), "发现新版本: V" + Config.serverVersionName);
            ViewUtils.setVisible(findViewById(R.id.btn_update_now));
        } else {
            ViewUtils.setText(findViewById(R.id.txt_check_version), "没有发现新版本");
            ViewUtils.setGone(findViewById(R.id.btn_update_now));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_update_now:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(Config.AppUpdateUrl));
                startActivity(intent);
                break;
        }
    }

}
