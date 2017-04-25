package com.easemob.easeui.widget.chatrow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.R;
import com.easemob.easeui.AppException;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.model.SysMsgVO;
import com.easemob.easeui.ui.EaseH5DetailAct;
import com.easemob.easeui.ui.SysMsgDetailActivity;
import com.easemob.easeui.utils.EaseSmileUtils;
import com.easemob.easeui.utils.GsonParser;
import com.easemob.easeui.widget.VolleyImageView;
import com.easemob.exceptions.EaseMobException;
import com.google.gson.reflect.TypeToken;

/**
 * Created by Administrator on 2015/12/5.消息框
 */
public class EaseChatRowSys extends EaseChatRow {
    ViewHolder holder;
    SparseArray<SysMsgVO> sysMsgVOs = new SparseArray<SysMsgVO>();
    private Spannable span;
    private CharSequence msgContent;
    private CharSequence time;

    public EaseChatRowSys(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
                isTimestamp(false);
    }

    public class ViewHolder {
        TextView tv_title;
        TextView tv_content;
        TextView tv_time;
        TextView tv_goto_detail;
        VolleyImageView iv_pic;
    }

    @Override
    protected void onInflatView() {
        holder = new ViewHolder();
        inflater.inflate(R.layout.ease_row_received_sys_message, this);

    }

    @Override
    protected void onFindViewById() {
        holder.tv_title = (TextView) findViewById(R.id.tv_sys_title);
        holder.tv_time = (TextView) findViewById(R.id.tv_time);
        holder.tv_content = (TextView) findViewById(R.id.tv_content);
        holder.tv_goto_detail = (TextView) findViewById(R.id.tv_goto_detail);
        holder.tv_goto_detail.setVisibility(View.GONE);//个推消息版本不需显示 查看详情 文字
        holder.iv_pic = (VolleyImageView) findViewById(R.id.iv_pic);
    }

    @Override
    protected void onUpdateView() {
        adapter.notifyDataSetChanged();
    }

    private String getString(EMMessage message) {
        try {
            return message.getStringAttribute("expend");
        } catch (EaseMobException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onSetUpView() {
        onFindViewById();

        TextMessageBody txtBody = (TextMessageBody) message.getBody();
        SysMsgVO sysMsgVO = new SysMsgVO();
        String expendString = null;

        span = EaseSmileUtils.getSmiledText(context, txtBody.getMessage());

        TypeToken<SysMsgVO> type = new TypeToken<SysMsgVO>() {
        };
        try {
            expendString = message.getStringAttribute("expend");
            String url = "";
            if (expendString.startsWith("http://")) {
                sysMsgVO = new SysMsgVO();
                sysMsgVO.setUrl(expendString);
                url = expendString;
            } else if (expendString.startsWith("{")) {
                sysMsgVO = GsonParser.getInstance()
                        .fromJson(expendString,
                                type);
                url = sysMsgVO.getUrl();
            } else {  //订单
                sysMsgVO = new SysMsgVO();
                sysMsgVO.setOrderNo(expendString);
            }

            if (url != null && !"".equals(url)) {
                sysMsgVO.setUrl(url.replace("{userId}", "" + EaseConstant.currentUser
                        .getFirendsUserInfo().getUserId()));
            } else if (TextUtils.isEmpty(sysMsgVO.getContent())) {
                sysMsgVO.setContent(span.toString());
            }
            //isUnread--已读未读状态，由于普通消息不能手动添加状态，故添加扩展属性，item点击时改变状态
            sysMsgVO.setUnRead(message.getBooleanAttribute("isUnread",false));
            sysMsgVO.setType(message.getIntAttribute("what"));
            sysMsgVO.setTypeName(message.getStringAttribute("nickname"));
            if(!message.getStringAttribute("nickname").equals("注册提醒")){
                sysMsgVO.setId(message.getIntAttribute("busId"));
            }
            //商户改版新增时间取值
            sysMsgVO.setMsgTime(message.getMsgTime());
            sysMsgVOs.put(position, sysMsgVO);
        } catch (EaseMobException e) {
            try {
                sysMsgVO = new SysMsgVO();
                sysMsgVO.setUnRead(message.getBooleanAttribute("isUnread",false));
                sysMsgVO.setId(message.getIntAttribute("busId"));
                sysMsgVO.setType(message.getIntAttribute("what"));
                sysMsgVO.setTypeName(message.getStringAttribute("nickname"));
                //商户改版新增时间取值
                sysMsgVO.setMsgTime(message.getMsgTime());
                sysMsgVOs.put(position, sysMsgVO);
            } catch (EaseMobException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } catch (AppException e) {
            e.printStackTrace();
            expendString = null;
        }
        if (expendString != null) {
//            time = HXConstant.formatOrderLongTime(
//                    sysMsgVO.getPublishTime() == 0 ? sysMsgVO.getCreateTime() : sysMsgVO.getPublishTime());
            time =HXConstant.formatOrderLongTime(sysMsgVO.getMsgTime()==0?sysMsgVO.getPublishTime():sysMsgVO.getMsgTime());
            msgContent = sysMsgVO.getContent();
        } else {
            // 设置内容
            msgContent = span;
            time = "";
        }

        // 设置时间
        holder.tv_time.setText(time, TextView.BufferType.SPANNABLE);
        // 设置内容
        if("订单提醒".equals(sysMsgVO.getTypeName())){
            holder.tv_content.setMaxLines(5);
        }else{
            holder.tv_content.setMaxLines(2);
        }
        holder.tv_content.setText(msgContent, TextView.BufferType.SPANNABLE);
        // 设置title
        if (TextUtils.isEmpty(sysMsgVO.getTitle())) {
            if(sysMsgVO.isUnRead()){
                holder.tv_title.setTextColor(Color.parseColor("#999999"));
                holder.tv_goto_detail.setTextColor(Color.parseColor("#999999"));
            }
            holder.tv_title.setText(span, TextView.BufferType.SPANNABLE);
        } else {
            if(sysMsgVO.isUnRead()){
                holder.tv_title.setTextColor(Color.parseColor("#999999"));
                holder.tv_goto_detail.setTextColor(Color.parseColor("#999999"));
            }
            holder.tv_title.setText(sysMsgVO.getTitle(), TextView.BufferType.SPANNABLE);
        }

        if("延保提醒".equals(sysMsgVO.getTypeName())){  //延保消息
            holder.tv_goto_detail.setVisibility(View.GONE);
            holder.iv_pic.setVisibility(View.GONE);
        }else if("订单提醒".equals(sysMsgVO.getTypeName())){
            holder.iv_pic.setVisibility(View.VISIBLE);
            EaseConstant.setImage( holder.iv_pic, sysMsgVO.getImageUrl(), R.drawable.ease_default_image,
                    holder.iv_pic);
        }else{
            holder.iv_pic.setVisibility(View.GONE);
        }

        //        conversation.setOnClickListener(new OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //
        //            }
        //        });


    }

    @Override
    public void onBubbleClick() {
        if (sysMsgVOs.size() == 0 || "".equals(sysMsgVOs.get(position).getUrl())) {
            if (EaseConstant.shopID > 0 &&sysMsgVOs.get(position).getOrderNo() != null) {  //进入订单详情
                EaseConstant.startOrderInfoActivity((Activity) context,
                        sysMsgVOs.get(position).getOrderNo());
            } else if (sysMsgVOs.get(position)!=null&&"延保提醒".equals(sysMsgVOs.get(position).getTypeName())) {

                if (EaseConstant.shopID < 0) {
                        EaseConstant.startInsureShopActivity((Activity) context,
                                sysMsgVOs.get(position).getId(),"信息补充");

                } else if (EaseConstant.shopID > 0) {
                    EaseConstant.startInsureUserActivity((Activity) context, sysMsgVOs.get(position).getId());
                }
            }else if(EaseConstant.shopID >0 &&sysMsgVOs.get(position).getType()==HXConstant
                    .SYS_BENEFIT_KEY_USER){
EaseConstant.startDiscountDetailAct((Activity) context,sysMsgVOs.get(position).getId());
            } else if(EaseConstant.shopID < 0&&sysMsgVOs.get(position)!=null&&sysMsgVOs.get(position).getUserId()!=0){
                //进入该注册用户的会员详情页面
                    EaseConstant.startOutLineFansDetailActivity((Activity) context,sysMsgVOs.get(position).getUserId());
            }else {
                Intent intent = new Intent();
                intent.setClass(context, SysMsgDetailActivity.class);
                intent.putExtra("title", span.toString());
                intent.putExtra("content", msgContent.toString());
                intent.putExtra("time", time.toString());
                if (sysMsgVOs != null && sysMsgVOs.get(position) != null && sysMsgVOs.get(position)
                        .getType() == 100) {
                    //门店快讯
                    intent.putExtra("id", sysMsgVOs.get(position).getId());
                }
                EaseConstant.startActivity(intent, (Activity) context);
            }
        } else if (sysMsgVOs != null && sysMsgVOs.get(position) != null) {
            EaseH5DetailAct.start((Activity) context, sysMsgVOs.get(position).getUrl(), sysMsgVOs
                    .get(position).getTypeName(), sysMsgVOs.get(position).getId());
        }



    }
}
