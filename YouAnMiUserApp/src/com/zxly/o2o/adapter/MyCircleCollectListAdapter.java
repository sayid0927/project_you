package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.dialog.TwoBtnActionDialog;
import com.zxly.o2o.model.ShopArticle;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.ArticleCollectRequst;
import com.zxly.o2o.request.BaseRequest.ResponseStateListener;
import com.zxly.o2o.util.ImageUtil;
import com.zxly.o2o.util.ParameCallBackById;
import com.zxly.o2o.util.ViewUtils;

public class MyCircleCollectListAdapter extends BasicMyCircleAdapter {

    public MyCircleCollectListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflateConvertView();
            viewHolder.icon = (NetworkImageView) convertView.findViewById(R.id.win_mycircle_list_item_icon);
            viewHolder.deleteCollected =  convertView.findViewById(R.id.btn_del);
            viewHolder.rePlyTip = (TextView) convertView.findViewById(R.id.win_mycircle_list_item_reply_tip);
            viewHolder.title = (TextView) convertView.findViewById(R.id.win_mycircle_list_item_title);
            viewHolder.uploadTime = (TextView) convertView.findViewById(R.id.win_mycircle_list_item_upload_time);
            viewHolder.tip = (TextView) convertView.findViewById(R.id.win_mycircle_list_item_tip);
            viewHolder.tip2 = (TextView) convertView.findViewById(R.id.win_mycircle_list_item_tip2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final ShopArticle sArticle = (ShopArticle) content.get(position);

        viewHolder.deleteCollected.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                new TwoBtnActionDialog().show(new ParameCallBackById() {

                    @Override
                    public void onCall(int id, Object object) {
                        switch (id) {
                            case R.id.dialog_btn_left:
                                ArticleCollectRequst articleCollectRequst = new ArticleCollectRequst(sArticle.getId(), 2);
                                articleCollectRequst.setOnResponseStateListener(new ResponseStateListener() {

                                    @Override
                                    public void onOK() {
                                        content.remove(position);
                                        notifyDataSetChanged();
                                        ViewUtils.showToast(context.getResources().getString(R.string.cancle_collect));
                                    }

                                    @Override
                                    public void onFail(int code) {
                                        ViewUtils.showToast("操作失败!");
                                    }
                                });
                                articleCollectRequst.start(context);
                                break;
                            case R.id.dialog_btn_right:
                                 ViewUtils.share(context, sArticle.getUrl() + "《" + sArticle.getTitle() + "》");

                                break;
                            case R.id.dialog_btn_bottom:
                                break;
                        }
                    }
                });

            }
        });

        if ("hot".equals(sArticle.getLableCode())) {
            viewHolder.tip.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tip.setVisibility(View.GONE);
        }
        if ("jian".equals(sArticle.getLableCode())) {
            viewHolder.tip2.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tip2.setVisibility(View.GONE);
        }

        viewHolder.title.setText(sArticle.getTitle());
        viewHolder.rePlyTip.setText(sArticle.getReplyAmount() + "");

        // 加载时间
        ViewUtils.setLastTime(viewHolder.uploadTime, sArticle.getCreateTime());

        // 加载图片
        ImageUtil.setImage(viewHolder.icon, sArticle.getHead_url(), R.drawable.ic_launcher, viewHolder.icon);

        setItemAnim(sArticle, convertView, position);
        return convertView;
    }

    private class ViewHolder {
        NetworkImageView icon;
        TextView rePlyTip;
        TextView title;
        TextView uploadTime;
        TextView tip;
        TextView tip2;
        View deleteCollected;
    }

    @Override
    public int getLayoutId() {
        return R.layout.win_mycircle_article_collected_list_item;
    }

}
