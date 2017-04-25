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
import com.zxly.o2o.model.ActivityVO;
import com.zxly.o2o.shop.R;

/**
 * Created by Administrator on 2016/1/11.
 */
public class IMSelectActivityAdapter extends ObjectAdapter {
    public IMSelectActivityAdapter(Context _context) {
        super(_context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.row_select_item_for_push;
    }

    class ViewHolder {
        TextView publishTime;
        TextView title;
        NetworkImageView imageView;
        ImageView go;
        ImageView checkIcon;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = inflateConvertView();
            vh.publishTime = (TextView) convertView.findViewById(R.id.tv_publish_time);
            vh.title = (TextView) convertView.findViewById(R.id.title);
            vh.checkIcon = (ImageView) convertView.findViewById(R.id.check_icon);
            vh.go = (ImageView) convertView.findViewById(R.id.go);
            vh.imageView = (NetworkImageView) convertView.findViewById(R.id.iv_icon);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }


        final ActivityVO item = (ActivityVO) getItem(position);

        vh.go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent actIntent = new Intent();
                actIntent.setClass(context, EaseH5DetailAct.class);
                actIntent.putExtra("sys_msg_url", item.getH5Url().replace("{userId}", String.valueOf(
                        EaseConstant.currentUser.getFirendsUserInfo().getId())));
                actIntent.putExtra("title", "活动详情");
                EaseConstant.startActivity(actIntent, (Activity) context);
            }
        });

        vh.title.setText(item.getTitle());
        vh.imageView.setDefaultImageResId(R.drawable.icon_default_330x330);
        if(item.getImageUrls()!=null){
            vh.imageView.setImageUrl(item.getImageUrls().split(",")[0], AppController.imageLoader);
        }
        vh.publishTime.setText("发布时间:" + EaseConstant.formatOrderTime(item.getCreateTime()));

        return convertView;
    }
}
