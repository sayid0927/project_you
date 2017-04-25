package com.easemob.easeui.ui;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chatuidemo.HXApplication;
import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.HXHelper;
import com.easemob.chatuidemo.R;
import com.easemob.chatuidemo.utils.EaseCallBack;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.adapter.EaseConversationAdapater;
import com.easemob.easeui.controller.EaseUI;
import com.easemob.easeui.request.GetuiTypeDataRequest;
import com.easemob.easeui.request.HXNormalRequest;
import com.easemob.easeui.widget.EaseConversationList;
import com.easemob.easeui.widget.EaseMyFlipperView;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;

import javax.security.auth.callback.Callback;

/**
 * 会话列表fragment
 */
public class EaseConversationListFragment extends EaseBaseFragment implements EMEventListener {
    protected EditText query;
    protected ImageButton clearSearch;
    protected boolean hidden;
    protected List<EMConversation> conversationList = new ArrayList<EMConversation>();
    protected EaseConversationList conversationListView;
    protected FrameLayout errorItemContainer;

    protected boolean isConflict;

    public EaseMyFlipperView viewContainer;
    protected EaseCallBack refreshCallBack;
    private static EaseConversationListFragment instance;
    private long lastClickTime;

    public void setCallBack(EaseCallBack callBack) {
        refreshCallBack = callBack;
    }

    private void setFlipper() {
        if (viewContainer == null) {
            viewContainer = (EaseMyFlipperView) getView().findViewById(R.id.list_layout);
            viewContainer.getRetryBtn().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    viewContainer.setDisplayedChild(0, true);
                    refresh();
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ease_fragment_conversation_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
            return;
        }
        super.onActivityCreated(savedInstanceState);
    }

    public static EaseConversationListFragment getInstance() {
        return instance;
    }

    @Override
    protected void initView() {
        //注册消息事件监听
        EMChatManager.getInstance().registerEventListener(this);
        EMChat.getInstance().setAppInited();
        instance =this;
        setFlipper();

        inputMethodManager =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //会话列表控件
        conversationListView = (EaseConversationList) getView().findViewById(R.id.list);
        if(EaseConstant.shopID<0)
            getView().findViewById(R.id.search_layout).setVisibility(View.GONE);
        // 搜索框
        query = (EditText) getView().findViewById(R.id.query);
        query.clearFocus();
        query.setSelected(false);
        conversationListView.setFocusable(true);
        conversationListView.setSelected(true);
        // 搜索框中清除button
        clearSearch = (ImageButton) getView().findViewById(R.id.search_right);
        errorItemContainer = (FrameLayout) getView().findViewById(R.id.fl_error_item);

        conversationListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                        //设置一个标识，当EaseConversationList正在滚动的时候不加载数据（）
                        //出现bug原因（消息列表快速滑动，同时消息与粉丝页面也来回快速切换导致）
                        //所以设置该标识，当状态处于1或2的时候不加载数据 处理见EaseConversationList--line99
                        EaseConstant.EaseConversationListscrollState=scrollState;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    @Override
    protected void setUpView() {

        if (listItemClickListener != null) {
            conversationListView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    EMConversation conversation = conversationListView.getItem(position);
                    listItemClickListener.onListItemClicked(conversation);
                }
            });
        }

        query.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                conversationListView.filter(s);
                if (s.length() > 0) {
                    clearSearch.setVisibility(View.VISIBLE);
                } else {
                    clearSearch.setVisibility(View.INVISIBLE);
                }
//搜索没消息
//                if(((EaseConversationAdapater)conversationListView.getAdapter()).getFilterCount()==0&&EaseConstant.shopID<0){
//                    viewContainer.setDisplayedChild(EaseMyFlipperView.NODATA, false);
//                    viewContainer.setEmptyImg(R.drawable.img_default_tired,"您还没有收到消息呢!");
//                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        clearSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                query.getText().clear();
                hideSoftKeyboard();
            }
        });

        conversationListView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard();
                return false;
            }
        });
        loadData();
    }

    private void loadData()
    {
                conversationList.addAll(conversationListView.loadConversationsWithRecentChat());
                conversationListView.init(conversationList);

        EMChatManager.getInstance().addConnectionListener(connectionListener);
    }

    protected EMConnectionListener connectionListener = new EMConnectionListener() {

        @Override
        public void onDisconnected(int error) {
            if (error == EMError.USER_REMOVED || error == EMError.CONNECTION_CONFLICT) {
                isConflict = true;
            } else {
                handler.sendEmptyMessage(0);
            }
        }

        @Override
        public void onConnected() {
            handler.sendEmptyMessage(1);
        }
    };
    private EaseConversationListItemClickListener listItemClickListener;

    protected Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    onConnectionDisconnected();
                    break;
                case 1:
                    onConnectionConnected();
                    break;

                default:
                    break;
            }
        }
    };

    /**
     * 连接到服务器
     */
    protected void onConnectionConnected() {
        errorItemContainer.setVisibility(View.GONE);
    }

    /**
     * 连接断开
     */
    protected void onConnectionDisconnected() {
        errorItemContainer.setVisibility(View.GONE);
    }

    /**
     * 刷新页面
     */
    public void refresh() {
            if (!TextUtils.isEmpty(query.getText())&&EaseConstant.shopID>0) {
                query.setText("");
            }
            conversationListView.refresh(new EaseCallBack() {
                @Override
                public void onCall() {
                    display();
                }
            });
            display();
    }

    private void display() {
        if (conversationListView.getCount() == 0 && HXApplication.getInstance().unReadMsgCount == 0) {
            viewContainer.setDisplayedChild(EaseMyFlipperView.NODATA, false);
            if(EaseConstant.shopID<0){
                hideSoftKeyboard();
                if(conversationListView.onFail){
                    viewContainer.setEmptyImg(R.drawable.img_default_shy,"您的手机网络不太顺畅哦");
                }else {
                    viewContainer.setEmptyImg(R.drawable.img_default_tired,"您还没有收到消息呢!");
                }

                viewContainer.getEmptyBtn("找人聊聊").setVisibility(View.GONE);
            }
        } else {
            viewContainer.setDisplayedChild(EaseMyFlipperView.LOADSUCCESSFUL, false);
        }
        if(EaseConstant.shopID<0&&conversationListView.onFail){
            hideSoftKeyboard();
            viewContainer.setDisplayedChild(EaseMyFlipperView.NODATA, false);
            viewContainer.setEmptyImg(R.drawable.img_default_shy,"您的手机网络不太顺畅哦");
//            viewContainer.getEmptyBtn("找人聊聊").setVisibility(View.VISIBLE);
                viewContainer.getEmptyBtn("重新加载").setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewContainer.setDisplayedChild(0, true);
                        long currentTime = Calendar.getInstance().getTimeInMillis();
                        if (currentTime - lastClickTime > 1500) {
                            lastClickTime = currentTime;
                            refresh();
                        }
                    }
                });
        }
        if (refreshCallBack != null) {
            refreshCallBack.onCall();
        }

    }

    /**
     * 获取会话列表
     *
     * @return +
     */
    //    protected List<EMConversation> loadConversationList() {
    //        // 获取所有会话，包括陌生人
    //        Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
    //        // 过滤掉messages size为0的conversation
    //        /**
    //         * 如果在排序过程中有新消息收到，lastMsgTime会发生变化
    //         * 影响排序过程，Collection.sort会产生异常
    //         * 保证Conversation在Sort过程中最后一条消息的时间不变
    //         * 避免并发问题
    //         */
    //        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
    //        synchronized (conversations) {
    //            for (EMConversation conversation : conversations.values()) {
    //                if (conversation.getAllMessages().size() != 0) {
    //                    //if(conversation.getType() != EMConversationType.ChatRoom){
    //                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(),
    //                            conversation));
    //                    //}
    //                }
    //            }
    //        }
    //        try {
    //            // Internal is TimSort algorithm, has bug
    //            sortConversationByLastChatTime(sortList);
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //        List<EMConversation> list = new ArrayList<EMConversation>();
    //        for (Pair<Long, EMConversation> sortItem : sortList) {
    //            list.add(sortItem.second);
    //        }
    //        return list;
    //    }

    /**
     * 根据最后一条消息的时间排序
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first == con2.first) {
                    return 0;
                } else if (con2.first > con1.first) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    protected void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode !=
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden && !isConflict) {
            refresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hidden) {
            refresh();
            if (refreshCallBack != null) {
                refreshCallBack.onCall();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMChatManager.getInstance().removeConnectionListener(connectionListener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isConflict) {
            outState.putBoolean("isConflict", true);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onEvent(EMNotifierEvent emNotifierEvent) {
        EMMessage message = null;
        if (emNotifierEvent.getData() instanceof EMMessage) {
            message = (EMMessage) emNotifierEvent.getData();
        }else{
            return;
        }

        //        if (message != null && message.getFrom().contains(HXConstant.SYS_MSG)) { //系统消息
        //            try {
        //                int what = message.getIntAttribute("what");
        //
        //                if (what == HXConstant.SYS_REGIST_KEY_SHOP) {
        //                }
        //
        //
        //            } catch (EaseMobException e) {
        //                goToNextStep(emNotifierEvent, message);
        //                e.printStackTrace();
        //            }
        //        }
        goToNextStep(emNotifierEvent, message);
    }


    private void goToNextStep(EMNotifierEvent event, EMMessage message) {
        BroadcastReceiver broadCastReceiver = null;
        switch (event.getEvent()) {
            case EventNewMessage:
                //应用在后台，不需要刷新UI,通知栏提示新消息
                EaseUI.getInstance().getNotifier().onNewMsg(message);
                refresh();
                callBack(message);
                break;
            case EventOfflineMessage:
                List<EMMessage> messages = (List<EMMessage>) event.getData();
                EaseUI.getInstance().getNotifier().onNewMesg(messages);
                refresh();
                callBack(message);
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
                        EaseUI.getInstance().getNotifier().onNewTuWenMsg(message, false, objectString);
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
                            EaseUI.getInstance().getNotifier().onNewTuWenMsg(message, false, objectString);
                            return;
                        }
                    }
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }

                break;

            case EventDeliveryAck:
                message.setDelivered(true);
                break;
            case EventReadAck:
                message.setAcked(true);
                break;
            case EventConversationListChanged: {
                refresh();
                break;
            }
            // add other events in case you are interested in
            default:
                break;
        }

    }

    private void callBack(EMMessage message) {
//        if (refreshCallBack != null) {
//            refreshCallBack.onCall();
//        }

        for (int i = 0; i < HXHelper.getInstance().getReceivedListeners().size(); i++) {
            HXHelper.getInstance().getReceivedListeners().get(i).onCall(message);
        }
    }

    public interface EaseConversationListItemClickListener {
        /**
         * 会话listview item点击事件
         *
         * @param conversation 被点击item所对应的会话
         */
        void onListItemClicked(EMConversation conversation);
    }

    /**
     * 设置listview item点击事件
     *
     * @param listItemClickListener
     */
    public void setConversationListItemClickListener(
            EaseConversationListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }


}
