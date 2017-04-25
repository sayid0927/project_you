package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;

import com.zxly.o2o.view.SwipeLayout;

import java.util.HashSet;
import java.util.Set;

public abstract class SwipeBaseAdapter extends ObjectAdapter {

	protected Set<SwipeLayout> mShownLayouts = new HashSet<SwipeLayout>();

	public SwipeBaseAdapter(Context _context) {
		super(_context);
	}
	
	@Override
	public View inflateConvertView() {
		SwipeLayout sLayout=(SwipeLayout) super.inflateConvertView();
		sLayout.addSwipeListener(new SwipeMemory());
		mShownLayouts.add(sLayout);
		return sLayout;
	}

	class SwipeMemory implements SwipeLayout.SwipeListener{

		@Override
		public void onStartOpen(SwipeLayout layout) {
			closeAllExcept(layout);
		}

		@Override
		public void onOpen(SwipeLayout layout) {
			closeAllExcept(layout);
		}

		@Override
		public void onStartClose(SwipeLayout layout) {
			
		}

		@Override
		public void onClose(SwipeLayout layout) {
			
		}

		@Override
		public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
			
		}

		@Override
		public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
			
		}
    	 
     }
	public void closeAllExcept(SwipeLayout layout) {
        for (SwipeLayout s : mShownLayouts) {
            if (s != layout)
                s.close();
        }
    }

}
