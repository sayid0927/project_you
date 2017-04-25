package com.zxly.o2o.service;

import android.os.AsyncTask;

import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chatuidemo.HXConstant;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.model.EaseTuWenVO;
import com.google.gson.Gson;
import com.zxly.o2o.util.ViewUtils;

/**
 * Created by Administrator on 2016/5/9.
 */
public class IMMultiPushTask extends AsyncTask<EaseTuWenVO, Integer, Integer> {
    private int PUSH_TYPE;  //推送的消息类型  小于0表示singlePush 大于0表示multiPush

    public IMMultiPushTask(int pushType) {
        this.PUSH_TYPE = pushType;
        EaseConstant.multiMsgProgress=100;
    }

    @Override
    protected Integer doInBackground(EaseTuWenVO... params) {
        Gson gson = new Gson();
        String jsonObj = gson.toJson(params[0]);
        String action = HXConstant.CMD_ACTION_TUWEN;//action可以自定义，在广播接收时可以收到
        CmdMessageBody cmdBody = new CmdMessageBody(action);
        EMMessage cmdMsg;
        for (String toChatUserName : EaseConstant.users) {
            //发送透传消息
            cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);

            //支持单聊和群聊，默认单聊，如果是群聊添加下面这行
            cmdMsg.setChatType(EMMessage.ChatType.Chat);
            //                    String toUsername="test1";//发送给某个人
            cmdMsg.setReceipt(toChatUserName);
            cmdMsg.setAttribute(HXConstant.SYS_TUWEN_PUSH,
                    jsonObj);
            cmdMsg.setAttribute(HXConstant.SYS_TUWEN_PUSH_TYPE, Math.abs(PUSH_TYPE));
            cmdMsg.setAttribute("what", Math.abs(PUSH_TYPE));
            cmdMsg.addBody(cmdBody);
            EMChatManager.getInstance().sendMessage(cmdMsg, null);
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
