package com.zxly.o2o.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.activity.H5DetailAct;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.dialog.ShareDialog;
import com.zxly.o2o.model.ShareInfo;
import com.zxly.o2o.model.StoreArticle;
import com.zxly.o2o.request.PromoteCallbackConfirmRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ShareListener;
import com.zxly.o2o.util.UmengUtil;
import com.zxly.o2o.util.ViewUtils;

import java.util.HashMap;

/**
 * Created by dsnx on 2016/9/7.
 */
public class StoreArticelAdapter extends ObjectAdapter implements View.OnClickListener {
    private int type;
    private ShareDialog dialog;

    public StoreArticelAdapter(Context _context) {
        super(_context);
    }

    public StoreArticelAdapter(Context _context, int type) {
        super(_context);
        this.type = type;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_store_article;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflateConvertView();
            holder = new ViewHolder();
            holder.headIcon = (NetworkImageView) convertView.findViewById(R.id.img_head_icon);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.txt_title);
            holder.txtBrwoseCount = (TextView) convertView.findViewById(R.id.txt_browseCount);
            holder.btnPromotion = convertView.findViewById(R.id.btn_promotion);
            holder.txtRecomend = convertView.findViewById(R.id.txt_recomend);
            holder.txtLabel1 = (TextView) convertView.findViewById(R.id.txt_label1);
            holder.txtLabel2 = (TextView) convertView.findViewById(R.id.txt_label2);
            holder.txtLabel3 = (TextView) convertView.findViewById(R.id.txt_label3);
            holder.labelProduct = convertView.findViewById(R.id.label_product);
            holder.btnPromotion.setOnClickListener(this);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewHolder hb = (ViewHolder) v.getTag();
                    ShareInfo shareInfo = new ShareInfo();
                    if (!TextUtils.isEmpty(hb.article.getUserAppName())) {
                        shareInfo.setDesc("【" + hb.article.getUserAppName() + "】" + hb.article.getDescription());
                    } else {
                        shareInfo.setDesc(hb.article.getDescription());
                    }
                    shareInfo.setTitle(hb.article.getTitle());
                    shareInfo.setUrl(hb.article.getUrl().replace("isShare=0", "isShare=1"));
//                    shareInfo.setIconUrl(hb.article.getHeadUrl());


                    shareInfo.setIconUrl(new String(Base64.decode(hb.article.getShareImageUrl().getBytes(), Base64.DEFAULT)));
                    H5DetailAct.start(H5DetailAct.TYPE_DEFAULT,
                            AppController.getInstance().getTopAct(),
                            hb.article.getUrl() + "&fromApp=true", "文章详情", shareInfo, true);

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("tabID", String.valueOf(hb.article.getArticleId()));
                    UmengUtil.onEvent(context, new UmengUtil().FIND_HOTTAB_CLICK, map);


                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        StoreArticle storeArticle = (StoreArticle) getItem(position);
        int hasNewLabel = storeArticle.getHasNewLabel();
        holder.headIcon.setImageUrl(storeArticle.getHeadUrl(), AppController.imageLoader);
        holder.headIcon.setDefaultImageResId(R.drawable.artice_def_icon);
        ViewUtils.setText(holder.txtTitle, storeArticle.getTitle());
        if (storeArticle.isProductArticle()) {
            ViewUtils.setVisible(holder.labelProduct);
        } else {
            ViewUtils.setGone(holder.labelProduct);
        }
        if (storeArticle.isRecomend()) {
            if (hasNewLabel == 1) {
                holder.txtRecomend.setBackgroundColor(Color.parseColor("#ff0200"));
            }
            ViewUtils.setVisible(holder.txtRecomend);
        } else {
            ViewUtils.setGone(holder.txtRecomend);
        }
        String[] labels = storeArticle.getLabels();
        ViewUtils.setGone(holder.txtLabel1);
        ViewUtils.setGone(holder.txtLabel2);
        ViewUtils.setGone(holder.txtLabel3);
        StringBuilder sbLabels = new StringBuilder();
        if (labels != null) {
            int len = labels.length;
            for (int i = 0; i < len; i++) {
                String[] label = labels[i].split(",");
                int id = Integer.parseInt(label[0]);
                String name = label[1];
                int m = id % 3;
                TextView tvLabel = null;
                switch (i) {
                    case 0:
                        tvLabel = holder.txtLabel1;
                        break;
                    case 1:
                        tvLabel = holder.txtLabel2;
                        break;
                    case 2:
                        tvLabel = holder.txtLabel3;
                        break;
                }
                ViewUtils.setVisible(tvLabel);
                //十一月运维2对标签背景颜色做了一个新规则  新旧规则通过hasNewLabel标识
                String color = "#ff5e1d";
                if (hasNewLabel == 1) {
                    switch (id) {
                        case 1:
                            color = "#1f9eef";
                            break;
                        case 2:
                            color = "#22b84a";
                            break;
                        case 3:
                            color = "#ff621f";
                            break;
                        case 4:
                            color = "#ffae00";
                            break;
                        case 5:
                            color = "#35c7b1";
                            break;
                        case 6:
                            color = "#ff0200";
                            break;
                    }
                } else {
                    switch (m) {
                        case 1:
                            color = "#1fb94e";
                            break;
                        case 2:
                            color = "#1ea1f7";
                            break;
                    }

                }
                sbLabels.append(name);
                if (sbLabels.length() <= 15) {
                    tvLabel.setBackgroundColor(Color.parseColor(color));
                    tvLabel.setText(name);
                }

            }
        }
        Spanned _txtShareCount = Html.fromHtml("<font color=\"#f49126\">" + storeArticle.getScanCount() + "&nbsp;</font><font color=\"#999999\">浏览</font>");
        holder.txtBrwoseCount.setText(_txtShareCount);
        holder.article = storeArticle;
        holder.btnPromotion.setTag(storeArticle);
        return convertView;
    }


    @Override
    public void onClick(View v) {
        if (dialog == null)
            dialog = new ShareDialog();

        final StoreArticle article = (StoreArticle) v.getTag();
        final String shareTitle = article.getTitle();
        String desc = article.getDescription().trim();
        String shareImageUrl = new String(Base64.decode(article.getShareImageUrl(), Base64.URL_SAFE));
        String userAppName = article.getUserAppName();
        String shareDesc = "";
        if (!TextUtils.isEmpty(userAppName)) {
            shareDesc = new StringBuilder().append("【" + userAppName + "】").append(desc).toString();
        } else {
            shareDesc = desc;
        }
        String shareUrl = article.getUrl();

       int index= isChinese(shareUrl);
        if(index!=000){

        shareUrl= shareUrl.substring(0,index);

        }
      // shareUrl="http://pxqd.youanmi.com/share/shop-app-article-detail/index.html?shopId=840&userId=61119&isShare=0&id=269075&articleFrom=2&u=aHR0cHM6Ly9zaGFyZS55b3Vhbm1pLmNvbS8&shareImage=aHR0cDovL2NkbmltZzMueW91YW5taS5jb20veWFtL00wMC81Ni9CRi9lRXhMS2xrYXY1cUFBN1NXQUFBYnptenJybUEyNi5KUEVH&title";


        dialog.show(shareTitle, shareDesc, shareUrl.replace("isShare=0", "isShare=1"), shareImageUrl, new ShareListener() {
            @Override
            public void onComplete(Object var1) {
                switch (type) {
                    case 1:
                        new PromoteCallbackConfirmRequest(article.getArticleId(), 2, 1, article.getTitle()).start();
                        break;
                    case 2:
                        new PromoteCallbackConfirmRequest(article.getArticleId(), 2, 3, article.getTitle()).start();
                        break;
                    case 3:
                        new PromoteCallbackConfirmRequest(article.getArticleId(), 2, 2, article.getTitle()).start();
                        break;
                }
            }

            @Override
            public void onFail(int errorCode) {

            }
        });
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

    class ViewHolder {
        StoreArticle article;
        NetworkImageView headIcon;
        TextView txtTitle, txtBrwoseCount;
        TextView txtLabel1, txtLabel2, txtLabel3;
        View btnPromotion, txtRecomend, labelProduct;
    }
}
