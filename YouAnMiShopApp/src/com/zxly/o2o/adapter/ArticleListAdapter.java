package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.dialog.ShareDialog;
import com.zxly.o2o.model.MyCirCleObject;
import com.zxly.o2o.model.ShopArticle;
import com.zxly.o2o.request.PromoteCallbackConfirmRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.ImageUtil;
import com.zxly.o2o.util.ViewUtils;

public class ArticleListAdapter extends ObjectAdapter {
    protected ShareDialog shareDialog;
    private Context mContext;

    public ArticleListAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        final ShopArticle sArticle = (ShopArticle) content.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflateConvertView();
            viewHolder.icon = (NetworkImageView) convertView
                    .findViewById(R.id.win_mycircle_list_item_icon);
            viewHolder.rePlyTip = (TextView) convertView
                    .findViewById(R.id.win_mycircle_list_item_reply_tip);
            viewHolder.title = (TextView) convertView
                    .findViewById(R.id.win_mycircle_list_item_title);
            viewHolder.uploadTime = (TextView) convertView
                    .findViewById(R.id.win_mycircle_list_item_upload_time);
            viewHolder.btn_public = (TextView) convertView.findViewById(R.id.btn_public);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (!"".equals(sArticle.getShareUrl())) {
            viewHolder.rePlyTip.setVisibility(View.VISIBLE);
            viewHolder.btn_public.setVisibility(View.VISIBLE);
            viewHolder.btn_public.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    new PromoteCallbackConfirmRequest(sArticle.getId(), 2,sArticle.getTitle()).start();
                    shareToOthers(sArticle);
//                    ViewUtils.share(context,sArticle.getTitle() +"  "+ sArticle.getHead_url());
                }
            });
        }

        viewHolder.title.setText(sArticle.getTitle());

        viewHolder.rePlyTip
                .setText(new StringBuffer("").append(sArticle.getReplyAmount()).toString());
        // 加载时间
        ViewUtils.setLastTime(viewHolder.uploadTime, sArticle.getCreateTime());

        // 加载图片
        ImageUtil.setImage(viewHolder.icon, sArticle.getHead_url(), R.drawable.ease_default_image, false);

        setItemAnim(sArticle, convertView, position);
        return convertView;
    }

    private void shareToOthers(ShopArticle article){
        if(shareDialog==null)
            shareDialog=new ShareDialog();

        shareDialog.show(article.getTitle(),article.getShareUrl()+"&isShare=1",article.getHead_url(),null);
    }

    public void setItemAnim(MyCirCleObject mObject, View mView, int position) {
        if (mObject.getIsShown()) {
            if (position == 0) {
                mView.startAnimation(
                        AnimationUtils.loadAnimation(context, R.anim.listview_first_item_anim));
                mObject.setIsShown(false);
            }
        } else {
            if (position > Constants.PER_PAGE_SIZE - 1) {
                mView.startAnimation(
                        AnimationUtils.loadAnimation(context, R.anim.listview_item_anim));
            }
            if (position > 0) {
                mObject.setIsShown(true);
            }
        }
    }

    private class ViewHolder {
        NetworkImageView icon;
        TextView rePlyTip;
        TextView title;
        TextView uploadTime;
        TextView btn_public;
    }

    @Override
    public int getLayoutId() {
        return R.layout.article_list_item;
    }

}
