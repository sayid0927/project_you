package com.zxly.o2o.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.easemob.easeui.EaseConstant;
import com.zxly.o2o.activity.AlbumActivity;
import com.zxly.o2o.activity.MyCircleThirdAct;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.MyImageManager;
import com.zxly.o2o.util.ParameCallBackById;
import com.zxly.o2o.util.ViewUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/5/26.
 */
public class GridImageWithDeleteAdapter extends ObjectAdapter {
    private Context context;

    public GridImageWithDeleteAdapter(Context _context) {
        super(_context);
        this.context = _context;

    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public int getLayoutId() {
        return R.layout.grid_image_item;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final String path = (String) getItem(position);

        ViewHolder viewHolder;
        viewHolder = new ViewHolder();
        convertView = inflateConvertView();
        viewHolder.content = (ImageView) convertView.findViewById(R.id.pic_select_btn);
        viewHolder.deleteBtn = (ImageView) convertView.findViewById(R.id.forum_publish_pic_clean_btn);

        viewHolder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (path.contains("_default")) {
                    if (context instanceof MyCircleThirdAct) {
                        if(content.size()>0) {
                            content.remove(getCount() - 1);
                        }
                        if (getCount() < 10) {
                            intent = new Intent(context, AlbumActivity.class);
                            intent.putExtra("dataList", content);
                            AlbumActivity
                                    .start((MyCircleThirdAct) context, intent, new ParameCallBackById() {
                                        @Override
                                        public void onCall(int id, Object object) {
                                            clear();
                                            addItem(((ArrayList<String>) object), false);
                                            addItem("_default", false);
                                            notifyDataSetChanged();
                                        }
                                    });
                        } else {
                            ViewUtils.showToast("最多上传9张图片!");
                        }
                    }
                } else {
                    EaseConstant.startShowLocalBigImageViewActivity((Activity) context, path);
                }
            }
        });


        Log.e("===============", position + "image======" + path);
        if (path.contains("_default")) {
            viewHolder.deleteBtn.setVisibility(View.GONE);
            viewHolder.content.setImageResource(R.drawable.qz_icon_tjtp);
        } else {
            viewHolder.deleteBtn.setVisibility(View.VISIBLE);
            MyImageManager.from(context)
                    .displayImage(viewHolder.content, path, R.drawable.pic_normal, 100, 100);
            viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    content.remove(position);
                    notifyDataSetChanged();
                }
            });
        }


        return convertView;
    }

    class ViewHolder {
        ImageView content;
        ImageView deleteBtn;
    }
}
