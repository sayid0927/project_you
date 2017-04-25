package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.activity.GalleryViewPagerAct;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.ImageUtil;

/**
 * Created by Administrator on 2015/12/28.
 */
public class ListViewObjectAdapter extends ObjectAdapter {
    public ListViewObjectAdapter(Context _context) {
        super(_context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.network_image_row;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        NetworkImageView imageView;
        if (convertView == null) {
            convertView = inflateConvertView();
            imageView=(NetworkImageView)convertView.findViewById(R.id.image);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GalleryViewPagerAct
                            .start(AppController.getInstance().getTopAct(), content.toArray(new String[content.size
                                            ()]),
                                    position);
                }
            });
            convertView.setTag(imageView);
        }else{
            imageView=(NetworkImageView)convertView.getTag();
        }

        ImageUtil.setImage(imageView,(String)getItem(position), R.drawable.pic_normal, null);

        return convertView;
    }
}
