/*
 * 文件名：ForumCommunityAdapter.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： ForumCommunityAdapter.java
 * 修改人：Administrator
 * 修改时间：2015-1-12
 * 修改内容：新增
 */
package com.zxly.o2o.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.easemob.easeui.widget.VolleyImageView;
import com.zxly.o2o.activity.IMUserDetailInfoActivity;
import com.zxly.o2o.activity.MyCircleThirdAct;
import com.zxly.o2o.model.ShopTopic;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.MyCircleRequest;
import com.zxly.o2o.request.TopicUpRequest;
import com.zxly.o2o.util.ImageUtil;
import com.zxly.o2o.util.ViewUtils;


/**
 * TODO 论坛列表适配器。
 * <p/>
 * TODO 详细描述
 * <p/>
 * TODO 示例代码
 * <p/>
 * <pre>
 * </pre>
 *
 * @author huangbin
 * @version YIBA-O2O 2015-1-12
 * @since YIBA-O2O
 */
public class ForumCommunityAdapter extends BasicMyCircleAdapter {
    private boolean hasOneTop;
    private byte isShop;
    private String title;
    private String circleType;

    public ForumCommunityAdapter(Context context, byte isShop, String title) {
        super(context);
        this.isShop = isShop;
        this.title = title;
        if (animation == null) {
            animation = AnimationUtils.loadAnimation(context, R.anim.text_operate_anim);
        }
    }

    public void setCircleType(String circleType){
        this.circleType = circleType;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflateConvertView();
            viewHolder.icon = (VolleyImageView) convertView
                    .findViewById(R.id.forum_community_list_item_icon);
            viewHolder.icon2 = (VolleyImageView) convertView
                    .findViewById(R.id.forum_community_list_item_icon2);
            viewHolder.icon3 = (VolleyImageView) convertView
                    .findViewById(R.id.forum_community_list_item_icon3);
            viewHolder.rePlyTip = (TextView) convertView
                    .findViewById(R.id.forum_community_list_item_reply_tip);
            viewHolder.upTip = (TextView) convertView
                    .findViewById(R.id.forum_community_list_item_up_tip);
            viewHolder.topTip = (TextView) convertView
                    .findViewById(R.id.topmost_tip);
            viewHolder.role = (TextView) convertView
                    .findViewById(R.id.forum_community_list_item_role);
            viewHolder.rolePhoto = (NetworkImageView) convertView
                    .findViewById(R.id.forum_community_list_item_role_photo);
            viewHolder.title = (TextView) convertView
                    .findViewById(R.id.forum_community_list_item_title);
            viewHolder.uploadTime = (TextView) convertView
                    .findViewById(R.id.forum_community_list_item_upload_time);
            viewHolder.iconLayout = (LinearLayout) convertView.findViewById(R.id.icon_layout);
            viewHolder.operateText = (TextView) convertView.findViewById(R.id.operate_text_anim);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final ShopTopic sTopic = (ShopTopic) content.get(position);

        viewHolder.rePlyTip.setText(sTopic.getReplyAmout() + "");


        setUpBtn(viewHolder.upTip, position, sTopic);

        //设置点赞动画
        if (sTopic.isNeedPraiseAnim()) {
            sTopic.setNeedPraiseAnim(false);
            viewHolder.operateText.setVisibility(View.VISIBLE);
            viewHolder.operateText.startAnimation(animation);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            }, 800);

        } else {
            viewHolder.operateText.setVisibility(View.GONE);
        }

        viewHolder.rePlyTip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCircleRequest.shopTopic = sTopic;

                if("brandCircle".equals(circleType)){
                    MyCircleThirdAct.start((Activity) context, (byte)1, MyCircleRequest.shopTopic, title, false);
                } else {
                    MyCircleThirdAct.start((Activity) context, MyCircleRequest.shopTopic.getIsShopTopic(), MyCircleRequest.shopTopic, title, false);
                }
            }
        });

        viewHolder.upTip.setText(sTopic.getPraiseAmout() + "");

        setIcon(viewHolder, sTopic);


        //        setIconClickListener(position, viewHolder);

        // 加载时间
        ViewUtils.setLastTime(viewHolder.uploadTime, sTopic.getCreateTime());

        // 加载图片
        ImageUtil.setImage(viewHolder.rolePhoto, sTopic.getPublishUser().getThumHeadUrl(),
                R.drawable.ease_default_avatar, null);

        if (context instanceof IMUserDetailInfoActivity) {
            viewHolder.rolePhoto.setVisibility(View.GONE);
        }

        viewHolder.rolePhoto.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //                showPic(((ShopTopic) content.get(position)).getUserVO().getThumHeadUrl(),
                //                        ((ShopTopic) content.get(position)).getCreateTime());
            }
        });
        viewHolder.role
                .setText(sTopic.getPublishMan() == 1 ? "匿名" : sTopic.getPublishMan() == 2 ? "柚安米" : sTopic
                        .getPublishUser().getNickname());

        int type = sTopic.getPublishUser().getType();
        //需求只能显示两个置顶，但只有一个的时候要显示一个，所以才有一下判断语句
        if (position == 0 && sTopic.getIs_top() == 1&&(parent.getChildCount()-sTopic.getIsShopTopic()<2)) {
            viewHolder.topTip.setVisibility(View.VISIBLE);

            if ((type == 1 || type == 2 || sTopic.getIsShopTopic() == 0) && !"brandCircle".equals(circleType)) {
                viewHolder.title.setText("          【本店交流】" + sTopic.getContent());
            } else {
                viewHolder.title.setText("          " + sTopic.getContent());
            }
            hasOneTop = true;
        } else {
            if ((type == 1 || type == 2 || sTopic.getIsShopTopic() == 0) && !"brandCircle".equals(circleType)) {
                viewHolder.title.setText("【本店交流】" + sTopic.getContent());
            } else {
                viewHolder.title.setText(sTopic.getContent());
            }
            viewHolder.topTip.setVisibility(View.GONE);
        }

        setItemAnim(sTopic, convertView, hasOneTop ? position - 1 : position);

        return convertView;
    }

    private void setIconClickListener(final int position, ViewHolder viewHolder) {
        viewHolder.icon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showPic(((ShopTopic) content.get(position))
                        .getOriginImageList(), 0);
            }

        });

        viewHolder.icon2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showPic(((ShopTopic) content.get(position))
                        .getOriginImageList(), 1);
            }

        });
        viewHolder.icon3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showPic(((ShopTopic) content.get(position))
                        .getOriginImageList(), 2);
            }

        });
    }

    private void setIcon(ViewHolder viewHolder, ShopTopic sTopic) {
        if (sTopic.getThumImageList().size() > 2) {
            ImageUtil.setImage(viewHolder.icon3, sTopic.getThumImageList().get(2), R.drawable.ic_launcher,
                    viewHolder.iconLayout);
            ImageUtil.setImage(viewHolder.icon2, sTopic.getThumImageList().get(1), R.drawable.ic_launcher,
                    viewHolder.iconLayout);
            ImageUtil.setImage(viewHolder.icon, sTopic.getThumImageList().get(0), R.drawable.ic_launcher,
                    viewHolder.iconLayout);
            viewHolder.iconLayout.setVisibility(View.VISIBLE);
        } else if (sTopic.getThumImageList().size() > 1) {
            ImageUtil.setImage(viewHolder.icon2, sTopic.getThumImageList().get(1), R.drawable.ic_launcher,
                    viewHolder.iconLayout);
            ImageUtil.setImage(viewHolder.icon, sTopic.getThumImageList().get(0), R.drawable.ic_launcher,
                    viewHolder.iconLayout);
            viewHolder.iconLayout.setVisibility(View.VISIBLE);
            ViewUtils.setInvisible(viewHolder.icon3);
        } else if (sTopic.getThumImageList().size() > 0) {
            ImageUtil.setImage(viewHolder.icon, sTopic.getThumImageList().get(0), R.drawable.ic_launcher,
                    viewHolder.iconLayout);
            viewHolder.iconLayout.setVisibility(View.VISIBLE);
            ViewUtils.setInvisible(viewHolder.icon2);
            ViewUtils.setInvisible(viewHolder.icon3);
        } else {
            viewHolder.iconLayout.setVisibility(View.GONE);
        }
    }

    private void setIconImage(VolleyImageView view, Bitmap mBitmap) {
        view.setImageUrl(null, null);
        view.setLocalImageBitmap(mBitmap);
    }


    private void setUpBtn(final TextView replyUp, final int position, final ShopTopic shopTopic) {
        int zan;
        if (shopTopic.getIsPraise() == 1) {
            zan = R.drawable.dianzan_normal;
            replyUp.setTextColor(context.getResources().getColor(R.color.orange_ff5f19));
        } else {
            zan = R.drawable.icon_zan;
            replyUp.setTextColor(context.getResources().getColor(R.color.gray_afafaf));
        }
        replyUp.setCompoundDrawablesWithIntrinsicBounds(zan, 0, 0, 0);

        replyUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!shopTopic
                        .isNeedPraiseAnim() && shopTopic
                        .getIsPraise() == 2) {

                    TopicUpRequest request = new TopicUpRequest(shopTopic.getIsShopTopic(), shopTopic.getId());
                    request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

                        @Override
                        public void onOK() {
                            shopTopic.setNeedPraiseAnim(true);
                            shopTopic
                                    .setPraiseAmout(shopTopic.getPraiseAmout() + 1);
                            shopTopic.setIsPraise((byte) 1);

                            notifyDataSetChanged();
                        }

                        @Override
                        public void onFail(int code) {
                            shopTopic.setNeedPraiseAnim(false);
                            ViewUtils.showToast("操作失败");
                        }
                    });
                    request.start(this);
                } else {
                    ViewUtils.showToast("已点赞");
                }
            }
        });

        replyUp.setText(String.valueOf(shopTopic.getPraiseAmout()));

    }


    private class ViewHolder {
        VolleyImageView icon;
        LinearLayout iconLayout;
        VolleyImageView icon2;
        VolleyImageView icon3;
        TextView rePlyTip;
        TextView upTip;
        TextView role;
        TextView topTip;
        TextView title;
        NetworkImageView rolePhoto;
        TextView operateText;
        TextView uploadTime;
    }

    @Override
    public int getLayoutId() {
        return R.layout.win_forum_community_list_item;
    }

}
