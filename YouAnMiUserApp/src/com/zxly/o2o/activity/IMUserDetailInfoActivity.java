package com.zxly.o2o.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.easemob.chat.EMChatRoom;
import com.easemob.chat.EMContactManager;
import com.easemob.chatuidemo.HXApplication;
import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.HXHelper;
import com.easemob.chatuidemo.db.DemoDBManager;
import com.easemob.chatuidemo.ui.ChatActivity;
import com.easemob.chatuidemo.ui.ContactListFragment;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.controller.EaseUI;
import com.easemob.easeui.model.IMUserInfoVO;
import com.easemob.easeui.request.EaseAddToBlackRequest;
import com.easemob.easeui.request.EaseDeleteBlackRequest;
import com.easemob.easeui.request.HXNormalRequest;
import com.easemob.easeui.ui.EaseBaseMyListPageActivity;
import com.easemob.easeui.widget.EaseMyFlipperView;
import com.easemob.easeui.widget.easepullrefresh.EasePullToRefreshListView;
import com.easemob.exceptions.EaseMobException;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.adapter.ForumCommunityAdapter;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.dialog.VerifyDialog;
import com.zxly.o2o.model.ShopTopic;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.IMGetUserInfoRequest;
import com.zxly.o2o.request.IMUserDetailTopicsRequest;
import com.zxly.o2o.request.MyCircleRequest;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.ViewUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/21.
 */
public class IMUserDetailInfoActivity extends EaseBaseMyListPageActivity implements
        AdapterView.OnItemClickListener {
    private long userId;
    private IMGetUserInfoRequest getUserInfoRequest;
    private ActionBar actionBar;
    private boolean isNeedAdd;
    private String title;
    private TextView addToBlack;
    private final int ISBACK = 2;
    private final int NOTBACK = 1;

    private HXNormalRequest blackRequest;
    private IMUserDetailTopicsRequest userTopicsRequest;
    private ForumCommunityAdapter userDetailAdapter;
    private View head;
    private byte isShop;
    private View sendMessageBtn;
    private int isFromCircle;   //1:from circle 2:chat
    private String tag;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.ease_pull_to_refresh_layout);

        AppController.addAct(this);

        page = 1; //init pageIndex

        userId = getIntent().getLongExtra(EaseConstant.EXTRA_USER_ID, 0);

        title = getIntent().getStringExtra("title");

        isFromCircle = getIntent().getIntExtra("isFromCircle", 0);

        isNeedAdd = getIntent().getBooleanExtra("isNeedAdd", false);

        isShop = getIntent().getByteExtra("isShop", (byte) 0);

        tag = getIntent().getStringExtra("Tag");

        actionBar = setUpActionBar(title);

        setFlipper();

        head = LayoutInflater.from(this).inflate(R.layout.im_user_info_activity_head, null);

        initListView();

        loadData();
    }

    private void initListView() {
        setListView();
        mListView.addH(head);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mListView.getLayoutParams();
        lp.setMargins(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -11, AppController
                .displayMetrics), 0, 0);
        mListView.setLayoutParams(lp);
        mListView.setDivideHeight(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, AppController
                        .displayMetrics));

        userDetailAdapter = new ForumCommunityAdapter(this,isShop,"详情");
        mListView.setAdapter(userDetailAdapter);

        mListView.setOnItemClickListener(this);
    }

    @Override
    protected void loadData() {
        if (getUserInfoRequest == null) {
            getUserInfoRequest = new IMGetUserInfoRequest(userId);
            getUserInfoRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                @Override
                public void onOK() {
                    setHeadView(getUserInfoRequest.imUserInfoVO);
                    //get user's topics from server
                    loadUserTopics();

                }

                @Override
                public void onFail(int code) {
                    viewContainer.setDisplayedChild(EaseMyFlipperView.LOADFAIL);
                }
            });
        }
        getUserInfoRequest.start();
    }

    private void setHeadView(final IMUserInfoVO imUserInfoVO) {
        //set username
        ((TextView) head.findViewById(R.id.name)).setText(imUserInfoVO.getNickname());

        if(imUserInfoVO.getId()==Account.user.getId()) {
            ((TextView) head.findViewById(R.id.whos_topic)).setText("我的发帖");
        }

        //set userhead image
        EaseConstant.setImage((NetworkImageView) head.findViewById(R.id.head_image),
                imUserInfoVO.getThumHeadUrl(), R.drawable.ease_default_avatar, null);

        ((ImageView) head.findViewById(R.id.gender)).setImageResource(
                imUserInfoVO.getGender() == 2 ? R.drawable
                        .ease_women_icon : R.drawable.ease_man_icon);

        //set name below
        ((TextView) head.findViewById(R.id.name_below))
                .setText(String.format(getResources().getString(R.string.user_detail_below),
                        imUserInfoVO.getUserName(), "\n" + imUserInfoVO.getProvinceName(),
                        imUserInfoVO.getCityName(),
                        EaseConstant
                                .formatOrderTime
                                        (imUserInfoVO.getBirthday())));

        //set signature
        if (!TextUtils.isEmpty(imUserInfoVO.getSignature())) {
            ((TextView) head.findViewById(R.id.signature))
                    .setText(String.format(getResources().getString(R.string
                            .signature), imUserInfoVO.getSignature()));
        }


        //set send message btn listener
        sendMessageBtn = head.findViewById(R.id.btn_send_message);
        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // demo中直接进入聊天页面，实际一般是进入用户详情页
                String tagStr;
                if (isFromCircle==1) {
                    tagStr = HXConstant.TAG_USER;
                } else if (tag != null) {
                    tagStr = tag;
                } else {
                    tagStr = getUserInfoRequest
                            .imUserInfoVO.getType() == 1 ? HXConstant.TAG_SHOP : HXConstant.TAG_USER;
                }

                String chatUserId = HXApplication.getInstance().parseUserFromID(userId,
                        tagStr);
                if (imUserInfoVO.getFriendInfo() == null && isNeedAdd) {
                    //还不是好友，需要添加好友标示、、
                    String name = imUserInfoVO.getNickname();
                    if (TextUtils.isEmpty(name)) {
                        name = imUserInfoVO.getUserName();
                    }
                    chatUserId = new StringBuffer("").append(name).append("#")
                            .append(userId).append("#").append(chatUserId)
                            .toString();
                }

                EaseConstant.startActivityNormalWithStringForResult(ChatActivity.class,
                        IMUserDetailInfoActivity.this, chatUserId,
                        EaseConstant.EXTRA_USER_ID);

                finish();
            }
        });

          /*是好友*/
        if (getUserInfoRequest.imUserInfoVO.getFriendInfo() != null) {
            if(isNeedAdd) {
            addToBlack = (TextView) actionBar.getCustomView().findViewById(R.id.tag_title_right);

            //init black button

                setAddToBlack();
            }
        } else {
            if (getUserInfoRequest
                    .imUserInfoVO.getId() == Account.user.getId()&&isFromCircle>0) {
                sendMessageBtn.setVisibility(View.GONE);
            } else if (getUserInfoRequest.imUserInfoVO.isCurrentShopUser()) {
                sendMessageBtn.setVisibility(View.VISIBLE);
            } else if (isFromCircle==1) {
                isNeedAdd = false;
                sendMessageBtn.setVisibility(View.GONE);
            } else {
                isNeedAdd = false;
            }
            //            if (getUserInfoRequest.imUserInfoVO.getType() == 1) {  //商户端用户
            //                isNeedAdd = false;
            //                if (getUserInfoRequest.imUserInfoVO.getShopId() != null && getUserInfoRequest
            //                        .imUserInfoVO.getShopId() != Config.shopId) {  //不是本门店
            //                    sendMessageBtn.setVisibility(View.GONE);
            //                }
            //
            //            } else if (getUserInfoRequest.imUserInfoVO.getType() == 2) {  //用户端用户
            //                //非本门店用户隐藏 sendMessageBtn
            //                if (getUserInfoRequest.imUserInfoVO.getRegistShopId() != Config.shopId || getUserInfoRequest
            //                        .imUserInfoVO.getId() == Account.user.getId()) {
            //                    sendMessageBtn.setVisibility(View.GONE);
            //                }
            //            }
        }

    }

    private void loadUserTopics() {
        userTopicsRequest = new IMUserDetailTopicsRequest(userId, page);
        userTopicsRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                if (userTopicsRequest.userTopics != null) {
                    if (page == 1) {
                        userDetailAdapter.clear();
                        viewContainer.setDisplayedChild(EaseMyFlipperView.LOADSUCCESSFUL, false);
                    }
                    userDetailAdapter.addItem(userTopicsRequest.userTopics, true);
                    mListView.onRefreshComplete();
                } else {
                    isLastPage = 1;
                }
            }

            @Override
            public void onFail(int code) {
                viewContainer.setDisplayedChild(EaseMyFlipperView.LOADFAIL);
                mListView.onRefreshComplete();
            }
        });
        userTopicsRequest.start();
    }

    private void setAddToBlack() {
        addToBlack.setVisibility(View.VISIBLE);
        addToBlack.setText(getUserInfoRequest.imUserInfoVO.getFriendInfo().getIsBlack()
                == NOTBACK ? "加入黑名单" : "移出黑名单");
        addToBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getUserInfoRequest.imUserInfoVO.getFriendInfo().getIsBlack() == NOTBACK) {
                    showVerifyDialog("加入黑名单，双方都不会再收到对方的消息");
                } else {
                    showVerifyDialog("移出黑名单，双方将继续收到对方的消息");
                }

            }
        });
    }

    private void showVerifyDialog(String s) {
        VerifyDialog verDialog = new VerifyDialog();
        verDialog.show(new CallBack() {

            @Override
            public void onCall() {
                if (getUserInfoRequest.imUserInfoVO.getFriendInfo().getIsBlack() == NOTBACK) {
                    blackRequest = new EaseAddToBlackRequest(userId);
                } else {
                    blackRequest = new EaseDeleteBlackRequest(userId);
                }
                blackRequest.setOnResponseStateListener(
                        new HXNormalRequest.ResponseStateListener() {
                            @Override
                            public void onOK() {
                                getUserInfoRequest.imUserInfoVO.getFriendInfo().setIsBlack(
                                        getUserInfoRequest.imUserInfoVO.getFriendInfo().getIsBlack()
                                                == NOTBACK ? ISBACK : NOTBACK);

                                String userName = HXApplication.getInstance().parseUserFromID(userId
                                        , HXConstant
                                        .TAG_USER);


                                if (getUserInfoRequest.imUserInfoVO.getFriendInfo().getIsBlack() == NOTBACK) {
                                    removeFromBlack(userName);
                                } else {
                                    addToBlackList(userName);
                                }


                                DemoDBManager.getInstance().updateYAMContactBlackStatus(
                                        getUserInfoRequest.imUserInfoVO.getFriendInfo()
                                                .getIsBlack(), String.valueOf(userId));

                                ContactListFragment.blackChangeStatu =
                                        getUserInfoRequest.imUserInfoVO.getFriendInfo().getIsBlack();
                            }

                            @Override
                            public void onFail(int code) {
                            }
                        });
                blackRequest.start();
            }

        }, s);
    }


    /*加入黑名单*/
    private void addToBlackList(final String userName) {
        addToBlack.setText("移出黑名单");
        //第二个参数如果为true，则把用户加入到黑名单后双方发消息时对方都收不到；false,则
        //我能给黑名单的中用户发消息，但是对方发给我时我是收不到的

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMContactManager.getInstance()
                            .addUserToBlackList(userName, true);//需异步处理
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    /*从黑名单中移出*/
    private void removeFromBlack(final String userName) {
        addToBlack.setText("加入黑名单");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMContactManager.getInstance()
                            .deleteUserFromBlackList(userName);//需异步处理
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (id >= 0) {
            MyCircleRequest.shopTopic = (ShopTopic) userDetailAdapter.getItem(position - 2);
            MyCircleThirdAct
                    .start(this, MyCircleRequest.shopTopic.getIsShopTopic(), MyCircleRequest.shopTopic,"详情");

        }
    }
}
