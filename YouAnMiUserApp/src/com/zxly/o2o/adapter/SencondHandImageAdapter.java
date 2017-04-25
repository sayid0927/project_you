package com.zxly.o2o.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.zxly.o2o.config.Config;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.MyImageManager;

import java.util.ArrayList;

/**
 * Created by Benjamin on 2015/7/21.
 */
public class SencondHandImageAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> dataList = new ArrayList<String>();
    private boolean isHttp;

    public SencondHandImageAdapter(Context c, ArrayList<String> dataList) {

        mContext = c;
        this.dataList = dataList;
    }

    public void setItems(ArrayList<String> mDataList, boolean isHttp) {
        this.isHttp = isHttp;
        notifyDataSetChanged();


    }

    public void addItems(String data) {
        this.dataList.add(data);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 65,
                            Config.displayMetrics)));
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            imageView = (ImageView) convertView;
        }
        String path;
        if (dataList != null && position < dataList.size()) {
            path = dataList.get(position);
        } else {
            path = "camera_default";
        }
        android.util.Log.e("path", "path:" + path + "::position" + position);
        if (path.contains("default")) {
            imageView.setImageResource(R.drawable.pic_normal);
        } else if (isHttp) {
//            ImageUtil.setImage(imageView, path, R.drawable.pic_normal, true);
        } else {
            MyImageManager.from(mContext)
                    .displayImage(imageView, path, R.drawable.pic_normal, 100, 100);
        }
        return imageView;
    }


}
