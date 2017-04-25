package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxly.o2o.model.UserMaintain;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

public class RenewAdapter extends ObjectAdapter {

    public RenewAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflateConvertView();
            holder = new ViewHolder();
            holder.viewTopLine = convertView
                    .findViewById(R.id.view_top_line);
            holder.txtEffectTime = (TextView) convertView
                    .findViewById(R.id.txt_effect_time);
            holder.txtLeftDay = (TextView) convertView.findViewById(R.id.txt_left_day);
            holder.txtProductName = (TextView) convertView
                    .findViewById(R.id.txt_product_name);
            holder.txtPrice = (TextView) convertView
                    .findViewById(R.id.txt_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        UserMaintain maintain = (UserMaintain) getItem(position);
        if (0 == position) {
            ViewUtils.setVisible(holder.viewTopLine);
        } else {
            ViewUtils.setGone(holder.viewTopLine);
        }
        ViewUtils.setText(holder.txtEffectTime, "生效日：" + StringUtil.getDateByMillis(maintain.getEffectTime(), "yyyy.MM.dd"));
        if (maintain.getResidueTime() < 1) {
            ViewUtils.setText(holder.txtLeftDay, "已过期");
        } else {
            ViewUtils.setText(holder.txtLeftDay, maintain.getResidueTime() + "天");
        }
        ViewUtils.setText(holder.txtProductName, maintain.getProductName());
        ViewUtils.setText(holder.txtPrice, "售价：￥" +  maintain.getPrice());
        return convertView;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_renew;
    }

    class ViewHolder {
        View viewTopLine;
        TextView txtEffectTime;
        TextView txtLeftDay;
        TextView txtProductName;
        TextView txtPrice;
    }

}
