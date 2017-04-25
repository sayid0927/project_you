package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.zxly.o2o.model.AddPeopleInfo;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.view.CircleImageView;

import java.util.Comparator;
import java.util.List;

/**
 * Created by hejun on 2016/8/30.
 */
public class AddPeopleToGroupAdapter extends ArrayAdapter<AddPeopleInfo> implements SectionIndexer {


    private final LayoutInflater layoutInflater;
    private int res;

    public AddPeopleToGroupAdapter(Context context, int resource, List<AddPeopleInfo> objects) {
        super(context, resource, objects);
        this.res = resource;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public AddPeopleInfo getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder=new ViewHolder();
            if (res == 0) {
                convertView = layoutInflater.inflate(R.layout.item_search_filterdata, null);
            } else {
                convertView = layoutInflater.inflate(res, null);
            }
            viewHolder.img_user_head= (CircleImageView) convertView.findViewById(R.id.img_user_head);
            viewHolder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_phone= (TextView) convertView.findViewById(R.id.tv_phone);
            viewHolder.img_check= (ImageView) convertView.findViewById(R.id.img_check);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        AddPeopleInfo addPeopleInfo = getItem(position);
        viewHolder.img_user_head.setImageUrl(addPeopleInfo.getHeadUrl(), R.drawable.default_head_big);
        viewHolder.tv_phone.setText(addPeopleInfo.getPhoneNum());
        viewHolder.tv_phone.setText(addPeopleInfo.getUserName());
        viewHolder.img_check.setImageResource(addPeopleInfo.isCheck()?R.drawable.icon_check_press:R.drawable.icon_check_normal);
        return convertView;
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    private class ViewHolder{
        CircleImageView img_user_head;
        TextView tv_name;
        TextView tv_phone;
        ImageView img_check;

    }

    public void sortList() {
        AddPeopleToGroupAdapter.this.sort(
                new Comparator<AddPeopleInfo>() {
                    @Override
                    public int compare(AddPeopleInfo lhs, AddPeopleInfo rhs) {
                        if (lhs.getFirstLetter()
                                .equals(rhs.getFirstLetter())) {
                            return lhs.getUserName()
                                    .compareTo(rhs.getUserName());
                        } else {
                            if ("#".equals(lhs.getFirstLetter())) {
                                return 1;
                            } else if ("#".equals(rhs.getFirstLetter())) {
                                return -1;
                            }
                            return lhs.getFirstLetter()
                                    .compareTo(rhs.getFirstLetter());
                        }
                    }
                }
        );
    }
}
