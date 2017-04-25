package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.easemob.chatuidemo.HXApplication;
import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.HXHelper;
import com.easemob.chatuidemo.ui.ChatActivity;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.domain.EaseYAMUser;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.dialog.SelectChatDialog;
import com.zxly.o2o.fragment.InsuranceApplyFragment;
import com.zxly.o2o.fragment.InsuranceFragment;
import com.zxly.o2o.fragment.InsuranceIntroFragment;
import com.zxly.o2o.fragment.InsuranceStateFragment;
import com.zxly.o2o.fragment.InsuranceSuccessFragment;
import com.zxly.o2o.model.InsuranceOrder;
import com.zxly.o2o.model.InsuranceProduct;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.InsuranceOrderRequest;
import com.zxly.o2o.request.PersonalInitRequest;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

public class InsuranceAct extends BasicAct {
    private FragmentManager fragmentManager;
    private SelectChatDialog selectChatDialog;
    private static CallBack callBack;
    private LoadingView loadingView;
    private InsuranceIntroFragment insuranceIntroFragment;
    private InsuranceApplyFragment insuranceApplyFragment;
    private InsuranceSuccessFragment insuranceSuccessFragment;
    private InsuranceFragment insuranceFragment;
    private InsuranceStateFragment insuranceStateFragment;

    private InsuranceProduct insuranceProduct;
    /**
     * 保单状态
     * -1:没有购买,1:已申购,2:已取消,3:已拒单,4:待支付,5:支付超时,6:审核中,7:保障中,8:已退单,9:已过期
     */
    public int orderStatus;
    public long id, orderId;
    public String name;
    public String imgUrl;
    public String orderNo;
    public float price;
    /**
     * 来源类型
     * 0：列表进入，1：消息进入或支付成功页面进入
     */
    public int type;

    /*初始化聊天提示语*/
    private String tips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_fragment_activity_main);
        loadingView = (LoadingView) findViewById(R.id.view_loading);

        orderId = getIntent().getLongExtra("orderId", 0);
        type = getIntent().getIntExtra("type", 0);
        if (type == 1) {//从消息列表进来或者从支付成功页面进来
            if (orderId != 0) {
                loadData(orderId);
            } else {
                ViewUtils.showToast("订单id为0");
            }
        } else {//从延保产品列表进来
            insuranceProduct = (InsuranceProduct) getIntent().getSerializableExtra("insuranceProduct");
            id = insuranceProduct.getId();
            name = insuranceProduct.getName();
            price = insuranceProduct.getPrice();
            imgUrl = insuranceProduct.getImage();
            orderNo = insuranceProduct.getOrderNo();
            orderId = insuranceProduct.getOrderId();
            if (orderId != 0) {
                loadData(orderId);
            } else {
                orderStatus = insuranceProduct.getOrderStatus();
                setPageShow(orderStatus, null);
            }
        }
    }

    private void loadData(long orderId) {
        final InsuranceOrderRequest request = new InsuranceOrderRequest(orderId);
        request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                loadingView.onLoadingComplete();
                InsuranceOrder insuranceOrder = request.getInsuranceOrder();
                orderStatus = insuranceOrder.getOrderStatus();
                price = insuranceOrder.getPrice();

                InsuranceProduct insuranceProduct = request.getInsuranceProduct();
                id = insuranceProduct.getId();
                name = insuranceProduct.getName();
                orderNo = insuranceProduct.getOrderNo();

                setPageShow(orderStatus, null);
            }

            @Override
            public void onFail(int code) {
                loadingView.onLoadingFail();
            }
        });
        loadingView.startLoading();
        request.start(this);
    }

    public void changeCurrentState(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setPageShow(int index, Bundle bundle) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 0://接收"知道了"跳转
            case -1://没有购买
            case 2://已取消
                if (insuranceIntroFragment == null) {
                    insuranceIntroFragment = new InsuranceIntroFragment();
                    insuranceIntroFragment.setArguments(bundle);
                    transaction.add(R.id.fragment_main, insuranceIntroFragment);
                } else {
                    transaction.show(insuranceIntroFragment);
                }
                break;
            case 1://已申购
                if (bundle != null) {
                    orderId = bundle.getLong("orderId");
                }
            case 3://已拒单
            case 5://支付超时
            case 8://已退单
                if (insuranceStateFragment == null) {
                    insuranceStateFragment = new InsuranceStateFragment();
                    transaction.add(R.id.fragment_main, insuranceStateFragment);
                } else {
                    transaction.show(insuranceStateFragment);
                }
                break;
            case 4://待支付
                if (insuranceSuccessFragment == null) {
                    insuranceSuccessFragment = new InsuranceSuccessFragment();
                    transaction.add(R.id.fragment_main, insuranceSuccessFragment);
                } else {
                    transaction.show(insuranceSuccessFragment);
                }
                break;
            case 6://审核中
            case 7://保障中
            case 9://已过期
                if (insuranceFragment == null) {
                    insuranceFragment = new InsuranceFragment();
                    transaction.add(R.id.fragment_main, insuranceFragment);
                } else {
                    transaction.show(insuranceFragment);
                }
                break;
            case 10://跳转到我要买页面
                if (insuranceApplyFragment == null) {
                    insuranceApplyFragment = new InsuranceApplyFragment();
                    transaction.add(R.id.fragment_main, insuranceApplyFragment);
                } else {
                    transaction.show(insuranceApplyFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (insuranceIntroFragment != null) {
            transaction.hide(insuranceIntroFragment);
        }
        if (insuranceApplyFragment != null) {
            transaction.hide(insuranceApplyFragment);
        }
        if (insuranceSuccessFragment != null) {
            transaction.hide(insuranceSuccessFragment);
        }
        if (insuranceStateFragment != null) {
            transaction.hide(insuranceStateFragment);
        }
        if (insuranceFragment != null) {
            transaction.hide(insuranceFragment);
        }
    }

    public static void start(Activity curAct, long orderId) {
        Intent intent = new Intent(curAct, InsuranceAct.class);
        intent.putExtra("orderId", orderId);
        intent.putExtra("type", 1);
        ViewUtils.startActivity(intent, curAct);
    }

    public static void start(Activity curAct, InsuranceProduct insuranceProduct) {
        Intent intent = new Intent(curAct, InsuranceAct.class);
        intent.putExtra("insuranceProduct", insuranceProduct);
        ViewUtils.startActivity(intent, curAct);
    }

    @Override
    public void finish() {
        super.finish();
        callBack = null;
    }

    public void insuranceService(){
        if (selectChatDialog == null) {
            selectChatDialog = new SelectChatDialog();
        }

        if (Account.user != null) {
            HXHelper.getInstance().getYAMContactList();

            if (Account.user.getBelongId() != 0) {
                goToChat();
            } else {
                PersonalInitRequest personalInitRequest = new PersonalInitRequest(Account.user
                        .getId(), Config.shopId);
                personalInitRequest.setOnResponseStateListener(
                        new BaseRequest.ResponseStateListener() {
                            @Override
                            public void onOK() {
                                if (Account.user.getBelongId() != 0) {
                                    goToChat();
                                } else {
                                    selectChatDialog.show();
                                }
                            }

                            @Override
                            public void onFail(int code) {

                            }
                        });
                personalInitRequest.start();
            }
        } else {
            LoginAct.start(this);
        }
    }

    private void goToChat() {
        String chatUserId =
                HXApplication.getInstance().parseUserFromID(Account.user.getBelongId(),
                        HXConstant.TAG_SHOP);
        EaseYAMUser user = HXHelper.getInstance().getUserInfo(chatUserId);

        if(user!=null) {
            String name = user.getFirendsUserInfo().getNickname();
            if (TextUtils.isEmpty(name)) {
                name = user.getFirendsUserInfo().getUserName();
            }
            chatUserId = new StringBuffer("").append(name).append("#")
                    .append(Account.user.getBelongId()).append("#").append(chatUserId)
                    .toString();
            EaseConstant.startActivityNormalWithStringForResult(ChatActivity.class, this,
                    chatUserId,
                    EaseConstant.EXTRA_USER_ID);
        }else{
            if(tips==null) {
                tips = "正在初始化聊天环境,请稍候...";
                ViewUtils.showToast(tips);
                AppController.getInstance().checkIsNeedUpdateContact(true);
            }
        }
    }

}
