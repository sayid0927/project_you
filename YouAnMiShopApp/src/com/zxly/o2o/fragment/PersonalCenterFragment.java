package com.zxly.o2o.fragment;

import android.view.View;
import android.widget.TextView;

import com.easemob.chatuidemo.HXApplication;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.model.GeTuiConversation;
import com.easemob.easeui.request.GetuiTypeDataRequest;
import com.easemob.easeui.request.HXNormalRequest;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.DevManageAct;
import com.zxly.o2o.activity.FeedbackAct;
import com.zxly.o2o.activity.FragmentListAct;
import com.zxly.o2o.activity.H5CommonAct;
import com.zxly.o2o.activity.MainActivity;
import com.zxly.o2o.activity.PayMyAccountAct;
import com.zxly.o2o.activity.PersonalHomepageAct;
import com.zxly.o2o.activity.SettingAct;
import com.zxly.o2o.activity.YamCollegeListAct;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.application.Config;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.GetDeviceStatueRequest;
import com.zxly.o2o.request.PersonalInitRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.FragmentTabHandler;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.PhoneUtil;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.UmengUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.CircleImageView;
import com.zxly.o2o.view.LoadingView;

import java.util.List;

public class PersonalCenterFragment extends BaseFragment implements View.OnClickListener {
    private FragmentTabHandler fragmentTabHandler;
    private CircleImageView imgUserHead;
    private TextView txtNickName, txtStaffId, txtShopName, txtUserBalance, txtMsgCount, btnTitleRight;
    private View btnDiscountList, btnTask;
    private String lastThumbUrl = "";
    private LoadingView loadingView;
    private PersonalInitRequest personalInitRequest;
    private ParameCallBack _callBack;

    @Override
    protected void initView() {

        UmengUtil.onEvent(getActivity(), "home_my_enter",null);
        super.initView(null);
        ViewUtils.setGone(findViewById(R.id.btn_back));
        ViewUtils.setText(findViewById(R.id.txt_title), "我的");
        loadingView = (LoadingView) findViewById(R.id.view_loading);
        btnTitleRight = (TextView) findViewById(R.id.btn_top_right);
        ViewUtils.setVisible(btnTitleRight);
        btnTitleRight.setOnClickListener(this);
        imgUserHead = (CircleImageView) findViewById(R.id.img_user_head);
        txtNickName = (TextView) findViewById(R.id.txt_user_name);
        txtShopName = (TextView) findViewById(R.id.txt_shop_name);
        txtStaffId = (TextView) findViewById(R.id.txt_staff_id);//店员编号
        findViewById(R.id.btn_home_page).setOnClickListener(this);
        findViewById(R.id.btn_user_balance).setOnClickListener(this);
        txtUserBalance = (TextView) findViewById(R.id.txt_user_balance);
        findViewById(R.id.btn_messages).setOnClickListener(this);
        txtMsgCount = (TextView) findViewById(R.id.txt_message_count);//我的消息数
//        msgRedPoint = (RedPoint) findViewById(R.id.view_redPoint);
        findViewById(R.id.btn_yam_college).setOnClickListener(this);
        findViewById(R.id.btn_devices).setOnClickListener(this);
        btnDiscountList = findViewById(R.id.btn_discount_list);
        btnDiscountList.setOnClickListener(this);
        btnTask = findViewById(R.id.btn_task);
        btnTask.setOnClickListener(this);

        if (Account.user != null) {
            if (Account.user.getIsBoss() == 1) {
                ViewUtils.setGone(btnDiscountList);
            } else {
                ViewUtils.setVisible(btnDiscountList);
            }
            if (Account.user.getRoleType() == Constants.USER_TYPE_ADMIN) {
                ViewUtils.setGone(btnTask);
            } else {
                ViewUtils.setVisible(btnTask);
            }

        }
        findViewById(R.id.btn_feedback).setOnClickListener(this);
        findViewById(R.id.btn_service_phone).setOnClickListener(this);
        getGetuiMsg();
    }

    public void getGetuiMsg() {
        final GetuiTypeDataRequest getuiTypeDataRequest = new GetuiTypeDataRequest(EaseConstant.shopID);
        getuiTypeDataRequest.start();
        getuiTypeDataRequest.setOnResponseStateListener(new HXNormalRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                setNetDataUnread(getuiTypeDataRequest.emConversationList);
                showMsgCount();
            }

            @Override
            public void onFail(int code) {

            }
        });
    }

    private void setNetDataUnread(List<GeTuiConversation> emConversationList) {
        int netDataUnread = 0;
        if (emConversationList.size() != 0) {
            for (int i = 0; i < emConversationList.size(); i++) {
                if (emConversationList.get(i).getNumber() != 0) {
                    netDataUnread = netDataUnread + emConversationList.get(i).getNumber();
                }
            }
        }
        HXApplication.getInstance().setGetuiUndreadcount(netDataUnread);
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentTabHandler = ((MainActivity) getActivity()).fragmentController;
        if (3 == fragmentTabHandler.getCurrentTab()) {
            getGetuiMsg();
            refreshUI();
        }
    }

    private void showMsgCount() {
        int unreadMsgCount = HXApplication.getInstance().getUnreadMsgCountTotal() + HXApplication.getInstance()
                .getGetuiUndreadcount();
        if (unreadMsgCount > 0) {
            txtMsgCount.setVisibility(View.VISIBLE);
            _callBack.onCall(unreadMsgCount);
            txtMsgCount.setText(unreadMsgCount > 99 ? "99+" : unreadMsgCount + "");
        } else {
            _callBack.onCall(unreadMsgCount);
            txtMsgCount.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (personalInitRequest != null) {
            personalInitRequest.cancel();
        }
    }

    public void refreshUI() {

        if (Account.user != null) {
            if (Account.user.getIsBoss() == 1) {
                ViewUtils.setGone(btnDiscountList);
            } else {
                ViewUtils.setVisible(btnDiscountList);
            }
            if (Account.user.getRoleType() == Constants.USER_TYPE_ADMIN) {
                ViewUtils.setGone(btnTask);
            } else {
                ViewUtils.setVisible(btnTask);
            }

        }
        setUserInfo();
        loadData();
    }

    private void setUserInfo() {
        if (Account.user != null) {
            String nickName = Account.user.getNickname();
            if (!StringUtil.isNull(nickName)) {
                ViewUtils.setText(txtNickName, nickName);
            } else {
                ViewUtils.setText(txtNickName, Account.user.getUserName());
            }
            ViewUtils.setText(txtShopName, Account.user.getShopName());
            ViewUtils.setText(txtStaffId, Account.user.getSerialNum());
            String thumHeadUrl = Account.user.getThumHeadUrl();
            if (StringUtil.isNull(thumHeadUrl)) {
                imgUserHead.setImageResource(R.drawable.default_head_small);
            } else if (!lastThumbUrl.equals(thumHeadUrl)) {
                imgUserHead.setImageUrl(thumHeadUrl, R.drawable.default_head_small);
                lastThumbUrl = thumHeadUrl;
            }
        }
    }

    private void loadData() {
        personalInitRequest = new PersonalInitRequest(Account.user.getId(), Account.user.getShopId(), 0, 0);
        personalInitRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                float userBalance = personalInitRequest.getAccountBalance();
                ViewUtils.setTextPrice(txtUserBalance, userBalance);
                if (personalInitRequest.getUser() != null) {
                    ViewUtils.setText(txtNickName, personalInitRequest.getUser().getNickname());
                }
                ViewUtils.setText(txtStaffId, Account.user.getSerialNum());
                loadingView.onLoadingComplete();
            }

            @Override
            public void onFail(int code) {
                loadingView.onLoadingComplete();
            }
        });
        loadingView.startLoading();
        personalInitRequest.start(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.win_personal_center;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_top_right://设置
                SettingAct.start(getActivity(), new CallBack() {

                    @Override
                    public void onCall() {
                        getActivity().finish();
                    }
                });

                UmengUtil.onEvent(getActivity(),new UmengUtil().MY_SETTING_CLICK,null);
                break;
            case R.id.btn_home_page://个人信息
                PersonalHomepageAct.start(getActivity());
                break;
            case R.id.btn_user_balance://账户余额
                PayMyAccountAct.start(getActivity());
                UmengUtil.onEvent(getActivity(),new UmengUtil().MY_BALANCE_CLICK,null);
                break;
            case R.id.btn_messages://我的消息
//                AllMessageAct.start(getActivity());
                FragmentListAct.start("我的消息", FragmentListAct.PAGE_GUARANTEE_MSG);
                UmengUtil.onEvent(getActivity(),new UmengUtil().MY_MESSAGE_CLICK,null);
                break;
            case R.id.btn_task:
                FragmentListAct.start("任务指标", FragmentListAct.PAGE_TASK_TARGET);
                UmengUtil.onEvent(getActivity(),new UmengUtil().MY_TARGET_CLICK,null);
                break;
            case R.id.btn_yam_college://柚安米商学院
                YamCollegeListAct.start(getActivity(), Constants.YAM_COURSE_NEW);
                UmengUtil.onEvent(getActivity(),new UmengUtil().MY_COLLEGE_CLICK,null);

                break;
            case R.id.btn_devices://设备管理
                UmengUtil.onEvent(getActivity(),new UmengUtil().MY_DEVICE_CLICK,null);
                final GetDeviceStatueRequest gdsRequest = new GetDeviceStatueRequest("");
                gdsRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                    @Override
                    public void onOK() {
                        StringBuilder h5Url = new StringBuilder();
                        h5Url.append("http://").append(gdsRequest.getIp());
                        h5Url.append("/h5-wizard/index.html#");
                        h5Url.append("&device=").append(gdsRequest.getDeviceType());
                        h5Url.append("&statue=").append(gdsRequest.getDeviceStatue());
                        h5Url.append("&shopName=").append(Account.user.getShopName());
                        h5Url.append("&serUrl=").append(DataUtil.encodeBase64(Config.dataBaseUrl));
                        h5Url.append("&token=").append(PreferUtil.getInstance().getLoginToken());
                        h5Url.append("&DeviceID=").append(AppController.imei);
                        h5Url.append("&DeviceType=").append(1);
                        H5CommonAct.start(getActivity(), h5Url.toString(), "设备管理");
                    }

                    @Override
                    public void onFail(int code) {
                        DevManageAct.start(getActivity());
                    }
                });
                gdsRequest.start(this);
                break;
            case R.id.btn_discount_list://优惠领取清单
                /*if (Account.user.getRoleType() == Constants.USER_TYPE_ADMIN) {
                    GetFavorableStatisticsAct.start(getActivity());
                } else {
                    EaseConstant.startBenefitsActivity(getActivity());
                }*/
                EaseConstant.startBenefitsActivity(getActivity());
                break;
            case R.id.btn_feedback://意见反馈
                FeedbackAct.start(getActivity());
                UmengUtil.onEvent(getActivity(),new UmengUtil().MY_FEEDBACK_CLICK,null);
                break;
            case R.id.btn_service_phone://客服
                PhoneUtil.openPhoneKeyBord("4008077067", getActivity());
                UmengUtil.onEvent(getActivity(),new UmengUtil().MY_CALL_CLICK,null);
                break;
        }
    }

    public void setCallBack(ParameCallBack callBack) {
        this._callBack = callBack;
    }
}
