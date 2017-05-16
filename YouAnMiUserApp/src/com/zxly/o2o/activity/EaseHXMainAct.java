package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.easemob.chatuidemo.HXHelper;
import com.easemob.chatuidemo.db.InviteMessgeDao;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.InviteMessage;
import com.easemob.chatuidemo.ui.ContactListFragment;
import com.easemob.chatuidemo.ui.ConversationListFragment;
import com.easemob.chatuidemo.utils.EaseCallBack;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.controller.EaseUI;
import com.easemob.easeui.widget.EaseTitleBar;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.utils.TabEntity;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.ViewUtils;

import java.util.ArrayList;

/**
 * Created on 2017/5/11.
 */

public class EaseHXMainAct  extends BasicAct{


    protected EaseTitleBar titleBar;
    protected InputMethodManager inputMethodManager;

    private String[] titles;

    private ArrayList<CustomTabEntity> tabs = new ArrayList<CustomTabEntity>();
    private View decorView;
    private CommonTabLayout tls;

    private InviteMessgeDao inviteMessgeDao;
    private UserDao userDao;
    private EaseCallBack easeCallBack;
    private ContactListFragment contactListFragment;
    private ConversationListFragment conversationListFragment;
    protected EaseCallBack newMsgCallBack;
//
    public void setNewMsgCallBack(EaseCallBack callBack){
        newMsgCallBack=callBack;
    }

    public ContactListFragment getContactListFragment(){
        return contactListFragment;
    }

    public ConversationListFragment getConversationListFragment(){
        return conversationListFragment;
    }


    public void setCallBack(EaseCallBack callBack){
        easeCallBack=callBack;
    }

    protected ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ease_tab_layout);
        inputMethodManager = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        titleBar = (EaseTitleBar) this.findViewById(R.id.title_bar);
        initView();
        setUpView();
    }

    protected void initView() {
        contactListFragment=new ContactListFragment();
        contactListFragment.setCallBack(easeCallBack);
        conversationListFragment = new ConversationListFragment();
        conversationListFragment.setCallBack(newMsgCallBack);
        fragments.add(conversationListFragment);
        fragments.add(contactListFragment);

        if (EaseConstant.shopID < 0) {
            titles = new String[]{"消息", "粉丝"};
        } else {
            titles = new String[]{"消息", "联系人"};
        }

        for (String title : titles) {
            tabs.add(new TabEntity(title, 0, 0));
        }

        decorView = this.getWindow().getDecorView();
        /** indicator圆角色块 */
        tls= (CommonTabLayout) this.findViewById(R.id.ease_tl_tabs);
        tls.setTabData(tabs, this, R.id.fl_change, fragments);
        if(EaseConstant.shopID < 0){
            tls.setVisibility(View.GONE);
        }
        addBackBtn();
    }



    protected void setUpView() {
        inviteMessgeDao = new InviteMessgeDao(this);
        userDao = new UserDao(this);

        // 这个fragment只显示好友和群组的聊天记录
        // chatHistoryFragment = new ChatHistoryFragment();
        // 显示所有人消息记录的fragment
//        EMChat.getInstance().setAppInited();
    }

//    @Override
//    public void onEvent(EMNotifierEvent event) {
//        switch (event.getEvent()) {
//            case EventNewMessage: {  // 普通消息
//                EMMessage message = (EMMessage) event.getData();
//                HXHelper.getInstance().getNotifier().onNewMsg(message);  // 提示新消息
//                break;
//            }
//
//            case EventOfflineMessage: {
//                break;
//            }
//
//            case EventConversationListChanged: {
//                break;
//            }
//
//            default:
//                break;
//        }
//    }


    /**
     * 保存提示新消息
     *
     * @param msg
     */
    private void notifyNewIviteMessage(InviteMessage msg) {
        // 提示有新消息
        HXHelper.getInstance().getNotifier().viberateAndPlayTone(null);
    }

    @Override
    public void onStop() {
//        EMChatManager.getInstance().unregisterEventListener(this);
        HXHelper helper = HXHelper.getInstance();
        helper.popActivity(this);

        super.onStop();
    }

    @Override
    public void onResume() {
        // unregister this event listener when this activity enters the
        // background
        HXHelper helper = HXHelper.getInstance();
        helper.pushActivity(this);

        // register the event listener when enter the foreground
//        EMChatManager.getInstance().registerEventListener(this,
//                new EMNotifierEvent.Event[]{EMNotifierEvent.Event.EventNewMessage,
//                        EMNotifierEvent.Event.EventOfflineMessage,
//                        EMNotifierEvent.Event.EventConversationListChanged});
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    public static void start(Activity curAct) {
        Intent intent = new Intent(curAct, EaseHXMainAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

    /*添加返回按钮*/
    private void addBackBtn() {
        ImageView backBtn = new ImageView(this);
        backBtn.setImageResource(R.drawable.back_white);
        backBtn.setScaleType(ImageView.ScaleType.CENTER);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EaseHXMainAct.this.finish();
            }
        });
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, EaseUI.displayMetrics),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50,
                        EaseUI.displayMetrics));
        addContentView(backBtn, layoutParams);
    }
}

