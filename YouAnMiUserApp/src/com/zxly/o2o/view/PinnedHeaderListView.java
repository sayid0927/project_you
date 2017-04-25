package com.zxly.o2o.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListAdapter;

import java.lang.ref.WeakReference;

/**
 * 支持ListView置顶功能
 */
public class PinnedHeaderListView extends FixedListView {
	private static final int RECOMMEND = 0;
	private static final int APP = 1;
	private static final int GAME = 2;
	private static final int RANK = 3;
	private float x=0,y=0;
	private View mHeaderView;
	private Context mContext;
	private String mNodeCodes;
	private int mMeasuredWidth;
	private int mMeasuredHeight;
	private boolean mDrawFlag = true;
	private PinnedHeaderAdapter mPinnedHeaderAdapter;
	
	public PinnedHeaderListView(Context context) {
		super(context);
	}
	
	public void setContext(Context context) {
		mContext = new WeakReference<Context>(context).get();
	}
	
	public PinnedHeaderListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public PinnedHeaderListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	
	/**
	 * 设置置顶的Header View
	 * 
	 * @param pHeader
	 */
	public void setPinnedHeader(View pHeader) {
		mHeaderView = pHeader;
		requestLayout();
	}
	
	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		mPinnedHeaderAdapter = (PinnedHeaderAdapter) adapter;
	}
	
	// 三个覆写方法负责在当前窗口显示inflate创建的Header View
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		if (null != mHeaderView) {
			measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
			mMeasuredWidth = mHeaderView.getMeasuredWidth();
			mMeasuredHeight = mHeaderView.getMeasuredHeight();
		}
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (null != mHeaderView) {
			mHeaderView.layout(0, 0, mMeasuredWidth, mMeasuredHeight);
			controlPinnedHeader(getFirstVisiblePosition());
		}
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (null != mHeaderView && mDrawFlag) {
			drawChild(canvas, mHeaderView, getDrawingTime());
		}
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(ev.getY() < mHeaderView.getBottom() && !getHeaderGone()) {
		    if(ev.getAction() == MotionEvent.ACTION_DOWN){
		        x=ev.getX();
		        y=ev.getY();
		        return true;
		    }
		    if((Math.abs(ev.getY()-y)>2)||(Math.abs(ev.getX()-x)>2)){
		        return true;
		    }
			if(ev.getAction() == MotionEvent.ACTION_UP) {
				int id = RECOMMEND;
				float positionX = ev.getX();
				int width = getWidth()/4;
				if(positionX>0 && positionX<=width) {
					id = RECOMMEND;
				} else if(positionX>width && positionX<=width*2) {
					id = APP;
				} else if(positionX>width*2 && positionX<=width*3) {
					id = GAME;
				} else {
					id = RANK;
				}
				Intent intent = null;
				String classId = null;
				String classId2 = null;
				String title = null;
				String title2 = null;
//				switch(id) {
//				case RECOMMEND:
//					UMengAgent.onEvent(mContext,UMengAgent.GENERAL_BOUTIQUE_RECOMMEND);
//					intent = new Intent(mContext, RecommendActivity.class);
//			        intent.putExtra(Constants.EXTRA_TITLE, mContext.getString(R.string.title_tab_recommend));
//					intent.putExtra(Constants.EXTRA_CLASS_ID, Constants.CLASSID_BOTIQUE_RECOMMEND);
//					break;
//				case RANK:
//					UMengAgent.onEvent(mContext,UMengAgent.GENERAL_BOUTIQUE_RANK);
//					try {
//						String[] str = mNodeCodes.split(",");
//						String[] str2 = str[3].split("@");
//						String[] strFirst = str2[1].split(";");
//						String[] strSecond = str2[2].split(";");
//						classId = strFirst[0];
//						classId2 = strSecond[0];
//						title = strFirst[1];
//						title2 = strSecond[1];
//					} catch(Exception e) {}
//					intent = new Intent(mContext, CommonSubActivity.class);
//					intent.putExtra(Constants.EXTRA_TAB1, (null != title) ? title : mContext.getString(R.string.boutique_tab_game));
//					intent.putExtra(Constants.EXTRA_TAB2, (null != title2) ? title2 : mContext.getString(R.string.boutique_tab_soft));
//			        intent.putExtra(Constants.EXTRA_TITLE, mContext.getString(R.string.title_tab_rank));
//			        intent.putExtra(Constants.EXTRA_CLASS_ID, (null != classId) ? classId : Constants.CLASSID_BOTIQUE_RANK_GAME);
//			        intent.putExtra(Constants.EXTRA_CLASS_ID2, (null != classId2) ? classId2 : Constants.CLASSID_BOTIQUE_RANK_SOFT);
//					break;
//				case GAME:
//					UMengAgent.onEvent(mContext,UMengAgent.GENERAL_BOUTIQUE_GAME);
//					try {
//						String[] str = mNodeCodes.split(",");
//						String[] str2 = str[2].split("@");
//						String[] strFirst = str2[1].split(";");
//						String[] strSecond = str2[2].split(";");
//						classId = strFirst[0];
//						classId2 = strSecond[0];
//						title = strFirst[1];
//						title2 = strSecond[1];
//					} catch(Exception e) {}
//					intent = new Intent(mContext, CommonSubActivity.class);
//					intent.putExtra(Constants.EXTRA_TAB1, (null != title) ? title : mContext.getString(R.string.boutique_tab_hot));
//					intent.putExtra(Constants.EXTRA_TAB2, (null != title2) ? title2 : mContext.getString(R.string.boutique_tab_up));
//			        intent.putExtra(Constants.EXTRA_TITLE, mContext.getString(R.string.title_tab_game));
//			        intent.putExtra(Constants.EXTRA_CLASS_ID, (null != classId) ? classId : Constants.CLASSID_BOTIQUE_GAME_HOT);
//			        intent.putExtra(Constants.EXTRA_CLASS_ID2, (null != classId2) ? classId2 : Constants.CLASSID_BOTIQUE_GAME_UP);
//					break;
//				case APP:
//					UMengAgent.onEvent(mContext,UMengAgent.GENERAL_BOUTIQUE_APP);
//					try {
//						String[] str = mNodeCodes.split(",");
//						String[] str2 = str[1].split("@");
//						String[] strFirst = str2[1].split(";");
//						String[] strSecond = str2[2].split(";");
//						classId = strFirst[0];
//						classId2 = strSecond[0];
//						title = strFirst[1];
//						title2 = strSecond[1];
//					} catch(Exception e) {}
//					intent = new Intent(mContext, CommonSubActivity.class);
//					intent.putExtra(Constants.EXTRA_TAB1, (null != title) ? title : mContext.getString(R.string.boutique_tab_hot));
//					intent.putExtra(Constants.EXTRA_TAB2, (null != title2) ? title2 : mContext.getString(R.string.boutique_tab_up));
//			        intent.putExtra(Constants.EXTRA_TITLE, mContext.getString(R.string.title_tab_app));
//			        intent.putExtra(Constants.EXTRA_CLASS_ID, (null != classId) ? classId : Constants.CLASSID_BOTIQUE_APP_HOT);
//			        intent.putExtra(Constants.EXTRA_CLASS_ID2, (null != classId2) ? classId2 : Constants.CLASSID_BOTIQUE_APP_UP);
//					break;
//				}
				mContext.startActivity(intent);
			}
			return true;
		}
		return super.dispatchTouchEvent(ev);
	}
	
	public void setNodeCodes(String nodeCodes) {
		mNodeCodes = nodeCodes;
	}
	
	/**
	 * 获取header是否可见
	 */
	private boolean getHeaderGone() {
		int pinnedHeaderState = mPinnedHeaderAdapter.getPinnedHeaderState(getFirstVisiblePosition());
		return pinnedHeaderState == PinnedHeaderAdapter.PINNED_HEADER_GONE;
	}
	
	/**
	 * HeaderView三种状态的具体处理
	 * 
	 * @param position
	 */
	public void controlPinnedHeader(int position) {
		if (null == mHeaderView) {
			return;
		}
		
		int pinnedHeaderState = mPinnedHeaderAdapter.getPinnedHeaderState(position);
		switch (pinnedHeaderState) {
		case PinnedHeaderAdapter.PINNED_HEADER_GONE:
			mDrawFlag = false;
			break;

		case PinnedHeaderAdapter.PINNED_HEADER_VISIBLE:
			mPinnedHeaderAdapter.configurePinnedHeader(mHeaderView, position, 0);
			mDrawFlag = true;
			mHeaderView.layout(0, 0, mMeasuredWidth, mMeasuredHeight);
			break;
			
		case PinnedHeaderAdapter.PINNED_HEADER_PUSHED_UP:
			mPinnedHeaderAdapter.configurePinnedHeader(mHeaderView, position, 0);
			mDrawFlag = true;
			
			// 移动位置
			View topItem = getChildAt(0);
			
			if (null != topItem) {
				int bottom = topItem.getBottom();
				int height = mHeaderView.getHeight();
				
				int y;
				if (bottom < height) {
					y = bottom - height;
				}else {
					y = 0;
				} 

				if (mHeaderView.getTop() != y) {
					mHeaderView.layout(0, 0, mMeasuredWidth, mMeasuredHeight);
				}
	
			}			
			break;
		}
		
	}
	
	public interface PinnedHeaderAdapter {
		
		public static final int PINNED_HEADER_GONE = 0;
		
		public static final int PINNED_HEADER_VISIBLE = 1;
		
		public static final int PINNED_HEADER_PUSHED_UP = 2;
		
		int getPinnedHeaderState(int position);
		
		void configurePinnedHeader(View headerView, int position, int alpaha);
	}
}
