/*
 * 文件名：StorePictureBrowseAct.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： StorePictureBrowseAct.java
 * 修改人：wuchenhui
 * 修改时间：2015-1-7
 * 修改内容：新增
 */
package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO 添加类的一句话简单描述。
 * <p>
 * TODO 详细描述
 * <p>
 * TODO 示例代码
 * 
 * <pre>
 * </pre>
 * 
 * @author wuchenhui
 * @version YIBA-O2O 2015-1-7
 * @since YIBA-O2O
 */
public class StorePictureBrowseAct extends BasicAct {

    private ViewPager mPager;
   
	private List<NetworkImageView> bannerList = new ArrayList<NetworkImageView>();
	private PicBrowseAdapter  pricBrowseAdapter;
	private static String[] imageUrls;
	private TextView txtIconSum;
	private static int curItem;


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.win_store_picture_browse);
		mPager = (ViewPager) findViewById(R.id.win_store_picture_viewPager);
		txtIconSum=(TextView) findViewById(R.id.txt_icon_sum);
		pricBrowseAdapter=new PicBrowseAdapter();
		int imgLength=imageUrls.length;
		for(int i=0;i<imgLength;i++)
		{
			NetworkImageView imgView=new NetworkImageView(this);
			imgView.setImageUrl(imageUrls[i],AppController.getInstance().imageLoader);
			bannerList.add(imgView);
		}
		mPager.setAdapter(pricBrowseAdapter);
		mPager.setPageMargin(10);
		mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int index) {
				ViewUtils.setText(txtIconSum,(index+1)+"/"+imageUrls.length);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
		mPager.setCurrentItem(curItem);
		ViewUtils.setText(txtIconSum,(curItem+1)+"/"+imageUrls.length);
		
	}

	public static void start(Activity curAct,String[] urls)
	{
		start(curAct, urls,0);
	}
	public static void start(Activity curAct,String[] urls,int _curItem)
	{
		if(urls!=null)
		{
			imageUrls=urls;
			curItem=_curItem;
			Intent it = new Intent(curAct,StorePictureBrowseAct.class);
			ViewUtils.startActivity(it, curAct);
		}
		
	}
	
	
	@Override
	public void finish() {
		super.finish();
		imageUrls=null;
	}


	private class PicBrowseAdapter extends PagerAdapter {

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {

			container.addView(bannerList.get(position));

			return bannerList.get(position);
		}

		@Override
		public int getCount() {
			return bannerList.size();
		}

		@Override
		public void destroyItem(ViewGroup view, int position, Object object) {
			view.removeView(bannerList.get(position));
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}
}
