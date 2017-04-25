package com.easemob.easeui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.R;
import com.easemob.easeui.AppException;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.model.GetuiMsg;
import com.easemob.easeui.model.GetuiTypeMsg;
import com.easemob.easeui.request.GetuiMsgCancleRequest;
import com.easemob.easeui.request.GetuiMsgClickRequest;
import com.easemob.easeui.ui.EaseH5DetailAct;
import com.easemob.easeui.utils.GsonParser;
import com.easemob.easeui.widget.VolleyImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejun on 2016/7/18.
 */
public class GetuiTypeMessageAdapter extends ObjectAdapter{

    private static final int LONG_CLICK = 2;
    private final Handler handler;
    private List<GetuiTypeMsg> list;
    private static final int REFRENSH = 1;

    private long id;

    public GetuiTypeMessageAdapter(Context context, Handler handler){
        super(context);
        this.handler =handler;
    }

    @Override
    public int getLayoutId() {
        return R.layout.getui_row_received_sys_message;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         ViewHolder holder;
        if (convertView==null){
            holder = new ViewHolder();
            convertView = inflateConvertView();
            holder.tv_title= (TextView) convertView.findViewById(R.id.tv_sys_title);
            holder.tv_content= (TextView) convertView.findViewById(R.id.tv_content);
            holder.tv_time= (TextView) convertView.findViewById(R.id.tv_time);
            holder.iv_pic= (VolleyImageView) convertView.findViewById(R.id.iv_pic);
            holder.tv_goto_detail= (TextView) convertView.findViewById(R.id.tv_goto_detail);
            holder.bubble= (LinearLayout) convertView.findViewById(R.id.bubble);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.tv_goto_detail.setVisibility(View.GONE);
        final GetuiTypeMsg getuiTypeMsg = (GetuiTypeMsg) content.get(position);
            if(!TextUtils.isEmpty(getuiTypeMsg.getHeadUrl())){
                EaseConstant.setImage(holder.iv_pic, getuiTypeMsg.getHeadUrl(), R.drawable.ease_default_image,
                        holder.iv_pic);
                holder.iv_pic.setVisibility(View.VISIBLE);
            }else{
                holder.iv_pic.setVisibility(View.GONE);
            }
            holder.tv_time.setText(HXConstant.formatOrderLongTime(getuiTypeMsg.getCreateTime()));
            holder.tv_title.setText(getuiTypeMsg.getTitle());
            if(getuiTypeMsg.getStatus()==1){
                if(getuiTypeMsg.getWhat()==HXConstant.SYS_SHOPPRODUCT_KEY_USER||getuiTypeMsg.getWhat()==HXConstant.SYS_ACTIVITY_KEY_USER){
                    holder.tv_content.setTextColor(Color.RED);
                }else{
                    holder.tv_content.setTextColor(context.getResources().getColor(R.color.text_gray));
                }
                //已读
                holder.tv_title.setTextColor(context.getResources().getColor(R.color.text_gray));
                holder.tv_goto_detail.setTextColor(context.getResources().getColor(R.color.text_gray));
            }else{
                if(getuiTypeMsg.getWhat()==HXConstant.SYS_SHOPPRODUCT_KEY_USER||getuiTypeMsg.getWhat()==HXConstant.SYS_ACTIVITY_KEY_USER){
                    holder.tv_content.setTextColor(Color.RED);
                }else{
                    holder.tv_content.setTextColor(context.getResources().getColor(R.color.text_gray));
                }
                holder.tv_title.setTextColor(context.getResources().getColor(R.color.list_itease_primary_color));
                holder.tv_goto_detail.setTextColor(context.getResources().getColor(R.color.list_itease_primary_color));
            }
            if(!TextUtils.isEmpty(getuiTypeMsg.getContent())){
                holder.tv_content.setText(getuiTypeMsg.getContent());
            }else {
                //如果消息的内容是空的（自定义消息）那么就将内容区域设置不可见 显示标题最多两行
                holder.tv_content.setVisibility(View.GONE);
            }
            holder.bubble.setOnClickListener(new View.OnClickListener() {
                String url=getuiTypeMsg.getH5Url();

                @Override
                public void onClick(View v) {
                    //目前个推点击跳转仅以下两种情况1、跳转商品详情页面  2、H5页面
                    if (getuiTypeMsg.getWhat()==HXConstant.SYS_SHOPPRODUCT_KEY_USER){
                        markReadMsgRequest(getuiTypeMsg.getDataId());
                        EaseConstant.startShopProduceActivity((Activity) context,getuiTypeMsg.getBusId());
                    }else if(getuiTypeMsg.getWhat()==HXConstant.SYS_SHOPDEFINE_KEY_USER){
                        EaseConstant.startShowDefineGetuiAct((Activity) context,getuiTypeMsg.getBusId(),getuiTypeMsg.getDataId());
                    }else{
                        markReadMsgRequest(getuiTypeMsg.getDataId());
                        //将链接中userId替换成获取的用户id
                        //加HXConstant.isLoginSuccess判断的原因:这个地方之前有个小问题就是有时在退出登录时，也能拿到id值，但在此处退出后就不应该能操作
                        if (url != null && !"".equals(url)&&url.contains("{userId}")&&getuiTypeMsg.getWhat()==HXConstant.SYS_ACTIVITY_KEY_USER) {
                            if(HXConstant.isLoginSuccess){
                                id = EaseConstant.currentUser.getFirendsUserInfo().getId();
                            }else{
                                id=0;
                            }
                            getuiTypeMsg.setH5Url(url.replace("{userId}", "" + id));
                        }
                        EaseH5DetailAct.start((Activity) context,getuiTypeMsg.getH5Url(),getuiTypeMsg.getTitle(),getuiTypeMsg.getBusId(),true,getuiTypeMsg.getWhat());
                    }
                    handler.obtainMessage(REFRENSH).sendToTarget();

                }

            });

        return convertView;
    }

    private void markReadMsgRequest(long dataId) {
        GetuiMsgClickRequest getuiMsgClickRequest = new GetuiMsgClickRequest(dataId);
        getuiMsgClickRequest.start();
    }


    public class ViewHolder {
        TextView tv_title;
        TextView tv_content;
        TextView tv_time;
        TextView tv_goto_detail;
        VolleyImageView iv_pic;
        LinearLayout  bubble;
    }
}
