package com.zxly.o2o.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.ArticlePlatformVipAct;
import com.zxly.o2o.activity.LoginAct;
import com.zxly.o2o.activity.ShopArticleAct;
import com.zxly.o2o.activity.ShopHotArticleAct;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.ShareInfo;
import com.zxly.o2o.model.ShopArticle;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.ArticlePraiseRequest;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.CallBackWithParam;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/9/20.
 */
public class ShopArticleAdapter extends ObjectAdapter {

    /**
     * 文章列表显示方式
     * TYPE_HORIZONTAL: 右侧显示一张图片
     * TYPE_VERTICAL: 底部显示三张图片
     * TYPE_COVER: 底部显示一张图片(封面图片)
     */
    private final int TYPE_HORIZONTAL = 0;
    private final int TYPE_VERTICAL = 1;
    private final int TYPE_COVER = 2;
    private final int TYPE_COUNT = 3;
    private byte articleType = 0;//1：店铺热文 2：平台专享
    private Animation animation;

    private Context context;
    private Activity activity;
    private boolean isShowType;

    public ShopArticleAdapter(Context context, Activity activity) {
        super(context);
        this.context = context;
        this.activity = activity;
    }

    /**
     * @param context
     * @param activity
     * @param isShowType 是否在左下角显示 文章分类图标（收藏页面用此构造）
     */
    public ShopArticleAdapter(Context context, Activity activity, boolean isShowType) {
        super(context);
        this.context = context;
        this.activity = activity;
        this.isShowType = isShowType;
        if (animation == null) {
            animation = AnimationUtils.loadAnimation(context, R.anim.text_operate_anim);
        }
    }

    public void setArticleType(byte articleType) {
        this.articleType = articleType;
    }

    @Override
    public int getItemViewType(int position) {
        ShopArticle shopArticle = (ShopArticle) getItem(position);
        String headUrl = shopArticle.getHead_url();
        int size = shopArticle.getImageUrls().size();
        if (!StringUtil.isNull(headUrl)) {
            return TYPE_COVER;
        } else {
            if (size == 1 || size == 2) {
                return TYPE_HORIZONTAL;
            } else {
                return TYPE_VERTICAL;
            }
        }
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        int type = getItemViewType(position);
        if (convertView == null) {
            if (type == TYPE_HORIZONTAL) {//单个图片
                convertView = inflateConvertView(R.layout.item_shop_article_horizontal);
                holder = new HorizontalViewHolder();
                ((HorizontalViewHolder) holder).imgArticle = (NetworkImageView) convertView.findViewById(R.id.img_article);
            } else if (type == TYPE_VERTICAL) {//多个图片
                convertView = inflateConvertView(R.layout.item_shop_article_vertical);
                holder = new VerticalViewHolder();
                ((VerticalViewHolder) holder).imgArticle1 = (NetworkImageView) convertView.findViewById(R.id.img_article1);
                ((VerticalViewHolder) holder).imgArticle2 = (NetworkImageView) convertView.findViewById(R.id.img_article2);
                ((VerticalViewHolder) holder).imgArticle3 = (NetworkImageView) convertView.findViewById(R.id.img_article3);
                ((VerticalViewHolder) holder).viewImg = convertView.findViewById(R.id.view_img);
            } else if (type == TYPE_COVER) {//封面图片
                convertView = inflateConvertView(R.layout.item_shop_article_cover);
                holder = new CoverViewHolder();
                ((CoverViewHolder) holder).imgCover = (NetworkImageView) convertView.findViewById(R.id.img_cover);
            } else {
                holder = new ViewHolder();
            }
            holder.viewStick = convertView.findViewById(R.id.view_stick);
            holder.imgTopStick = (TextView) convertView.findViewById(R.id.img_top_stick);
            holder.txtLabel1 = (TextView) convertView.findViewById(R.id.img_label1);
            holder.txtLabel2 = (TextView) convertView.findViewById(R.id.img_label2);
            holder.txtLabel3 = (TextView) convertView.findViewById(R.id.img_label3);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.txt_article_title);
            holder.txtContent = (TextView) convertView.findViewById(R.id.txt_article_content);
            holder.txtPraise = (TextView) convertView.findViewById(R.id.txt_praise);
            holder.txtReply = (TextView) convertView.findViewById(R.id.txt_reply);

            holder.img_articletype = (ImageView) convertView.findViewById(R.id.img_articletype);
            holder.txt_type = (TextView) convertView.findViewById(R.id.txt_type);
            holder.txtPraiseAnim = (TextView) convertView.findViewById(R.id.txt_praise_anim);
            holder.layout_type= (RelativeLayout) convertView.findViewById(R.id.layout_type);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ShopArticle shopArticle = (ShopArticle) getItem(position);
        if (isShowType&&shopArticle.getStatus() != 2) {
            holder.txtPraise.setEnabled(false);
            holder.txtReply.setEnabled(false);
        } else {
            holder.txtPraise.setEnabled(true);
            holder.txtReply.setEnabled(true);
        }
        fillData(holder, shopArticle, type, position);

        setPraiseBtn(holder.txtPraise, position, shopArticle);

        if (shopArticle.isNeedPraiseAnim()) {
            shopArticle.setNeedPraiseAnim(false);
            holder.txtPraiseAnim.setVisibility(View.VISIBLE);
            holder.txtPraiseAnim.startAnimation(animation);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            }, 800);

        } else {
            holder.txtPraiseAnim.setVisibility(View.GONE);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shopArticle.getArticleType() == 2) {//平台专享
                    ShareInfo shareInfo = new ShareInfo();
                    shareInfo.setTitle(shopArticle.getTitle());
                    shareInfo.setUrl(shopArticle.getUrl());
                    ArticlePlatformVipAct.start( "详情", shareInfo, 2, shopArticle, true,new CallBackWithParam() {
                        @Override
                        public void onCall(int param) {
                            if(1 == param){
                                shopArticle.setNeedPraiseAnim(true);
                                shopArticle.setPraiseAmount(shopArticle.getPraiseAmount() + 1);
                                shopArticle.setIsPraise((byte) 0);
                                notifyDataSetChanged();
                            }
                        }
                    });
                    return;
                }

                ShopHotArticleAct.start((Activity)context,shopArticle, 1, "详情",true,new CallBackWithParam() {
                    @Override
                    public void onCall(int param) {
                        if(1 == param){
                            shopArticle.setNeedPraiseAnim(true);
                            shopArticle.setPraiseAmount(shopArticle.getPraiseAmount() + 1);
                            shopArticle.setIsPraise((byte) 0);
                            notifyDataSetChanged();
                        }
                    }
                });
            }
        });

        return convertView;
    }

    private void setPraiseBtn(final TextView btnPraise, final int position, final ShopArticle shopArticle) {
        int drawablePraise;
        final int isPraise = shopArticle.getIsPraise();
        if (isPraise == 1) {//没有被点赞过
            drawablePraise = R.drawable.icon_praise_normal;
            btnPraise.setTextColor(context.getResources().getColor(R.color.gray_afafaf));
        } else {
            drawablePraise = R.drawable.icon_praise_press;
            btnPraise.setTextColor(context.getResources().getColor(R.color.orange_ff5f19));
        }
        if(isShowType&&shopArticle.getStatus() != 2){
            drawablePraise = R.drawable.icon_dianzan_no;
            btnPraise.setTextColor(context.getResources().getColor(R.color.gray_dcdcdc));
        }
        btnPraise.setCompoundDrawablesWithIntrinsicBounds(drawablePraise, 0, 0, 0);

        long praiseAmount = shopArticle.getPraiseAmount();
        if (praiseAmount != 0) {
            ViewUtils.setText(btnPraise, praiseAmount);
        } else {
            ViewUtils.setText(btnPraise, "");
        }

        btnPraise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Account.user == null) {
                    LoginAct.start(activity, new CallBack() {
                        @Override
                        public void onCall() {
                            notifyDataSetChanged();
                        }
                    });
                } else {
                    if (!shopArticle.isNeedPraiseAnim() && isPraise == 1) {
                        ArticlePraiseRequest articlePraiseRequest = new ArticlePraiseRequest(shopArticle.getId(), articleType);
                        articlePraiseRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                            @Override
                            public void onOK() {
                                shopArticle.setNeedPraiseAnim(true);
                                shopArticle.setPraiseAmount(shopArticle.getPraiseAmount() + 1);
                                shopArticle.setIsPraise((byte) 0);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onFail(int code) {
                                shopArticle.setNeedPraiseAnim(false);
                            }
                        });
                        articlePraiseRequest.start(context);
                    } else {
                        ViewUtils.showToast("介么喜欢呀，已经赞过了哦…");
                    }
                }
            }
        });
    }

    void fillData(ViewHolder holder, final ShopArticle shopArticle, int type, int position) {
        ViewUtils.setText(holder.txtTitle, shopArticle.getTitle());
        if (shopArticle.getContent().length() > 200) {
            ViewUtils.setText(holder.txtContent, shopArticle.getContent().substring(0, 200));
        } else {
            ViewUtils.setText(holder.txtContent, shopArticle.getContent());
        }

        if (isShowType) {
            if (shopArticle.getArticleType() == 1) {
                ViewUtils.setVisible(holder.img_articletype);
                ViewUtils.setVisible(holder.txt_type);
                holder.img_articletype.setBackgroundResource(R.drawable.icon_dprw);
                holder.txt_type.setText("店铺热文");
            } else if (shopArticle.getArticleType() == 2) {
                ViewUtils.setVisible(holder.img_articletype);
                ViewUtils.setVisible(holder.txt_type);
                holder.img_articletype.setBackgroundResource(R.drawable.icon_ptzx);
                holder.txt_type.setText("平台专享");
                ViewUtils.setGone(holder.txtReply);
            }
            holder.layout_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShopArticleAct.start((Activity) context, (byte) shopArticle.getArticleType());
                }
            });
        } else {
            ViewUtils.setGone(holder.img_articletype);
            ViewUtils.setGone(holder.txt_type);
        }


        if (articleType == 1 || shopArticle.getArticleType() == 1) {//店铺热文
            List<String> labels = shopArticle.getLabels();
            int labelLength = labels.size();
            int isTop = shopArticle.getIsTop();
            if (isTop == 0 && labelLength == 0) {//没有置顶没有标签
                ViewUtils.setGone(holder.viewStick);
                ViewUtils.setGone(holder.imgTopStick);
            } else {
                ViewUtils.setVisible(holder.viewStick);
                ViewUtils.setVisible(holder.imgTopStick);
                if (isTop == 0) {
                    ViewUtils.setGone(holder.imgTopStick);
                } else {
                    ViewUtils.setVisible(holder.imgTopStick);
                }
                if (labelLength == 0) {
                    ViewUtils.setGone(holder.txtLabel1);
                    ViewUtils.setGone(holder.txtLabel2);
                    ViewUtils.setGone(holder.txtLabel3);
                } else if (labelLength == 1) {
                    ViewUtils.setVisible(holder.txtLabel1);
                    ViewUtils.setText(holder.txtLabel1, labels.get(0));
                    ViewUtils.setGone(holder.txtLabel2);
                    ViewUtils.setGone(holder.txtLabel3);
                } else if (labelLength == 2) {
                    ViewUtils.setVisible(holder.txtLabel1);
                    ViewUtils.setText(holder.txtLabel1, labels.get(0));
                    ViewUtils.setVisible(holder.txtLabel2);
                    ViewUtils.setText(holder.txtLabel2, labels.get(1));
                    ViewUtils.setGone(holder.txtLabel3);
                } else {
                    ViewUtils.setVisible(holder.txtLabel1);
                    ViewUtils.setText(holder.txtLabel1, labels.get(0));
                    ViewUtils.setVisible(holder.txtLabel2);
                    ViewUtils.setText(holder.txtLabel2, labels.get(1));
                    ViewUtils.setVisible(holder.txtLabel3);
                    ViewUtils.setText(holder.txtLabel3, labels.get(2));
                }
            }
            ViewUtils.setVisible(holder.txtReply);

        } else if (articleType == 2 || shopArticle.getArticleType() == 2) {//平台专享
            ViewUtils.setGone(holder.viewStick);
            ViewUtils.setGone(holder.txtReply);
        }

        List<String> imageUrls = shopArticle.getImageUrls();
        if (type == TYPE_COVER) {
            CoverViewHolder coverViewHolder = (CoverViewHolder) holder;
            if(shopArticle.getHead_url().contains("http://")){
                ViewUtils.setVisible(coverViewHolder.imgCover);
                coverViewHolder.imgCover.setImageUrl(shopArticle.getHead_url(), AppController.imageLoader);
            } else {
                ViewUtils.setGone(coverViewHolder.imgCover);
            }
        } else if (type == TYPE_HORIZONTAL) {
            HorizontalViewHolder horizontalViewHolder = (HorizontalViewHolder) holder;
            horizontalViewHolder.imgArticle.setImageUrl(imageUrls.get(0), AppController.imageLoader);
        } else if (type == TYPE_VERTICAL) {
            VerticalViewHolder verticalViewHolder = (VerticalViewHolder) holder;
            if (imageUrls.size() == 0) {
                ViewUtils.setGone(verticalViewHolder.viewImg);
            } else {
                ViewUtils.setVisible(verticalViewHolder.viewImg);
                verticalViewHolder.imgArticle1.setImageUrl(imageUrls.get(0), AppController.imageLoader);
                verticalViewHolder.imgArticle2.setImageUrl(imageUrls.get(1), AppController.imageLoader);
                verticalViewHolder.imgArticle3.setImageUrl(imageUrls.get(2), AppController.imageLoader);
            }
        }

        long replyAmount = shopArticle.getReplyAmount();
        if (replyAmount != 0) {
            ViewUtils.setText(holder.txtReply, replyAmount);
        } else {
            ViewUtils.setText(holder.txtReply, "");
        }

    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    class ViewHolder {
        View viewStick;
        TextView imgTopStick;
        TextView txtLabel1, txtLabel2, txtLabel3;
        TextView txtTitle, txtContent, txtReply, txtPraise, txt_type;
        TextView txtPraiseAnim;
        ImageView img_articletype,iv_dainzan;
        RelativeLayout layout_type;
    }

    class HorizontalViewHolder extends ViewHolder {
        NetworkImageView imgArticle;
    }

    class VerticalViewHolder extends ViewHolder {
        NetworkImageView imgArticle1, imgArticle2, imgArticle3;
        View viewImg;
    }

    class CoverViewHolder extends ViewHolder {
        NetworkImageView imgCover;
    }

}