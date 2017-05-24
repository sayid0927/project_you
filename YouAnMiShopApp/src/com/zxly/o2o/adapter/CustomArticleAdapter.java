package com.zxly.o2o.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easemob.easeui.widget.SlideDelete;
import com.zxly.o2o.activity.FragmentListAct;
import com.zxly.o2o.dialog.ShareDialog;
import com.zxly.o2o.model.PromotionArticle;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.PromoteCallbackConfirmRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.ShareListener;
import com.zxly.o2o.util.ViewUtils;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Created by kenwu on 2015/12/17.
 */
public class CustomArticleAdapter extends ObjectAdapter implements View.OnClickListener {

    protected ArrayList<SlideDelete> slideDeleteArrayList = new ArrayList<SlideDelete>();

    private ArticleDeleteRequest articleDeleteRequest;

    ParameCallBack articleDeleteCallBack;


    ShareDialog dialog;
    private boolean isDelItem=true;
    private String shareTitle;

    public CustomArticleAdapter(Context _context) {
        super(_context);
    }



    public void setArticleDeleteCallBack(ParameCallBack articleDeleteCallBack) {
        this.articleDeleteCallBack = articleDeleteCallBack;
    }



    @Override
    public int getLayoutId() {
        return R.layout.item_custom_article;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflateConvertView();
            convertView.setId(R.id.convertView);

            holder = new ViewHolder();
            holder.slideDelete = (SlideDelete) convertView.findViewById(R.id.layout_swipe);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.txt_title);
            holder.txtBrowseCount = (TextView) convertView.findViewById(R.id.txt_browseCount);
            holder.btnPromotion = convertView.findViewById(R.id.btn_promotion);
            holder.btnContent=convertView.findViewById(R.id.layout_article_content);
            holder.btnPromotion.setOnClickListener(this);
            holder.btnContent.setOnClickListener(this);
            holder.btnDelete = convertView.findViewById(R.id.btn_delete);
            holder.btnDelete.setOnClickListener(this);
            holder.slideDelete.setOnSlideDeleteListener(new SlideDelete.OnSlideDeleteListener() {
                @Override
                public void onOpen(SlideDelete slideDelete) {
                    closeOtherItem();
                    slideDeleteArrayList.add(slideDelete);
                }

                @Override
                public void onClose(SlideDelete slideDelete) {
                    slideDeleteArrayList.remove(slideDelete);
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.slideDelete.setDragEnable(isDelItem);

       fillData((PromotionArticle) getItem(position), holder);

        return convertView;
    }

    public void setDelItem(boolean delItem) {
        isDelItem = delItem;
    }
    private void closeOtherItem(){
//        System.out.println("closeOtherItem");
        // 采用Iterator的原因是for是线程不安全的，迭代器是线程安全的
        ListIterator<SlideDelete> slideDeleteListIterator = slideDeleteArrayList.listIterator();
        while(slideDeleteListIterator.hasNext()){
            SlideDelete slideDelete = slideDeleteListIterator.next();
            slideDelete.isShowDelete(false);
        }
        slideDeleteArrayList.clear();
    }
    private void fillData(PromotionArticle article, ViewHolder holder) {
        holder.btnContent.setTag(article);
        ViewUtils.setText(holder.txtTitle, article.getTitle());
        Spanned _txtScanCount = Html.fromHtml("<font color=\"#f49126\">" + article.getScanCount() + "&nbsp;</font><font color=\"#999999\">浏览</font>");
        holder.txtBrowseCount.setText(_txtScanCount);
        holder.btnPromotion.setTag(article);
        holder.btnDelete.setTag(article);
    }


    @Override
    public void onClick(View v) {
        PromotionArticle article = null;
        ViewHolder holder = null;
        switch (v.getId()) {

            case R.id.layout_article_content:
                article = (PromotionArticle) v.getTag();
                Bundle bundle = new Bundle();
                bundle.putString("title", article.getTitle());
                bundle.putString("fireUrl", article.getUrl());
                //从后台返回连接中截取的分享图片链接
                bundle.putString("shareImageUrl",new String(android.util.Base64.decode(article.getShareImageUrl().getBytes(), android.util.Base64.DEFAULT)));
                bundle.putString("userAppName",article.getUserAppName());
                bundle.putString("desc",article.getDescription());
                FragmentListAct.start("文章详情", FragmentListAct.PAGE_CUSTOM_ARTICLE_DETAIL, bundle, null);
                break;

            case R.id.btn_promotion:
                if (dialog == null)
                    dialog = new ShareDialog();
                //使用门店icon
                 article = (PromotionArticle) v.getTag();
                 final long articleId=article.getArticleId();
                final String _title=article.getTitle();
                String desc=article.getDescription();
                if(!TextUtils.isEmpty(article.getUserAppName())){
                    desc = ("【"+article.getUserAppName()+"】"+desc);
                }
                String shareImageUrl=new String(android.util.Base64.decode(article.getShareImageUrl().getBytes(), android.util.Base64.DEFAULT));

               String  url= article.getUrl();

                int index= isChinese(url);
                if(index!=000){
                    url= url.substring(0,index);
                }


                dialog.show(_title,desc,url.replace("isShare=0","isShare=1"), shareImageUrl, new ShareListener() {
                    @Override
                    public void onComplete(Object var1) {
                        if(isDelItem){
                            new PromoteCallbackConfirmRequest(articleId,2,2,_title).start();
                        }else {
                            new PromoteCallbackConfirmRequest(articleId,2,4,_title).start();
                        }
                    }

                    @Override
                    public void onFail(int errorCode) {

                    }
                });
                break;

            case R.id.btn_delete:
                closeOtherItem();
                final PromotionArticle mAritcle = (PromotionArticle) v.getTag();
                if (articleDeleteRequest == null) {
                    articleDeleteRequest = new ArticleDeleteRequest();
                }

                articleDeleteRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                    @Override
                    public void onOK() {
                        if (articleDeleteCallBack != null)
                            articleDeleteCallBack.onCall(mAritcle.getArticleId());
                        ViewUtils.showToast("文章删除成功!");

                    }

                    @Override
                    public void onFail(int code) {
                        ViewUtils.showToast("文章删除失败!");
                    }
                });

                articleDeleteRequest.addParams("articleId", mAritcle.getArticleId());
                articleDeleteRequest.start();

                getContent().remove(mAritcle);
                notifyDataSetChanged();
                break;

            default:
                break;

        }

    }

    static class ArticleDeleteRequest extends BaseRequest {

        @Override
        protected String method() {
            return "makeFans/custome/article/del";
        }

    }


    // 判断一个字符串是否含有中文
    public int isChinese(String str) {
        if (str == null)
            return 000;
        int i=0;
        for (char c : str.toCharArray()) {
            if (isChinese(c)) {
                return i;// 有一个中文字符就返回
            }else {
                i++;
                continue;
            }
        }
        return 000;
    }

    // 判断一个字符是否是中文
    public boolean isChinese(char c) {
        return c >= 0x4E00 && c <= 0x9FA5;// 根据字节码判断
    }

    static class ViewHolder {
        SlideDelete slideDelete;
        TextView txtTitle;
        TextView txtBrowseCount;
        View btnPromotion,btnContent;
        View btnDelete;
        boolean isOpen;
    }

}
