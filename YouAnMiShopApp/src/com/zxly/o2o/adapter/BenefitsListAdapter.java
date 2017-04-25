package com.zxly.o2o.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.chatuidemo.HXApplication;
import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.ui.ChatActivity;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.widget.VolleyImageView;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.BenefitListVO;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ImageUtil;
import com.zxly.o2o.util.TimeUtil;
import com.zxly.o2o.util.ViewUtils;

/**
 * Created by Administrator on 2016/6/12.
 */
public class BenefitsListAdapter extends ObjectAdapter{
    private String discountInfo = "全部优惠券";
    private long discountId;
    public BenefitsListAdapter(Context _context) {
        super(_context);

    }

    public void setDiscountId(long discountId){
        this.discountId=discountId;
    }

    @Override
    public int getLayoutId() {
        return R.layout.benefits_list_item;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflateConvertView();
            viewHolder.ivHead =(VolleyImageView)convertView.findViewById(R.id.head_image);
            viewHolder.ivMessageSend=(ImageView)convertView.findViewById(R.id.iv_message_send);
            viewHolder.ivPhoneCall=(ImageView)convertView.findViewById(R.id.iv_phone_call);
            viewHolder.tvDate=(TextView)convertView.findViewById(R.id.tv_date);
            viewHolder.tvPhone=(TextView)convertView.findViewById(R.id.tv_phone);
            viewHolder.tvWhat=(TextView)convertView.findViewById(R.id.tv_what);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final BenefitListVO vo = (BenefitListVO) getItem(position);

        if(position==0){
            convertView.findViewById(R.id.head).setVisibility(View.VISIBLE);
            if(discountId==0) {
                ((TextView) convertView.findViewById(R.id.head)).setText(discountInfo);
            }else{
                ((TextView) convertView.findViewById(R.id.head)).setText(vo.getDiscountInfo());
            }
        }else{
            convertView.findViewById(R.id.head).setVisibility(View.GONE);
        }

        viewHolder.tvDate.setText(TimeUtil.formatOrderTime(vo.getTakeTime()));
        if(2 == vo.getUserType()){
            viewHolder.tvPhone.setText("  手机号:"+vo.getUserName());
            viewHolder.ivMessageSend.setVisibility(View.GONE);
            viewHolder.tvWhat.setText("  "+vo.getDiscountInfo());
            ViewUtils.setGone(viewHolder.ivHead);
        }else if(1 == vo.getUserType()){
            if(vo.getShopId()== Account.user.getShopId()) {
                viewHolder.ivMessageSend.setVisibility(View.VISIBLE);
            }else{
                viewHolder.ivMessageSend.setVisibility(View.GONE);
            }
            viewHolder.tvPhone.setText(vo.getUserName());
            viewHolder.tvWhat.setText(vo.getDiscountInfo());
            ViewUtils.setVisible(viewHolder.ivHead);
            ImageUtil.setImage(viewHolder.ivHead,vo.getImageUrl(),R.drawable.ease_default_avatar,true);
        }

        viewHolder.ivMessageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chatUserId = HXApplication.getInstance().parseUserFromID(vo.getUserId(), HXConstant.TAG_USER);
                EaseConstant.startActivityNormalWithStringForResult(ChatActivity.class,
                        (Activity) context, chatUserId,
                        EaseConstant.EXTRA_USER_ID);
            }
        });

        viewHolder.ivPhoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+vo.getUserName()));
                EaseConstant.startActivity(intent,(Activity) context);
            }
        });


        return convertView;
    }

    class ViewHolder{
        VolleyImageView ivHead;
        ImageView ivPhoneCall;
        ImageView ivMessageSend;
        TextView tvPhone;
        TextView tvWhat;
        TextView tvDate;
    }
}
