package com.zxly.o2o.adapter;

/**
 * @author 作者huangbin:
 * @version 创建时间：2015-3-4 下午3:06:06
 * 类说明
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.easemob.easeui.widget.VolleyImageView;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.MyCircleThirdActAssi;
import com.zxly.o2o.model.ShopTopic;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.ImageUtil;
import com.zxly.o2o.util.ViewUtils;

import java.util.HashMap;

public class MyTopicsAdapter extends BasicMyCircleAdapter {
	private HashMap<Integer, Bitmap> cacheBitmaps = new HashMap<Integer, Bitmap>();

	public MyTopicsAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder ;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflateConvertView();
			viewHolder.icon = (VolleyImageView) convertView
					.findViewById(R.id.forum_community_list_item_icon);
			viewHolder.rePlyTip = (TextView) convertView
					.findViewById(R.id.forum_community_list_item_reply_tip);
			viewHolder.upTip = (TextView) convertView
					.findViewById(R.id.forum_community_list_item_up_tip);
			viewHolder.role = (TextView) convertView
					.findViewById(R.id.forum_community_list_item_role);
			viewHolder.rolePhoto = (NetworkImageView) convertView
					.findViewById(R.id.forum_community_list_item_role_photo);
			viewHolder.title = (TextView) convertView
					.findViewById(R.id.forum_community_list_item_title);
			viewHolder.uploadTime = (TextView) convertView
					.findViewById(R.id.forum_community_list_item_upload_time);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		ShopTopic sTopic = (ShopTopic) content.get(position);

		viewHolder.rePlyTip.setText(sTopic.getReplyAmout() + "");

		viewHolder.upTip.setText(sTopic.getPraiseAmout() + "");

        if(sTopic.getContent().length()==0){

            viewHolder.title.setVisibility(View.GONE);
        }else{
            viewHolder.title.setText(sTopic.getContent());
            viewHolder.title.setVisibility(View.VISIBLE);
        }


		viewHolder.role.setText(Account.user.getNickName());

		if (sTopic.getCreateTime() == 0) {
			if (position == 0 && MyCircleThirdActAssi.photoBitmap != null) {
				viewHolder.icon
						.setLocalImageBitmap(MyCircleThirdActAssi.photoBitmap);
				viewHolder.icon.setVisibility(View.VISIBLE);
				cacheBitmaps.put(position, MyCircleThirdActAssi.photoBitmap);
				MyCircleThirdActAssi.photoBitmap = null;
			} else if (cacheBitmaps.get(position) != null) {
				viewHolder.icon.setLocalImageBitmap(cacheBitmaps.get(position));
				viewHolder.icon.setVisibility(View.VISIBLE);
			} else {
				viewHolder.icon.setVisibility(View.GONE);
			}
		} else {
//			ImageUtil.setImage(viewHolder.icon, sTopic.getThum_image_url(), R.drawable.ic_launcher, viewHolder.icon);
		}
		
//		viewHolder.icon.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				showPic(((ShopTopic) content.get(position))
//						.getOrigin_image_url(), ((ShopTopic) content
//						.get(position)).getCreateTime());
//			}
//
//		});

		// 加载时间
		ViewUtils.setLastTime(viewHolder.uploadTime, sTopic.getCreateTime());

		// 加载图片
		ImageUtil.setImage(viewHolder.rolePhoto, Account.user.getThumHeadUrl(),
				R.drawable.luntan_photo, null);
		
		viewHolder.rolePhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				showPic( Account.user.getThumHeadUrl(), ((ShopTopic) content
//						.get(position)).getCreateTime());
			}

		});

		setItemAnim(sTopic, convertView, position);
		return convertView;
	}

	private class ViewHolder {

		VolleyImageView icon;
		TextView rePlyTip;
		TextView upTip;
		TextView role;
		TextView title;
		NetworkImageView rolePhoto;
		TextView uploadTime;
	}

	@Override
	public int getLayoutId() {
		return R.layout.win_forum_community_list_item;
	}

}
