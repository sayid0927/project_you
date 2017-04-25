package com.zxly.o2o.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.activity.YamCollegeDetailAct;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.model.CollegeCourse;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

import java.util.ArrayList;

/**
 * Created by fengrongjian on 2016/9/19.
 */
public class YamCourseAdapter extends ObjectAdapter {
    private Activity activity;

    public YamCourseAdapter(Context context, Activity activity) {
        super(context);
        this.activity = activity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflateConvertView();
            holder = new ViewHolder();
            holder.imgCourse = (NetworkImageView) convertView
                    .findViewById(R.id.img_study_course);
            holder.txtTitle = (TextView) convertView
                    .findViewById(R.id.txt_title);
            holder.txtLabel1 = (TextView) convertView.findViewById(R.id.txt_label1);
            holder.txtLabel2 = (TextView) convertView.findViewById(R.id.txt_label2);
            holder.txtTime = (TextView) convertView
                    .findViewById(R.id.txt_time);
            holder.txtCount = (TextView) convertView
                    .findViewById(R.id.txt_study_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CollegeCourse collegeCourse = (CollegeCourse) getItem(position);
        holder.imgCourse.setImageUrl(collegeCourse.getIconUrl(),
                AppController.imageLoader);
        ViewUtils.setText(holder.txtTitle, collegeCourse.getTitle());

        ArrayList<String> lable = collegeCourse.getLable();
        if (DataUtil.listIsNull(lable)) {
            ViewUtils.setGone(holder.txtLabel1);
            ViewUtils.setGone(holder.txtLabel2);
        } else {
            if (lable.size() == 1) {
                if(!StringUtil.isNull(lable.get(0))){
                    ViewUtils.setVisible(holder.txtLabel1);
                    ViewUtils.setText(holder.txtLabel1, lable.get(0));
                } else {
                    ViewUtils.setGone(holder.txtLabel1);
                }
                ViewUtils.setGone(holder.txtLabel2);
            } else if (lable.size() == 2) {
                if(!StringUtil.isNull(lable.get(0))){
                    ViewUtils.setVisible(holder.txtLabel1);
                    ViewUtils.setText(holder.txtLabel1, lable.get(0));
                } else {
                    ViewUtils.setGone(holder.txtLabel1);
                }
                if(!StringUtil.isNull(lable.get(1))){
                    ViewUtils.setVisible(holder.txtLabel2);
                    ViewUtils.setText(holder.txtLabel2, lable.get(1));
                } else {
                    ViewUtils.setGone(holder.txtLabel2);
                }
            }
        }

        ViewUtils.setText(holder.txtTime, StringUtil.getDateByMillis(collegeCourse.getCreateTime()));
        ViewUtils.setText(holder.txtCount, collegeCourse.getReadAmount());
        final long articleId = collegeCourse.getArticleId();
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YamCollegeDetailAct.start(activity, articleId);
            }
        });
        return convertView;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_study_course;
    }

    class ViewHolder {
        NetworkImageView imgCourse;
        TextView txtTitle, txtLabel1, txtLabel2, txtTime, txtCount;
    }

}
