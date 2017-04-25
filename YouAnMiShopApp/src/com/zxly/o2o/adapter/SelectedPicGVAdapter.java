package com.zxly.o2o.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.MyImageManager;

import java.util.ArrayList;

public class SelectedPicGVAdapter extends BaseAdapter implements OnClickListener {

    private Context mContext;
    private ArrayList<String> dataList;
    private ArrayList<String> selectedDataList = new ArrayList<String>();
    private DisplayMetrics dm;

    public SelectedPicGVAdapter(Context c, ArrayList<String> dataList, ArrayList<String> selectedDataList) {

        mContext = c;
        this.dataList = dataList;
        this.selectedDataList = selectedDataList;
        dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);

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

    /**
     * 存放列表项控件句柄
     */
    private class ViewHolder {
        public ImageView imageView;
        public ToggleButton toggleButton;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.select_imageview, parent, false);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
            viewHolder.toggleButton = (ToggleButton) convertView.findViewById(R.id.toggle_button);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String path;
        if (dataList != null && dataList.size() > position)
            path = dataList.get(position);
        else
            path = "camera_default";
        if (path.contains("_default")) {
            viewHolder.imageView.setImageResource(R.drawable.pic_normal);
        } else if( path.contains("_camera")){
            viewHolder.toggleButton.setVisibility(View.GONE);
            viewHolder.imageView.setImageResource(R.drawable.qz_icon_pszp);
        }else {
            viewHolder.toggleButton.setVisibility(View.VISIBLE);
            MyImageManager.from(mContext).displayImage(viewHolder.imageView, path, R.drawable.pic_normal, 100, 100);
        }
        viewHolder.toggleButton.setTag(position);
        viewHolder.toggleButton.setOnClickListener(this);
        viewHolder.imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.toggleButton.performClick();
            }
        });
        if (isInSelectedDataList(path)) {
            viewHolder.toggleButton.setChecked(true);
        } else {
            viewHolder.toggleButton.setChecked(false);
        }

        return convertView;
    }

    private boolean isInSelectedDataList(String selectedString) {
        for (int i = 0; i < selectedDataList.size(); i++) {
            if (selectedDataList.get(i).equals(selectedString)) {
                return true;
            }
        }
        return false;
    }

    public int dipToPx(int dip) {
        return (int) (dip * dm.density + 0.5f);
    }

    @Override
    public void onClick(View view) {
        if (view instanceof ToggleButton) {
            ToggleButton toggleButton = (ToggleButton) view;
            int position = (Integer) toggleButton.getTag();
            if (dataList != null && mOnItemClickListener != null && position < dataList.size()) {
                mOnItemClickListener.onItemClick(toggleButton, position, dataList.get(position), toggleButton.isChecked());
            }
        }
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener l) {
        mOnItemClickListener = l;
    }

    public interface OnItemClickListener {
        void onItemClick(ToggleButton view, int position, String path, boolean isChecked);
    }

}
