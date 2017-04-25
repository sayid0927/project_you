package com.zxly.o2o.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.H5DetailAct;
import com.zxly.o2o.activity.LoginAct;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.dialog.ShareDialog;
import com.zxly.o2o.model.ActicityInfo;
import com.zxly.o2o.model.ShareInfo;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.BitmapUtil;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.ShareListener;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

/**
 * Created by kenwu on 2015/12/17.
 */
public class PromotionActivityAdapter extends ObjectAdapter implements View.OnClickListener {

    ShareDialog shareDialog;
    ShareSuccessRequest shareSuccessRequest;

    public PromotionActivityAdapter(Context _context) {
        super(_context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView=inflateConvertView();
            holder=new ViewHolder();
            holder.imgHeadIcon= (NetworkImageView) convertView.findViewById(R.id.img_head_icon);
            holder.txtTitle= (TextView) convertView.findViewById(R.id.txt_title);
            holder.txtDesc= (TextView) convertView.findViewById(R.id.txt_desc);
            holder.txtShareCount= (TextView) convertView.findViewById(R.id.txt_shareCount);
            holder.txtBrowseCount= (TextView) convertView.findViewById(R.id.txt_browseCount);
            holder.btnPromotion=convertView.findViewById(R.id.btn_promotion);
            holder.btnPromotion.setOnClickListener(this);
            convertView.setOnClickListener(this);
            convertView.setId(R.id.convertView);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        fillData(holder, (ActicityInfo) getItem(position));

        return convertView;
    }

    private void fillData(ViewHolder holder,ActicityInfo acticityInfo){
//        holder.imgHeadIcon.setDefaultImageResId(R.drawable.icon_default_100x100);
//        holder.imgHeadIcon.setImageUrl(acticityInfo.getImageUrl(), AppController.imageLoader);

        holder.imgHeadIcon.setImageUrl(acticityInfo.getImageUrl(), AppController.imageLoader);
        if(acticityInfo.getType()==ActicityInfo.TYPE_DDYH)//到店优惠
        {
            ViewUtils.setText(holder.txtTitle,"到店优惠");
            ViewUtils.setText(holder.txtDesc,"拉动客户去店里，成为你的粉丝，赚长期分成");
            holder.imgHeadIcon.setDefaultImageResId(R.drawable.icon_ddyh);
        }else if(acticityInfo.getType()==ActicityInfo.TYPE_DZP){//大转盘
            ViewUtils.setText(holder.txtTitle,"大转盘");
            ViewUtils.setText(holder.txtDesc,acticityInfo.getPopuName());
            holder.imgHeadIcon.setDefaultImageResId(R.drawable.icon_dazhuanpan);
        }else
        {
            ViewUtils.setText(holder.txtTitle,acticityInfo.getPopuName());
            ViewUtils.setText(holder.txtDesc,acticityInfo.getPopuDesc());
        }

        Spanned _txtShareCount= Html.fromHtml("<font color=\"#f49126\">" + acticityInfo.getShareAmount() + "&nbsp;</font><font color=\"#999999\">转发</font>");
        holder.txtShareCount.setText(_txtShareCount);
        Spanned _txtBrowseCount= Html.fromHtml("<font color=\"#f49126\">" + acticityInfo.getReadAmount() + "&nbsp;</font><font color=\"#999999\">浏览</font>");
        holder.txtBrowseCount.setText(_txtBrowseCount);
        holder.btnPromotion.setTag(acticityInfo);
        holder.acticityInfo=acticityInfo;
    }

    @Override
    public void onClick(View v) {
        String shareTitle="";
        switch (v.getId()){
            case R.id.convertView:
                ViewHolder holder= (ViewHolder) v.getTag();
                ShareInfo shareInfo=new ShareInfo();
                shareInfo.setIconUrl(holder.acticityInfo.getImageUrl());
                if(holder.acticityInfo.getType()==ActicityInfo.TYPE_DDYH)
                {
                    if(!StringUtil.isNull(holder.acticityInfo.getAppName()))
                    {
                        shareTitle="【"+holder.acticityInfo.getAppName()+"】"+"到店优惠享不停！";
                    }else
                    {
                        shareTitle="到店优惠享不停！";
                    }

                }else
                {
                    shareTitle= DataUtil.stringIsNull(holder.acticityInfo.getAppName()) ?
                            holder.acticityInfo.getPopuName() :
                            "【"+holder.acticityInfo.getAppName()+"】"+holder.acticityInfo.getPopuName();
                }
                shareInfo.setTitle(shareTitle);
                shareInfo.setDesc(holder.acticityInfo.getPopuDesc());
                shareInfo.setUrl(holder.acticityInfo.getShareUrl()+"&title="+shareTitle+"&desc="+shareInfo.getDesc());
                shareInfo.setId(holder.acticityInfo.getId());
                shareInfo.setType(holder.acticityInfo.getType());
                H5DetailAct.start(H5DetailAct.TYPE_H5_GAME,
                        AppController.getInstance().getTopAct(),
                        holder.acticityInfo.getShareUrl(),
                        holder.acticityInfo.getPopuName(), shareInfo);
                break;

            case R.id.btn_promotion:
                final ActicityInfo acticityInfo= (ActicityInfo) v.getTag();
                if(shareDialog==null)
                    shareDialog=new ShareDialog();

                if(Account.user==null)
                    LoginAct.start(AppController.getInstance().getTopAct());

                      shareTitle= DataUtil.stringIsNull(acticityInfo.getAppName()) ?
                        acticityInfo.getPopuName() :
                        "【"+acticityInfo.getAppName()+"】"+ acticityInfo.getPopuName();


                if(acticityInfo.getType()==ActicityInfo.TYPE_DDYH)
                {
                    if(!StringUtil.isNull(acticityInfo.getAppName()))
                    {
                        shareTitle="【"+acticityInfo.getAppName()+"】"+"到店优惠享不停！";
                    }else
                    {
                        shareTitle="到店优惠享不停！";
                    }
                }
                Object shareImage=null;
                if(StringUtil.isNull(acticityInfo.getImageUrl()))
                {
                    if(acticityInfo.getType()==ActicityInfo.TYPE_DDYH){
                        shareImage=BitmapUtil.drawableToBitmap(context.getResources().getDrawable(R.drawable.ddyh_icon));
                    }else if(acticityInfo.getType()==ActicityInfo.TYPE_DZP){
                        shareImage=BitmapUtil.drawableToBitmap(context.getResources().getDrawable(R.drawable.icon_dazhuanpan));
                    }
                } else {
                    shareImage=acticityInfo.getImageUrl();
                }
                shareDialog.show(shareTitle, acticityInfo.getPopuDesc(),
                        acticityInfo.getShareUrl()+"&title="+shareTitle+"&desc="+acticityInfo.getPopuDesc(),
                        shareImage,
                        new ShareListener() {
                            @Override
                            public void onComplete(Object var1) {
                                if(shareSuccessRequest==null)
                                    shareSuccessRequest=new ShareSuccessRequest();

                                shareSuccessRequest.addParams("type",acticityInfo.getType());
                                shareSuccessRequest.addParams("id",acticityInfo.getId());
                                shareSuccessRequest.addParams("title",acticityInfo.getPopuName());
                                shareSuccessRequest.addParams("shopId", Account.user.getShopId());
                                shareSuccessRequest.start();
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
        TextView txtDesc;
        TextView txtShareCount;
        TextView txtBrowseCount;
        View btnPromotion;
        ActicityInfo acticityInfo;
    }


   static class ShareSuccessRequest extends BaseRequest{
       @Override
       protected String method() {
           return "/makeFans/addShareAmount";
       }
   }


}
