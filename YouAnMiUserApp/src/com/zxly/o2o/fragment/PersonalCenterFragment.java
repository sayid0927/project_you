package com.zxly.o2o.fragment;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.easemob.easeui.EaseConstant;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.DiscountListAct;
import com.zxly.o2o.activity.FeedbackAct;
import com.zxly.o2o.activity.HomeAct;
import com.zxly.o2o.activity.InsuranceListAct;
import com.zxly.o2o.activity.LoginAct;
import com.zxly.o2o.activity.MakecommissionAct;
import com.zxly.o2o.activity.MyOrderAct;
import com.zxly.o2o.activity.PayMyAccountAct;
import com.zxly.o2o.activity.PersonalHomepageAct;
import com.zxly.o2o.activity.PromotionBindAct;
import com.zxly.o2o.activity.PromotionDetailAct;
import com.zxly.o2o.activity.RefundmentListActivity;
import com.zxly.o2o.activity.SettingAct;
import com.zxly.o2o.activity.ShopCartAct;
import com.zxly.o2o.activity.ShowDefineGetuiAct;
import com.zxly.o2o.activity.UserCollectedAct;
import com.zxly.o2o.activity.UserTopicAct;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.UserOrder;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.PersonalInitRequest;
import com.zxly.o2o.request.PromotionCheckRequest;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.DesityUtil;
import com.zxly.o2o.util.FragmentTabHandler;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.UMengAgent;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.CircleImageView;
import com.zxly.o2o.view.LoadingView;
import com.zxly.o2o.view.ObservableScrollView;
import com.zxly.o2o.view.OnScrollChangedCallback;

public class PersonalCenterFragment extends BaseFragment implements
        OnClickListener, OnScrollChangedCallback {
    private CircleImageView imgUserHead;
    private View btnUserInfo, viewUserInfo;
    private TextView txtUserName;
    private TextView txtUserBalance;
    private TextView btnLogin;
    private Activity curAct;
    private String lastThumbUrl = "";
    private FragmentTabHandler fragmentTabHandler;
    private TextView txtNickName;
    private TextView txtDaifukuan;//待付款数量
    private TextView txtDaishouhuo;//待收货数量
    private LoadingView loadingView;
    private ObservableScrollView scrollView;
    private View viewTitle;
    private int scrollHeight, screenHeight;

    @Override
    public void onResume() {
        super.onResume();
        fragmentTabHandler = ((HomeAct) getActivity()).fragmentContorler;
        if (3 == fragmentTabHandler.getCurrentTab()) {
            refreshUI();
        }
    }

    public void refreshUI() {
        if (!Account.hasLogin()) {
            imgUserHead.setImageResource(R.drawable.personal_default_head);
            ViewUtils.setGone(viewUserInfo);
            btnUserInfo.setClickable(false);
            ViewUtils.setVisible(btnLogin);

            ViewUtils.setText(txtUserBalance, "￥--");
            ViewUtils.setGone(txtDaifukuan);
            ViewUtils.setGone(txtDaishouhuo);
            ViewUtils.setGone(findViewById(R.id.img_guarantee_tip));
        } else {
            setUserInfo();
            loadData();

            ViewUtils.setVisible(viewUserInfo);
            btnUserInfo.setClickable(true);
            ViewUtils.setGone(btnLogin);

            if (PreferUtil.getInstance().getIsShowRenewGuide()) {
                ViewUtils.setVisible(findViewById(R.id.img_guarantee_tip));
            } else {
                ViewUtils.setGone(findViewById(R.id.img_guarantee_tip));
            }
        }
    }

    private void loadData() {
        final PersonalInitRequest personalInitRequest = new PersonalInitRequest(Account.user.getId(), Config.shopId);
        personalInitRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                float userBalance = personalInitRequest.getAccountBalance();
                ViewUtils.setTextPrice(txtUserBalance, userBalance);
                UserOrder userOrder = personalInitRequest.getUserOrder();
                if (userOrder.getWaitPay() > 0) {
                    ViewUtils.setVisible(txtDaifukuan);
                    ViewUtils.setText(txtDaifukuan, userOrder.getWaitPay());
                } else {
                    ViewUtils.setGone(txtDaifukuan);
                }
                if (userOrder.getWaitAffirm() > 0) {
                    ViewUtils.setVisible(txtDaishouhuo);
                    ViewUtils.setText(txtDaishouhuo, userOrder.getWaitAffirm());
                } else {
                    ViewUtils.setGone(txtDaishouhuo);
                }
            }

            @Override
            public void onFail(int code) {
            }
        });
        personalInitRequest.start(this);
    }

    private void setUserInfo() {
        String nickName = Account.user.getNickName();
        if (nickName != null) {
            ViewUtils.setText(txtNickName, nickName);
        }
        ViewUtils.setText(txtUserName, Account.user.getUserName());
        String thumHeadUrl = Account.user.getThumHeadUrl();
        if (StringUtil.isNull(thumHeadUrl)) {
            imgUserHead.setImageResource(R.drawable.personal_default_head);
        } else if (!lastThumbUrl.equals(thumHeadUrl)) {
            imgUserHead.setImageUrl(thumHeadUrl, R.drawable.personal_default_head);
            lastThumbUrl = thumHeadUrl;
        }
    }

    @Override
    protected void initView() {
        curAct = getActivity();
        loadingView = (LoadingView) findViewById(R.id.view_loading);
        scrollView = (ObservableScrollView) findViewById(R.id.scroll_view);
//        scrollView.setOnScrollChangedCallback(this);
        viewTitle = findViewById(R.id.view_title);
        viewTitle.setOnClickListener(this);
        findViewById(R.id.btn_user_shoppingcart).setOnClickListener(this);
        btnLogin = (TextView) findViewById(R.id.btn_login);
        btnUserInfo = findViewById(R.id.btn_user_info);
        viewUserInfo = findViewById(R.id.view_user_info);
        imgUserHead = (CircleImageView) findViewById(R.id.img_user_head);
        btnLogin.setOnClickListener(this);
        btnUserInfo.setOnClickListener(this);
        findViewById(R.id.btn_user_account).setOnClickListener(this);
        findViewById(R.id.btn_user_order).setOnClickListener(this);

        findViewById(R.id.btn_daifukuan).setOnClickListener(this);
        findViewById(R.id.btn_daifahuo).setOnClickListener(this);
        findViewById(R.id.btn_daishouhuo).setOnClickListener(this);
        findViewById(R.id.btn_user_refund).setOnClickListener(this);

        findViewById(R.id.btn_user_collect).setOnClickListener(this);
        findViewById(R.id.btn_user_topic).setOnClickListener(this);
        findViewById(R.id.btn_earn_commission).setOnClickListener(this);
        findViewById(R.id.btn_user_guarantees).setOnClickListener(this);
        findViewById(R.id.btn_my_salesman).setOnClickListener(this);
        findViewById(R.id.btn_promotion).setOnClickListener(this);
        findViewById(R.id.btn_feedback).setOnClickListener(this);
        findViewById(R.id.btn_setting).setOnClickListener(this);
        findViewById(R.id.btn_ddyh).setOnClickListener(this);

        txtNickName = (TextView) findViewById(R.id.txt_user_nickname);
        txtUserName = (TextView) findViewById(R.id.txt_user_name);
        txtUserBalance = (TextView) findViewById(R.id.txt_user_balance);
        txtDaifukuan = (TextView) findViewById(R.id.txt_daifukuan_count);
        txtDaishouhuo = (TextView) findViewById(R.id.txt_daishouhuo_count);
    }

    @Override
    public void onClick(View v) {
        if (Account.hasLogin()) {
            Intent intent;
            switch (v.getId()) {
                case R.id.view_title:
                    break;
                case R.id.btn_user_shoppingcart:
                    ShopCartAct.start(curAct);
                    break;
                case R.id.btn_login:
                    LoginAct.start(curAct);
                    break;
                case R.id.btn_user_info:
                    PersonalHomepageAct.start(curAct);
                    break;
                case R.id.btn_user_account:
                    PayMyAccountAct.start(curAct);
                    break;
                case R.id.btn_user_order:
                    MyOrderAct.start(Constants.ORDER_REQUEST_ALL, curAct);
                    UMengAgent.onEvent(curAct, UMengAgent.personal_home_order);
                    break;
                case R.id.btn_daifukuan:
                    MyOrderAct.start(Constants.ORDER_REQUEST_WAIT_ROR_PAY, curAct);
                    break;
                case R.id.btn_daifahuo:
                    MyOrderAct.start(Constants.ORDER_REQUEST_WAIT_ROR_TAKE, curAct);
                    break;
                case R.id.btn_daishouhuo:
                    MyOrderAct.start(Constants.ORDER_REQUEST_SUCCESS, curAct);
                    break;
                case R.id.btn_user_refund:
                    ViewUtils.startActivity(new Intent(curAct,
                            RefundmentListActivity.class), curAct);
                    break;
                case R.id.btn_user_collect:
                    UserCollectedAct.start(curAct);
                    UMengAgent.onEvent(curAct, UMengAgent.personal_home_collect);
                    break;
                case R.id.btn_user_topic:
                    UserTopicAct.start(curAct);
                    UMengAgent.onEvent(curAct, UMengAgent.personal_home_topic);
                    break;
                case R.id.btn_ddyh:
                    DiscountListAct.start(curAct);
                    break;
                case R.id.btn_earn_commission:
                    intent = new Intent(curAct, MakecommissionAct.class);
                    startActivity(intent);
                    break;
                case R.id.btn_user_guarantees:
                    InsuranceListAct.start(curAct);
                    UMengAgent.onEvent(curAct, UMengAgent.personal_home_renew);
                    break;
                case R.id.btn_my_salesman:
                    if(Account.user.getBelongId() != 0) {
                        EaseConstant.startIMUserDetailInfo(Account.user.getBelongId(), true, curAct, "专属业务员",
                                0, null);
                    }
                    break;
                case R.id.btn_promotion:
                    checkPromotion();
                    break;
                case R.id.btn_feedback:
                    FeedbackAct.start(curAct);
                    UMengAgent.onEvent(curAct, UMengAgent.personal_home_feedback);
                    break;
                case R.id.btn_setting:
                    SettingAct.start(curAct);
                    break;
            }
        } else {
            LoginAct.start(curAct);
        }
    }

    @Override
    protected int layoutId() {
        return R.layout.tag_personal_center;
    }

    private void checkPromotion() {
        final PromotionCheckRequest promotionCheckRequest = new PromotionCheckRequest(Account.user.getId());
        promotionCheckRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                loadingView.onLoadingComplete();
                Intent intent = new Intent(curAct, PromotionDetailAct.class);
                ViewUtils.startActivity(intent, curAct);
            }

            @Override
            public void onFail(int code) {
                loadingView.onLoadingComplete();
                if(code == 40002) {
                    Intent intent = new Intent(curAct, PromotionBindAct.class);
                    ViewUtils.startActivity(intent, curAct);
                }
            }
        });
        loadingView.startLoading();
        promotionCheckRequest.start(this);
    }

    @Override
    public void onScroll(int l, int t) {
        screenHeight = Config.screenHeight - DesityUtil.dp2px(getActivity(), 60) - DesityUtil.dp2px(getActivity(), 25);
        scrollHeight = scrollView.getChildAt(0).getMeasuredHeight() - screenHeight;
        float alpha = (float) t / scrollHeight;
        viewTitle.setAlpha(alpha);
    }
}
