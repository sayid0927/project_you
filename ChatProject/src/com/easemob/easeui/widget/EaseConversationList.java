package com.easemob.easeui.widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.LightingColorFilter;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.widget.ListView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.chatuidemo.HXApplication;
import com.easemob.chatuidemo.R;
import com.easemob.chatuidemo.utils.EaseCallBack;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.adapter.EaseConversationAdapater;
import com.easemob.easeui.model.GeTuiConversation;
import com.easemob.easeui.request.GetuiTypeDataRequest;
import com.easemob.easeui.request.HXNormalRequest;
import com.easemob.easeui.ui.EaseConversationListFragment;


public class EaseConversationList extends ListView {

    protected int primaryColor;
    protected int secondaryColor;
    protected int timeColor;
    protected int primarySize;
    protected int secondarySize;
    protected float timeSize;

    protected final int MSG_REFRESH_ADAPTER_DATA = 0;

    protected Context context;
    protected EaseConversationAdapater adapter;
    protected List<EMConversation> conversationList = new ArrayList<EMConversation>();
    private EaseCallBack callBack;
    private List<GeTuiConversation> getuiMsgList;
    public boolean onFail;

    public EaseConversationList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EaseConversationList(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EaseConversationList);
        primaryColor = ta.getColor(R.styleable.EaseConversationList_cvsListPrimaryTextColor,
                R.color.list_itease_primary_color);
        secondaryColor = ta.getColor(R.styleable.EaseConversationList_cvsListSecondaryTextColor,
                R.color.list_itease_secondary_color);
        timeColor = ta.getColor(R.styleable.EaseConversationList_cvsListTimeTextColor,
                R.color.list_itease_secondary_color);
        primarySize = ta.getDimensionPixelSize(R.styleable.EaseConversationList_cvsListPrimaryTextSize, 0);
        secondarySize =
                ta.getDimensionPixelSize(R.styleable.EaseConversationList_cvsListSecondaryTextSize, 0);
        timeSize = ta.getDimension(R.styleable.EaseConversationList_cvsListTimeTextSize, 0);

        ta.recycle();

    }

    public void init(List<EMConversation> conversationList) {
        this.conversationList = conversationList;
        adapter = new EaseConversationAdapater(context, 0, conversationList);
        adapter.setPrimaryColor(primaryColor);
        adapter.setPrimarySize(primarySize);
        adapter.setSecondaryColor(secondaryColor);
        adapter.setSecondarySize(secondarySize);
        adapter.setTimeColor(timeColor);
        adapter.setTimeSize(timeSize);
        setAdapter(adapter);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case MSG_REFRESH_ADAPTER_DATA:
                    if (adapter != null&&EaseConstant.EaseConversationListscrollState==0) {
                        updateGetuiMsgs();//每次刷新更新个推数据
                        conversationList.clear();//这句虽与下面run（）方法中重复  但不加的话有时在加载消息列表的时候偶尔会出现重复的情况
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                conversationList.clear();
                                getuiMsgList = HXApplication.getInstance().getGetuiMsgList();
                                conversationList.addAll(loadConversationsWithRecentChat());
                                adapter.notifyDataSetChanged();
                                if(callBack!=null){
                                    callBack.onCall();
                                }
                            }
                        },400);
                    }
                    break;
                default:
                    break;
            }
        }
    };


    /**
     * 获取所有会话
     *
     * @param
     * @return
     */
    public List<EMConversation> loadConversationsWithRecentChat() {
        // 获取所有会话，包括陌生人
        Map<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
        // 过滤掉messages size为0的conversation
        /**
         * 如果在排序过程中有新消息收到，lastMsgTime会发生变化
         * 影响排序过程，Collection.sort会产生异常
         * 保证Conversation在Sort过程中最后一条消息的时间不变
         * 避免并发问题
         */
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        synchronized (conversations) {
            int unRead = 0;
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(),
                            conversation));
                    unRead += conversation.getUnreadMsgCount();
                }
            }

            HXApplication.getInstance().unReadMsgCount = unRead+HXApplication.getInstance().getGetuiUndreadcount();
        }
        if(HXApplication.getInstance().getGetuiMsgList().size()!=0){
            for(int i=0;i<HXApplication.getInstance().getGetuiMsgList().size();i++)  {
                sortList.add(new Pair<Long, EMConversation>(HXApplication.getInstance().getGetuiMsgList().get(i).getUpdateTime(),HXApplication.getInstance().getGetuiMsgList().get(i)));
            }
        }

        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }

//        list.addAll(HXApplication.getInstance().getGetuiMsgList());
//        getuiMsgList = HXApplication.getInstance().getGetuiMsgList();
        return list;
    }

    public void updateGetuiMsgs() {
        final GetuiTypeDataRequest getuiTypeDataRequest=new GetuiTypeDataRequest(EaseConstant.shopID);
        getuiTypeDataRequest.start();
        getuiTypeDataRequest.setOnResponseStateListener(new HXNormalRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                HXApplication.getInstance().setGetuiMsgList(getuiTypeDataRequest.emConversationList);
                setNetDataUnread(getuiTypeDataRequest.emConversationList);
                onFail=false;
            }

            @Override
            public void onFail(int code) {
                onFail =true;
            }
        });
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
     * 根据最后一条消息的时间排序
     *
     * @param
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

    public EMConversation getItem(int position) {
        return adapter.getItem(position);
    }

    public void refresh(EaseCallBack callBack) {
        //conversationList = loadConversationsWithRecentChat();
        this.callBack = callBack;
        handler.sendEmptyMessage(MSG_REFRESH_ADAPTER_DATA);
    }

    public void filter(CharSequence str) {
        adapter.getFilter().filter(str);
    }
}
