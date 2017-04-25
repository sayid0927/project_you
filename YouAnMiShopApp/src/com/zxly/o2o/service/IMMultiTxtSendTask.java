package com.zxly.o2o.service;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.easemob.chat.CmdMessageBody;
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
public class IMMultiTxtSendTask extends AsyncTask<String, Integer, Integer> {

    public IMMultiTxtSendTask(){
        EaseConstant.multiMsgProgress=100;
    }


    @Override
    protected Integer doInBackground(String... params) {
        TextMessageBody body = new TextMessageBody(params[0]);
        EMMessage message;
        for (String toChatUserName : EaseConstant.users) {
            //获取该id用户的toChatUserName
            message = EMMessage.createSendMessage(EMMessage.Type.TXT);
            message.addBody(body);
            message.setReceipt(toChatUserName);
            if (EaseConstant.currentUser != null) {
                message.setAttribute("nickname", EaseConstant.currentUser.getFirendsUserInfo().getNickname());
                message.setAttribute("headImageUrl",
                        EaseConstant.currentUser.getFirendsUserInfo().getThumHeadUrl());
            }
            //发送消息
            EMChatManager.getInstance().sendMessage(message, null);

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
        EaseConstant.multiMsgProgress=0;
        ViewUtils.showToast("后台消息推送完成");
        if (EaseConstant.users != null) {
            EaseConstant.users.clear();
        }
    }

}
