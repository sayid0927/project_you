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
package com.easemob.easeui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.MessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.HXHelper;
import com.easemob.chatuidemo.R;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.domain.EaseYAMUser;
import com.easemob.easeui.utils.GsonParser;
import com.easemob.easeui.widget.EaseChatMessageList.MessageListItemClickListener;
import com.easemob.easeui.widget.chatrow.EaseChatRow;
import com.easemob.easeui.widget.chatrow.EaseChatRowFile;
import com.easemob.easeui.widget.chatrow.EaseChatRowImage;
import com.easemob.easeui.widget.chatrow.EaseChatRowLocation;
import com.easemob.easeui.widget.chatrow.EaseChatRowSys;
import com.easemob.easeui.widget.chatrow.EaseChatRowText;
import com.easemob.easeui.widget.chatrow.EaseChatRowTuWen;
import com.easemob.easeui.widget.chatrow.EaseChatRowVideo;
import com.easemob.easeui.widget.chatrow.EaseChatRowVoice;
import com.easemob.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.easemob.easeui.widget.chatrow.EaseWelcome;
import com.easemob.exceptions.EaseMobException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EaseMessageAdapter extends ArrayAdapter<EMMessage> {

    private final static String TAG = "msg";

    private Context context;

    private static final int HANDLER_MESSAGE_REFRESH_LIST = 0;
    private static final int HANDLER_MESSAGE_SELECT_LAST = 1;
    private static final int HANDLER_MESSAGE_SEEK_TO = 2;
    private static final int HANDLER_MESSAGE_SELECT_FIRST = 3;

    //    private static final int MESSAGE_TYPE_RECV_TXT = 0;
    //    private static final int MESSAGE_TYPE_SENT_TXT = 1;
    //    private static final int MESSAGE_TYPE_SENT_IMAGE = 2;
    //    private static final int MESSAGE_TYPE_SENT_LOCATION = 3;
    //    private static final int MESSAGE_TYPE_RECV_LOCATION = 4;
    //    private static final int MESSAGE_TYPE_RECV_IMAGE = 5;
    //    private static final int MESSAGE_TYPE_SENT_VOICE = 6;
    //    private static final int MESSAGE_TYPE_RECV_VOICE = 7;
    //    private static final int MESSAGE_TYPE_SENT_VIDEO = 8;
    //    private static final int MESSAGE_TYPE_RECV_VIDEO = 9;
    //    private static final int MESSAGE_TYPE_SENT_FILE = 10;
    //    private static final int MESSAGE_TYPE_RECV_FILE = 11;
    //    private static final int MESSAGE_TYPE_SENT_TUWEN = 12;
    //    private static final int MESSAGE_TYPE_RECV_TUWEN = 13;


    public int itemTypeCount;

    // reference to conversation object in chatsdk
    //    List<EMMessage> messages = null;

    private String toChatUsername;

    private MessageListItemClickListener itemClickListener;
    private EaseCustomChatRowProvider customRowProvider;

    private boolean showUserNick;
    private boolean showAvatar;
    private Drawable myBubbleBg;
    private Drawable otherBuddleBg;

    private ListView listView;
    private int chatType;
    EMConversation conversation;

    public EaseMessageAdapter(Context context, String username, int chatType, ListView listView,
                              List<EMMessage>
                                      messages) {
        super(context, 0, messages);
        this.context = context;
        this.listView = listView;
        toChatUsername = username;
        this.chatType = chatType;

        //添加欢迎词
        addWelcome(username);
    }

    public void setConversation(EMConversation conversation) {
        this.conversation = conversation;

    }

    private void addWelcome(String username) {
        //一天只显示两次
        long time = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        String checkWelcom = username + EaseConstant.currentUser.getFirendsUserInfo().getId();
        if ((EaseConstant.isWelcome.size() == 0 || !EaseConstant.isWelcome.contains(checkWelcom)) && time >
                5 && EaseConstant.shopID > 0 &&
                HXHelper.getInstance().getYAMContactList()
                        .containsKey
                                (username)) {
            EaseConstant.isWelcome.add(checkWelcom);
            EaseYAMUser user = HXHelper.getInstance().getYAMContactList().get(username);
            if (!TextUtils.isEmpty(user.getFirendsUserInfo().getGroupName())) {
                EMMessage emMessage = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
                TextMessageBody var0 = new TextMessageBody(
                        String.format(context.getResources().getString
                                (R.string.shop_welcome_text), user.getFirendsUserInfo().getGroupName(), user
                                .getFirendsUserInfo().getNickname()));
                emMessage.addBody(var0);
                emMessage.setFrom(username);
                emMessage.setTo(HXConstant.WELCOME);
                emMessage.setMsgId(HXConstant.WELCOME);
                emMessage.direct = EMMessage.Direct.RECEIVE;
                add(emMessage);
            }
        }
    }

    public void sortList() {
        EaseMessageAdapter.this.sort(new Comparator<EMMessage>() {

            @Override
            public int compare(EMMessage lhs, EMMessage rhs) {
                if (lhs.getMsgTime() > rhs.getMsgTime()) {
                    return -1;
                } else {
                    return 1;
                }

            }
        });
    }

    Handler handler = new Handler() {
        private void refreshList() {
            if (conversation != null) {
                // UI线程不能直接使用conversation.getAllMessages()
                // 否则在UI刷新过程中，如果收到新的消息，会导致并发问题
                EMMessage[] messages = conversation.getAllMessages()
                        .toArray(new EMMessage[conversation.getAllMessages().size()]);
                for (int i = 0; i < messages.length; i++) {
                    // getMessage will set message as read status
                    conversation.getMessage(i);
                }
            }

            // UI线程不能直接使用conversation.getAllMessages()
            // 否则在UI刷新过程中，如果收到新的消息，会导致并发问题
            if (getCount() > 1 && getItem(0).getFrom().contains(HXConstant.SYS_MSG)) {
                sortList();
            }
            //            messages = this.toArray(new EMMessage[getCount()]);
            //            for (int i = 0; i < messages.size(); i++) {
            // getMessage will set message as read status
            //                conversation.getMessage(i);
            //            }
            notifyDataSetChanged();
        }

        @Override
        public void handleMessage(android.os.Message message) {
            switch (message.what) {
                case HANDLER_MESSAGE_REFRESH_LIST:
                    refreshList();
                    break;
                case HANDLER_MESSAGE_SELECT_LAST:
                    if (getCount() > 0) {
                        listView.setSelection(getCount() - 1);
                    }
                    break;
                case HANDLER_MESSAGE_SELECT_FIRST:
                    if (getCount() > 0) {
                        listView.setSelection(0);
                    }
                    break;
                case HANDLER_MESSAGE_SEEK_TO:
                    int position = message.arg1;
                    listView.setSelection(position);
                    break;
                default:
                    break;
            }
        }
    };


    /**
     * 刷新页面
     */
    public void refresh() {
        if (handler.hasMessages(HANDLER_MESSAGE_REFRESH_LIST)) {
            return;
        }
        android.os.Message msg = handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST);
        handler.sendMessage(msg);
    }

    /**
     * 刷新页面, 选择最后一个
     */
    public void refreshSelectLast() {
        handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST));
        if (getCount() > 0 && !getItem(0).getFrom().contains(HXConstant.SYS_MSG)) {
            //其它消息定位到第最后
            handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_SELECT_LAST));
        } else {
            handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_SELECT_FIRST));
        }
    }

    /**
     * 刷新页面, 选择Position
     */
    public void refreshSeekTo(int position) {
        handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST));
        android.os.Message msg = handler.obtainMessage(HANDLER_MESSAGE_SEEK_TO);
        msg.arg1 = position;
        handler.sendMessage(msg);
    }


    public long getItemId(int position) {
        return position;
    }


    /**
     * 获取item类型数
     */
    public int getViewTypeCount() {
        if (customRowProvider != null && customRowProvider.getCustomChatRowTypeCount() > 0) {
            return customRowProvider.getCustomChatRowTypeCount() + 14;
        }
        return 14;
    }


    /**
     * 获取item类型
     */
    public int getItemViewType(int position) {
        //		EMMessage message = getItem(position);
        //		if (message == null) {
        //			return -1;
        //		}
        //
        //		if(customRowProvider != null && customRowProvider.getCustomChatRowType(message) > 0){
        //		    return customRowProvider.getCustomChatRowType(message) + 13;
        //		}
        //
        //		if (message.getType() == EMMessage.Type.TXT) {
        //			return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_TXT : MESSAGE_TYPE_SENT_TXT;
        //		}
        //		if (message.getType() == EMMessage.Type.IMAGE) {
        //			return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_IMAGE : MESSAGE_TYPE_SENT_IMAGE;
        //
        //		}
        //		if (message.getType() == EMMessage.Type.LOCATION) {
        //			return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_LOCATION : MESSAGE_TYPE_SENT_LOCATION;
        //		}
        //		if (message.getType() == EMMessage.Type.VOICE) {
        //			return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE : MESSAGE_TYPE_SENT_VOICE;
        //		}
        //		if (message.getType() == EMMessage.Type.VIDEO) {
        //			return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO : MESSAGE_TYPE_SENT_VIDEO;
        //		}
        //		if (message.getType() == EMMessage.Type.FILE) {
        //			return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_FILE : MESSAGE_TYPE_SENT_FILE;
        //		}

        return -1;// invalid
    }

    private EaseChatRow mChatRow;

    public EaseChatRow getFirstChatRow() {
        return mChatRow;
    }

    protected EaseChatRow createChatRow(Context context, EMMessage message, int position) {
        EaseChatRow chatRow = null;
        if (customRowProvider != null &&
                customRowProvider.getCustomChatRow(message, position, this) != null) {
            return customRowProvider.getCustomChatRow(message, position, this);
        }


        if (message.getFrom().contains(HXConstant.SYS_MSG)) {
            chatRow = new EaseChatRowSys(context, message, position, this);
            if (position == 0) {
                this.mChatRow = chatRow;
            }
        } else {
            switch (message.getType()) {
                case TXT:  //判断图文消息和文本消息
                    try {

                        String jsonObject = message.getStringAttribute(HXConstant.SYS_TUWEN_PUSH);
                        int pushType = message.getIntAttribute(HXConstant.SYS_TUWEN_PUSH_TYPE);
                        chatRow =
                                new EaseChatRowTuWen(context, message, position, this, jsonObject, pushType);
                    } catch (EaseMobException e) {
                        e.printStackTrace();
                        try {
                            String ext = message.getStringAttribute("ext");
                            if (ext != null && ext.contains("welcome")) {  //欢迎词
                                chatRow = new EaseWelcome(context, message, position, this);
                            }
                        } catch (EaseMobException erro) {
                            erro.printStackTrace();
                            chatRow = new EaseChatRowText(context, message, position, this);
                        }
                    }

                    break;
                case LOCATION:
                    chatRow = new EaseChatRowLocation(context, message, position, this);
                    break;
                case FILE:
                    chatRow = new EaseChatRowFile(context, message, position, this);
                    break;
                case IMAGE:
                    chatRow = new EaseChatRowImage(context, message, position, this);
                    break;
                case VOICE:
                    chatRow = new EaseChatRowVoice(context, message, position, this);
                    break;
                case VIDEO:
                    chatRow = new EaseChatRowVideo(context, message, position, this);
                    break;
                default:
                    break;
            }
        }

        return chatRow;
    }


    @SuppressLint("NewApi")
    public View getView(final int position, View convertView, ViewGroup parent) {
        EMMessage message = getItem(position);
        if (convertView == null) {
            convertView = createChatRow(context, message, position);
        }
        //缓存的view的message很可能不是当前item的，传入当前message和position更新ui
        ((EaseChatRow) convertView).setUpView(message, position, itemClickListener);

        return convertView;
    }


    public String getToChatUsername() {
        return toChatUsername;
    }


    public void setShowUserNick(boolean showUserNick) {
        this.showUserNick = showUserNick;
    }


    public void setShowAvatar(boolean showAvatar) {
        this.showAvatar = showAvatar;
    }


    public void setMyBubbleBg(Drawable myBubbleBg) {
        this.myBubbleBg = myBubbleBg;
    }


    public void setOtherBuddleBg(Drawable otherBuddleBg) {
        this.otherBuddleBg = otherBuddleBg;
    }


    public void setItemClickListener(MessageListItemClickListener listener) {
        itemClickListener = listener;
    }

    public void setCustomChatRowProvider(EaseCustomChatRowProvider rowProvider) {
        customRowProvider = rowProvider;
    }


    public boolean isShowUserNick() {
        return showUserNick;
    }


    public boolean isShowAvatar() {
        return showAvatar;
    }


    public Drawable getMyBubbleBg() {
        return myBubbleBg;
    }


    public Drawable getOtherBuddleBg() {
        return otherBuddleBg;
    }


}
