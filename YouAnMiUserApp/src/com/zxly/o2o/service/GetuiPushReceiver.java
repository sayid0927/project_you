package com.zxly.o2o.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.chatuidemo.HXConstant;
import com.easemob.easeui.AppException;
import com.easemob.easeui.ui.EaseConversationListFragment;
import com.easemob.easeui.ui.EaseH5DetailAct;
import com.easemob.easeui.utils.GsonParser;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.zxly.o2o.activity.ProductInfoAct;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.GetuiMsg;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.GetuiMsgShowRequest;
import com.zxly.o2o.request.PushBindRequest;
import com.zxly.o2o.util.ApkInfoUtil;
import com.zxly.o2o.util.AppLog;
import com.zxly.o2o.util.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class GetuiPushReceiver extends BroadcastReceiver {

    /**
     * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息(此时 GetuiSdkDemoActivity.tLogView == null)
     */
    public static StringBuilder payloadData = new StringBuilder();
    private int busId;
    private int what;
    private String expend;
    private long createTime;
    private String packageName;
    private GetuiMsg getuiMsg;

    private void registerClientId() {
        PushBindRequest pushBindRequest = new PushBindRequest();
        pushBindRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                Log.e("TAG","绑定clientId成功");
            }

            @Override
            public void onFail(int code) {
            }
        });
        pushBindRequest.start(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                // 获取透传数据
                // String appid = bundle.getString("appid");
                byte[] payload = bundle.getByteArray("payload");

                String taskid = bundle.getString("taskid");
                String messageid = bundle.getString("messageid");

                // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行(目前)
                boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
                if (payload != null) {
                    String data = new String(payload);
                    showNotification(context, data);
                    payloadData.append(data);
                    payloadData.append("\n");
                }
                break;

            case PushConsts.GET_CLIENTID:
                // 获取ClientID(CID)
                // 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
                String cid = bundle.getString("clientid");
                Config.getuiClientId = cid;
                registerClientId();
                break;
            case PushConsts.GET_SDKONLINESTATE:
                //状态
                boolean online = bundle.getBoolean("onlineState");
                break;

            case PushConsts.SET_TAG_RESULT:
                String sn = bundle.getString("sn");
                String code = bundle.getString("code");

                String text = "设置标签失败, 未知异常";
                switch (Integer.valueOf(code)) {
                    case PushConsts.SETTAG_SUCCESS:
                        text = "设置标签成功";
                        break;

                    case PushConsts.SETTAG_ERROR_COUNT:
                        text = "设置标签失败, tag数量过大, 最大不能超过200个";
                        break;

                    case PushConsts.SETTAG_ERROR_FREQUENCY:
                        text = "设置标签失败, 频率过快, 两次间隔应大于1s";
                        break;

                    case PushConsts.SETTAG_ERROR_REPEAT:
                        text = "设置标签失败, 标签重复";
                        break;

                    case PushConsts.SETTAG_ERROR_UNBIND:
                        text = "设置标签失败, 服务未初始化成功";
                        break;

                    case PushConsts.SETTAG_ERROR_EXCEPTION:
                        text = "设置标签失败, 未知异常";
                        break;

                    case PushConsts.SETTAG_ERROR_NULL:
                        text = "设置标签失败, tag 为空";
                        break;

                    case PushConsts.SETTAG_NOTONLINE:
                        text = "还未登陆成功";
                        break;

                    case PushConsts.SETTAG_IN_BLACKLIST:
                        text = "该应用已经在黑名单中,请联系售后支持!";
                        break;

                    case PushConsts.SETTAG_NUM_EXCEED:
                        text = "已存 tag 超过限制";
                        break;

                    default:
                        break;
                }
                Log.e("TAG",text);
                break;
            case PushConsts.THIRDPART_FEEDBACK:
                break;

            default:
                break;
        }

    }

    /**
     * 收到透传消息随即发送通知
     *
     * @param context
     * @param data
     */
    private void showNotification(Context context, String data) {
        //收到透传消息立即刷新消息页面数据
        if(EaseConversationListFragment.getInstance()!=null){
            EaseConversationListFragment.getInstance().refresh();
        }
        JSONObject jo = null;
        try {
            jo = new JSONObject(data);
            busId = jo.getInt("busId");
            what = jo.getInt("what");
            createTime = jo.getLong("createTime");
            getuiMsg = GsonParser.getInstance().getBean(jo.getString("expend"), GetuiMsg.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(getuiMsg!=null) {
            Intent broadcastIntent = new Intent(context, NotificationReceiver.class);
            putExtra(broadcastIntent);
            PendingIntent pendingIntent = PendingIntent.
                    getBroadcast(context, (int) getuiMsg.getDataId(), broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContentTitle(ApkInfoUtil.getAppName(context))
                    .setContentText(getuiMsg.getTitle())
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.logo)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL);
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            hasShowRequest(getuiMsg.getDataId());
            manager.notify((int) getuiMsg.getDataId(), builder.build());
        }
    }

    private void hasShowRequest(long dataId) {
        new GetuiMsgShowRequest(dataId).start();
    }

    private void putExtra(Intent broadcastIntent) {
        broadcastIntent.putExtra("notifyId",getuiMsg.getDataId());
        switch (Math.abs(what)) {
            case HXConstant.SYS_ARTICLE_KEY_USER://（门店快讯类）
                broadcastIntent.putExtra("goto_h5", true);
                broadcastIntent.putExtra("sys_msg_url", getuiMsg.getH5Url());
                broadcastIntent.putExtra("what", what);
                //用于标记消息唯一性（点击通知栏消失）
                broadcastIntent.putExtra("id", busId);
                broadcastIntent.putExtra("title", "文章详情");
                break;
            case HXConstant.SYS_YAMARTICLE_KEY_USER://运营文章（门店快讯类）
                broadcastIntent.putExtra("goto_h5", true);
                broadcastIntent.putExtra("sys_msg_url", getuiMsg.getH5Url());
                broadcastIntent.putExtra("what", what);
                //用于标记消息唯一性（点击通知栏消失）
                broadcastIntent.putExtra("id", busId);
                broadcastIntent.putExtra("title", "文章详情");
                break;
            case HXConstant.SYS_SHOPPRODUCT_KEY_USER:
                //个推商品
                broadcastIntent.putExtra("bannerId", -1);
                broadcastIntent.putExtra("produceId", getuiMsg.getDataId());  //商品id
                break;
            case HXConstant.SYS_ACTIVITY_KEY_USER:
                broadcastIntent.putExtra("goto_h5", true);
                broadcastIntent.putExtra("sys_msg_url", getuiMsg.getH5Url());
                broadcastIntent.putExtra("what", what);
                broadcastIntent.putExtra("id", busId);
                broadcastIntent.putExtra("title", "门店活动");
                break;
            case HXConstant.SYS_NOTIC_KEY_USER:
                broadcastIntent.putExtra("goto_h5", true);
                broadcastIntent.putExtra("sys_msg_url", getuiMsg.getH5Url());
                broadcastIntent.putExtra("id", busId);
                broadcastIntent.putExtra("what", what);
                broadcastIntent.putExtra("title", "柚安米公告");
                break;
            case HXConstant.SYS_SHOPNOTICE_KEY_USER://门店公告
                broadcastIntent.putExtra("goto_h5", true);
                broadcastIntent.putExtra("what", what);
                broadcastIntent.putExtra("id", busId);
                broadcastIntent.putExtra("sys_msg_url", getuiMsg.getH5Url());
                broadcastIntent.putExtra("title", "门店公告");
                break;
            case HXConstant.SYS_SHOPDEFINE_KEY_USER://自定义推送
                broadcastIntent.putExtra("goto_h5", true);
                broadcastIntent.putExtra("goto_native", true);
                broadcastIntent.putExtra("what", what);
                broadcastIntent.putExtra("id", busId);
                broadcastIntent.putExtra("title", getuiMsg.getTitle());
            default:
                break;
        }
    }
}
