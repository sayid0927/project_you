package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.easemob.easeui.EaseConstant;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.User;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.TimeUtil;

/**
 * Created by kenwu on 2015/12/8.
 */
public class LikeAndCollectHistoyAdapter extends  ObjectAdapter implements OnClickListener {
    public LikeAndCollectHistoyAdapter(Context _context) {
        super(_context);
    }


    @Override
    public int getLayoutId() {
        return R.layout.item_like_and_collect;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder =null;
        if(convertView==null){
            convertView=inflateConvertView();
            holder=new ViewHolder();
            holder.imgHead = (NetworkImageView) convertView.findViewById(R.id.img_head);
            holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
            holder.txtAction = (TextView) convertView.findViewById(R.id.txt_action);
            holder.txtDesc = (TextView) convertView.findViewById(R.id.txt_desc);
            holder.txtTime = (TextView) convertView.findViewById(R.id.txt_time);
            holder.botLine=convertView.findViewById(R.id.view_bot_line);
            convertView.setTag(holder);
            convertView.setOnClickListener(this);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

        fillData(holder, (User) getItem(position));

        return convertView;
    }


    private void fillData(ViewHolder holder,User user){
        holder.user=user;
        holder.imgHead.setDefaultImageResId(R.drawable.head_icon_100);
        holder.imgHead.setImageUrl(user.getThumHeadUrl(), AppController.imageLoader);
        holder.txtName.setText(user.getName());

        if(DataUtil.stringIsNull(user.getSignature())){
            holder.txtDesc.setText("这个人很懒，还没有个性签名");
        }else {
            holder.txtDesc.setText(user.getSignature());
        }

        if(user.getType()== User.ACTION_TYPE_LIKE){
            holder.txtAction.setText("喜欢了");
        }else if(user.getType()== User.ACTION_TYPE_SHARE){
            holder.txtAction.setText("分享了");
        }else if(user.getType()== User.ACTION_TYPE_PRISE){
            holder.txtAction.setText("赞了一个");
        }else if ((user.getType()== User.ACTION_TYPE_COLLECT)){
            holder.txtAction.setText("收藏了");
        }else {
            holder.txtAction.setText("");
        }

        holder.txtTime.setText(TimeUtil.getBeforeTime(Config.serverTime()-user.getOprateTime()));
      //  holder.txtTime.setText((Config.serverTime()-user.getOprateTime())+"");
    }

    @Override
    public void onClick(View v) {
        User user=((ViewHolder)v.getTag()).user;
        EaseConstant.startIMUserDetailInfo(user.getId(),true, AppController.getInstance().getTopAct(),
                "个人信息",1,null);
    }


    static class ViewHolder{
        NetworkImageView imgHead;
        TextView txtName;
        TextView txtAction;
        TextView txtDesc;
        TextView txtTime;
        View botLine;
        User user;
    }
}
