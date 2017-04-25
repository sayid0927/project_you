package com.easemob.easeui.widget.chatrow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.Html;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.R;
import com.easemob.easeui.AppException;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.model.EaseTuWenVO;
import com.easemob.easeui.model.IMUserInfoVO;
import com.easemob.easeui.ui.EaseH5DetailAct;
import com.easemob.easeui.utils.EaseSmileUtils;
import com.easemob.easeui.utils.GsonParser;
import com.easemob.exceptions.EaseMobException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/12/5.
 */
public class EaseChatRowTuWen extends EaseChatRow {
    ViewHolder holder;
    String jsonObject;
    private int PUSH_TYPE;
    private EaseTuWenVO easeTuWenVO;

    public EaseChatRowTuWen(Context context, EMMessage message, int position, BaseAdapter adapter, String
            jsonObject, int pushType) {
        super(context, message, position, adapter);
        this.jsonObject = jsonObject;
        PUSH_TYPE = pushType;
    }

    public class ViewHolder {
        TextView tv_title;
        TextView tv_content;
        TextView tv_content_below;
        TextView tv_content_left;
        NetworkImageView iv_image;
    }

    @Override
    protected void onInflatView() {
        holder = new ViewHolder();
        inflater.inflate(message.direct == EMMessage.Direct.RECEIVE ?
                R.layout.ease_row_received_tuwen : R.layout.ease_row_sent_tuwen, this);
    }

    @Override
    protected void onFindViewById() {
        holder.tv_title = (TextView) findViewById(R.id.tv_title);
        holder.tv_content = (TextView) findViewById(R.id.tv_content);
        holder.tv_content_below = (TextView) findViewById(R.id.tv_content_below);
        holder.tv_content_left = (TextView) findViewById(R.id.tv_content_left);
        holder.iv_image = (NetworkImageView) findViewById(R.id.iv_image);
    }

    @Override
    protected void onUpdateView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onSetUpView() {
        try {
            easeTuWenVO = GsonParser.getInstance().fromJson(jsonObject, new TypeToken<EaseTuWenVO>() {
            });
        } catch (AppException e) {
            e.printStackTrace();
        }
        TextMessageBody txtBody = (TextMessageBody) message.getBody();
        final Spannable span = EaseSmileUtils.getSmiledText(context, txtBody.getMessage());

        // 设置内容
        holder.tv_content.setText(easeTuWenVO.getContent(), TextView.BufferType.SPANNABLE);

        // 设置内容
        holder.tv_content.setText(easeTuWenVO.getContent(), TextView.BufferType.SPANNABLE);

        if (Math.abs(PUSH_TYPE) == HXConstant.PUSH_ARTICLE||Math.abs(PUSH_TYPE) == HXConstant
                .PUSH_CAMPAIGN) {
            holder.tv_content_left.setVisibility(View.GONE);
            holder.tv_content.getPaint().setFlags(0);
            holder.tv_title.setVisibility(View.GONE);
            holder.tv_content.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            if(TextUtils.isEmpty(easeTuWenVO.getCreateTime())){
                holder.tv_content_below.setVisibility(View.GONE);
            }else{
                holder.tv_content_below.setVisibility(View.VISIBLE);
                holder.tv_content_below.setText(easeTuWenVO.getCreateTime());
                holder.tv_content_below.setText(Html.fromHtml(easeTuWenVO.getCreateTime()));
            }
        } else if(Math.abs(PUSH_TYPE) == HXConstant.PUSH_SHOP_INFO){
            // 设置title
            holder.tv_content_left.setVisibility(View.VISIBLE);
            holder.tv_title.setText(span, TextView.BufferType.SPANNABLE);
            holder.tv_title.setVisibility(View.VISIBLE);
            holder.tv_content.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            // 设置内容
            if(easeTuWenVO.getContent().contains("^")){
                holder.tv_content_left.setText(easeTuWenVO.getContent().substring(0,easeTuWenVO.getContent()
                        .indexOf("^")),TextView.BufferType.SPANNABLE);
                holder.tv_content.setText(easeTuWenVO.getContent().substring(easeTuWenVO.getContent()
                        .indexOf("^")+1,easeTuWenVO.getContent().length()),
                        TextView.BufferType.SPANNABLE);
                holder.tv_content.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }else{
                holder.tv_content.getPaint().setFlags(0);  //取消中划线
            }
        }

        EaseConstant.setImage(holder.iv_image,
                easeTuWenVO.getUrl(), R.drawable.ease_default_image,
                holder.iv_image);

        if (message.direct == EMMessage.Direct.SEND) {
            setMessageSendCallback();
            switch (message.status) {
                case CREATE:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.GONE);
                    // 发送消息
                    //                sendMsgInBackground(message);
                    break;
                case SUCCESS: // 发送成功
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.GONE);
                    break;
                case FAIL: // 发送失败
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.VISIBLE);
                    break;
                case INPROGRESS: // 发送中
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        } else {
            if (!message.isAcked() && message.getChatType() == EMMessage.ChatType.Chat) {
                try {
                    EMChatManager.getInstance().ackMessageRead(message.getFrom(), message.getMsgId());
                    message.isAcked = true;
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    @Override
    public void onBubbleClick() {

        switch (Math.abs(PUSH_TYPE)) {
            case HXConstant.PUSH_ARTICLE:
                Intent articleIntent = new Intent();
                articleIntent.setClass(getContext(), EaseH5DetailAct.class);
                articleIntent.putExtra("sys_msg_url", easeTuWenVO.getH5Url());
                articleIntent.putExtra("title", "文章详情");
                EaseConstant.startActivity(articleIntent, (Activity) getContext());
                break;

            case HXConstant.PUSH_CAMPAIGN:
                Intent actIntent = new Intent();
                actIntent.setClass(getContext(), EaseH5DetailAct.class);
                actIntent.putExtra("sys_msg_url",
                        easeTuWenVO.getH5Url().replace("{userId}", String.valueOf(
                                EaseConstant.currentUser.getFirendsUserInfo().getId()))
                );
                actIntent.putExtra("title", "活动详情");
                EaseConstant.startActivity(actIntent, (Activity) getContext());
                break;

            case HXConstant.PUSH_SHOP_INFO:
                if (EaseConstant.shopID > 0)  //用户app才需要进入
                {
                    EaseConstant.startShopProduceActivity((Activity) getContext(), easeTuWenVO.getId());
                }else if(EaseConstant.shopID < 0){
                    Intent produceIntent = new Intent();
                    produceIntent.setClass(getContext(), EaseH5DetailAct.class);
                    produceIntent.putExtra("sys_msg_url",
                            easeTuWenVO.getH5Url()+"&isHide=true&promotionUserId="+EaseConstant.currentUser
                                    .getFirendsUserInfo().getId());
                    produceIntent.putExtra("title", "商品详情");
                    EaseConstant.startActivity(produceIntent, (Activity) getContext());
                }
                break;
        }

    }
}
