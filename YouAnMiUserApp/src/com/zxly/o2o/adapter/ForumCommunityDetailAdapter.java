package com.zxly.o2o.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.util.LongSparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.easemob.easeui.EaseConstant;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.TopicReply;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.TopicReplyPraiseRequest;
import com.zxly.o2o.util.ImageUtil;
import com.zxly.o2o.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO 添加类的一句话简单描述。 <p/> TODO 详细描述 <p/> TODO 示例代码 <p/> <pre> </pre> @author huangbin @version YIBA-O2O 2015-1-13 @since YIBA-O2O
 */
public class ForumCommunityDetailAdapter extends BasicMyCircleAdapter {
    private LongSparseArray<Bitmap> cacheBitmaps = new LongSparseArray<Bitmap>(50);
    private List<TopicReply> MyTopicReplys = new ArrayList<TopicReply>();
    private boolean isNeedShowAll = true;
    private boolean isAddNewItem = false;
    private long topicId;
    private byte isShop;
    private Handler mHandler;

    public ForumCommunityDetailAdapter(Context context, long topicId, byte isShop, Animation animation,
                                       Handler mHandler) {
        super(context);
        this.context = context;
        this.topicId = topicId;
        this.isShop = isShop;
        this.animation = animation;
        this.mHandler = mHandler;
    }

    public int getCachBitmapsSize() {
        return cacheBitmaps.size();
    }

    public void setIsNeedShowAll(boolean isNeedShowAll) {
        this.isNeedShowAll = isNeedShowAll;
        notifyDataSetChanged();
    }

    public boolean getIsNeedShowAll() {
        return isNeedShowAll;
    }

    public Object getItem(int position) {
        if (isNeedShowAll) {
            return content.get(position);
        } else {
            return MyTopicReplys.get(position);
        }
    }

    public void setMyTopicReplys(List<TopicReply> items) {
        MyTopicReplys.clear();
        addMyTopicReplys(items, true);
    }

    public void addMyTopicReplys(List<TopicReply> items, boolean isNotify) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getReplyer().getId() == Account.user.getId()) {
                MyTopicReplys.add(items.get(i));
            }
        }
        if (isNotify) {
            notifyDataSetChanged();
        }
    }

    public void addMyTopicReply(TopicReply item, boolean isNotify) {
        MyTopicReplys.add(0, item);
        if (isNotify) {
            isAddNewItem = true;
            notifyDataSetChanged();
        }
    }

    public void addObject(TopicReply item, boolean isNotify) {
        if (isNotify) {
            isAddNewItem = true;
        }
        addItem(item, isNotify);
    }

    @Override
    public int getCount() {
        if (isNeedShowAll) {
            return super.getCount();
        } else {
            return MyTopicReplys.size();
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflateConvertView();
            viewHolder.userPhoto =
                    (NetworkImageView) convertView.findViewById(R.id.forum_community_detail_user_photo);
            viewHolder.reply =
                    (TextView) convertView.findViewById(R.id.reply_btn);
            viewHolder.replyUp =
                    (TextView) convertView.findViewById(R.id.reply_up_btn);
            viewHolder.operateText = (TextView) convertView.findViewById(R.id.operate_text_anim);
            viewHolder.content =
                    (TextView) convertView.findViewById(R.id.forum_community_detail_listitem_content);
            viewHolder.callwho =
                    (TextView) convertView.findViewById(R.id.forum_community_detail_listitem_callwho);
            viewHolder.uploadTime =
                    (TextView) convertView.findViewById(R.id.forum_community_detail_user_below_text);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.forum_community_detail_user);

            viewHolder.reply.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(viewHolder.reply, position);

                }
            });

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        final TopicReply topicReply =
                isNeedShowAll ? (TopicReply) content.get(position) : MyTopicReplys.get(position);

        convertView.setVisibility(View.VISIBLE);
        viewHolder.userName.setText(topicReply.getReplyer().getNickname());
        if (!"".equals(topicReply.getParentNickname())) {
            viewHolder.callwho
                    .setText(String.format(context.getString(R.string.topic_reply_other_text),
                            topicReply.getParentNickname(), topicReply.getParentContent()));
            viewHolder.content.setText(topicReply.getContent());
            viewHolder.callwho.setVisibility(View.VISIBLE);
        } else {
            viewHolder.callwho.setVisibility(View.GONE);
            if (topicReply.getContent().length() == 0) {
                viewHolder.content.setVisibility(View.GONE);
            } else {
                viewHolder.content.setText(topicReply.getContent());
                viewHolder.content.setVisibility(View.VISIBLE);
            }
        }
        ViewUtils.setLastTime(viewHolder.uploadTime, topicReply.getCreateTime()); /* 设置item 的icon*/
        viewHolder.uploadTime.setText(
                viewHolder.uploadTime.getText() + "   " + topicReply.getFloor() + "楼");

        //设置点赞按钮
        setReplyUpBtn(viewHolder.replyUp, position, topicReply);

        //设置点赞动画
        if (topicReply.isNeedPraiseAnim()) {
            topicReply.setNeedPraiseAnim(false);
            viewHolder.operateText.setVisibility(View.VISIBLE);
            viewHolder.operateText.startAnimation(animation);
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            }, 1000);

        } else {
            viewHolder.operateText.setVisibility(View.GONE);
        }

        // 设置头像
        ImageUtil.setImage(viewHolder.userPhoto, topicReply.getReplyer().getThumHeadUrl(), R.drawable
                .luntan_photo, null);
        viewHolder.userPhoto.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                EaseConstant
                        .startIMUserDetailInfo(topicReply.getReplyer().getId(),
                                true,
                                (Activity) context, "个人信息", 1, null);
                //                    toShowPic(position);
            }
        });
        //            setItemAnim(topicReply, convertView, position, getCount());
        return convertView;
    }

    private void setReplyUpBtn(TextView replyUp, final int position, final TopicReply topicReply) {

        int zan = R.drawable.dianzan_normal;
        if (topicReply.getIsPraise() == 1) {
            zan = R.drawable.dianzan_press;
        }
        replyUp.setCompoundDrawablesWithIntrinsicBounds(zan, 0, 0, 0);

        replyUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!topicReply
                        .isNeedPraiseAnim() && topicReply
                        .getIsPraise() == 2) {

                    TopicReplyPraiseRequest request = new TopicReplyPraiseRequest(topicId,
                            topicReply.getId(), isShop);
                    request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

                        @Override
                        public void onOK() {
                            topicReply.setNeedPraiseAnim(true);
                            topicReply
                                    .setPraiseAmount(topicReply.getPraiseAmount() + 1);
                            topicReply.setIsPraise((byte) 1);

                            notifyDataSetChanged();
                        }

                        @Override
                        public void onFail(int code) {
                            topicReply.setNeedPraiseAnim(false);
                            ViewUtils.showToast("操作失败");
                        }
                    });
                    request.start(this);
                } else {
                    ViewUtils.showToast("已点赞");
                }
            }
        });

        replyUp.setText(String.valueOf(topicReply.getPraiseAmount()));
    }

    private void toShowPic(int position) {
        if (isNeedShowAll) {
            showPic(((TopicReply) content.get(position)).getReplyer().getThumHeadUrl(),
                    ((TopicReply) content.get(position)).getCreateTime());
        } else {
            showPic((MyTopicReplys.get(position)).getReplyer().getThumHeadUrl(),
                    (MyTopicReplys.get(position)).getCreateTime());
        }
    }

    private class ViewHolder {
        TextView content;
        TextView userName;
        TextView uploadTime;
        TextView callwho;
        TextView reply;
        TextView replyUp;
        TextView operateText;
        NetworkImageView userPhoto;
    }

    @Override
    public int getLayoutId() {
        return R.layout.win_forum_community_detail_listitem;
    }


    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener l) {
        mOnItemClickListener = l;
    }

    public interface OnItemClickListener {
        void onItemClick(TextView view, int positionan);
    }
}
