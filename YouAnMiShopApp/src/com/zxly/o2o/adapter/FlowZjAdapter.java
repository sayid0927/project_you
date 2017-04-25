package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxly.o2o.model.KeyValue;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ViewUtils;

/**
 * Created by dsnx on 2016/5/19.
 */
public class FlowZjAdapter extends ObjectAdapter {

    public FlowZjAdapter(Context _context) {
        super(_context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_flow_zj;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null)
        {
            convertView=inflateConvertView();
            holder=new ViewHolder();
            holder.txtName= (TextView) convertView.findViewById(R.id.txt_name);
            holder.txtDesc= (TextView) convertView.findViewById(R.id.txt_desc);
            convertView.setTag(holder);
        }else
        {
            holder= (ViewHolder) convertView.getTag();
        }
        KeyValue kv= (KeyValue) getItem(position);
        holder.txtName.setText(kv.getKey());
        holder.txtDesc.setText(kv.getValue());
        if(position==(getCount()-1))
        {
            ViewUtils.setGone(convertView,R.id.line);
        }
        return convertView;
    }

    class ViewHolder{
        TextView txtName,txtDesc;
    }
}

