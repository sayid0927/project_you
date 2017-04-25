/*
 * 文件名：ArticleTagItemAdapter.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： ArticleTagItemAdapter.java
 * 修改人：wuchenhui
 * 修改时间：2015-1-23
 * 修改内容：新增
 */
package com.zxly.o2o.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxly.o2o.activity.MyCircleSecondAct;
import com.zxly.o2o.model.ShopArticle;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.MyCircleRequest;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.DesityUtil;
import com.zxly.o2o.util.UMengAgent;
import com.zxly.o2o.util.ViewUtils;

import java.util.List;

/**
 * TODO 添加类的一句话简单描述。
 * <p>
 * TODO 详细描述
 * <p>
 * TODO 示例代码
 * <pre>
 * </pre>
 * 
 * @author     wuchenhui
 * @version    YIBA-O2O 2015-1-23
 * @since      YIBA-O2O
 */
public class ArticleTagItemAdapterV1 extends ObjectAdapter {

	private Activity activity;
	public ArticleTagItemAdapterV1(Activity activity) {
		super(activity);
		this.activity=activity;
	}

	public void refreshListItem(List<ShopArticle> articleList) {
		if (articleList != null && articleList.size() > 0) {
			clear();
			addItem(articleList);
		}
		notifyDataSetChanged();
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ShopArticle info=(ShopArticle) getItem(position);
		
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflateConvertView();
			viewHolder.layout=(LinearLayout) convertView.findViewById(R.id.tag_article_item_layout);
		    viewHolder.title=(TextView) convertView.findViewById(R.id.tag_article_title);
		    viewHolder.typeName=(TextView) convertView.findViewById(R.id.tag_article_type);
		    viewHolder.replyCount=(TextView) convertView.findViewById(R.id.tag_article_replycount);
		    viewHolder.botLine=convertView.findViewById(R.id.tag_article_bot_line);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				MyCircleRequest.shopArticle = (ShopArticle) getItem(position);
				MyCircleSecondAct.MyCircleLunch(activity, Constants.ARTICLE_DETAIL,"文章详情");
				UMengAgent.onEvent(activity, UMengAgent.tag_article_item);
				Log.d("adapter", "pos=="+position);				
			}
		});
		
		if("新手必看".equals(info.getTypeName())){
			viewHolder.typeName.setTextColor(activity.getResources().getColor(R.color.light_yellow));
		}else if("本店热文".equals(info.getTypeName())){
			viewHolder.typeName.setTextColor(activity.getResources().getColor(R.color.light_green));
		}else if("手机保养".equals(info.getTypeName())){
			viewHolder.typeName.setTextColor(activity.getResources().getColor(R.color.light_blue));
		}else{
			viewHolder.typeName.setTextColor(activity.getResources().getColor(R.color.orange));
		}		
        viewHolder.typeName.setPadding(DesityUtil.dp2px(activity, 2), DesityUtil.dp2px(activity, 1),
		                               DesityUtil.dp2px(activity, 6), DesityUtil.dp2px(activity, 1));

		ViewUtils.setText(viewHolder.typeName,"【"+info.getTypeName()+"】");
		ViewUtils.setText(viewHolder.title, info.getTitle());
		ViewUtils.setText(viewHolder.replyCount, info.getReplyAmount());
		
		if(position==getCount()-1){
			viewHolder.botLine.setVisibility(View.GONE);
		}else{
			viewHolder.botLine.setVisibility(View.VISIBLE);
		}
		
		
		return convertView;
	}


	@Override
	public int getLayoutId() {
		return R.layout.item_tag_article_v1;
	}
	
	private class ViewHolder {
		LinearLayout layout;
		TextView typeName;
		TextView title;
		TextView replyCount;
		View botLine;
	}

}
