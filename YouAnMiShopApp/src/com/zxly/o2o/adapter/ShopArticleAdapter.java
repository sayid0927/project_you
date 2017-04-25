package com.zxly.o2o.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.activity.H5DetailAct;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.dialog.ShareDialog;
import com.zxly.o2o.model.PromotionArticle;
import com.zxly.o2o.model.ShareInfo;
import com.zxly.o2o.request.PromoteCallbackConfirmRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ShareListener;
import com.zxly.o2o.util.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kenwu on 2015/12/17.
 */
public class ShopArticleAdapter extends ObjectAdapter implements View.OnClickListener {

    ShareDialog dialog;
    public ShopArticleAdapter(Context _context) {
        super(_context);
    }


    @Override
    public int getLayoutId() {
        return R.layout.item_promotion_article;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView=inflateConvertView();
            convertView.setId(R.id.convertView);
            convertView.setOnClickListener(this);
            holder=new ViewHolder();
            holder.imgHeadIcon= (NetworkImageView) convertView.findViewById(R.id.img_head_icon);
            holder.txtTitle= (TextView) convertView.findViewById(R.id.txt_title);
            holder.txtBrowseCount= (TextView) convertView.findViewById(R.id.txt_browseCount);
            holder.btnPromotion=convertView.findViewById(R.id.btn_promotion);
            holder.btnPromotion.setOnClickListener(this);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        fillData((PromotionArticle) getItem(position),holder);



        return convertView;
    }

    private void fillData(PromotionArticle article,ViewHolder holder){
        holder.article=article;
        holder.imgHeadIcon.setDefaultImageResId(R.drawable.icon_default_170x170);
        holder.imgHeadIcon.setImageUrl(article.getHeadUrl(), AppController.imageLoader);
        ViewUtils.setText(holder.txtTitle, article.getTitle());
        Spanned _txtScanCount= Html.fromHtml("<font color=\"#f49126\">"+article.getScanCount()+"&nbsp;</font><font color=\"#999999\">浏览</font>");
        holder.txtBrowseCount.setText(_txtScanCount);
        holder.btnPromotion.setTag(article);
    }

    @Override
    public void onClick(View v) {

     switch (v.getId()){
            case R.id.convertView:
                ViewHolder holder= (ViewHolder) v.getTag();
                ShareInfo shareInfo=new ShareInfo();
                shareInfo.setTitle(holder.article.getTitle());
                shareInfo.setDesc("");
                shareInfo.setUrl(holder.article.getUrl().replace("isShare=0","isShare=1"));
                shareInfo.setIconUrl(holder.article.getHeadUrl());
                H5DetailAct.start(H5DetailAct.TYPE_DEFAULT,
                                  AppController.getInstance().getTopAct(),
                                  holder.article.getUrl()+"&fromApp=true", "文章详情", shareInfo,true);
                break;

            case R.id.btn_promotion:
                if(dialog==null)
                    dialog=new ShareDialog();

                final PromotionArticle article= (PromotionArticle) v.getTag();
                dialog.show(article.getTitle(), article.getUrl().replace("isShare=0","isShare=1"), article.getHeadUrl(), new ShareListener() {
                    @Override
                    public void onComplete(Object var1) {
                        new PromoteCallbackConfirmRequest(article.getArticleId(),2,article.getTitle()).start();
                    }

                    @Override
                    public void onFail(int errorCode) {

                    }
                });
                break;

            default:
                break;

        }

     }


    static class ViewHolder{
        NetworkImageView imgHeadIcon;
        TextView txtTitle;
        TextView txtBrowseCount;
        View btnPromotion;
        PromotionArticle article;
    }


}
