package com.zxly.o2o.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zxly.o2o.activity.FansDetailNewAct;
import com.zxly.o2o.activity.OutLineFansDetailAct;
import com.zxly.o2o.model.FilterPeopleModel;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.CircleImageView;

/**
 * Created by hejun on 2016/9/19.
 */
public class FilterDataAdapter extends ObjectAdapter{

    public FilterDataAdapter(Context _context) {
        super(_context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_search_filterdata;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflateConvertView();
            viewHolder.img_user_head= (CircleImageView) convertView.findViewById(R.id.img_user_head);
            viewHolder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_phone= (TextView) convertView.findViewById(R.id.tv_phone);
            viewHolder.img_check= (ImageView) convertView.findViewById(R.id.img_check);
            viewHolder.layout_container= (RelativeLayout) convertView.findViewById(R.id.layout_container);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.img_check.setVisibility(View.GONE);
        final FilterPeopleModel filterPeopleModel= (FilterPeopleModel) getItem(i);
        viewHolder.img_user_head.setImageUrl(filterPeopleModel.getHeadUrl(),R.drawable.default_head_big);
        String userName = filterPeopleModel.getUserName();
        if(!TextUtils.isEmpty(userName)){
            viewHolder.tv_name.setText(userName);
        }else {
            viewHolder.tv_name.setText(filterPeopleModel.getImei());
        }

        if(!TextUtils.isEmpty(filterPeopleModel.getMobilePhone())){
            viewHolder.tv_phone.setText(filterPeopleModel.getMobilePhone());
        }else {
            viewHolder.tv_phone.setText("未备注手机号");
        }
        viewHolder.layout_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(filterPeopleModel.getUserType()==1){
                    if(filterPeopleModel.isInputFans()){
                        //线下录入粉丝
                        FansDetailNewAct.start((Activity) context,filterPeopleModel.getId(),2);
                    }else {
                        //普通粉丝
                        FansDetailNewAct.start((Activity) context,filterPeopleModel.getId(),1);
                    }
                }else if(filterPeopleModel.getUserType()==2){
                    OutLineFansDetailAct.start((Activity) context,filterPeopleModel.getId());
                }

            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tv_phone;
        TextView tv_name;
        ImageView img_check;
        CircleImageView img_user_head;
        RelativeLayout layout_container;
    }
}
