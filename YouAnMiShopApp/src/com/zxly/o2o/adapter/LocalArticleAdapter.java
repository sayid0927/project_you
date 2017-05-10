package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.Shop.entity.Shop;
import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.activity.H5DetailAct;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.dbmanager.CommonUtils;
import com.zxly.o2o.dialog.ShareDialog;
import com.zxly.o2o.model.LocalArticle;
import com.zxly.o2o.model.LocalArticlesInfo;
import com.zxly.o2o.model.ShareInfo;
import com.zxly.o2o.request.PromoteCallbackConfirmRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ShareListener;
import com.zxly.o2o.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import static com.zxly.o2o.util.TimeUtil.formatTimeMMDD;

/**
 * Created by dsnx on 2016/9/7.
 */
public class LocalArticleAdapter extends ObjectAdapter implements View.OnClickListener {
    private int type;
    private ShareDialog dialog;
    private  LocalArticle localArticle;
    private List<Shop> shopDatas = new ArrayList<>();

    public CommonUtils mCommonUtils;

    public LocalArticleAdapter(Context _context) {
        super(_context);
    }

    public LocalArticleAdapter(Context _context, int type) {
        super(_context);
        this.type = type;
        this.mCommonUtils = new CommonUtils(context);
    }

    public void setLocalArticle(LocalArticle localArticle){
        this.localArticle=localArticle;
        this.mCommonUtils = new CommonUtils(context);

    }

    @Override
    public int getLayoutId() {
        return R.layout.item_local_article;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final LocalArticlesInfo loaclItem=(LocalArticlesInfo) getItem(position);
        if (convertView == null) {


            convertView = inflateConvertView();
            holder = new ViewHolder();
            holder.headIcon = (NetworkImageView) convertView.findViewById(R.id.img_head_icon);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.txt_title);
            holder.txtBrwoseCount = (TextView) convertView.findViewById(R.id.txt_browseCount);
            holder.btnPromotion = convertView.findViewById(R.id.btn_promotion);
            holder.txCityName= (TextView) convertView.findViewById(R.id.btn_cityName);
            holder.txDate=(TextView)convertView.findViewById(R.id.txt_date);
            holder.btnPromotion.setOnClickListener(this);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ViewHolder hb = (ViewHolder) v.getTag();
                    if (shopDatas.size() != 0) shopDatas.clear();
                    shopDatas = mCommonUtils.queryByBuilder(String.valueOf(hb.article.getId()));
                    if (shopDatas.size() != 0) {
                        Shop shop= new Shop();
                        shop.setIsclick(false);
                        shop.setClickid(String.valueOf(hb.article.getId()));
                        shop.setId(shopDatas.get(0).getId());
                        mCommonUtils.uoDateShop(shop);//更新数据
                    }

                    hb.txtTitle.setTextColor(context.getResources().getColor(R.color.light_grey));
                    ShareInfo shareInfo = new ShareInfo();
//                    if (!TextUtils.isEmpty(hb.article.getUserAppName())) {
//                        shareInfo.setDesc("【" + hb.article.getUserAppName() + "】" + hb.article.getDescription());
//                    } else {
                        shareInfo.setDesc(hb.article.getTitle());
//                    }
                    shareInfo.setTitle(hb.article.getTitle());
                    shareInfo.setUrl(hb.article.getShareUrl().replace("isShare=0", "isShare=1"));


//                    shareInfo.setIconUrl(hb.article.getHeadUrl());
                   // shareInfo.setIconUrl(new String(Base64.decode(hb.article.getHeadImage().getBytes(), Base64.DEFAULT)));
                    H5DetailAct.start(H5DetailAct.TYPE_DEFAULT,
                            AppController.getInstance().getTopAct(),
                            hb.article.getShareUrl() + "&from=app", "文章详情", shareInfo, false);



//                    HashMap<String, String> map = new HashMap<String, String>();
//                    map.put("tabID", String.valueOf(hb.article.getArticleId()));
//                    UmengUtil.onEvent(context, new UmengUtil().FIND_HOTTAB_CLICK, map);


                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.headIcon.setImageUrl(loaclItem.getHeadImage(), AppController.imageLoader);
        holder.headIcon.setDefaultImageResId(R.drawable.artice_def_icon);
        holder.txCityName.setText(loaclItem.getLabel());

        long time=loaclItem.getPublishTime();
        String date =formatTimeMMDD(time);
        holder.txDate.setText(date);

        int arrReadNum = loaclItem.getAllReadNum();
        if(arrReadNum >= 1000&&arrReadNum<10000){
            holder.txtBrwoseCount.setText((arrReadNum/1000)+"千 阅读");
        }else if(arrReadNum >= 10000&&arrReadNum<100000){
            holder.txtBrwoseCount.setText((arrReadNum/10000)+"万 阅读");
        }else if(arrReadNum >= 100000){
            holder.txtBrwoseCount.setText("10万+ 阅读");
        }else {
            holder.txtBrwoseCount.setText((arrReadNum)+" 阅读");
        }
         boolean isclick = true;
        if(shopDatas.size()!=0)shopDatas.clear();
        shopDatas = mCommonUtils.queryByBuilder(String.valueOf(loaclItem.getId()));
        for (int i=0;i<shopDatas.size();i++){
           isclick= shopDatas.get(i).getIsclick();
        }
        if(!isclick)
            holder.txtTitle.setTextColor(context.getResources().getColor(R.color.light_grey));
        else
            holder.txtTitle.setTextColor(context.getResources().getColor(R.color.gray_333333));

        ViewUtils.setText(holder.txtTitle, loaclItem.getTitle());

        holder.article = loaclItem;
        holder.btnPromotion.setTag(loaclItem);
        return convertView;
    }


    @Override
    public void onClick(View v) {
        if (dialog == null)
            dialog = new ShareDialog();

        final LocalArticlesInfo article = (LocalArticlesInfo) v.getTag();
        final String shareTitle = article.getTitle();
        String shareImageUrl = article.getHeadImage();
        String shareDesc = "";
        shareDesc = article.getTitle();
        String shareUrl = article.getShareUrl();
        dialog.show(shareTitle, shareDesc, shareUrl.replace("isShare=0", "isShare=1"), shareImageUrl, new ShareListener() {
            @Override
            public void onComplete(Object var1) {
                switch (type) {
                    case 1:
                        new PromoteCallbackConfirmRequest(article.getId(), 2, 1, article.getTitle()).start();
                        break;
                    case 2:
                        new PromoteCallbackConfirmRequest(article.getId(), 2, 3, article.getTitle()).start();
                        break;
                    case 3:
                        new PromoteCallbackConfirmRequest(article.getId(), 2, 2, article.getTitle()).start();
                        break;
                }
            }

            @Override
            public void onFail(int errorCode) {

            }
        });
    }


    class ViewHolder {
        LocalArticlesInfo article;
        NetworkImageView headIcon;
        TextView txtTitle, txtBrwoseCount;
        TextView txCityName,txDate;
        View btnPromotion;
    }
}
