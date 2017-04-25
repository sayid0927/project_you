package com.zxly.o2o.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easemob.easeui.EaseConstant;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.LoginAct;
import com.zxly.o2o.model.ArticleReply;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.PraiseReplyRequest;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.CircleImageView;

/**
 * Created by hejun on 2016/10/25.
 */
public class HotArticleDetailAdapter extends BasicMyCircleAdapter{

    boolean isNoData;
    private Animation animation;
    private Handler mHandler;
    private InputMethodManager imm;
    private boolean isShowHeadImage=true;

    public HotArticleDetailAdapter(Context context, Animation animation, Handler mHandler) {
        super(context);
        this.animation = animation;
        this.mHandler = mHandler;
    }

    public void setIsshowHeadImage(boolean isShowHeadImage){
        this.isShowHeadImage=isShowHeadImage;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflateConvertView();
            viewHolder.user = (TextView) convertView.findViewById(R.id.article_detail_user);
            viewHolder.userPhoto = (CircleImageView) convertView
                    .findViewById(R.id.article_detail_user_photo);
            viewHolder.upBtn = (TextView) convertView.findViewById(R.id.article_detail_up_btn);
            viewHolder.operateText = (TextView) convertView.findViewById(R.id.operate_text_anim);
            viewHolder.replyTime = (TextView) convertView
                    .findViewById(R.id.article_detail_reply_time);
            viewHolder.content = (TextView) convertView
                    .findViewById(R.id.article_detail_listitem_content);
            viewHolder.img_dianzan = (ImageView) convertView.findViewById(R.id.img_dianzan_list);
            viewHolder.article_illegal_comments= (LinearLayout) convertView.findViewById(R.id.article_illegal_comments);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (!isNoData) {
            convertView.setVisibility(View.VISIBLE);
            final ArticleReply articleReply = (ArticleReply) content.get(position);
            viewHolder.user.setText(articleReply.getNickname());


            ViewUtils.setLastTime(viewHolder.replyTime, articleReply.getUpdateTime());
            viewHolder.replyTime.setText(EaseConstant.getShortTime(articleReply.getUpdateTime()));
            if(articleReply.getStatus()==1){
                viewHolder.content.setVisibility(View.VISIBLE);
                viewHolder.content.setText(articleReply.getContent());
                viewHolder.article_illegal_comments.setVisibility(View.GONE);
            }else{
                viewHolder.article_illegal_comments.setVisibility(View.VISIBLE);
                viewHolder.content.setVisibility(View.GONE);
            }

            if ( articleReply.getHeadUrl() == null) {
                viewHolder.userPhoto.setImageResource(R.drawable.luntan_photo);
            } else {
                viewHolder.userPhoto.setImageUrl(articleReply.getHeadUrl(), R.drawable.luntan_photo);
            }

            viewHolder.userPhoto.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(isShowHeadImage){
                        showPic(((ArticleReply) content.get(position)).getHeadUrl(),
                                10000);
                    }
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
            if(articleReply.getPraiseAmount()==0){
                viewHolder.upBtn.setText(articleReply.getPraiseAmount() + "");
                viewHolder.upBtn.setVisibility(View.INVISIBLE);
            }else{
                viewHolder.upBtn.setText(articleReply.getPraiseAmount() + "");
            }
            int zan = R.drawable.icon_zan_normal_new;
            if (articleReply.getIsPraise() == 0) {
                zan = R.drawable.icon_zan_press_new;
                viewHolder.upBtn.setTextColor(context.getResources().getColor(R.color.color_ff5f19));
            }
            viewHolder.img_dianzan.setImageResource(zan);
            viewHolder.img_dianzan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Account.user==null){
                        ViewUtils.showToast("要先登录哟,亲");
                        ViewUtils.startActivity(new Intent(context, LoginAct.class), (Activity) context);
                        return;
                    }
                    if (!((ArticleReply) content.get(position))
                            .getIsNeedAnim() && ((ArticleReply) content.get(position))
                            .getIsPraiseAmount() == 1) {

                        ((ArticleReply) content.get(position)).setIsNeedAnim(true);
                        final ArticleReply articleReply = (ArticleReply) content.get(position);
                        PraiseReplyRequest request = new PraiseReplyRequest(
                                articleReply.getId());
                        request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

                            @Override
                            public void onOK() {
                                ((ArticleReply) content.get(position))
                                        .setPraiseAmount(articleReply.getPraiseAmount() + 1);
                                ((ArticleReply) content.get(position)).setIsPraiseAmount((byte) 0);

                                notifyDataSetChanged();
                            }

                            @Override
                            public void onFail(int code) {
                                ((ArticleReply) content.get(position)).setIsNeedAnim(false);
                            }
                        });
                        request.start(this);
                    } else {
                        ViewUtils.showToast("介么喜欢呀，已经赞过了哦…");
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
        ImageView img_dianzan;
        CircleImageView userPhoto;
        LinearLayout article_illegal_comments;
    }

    @Override
    public int getLayoutId() {
        return R.layout.win_article_detail_listitem_new;
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
