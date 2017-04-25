package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.model.IMUserInfoVO;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.IMGetUserDetailInfoRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.CircleImageView;
import com.zxly.o2o.view.ObservableScrollView;
import com.zxly.o2o.view.OnScrollChangedCallback;

/**
 * Created by hejun on 2016/10/8.
 */
public class DetailPersonalAct extends BasicAct implements  OnScrollChangedCallback {


    private IMGetUserDetailInfoRequest imGetUserDetailInfoRequest;
    private ObservableScrollView scrollView;
    private CircleImageView imgUserHead;
    private DetailPersonalAct context;
    private TextView txtNickName;
    private TextView txtSignature;
    private TextView txtNick;
    private TextView txtGender;
    private TextView txtBirthday;
    private TextView txtCity;
    private String curBirth;
    private TextView txt_register_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_personal_detail);
        context = this;
        long userId = getIntent().getLongExtra("userId", 0);
        loadPersonalInfo(userId);
    }

    public static void start(Activity curAct,long userId) {
        Intent intent = new Intent(curAct, DetailPersonalAct.class);
        intent.putExtra("userId",userId);
        ViewUtils.startActivity(intent, curAct);
    }

    private void loadPersonalInfo(long userId) {
        if (Account.user != null) {
            if (imGetUserDetailInfoRequest == null) {
                imGetUserDetailInfoRequest = new IMGetUserDetailInfoRequest(userId);
                imGetUserDetailInfoRequest
                        .setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                            @Override
                            public void onOK() {
                                if (imGetUserDetailInfoRequest.imUserInfoVO != null) {
                                    //之前版本是只在登录时才未用户基本信息赋值一次 现改为每次进入该页面刷新数据
                                    updateUserInfo(imGetUserDetailInfoRequest.imUserInfoVO);
                                }
                            }

                            @Override
                            public void onFail(int code) {

                            }
                        });
            }
            imGetUserDetailInfoRequest.start();
        }
    }

    private void updateUserInfo(IMUserInfoVO imUserInfoVO) {
        String signature = imUserInfoVO.getSignature();
        String nickname = imUserInfoVO.getNickname();
        byte gender = imUserInfoVO.getGender();
        Long birthday = imUserInfoVO.getBirthday();
        String provinceName = imUserInfoVO.getProvinceName();
        String cityName = imUserInfoVO.getCityName();
        String thumHeadUrl = imUserInfoVO.getThumHeadUrl();
        Long createTime = imUserInfoVO.getCreateTime();
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ViewUtils.setText(findViewById(R.id.txt_title), "个人信息");
        scrollView = (ObservableScrollView) findViewById(R.id.scroll_view);
        scrollView.setOnScrollChangedCallback(this);

        imgUserHead = (CircleImageView) findViewById(R.id.img_user_head);

        txtNickName = (TextView) findViewById(R.id.txt_user_name);
        ViewUtils.setText(txtNickName, imUserInfoVO.getMobilePhone());
        txtSignature = (TextView) findViewById(R.id.txt_user_signature);

        txtNick = (TextView) findViewById(R.id.txt_nick);
        txtGender = (TextView) findViewById(R.id.txt_gender);
        txtBirthday = (TextView) findViewById(R.id.txt_birthday);
        txtCity = (TextView) findViewById(R.id.txt_city);
        txt_register_time = (TextView) findViewById(R.id.txt_register_time);

        if (!StringUtil.isNull(signature)) {
            txtSignature.setText(signature);
        } else {
            txtSignature.setText("");
        }

        txtNick.setText(nickname);
        if (gender == 0) {
            gender = 1;
        }
        txtGender.setText(gender == 1 ? "男" : "女");
        if (birthday != 0) {
            curBirth = StringUtil.getDateByMillis(
                    birthday);
        } else {
            curBirth = "1900-01-01";
        }
        txtBirthday.setText(curBirth);
        txt_register_time.setText(EaseConstant.formatOrderLongTime(createTime));
        if (cityName != null && !cityName.equals(provinceName)) {
            ViewUtils.setText(txtCity, provinceName + "  " + cityName);
        } else {
            ViewUtils.setText(txtCity, provinceName);
        }

        if (StringUtil.isNull(thumHeadUrl)) {
            imgUserHead.setImageResource(R.drawable.default_head_big);
        } else {
            imgUserHead.setImageUrl(thumHeadUrl, R.drawable.default_head_big);
        }
    }



    @Override
    public void onScroll(int l, int t) {

    }
}
