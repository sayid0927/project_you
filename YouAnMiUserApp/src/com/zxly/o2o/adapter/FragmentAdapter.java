/**
 * Copyright(C)2012-2013 深圳市掌星立意科技有限公司版权所有
 * 创 建 人:Jacky
 * 修 改 人:wuchenhui
 * 创 建日期:2013-7-19
 * 描    述:
 * 版 本 号:
 */
package com.zxly.o2o.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Fragment适配
 */
public class FragmentAdapter extends FragmentPagerAdapter {
	private ArrayList<Fragment> mFragmentsList;
	public FragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	public FragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
		super(fm);
		mFragmentsList = fragments;
	}

	@Override
	public int getCount() {
		return mFragmentsList.size();
	}

	@Override
	public Fragment getItem(int position) {
		return mFragmentsList.get(position);
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

}
