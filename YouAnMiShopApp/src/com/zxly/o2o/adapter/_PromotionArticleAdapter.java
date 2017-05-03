package com.zxly.o2o.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.activity.FragmentListAct;
import com.zxly.o2o.activity.H5DetailAct;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.dialog.ArticleTypeSelectDialog;
import com.zxly.o2o.dialog.ShareDialog;
import com.zxly.o2o.fragment.PromotionArticleFragment;
import com.zxly.o2o.model.ArticleType;
import com.zxly.o2o.model.PromotionArticle;
import com.zxly.o2o.model.ShareInfo;
import com.zxly.o2o.request.PromoteCallbackConfirmRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.ShareListener;
import com.zxly.o2o.util.UmengUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.MListView;

import java.util.List;
import java.util.Map;

/**
 * Created by kenwu on 2015/12/17.
 */
public class _PromotionArticleAdapter extends ObjectAdapter implements View.OnClickListener {

    private static final int TYPE_COUNT =2 ;

    ItemCustomArticleHolder itemCustomArticleHolder;

    ShareDialog dialog;

    ArticleTypeSelectDialog articleTypeSelectDialog;

    List<ArticleType> articleTypes;

    ArticleType defaultType;

    private PromotionArticleFragment curFragment;

    private ParameCallBack callBack;

    private ParameCallBack articleRefreshCallBack=new ParameCallBack() {
        @Override
        public void onCall(Object object) {

        }
    };

    public _PromotionArticleAdapter(Context _context) {
        super(_context);
    }

    // 每个convert view都会调用此方法，获得当前所需要的view样式
    @Override
    public int getItemViewType(int position) {
        if(getItem(position) instanceof Map){
           // return PromotionArticleFragment.TYPE_CUSTOM_ARTICLE;
        }else {
           // return PromotionArticleFragment.TYPE_SHOP_ARTICLE;
        }
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }


    public void setCallBack(ParameCallBack callBack) {
        this.callBack = callBack;
    }


    public void setCurFragment(PromotionArticleFragment curFragment) {
        this.curFragment = curFragment;
    }

    public void setArticleTypes(List<ArticleType> articleTypes) {
        this.articleTypes = articleTypes;
    }

    public void setDefaultType(ArticleType defaultType) {
        this.defaultType = defaultType;
    }

    public void clearAllShopArticle(){
        if(!DataUtil.listIsNull(getContent())){
            for (Object object:getContent()) {
                if(object instanceof PromotionArticle){
                    getContent().remove(object);
                }
            }
        }
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object holder=null;

        if(convertView==null){
            if(getItemViewType(position)==1){
                convertView= LayoutInflater.from(context).inflate(R.layout.item_custom_article_list,null);
                ItemCustomArticleHolder mHolder=new ItemCustomArticleHolder();
                mHolder.mListView= (MListView) convertView.findViewById(R.id.mListView);
                CustomArticleAdapter adapter=new CustomArticleAdapter(context);
                mHolder.mListView.setAdapter(adapter);
                mHolder.btnMore= (TextView) convertView.findViewById(R.id.btn_more);
                mHolder.btnMore.setOnClickListener(this);

               // mHolder.layoutArticleType=convertView.findViewById(R.id.layout_article_type);

               // mHolder.btnShowAllType= (TextView) convertView.findViewById(R.id.btn_type);
                mHolder.btnShowAllType.setOnClickListener(this);

                mHolder.btnPaste= (TextView) convertView.findViewById(R.id.btn_paste);
                mHolder.btnPaste.setOnClickListener(this);

                //mHolder.viewEmptyBg=convertView.findViewById(R.id.view_empty_bg);
                holder=mHolder;
                itemCustomArticleHolder=mHolder;
            }else {
                convertView= LayoutInflater.from(context).inflate(R.layout.item_promotion_article,null);
                convertView.setId(R.id.convertView);
                convertView.setOnClickListener(this);
                ItemShopArticleHolder mHolder=new ItemShopArticleHolder();
                mHolder.imgHeadIcon= (NetworkImageView) convertView.findViewById(R.id.img_head_icon);
                mHolder.txtTitle= (TextView) convertView.findViewById(R.id.txt_title);
                mHolder.txtBrowseCount= (TextView) convertView.findViewById(R.id.txt_browseCount);
                mHolder.btnPromotion=convertView.findViewById(R.id.btn_promotion);
                mHolder.btnPromotion.setOnClickListener(this);
                convertView.setTag(holder);
                holder=mHolder;
            }

            convertView.setOnClickListener(this);
            convertView.setTag(holder);
        }else {
            holder=convertView.getTag();
        }

        if(getItemViewType(position)==1){
            Map<String,Object> data= (Map<String, Object>) getItem(position);
            List<PromotionArticle> itemData= (List<PromotionArticle>) data.get("data");
            fillCustomArticleItemData(itemData, (ItemCustomArticleHolder) holder);
        }else {
            fillShopArticleItemData((PromotionArticle) getItem(position), (ItemShopArticleHolder) holder);
        }

        return convertView;
    }

    private void fillCustomArticleItemData(List<PromotionArticle> articles,ItemCustomArticleHolder holder){
        CustomArticleAdapter adapter= (CustomArticleAdapter) holder.mListView.getAdapter();
        adapter.clear();


        if(DataUtil.listIsNull(articleTypes)){
            holder.layoutArticleType.setVisibility(View.GONE);
        }else {
            holder.layoutArticleType.setVisibility(View.VISIBLE);
            ViewUtils.setText(holder.btnShowAllType, defaultType.getCodeName());
        }

        if(DataUtil.listIsNull(articles)){
            adapter.notifyDataSetChanged();
            holder.viewEmptyBg.setVisibility(View.VISIBLE);
            holder.btnMore.setVisibility(View.GONE);
        }else {
            holder.btnMore.setVisibility(View.VISIBLE);
            holder.viewEmptyBg.setVisibility(View.GONE);
            adapter.addItem(articles, true);
        }
    }

    private void fillShopArticleItemData(PromotionArticle article,ItemShopArticleHolder holder){
        holder.article=article;
        holder.imgHeadIcon.setDefaultImageResId(R.drawable.icon_default_170x170);
        holder.imgHeadIcon.setImageUrl(article.getHeadUrl(), AppController.imageLoader);
        ViewUtils.setText(holder.txtTitle, article.getTitle());
        Spanned _txtScanCount= Html.fromHtml("<font color=\"#f49126\">" + article.getScanCount() + "&nbsp;</font><font color=\"#999999\">浏览</font>");
        holder.txtBrowseCount.setText(_txtScanCount);
        holder.btnPromotion.setTag(article);

    }



    @Override
    public void onClick(View v) {

     switch (v.getId()){


         case R.id.convertView:
            ItemShopArticleHolder holder= (ItemShopArticleHolder) v.getTag();
             ShareInfo shareInfo=new ShareInfo();
             shareInfo.setTitle(holder.article.getTitle());
             shareInfo.setDesc("");
             shareInfo.setUrl(holder.article.getUrl().replace("isShare=0","isShare=1"));
             shareInfo.setIconUrl(holder.article.getHeadUrl());
             H5DetailAct.start(H5DetailAct.TYPE_DEFAULT,
                     AppController.getInstance().getTopAct(),
                     holder.article.getUrl() + "&fromApp=true", "文章详情", shareInfo, true);
             break;


         case R.id.btn_promotion:
             if(dialog==null)
                 dialog=new ShareDialog();

             final PromotionArticle article= (PromotionArticle) v.getTag();
             dialog.show(article.getTitle(), article.getUrl().replace("isShare=0", "isShare=1"), article.getHeadUrl(), new ShareListener() {
                 @Override
                 public void onComplete(Object var1) {
                     new PromoteCallbackConfirmRequest(article.getArticleId(),2,article.getTitle()).start();
                 }

                 @Override
                 public void onFail(int errorCode) {

                 }
             });
             break;

         case R.id.btn_more:
             FragmentListAct.start("自定义文章", FragmentListAct.PAGE_CUSTOM_ARTICLE_LIST,articleRefreshCallBack);
             break;

         case R.id.btn_paste:
             String wxUrl=null;

             //wxUrl="http://mp.weixin.qq.com/s?__biz=MzA4NjA3NjIyNg==&mid=2652078409&idx=1&sn=e9fc98bd2130f48abc80453a826b2715&scene=4";

            // wxUrl="http://5566.net/";

             //适用3.0 之后的机器
             ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
             if(cm.hasPrimaryClip()){
                 ClipData data = cm.getPrimaryClip();
                 ClipData.Item item = data.getItemAt(0);
                  wxUrl=item.getText().toString();
             }

             if(!TextUtils.isEmpty(wxUrl)){
                 Bundle bundle=new Bundle();
                 bundle.putString("wxUrl",wxUrl);
                 FragmentListAct.start("详情",FragmentListAct.PAGE_CUSTOM_ARTICLE_DETAIL,bundle,articleRefreshCallBack);
             }else {
                 ViewUtils.showToast("请先去微信公众号复制链接！");
             }

             UmengUtil.onEvent(context,new UmengUtil().FIND_CUSTOM_PASTE_CLICK,null);

             break;

         case 1: //R.id.btn_type:
             if(articleTypeSelectDialog==null){
                 articleTypeSelectDialog=new ArticleTypeSelectDialog();
                 articleTypeSelectDialog.setCallBack(new ParameCallBack() {
                     @Override
                     public void onCall(Object object) {
                         Drawable drawable= context.getResources().getDrawable(R.drawable.turn_normal);
                         drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                         itemCustomArticleHolder.btnShowAllType.setCompoundDrawables(null,null,drawable,null);

                         if(object instanceof  ArticleType){
                             ArticleType articleType= (ArticleType) object;
                             itemCustomArticleHolder.btnShowAllType.setText(articleType.getCodeName());
                             if(callBack!=null)
                                 callBack.onCall(articleType);
                         }

                     }
                 });
             }

             if(!DataUtil.listIsNull(articleTypes)){
                 Drawable drawable= context.getResources().getDrawable(R.drawable.turn_press);
                 drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                 itemCustomArticleHolder.btnShowAllType.setCompoundDrawables(null,null,drawable,null);
                 articleTypeSelectDialog.setCurSelectType(defaultType);
                 articleTypeSelectDialog.setArticleTypes(articleTypes);
                 articleTypeSelectDialog.show();
             }
             break;

            default:
                break;
        }

     }



    static class ItemCustomArticleHolder{
        MListView mListView;
        TextView btnMore;
        TextView btnPaste;
        View layoutArticleType;
        TextView btnShowAllType;
        View viewEmptyBg;
    }


    static class ItemShopArticleHolder{
        NetworkImageView imgHeadIcon;
        TextView txtTitle;
        TextView txtBrowseCount;
        View btnPromotion;
        PromotionArticle article;
    }

}
