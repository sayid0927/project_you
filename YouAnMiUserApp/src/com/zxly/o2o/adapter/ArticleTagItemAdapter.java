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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxly.o2o.activity.MyCircleSecondAct;
import com.zxly.o2o.model.ShopArticle;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.MyCircleRequest;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.UMengAgent;
import com.zxly.o2o.util.ViewUtils;

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
public class ArticleTagItemAdapter extends ObjectAdapter implements OnClickListener {

	private Activity activity;
	public ArticleTagItemAdapter(Activity activity) {
		super(activity);
		this.activity=activity;
	}

	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ShopArticle info=(ShopArticle) getItem(position);
		
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflateConvertView();
			convertView.setOnClickListener(this);
		    viewHolder.title=(TextView) convertView.findViewById(R.id.txt_article_title);
		    viewHolder.txtArticleType =(TextView) convertView.findViewById(R.id.txt_article_type);
		    viewHolder.imgArticleStatus=(ImageView) convertView.findViewById(R.id.img_article_status);
		    viewHolder.botLine=convertView.findViewById(R.id.line_bot);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		fillData(viewHolder, info);
		
//		if(position==getCount()-1){
//			viewHolder.botLine.setVisibility(View.GONE);
//		}else{
//			viewHolder.botLine.setVisibility(View.VISIBLE);
//		}
				
		return convertView;
	}

	
	private void fillData(ViewHolder viewHolder,ShopArticle info){
	    viewHolder.info=info;
		viewHolder.txtArticleType.setText(info.getTypeName());
		ViewUtils.setText(viewHolder.title, info.getTitle());
	}

	@Override
	public int getLayoutId() {
		return R.layout.tag_article_list_item;
	}
	
	private class ViewHolder {
		TextView txtArticleType;
		ImageView imgArticleStatus;
		TextView title;
		View botLine;
		ShopArticle info;
	}

	@Override
	public void onClick(View v) {
		MyCircleRequest.shopArticle = ((ViewHolder) v.getTag()).info;
		MyCircleSecondAct.MyCircleLunch(activity, Constants.ARTICLE_DETAIL,"文章详情");
		UMengAgent.onEvent(activity, UMengAgent.tag_article_item);	
	}

}
