package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.activity.SalesmanRankingAct;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.model.RankingInfo;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ViewUtils;

/**
 * Created by kenwu on 2015/12/15.
 */
public class SalesmanRankingAdapter extends ObjectAdapter {


    private int type;

    public SalesmanRankingAdapter(Context _context) {
        super(_context);
    }


    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_salesman_ranking;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder =null;
        if(convertView==null){
            convertView=inflateConvertView();
            holder=new ViewHolder();
            holder.txtRanking= (TextView) convertView.findViewById(R.id.txt_ranking);
            holder.imgHead = (NetworkImageView) convertView.findViewById(R.id.img_head);
            holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
            holder.txtPhoneNo = (TextView) convertView.findViewById(R.id.txt_phoneNo);
            holder.txtPromotionType = (TextView) convertView.findViewById(R.id.txt_promotion_type);
            holder.txtCount= (TextView) convertView.findViewById(R.id.txt_count);
            holder.txtUserType= (TextView) convertView.findViewById(R.id.txt_user_type);
            holder.botLine=convertView.findViewById(R.id.view_bot_line);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
//
        fillData((RankingInfo) getItem(position),holder,position);
        if(position==(getCount()-1)){
            ViewUtils.setGone(holder.botLine);
        }else {
            ViewUtils.setVisible(holder.botLine);
        }

        return convertView;
    }


    private void fillData(RankingInfo info,ViewHolder holder,int position){

        if(position==0){
            holder.txtRanking.setText("");
holder.txtRanking.setBackgroundResource(R.drawable.one);
        }else if (position==1){
            holder.txtRanking.setText("");
            holder.txtRanking.setBackgroundResource(R.drawable.two);
        }else if (position==2){
            holder.txtRanking.setText("");
            holder.txtRanking.setBackgroundResource(R.drawable.three);
        }else {
            holder.txtRanking.setText(""+(position+1));
            holder.txtRanking.setBackgroundResource(R.color.transparent);
        }

        holder.imgHead.setDefaultImageResId(R.drawable.head_icon_90x90);
        holder.imgHead.setImageUrl(info.getThumHeadUrl(), AppController.imageLoader);
        ViewUtils.setText(holder.txtName, info.getName());
        ViewUtils.setText(holder.txtPhoneNo,info.getUserName());
        holder.txtUserType.setText(info.getRoleName());

        if(type== SalesmanRankingAct.PROMOTION_TYPE_ARTICLE){
            holder.txtCount.setText(info.getAmount()+"");
            holder.txtPromotionType.setText("浏览量");
        }else if(type== SalesmanRankingAct.PROMOTION_TYPE_PRODUCT){
            holder.txtCount.setText(info.getAmount()+"");
            holder.txtPromotionType.setText("浏览量");

        }else {
            holder.txtCount.setText(info.getAmount()+"人");
            holder.txtPromotionType.setText("推广会员");
        }
 //       ViewUtils.setText(holder.txtRanking,info.getAmount());
    }


    static class ViewHolder{
        TextView txtRanking;
        NetworkImageView imgHead;
        TextView txtName;
        TextView txtPhoneNo;
        TextView txtPromotionType;
        TextView txtCount;
        TextView txtUserType;
        View botLine;
    }


}
