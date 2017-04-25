package com.zxly.o2o.SnapScrollView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public abstract class BottomPage implements SnapPageLayout.McoySnapPage{
	
	protected  Context context;
	
	private View rootView = null;

	
	public BottomPage(Context context) {
		this.context = context;
		this.rootView = LayoutInflater.from(context).inflate(getLayoutId(),null);
	}
	protected abstract int getLayoutId();
	protected  View findViewById(int id){
		return this.rootView.findViewById(id);
	}
	@Override
	public View getRootView() {
		return rootView;
	}

	@Override
	public boolean isAtTop() {
		return true;
	}


	@Override
	public boolean isAtBottom() {

        return false;
	}

}
