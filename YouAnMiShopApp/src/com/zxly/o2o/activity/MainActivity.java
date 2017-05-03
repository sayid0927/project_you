package com.zxly.o2o.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chatuidemo.HXApplication;
import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.HXHelper;
import com.easemob.chatuidemo.utils.EaseCallBack;
import com.easemob.chatuidemo.utils.HXNewMsgCallBack;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.model.GeTuiConversation;
import com.easemob.easeui.request.GetuiTypeDataRequest;
import com.easemob.easeui.request.HXNormalRequest;
import com.easemob.easeui.ui.EaseHXMainFragment;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.application.Config;
import com.zxly.o2o.fragment.AllCustomerFragment;
import com.zxly.o2o.fragment.AllCustomerFragmentNew;
import com.zxly.o2o.fragment.MakeMoneyFragment;
import com.zxly.o2o.fragment.ManageFragment;
import com.zxly.o2o.fragment.PersonalCenterFragment;
import com.zxly.o2o.fragment.PromotionFragment;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.IMGetContactListRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.FragmentTabHandler;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.UmengUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.FlowRadioGroup;
import com.zxly.o2o.view.RedPoint;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BasicAct {
    public FlowRadioGroup rgs;
    private RedPoint msgRedPoint;
    public FragmentTabHandler fragmentController;
    public List<Fragment> fragments;
    private EaseHXMainFragment easeHXMainFragment;

    // 账号在别处登录
    public boolean isConflict = false;
    private boolean isConflictDialogShow = true;
    private boolean isAccountRemovedDialogShow = true;
    private boolean isCurrentAccountRemoved = false;  // 账号被移除
    private android.app.AlertDialog.Builder conflictBuilder;
    private android.app.AlertDialog.Builder accountRemovedBuilder;
    private boolean isQuit;

	
    private RadioButton btnManageOrPromotion;
    private ImageView imgFans, imgMakeMoney, imgMine;//底部小红点
    private static MainActivity instance;
    private PersonalCenterFragment personalCenterFragment;
    private AllCustomerFragment allCustomerFragment;
    private boolean shouldRefrensh;
    private AllCustomerFragmentNew allCustomerFragmentNew;

    public static MainActivity getIncetance(){
        return instance;
    }

    HXNewMsgCallBack newMsgReceivedListener = new HXNewMsgCallBack() {
        @Override
        public void onCall(final EMMessage message) {
            try {
                int what = message.getIntAttribute("what");
                if (what == HXConstant.SYS_INSURE_KEY_SHOP) {
                    ViewUtils.setVisible(imgMakeMoney);
                }
            } catch (EaseMobException e) {
                e.printStackTrace();
            }
        }
    };

    public void setRedPointVisible(boolean isVisible) {
        if (isVisible) {
            ViewUtils.setVisible(imgMakeMoney);
        } else {
            ViewUtils.setGone(imgMakeMoney);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tab_main);

        HXHelper.getInstance().addListener(newMsgReceivedListener);
        instance =this;

		
        if (getIntent().getBooleanExtra(HXConstant.ACCOUNT_CONFLICT, false)) {
            showConflictDialog();
        }

		
        Bundle extra_bundle = getIntent().getBundleExtra("extra_bundle");
        if(extra_bundle!=null){
            String sys_msg_url = extra_bundle.getString("sys_msg_url");
            String title = extra_bundle.getString("文章详情");
            boolean isFromNotice = extra_bundle.getBoolean("isFromNotice");
            long id = extra_bundle.getLong("id");
            Intent intent = new Intent(MainActivity.this, H5DetailAct.class);
            intent.putExtra("sys_msg_url", sys_msg_url);
            //需要统计用户是否有点击从通知栏的消息，标记用
            intent.putExtra("isFromNotice",isFromNotice);
            intent.putExtra("id",id);
            intent.putExtra("title", "文章详情");
            GetuiShopH5DetailAct.start(this,sys_msg_url,title,id);
        }

		
        initView();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra(HXConstant.ACCOUNT_CONFLICT, false)) {
            showConflictDialog();
        } else if (intent.getBooleanExtra(HXConstant.ACCOUNT_REMOVED,
                false) && !isAccountRemovedDialogShow) {
            showAccountRemovedDialog();
        }

    }


    public void initView() {

	

//        msgRedPoint = (RedPoint) findViewById(R.id.view_redPoint);
        imgFans = (ImageView) findViewById(R.id.point_fans);
        imgMakeMoney = (ImageView) findViewById(R.id.point_make_money);
        imgMine = (ImageView) findViewById(R.id.point_mine);
        btnManageOrPromotion = (RadioButton) findViewById(R.id.btn_manage_or_promotion);
        getGetuiMsgCount();
        initHXMainFragment();
        fragments = new ArrayList<Fragment>();
//        allCustomerFragment = new AllCustomerFragment();
        allCustomerFragmentNew =new AllCustomerFragmentNew();
        fragments.add(allCustomerFragmentNew);
//        fragments.add(initHXMainFragment());
//        fragments.add(PromotionFragment.newInstance());
        if (Account.user.getRoleType() == Constants.USER_TYPE_ADMIN) {
            fragments.add(new ManageFragment());
            ViewUtils.setText(btnManageOrPromotion, "管理");

            Drawable drawable = getResources().getDrawable(R.drawable.btn_tab_manage_selector);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            btnManageOrPromotion.setCompoundDrawables(null, drawable, null, null);
        } else {
            fragments.add(PromotionFragment.newInstance());
//            fragments.add(TaskTargetListFragment.newInstance());
            ViewUtils.setText(btnManageOrPromotion, "找客");
            UmengUtil.onEvent(this,"home_find_click",null);
            Drawable drawable = getResources().getDrawable(R.drawable.btn_tab_promotion_selector);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            btnManageOrPromotion.setCompoundDrawables(null, drawable, null, null);
        }
        fragments.add(new MakeMoneyFragment());
        personalCenterFragment = new PersonalCenterFragment();
        personalCenterFragment.setCallBack(callBack);
        fragments.add(personalCenterFragment);
        rgs = (FlowRadioGroup) findViewById(R.id.main_act_radio_group);
        fragmentController = new FragmentTabHandler(this, fragments, R.id.main_act_tabcontent);
        rgs.setOnCheckedChangeListener(new FlowRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(FlowRadioGroup group, int checkedId) {
                switch (checkedId) {

				
                    case R.id.btn_fans:
                        fragmentController.showTab(0);

						
//                        easeHXMainFragment.refreshContainFragment();  //刷新fragment数据
//                        if(Account.user!=null) {
//                            new ContactCommissionListRequest().start();
//                        }
//
//                        //消息界面
//                        if (!IMGetContactListRequest.isLoadingContact&&Account.user != null && (HXHelper
//                                .getInstance()
//                                .getYAMContactList().size() ==
//                                0 || System.currentTimeMillis()
//                                - PreferenceManager.getInstance().getMistiming() >
//                                (1000 * 60 * 5))) {
//                            Log.e("reload contact", "reload contact...");
//                            PreferenceManager.getInstance().setMistiming(System.currentTimeMillis());
//                            if (HXHelper.yamContactList.size() == 0 && Account.user != null) {
//                                final IMGetContactListRequest imGetContactListRequest = new
//                                        IMGetContactListRequest(Account.user
//                                        .getShopId());
//                                imGetContactListRequest.setOnResponseStateListener(
//                                        new BaseRequest.ResponseStateListener() {
//                                            @Override
//                                            public void onOK() {
//                                                imGetContactListRequest.isLoadingContact = false;
//                                            }
//
//                                            @Override
//                                            public void onFail(int code) {
//                                                imGetContactListRequest.isLoadingContact = false;
//
//                                            }
//                                        });
//                                imGetContactListRequest.start();
//                            }
//                        }

                        break;

                    case R.id.btn_manage_or_promotion:
                        fragmentController.showTab(1);
                        break;
                    case R.id.btn_make_money:
                        fragmentController.showTab(2);
                        break;
                    case R.id.btn_mine:
                        fragmentController.showTab(3);

						
                        break;

                    default:
                        break;

                }

            }

        });


        ((RadioButton) rgs.findViewById(R.id.btn_fans)).setChecked(true);

    }

    private void getGetuiMsgCount() {
            final GetuiTypeDataRequest getuiTypeDataRequest=new GetuiTypeDataRequest(EaseConstant.shopID);
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

    private void showMsgCount() {
        if (HXApplication.getInstance().unReadMsgCount +HXApplication.getInstance().getGetuiUndreadcount()> 0) {
            ViewUtils.setVisible(imgMine);
        }
    }

    private void setNetDataUnread(List<GeTuiConversation> emConversationList) {
        int netDataUnread=0;
        if(emConversationList.size()!=0){
            for (int i = 0; i< emConversationList.size(); i++){
                if(emConversationList.get(i).getNumber()!=0){
                    netDataUnread = netDataUnread+ emConversationList.get(i).getNumber();
                }
            }
        }
        HXApplication.getInstance().setGetuiUndreadcount(netDataUnread);
    }


    /**
     * 显示帐号在别处登录dialog
     */
    private void showConflictDialog() {
        isConflictDialogShow = true;
        HXHelper.getInstance().logout(true, null);
        String st = getResources().getString(com.easemob.chatuidemo.R.string.Logoff_notification);
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (conflictBuilder == null) {
                    conflictBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                }
                conflictBuilder.setTitle(st);
                conflictBuilder.setMessage(com.easemob.chatuidemo.R.string.connect_conflict);
                conflictBuilder
                        .setPositiveButton(com.easemob.chatuidemo.R.string.ok,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        //商户改版  加个标识 账号冲突后刷新首页
                                        Config.shouldRefrensh =true;
                                        AppController.getInstance().deleteFile("user");
                                        Account.user = null;
                                        PreferUtil.getInstance().setLoginToken("");
                                        conflictBuilder = null;
                                        AppController.getInstance().exitAndGoToMainPage();
                                        LoginAct.start(AppController.getInstance().getTopAct());
                                        finish();
                                    }
                                });
                conflictBuilder.setCancelable(false);
                conflictBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                EMLog.e("", "---------color conflictBuilder error" + e.getMessage());
            }

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (conflictBuilder != null) {
            conflictBuilder.create().dismiss();
            conflictBuilder = null;
        }

        //清空消息监听callBack
        HXHelper.getInstance().cleanReceivedListeners();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isConflict", isConflict);
        outState.putBoolean(HXConstant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        if (!isConflict && !isCurrentAccountRemoved) {
            EMChatManager.getInstance().activityResumed();
        }
        if (fragmentController != null && fragmentController.getCurrentTab() == 1) {
            isNeedToRefresh();
        }
        if (fragmentController != null && fragmentController.getCurrentTab() == 0) {
            if(Config.shouldRefrensh&&allCustomerFragment!=null){
                allCustomerFragment.setShouldRfrensh(true);
                allCustomerFragment.onResume();
                Config.shouldRefrensh=false;
            }
        }

        if(HXApplication.getInstance().getUnreadMsgCountTotal()+HXApplication.getInstance()
                .getGetuiUndreadcount()>0){
            ViewUtils.setVisible(imgMine);
        }else {
            ViewUtils.setGone(imgMine);
        }

        super.onResume();
    }

    private void isNeedToRefresh(){

        mMainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!IMGetContactListRequest.isLoadingContact) {
                    easeHXMainFragment.getContactListFragment()
                            .refresh();
                }else{
                    isNeedToRefresh();
                }
            }
        }, 2000);
    }


    /**
     * 帐号被移除的dialog
     */

    private void showAccountRemovedDialog() {
        isAccountRemovedDialogShow = true;
        HXHelper.getInstance().logout(true, null);
        String st5 = getResources()
                .getString(com.easemob.chatuidemo.R.string.Remove_the_notification);
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (accountRemovedBuilder == null) {
                    accountRemovedBuilder = new android.app.AlertDialog.Builder(
                            MainActivity.this);
                }
                accountRemovedBuilder.setTitle(st5);
                accountRemovedBuilder.setMessage(com.easemob.chatuidemo.R.string.em_user_remove);
                accountRemovedBuilder
                        .setPositiveButton(com.easemob.chatuidemo.R.string.ok,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        accountRemovedBuilder = null;
                                        finish();
                                    }
                                });
                accountRemovedBuilder.setCancelable(false);
                accountRemovedBuilder.create().show();
                isCurrentAccountRemoved = true;
            } catch (Exception e) {
                EMLog.e("MainActivity", "---------color userRemovedBuilder error" + e.getMessage());
            }
        }
    }

    private Fragment initHXMainFragment() {
        easeHXMainFragment = new EaseHXMainFragment();
        easeHXMainFragment.setCallBack(new EaseCallBack() {
            @Override
            public void onCall() {
                //set system time
                if (EaseConstant.shopID != 0) {
                    final IMGetContactListRequest imGetShopContactsRequest =
                            new IMGetContactListRequest(Math.abs(EaseConstant.shopID));
                    imGetShopContactsRequest.setOnResponseStateListener(
                            new BaseRequest.ResponseStateListener() {
                                @Override
                                public void onOK() {
                                    imGetShopContactsRequest.isLoadingContact = false;
                                    mMainHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            easeHXMainFragment.getContactListFragment().refresh();
                                        }
                                    }, 1000);
                                }

                                @Override
                                public void onFail(int code) {
                                    imGetShopContactsRequest.isLoadingContact = false;
                                }
                            });
                    imGetShopContactsRequest.start();
                }
            }
        });
        easeHXMainFragment.setNewMsgCallBack(new EaseCallBack() {
            @Override
            public void onCall() {
                mMainHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        msgRedPoint.showRedPoint();
                        if (HXApplication.getInstance().unReadMsgCount +HXApplication.getInstance()
                                .getGetuiUndreadcount()> 0) {
                            ViewUtils.setVisible(imgMine);
                        }
                    }
                }, 200);

            }
        });
        return easeHXMainFragment;
    }

    @Override
    public void onBackPressed() {
        if (!isQuit) {
            ViewUtils.showToast("再按一次返回键退出程序");
            isQuit = true;
            return;
        }
        super.onBackPressed();
    }

    /**
     * 显示客多多下方红点
     */
    public void showKeDDRedPoint(){
        imgFans.setVisibility(Config.fansNewBehavoir+Config.menberNewBehavoir>0? View.VISIBLE:View.GONE);
    }

    ParameCallBack callBack=new ParameCallBack() {
        @Override
        public void onCall(Object object) {
            if ((Integer)object> 0) {
                ViewUtils.setVisible(imgMine);
            }else {
                ViewUtils.setGone(imgMine);
            }
        }
    };


}
 