package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easemob.easeui.widget.shapeimageview.PorterShapeImageView;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.model.GiftGetInfo;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ViewUtils;

/**
 * Created by dsnx on 2016/6/13.
 */
public class SysMsgAdater extends ObjectAdapter {
    public SysMsgAdater(Context _context) {
        super(_context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_sys_msg;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null)
        {
            holder=new ViewHolder();
            convertView=inflateConvertView();
            holder.headImage= (PorterShapeImageView) convertView.findViewById(R.id.head_image);
            holder.txtMsg = (TextView) convertView.findViewById(R.id.txt_msg);
            convertView.setTag(holder);
        }else
        {
            holder= (ViewHolder) convertView.getTag();
        }
        GiftGetInfo giftGetInfo= (GiftGetInfo) getItem(position);
        holder.headImage.setImageUrl(giftGetInfo.getHeadUrl(), AppController.imageLoader);
        StringBuffer buffer=new StringBuffer(giftGetInfo.getUserName());
        buffer.append("领取了");
        if(giftGetInfo.getDiscountType()==1)
        {
            buffer.append("现金折扣");
        }else
        {
            buffer.append("礼品赠送  ");
        }
        buffer.append(giftGetInfo.getDiscountInfo());
        ViewUtils.setText(holder.txtMsg,buffer.toString());
        return convertView;
    }



    private class ViewHolder{
        PorterShapeImageView headImage;
        TextView txtMsg;
    }
}
