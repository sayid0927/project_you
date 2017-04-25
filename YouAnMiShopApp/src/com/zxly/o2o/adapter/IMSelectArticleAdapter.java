package com.zxly.o2o.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.ui.EaseH5DetailAct;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.model.PromotionArticle;
import com.zxly.o2o.shop.R;

/**
 * Created by Administrator on 2016/1/5.
 */
public class IMSelectArticleAdapter extends ObjectAdapter{

    private int checkPosition = -1;

    public IMSelectArticleAdapter(Context _context) {
        super(_context);
    }

    public int getCheckPosition() {
        return checkPosition;
    }

    public void setCheckPosition(int checkPosition) {
        this.checkPosition = checkPosition;
    }

    @Override
    public int getLayoutId() {
        return R.layout.row_select_item_for_push;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        final PromotionArticle item = (PromotionArticle)getItem(position);
        if(convertView==null){
            vh=new ViewHolder();
            convertView=inflateConvertView();
            vh.publishTime = (TextView) convertView.findViewById(R.id.tv_publish_time);
            vh.title = (TextView) convertView.findViewById(R.id.title);
            vh.checkIcon = (ImageView) convertView.findViewById(R.id.check_icon);
            vh.imageView = (NetworkImageView) convertView.findViewById(R.id.iv_icon);
            vh.go = (ImageView) convertView.findViewById(R.id.go);

            convertView.setTag(vh);
        }else{
            vh= (ViewHolder)convertView.getTag();
        }

        vh.go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent articleIntent = new Intent();
                articleIntent.setClass(context, EaseH5DetailAct.class);
//                articleIntent.putExtra("sys_msg_url", item.getH5Url().replace("{userId}", String.valueOf(
//                        EaseConstant.currentUser.getFirendsUserInfo().getId())));
                articleIntent.putExtra("sys_msg_url", item.getUrl());
                articleIntent.putExtra("title", "文章详情");
                EaseConstant.startActivity(articleIntent, (Activity) context);
            }
        });

        if (checkPosition == position) {
            vh.checkIcon.setImageResource(
                    R.drawable.checkbox_press);
        } else {
            vh.checkIcon.setImageResource(
                    R.drawable.checkbox_normal);
        }

        vh.imageView.setDefaultImageResId(R.drawable.icon_default_330x330);
        vh.imageView.setImageUrl(item.getHeadUrl(), AppController.imageLoader);

        vh.title.setText(item.getTitle());
        vh.publishTime.setText("发布时间:"+EaseConstant.formatOrderTime(item.getCreateTime()));
        return convertView;
    }

    class ViewHolder {
        TextView publishTime;
        TextView title;
        ImageView checkIcon;
        ImageView go;
        NetworkImageView imageView;
    }
}
