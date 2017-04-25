package com.zxly.o2o.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.zxly.o2o.config.Config;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.AppLog;
import com.zxly.o2o.util.ViewUtils;

/**
 *     @author dsnx  @version 创建时间：2015-1-15 下午1:39:49    类说明: 
 */
public abstract class BaseDialog {

	protected Dialog dialog;
	protected WindowManager.LayoutParams lp;
	protected Window dialogWindow;
	protected boolean isShow = false;
	protected View content;
	protected Context context;
	private ContentView contentView;

	public BaseDialog() {
		context = AppController.getInstance().getTopAct();
		dialog = new Dialog(context, R.style.dialog);
		dialogWindow = this.dialog.getWindow();
		lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(getGravity());
		contentView = new ContentView(context);
		content = createContentView();
		contentView.addView(content);
		dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				doOnDismiss();
			}
		});

		initView();
		
		if(isShowAnimation())
		initAnim();
	}

	public void initAnim() {
		dialogWindow.setWindowAnimations(R.style.dialogWindowAnim);
	}

	abstract protected void initView();

	abstract public int getLayoutId();

	/***
	 * 是否限定弹出框高度不超过标题栏，如果需要限定 需要重写 return ture
	 * 
	 * @return
	 */
	protected boolean isLimitHeight() {
		return false;
	}
	
	
	/***
	 * 默认显示动画
	 * @return
	 */
	protected boolean isShowAnimation() {
		return true;
	}

	protected View findViewById(int id) {
		return content.findViewById(id);
	}

	protected View createContentView() {
		return LayoutInflater.from(context).inflate(getLayoutId(), null);
	}

	public void cancelable(boolean cancelable)
	{
		dialog.setCanceledOnTouchOutside(cancelable);
		dialog.setCancelable(cancelable);
	}
	public void show(float heightScale, float widthScale) {
		show();
		lp.height = (int) (Config.screenHeight * heightScale);
		lp.width = (int) (Config.screenWidth * widthScale);
		dialogWindow.setAttributes(lp);
	}

	public void show() {
		if (dialog.isShowing())
			return;
		dialog.getWindow().setContentView(contentView);
		dialog.show();
		isShow = true;
	}

	public ViewGroup getContentView(){
		return contentView;
	}
	
	protected void doOnDismiss() {
	}

	public void dismiss() {
		this.dialog.cancel();
		AppController.cancelAll(this);
		isShow = false;
	}

	protected void okDismiss() {
		this.dialog.dismiss();
		isShow = false;
	}

	public boolean isShow() {
		return isShow;
	}

	public int getGravity() {
		return Gravity.BOTTOM;
	}
	
   public Context getContext(){
	   return context;
   }

	class ContentView extends LinearLayout {

		public ContentView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			if (isLimitHeight()) {
				int maxHeight = Config.screenHeight - ViewUtils.dpToPx(this.getResources(), 80);
				final View child = getChildAt(0);
				final int childHeight = child.getMeasuredHeight();
				if (childHeight > maxHeight) {
					lp.height = maxHeight;
				}
				lp.width = Config.screenWidth;
				dialogWindow.setAttributes(lp);
			}

		}

	}
}
