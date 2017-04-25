package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.easemob.easeui.widget.shapeimageview.PorterShapeImageView;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.ImageUtil;

/**
 * Created by Administrator on 2015/12/16.
 */
public class CircleHeadGVAdapter extends ObjectAdapter {

    public CircleHeadGVAdapter(Context _context) {
        super(_context);
    }


    @Override
    public int getLayoutId() {
        return R.layout.ease_circle_view_include;
    }

    @Override
    public int getCount() {
        return super.getCount()>8?8:super.getCount();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final String item = (String) getItem(position);
        if (convertView == null) {
            convertView = inflateConvertView();
        }

        ImageUtil.setImage((PorterShapeImageView) convertView.findViewById(R.id.circle_image_row),
                item, R.drawable.ease_default_avatar,
                null);

        return convertView;
    }
}
