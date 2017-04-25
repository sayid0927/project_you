package com.easemob.easeui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.easemob.chatuidemo.HXApplication;
import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.R;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.controller.EaseUI;


/**
 *     @author dsnx  @version 创建时间：2015-1-15 下午1:39:49    类说明: 
 */
public abstract class EaseBaseDialog {

	protected Dialog dialog;
	protected Window dialogWindow;
	protected boolean isShow = false;
	private View contentView;
	protected Context context;

	public EaseBaseDialog(Context context) {
		this.context = context;
		dialog = new Dialog(context, R.style.ease_dialog);
		dialogWindow = this.dialog.getWindow();
		dialogWindow.setGravity(getGravity());
		dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
		setHeightAndWidth();
		contentView = createContentView();
		initView();
		if(isShowAnimation())
		initAnim();
	}

	public EaseBaseDialog(Context context,int Gravity) {
		this.context = context;
		dialog = new Dialog(context, R.style.ease_dialog);
		dialogWindow = this.dialog.getWindow();
		dialogWindow.setGravity(Gravity);
		dialogWindow.getDecorView().setPadding((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,20,
				EaseUI.displayMetrics)
				, 0,
				(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,20,
						EaseUI.displayMetrics), 0);
		setHeightAndWidth();
		contentView = createContentView();
		initView();
		if(isShowAnimation())
			initAnim();
	}


	protected  void setHeightAndWidth()
	{
		WindowManager.LayoutParams lp;
		lp = dialogWindow.getAttributes();
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		dialogWindow.setAttributes(lp);
	}
	public void initAnim() {
		dialogWindow.setWindowAnimations(R.style.ease_dialogWindowAnim);
	}

	abstract protected void initView();

	abstract public int getLayoutId();


	
	
	/***
	 * 默认显示动画
	 * @return
	 */
	protected boolean isShowAnimation() {
		return true;
	}

	protected View findViewById(int id) {
		return contentView.findViewById(id);
	}

	protected View createContentView() {
		return LayoutInflater.from(context).inflate(getLayoutId(), null);
	}

	public void show() {
		if (dialog.isShowing())
			return;
		dialog.getWindow().setContentView(contentView);
		dialog.show();
		isShow = true;

		this.dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				isShow = false;
				doOnDismiss();
			}
		});
	}

	
	protected void doOnDismiss() {
	}

	public void dismiss() {
		this.dialog.cancel();
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


}
