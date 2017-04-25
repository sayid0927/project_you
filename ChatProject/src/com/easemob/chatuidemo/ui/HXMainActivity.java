/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.easemob.chatuidemo.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easemob.EMConnectionListener;
import com.easemob.EMEventListener;
import com.easemob.EMGroupChangeListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMConversation.EMConversationType;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMMessage.Type;
import com.easemob.chat.TextMessageBody;
import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.HXHelper;
import com.easemob.chatuidemo.R;
import com.easemob.chatuidemo.db.InviteMessgeDao;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.InviteMessage;
import com.easemob.chatuidemo.domain.InviteMessage.InviteMesageStatus;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.controller.EaseUI;
import com.easemob.easeui.ui.EaseBaseViewPageAct;
import com.easemob.util.EMLog;
import java.util.UUID;

public class HXMainActivity extends EaseBaseViewPageAct implements EMEventListener {
    protected static final String TAG = "HXMainActivity";

    private TextView unreadLabel;  // 未读消息textview
    private TextView unreadAddressLable;  // 未读通讯录textview

    private ContactListFragment mContactListFragment;
    private ConversationListFragment chatHistoryFragment;
    private SettingsFragment settingFragment;
    public boolean isConflict = false;   // 账号在别处登录
    private boolean isCurrentAccountRemoved = false;  // 账号被移除

    /**
     * 检查当前用户是否被删除
     */
    public boolean getCurrentAccountRemoved() {
        return isCurrentAccountRemoved;
    }

    @Override
    protected String[] tabName() {
        if(EaseConstant.shopID<0) {
            return new String[]{"消息", "会员"};
        }else{
            return new String[]{"消息", "联系人"};
        }
    }

    /**
     * 初始化组件
     */
    @Override
    public void initView() {
        addBackBtn();
        unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
        unreadAddressLable = (TextView) findViewById(R.id.unread_address_number);

        if (getIntent().getBooleanExtra("conflict", false) && !isConflictDialogShow) {
            showConflictDialog();
        } else if (getIntent().getBooleanExtra(HXConstant.ACCOUNT_REMOVED, false)
                && !isAccountRemovedDialogShow) {
            showAccountRemovedDialog();
        }

        inviteMessgeDao = new InviteMessgeDao(this);
        userDao = new UserDao(this);
        // 这个fragment只显示好友和群组的聊天记录
        // chatHistoryFragment = new ChatHistoryFragment();
        // 显示所有人消息记录的fragment
        chatHistoryFragment = new ConversationListFragment();
        mContactListFragment = new ContactListFragment();
        setTabParam();
        fragments.add(chatHistoryFragment);
        fragments.add(mContactListFragment);
        EMChatManager.getInstance().addConnectionListener(new MyConnectionListener());
        EMChat.getInstance().setAppInited();
    }

    private void setTabParam() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, EaseUI.displayMetrics));
        lp.setMargins((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60,
                        EaseUI.displayMetrics), 0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60,
                        EaseUI.displayMetrics), 0);
        tabs.setLayoutParams(lp);
        tabs.setIsShowDivider(false);
        setDividerColor(R.color.transparent);
        setTabBackground(R.color.transparent);
    }

    private void addBackBtn() {
        ImageView backBtn = new ImageView(HXMainActivity.this);
        backBtn.setImageResource(R.drawable.ease_back_normal);
        backBtn.setScaleType(ImageView.ScaleType.CENTER);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HXMainActivity.this.finish();
            }
        });
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, EaseUI.displayMetrics),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48,
                        EaseUI.displayMetrics));
        addContentView(backBtn, layoutParams);
    }

    public static void start(Activity curAct) {
        if (HXConstant.isLoginSuccess) {
            Intent intent = new Intent(curAct, HXMainActivity
                    .class);
            HXConstant.startActivity(intent, curAct);
        }
    }

    /**
     * 监听事件
     */
    @Override
    public void onEvent(EMNotifierEvent event) {
        switch (event.getEvent()) {
            case EventNewMessage: {  // 普通消息
                EMMessage message = (EMMessage) event.getData();
                HXHelper.getInstance().getNotifier().onNewMsg(message);  // 提示新消息
                refreshUI();
                break;
            }

            case EventOfflineMessage: {
                refreshUI();
                break;
            }

            case EventConversationListChanged: {
                refreshUI();
                break;
            }

            default:
                break;
        }
    }

    private void refreshUI() {
        runOnUiThread(new Runnable() {
            public void run() {
                updateUnreadLabel();   // 刷新bottom bar消息未读数
                if (pager.getCurrentItem() == 0) {
                    if (chatHistoryFragment != null) {   // 当前页面如果为聊天历史页面，刷新此页面
                        chatHistoryFragment.refresh();
                    }
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (conflictBuilder != null) {
            conflictBuilder.create().dismiss();
            conflictBuilder = null;
        }

    }

    /**
     * 刷新未读消息数
     */
    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        //		if (unReadMsgCount > 0) {
        //			unreadLabel.setText(String.valueOf(unReadMsgCount));
        //			unreadLabel.setVisibility(View.VISIBLE);
        //		} else {
        //			unreadLabel.setVisibility(View.INVISIBLE);
        //		}
    }

    /**
     * 刷新申请与通知消息数
     */
    public void updateUnreadAddressLable() {
        //		runOnUiThread(new Runnable() {
        //			public void run() {
        //				int unReadMsgCount = getUnreadAddressCountTotal();
        //				if (unReadMsgCount > 0) {
        //					unreadAddressLable.setText(String.valueOf(unReadMsgCount));
        //					unreadAddressLable.setVisibility(View.VISIBLE);
        //				} else {
        //					unreadAddressLable.setVisibility(View.INVISIBLE);
        //				}
        //			}
        //		});

    }


    /**
     * 获取未读消息数
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
        for (EMConversation conversation : EMChatManager.getInstance().getAllConversations()
                .values()) {
            if (conversation.getType() == EMConversationType.ChatRoom) {
                chatroomUnreadMsgCount = chatroomUnreadMsgCount + conversation.getUnreadMsgCount();
            }
        }
        return unreadMsgCountTotal - chatroomUnreadMsgCount;
    }

    private InviteMessgeDao inviteMessgeDao;
    private UserDao userDao;


    /**
     * 连接监听listener
     */
    private class MyConnectionListener implements EMConnectionListener {

        @Override
        public void onConnected() {
            //			runOnUiThread(new Runnable() {
            //				@Override
            //				public void run() {
            //					chatHistoryFragment.errorItem.setVisibility(View.GONE);
            //				}
            //			});
        }

        @Override
        public void onDisconnected(final int error) {
            final String st1 = getResources()
                    .getString(com.easemob.chatuidemo.R.string.can_not_connect_chat_server_connection);
            final String st2 = getResources()
                    .getString(com.easemob.chatuidemo.R.string.the_current_network);
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    //                    if (error == EMError.USER_REMOVED) {
                    //                        // 显示帐号已经被移除
                    //                        showAccountRemovedDialog();
                    //                    } else if (error == EMError.CONNECTION_CONFLICT) {
                    //                        // 显示帐号在其他设备登陆dialog
                    //                        showConflictDialog();
                    //                    } else {
                    //                        chatHistoryFragment.errorItem.setVisibility(View.VISIBLE);
                    //                        if (NetUtils.hasNetwork(HXMainActivity.this))
                    //                            chatHistoryFragment.errorText.setText(st1);
                    //                        else
                    //                            chatHistoryFragment.errorText.setText(st2);
                    //
                    //                    }
                }

            });
        }
    }

    /**
     * MyGroupChangeListener
     */
    private class MyGroupChangeListener implements EMGroupChangeListener {

        @Override
        public void onInvitationReceived(String groupId, String groupName, String inviter,
                                         String reason) {
            boolean hasGroup = false;
            for (EMGroup group : EMGroupManager.getInstance().getAllGroups()) {
                if (group.getGroupId().equals(groupId)) {
                    hasGroup = true;
                    break;
                }
            }
            if (!hasGroup) {
                return;
            }

            // 被邀请
            String st3 = getResources()
                    .getString(com.easemob.chatuidemo.R.string.Invite_you_to_join_a_group_chat);
            EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
            msg.setChatType(ChatType.GroupChat);
            msg.setFrom(inviter);
            msg.setTo(groupId);
            msg.setMsgId(UUID.randomUUID().toString());
            msg.addBody(new TextMessageBody(inviter + st3));
            // 保存邀请消息
            EMChatManager.getInstance().saveMessage(msg);
            // 提醒新消息
            HXHelper.getInstance().getNotifier().viberateAndPlayTone(msg);

            runOnUiThread(new Runnable() {
                public void run() {
                    updateUnreadLabel();
                    // 刷新ui
                    if (pager.getCurrentItem() == 0) {
                        chatHistoryFragment.refresh();
                    }
                }
            });

        }

        @Override
        public void onInvitationAccpted(String groupId, String inviter, String reason) {

        }

        @Override
        public void onInvitationDeclined(String groupId, String invitee, String reason) {

        }

        @Override
        public void onUserRemoved(String groupId, String groupName) {
            // 提示用户被T了，demo省略此步骤
            // 刷新ui
            runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        updateUnreadLabel();
                        if (pager.getCurrentItem() == 0) {
                            chatHistoryFragment.refresh();
                        }
                    } catch (Exception e) {
                        EMLog.e(TAG, "refresh exception " + e.getMessage());
                    }
                }
            });
        }

        @Override
        public void onGroupDestroy(String groupId, String groupName) {
            // 提示用户群被解散,demo省略  刷新ui
            runOnUiThread(new Runnable() {
                public void run() {
                    updateUnreadLabel();
                    if (pager.getCurrentItem() == 0) {
                        chatHistoryFragment.refresh();
                    }
                }
            });
        }

        @Override
        public void onApplicationReceived(String groupId, String groupName, String applyer,
                                          String reason) {
            // 用户申请加入群聊
            InviteMessage msg = new InviteMessage();
            msg.setFrom(applyer);
            msg.setTime(System.currentTimeMillis());
            msg.setGroupId(groupId);
            msg.setGroupName(groupName);
            msg.setReason(reason);
            Log.d(TAG, applyer + " 申请加入群聊：" + groupName);
            msg.setStatus(InviteMesageStatus.BEAPPLYED);
            notifyNewIviteMessage(msg);
        }

        @Override
        public void onApplicationAccept(String groupId, String groupName, String accepter) {
            String st4 = getResources().getString(
                    com.easemob.chatuidemo.R.string.Agreed_to_your_group_chat_application);

            EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);  // 加群申请被同意
            msg.setChatType(ChatType.GroupChat);
            msg.setFrom(accepter);
            msg.setTo(groupId);
            msg.setMsgId(UUID.randomUUID().toString());
            msg.addBody(new TextMessageBody(accepter + st4));

            EMChatManager.getInstance().saveMessage(msg);  // 保存同意消息

            HXHelper.getInstance().getNotifier().viberateAndPlayTone(msg);  // 提醒新消息

            runOnUiThread(new Runnable() {
                public void run() {
                    updateUnreadLabel();
                    if (pager.getCurrentItem() == 0) {
                        chatHistoryFragment.refresh();  // 刷新ui
                    }
                }
            });
        }

        @Override
        public void onApplicationDeclined(String groupId, String groupName, String decliner,
                                          String reason) {
            // 加群申请被拒绝，demo未实现
        }

    }

    /**
     * 保存提示新消息
     * @param msg
     */
    private void notifyNewIviteMessage(InviteMessage msg) {
//        saveInviteMsg(msg);
        // 提示有新消息
        HXHelper.getInstance().getNotifier().viberateAndPlayTone(null);

        // 刷新bottom bar消息未读数
        updateUnreadAddressLable();
        // 刷新好友页面ui
//        if (pager.getCurrentItem() == 1)
//            contactListFragment.refresh();
    }

    protected void onResume() {
        if (!isConflict && !isCurrentAccountRemoved) {
            updateUnreadLabel();
            updateUnreadAddressLable();
            EMChatManager.getInstance().activityResumed();

        }

        // unregister this event listener when this activity enters the
        // background
        HXHelper helper =  HXHelper.getInstance();
        helper.pushActivity(this);

        // register the event listener when enter the foreground
        EMChatManager.getInstance().registerEventListener(this,
                new EMNotifierEvent.Event[]{EMNotifierEvent.Event.EventNewMessage, EMNotifierEvent.Event.EventOfflineMessage, EMNotifierEvent.Event.EventConversationListChanged});
        super.onResume();
    }

    @Override
    protected void onStop() {
        EMChatManager.getInstance().unregisterEventListener(this);
        HXHelper helper =  HXHelper.getInstance();
        helper.popActivity(this);

        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isConflict", isConflict);
        outState.putBoolean(HXConstant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            moveTaskToBack(false);
//            return true;
//        }
        return super.onKeyDown(keyCode, event);
    }

    private android.app.AlertDialog.Builder conflictBuilder;
    private android.app.AlertDialog.Builder accountRemovedBuilder;
    private boolean isConflictDialogShow;
    private boolean isAccountRemovedDialogShow;

    /**
     * 显示帐号在别处登录dialog
     */
    private void showConflictDialog() {
        isConflictDialogShow = true;
        HXHelper.getInstance().logout(true,null);
        String st = getResources().getString(com.easemob.chatuidemo.R.string.Logoff_notification);
        if (!HXMainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (conflictBuilder == null) {
                    conflictBuilder = new android.app.AlertDialog.Builder(HXMainActivity.this);
                }
                conflictBuilder.setTitle(st);
                conflictBuilder.setMessage(com.easemob.chatuidemo.R.string.connect_conflict);
                conflictBuilder
                        .setPositiveButton(com.easemob.chatuidemo.R.string.ok,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        conflictBuilder = null;
                                        finish();
                                    }
                                });
                conflictBuilder.setCancelable(false);
                conflictBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                EMLog.e(TAG, "---------color conflictBuilder error" + e.getMessage());
            }

        }

    }

    /**
     * 帐号被移除的dialog
     */
    private void showAccountRemovedDialog() {
        isAccountRemovedDialogShow = true;
        HXHelper.getInstance().logout(true,null);
        String st5 = getResources()
                .getString(com.easemob.chatuidemo.R.string.Remove_the_notification);
        if (!HXMainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (accountRemovedBuilder == null) {
                    accountRemovedBuilder = new android.app.AlertDialog.Builder(
                            HXMainActivity.this);
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
                EMLog.e(TAG, "---------color userRemovedBuilder error" + e.getMessage());
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (getIntent().getBooleanExtra("conflict", false) && !isConflictDialogShow) {
            showConflictDialog();
        } else if (getIntent().getBooleanExtra(HXConstant.ACCOUNT_REMOVED,
                false) && !isAccountRemovedDialogShow) {
            showAccountRemovedDialog();
        }
    }

}
