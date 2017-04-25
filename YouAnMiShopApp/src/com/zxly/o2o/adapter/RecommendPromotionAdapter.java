package com.zxly.o2o.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxly.o2o.model.ActicityInfo;
import com.zxly.o2o.model.BuyItem;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.DesityUtil;
import com.zxly.o2o.view.MListView;

import java.util.List;
import java.util.Map;

/**
 * Created by kenwu on 2015/12/17.
 */
public class RecommendPromotionAdapter extends ObjectAdapter implements View.OnClickListener {
    public static final int TYPE_ACTIVITY=0;
    public static final int TYPE_ARTICLE=1;
    private static final int TYPE_COUNT =2 ;

    public RecommendPromotionAdapter(Context _context) {
        super(_context);
    }


    // 每个convert view都会调用此方法，获得当前所需要的view样式
    @Override
    public int getItemViewType(int position) {
        Map<Integer,Object> data= (Map<Integer,Object>) getItem(position);
        List<Object> datas= (List<Object>) data.get("data");
        if(datas.get(0) instanceof ActicityInfo){
            return TYPE_ACTIVITY;
        }else {
            return TYPE_ARTICLE;
        }
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }


    @Override
    public int getLayoutId() {
        return R.layout.item_recommend;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView=inflateConvertView();
            holder=new ViewHolder();
            holder.imgType= (ImageView) convertView.findViewById(R.id.img_type);
            holder.mListView= (MListView) convertView.findViewById(R.id.listView);
            convertView.setTag(holder);
            if(getItemViewType(position)==TYPE_ACTIVITY){
            holder.mListView.setAdapter(new PromotionActivityAdapter(context));
                holder.mListView.setDividerHeight(DesityUtil.dp2px(context,5));
             //   holder.mListView.setDivider(0xffaaaaaa);
            }else {
             holder.mListView.setAdapter(new ShopArticleAdapter(context));
                holder.mListView.setDividerHeight(DesityUtil.dp2px(context, 0.2f));
            }
        }else {
            holder= (ViewHolder) convertView.getTag();
        }


        if(getItemViewType(position)==TYPE_ACTIVITY){
            holder.imgType.setImageResource(R.drawable.icon_hot_activity);
        }else {
            holder.imgType.setImageResource(R.drawable.icon_hot_article);
        }

        Map<Integer,Object> data= (Map<Integer,Object>) getItem(position);
        List<Object> datas= (List<Object>) data.get("data");
        ObjectAdapter adapter= (ObjectAdapter) holder.mListView.getAdapter();
        adapter.clear();
        adapter.addItem(datas,true);

        return convertView;
    }

    @Override
    public void onClick(View v) {

    }

    static class ViewHolder{
        ImageView imgType;
        MListView mListView;
    }


}
