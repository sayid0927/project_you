package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.TopicReply;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.ImageUtil;
import com.zxly.o2o.util.ViewUtils;

/**
 *     @author huangbin  @version 创建时间：2015-2-4 下午4:11:31    类说明: 
 */
public class MyTopicReplysAdapter extends BasicMyCircleAdapter {

    public MyTopicReplysAdapter(Context _context) {
        super(_context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflateConvertView();
            viewHolder.icon = (NetworkImageView) convertView.findViewById(R.id.my_topics_reply_item_content_icon);
            viewHolder.photo = (NetworkImageView) convertView.findViewById(R.id.my_topics_reply_item_user_photo);
            viewHolder.reply_icon = (NetworkImageView) convertView.findViewById(R.id.my_topics_reply_icon);
            viewHolder.name = (TextView) convertView.findViewById(R.id.my_topics_reply_item_user);
            viewHolder.lastTime = (TextView) convertView.findViewById(R.id.my_topics_reply_item_user_below_text);
            viewHolder.content = (TextView) convertView.findViewById(R.id.my_topics_reply_item_content);
            viewHolder.from = (TextView) convertView.findViewById(R.id.my_topics_reply_from_item);
            viewHolder.up = (TextView) convertView.findViewById(R.id.my_topics_reply_item_up_tip);
            viewHolder.reply = (TextView) convertView.findViewById(R.id.my_topics_reply_item_reply_tip);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TopicReply topicReply = (TopicReply) content.get(position);
        viewHolder.name.setText(Account.user.getNickName());
        ViewUtils.setLastTime(viewHolder.lastTime, topicReply.getCreateTime());
//        ImageUtil.setImage(viewHolder.icon, topicReply.getTopicVO().getThum_image_url(), R.drawable.ic_launcher, viewHolder.icon);
//        ImageUtil.setImage(viewHolder.reply_icon, topicReply.getThum_image_url(), R.drawable.ic_launcher, viewHolder.reply_icon);
//
//        viewHolder.reply_icon.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                showPic(((TopicReply) content.get(position)).getOrigin_image_ur(), ((TopicReply) content.get(position)).getCreateTime());
//            }
//
//        });
        viewHolder.icon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//                showPic(((TopicReply) content.get(position)).getTopicVO().getOrigin_image_url(), ((TopicReply) content.get(position)).getCreateTime());
            }

        });

        ImageUtil.setImage(viewHolder.photo, Account.user.getThumHeadUrl(), R.drawable.luntan_photo, null);

        viewHolder.photo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//                showPic(Account.user.getThumHeadUrl(), ((TopicReply) content.get(position)).getCreateTime());
            }

        });


        if (topicReply.getContent().length() == 0) {

            viewHolder.content.setVisibility(View.GONE);
        } else {
            viewHolder.content.setText(topicReply.getContent());
            viewHolder.content.setVisibility(View.VISIBLE);
        }

        viewHolder.up.setText(topicReply.getTopicVO().getPraiseAmout() + "");
        viewHolder.reply.setText(topicReply.getTopicVO().getReplyAmout() + "");
        viewHolder.from.setText("原帖 : " + topicReply.getTopicVO().getContent());

        return convertView;
    }

    class ViewHolder {
        NetworkImageView icon;
        NetworkImageView reply_icon;
        NetworkImageView photo;
        TextView name;
        TextView lastTime;
        TextView content;
        TextView from;
        TextView up;
        TextView reply;
    }

    @Override
    public int getLayoutId() {
        return R.layout.my_reply_topics_item;
    }

}
