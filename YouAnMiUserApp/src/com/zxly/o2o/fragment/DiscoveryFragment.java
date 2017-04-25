package com.zxly.o2o.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;

import com.zxly.o2o.activity.SearchProductAct;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.ApkInfoUtil;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.viewpagerindicator.MPagerSlidingTab;
import com.zxly.o2o.viewpagerindicator.ViewPageFragmentAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("InflateParams")
public class DiscoveryFragment extends BaseFragment implements OnClickListener {

//	public static final int TYPE_ARTICLE=0;
	public static final int TYPE_MOBILEPHONE=1;
	public static final int TYPE_PARTS=2;
	public static  final int TYPE_PAD=3;

	private MPagerSlidingTab tabs;
	List<Fragment> fragments;
	public int curStatus;
	public int curTab;
	private View btnSearch;
	ViewPager pager;
	public static boolean turnToProductList=false;
	public static boolean turnToArticleList=false;
	String homeUrl;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initView() {

		fragments = new ArrayList<Fragment>();
//		String homeUrl=Config.h5Url+"/multiTemp/view/discovery.html?shopId="+ Config.shopId+"&baseUrl="+Config.dataBaseUrl+
//				      "&Authorization="+ PreferUtil.getInstance().getLoginToken();

		if((PreferUtil.getInstance().getH5VersionCode()> ApkInfoUtil.getDefaultH5Version(getActivity())||
				PreferUtil.getInstance().getH5StyleId()!=ApkInfoUtil.getDefaultH5StyleId(getActivity()))&&
				new File(Constants.H5_PROJECT_PATH).exists()){
			homeUrl="file:///"+Constants.H5_PROJECT_PATH+"/discovery.html?shopId="+Config.shopId+"&baseUrl="+DataUtil.encodeBase64(Config.dataBaseUrl);
		}else {
			homeUrl="file://"+Config.getH5CachePath(getActivity())+"discovery.html?shopId="+ Config.shopId+"&baseUrl="+DataUtil.encodeBase64(Config.dataBaseUrl);
//			homeUrl="file:///android_asset/multiTemp/discovery.html?shopId="+ Config.shopId+"&baseUrl="+DataUtil.encodeBase64(Config.dataBaseUrl);
		}

		Log.d("downLoad", "homeUrl-->" + homeUrl);

//		String homeUrl="http://192.168.1.7/multiTemp/view/discovery.html?shopId="+ Config.shopId+"&baseUrl="+Config.dataBaseUrl+
//				"&Authorization="+ PreferUtil.getInstance().getLoginToken();
//		fragments.add(RecommendWebFragment.newInstance(homeUrl));
		fragments.add(RecommendProductFragment.newInstance(TYPE_MOBILEPHONE));
		fragments.add(RecommendProductFragment.newInstance(TYPE_PARTS));
		fragments.add(RecommendProductFragment.newInstance(TYPE_PAD));
//		String strings[] = {"精选", "手机", "配件","平板"};
		String strings[] = {"手机", "配件","平板"};

//		curStatus=TYPE_ARTICLE;
		curStatus=TYPE_MOBILEPHONE;
		curTab=0;
		switch (curStatus) {
			case TYPE_MOBILEPHONE :
				curTab=0;
				break;
			case TYPE_PARTS :
				curTab=1;
				break;
			case TYPE_PAD :
				curTab=2;
				break;
			default :
				break;
		}

		pager = (ViewPager) findViewById(R.id.pager);
		//((FixedViewPager)pager).setTouchIntercept(false);
		tabs = (MPagerSlidingTab) findViewById(R.id.tabs);
		btnSearch=findViewById(R.id.btn_search);
		btnSearch.setOnClickListener(this);
		pager.setAdapter(new ViewPageFragmentAdapter(getActivity().getSupportFragmentManager(), fragments, strings));
		tabs.setViewPager(pager);
		setTabsValue();
		pager.setCurrentItem(curTab);
		pager.setOffscreenPageLimit(5);

		tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int i, float v, int i1) {
			}

			@Override
			public void onPageSelected(int i) {
				curTab=i;
			}

			@Override
			public void onPageScrollStateChanged(int i) {
			}
		});



	}

	@Override
	public void onResume() {
		super.onResume();
		if(turnToProductList){
			pager.setCurrentItem(1);
			turnToProductList=false;
		}
		if(turnToArticleList){
			pager.setCurrentItem(0);
			turnToArticleList=false;
		}
	}


	@Override
	protected int layoutId() {
		return R.layout.win_discovery;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

			case R.id.btn_shopInfo:


				break;
			case R.id.btn_search:
				switch (curTab)
				{
//					case TYPE_ARTICLE:
//						SearchArticleAct.start(this.getActivity());
//						break;
					case 0:
						SearchProductAct.start(this.getActivity(),SearchProductAct.TYPE_PRODUCT);
						break;
					case 1:
						SearchProductAct.start(this.getActivity(),SearchProductAct.TYPE_PARTS);
						break;
					case 2:
						SearchProductAct.start(this.getActivity(),SearchProductAct.TYPE_PAD);
						break;
				}

				break;
			default:
				break;
		}

	}


	/**
	 * 对PagerSlidingTabStrip的各项属性进行赋值。
	 */
	private void setTabsValue() {
		// 设置Tab是自动填充满屏幕的
		tabs.setShouldExpand(true);
		// 设置Tab的分割线颜色
		tabs.setDividerColor(getResources().getColor(R.color.white));
		// 设置Tab底部线的高度
		tabs.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, Config.displayMetrics));
//        // 设置Tab Indicator的高度
		tabs.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2.5f, Config.displayMetrics));
//        // 设置Tab标题文字的大小
		tabs.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, Config.displayMetrics));
		// 设置Tab Indicator的颜色
		tabs.setIndicatorColor(Color.parseColor("#ff5f19"));
		// 设置选中Tab文字的颜色 (这是我自定义的一个方法)
		tabs.setSelectedTextColor(Color.parseColor("#ff5f19"));
		// 取消点击Tab时的背景色
		tabs.setTabBackground(R.color.transparent);
		tabs.initTabStyles();
	}




}
