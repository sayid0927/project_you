package com.zxly.o2o.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.model.ArticleReply;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.ArticleReplyPraiseRequest;
import com.zxly.o2o.request.BaseRequest.ResponseStateListener;
import com.zxly.o2o.util.ImageUtil;
import com.zxly.o2o.util.ViewUtils;

/**
 * TODO 添加类的一句话简单描述。
 * <p>
 * TODO 详细描述
 * <p>
 * TODO 示例代码
 * <p>
 * <pre>
 * </pre>
 *
 * @author huangbin
 * @version YIBA-O2O 2015-1-13
 * @since YIBA-O2O
 */
public class ArticleDetailAdapter extends BasicMyCircleAdapter {

    boolean isNoData;
    private Animation animation;
    private Handler mHandler;

    public ArticleDetailAdapter(Context context, Animation animation, Handler mHandler) {
        super(context);
        this.animation = animation;
        this.mHandler = mHandler;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflateConvertView();
            viewHolder.user = (TextView) convertView.findViewById(R.id.article_detail_user);
            viewHolder.userPhoto = (NetworkImageView) convertView
                    .findViewById(R.id.article_detail_user_photo);
            viewHolder.upBtn = (TextView) convertView.findViewById(R.id.article_detail_up_btn);
            viewHolder.operateText = (TextView) convertView.findViewById(R.id.operate_text_anim);
            viewHolder.replyTime = (TextView) convertView
                    .findViewById(R.id.article_detail_reply_time);
            viewHolder.content = (TextView) convertView
                    .findViewById(R.id.article_detail_listitem_content);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (!isNoData) {
            convertView.setVisibility(View.VISIBLE);
            final ArticleReply articleReply = (ArticleReply) content.get(position);
            viewHolder.user.setText(articleReply.getReplyer().getNickname());

            viewHolder.content.setText(articleReply.getContent());

            ViewUtils.setLastTime(viewHolder.replyTime, articleReply.getCreateTime());

            ImageUtil.setImage(viewHolder.userPhoto, articleReply.getReplyer().getThumHeadUrl(),
                    R.drawable.luntan_photo, null);

            viewHolder.userPhoto.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    showPic(((ArticleReply) content.get(position)).getReplyer().getThumHeadUrl(),
                            ((ArticleReply) content.get(position)).getCreateTime());
                }
            });

            if (articleReply.getIsNeedAnim()) {
                ((ArticleReply) content.get(position)).setIsNeedAnim(false);
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
            setItemAnim(articleReply, convertView, position);
            viewHolder.upBtn.setVisibility(View.VISIBLE);
            viewHolder.upBtn.setText(articleReply.getPraiseAmount() + "");
            int zan = R.drawable.dianzan_normal;
            if (articleReply.getIsPraiseAmount() == 1) {
                zan = R.drawable.dianzan_press;
            }
            viewHolder.upBtn.setCompoundDrawablesWithIntrinsicBounds(zan, 0, 0, 0);

            viewHolder.upBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!((ArticleReply) content.get(position))
                            .getIsNeedAnim() && ((ArticleReply) content.get(position))
                            .getIsPraiseAmount() == 0) {

                        ((ArticleReply) content.get(position)).setIsNeedAnim(true);
                        final ArticleReply articleReply = (ArticleReply) content.get(position);
                        ArticleReplyPraiseRequest request = new ArticleReplyPraiseRequest(
                                articleReply.getId());
                        request.setOnResponseStateListener(new ResponseStateListener() {

                            @Override
                            public void onOK() {
                                ((ArticleReply) content.get(position))
                                        .setPraiseAmount(articleReply.getPraiseAmount() + 1);
                                ((ArticleReply) content.get(position)).setIsPraiseAmount((byte) 1);

                                notifyDataSetChanged();
                            }

                            @Override
                            public void onFail(int code) {
                                ((ArticleReply) content.get(position)).setIsNeedAnim(false);
                                ViewUtils.showToast("操作失败");
                            }
                        });
                        request.start(this);
                    } else {
                        ViewUtils.showToast("已点赞");
                    }
                }
            });

        } else {
            convertView.setVisibility(View.GONE);
        }
        return convertView;
    }

    private class ViewHolder {

        TextView user;
        TextView content;
        TextView upBtn;
        TextView operateText;
        TextView replyTime;
        NetworkImageView userPhoto;
    }

    @Override
    public int getLayoutId() {
        return R.layout.win_article_detail_listitem;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (content.size() == 0) {
            isNoData = true;
            return 1;
        } else {
            isNoData = false;
        }
        return content.size();
    }

}
