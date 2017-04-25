package com.zxly.o2o.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.easemob.easeui.EaseConstant;
import com.zxly.o2o.activity.AlbumActivity;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.MyImageManager;
import com.zxly.o2o.util.ParameCallBackById;

import java.util.ArrayList;


/**
 * Created by hejun on 2016/9/5.
 */
public class UserDefineDeleteGridAdapter extends ObjectAdapter {
    private Context context;
    private ArrayList<String> imgPaths = new ArrayList<String>();

    public UserDefineDeleteGridAdapter(Context _context) {
        super(_context);
        this.context = _context;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_deletegrid_image;
    }

    public void refreshView(String path){
        if(DataUtil.listIsNull(imgPaths)){
            imgPaths.add(path);
            imgPaths.add("_default");
        } else {
            if(imgPaths.contains("_default")){
                imgPaths.add(imgPaths.size() -1, path);
            } else {
                imgPaths.add(path);
                imgPaths.add("_default");
            }
        }
        clear();
        addItem(imgPaths, true);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        viewHolder = new ViewHolder();
        convertView = inflateConvertView();
        viewHolder.img_add = (ImageView) convertView.findViewById(R.id.img_add);
        viewHolder.img_delete = (ImageView) convertView.findViewById(R.id.img_delete);
        viewHolder.layout_delete = (RelativeLayout) convertView.findViewById(R.id.layout_delete);
        convertView.setTag(viewHolder);
        final String path = (String) getItem(position);
        viewHolder.img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (path.contains("_default")) {//点增加按钮
//                    if(content.size()>0) {
//                        content.remove(getCount() - 1);
//                    }
//                    if (getCount() < 7) {
                    intent = new Intent(context, AlbumActivity.class);
                    intent.putExtra("dataList", content);
                    intent.putExtra("MaxSelectedSize", 6);
                    AlbumActivity
                            .start((FragmentActivity) context, intent, new ParameCallBackById() {
                                @Override
                                public void onCall(int id, Object object) {
                                    imgPaths.clear();
                                    imgPaths.addAll((ArrayList<String>) object);
                                    if (imgPaths.size() < 6) {
                                        imgPaths.add("_default");
                                    }
                                    clear();
                                    addItem(imgPaths, true);

//                                        addItem(((ArrayList<String>) object), false);
//                                        notifyDataSetChanged();
                                }
                            });
//                    } else {
//                        ViewUtils.showToast("最多上传6张图片!");
//                    }
                } else {
                    EaseConstant.startShowLocalBigImageViewActivity((Activity) context, path);
                }
            }
        });

        if (path.contains("_default")) {
            viewHolder.layout_delete.setVisibility(View.GONE);
            viewHolder.img_add.setImageResource(R.drawable.icon_add_img);
        } else {
            viewHolder.layout_delete.setVisibility(View.VISIBLE);
            MyImageManager.from(context)
                    .displayImage(viewHolder.img_add, path, R.drawable.pic_normal, 100, 100);
            viewHolder.img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imgPaths.remove(position);
                    if (imgPaths.size() < 6 && !imgPaths.contains("_default")) {
                        imgPaths.add("_default");
                    }
                    clear();
                    addItem(imgPaths, true);

//                    content.remove(position);
//                    notifyDataSetChanged();
                }
            });
        }

        return convertView;
    }

    class ViewHolder {
        ImageView img_add;
        ImageView img_delete;
        RelativeLayout layout_delete;
    }
}
