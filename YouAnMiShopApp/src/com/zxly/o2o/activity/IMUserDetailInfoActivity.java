package com.zxly.o2o.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.easemob.chatuidemo.HXApplication;
import com.easemob.chatuidemo.HXHelper;
import com.easemob.chatuidemo.ui.ChatActivity;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.adapter.EaseContactAdapter;
import com.easemob.easeui.model.IMUserInfoVO;
import com.easemob.easeui.ui.EaseBaseActivity;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.IMGetContactListRequest;
import com.zxly.o2o.request.IMGetUserDetailInfoRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ViewUtils;

/**
 * Created by Administrator on 2016/1/4.
 */
public class IMUserDetailInfoActivity extends EaseBaseActivity implements View.OnClickListener {
    private long userID;
    private IMGetUserDetailInfoRequest imGetUserDetailInfoRequest;
    private Button sendMessageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.im_user_detail_with_usermark);
        userID = getIntent().getLongExtra(EaseConstant.EXTRA_USER_ID, 0);
        setUpActionBar("个人信息");
        initView();
        loadData();
    }

    private void initView() {
        sendMessageBtn = (Button) findViewById(R.id.btn_send_message);
        if (EaseConstant.currentUser.getFirendsUserInfo().getId() == userID) {
            sendMessageBtn.setVisibility(View.GONE);
        } else {
            sendMessageBtn.setOnClickListener(this);
        }
    }

    private void loadData() {
        if (Account.user != null) {
            if(imGetUserDetailInfoRequest==null) {
                imGetUserDetailInfoRequest = new IMGetUserDetailInfoRequest(userID);
                imGetUserDetailInfoRequest
                        .setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                            @Override
                            public void onOK() {
                                if (imGetUserDetailInfoRequest.imUserInfoVO != null) {
                                    setDetail(imGetUserDetailInfoRequest.imUserInfoVO);
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

    private void setDetail(IMUserInfoVO imUserInfoVO) {


        findViewById(R.id.mark_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(IMUserDetailInfoActivity.this, UserSetMarkInfoAct.class);
                intent.putExtra("userId", userID);
                ViewUtils.startActivity(intent, IMUserDetailInfoActivity.this);
            }
        });

        //设置备注
        if (TextUtils.isEmpty(imUserInfoVO.getUserRemark().getRemarkName())) {
            ((TextView) findViewById(R.id.name)).setText
                    (TextUtils.isEmpty(imUserInfoVO.getNickname()) ? imUserInfoVO.getUserName() :
                            imUserInfoVO.getNickname());
            ((TextView) findViewById(R.id.name_below)).setText
                    (Html.fromHtml(
                            String.format(getResources().getString(R.string.im_user_detail_name_below_shop),
                                    imUserInfoVO.getUserName())));
        } else {
            ((TextView) findViewById(R.id.name)).setText
                    (imUserInfoVO.getUserRemark().getRemarkName());
            ((TextView) findViewById(R.id.name_below)).setText
                    (Html.fromHtml(String.format(getResources().getString(R.string
                                    .im_user_detail_name_below_nick_shop),
                            imUserInfoVO.getUserName(),
                            imUserInfoVO.getNickname())));
        }

        if (TextUtils.isEmpty(imUserInfoVO.getUserRemark().getDescription())) {
            ((TextView) findViewById(R.id.set_mark_tag)).setText("设置备注");
        } else {
            ((TextView) findViewById(R.id.set_mark_tag)).setText("备注        ");
            ((TextView) findViewById(R.id.mark)).setText(imUserInfoVO.getUserRemark().getDescription());

        }

        //set userhead image
        EaseConstant.setImage((NetworkImageView) findViewById(R.id.head_image),
                imUserInfoVO.getThumHeadUrl(), R.drawable.ease_default_avatar, null);

        //set location
        if(!TextUtils.isEmpty(imUserInfoVO.getProvinceName())||!TextUtils.isEmpty(imUserInfoVO.getCityName())) {
            ((TextView) findViewById(R.id.location)).setText
                    (String.format(getString(R.string.im_user_detail_location),
                            imUserInfoVO.getProvinceName(), imUserInfoVO.getCityName()));
            findViewById(R.id.location).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.location).setVisibility(View.GONE);
        }


        //set birthday
        ((TextView) findViewById(R.id.birthday)).setText
                (EaseConstant.formatOrderTime(imUserInfoVO.getBirthday()));


        //set signature
        ((TextView) findViewById(R.id.signature)).setText(
                imUserInfoVO.getSignature());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_message:
                String chatUserId = HXApplication.getInstance().parseUserFromID(userID, "_2");
                EaseConstant.startActivityNormalWithStringForResult(ChatActivity.class,
                        IMUserDetailInfoActivity.this, chatUserId,
                        EaseConstant.EXTRA_USER_ID);
                finish();
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (UserSetMarkInfoAct.isRemarkOK) { //修改备注成功回调
            UserSetMarkInfoAct.isRemarkOK=false;
            loadData();

            //刷新联系人
            EaseContactAdapter.unRegistList.clear();
            final IMGetContactListRequest iMGetContactListRequest= new IMGetContactListRequest(Account.user
                    .getShopId());
            iMGetContactListRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                @Override
                public void onOK() {
                    iMGetContactListRequest.isLoadingContact=false;
                }

                @Override
                public void onFail(int code) {
                    iMGetContactListRequest.isLoadingContact=false;
                }
            });
            iMGetContactListRequest.start();

        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 110&&resultCode==110) { //修改备注成功回调
//            loadData();
//
//            //刷新联系人
//            EaseContactAdapter.unRegistList.clear();
//            final IMGetContactListRequest iMGetContactListRequest= new IMGetContactListRequest(Account.user
//                    .getShopId());
//            iMGetContactListRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
//                @Override
//                public void onOK() {
//                    iMGetContactListRequest.isLoadingContact=false;
//                }
//
//                @Override
//                public void onFail(int code) {
//                    iMGetContactListRequest.isLoadingContact=false;
//                }
//            });
//            iMGetContactListRequest.start();
//
//        }
//    }
}
