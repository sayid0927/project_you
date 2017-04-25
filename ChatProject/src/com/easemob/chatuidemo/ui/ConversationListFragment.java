package com.easemob.chatuidemo.ui;

import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMConversation.EMConversationType;
import com.easemob.chatuidemo.HXApplication;
import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.R;
import com.easemob.chatuidemo.db.InviteMessgeDao;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.model.GeTuiConversation;
import com.easemob.easeui.request.DeleteNetMsgRequest;
import com.easemob.easeui.request.GetuiTypeDataRequest;
import com.easemob.easeui.request.HXNormalRequest;
import com.easemob.easeui.ui.EaseConversationListFragment;
import com.easemob.easeui.ui.GetuiTypeMessageActivity;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.NetUtils;

import java.util.List;

public class ConversationListFragment extends EaseConversationListFragment{

    private TextView errorText;

    @Override
    protected void initView() {
        super.initView();
        hideTitleBar();
        View errorView = View.inflate(getActivity(),R.layout.em_chat_neterror_item, null);
        errorItemContainer.addView(errorView);
        errorText = (TextView) errorView.findViewById(R.id.tv_connect_errormsg);
    }
    
    @Override
    protected void setUpView() {
        super.setUpView();
        // 注册上下文菜单
        registerForContextMenu(conversationListView);
        conversationListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation conversation = conversationListView.getItem(position);
                String username = conversation.getUserName();
                if (username.equals(EMChatManager.getInstance().getCurrentUser()))
                    Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
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
                        EaseConstant.startBenefitsActivity(getActivity());
                    }else{
                    /* 进入聊天页面*/
                        Intent intent;
                        if(conversation instanceof GeTuiConversation){
                            intent = new Intent(getActivity(), GetuiTypeMessageActivity.class);
                            intent.putExtra("title",((GeTuiConversation) conversation).getNickName());
                            intent.putExtra("typeMsg",((GeTuiConversation) conversation).getConversationType());
                        }else {
                            intent = new Intent(getActivity(), ChatActivity.class);
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
                    startActivity(intent);
                }}
            }
        });
    }

    @Override
    protected void onConnectionDisconnected() {
        super.onConnectionDisconnected();
        if (NetUtils.hasNetwork(getActivity())){
         errorText.setText(R.string.can_not_connect_chat_server_connection);
        } else {
          errorText.setText(R.string.the_current_network);
        }
    }
    
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.em_delete_message, menu); 
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean handled = false;
        boolean deleteMessage = false;
        /*if (item.getItemId() == R.id.delete_message) {
            deleteMessage = true;
            handled = true;
        } else*/ if (item.getItemId() == R.id.delete_conversation) {
            deleteMessage = true;
            handled = true;
        }

        EMConversation tobeDeleteCons = conversationListView.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
        if(tobeDeleteCons instanceof GeTuiConversation){
            deleteNetData(((GeTuiConversation) tobeDeleteCons).getConversationType());
        }else{

            // 删除此会话
            EMChatManager.getInstance().deleteConversation(tobeDeleteCons.getUserName(), tobeDeleteCons.isGroup(), deleteMessage);
            InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
            inviteMessgeDao.deleteMessage(tobeDeleteCons.getUserName());
        }
        refresh();

        // 更新消息未读数
//        ((MainActivity) getActivity()).updateUnreadLabel();
        
        return handled ? true : super.onContextItemSelected(item);
    }

    /**个推会话删除请求（仅个推消息）*/
    private void deleteNetData(int type) {
        DeleteNetMsgRequest deleteNetMsgRequest = new DeleteNetMsgRequest(type);
        deleteNetMsgRequest.start();
        deleteNetMsgRequest.setOnResponseStateListener(new HXNormalRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                updateGetuiMsgs();
            }

            @Override
            public void onFail(int code) {

            }
        });

    }

    /**
     * 删除某消息类型后 更新个推的消息数据
     */
    public void updateGetuiMsgs() {
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

    @Override
    public void onResume() {
        super.onResume();
    }
}
