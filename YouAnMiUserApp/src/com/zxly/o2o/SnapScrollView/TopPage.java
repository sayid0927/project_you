package com.zxly.o2o.SnapScrollView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public abstract class TopPage implements SnapPageLayout.McoySnapPage {
	
	protected Context context;

	private View rootView = null;

	public TopPage(Context context) {
		this.context = context;
		this.rootView = LayoutInflater.from(context).inflate(getLayoutId(),null);
	}

	protected abstract int getLayoutId();

	@Override
	public View getRootView() {
		return rootView;
	}
	protected  View findViewById(int id){
		return this.rootView.findViewById(id);
	}

	@Override
	public boolean isAtTop() {
		return true;
	}

	@Override
	public boolean isAtBottom() {
		return true;
	}

}
