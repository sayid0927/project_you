package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zxly.o2o.model.TagModel;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.view.FlowTagLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejun on 16/9/10.
 * 标签布局流 自动换行 可多选单选及不选适配器
 */
public class TagAdapter extends BaseAdapter implements FlowTagLayout.OnInitSelectedPosition {

    private final Context mContext;
    private final List<TagModel> mDataList;

    public TagAdapter(Context context) {
        this.mContext = context;
        mDataList = new ArrayList<TagModel>();
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_settag, null);

        TextView textView = (TextView) view.findViewById(R.id.tv_tag);
        TagModel t = mDataList.get(position);


        textView.setText(t.getName());
        return view;
    }

    public void onlyAddAll(List<TagModel> datas) {
        mDataList.addAll(datas);
        notifyDataSetChanged();
    }

    public void clearAndAddAll(List<TagModel> datas) {
        mDataList.clear();
        onlyAddAll(datas);
    }

    @Override
    public boolean isSelectedPosition(int position) {
        if (mDataList.get(position).ischoose()) {
            return true;
        }
        return false;
    }
}