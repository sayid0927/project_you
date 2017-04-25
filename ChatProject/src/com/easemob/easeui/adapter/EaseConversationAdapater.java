package com.easemob.easeui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatRoom;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMConversation.EMConversationType;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chatuidemo.HXApplication;
import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.HXHelper;
import com.easemob.chatuidemo.R;
import com.easemob.chatuidemo.ui.ChatActivity;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.domain.EaseYAMUser;
import com.easemob.easeui.model.GeTuiConversation;
import com.easemob.easeui.request.DeleteNetMsgRequest;
import com.easemob.easeui.request.EaseAddFriendRequest;
import com.easemob.easeui.request.GetuiTypeDataRequest;
import com.easemob.easeui.request.HXNormalRequest;
import com.easemob.easeui.ui.EaseConversationListFragment;
import com.easemob.easeui.ui.GetuiTypeMessageActivity;
import com.easemob.easeui.utils.EaseCommonUtils;
import com.easemob.easeui.utils.EaseSmileUtils;
import com.easemob.easeui.utils.EaseUserUtils;
import com.easemob.easeui.widget.SlideDelete;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.NetUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.ListIterator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * 会话列表adapter
 */
public class EaseConversationAdapater extends ArrayAdapter<EMConversation> {
    private static final String TAG = "ChatAllHistoryAdapter";
    private final Context context;
    private List<EMConversation> conversationList;
    private List<EMConversation> copyConversationList;
    private ConversationFilter conversationFilter;
    private boolean notiyfyByFilter;

    protected int primaryColor;
    protected int secondaryColor;
    protected int timeColor;
    protected int primarySize;
    protected int secondarySize;
    protected float timeSize;

    private List<SlideDelete> slideDeleteArrayList = new ArrayList<SlideDelete>();
    private int filterCount;

    public EaseConversationAdapater(Context context, int resource,
                                    List<EMConversation> objects) {
        super(context, resource, objects);
        this.context =context;
        conversationList = objects;
        copyConversationList = new ArrayList<EMConversation>();
        copyConversationList.clear();
        copyConversationList.addAll(objects);
    }

    @Override
    public int getCount() {
        return conversationList.size();
    }

    @Override
    public EMConversation getItem(int arg0) {
        if (arg0 < conversationList.size()) {
            return conversationList.get(arg0);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView =
                    LayoutInflater.from(getContext()).inflate(R.layout.ease_row_chat_history, parent, false);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.slideDelete = (SlideDelete) convertView.findViewById(R.id.slidedelete);
            holder.slideDelete.setDragEnable(true);

            holder.deleteMsg = (ImageView) convertView.findViewById(R.id.sw_btn);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.unreadLabel = (TextView) convertView.findViewById(R.id.unread_msg_number);
            holder.message = (TextView) convertView.findViewById(R.id.message);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.nameLeft = (TextView) convertView.findViewById(R.id.name_left);
            holder.avatar = (NetworkImageView) convertView.findViewById(R.id.avatar);
            holder.msgState = convertView.findViewById(R.id.msg_state);
            holder.list_itease_layout = (RelativeLayout) convertView.findViewById(R.id.list_itease_layout);
            holder.content= (LinearLayout) convertView.findViewById(R.id.content);
            convertView.setTag(holder);
        }
        holder.avatar.setImageResource(0);
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(slideDeleteArrayList.size()==1){
                    closeOtherItem();
                }else{
                    EMConversation conversation = conversationList.get(position);
                    String username = conversation.getUserName();
                    if (username.equals(EMChatManager.getInstance().getCurrentUser()))
                        Toast.makeText(context, R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
                    else {

                        int what = 0;
                        try {
                            if(conversation instanceof GeTuiConversation){
                                what=((GeTuiConversation) conversation).getWhat();
                            }else{
                                what = conversation.getMessage(0).getIntAttribute("what");
                            }
                        } catch (EaseMobException e) {
                            e.printStackTrace();
                        }
                    /*判断是不是优惠消息*/
                        if(EaseConstant.shopID<0&&what==HXConstant.SYS_BENEFIT_KEY_SHOP){
                        /*进入商户端优惠列表界面*/
                            conversation.markAllMessagesAsRead();
                            EaseConstant.startBenefitsActivity((Activity) context);
                        }else{
                    /* 进入聊天页面*/
                            Intent intent;
                            if(conversation instanceof GeTuiConversation){
                                intent = new Intent(context, GetuiTypeMessageActivity.class);
                                intent.putExtra("title",((GeTuiConversation) conversation).getNickName());
                                intent.putExtra("typeMsg",((GeTuiConversation) conversation).getConversationType());
                            }else {
                                intent = new Intent(context, ChatActivity.class);
                            }
                            if(conversation.isGroup()){
                                if(conversation.getType() == EMConversationType.ChatRoom){
                                    // it's group chat
                                    intent.putExtra(HXConstant.EXTRA_CHAT_TYPE, HXConstant.CHATTYPE_CHATROOM);
                                }else{
                                    intent.putExtra(HXConstant.EXTRA_CHAT_TYPE, HXConstant.CHATTYPE_GROUP);
                                }

                            }
                            // it's single chat
                            intent.putExtra(HXConstant.EXTRA_USER_ID, username);
                            context.startActivity(intent);
                        }}
                }
            }
        });

        holder.slideDelete.setOnSlideDeleteListener(new SlideDelete.OnSlideDeleteListener() {
            @Override
            public void onOpen(SlideDelete slideDelete) {
                closeOtherItem();
                slideDeleteArrayList.add(slideDelete);
            }

            @Override
            public void onClose(SlideDelete slideDelete) {
                slideDeleteArrayList.remove(slideDelete);
            }
        });

        // 获取与此用户/群组的会话
        final EMConversation conversation = getItem(position);
        holder.deleteMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(conversation instanceof GeTuiConversation){
                    deleteNetData(((GeTuiConversation) conversation).getConversationType());
                }else{
                    EMChatManager.getInstance().deleteConversation(conversation.getUserName(), false, true);
                }
//                HXApplication.getInstance().unReadMsgCount = HXApplication.getInstance().getUnreadMsgCountTotal();
                conversationList.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.list_itease_layout.setBackgroundResource(R.drawable.ease_mm_listitem);

        // 获取用户username或者群组groupid
        String username =null;
        if(conversation instanceof GeTuiConversation){
            int type=((GeTuiConversation) conversation).getConversationType();
            switch (type){
                case 0:
                    username="user_shopinfo_push";
                    break;
                case 2:
                    username="user_shopnotice_push";
                    break;
                case 3:
                    username="user_shopactivity_push";
                    break;
                case 4:
                    username="user_yamnotice_push";
                    break;
                default:
                    break;
            }
            ((GeTuiConversation) conversation).setUserName(username);
        }else{
            username = conversation.getUserName();
        }

        if (conversation.getType() == EMConversationType.GroupChat) {
            // 群聊消息，显示群聊头像
            holder.avatar.setImageResource(R.drawable.ease_group_icon);
            EMGroup group = EMGroupManager.getInstance().getGroup(username);
            holder.name.setText(group != null ? group.getGroupName() : username);
        } else if (conversation.getType() == EMConversationType.ChatRoom) {
            holder.avatar.setImageResource(R.drawable.ease_group_icon);
            EMChatRoom room = EMChatManager.getInstance().getChatRoom(username);
            holder.name
                    .setText(room != null && !TextUtils.isEmpty(room.getName()) ? room.getName() : username);
        } else {

            try {
                String nickName = null;
                int what=0;
                int typeMsg=-1;
                if (username.contains(HXConstant.SYS_MSG)) {  //系统消息
                    if(conversation instanceof GeTuiConversation){
                        //当该会话为个推消息会话时，将字段赋值（个推数据原本不具有）
                        int type=((GeTuiConversation) conversation).getConversationType();
                        switch (type){
                            case HXConstant.GETUI_SHOP_INFO:
                                nickName="门店快讯";
                                what=HXConstant.SYS_SHOPINFO_KEY_USER;
                                typeMsg=0;
                                break;
                            case HXConstant.GETUI_SHOP_NOTICE:
                                nickName="门店公告";
                                what=HXConstant.SYS_SHOPNOTICE_KEY_USER;
                                typeMsg=2;
                                break;
                            case HXConstant.GETUI_SHOP_ACTIVITY:
                                nickName="门店活动";
                                what=HXConstant.SYS_ACTIVITY_KEY_USER;
                                typeMsg=3;
                                break;
                            case HXConstant.GETUI_YAM_NOTICE:
                                nickName="柚安米公告";
                                if(EaseConstant.shopID > 0){
                                    what=HXConstant.SYS_NOTIC_KEY_USER;
                                }else {
                                    what=HXConstant.SYS_NOTIC_KEY_SHOP;
                                }
                                typeMsg=4;
                                break;
                        }
                        ((GeTuiConversation) conversation).setWhat(what);
                        ((GeTuiConversation) conversation).setNickName(nickName);
                        ((GeTuiConversation) conversation).setTypeMsg(typeMsg);
                    }else{
                        nickName = conversation.getMessage(0)
                                .getStringAttribute("nickname");
                        what = conversation.getMessage(0).getIntAttribute("what");
                    }
                    holder.name.setText(nickName);
                    if (EaseConstant.shopID > 0) {
                        if (what >= HXConstant.SYS_SHOPINFO_KEY_USER &&
                                what < HXConstant.SYS_SHOPINFO_KEY_USER + 50) {   //门店快讯
                            EaseConstant.setImage(holder.avatar, "", R.drawable.kuaixun, null);
                        } else if (what >= HXConstant.SYS_ORDER_KEY_USER &&
                                what < HXConstant.SYS_ORDER_KEY_USER + 50) {  //订单消息
                            EaseConstant.setImage(holder.avatar, "", R.drawable.dingdan, null);
                        } else if (what >= HXConstant.SYS_MAINTAIN_KEY_USER &&
                                what < HXConstant.SYS_MAINTAIN_KEY_USER + 50) {  //续保
                            EaseConstant.setImage(holder.avatar, "", R.drawable.xubao,
                                    null);
                        } else if (what == HXConstant.SYS_NOTIC_KEY_USER) {  //柚安米公告
                            EaseConstant.setImage(holder.avatar, "", R.drawable.gonggao,
                                    null);
                        } else if (what >= HXConstant.SYS_ACTIVITY_KEY_USER &&
                                what < HXConstant.SYS_ACTIVITY_KEY_USER + 50) {   //门店活动
                            EaseConstant.setImage(holder.avatar, "", R.drawable.ease_msg_activity_icon,
                                    null);
                        } else if (what >= HXConstant.SYS_INSURE_KEY_USER &&
                                what < HXConstant.SYS_INSURE_KEY_USER + 50) {   //延保订单
                            EaseConstant.setImage(holder.avatar, "", R.drawable.ease_msg_insure_icon,
                                    null);
                        }else if(what >= HXConstant.SYS_BENEFIT_KEY_USER &&
                                what < HXConstant.SYS_BENEFIT_KEY_USER + 50){  //优惠消息
                            EaseConstant.setImage(holder.avatar, "", R.drawable.xx_gonggao_icon,
                                    null);
                        }else if(what==HXConstant.SYS_SHOPNOTICE_KEY_USER){//门店公告
                            EaseConstant.setImage(holder.avatar, "", R.drawable.icon_shop_nitice,
                                    null);
                        }

                    } else if (EaseConstant.shopID < 0) {
                        if (what == HXConstant.SYS_REGIST_KEY_SHOP) {   //会员消息
                            EaseConstant.setImage(holder.avatar, "", R.drawable.huiyuan,
                                    null);
                        } else if (what >= HXConstant.SYS_NOTIC_KEY_SHOP&&
                                what<HXConstant.SYS_NOTIC_KEY_SHOP+50) {  //柚安米公告
                            EaseConstant.setImage(holder.avatar, "", R.drawable.gonggao,
                                    null);
                        } else if (what == HXConstant.SYS_FEEDBACK_KEY_SHOP) {  //会员反馈消息
                            EaseConstant.setImage(holder.avatar, "", R.drawable.fankui,
                                    null);
                        } else if (what >= HXConstant.SYS_ORDER_KEY_SHOP &&
                                what < HXConstant.SYS_ORDER_KEY_SHOP + 50) {   //订单消息
                            EaseConstant.setImage(holder.avatar, "", R.drawable.dingdan,
                                    null);
                        } else if (what >= HXConstant.SYS_MAINTAIN_KEY_SHOP &&
                                what < HXConstant.SYS_MAINTAIN_KEY_SHOP + 50) {   //续保单

                            EaseConstant.setImage(holder.avatar, "", R.drawable.img_msg_renewal,
                                    null);
                        } else if (what >= HXConstant.SYS_INSURE_KEY_SHOP &&
                                what < HXConstant.SYS_INSURE_KEY_SHOP + 50) {   //资金记录
                            EaseConstant.setImage(holder.avatar, "", R.drawable.ease_msg_insure_icon,
                                    null);
                        }else if(what >= HXConstant.SYS_BENEFIT_KEY_SHOP &&
                                what < HXConstant.SYS_BENEFIT_KEY_SHOP + 50){  //优惠消息
                            EaseConstant.setImage(holder.avatar, "", R.drawable.xx_gonggao_icon,
                                    null);
                        } else if (what >= HXConstant.SYS_FUND_RECORD && what < HXConstant.SYS_FUND_RECORD + 50) {
                            EaseConstant.setImage(holder.avatar, "", R.drawable.zijintixing_icon,
                                    null);
                        }
                    }
                    holder.nameLeft.setVisibility(View.GONE);
                } else {
                    holder.nameLeft.setVisibility(View.VISIBLE);
                    if(conversation instanceof GeTuiConversation){

                    }else{
                        if (EaseConstant.shopID > 0 && conversation.getMessage(0).direct == EMMessage.Direct
                                .RECEIVE) {
                            EaseYAMUser user = HXHelper.getInstance().getYAMContactList()
                                    .get(conversation.getMessage(0).getUserName());
                            long userId=HXApplication.getInstance()
                                    .parseIdFromName(conversation.getMessage(0).getFrom());
                            if (user == null&&userId!=0) {
                                //add friend
                                new EaseAddFriendRequest(userId).start();
                            }
                        }

                        EaseUserUtils.setUserNickAndAvatar3(conversation.getMessage(0), holder.name, holder
                                .avatar, holder.nameLeft);

                        if(holder.name.getText().toString().contains("游客")){
                            holder.nameLeft.setVisibility(View.GONE);
                        }else{
                            holder.nameLeft.setVisibility(View.VISIBLE);
                        }
                        if (TextUtils.isEmpty(holder.nameLeft.getText().toString().trim())) {
                            holder.nameLeft.setText("好友");
                            holder.nameLeft.setTextColor(Color.parseColor("#ff78b3"));
                            holder.nameLeft.setBackgroundResource(R.drawable.im_friend_tab);
                        }else{
                            holder.nameLeft.setTextColor(Color.parseColor("#ffffff"));
                            holder.nameLeft.setBackgroundColor(Color.parseColor("#349fe2"));

                        }
                    }


                }

            } catch (EaseMobException e) {
                e.printStackTrace();
            }
        }
        if(conversation instanceof GeTuiConversation){
            if (((GeTuiConversation) conversation).getNumber() > 0) {
                // 显示与此用户的消息未读数
                holder.unreadLabel.setText(String.valueOf(((GeTuiConversation) conversation).getNumber()
                        >99?99+"+":((GeTuiConversation) conversation).getNumber()));
                holder.unreadLabel.setVisibility(View.VISIBLE);
            } else {
                holder.unreadLabel.setVisibility(View.INVISIBLE);
            }
            holder.message.setText(((GeTuiConversation) conversation).getTitle());
            holder.time.setText(EaseConstant.getTimestampString((new Date(((GeTuiConversation) conversation).getUpdateTime()))));

        }else{
            if (conversation.getUnreadMsgCount() > 0) {
                // 显示与此用户的消息未读数
                holder.unreadLabel.setText(String.valueOf(conversation.getUnreadMsgCount()
                        >99?99+"+":conversation.getUnreadMsgCount()));
                holder.unreadLabel.setVisibility(View.VISIBLE);
            } else {
                holder.unreadLabel.setVisibility(View.INVISIBLE);
            }
            if (conversation.getMsgCount() != 0) {
                // 把最后一条消息的内容作为item的message内容
                EMMessage lastMessage = conversation.getLastMessage();
                if (username.contains(HXConstant.SYS_MSG)) {
                    if(conversation.getMessage(0).getMsgTime()>conversation.getLastMessage().getMsgTime())
                        lastMessage= conversation.getMessage(0);
                }
                holder.message.setText(EaseSmileUtils.getSmiledText(getContext(),
                        EaseCommonUtils.getMessageDigest(lastMessage, (this.getContext()))),
                        BufferType.SPANNABLE);

                holder.time.setText(EaseConstant.getTimestampString((new Date(lastMessage.getMsgTime()))));
                if (lastMessage.direct == EMMessage.Direct.SEND && lastMessage.status == EMMessage.Status.FAIL) {
                    holder.msgState.setVisibility(View.VISIBLE);
                    //有网络的情况下重新发送
                    if(NetUtils.hasNetwork(getContext())) {
                        EMChatManager.getInstance().sendMessage(lastMessage, null);
                    }
                } else {
                    holder.msgState.setVisibility(View.GONE);
                }
            }


        }

        //设置自定义属性
//        holder.name.setTextColor(primaryColor);
//        holder.message.setTextColor(secondaryColor);
//        holder.time.setTextColor(timeColor);
        if (primarySize != 0) {
            holder.name.setTextSize(TypedValue.COMPLEX_UNIT_PX, primarySize);
        }
        if (secondarySize != 0) {
            holder.message.setTextSize(TypedValue.COMPLEX_UNIT_PX, secondarySize);
        }
        if (timeSize != 0) {
            holder.time.setTextSize(TypedValue.COMPLEX_UNIT_PX, timeSize);
        }

        return convertView;
    }
    /**个推会话删除请求（仅个推消息）*/
    private void deleteNetData(int type) {
        DeleteNetMsgRequest deleteNetMsgRequest = new DeleteNetMsgRequest(type);
        deleteNetMsgRequest.start();
        deleteNetMsgRequest.setOnResponseStateListener(new HXNormalRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                updateGetuiMsgs();
                EaseConversationListFragment.getInstance().refresh();
            }

            @Override
            public void onFail(int code) {
                Toast.makeText(context,"删除失败,请重试",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void notifyDataSetChanged() {
        if (!notiyfyByFilter) {
            copyConversationList.clear();
            copyConversationList.addAll(conversationList);
            notiyfyByFilter = false;
        }
        super.notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        if (conversationFilter == null) {
            conversationFilter = new ConversationFilter(conversationList);
        }
        return conversationFilter;
    }


    public void setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
    }

    public void setSecondaryColor(int secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public void setTimeColor(int timeColor) {
        this.timeColor = timeColor;
    }

    public void setPrimarySize(int primarySize) {
        this.primarySize = primarySize;
    }

    public void setSecondarySize(int secondarySize) {
        this.secondarySize = secondarySize;
    }

    public void setTimeSize(float timeSize) {
        this.timeSize = timeSize;
    }


    private class ConversationFilter extends Filter {
        List<EMConversation> mOriginalValues = null;

        public ConversationFilter(List<EMConversation> mList) {
            mOriginalValues = mList;
        }

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                mOriginalValues = new ArrayList<EMConversation>();
            }
            if (prefix == null || prefix.length() == 0) {
                results.values = copyConversationList;
                results.count = copyConversationList.size();
            } else {
                String prefixString = prefix.toString();
                final int count = copyConversationList.size();
                final ArrayList<EMConversation> newValues = new ArrayList<EMConversation>();

                for (int i = 0; i < count; i++) {
                    final EMConversation value = copyConversationList.get(i);
                    String username = value.getUserName();

                    //                    EMGroup group = EMGroupManager.getInstance().getGroup(username);
                    //                    if (group != null) {
                    //                        username = group.getGroupName();
                    //                    } else {

                    if (username.contains(HXConstant.SYS_MSG)) {
                        try {
                            if(value instanceof GeTuiConversation){
                                username=((GeTuiConversation) value).getNickName();
                            }else{
                                username = value.getMessage(0)
                                        .getStringAttribute("nickname");
                            }
                        } catch (EaseMobException e) {
                            e.printStackTrace();
                        }
                    } else {
                        EaseYAMUser user = HXHelper.getInstance().getUserInfo(username);
                        if (user != null && !TextUtils.isEmpty(user.getFirendsUserInfo().getNickname())) {
                            username = user.getFirendsUserInfo().getNickname();
                        } else if (user != null &&
                                !TextUtils.isEmpty(user.getFirendsUserInfo().getUserName())) {
                            username = user.getFirendsUserInfo().getUserName();
                        }
                    }
                    //                    }
                    // First match against the whole ,non-splitted value
                    if (username.contains(prefixString)) {
                        newValues.add(value);
                    }
                    //                    else {
                    //                        final String[] words = username.split(" ");
                    //
                    //                        // Start at index 0, in case valueText starts with space(s)
                    //                        for (String word : words) {
                    //                            if (word.startsWith(prefixString)) {
                    //                                newValues.add(value);
                    //                                break;
                    //                            }
                    //                        }
                    //                    }
                }

                results.values = newValues;
                results.count = newValues.size();
                filterCount =results.count;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            conversationList.clear();
            conversationList.addAll((List<EMConversation>) results.values);
            if (results.count > 0) {
                notiyfyByFilter = true;
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }

        }

    }

    public int getFilterCount() {
        return filterCount;
    }

    public void setFilterCount(int filterCount) {
        this.filterCount = filterCount;
    }

    private static class ViewHolder {
        /**
         * 和谁的聊天记录
         */
        TextView name;
        TextView nameLeft;
        /**
         * 消息未读数
         */
        TextView unreadLabel;
        /**
         * 最后一条消息的内容
         */
        TextView message;
        /**
         * 最后一条消息的时间
         */
        TextView time;
        /**
         * 用户头像
         */
        NetworkImageView avatar;
        /**
         * 最后一条消息的发送状态
         */
        View msgState;
        /**
         * 整个list中每一行总布局
         */
        RelativeLayout list_itease_layout;

        ImageView deleteMsg;

        SlideDelete slideDelete;

        LinearLayout content;

    }

    /**
     * 删除某消息类型后 更新个推的消息数据
     */
    private void updateGetuiMsgs() {
        final GetuiTypeDataRequest getuiTypeDataRequest=new GetuiTypeDataRequest(EaseConstant.shopID);
        getuiTypeDataRequest.start();
        getuiTypeDataRequest.setOnResponseStateListener(new HXNormalRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                HXApplication.getInstance().setGetuiMsgList(getuiTypeDataRequest.emConversationList);
                setNetDataUnread(getuiTypeDataRequest.emConversationList);
            }

            @Override
            public void onFail(int code) {

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


    private void closeOtherItem(){
//        System.out.println("closeOtherItem");
        // 采用Iterator的原因是for是线程不安全的，迭代器是线程安全的
        ListIterator<SlideDelete> slideDeleteListIterator = slideDeleteArrayList.listIterator();
        while(slideDeleteListIterator.hasNext()){
            SlideDelete slideDelete = slideDeleteListIterator.next();
            slideDelete.isShowDelete(false);
        }
        slideDeleteArrayList.clear();
    }
}

