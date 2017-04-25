package com.zxly.o2o.service;

import android.os.AsyncTask;
import android.util.Log;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.chatuidemo.HXConstant;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.model.EaseTuWenVO;
import com.google.gson.Gson;
import com.zxly.o2o.util.ViewUtils;

/**
 * Created by Administrator on 2016/5/9.
 */
public class IMMultiSendTask extends AsyncTask<EaseTuWenVO, Integer, Integer> {
    private int PUSH_TYPE;  //推送的消息类型  小于0表示singlePush 大于0表示multiPush
    private int multiPushOrSend;

    public IMMultiSendTask(int pushType,int multiPushOrSend){
        this.PUSH_TYPE = pushType;
        this.multiPushOrSend=multiPushOrSend;
        EaseConstant.multiMsgProgress=100;
    }

    @Override
    protected Integer doInBackground(EaseTuWenVO... params) {
        Gson gson = new Gson();
        Log.e("TAG",gson+"=============gson====");
        String jsonObj = gson.toJson(params[0]);
        TextMessageBody body = new TextMessageBody(params[0].getTitle());
        EMMessage message;
        //获取该id用户的toChatUserName
        for (String toChatUserName : EaseConstant.users) {
            message = EMMessage.createSendMessage(EMMessage.Type.TXT);
            message.addBody(body);
            message.setReceipt(toChatUserName);
            message.setAttribute(HXConstant.SYS_TUWEN_PUSH, jsonObj);
            message.setAttribute(HXConstant.SYS_TUWEN_PUSH_TYPE, Math.abs(PUSH_TYPE));
            if (EaseConstant.currentUser != null) {
                message.setAttribute("nickname", EaseConstant.currentUser.getFirendsUserInfo().getNickname());
                message.setAttribute("headImageUrl",
                        EaseConstant.currentUser.getFirendsUserInfo().getThumHeadUrl());
            }
            //发送消息
            EMChatManager.getInstance().sendMessage(message, null);
            if (multiPushOrSend == HXConstant.MUL_PUSH) {
                EMChatManager.getInstance().deleteConversation(toChatUserName, false, true);
            }

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        ViewUtils.showToast("后台消息推送完成");
        EaseConstant.multiMsgProgress=0;
        if (EaseConstant.users != null) {
            EaseConstant.users.clear();
        }
    }

}
