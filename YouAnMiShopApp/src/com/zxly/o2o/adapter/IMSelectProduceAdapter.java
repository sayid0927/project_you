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
import com.zxly.o2o.model.CommissionProduct;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ViewUtils;

/**
 * Created by Administrator on 2016/1/5.
 */
public class IMSelectProduceAdapter extends ObjectAdapter {
    private int checkPosition = -1;

    public IMSelectProduceAdapter(Context _context) {
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
        return R.layout.row_select_produce_for_push;
    }

    class ViewHolder {
        TextView realPrice;
        TextView nowPrice;
        TextView title;
        NetworkImageView icon;
        ImageView go;
        ImageView checkIcon;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder vh;
        final CommissionProduct item = (CommissionProduct) getItem(position);
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = inflateConvertView();
            vh.realPrice = (TextView) convertView.findViewById(R.id.real_price);
            vh.nowPrice = (TextView) convertView.findViewById(R.id.now_price);
            vh.title = (TextView) convertView.findViewById(R.id.title);
            vh.go = (ImageView) convertView.findViewById(R.id.go);
            vh.icon = (NetworkImageView) convertView.findViewById(R.id.iv_icon);
            vh.checkIcon = (ImageView) convertView.findViewById(R.id.check_icon);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        ViewUtils.setTextPrice(vh.nowPrice, item.getPrice() - item.getPreference());
        if(item.getPreference()>0)
        {
            ViewUtils.strikeThruText(vh.realPrice, item.getPrice());
        }

        if (checkPosition == position) {
            vh.checkIcon.setImageResource(
                    R.drawable.checkbox_press);
        } else {
            vh.checkIcon.setImageResource(
                    R.drawable.checkbox_normal);
        }

        vh.go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent produceIntent = new Intent();
                produceIntent.setClass(context, EaseH5DetailAct.class);
                produceIntent.putExtra("sys_msg_url", item.getUrl() + "&isHide=1");
                produceIntent.putExtra("title", "商品详情");
                produceIntent.putExtra("hideTitle", false);
                EaseConstant.startActivity(produceIntent, (Activity) context);
            }
        });

        vh.title.setText(item.getName());

        vh.icon.setDefaultImageResId(R.drawable.icon_default_330x330);
        vh.icon.setImageUrl(item.getHeadUrl(), AppController.imageLoader);


        return convertView;
    }
}
