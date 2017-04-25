package com.zxly.o2o.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxly.o2o.model.OrderCommVO;
import com.zxly.o2o.o2o_user.R;

/**
 * Created by Benjamin on 2015/6/5.
 */
public class CommisionAdapter extends ObjectAdapter {

    public CommisionAdapter(Context context) {
        super(context);
    }


    @Override
    public Object getItem(int position) {
        return content.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.commision_list_item;
    }

    class ViewHolder{
        TextView textLeft;
        TextView textRight;
        TextView textRightBottom;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder ;
        OrderCommVO mItem = (OrderCommVO)getItem(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflateConvertView();
            viewHolder.textLeft = (TextView)convertView.findViewById(R.id.commision_list_item_left);
            viewHolder.textRight = (TextView)convertView.findViewById(R.id.commision_list_item_right);
            viewHolder.textRightBottom = (TextView)convertView.findViewById(R.id.commision_list_item_right_bottom);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textLeft.setText(Html.fromHtml(String.format(context.getResources().getString(R.string.commision_list_item_left), mItem.getOrderNo(), mItem.getTimeStr())));
        //        StringUtil.setTextSpan(7, 14, 26, 0, Html.fromHtml(String.format(context.getResources().getString(R.string.commision_detail_top_string), "889554455545", "2015-10-10")).toString(), viewHolder.textLeft, context.getResources().getDisplayMetrics());
//        StringUtil.setTextSpan(0, 6, 18, 0, Html.fromHtml(String.format(context.getResources().getString(R.string.commision_list_item_right), "+40.00", "预到账")).toString(), viewHolder.textRight, context.getResources().getDisplayMetrics());
        viewHolder.textRight.setText(new StringBuffer("+ ").append(mItem.getOrderCommission().toString()));
        switch (mItem.getStatus())
        {
            case 1://已到账
                viewHolder.textRight.setTextColor(context.getResources().getColor(R.color.c_green_449900));
                break;
            case 2://预到账
                viewHolder.textRight.setTextColor(context.getResources().getColor(R.color.black));
                break;
            case 3://订单取消
                viewHolder.textRight.setTextColor(context.getResources().getColor(R.color.grey_aaaaaa));
                viewHolder.textRight.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                break;
        }

        viewHolder.textRightBottom.setText(mItem.getStatusName());
        return convertView;
    }

}

