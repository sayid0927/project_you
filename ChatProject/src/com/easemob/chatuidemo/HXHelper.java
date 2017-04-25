package com.easemob.chatuidemo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.easemob.EMCallBack;
import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.EMEventListener;
import com.easemob.EMGroupChangeListener;
import com.easemob.EMNotifierEvent;
import com.easemob.EMValueCallBack;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMCmdManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMMessage.Type;
import com.easemob.chat.TextMessageBody;
import com.easemob.chatuidemo.db.DemoDBManager;
import com.easemob.chatuidemo.db.InviteMessgeDao;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.InviteMessage;
import com.easemob.chatuidemo.domain.InviteMessage.InviteMesageStatus;
import com.easemob.chatuidemo.domain.RobotUser;
import com.easemob.chatuidemo.parse.UserProfileManager;
import com.easemob.chatuidemo.receiver.CallReceiver;
import com.easemob.chatuidemo.ui.ChatActivity;
import com.easemob.chatuidemo.ui.VideoCallActivity;
import com.easemob.chatuidemo.ui.VoiceCallActivity;
import com.easemob.chatuidemo.utils.HXNewMsgCallBack;
import com.easemob.chatuidemo.utils.PreferenceManager;
import com.easemob.easeui.AppException;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.controller.EaseUI;
import com.easemob.easeui.controller.EaseUI.EaseSettingsProvider;
import com.easemob.easeui.domain.EaseYAMUser;
import com.easemob.easeui.model.EaseNotifier;
import com.easemob.easeui.model.EaseNotifier.EaseNotificationInfoProvider;
import com.easemob.easeui.request.EaseAddFriendRequest;
import com.easemob.easeui.ui.EaseH5DetailAct;
import com.easemob.easeui.ui.SysMsgDetailActivity;
import com.easemob.easeui.utils.EaseCommonUtils;
import com.easemob.easeui.utils.GsonParser;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.google.gson.reflect.TypeToken;

import org.jivesoftware.smack.packet.Message;

public class HXHelper {


    /**
     * 数据同步listener
     */
    public interface DataSyncListener {
        /**
         * 同步完毕
         *
         * @param success true：成功同步到数据，false失败
         */
        void onSyncComplete(boolean success);
    }

    protected static final String TAG = "HXHelper";

    private EaseUI easeUI;

    /**
     * EMEventListener
     */
    protected EMEventListener eventListener = null;

    private Map<String, EaseYAMUser> yamContactMap = new HashMap<String, EaseYAMUser>();

    public static List<EaseYAMUser> yamContactList = new ArrayList<EaseYAMUser>();

    private List<String> blackList;

    private Map<String, RobotUser> robotList;

    private UserProfileManager userProManager;

    private static HXHelper instance = null;

    private HXModel demoModel = null;

    /**
     * HuanXin sync groups status listener
     */
    private List<DataSyncListener> syncGroupsListeners;
    /**
     * HuanXin sync contacts status listener
     */
    private List<DataSyncListener> syncContactsListeners;
    /**
     * HuanXin sync blacklist status listener
     */
    private List<DataSyncListener> syncBlackListListeners;

    private boolean isSyncingGroupsWithServer = false;
    private boolean isSyncingContactsWithServer = false;
    private boolean isSyncingBlackListWithServer = false;
    private boolean isGroupsSyncedWithServer = false;
    private boolean isContactsSyncedWithServer = false;
    private boolean isBlackListSyncedWithServer = false;

    private boolean alreadyNotified = false;

    public boolean isVoiceCalling;
    public boolean isVideoCalling;

    private String username;

    private Context appContext;

    private CallReceiver callReceiver;

    private EMConnectionListener connectionListener;

    private MyContactListener myContactListener;

    private InviteMessgeDao inviteMessgeDao;
    private UserDao userDao;

    private LocalBroadcastManager broadcastManager;

    private boolean isGroupAndContactListenerRegisted;

    private List<HXNewMsgCallBack> msgReceivedListeners = new ArrayList<HXNewMsgCallBack>();

    private HXHelper() {
    }

    public synchronized static HXHelper getInstance() {
        if (instance == null) {
            instance = new HXHelper();
        }
        return instance;
    }

    /**
     * init helper
     *
     * @param context application context
     */
    public void init(Context context,int sslRawId) {
        if (EaseUI.getInstance().init(context,sslRawId)) {
            appContext = context;
            //设为调试模式，打成正式包时，最好设为false，以免消耗额外的资源
            EMChat.getInstance().setDebugMode(true);
            //get easeui instance
            easeUI = EaseUI.getInstance();
            //调用easeui的api设置providers
            setEaseUIProviders();
            demoModel = new HXModel(context);
            //设置chat options
            setChatoptions();
            //初始化PreferenceManager
            PreferenceManager.init(context);
            //初始化用户管理类
            getUserProfileManager().init(context);

            //设置全局监听
            setGlobalListeners();
            broadcastManager = LocalBroadcastManager.getInstance(appContext);
            initDbDao();




        }
    }

    private void setChatoptions() {
        //easeui库默认设置了一些options，可以覆盖
        EMChatOptions options = EMChatManager.getInstance().getChatOptions();
        options.allowChatroomOwnerLeave(getModel().isChatroomOwnerLeaveAllowed());
    }

    protected void setEaseUIProviders() {
        //需要easeui库显示用户头像和昵称设置此provider
        //        easeUI.setUserProfileProvider(new EaseUserProfileProvider() {
        //
        //            @Override
        //            public EaseYAMUser getUser(String username) {
        //                return getUserInfo(username);
        //            }
        //        });

        //不设置，则使用easeui默认的
        easeUI.setSettingsProvider(new EaseSettingsProvider() {

            @Override
            public boolean isSpeakerOpened() {
                return demoModel.getSettingMsgSpeaker();
            }

            @Override
            public boolean isMsgVibrateAllowed(EMMessage message) {
                return demoModel.getSettingMsgVibrate();
            }

            @Override
            public boolean isMsgSoundAllowed(EMMessage message) {
                return demoModel.getSettingMsgSound();
            }

            @Override
            public boolean isMsgNotifyAllowed(EMMessage message) {
                if (message == null) {
                    return demoModel.getSettingMsgNotification();
                }
                if (!demoModel.getSettingMsgNotification()) {
                    return false;
                } else {
                    //如果允许新消息提示
                    //屏蔽的用户和群组不提示用户
                    String chatUsename = null;
                    List<String> notNotifyIds = null;
                    // 获取设置的不提示新消息的用户或者群组ids
                    if (message.getChatType() == ChatType.Chat) {
                        chatUsename = message.getFrom();
                        notNotifyIds = demoModel.getDisabledIds();
                    } else {
                        chatUsename = message.getTo();
                        notNotifyIds = demoModel.getDisabledGroups();
                    }

                    if (notNotifyIds == null || !notNotifyIds.contains(chatUsename)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        });
        //不设置，则使用easeui默认的
        easeUI.getNotifier().setNotificationInfoProvider(new EaseNotificationInfoProvider() {

            @Override
            public String getTitle(EMMessage message) {
                //修改标题,这里使用默认
                try {
                    return message.getStringAttribute("nickname");
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public int getSmallIcon(EMMessage message) {
                //设置小图标，这里为默认
                return 0;
            }

            @Override
            public String getDisplayedText(EMMessage message) {
                // 设置状态栏的消息提示，可以根据message的类型做相应提示
                String ticker = EaseCommonUtils.getMessageDigest(message, appContext);
                if (message.getType() == Type.TXT) {
                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
                }
                EaseYAMUser user = getUserInfo(message.getFrom());
                if (user != null) {
                    return user.getFirendsUserInfo().getNickname() + ": " + ticker;
                } else {
                    try {
                        String userName = message.getStringAttribute("nickname");
                        return userName + ": " + ticker;
                    } catch (EaseMobException e) {
                        e.printStackTrace();
                        return "陌生人: " + ticker;
                    }
                }
            }

            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                return null;
                // return fromUsersNum + "个基友，发来了" + messageNum + "条消息";
            }

            @Override
            public Intent getLaunchIntent(EMMessage message) {
                //设置点击通知栏跳转事件
                Intent intent = new Intent(appContext, ChatActivity.class);


                //有电话时优先跳转到通话页面
                if (isVideoCalling) {
                    intent = new Intent(appContext, VideoCallActivity.class);
                } else if (isVoiceCalling) {
                    intent = new Intent(appContext, VoiceCallActivity.class);
                } else {
                    ChatType chatType = message.getChatType();
                    if (chatType == ChatType.Chat) { // 单聊信息
                        intent.putExtra(EaseConstant.EXTRA_USER_ID, message.getFrom());
                        intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, HXConstant.CHATTYPE_SINGLE);

                        if(message.getFrom().contains(HXConstant.SYS_MSG)){
                            intent.putExtra(HXConstant.GOTO_DETAIL,true);
                        }
                    } else { // 群聊信息
                        // message.getTo()为群聊id
                        intent.putExtra(EaseConstant.EXTRA_USER_ID, message.getTo());
                        if (chatType == ChatType.GroupChat) {
                            intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, HXConstant.CHATTYPE_GROUP);
                        } else {
                            intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, HXConstant.CHATTYPE_CHATROOM);
                        }

                    }
                }
                return intent;
            }
        });
    }

    /**
     * 设置全局事件监听
     */
    protected void setGlobalListeners() {
        syncGroupsListeners = new ArrayList<DataSyncListener>();
        syncContactsListeners = new ArrayList<DataSyncListener>();
        syncBlackListListeners = new ArrayList<DataSyncListener>();

        isGroupsSyncedWithServer = demoModel.isGroupsSynced();
        isContactsSyncedWithServer = demoModel.isContactSynced();
        isBlackListSyncedWithServer = demoModel.isBacklistSynced();

        // create the global connection listener
        connectionListener = new EMConnectionListener() {
            @Override
            public void onDisconnected(int error) {
                if (error == EMError.USER_REMOVED) {
                    onCurrentAccountRemoved();
                } else if (error == EMError.CONNECTION_CONFLICT) {
                    onConnectionConflict();
                }
            }

            @Override
            public void onConnected() {

                // in case group and contact were already synced, we supposed to notify sdk we are ready to receive the events
                if (isGroupsSyncedWithServer && isContactsSyncedWithServer) {
                    new Thread() {
                        @Override
                        public void run() {
                            HXHelper.getInstance().notifyForRecevingEvents();
                        }
                    }.start();
                } else {
                    //                    if (!isGroupsSyncedWithServer) {
                    //                        asyncFetchGroupsFromServer(null);
                    //                    }
                    //
                    //                    if (!isContactsSyncedWithServer) {
                    //                        asyncFetchContactsFromServer(null);
                    //                    }
                    //
                    //                    if (!isBlackListSyncedWithServer) {
                    //                        asyncFetchBlackListFromServer(null);
                    //                    }
                }
            }
        };


        IntentFilter callFilter =
                new IntentFilter(EMChatManager.getInstance().getIncomingCallBroadcastAction());
        if (callReceiver == null) {
            callReceiver = new CallReceiver();
        }

        //注册通话广播接收者
        appContext.registerReceiver(callReceiver, callFilter);
        //注册连接监听
        EMChatManager.getInstance().addConnectionListener(connectionListener);
        //注册群组和联系人监听
        registerGroupAndContactListener();
        //注册消息事件监听
//        registerEventListener();


//        IntentFilter cmdIntentFilter = new IntentFilter(EMChatManager.getInstance().getCmdMessageBroadcastAction());
    }


    private void initDbDao() {
        inviteMessgeDao = new InviteMessgeDao(appContext);
        userDao = new UserDao(appContext);
    }

    /**
     * 注册群组和联系人监听，由于logout的时候会被sdk清除掉，再次登录的时候需要再注册一下
     */
    public void registerGroupAndContactListener() {
        if (!isGroupAndContactListenerRegisted) {
            //注册群组变动监听
            EMGroupManager.getInstance().addGroupChangeListener(new MyGroupChangeListener());
            //注册联系人变动监听
            myContactListener = new MyContactListener();
            EMContactManager.getInstance().setContactListener(myContactListener);
            isGroupAndContactListenerRegisted = true;
        }

    }

    /**
     * 群组变动监听
     */
    class MyGroupChangeListener implements EMGroupChangeListener {

        @Override
        public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {

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
            String st3 = appContext.getString(R.string.Invite_you_to_join_a_group_chat);
            EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
            msg.setChatType(ChatType.GroupChat);
            msg.setFrom(inviter);
            msg.setTo(groupId);
            msg.setMsgId(UUID.randomUUID().toString());
            msg.addBody(new TextMessageBody(inviter + " " + st3));
            // 保存邀请消息
            EMChatManager.getInstance().saveMessage(msg);
            // 提醒新消息
            getNotifier().viberateAndPlayTone(msg);
            //发送local广播
            broadcastManager.sendBroadcast(new Intent(HXConstant.ACTION_GROUP_CHANAGED));
        }

        @Override
        public void onInvitationAccpted(String groupId, String inviter, String reason) {
        }

        @Override
        public void onInvitationDeclined(String groupId, String invitee, String reason) {
        }

        @Override
        public void onUserRemoved(String groupId, String groupName) {
            //TODO 提示用户被T了，demo省略此步骤
            broadcastManager.sendBroadcast(new Intent(HXConstant.ACTION_GROUP_CHANAGED));
        }

        @Override
        public void onGroupDestroy(String groupId, String groupName) {
            // 群被解散
            //TODO 提示用户群被解散,demo省略
            broadcastManager.sendBroadcast(new Intent(HXConstant.ACTION_GROUP_CHANAGED));
        }

        @Override
        public void onApplicationReceived(String groupId, String groupName, String applyer, String reason) {

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
            broadcastManager.sendBroadcast(new Intent(HXConstant.ACTION_GROUP_CHANAGED));
        }

        @Override
        public void onApplicationAccept(String groupId, String groupName, String accepter) {

            String st4 = appContext.getString(R.string.Agreed_to_your_group_chat_application);
            // 加群申请被同意
            EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
            msg.setChatType(ChatType.GroupChat);
            msg.setFrom(accepter);
            msg.setTo(groupId);
            msg.setMsgId(UUID.randomUUID().toString());
            msg.addBody(new TextMessageBody(accepter + " " + st4));
            // 保存同意消息
            EMChatManager.getInstance().saveMessage(msg);
            // 提醒新消息
            getNotifier().viberateAndPlayTone(msg);
            broadcastManager.sendBroadcast(new Intent(HXConstant.ACTION_GROUP_CHANAGED));
        }

        @Override
        public void onApplicationDeclined(String groupId, String groupName, String decliner, String reason) {
            // 加群申请被拒绝，demo未实现
        }
    }

    /***
     * 好友变化listener
     */
    public class MyContactListener implements EMContactListener {

        @Override
        public void onContactAdded(List<String> usernameList) {
            // 保存增加的联系人
            Map<String, EaseYAMUser> localUsers = getYAMContactList();
            Map<String, EaseYAMUser> toAddUsers = new HashMap<String, EaseYAMUser>();
            for (String username : usernameList) {
                EaseYAMUser user = new EaseYAMUser();
                // 添加好友时可能会回调added方法两次
                if (!localUsers.containsKey(username)) {
                    userDao.saveYAMContact(user);
                }
                toAddUsers.put(username, user);
            }
            localUsers.putAll(toAddUsers);
            //发送好友变动广播
            broadcastManager.sendBroadcast(new Intent(HXConstant.ACTION_CONTACT_CHANAGED));
        }

        @Override
        public void onContactDeleted(final List<String> usernameList) {
            // 被删除
            Map<String, EaseYAMUser> localUsers = HXHelper.getInstance().getYAMContactList();
            for (String username : usernameList) {
                localUsers.remove(HXApplication.getInstance().parseIdFromName(username));
                userDao.deleteYAMContact(username);
                inviteMessgeDao.deleteMessage(username);
            }
            broadcastManager.sendBroadcast(new Intent(HXConstant.ACTION_CONTACT_CHANAGED));
        }

        @Override
        public void onContactInvited(String username, String reason) {
            // 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不需要重复提醒
            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();

            for (InviteMessage inviteMessage : msgs) {
                if (inviteMessage.getGroupId() == null && inviteMessage.getFrom().equals(username)) {
                    inviteMessgeDao.deleteMessage(username);
                }
            }
            // 自己封装的javabean
            InviteMessage msg = new InviteMessage();
            msg.setFrom(username);
            msg.setTime(System.currentTimeMillis());
            msg.setReason(reason);
            Log.d(TAG, username + "请求加你为好友,reason: " + reason);
            // 设置相应status
            msg.setStatus(InviteMesageStatus.BEINVITEED);
            notifyNewIviteMessage(msg);
            broadcastManager.sendBroadcast(new Intent(HXConstant.ACTION_CONTACT_CHANAGED));
        }

        @Override
        public void onContactAgreed(String username) {
            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
            for (InviteMessage inviteMessage : msgs) {
                if (inviteMessage.getFrom().equals(username)) {
                    return;
                }
            }
            // 自己封装的javabean
            InviteMessage msg = new InviteMessage();
            msg.setFrom(username);
            msg.setTime(System.currentTimeMillis());
            Log.d(TAG, username + "同意了你的好友请求");
            msg.setStatus(InviteMesageStatus.BEAGREED);
            notifyNewIviteMessage(msg);
            broadcastManager.sendBroadcast(new Intent(HXConstant.ACTION_CONTACT_CHANAGED));
        }

        @Override
        public void onContactRefused(String username) {
            // 参考同意，被邀请实现此功能,demo未实现
            Log.d(username, username + "拒绝了你的好友请求");
        }

    }

    /**
     * 保存并提示消息的邀请消息
     *
     * @param msg
     */
    private void notifyNewIviteMessage(InviteMessage msg) {
        if (inviteMessgeDao == null) {
            inviteMessgeDao = new InviteMessgeDao(appContext);
        }
        inviteMessgeDao.saveMessage(msg);
        //保存未读数，这里没有精确计算
        inviteMessgeDao.saveUnreadMessageCount(1);
        // 提示有新消息
        getNotifier().viberateAndPlayTone(null);
    }

    /**
     * 账号在别的设备登录
     */
    protected void onConnectionConflict() {
        Intent intent = null;
        if (EaseConstant.shopID > 0) {
            intent = new Intent("com.zxly.o2o.activity.MAINPAGE_USER");
        } else if (EaseConstant.shopID < 0) {
            intent = new Intent("com.zxly.o2o.activity.MAINPAGE_SHOP");
        }
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(HXConstant.ACCOUNT_CONFLICT, true);
            appContext.startActivity(intent);
        }
    }

    /**
     * 账号被移除
     */
    protected void onCurrentAccountRemoved() {
        //        Intent intent = new Intent(appContext, MainActivity.class);
        //        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //        intent.putExtra(HXConstant.ACCOUNT_REMOVED, true);
        //        appContext.startActivity(intent);

        showAccountRemovedDialog();
    }

    private android.app.AlertDialog.Builder conflictBuilder;
    private android.app.AlertDialog.Builder accountRemovedBuilder;


    /**
     * 帐号被移除的dialog
     */
    private void showAccountRemovedDialog() {
        Looper.prepare();
        HXHelper.getInstance().logout(true, null);
        String st5 = appContext.getResources()
                .getString(com.easemob.chatuidemo.R.string.Remove_the_notification);
        // clear up global variables
        try {
            if (accountRemovedBuilder == null) {
                accountRemovedBuilder = new android.app.AlertDialog.Builder(
                        appContext);
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
                                    EaseUI.exitAct();
                                }
                            });
            accountRemovedBuilder.setCancelable(false);
            accountRemovedBuilder.create().show();
            Looper.loop();
        } catch (Exception e) {
            EMLog.e("MainActivity", "---------color userRemovedBuilder error" + e.getMessage());
        }
    }


    /**
     * 显示帐号在别处登录dialog
     */
    private void showConflictDialog() {
        Looper.prepare();
        HXHelper.getInstance().logout(true, null);
        String st = appContext.getResources().getString(R.string.Logoff_notification);
        // clear up global variables
        try {
            if (conflictBuilder == null) {
                conflictBuilder = new android.app.AlertDialog.Builder(EaseUI.getInstance().getTopAct());
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
                                    EaseUI.exitAct();
                                }
                            });
            conflictBuilder.setCancelable(false);
            conflictBuilder.create().show();
            Looper.loop();
        } catch (Exception e) {
            EMLog.e("", "---------color conflictBuilder error" + e.getMessage());
        }


    }

    public EaseYAMUser getUserInfo(String username) {
        //获取user信息，demo是从内存的好友列表里获取，
        //实际开发中，可能还需要从服务器获取用户信息,
        //从服务器获取的数据，最好缓存起来，避免频繁的网络请求
        EaseYAMUser user;
        if (username.equals(EaseConstant.currentUser.getFirendsUserInfo().getEid())) {
            return EaseConstant.currentUser;
        }
        user = getYAMContactList().get(username);
        //TODO 获取不在好友列表里的群成员具体信息，即陌生人信息，demo未实现
        //        if (user == null && getRobotList() != null) {
        //            user = getRobotList().get(username);
        //        }
        return user;
    }

    public static boolean inSleepTime() {
        Calendar calendar = Calendar.getInstance();
        int curHour = calendar.get(Calendar.HOUR_OF_DAY);
        if (curHour < 8 || curHour > 22) {
            return true;
        }
        return false;
    }

    /**
     * 全局事件监听
     * 因为可能会有UI页面先处理到这个消息，所以一般如果UI页面已经处理，这里就不需要再次处理
     * activityList.size() <= 0 意味着所有页面都已经在后台运行，或者已经离开Activity Stack
     */
    protected void registerEventListener() {
        eventListener = new EMEventListener() {
            @Override
            public void onEvent(EMNotifierEvent event) {
                EMMessage message = null;
                if (event.getData() instanceof EMMessage) {
                    message = (EMMessage) event.getData();
                }

                for (int i = 0; i < msgReceivedListeners.size(); i++) {
                    msgReceivedListeners.get(i).onCall(message);
                }

//                if (!PreferenceManager.getInstance().getNotifyLogout() &&
//                        EaseCommonUtils.getTopActivity(HXApplication.applicationContext) == null) {
//                    return;
//                } else if (PreferenceManager.getInstance().getNotifySleep() && inSleepTime()) {
//                    return;
//                } else
                if (message != null && message.getFrom().contains(HXConstant.SYS_MSG)) { //系统消息
                    try {
                        int what = message.getIntAttribute("what");

                        if (what == HXConstant.SYS_REGIST_KEY_SHOP) {
                            //新注册成员消息执行更新本地联系人列表
                            //                        HXApplication.getInstance().addNewMemberToDB(message);
                        }

//                        if (!PreferenceManager.getInstance().getNotifySystem()) {
//                            return;
//                        }
                        //                        else if (!PreferenceManager.getInstance().getNotifyFeedback() &&
                        //                                HXConstant.SYS_FEEDBACK_KEY_SHOP == what) {
                        //                            return;
                        //                        } else if (!PreferenceManager.getInstance().getNotifyOrder() &&
                        //                                what >= HXConstant.SYS_ORDER_KEY_SHOP &&
                        //                                what < HXConstant.SYS_ORDER_KEY_SHOP + 50) {
                        //                            return;
                        //                        }

                    } catch (EaseMobException e) {
                        goToNextStep(event, message);
                        e.printStackTrace();
                    }
                }


                goToNextStep(event, message);
            }
        };

        EMChatManager.getInstance().registerEventListener(eventListener);
//        EMChat.getInstance().setAppInited();
    }

    private void goToNextStep(EMNotifierEvent event, EMMessage message) {
        BroadcastReceiver broadCastReceiver = null;
        switch (event.getEvent()) {
            case EventNewMessage:
                //应用在后台，不需要刷新UI,通知栏提示新消息
                if (!easeUI.hasForegroundActivies()) {
                    getNotifier().onNewMsg(message);
                }
                break;
            case EventOfflineMessage:
                if (!easeUI.hasForegroundActivies()) {
                    EMLog.d(TAG, "received offline messages");
                    List<EMMessage> messages = (List<EMMessage>) event.getData();
                    getNotifier().onNewMesg(messages);
                }
                break;
            // below is just giving a example to show a cmd toast, the app should not follow this
            // so be careful of this
            case EventNewCMDMessage:

                //                EMLog.e(TAG, "收到透传消息");
                //获取消息body
                CmdMessageBody cmdMsgBody = (CmdMessageBody) message.getBody();
                final String action = cmdMsgBody.action;//获取自定义action
                try {
                    if (message.getFrom().contains(HXConstant.SYS_MSG)) {
                        //获取自定义图文扩展属性 此处省略
                        String objectString = message.getStringAttribute("expend");
                        //show
                        getNotifier().onNewTuWenMsg(message, false, objectString);
                    } else {
                        if (HXConstant.CMD_ACTION_NEWFRIEND.equals(action)) {
                            //获取添加好友扩展属性 此处省略
                            String friendId = message.getStringAttribute(HXConstant.NEW_FRIENDS_USERNAME);
                            //add friend
                            //                            new EaseAddFriendRequest(Long.valueOf(friendId)).start();

                        } else if (HXConstant.CMD_ACTION_TUWEN.equals(action)) {
                            //获取自定义图文扩展属性 此处省略
                            String objectString = message.getStringAttribute(HXConstant.SYS_TUWEN_PUSH);
                            //show
                            getNotifier().onNewTuWenMsg(message, false, objectString);
                            return;
                        }
                    }
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }


                //                EMLog.d(TAG, String.format("%s,message:%s", action, message.toString()));
                //                final String str = appContext.getString(R.string.receive_the_passthrough);
                //
                //                final String CMD_TOAST_BROADCAST = "easemob.demo.cmd.toast";
                //                IntentFilter cmdFilter = new IntentFilter(CMD_TOAST_BROADCAST);
                //
                //                if (broadCastReceiver == null) {
                //                    broadCastReceiver = new BroadcastReceiver() {
                //
                //                        @Override
                //                        public void onReceive(Context context, Intent intent) {
                //                            // TODO Auto-generated method stub
                //                            Toast.makeText(appContext, intent.getStringExtra("cmd_value"),
                //                                    Toast.LENGTH_SHORT).show();
                //                        }
                //                    };
                //
                ////                    注册广播接收者
                //                    appContext.registerReceiver(broadCastReceiver, cmdFilter);
                //                }
                //
                //                Intent broadcastIntent = new Intent(CMD_TOAST_BROADCAST);
                //                broadcastIntent.putExtra("cmd_value", str + action);
                //                appContext.sendBroadcast(broadcastIntent, null);

                break;

            case EventDeliveryAck:
                message.setDelivered(true);
                break;
            case EventReadAck:
                message.setAcked(true);
                break;
            // add other events in case you are interested in
            default:
                break;
        }
    }

    /**
     * 是否登录成功过
     *
     * @return
     */
    public boolean isLoggedIn() {
        return EMChat.getInstance().isLoggedIn();
    }

    /**
     * 退出登录
     *
     * @param unbindDeviceToken 是否解绑设备token(使用GCM才有)
     * @param callback          callback
     */
    public void logout(boolean unbindDeviceToken, final EMCallBack callback) {
        endCall();
        EMChatManager.getInstance().logout(unbindDeviceToken, new EMCallBack() {

            @Override
            public void onSuccess() {
                reset();
                if (callback != null) {
                    callback.onSuccess();
                }

            }

            @Override
            public void onProgress(int progress, String status) {
                if (callback != null) {
                    callback.onProgress(progress, status);
                }
            }

            @Override
            public void onError(int code, String error) {
                if (callback != null) {
                    callback.onError(code, error);
                }
            }
        });
    }

    /**
     * 获取消息通知类
     *
     * @return
     */
    public EaseNotifier getNotifier() {
        return easeUI.getNotifier();
    }

    public HXModel getModel() {
        return demoModel;
    }

    /**
     * 设置好友user list到内存中
     *
     * @param contactList
     */
    public void setYAMContactList(Map<String, EaseYAMUser> contactList) {
        this.yamContactMap = contactList;
    }

    /**
     * 保存单个user
     */
    public void saveYAMContact(EaseYAMUser user) {
        yamContactMap.put(user.getFirendsUserInfo().getEid(), user);
        demoModel.saveYAMContact(user);
    }

    /**
     * 获取好友list
     *
     * @return
     */
    public Map<String, EaseYAMUser> getYAMContactList() {
        if (yamContactMap == null) {
            yamContactMap = new HashMap<String, EaseYAMUser>();
        }
        if (demoModel != null && (yamContactMap.size() == 0 ||
                yamContactMap.size() < 3)) {
            yamContactMap = demoModel.getYAMContactList();
            if(EaseConstant.shopID<0) {
                setContactTopInfo();
            }
            getYAMAndFilterContactList();
        }

        return yamContactMap;
    }

    private void setContactTopInfo() {
        if (yamContactMap.size() > 0) {
            TypeToken<ArrayList<Long>> token =
                    new TypeToken<ArrayList<Long>>() {
                    };
            try {
                ArrayList<Long> tops = GsonParser.getInstance()
                        .fromJson(PreferenceManager.getInstance().getTopContacts(), token);
                if(tops!=null&&tops.size()>0) {
                    for (Long id : tops) {
                        String hxId=HXApplication.getInstance().parseUserFromID(id, HXConstant.TAG_USER);
                       if( yamContactMap.containsKey(hxId)) {
                           yamContactMap.get(hxId).getFirendsUserInfo().setIsTop(1);
                       }
                    }
                }
            } catch (AppException e) {
                e.printStackTrace();
            }
        }
    }

    public void cleanCheckMembers() {
        if (yamContactList != null) {
            for (int i = 0; i < yamContactList.size(); i++) {
                yamContactList.get(i).setIsCheck(false);
            }
        }
    }


    public List<EaseYAMUser> getYAMAndFilterContactList() {
        synchronized (yamContactMap) {
            //获取联系人列表
            if (yamContactMap == null || yamContactMap.size() == 0) {
                return new ArrayList<EaseYAMUser>();
            }
            yamContactList.clear();
            for (Map.Entry<String, EaseYAMUser> entry : yamContactMap.entrySet()) {
                //兼容以前的通讯录里的已有的数据显示，加上此判断，如果是新集成的可以去掉此判断
                //                if (!entry.getKey().equals("item_new_friends")
                //                        && !entry.getKey().equals("item_groups")
                //                        && !entry.getKey().equals("item_chatroom")
                //                        && !entry.getKey().equals("item_robots")) {
                //                    if (!blackList.contains(entry.getKey())) {
                //不显示黑名单中的用户
                EaseYAMUser user = entry.getValue();
                EaseCommonUtils.setUserInitialLetter(user.getFirendsUserInfo());
                yamContactList.add(user);
                //                    }
                //                }
            }
            // 排序
            if (yamContactList.size() > 0) {
                Collections.sort(yamContactList, new Comparator<EaseYAMUser>() {

                    @Override
                    public int compare(EaseYAMUser lhs, EaseYAMUser rhs) {
                        if (1 == lhs.getFirendsUserInfo().getIsTop()) {

                            if (lhs.getFirendsUserInfo().getIsTop() == rhs.getFirendsUserInfo().getIsTop()) {
                                if (EaseConstant.shopID > 0) {   //兼容用户app 用户app的业务员列表没有nickname
                                    return lhs.getFirendsUserInfo().getInitialLetter()
                                            .compareTo(rhs.getFirendsUserInfo().getInitialLetter());

                                } else {
                                    return lhs.getFirendsUserInfo().getNickname()
                                            .compareTo(rhs.getFirendsUserInfo().getNickname());
                                }
                            } else {
                                return -1;
                            }
                        } else if (1 == rhs.getFirendsUserInfo().getIsTop()) {
                            return 1;
                        } else {
                            if (lhs.getFirendsUserInfo().getInitialLetter()
                                    .equals(rhs.getFirendsUserInfo().getInitialLetter())) {
                                return lhs.getFirendsUserInfo().getNickname()
                                        .compareTo(rhs.getFirendsUserInfo().getNickname());
                            } else {
                                if ("#".equals(lhs.getFirendsUserInfo().getInitialLetter())) {
                                    return 1;
                                } else if ("#".equals(rhs.getFirendsUserInfo().getInitialLetter())) {
                                    return -1;
                                }
                                return lhs.getFirendsUserInfo().getInitialLetter()
                                        .compareTo(rhs.getFirendsUserInfo().getInitialLetter());
                            }
                        }
                    }
                });
            }
        }
        return yamContactList;
    }

    public void deleteDBContactCache() {
        demoModel.deleteContactList();
    }

    public void cleanYAMContactList() {
        if (yamContactMap != null) {
            yamContactMap.clear();
        }
        if (yamContactList != null) {
            yamContactList.clear();
        }
    }

    //黑名单列表
    public List<String> getBlackList() {
        //        blackList = EMContactManager.getInstance().getBlackListUsernames();
        return blackList;
    }

    /**
     * 设置当前用户的环信id
     *
     * @param username
     */
    public void setCurrentUserName(String username) {
        this.username = username;
        demoModel.setCurrentUserName(username);
    }

    /**
     * 获取当前用户的环信id
     */
    public String getCurrentUsernName() {
        if (username == null) {
            username = demoModel.getCurrentUsernName();
        }
        return username;
    }

    public void setRobotList(Map<String, RobotUser> robotList) {
        this.robotList = robotList;
    }

    public Map<String, RobotUser> getRobotList() {
        if (isLoggedIn() && robotList == null) {
            robotList = demoModel.getRobotList();
        }
        return robotList;
    }

    /**
     * update user list to cach And db
     */
    public void updateYAMContactMap(List<EaseYAMUser> contactInfoList) {
        for (EaseYAMUser u : contactInfoList) {
            yamContactMap.put(u.getFirendsUserInfo().getEid(), u);
        }
        ArrayList<EaseYAMUser> mList = new ArrayList<EaseYAMUser>();
        mList.addAll(yamContactMap.values());
        demoModel.saveYAMContactList(mList);
    }

    public UserProfileManager getUserProfileManager() {
        if (userProManager == null) {
            userProManager = new UserProfileManager();
        }
        return userProManager;
    }

    void endCall() {
        try {
            EMChatManager.getInstance().endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addSyncGroupListener(DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (!syncGroupsListeners.contains(listener)) {
            syncGroupsListeners.add(listener);
        }
    }

    public void removeSyncGroupListener(DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (syncGroupsListeners.contains(listener)) {
            syncGroupsListeners.remove(listener);
        }
    }

    public void addSyncContactListener(DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (!syncContactsListeners.contains(listener)) {
            syncContactsListeners.add(listener);
        }
    }

    public void removeSyncContactListener(DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (syncContactsListeners.contains(listener)) {
            syncContactsListeners.remove(listener);
        }
    }

    public void addSyncBlackListListener(DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (!syncBlackListListeners.contains(listener)) {
            syncBlackListListeners.add(listener);
        }
    }

    public void removeSyncBlackListListener(DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (syncBlackListListeners.contains(listener)) {
            syncBlackListListeners.remove(listener);
        }
    }

    /**
     * 同步操作，从服务器获取群组列表
     * 该方法会记录更新状态，可以通过isSyncingGroupsFromServer获取是否正在更新
     * 和isGroupsSyncedWithServer获取是否更新已经完成
     *
     * @throws EaseMobException
     */
    public synchronized void asyncFetchGroupsFromServer(final EMCallBack callback) {
        if (isSyncingGroupsWithServer) {
            return;
        }

        isSyncingGroupsWithServer = true;

        new Thread() {
            @Override
            public void run() {
                try {
                    EMGroupManager.getInstance().getGroupsFromServer();

                    // in case that logout already before server returns, we should return immediately
                    if (!EMChat.getInstance().isLoggedIn()) {
                        return;
                    }

                    demoModel.setGroupsSynced(true);

                    isGroupsSyncedWithServer = true;
                    isSyncingGroupsWithServer = false;

                    //通知listener同步群组完毕
                    noitifyGroupSyncListeners(true);
                    if (isContactsSyncedWithServer()) {
                        notifyForRecevingEvents();
                    }
                    if (callback != null) {
                        callback.onSuccess();
                    }
                } catch (EaseMobException e) {
                    demoModel.setGroupsSynced(false);
                    isGroupsSyncedWithServer = false;
                    isSyncingGroupsWithServer = false;
                    noitifyGroupSyncListeners(false);
                    if (callback != null) {
                        callback.onError(e.getErrorCode(), e.toString());
                    }
                }

            }
        }.start();
    }

    public void noitifyGroupSyncListeners(boolean success) {
        for (DataSyncListener listener : syncGroupsListeners) {
            listener.onSyncComplete(success);
        }
    }

    public void asyncFetchContactsFromServer(final EMValueCallBack<List<String>> callback) {
        if (isSyncingContactsWithServer) {
            return;
        }

        isSyncingContactsWithServer = true;

        new Thread() {
            @Override
            public void run() {
                List<String> usernames;
                try {
                    usernames = EMContactManager.getInstance().getContactUserNames();
                    // in case that logout already before server returns, we should return immediately
                    if (!EMChat.getInstance().isLoggedIn()) {
                        return;
                    }

                    Map<String, EaseYAMUser> userlist = new HashMap<String, EaseYAMUser>();
                    for (String username : usernames) {
                        EaseYAMUser user = new EaseYAMUser();
                        EaseCommonUtils.setUserInitialLetter(user.getFirendsUserInfo());
                        userlist.put(username, user);
                    }
                    // 存入内存
                    getYAMContactList().clear();
                    getYAMContactList().putAll(userlist);
                    // 存入db
                    UserDao dao = new UserDao(appContext);
                    List<EaseYAMUser> users = new ArrayList<EaseYAMUser>(userlist.values());
                    dao.saveYAMContactList(users);

                    demoModel.setContactSynced(true);
                    EMLog.d(TAG, "set contact syn status to true");

                    isContactsSyncedWithServer = true;
                    isSyncingContactsWithServer = false;

                    //通知listeners联系人同步完毕
                    notifyContactsSyncListener(true);
                    if (isGroupsSyncedWithServer()) {
                        notifyForRecevingEvents();
                    }


                    getUserProfileManager().asyncFetchContactInfosFromServer(usernames,
                            new EMValueCallBack<List<EaseYAMUser>>() {

                                @Override
                                public void onSuccess(List<EaseYAMUser> uList) {
                                    updateYAMContactMap(uList);
                                    getUserProfileManager().notifyContactInfosSyncListener(true);
                                }

                                @Override
                                public void onError(int error, String errorMsg) {
                                }
                            });
                    if (callback != null) {
                        callback.onSuccess(usernames);
                    }
                } catch (EaseMobException e) {
                    demoModel.setContactSynced(false);
                    isContactsSyncedWithServer = false;
                    isSyncingContactsWithServer = false;
                    noitifyGroupSyncListeners(false);
                    e.printStackTrace();
                    if (callback != null) {
                        callback.onError(e.getErrorCode(), e.toString());
                    }
                }

            }
        }.start();
    }

    public void notifyContactsSyncListener(boolean success) {
        for (DataSyncListener listener : syncContactsListeners) {
            listener.onSyncComplete(success);
        }
    }

    public void asyncFetchBlackListFromServer(final EMValueCallBack<List<String>> callback) {

        if (isSyncingBlackListWithServer) {
            return;
        }

        isSyncingBlackListWithServer = true;

        new Thread() {
            @Override
            public void run() {
                try {
                    List<String> usernames = EMContactManager.getInstance().getBlackListUsernamesFromServer();

                    // in case that logout already before server returns, we should return immediately
                    if (!EMChat.getInstance().isLoggedIn()) {
                        return;
                    }

                    demoModel.setBlacklistSynced(true);

                    isBlackListSyncedWithServer = true;
                    isSyncingBlackListWithServer = false;

                    EMContactManager.getInstance().saveBlackList(usernames);
                    notifyBlackListSyncListener(true);
                    if (callback != null) {
                        callback.onSuccess(usernames);
                    }
                } catch (EaseMobException e) {
                    demoModel.setBlacklistSynced(false);

                    isBlackListSyncedWithServer = false;
                    isSyncingBlackListWithServer = true;
                    e.printStackTrace();

                    if (callback != null) {
                        callback.onError(e.getErrorCode(), e.toString());
                    }
                }

            }
        }.start();
    }

    public void notifyBlackListSyncListener(boolean success) {
        for (DataSyncListener listener : syncBlackListListeners) {
            listener.onSyncComplete(success);
        }
    }

    public boolean isSyncingGroupsWithServer() {
        return isSyncingGroupsWithServer;
    }

    public boolean isSyncingContactsWithServer() {
        return isSyncingContactsWithServer;
    }

    public boolean isSyncingBlackListWithServer() {
        return isSyncingBlackListWithServer;
    }

    public boolean isGroupsSyncedWithServer() {
        return isGroupsSyncedWithServer;
    }

    public boolean isContactsSyncedWithServer() {
        return isContactsSyncedWithServer;
    }

    public boolean isBlackListSyncedWithServer() {
        return isBlackListSyncedWithServer;
    }

    public synchronized void notifyForRecevingEvents() {
        if (alreadyNotified) {
            return;
        }

        // 通知sdk，UI 已经初始化完毕，注册了相应的receiver和listener, 可以接受broadcast了
        EMChat.getInstance().setAppInited();
        alreadyNotified = true;
    }

    synchronized void reset() {
        isSyncingGroupsWithServer = false;
        isSyncingContactsWithServer = false;
        isSyncingBlackListWithServer = false;

        demoModel.setGroupsSynced(false);
        demoModel.setContactSynced(false);
        demoModel.setBlacklistSynced(false);

        isGroupsSyncedWithServer = false;
        isContactsSyncedWithServer = false;
        isBlackListSyncedWithServer = false;

        alreadyNotified = false;
        isGroupAndContactListenerRegisted = false;

        setYAMContactList(null);
        setRobotList(null);
        getUserProfileManager().reset();
        DemoDBManager.getInstance().closeDB();
    }

    public void pushActivity(Activity activity) {
        easeUI.addAct(activity);
    }

    public void popActivity(Activity activity) {
        easeUI.remove(activity);
    }

    public void addListener(HXNewMsgCallBack listener) {
        msgReceivedListeners.add(listener);
    }

    public void cleanReceivedListeners(){
        if(msgReceivedListeners!=null) {
            msgReceivedListeners.clear();
        }
    }

    public List<HXNewMsgCallBack> getReceivedListeners(){
        return msgReceivedListeners;
    }

}
