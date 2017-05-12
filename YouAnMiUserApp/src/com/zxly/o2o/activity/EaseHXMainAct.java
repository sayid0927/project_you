package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.easemob.chatuidemo.HXHelper;
import com.easemob.chatuidemo.db.InviteMessgeDao;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.InviteMessage;
import com.easemob.chatuidemo.ui.ContactListFragment;
import com.easemob.chatuidemo.ui.ConversationListFragment;
import com.easemob.chatuidemo.utils.EaseCallBack;
import com.easemob.easeui.EaseConstant;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.utils.TabEntity;
import com.flyco.tablayout.utils.ViewFindUtils;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.ViewUtils;

import java.util.ArrayList;

/**
 * Created on 2017/5/11.
 */

public class EaseHXMainAct  extends  EaseBaseAct{

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


//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.ease_tab_layout, container, false);
//    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        Log.e("TAG","1111");
        setContentView(R.layout.ease_tab_layout);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.ease_tab_layout;
    }

    public void refreshContainFragment(){
        if(contactListFragment!=null&&conversationListFragment!=null) {
            contactListFragment.refresh();
            conversationListFragment.refresh();
        }
    }

    @Override
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
        tls = ViewFindUtils.find(decorView, R.id.ease_tl_tabs);
        tls.setTabData(tabs, this, R.id.fl_change, fragments);
        if(EaseConstant.shopID < 0){
            tls.setVisibility(View.GONE);
        }
    }


    @Override
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
}

