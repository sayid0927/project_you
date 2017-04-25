package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.User;
import com.zxly.o2o.o2o_user.R;

/**
 * Created by dsnx on 2015/12/11.
 */
public class ActiveUserAdapter extends ObjectAdapter {

    public ActiveUserAdapter(Context _context) {
        super(_context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_user_icon;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final User user= (User) getItem(position);
        if(convertView==null)
        {
            convertView = inflateConvertView();
            holder = new ViewHolder();
            holder.userIconImageView= (NetworkImageView) convertView.findViewById(R.id.img_user);
            convertView.setTag(holder);

        }else
        {
            holder= (ViewHolder) convertView.getTag();
        }

        holder.userIconImageView.setImageUrl(user.getThumHeadUrl(), AppController.imageLoader);
        return convertView;
    }

    class ViewHolder{
        NetworkImageView userIconImageView;
    }
}
