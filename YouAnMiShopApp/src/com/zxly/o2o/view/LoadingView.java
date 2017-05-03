package com.zxly.o2o.view;

import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ViewUtils;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.animation.Animation.AnimationListener;

/**
 * @author dsnx
 * @version YIBA-O2O 2015-3-24
 * @param <T>
 * @since YIBA-O2O
 */
public class LoadingView extends LinearLayout implements AnimationListener {
	private ImageView imgLoadResult,imgProgress;
	private TextView txtLoading;
	private View btnLoading;
	protected Animation progressAnima;
	private View viewLoading,viewLoadFail;
	private OnAgainListener onAgainListener;
	private AnimationDrawable animationDrawable;

	public LoadingView(Context context) {
		super(context);
		init();

	}

	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public LoadingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		inflate(getContext(), R.layout.view_loading, this);
		setVisibility(View.GONE);
		viewLoading=findViewById(R.id.loading);
		viewLoadFail=findViewById(R.id.view_loadFail);
		imgLoadResult = (ImageView) findViewById(R.id.img_loadResult);
		imgProgress=(ImageView)findViewById(R.id.img_progress);
		//原先加载图片
//		progressAnima = AnimationUtils.loadAnimation(getContext(),
//				R.anim.loading_progressbar_anim);
		//现在加载图片
		imgProgress.setImageResource(R.drawable.loading_ivset);
		animationDrawable = (AnimationDrawable) imgProgress.getDrawable();

//		LinearInterpolator lin = new LinearInterpolator();
//		progressAnima.setInterpolator(lin);
		txtLoading = (TextView) findViewById(R.id.text_loading);
		btnLoading = findViewById(R.id.btn_loading);
		btnLoading.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onAgainListener != null) {
					onAgainListener.onLoading();
				}
			}
		});
	}

	public void setBtnText(String btnText) {
		ViewUtils.setText(btnLoading, btnText);
	}

	/**
	 * 开始加载数据时调用此函数
	 */


	public void startLoading() {
		setVisibility(View.VISIBLE);
		ViewUtils.setGone(viewLoadFail);
		ViewUtils.setVisible(viewLoading);
//		imgProgress.startAnimation(progressAnima);
		animationDrawable.start();
	}

	/**
	 * 数据加载成功切有数据时调用
	 */
	public void onLoadingComplete() {
		setVisibility(View.GONE);
//		imgProgress.clearAnimation();
		animationDrawable.stop();
	}

	public void onLoadingFail() {
		onLoadingFail("您的手机网络不太顺畅哦!", R.drawable.img_default_shy);
	}

	public void onLoadingFail(String failMsg) {
		onLoadingFail(failMsg, R.drawable.img_default_shy);
	}

	public void onLoadingFail(String failMsg, boolean isLoading) {
		onLoadingFail(failMsg, R.drawable.img_default_shy, isLoading);
	}

	/**
	 * 加载成功但没有数据时调用此函数
	 */
	public void onDataEmpty() {
		onDataEmpty("暂无数据!", false, R.drawable.img_default_sad);
	}

	public void onDataEmpty(String emptyMsg) {
		onDataEmpty(emptyMsg,false,R.drawable.img_default_sad);
	}
	public void onDataEmpty(String emptyMsg, boolean showLoadBtn) {
		onDataEmpty(emptyMsg,showLoadBtn,R.drawable.img_default_sad);
	}
	public void onDataEmpty(String emptyMsg,int img_empty) {
		onDataEmpty(emptyMsg,false,img_empty);
	}
	public void onDataEmpty(String emptyMsg,boolean showLoadBtn,int img_empty) {
		setVisibility(View.VISIBLE);
		ViewUtils.setText(txtLoading, emptyMsg);
		ViewUtils.setGone(viewLoading);
		ViewUtils.setVisible(viewLoadFail);
		if(showLoadBtn)
		{
			ViewUtils.setVisible(btnLoading);
		}else
		{
			ViewUtils.setGone(btnLoading);
		}
//		imgProgress.clearAnimation();
		animationDrawable.stop();
		imgLoadResult.setImageDrawable(getResources().getDrawable(img_empty));
	}





	/**
	 * 数据加载失败是调用该函数
	 *
	 * @param failMsg
	 * @param failImg
	 */
	public void onLoadingFail(String failMsg, int failImg) {
		onLoadingFail(failMsg, failImg, true);
	}

	public void onLoadingFail(String failMsg, int failImg, boolean isLoading) {
		setVisibility(View.VISIBLE);
		ViewUtils.setGone(viewLoading);
		ViewUtils.setVisible(viewLoadFail);
		ViewUtils.setText(txtLoading, failMsg);
		if (isLoading) {
			ViewUtils.setVisible(btnLoading);
		} else {
			ViewUtils.setGone(btnLoading);
		}
//		imgProgress.clearAnimation();
		animationDrawable.stop();
		imgLoadResult.setImageDrawable(getResources().getDrawable(failImg));
	}

	public void setOnAgainListener(OnAgainListener onAgainListener) {
		this.onAgainListener = onAgainListener;
	}

	@Override
	public void onAnimationStart(Animation animation) {

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		viewLoading.clearAnimation();
	}

	@Override
	public void onAnimationRepeat(Animation animation) {

	}


	public interface OnAgainListener {
		public void onLoading();
	}

}
