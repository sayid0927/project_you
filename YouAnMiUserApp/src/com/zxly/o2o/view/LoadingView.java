package com.zxly.o2o.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.ViewUtils;

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
        progressAnima = AnimationUtils.loadAnimation(getContext(),
                R.anim.loading_progressbar_anim);
        LinearInterpolator lin = new LinearInterpolator();
        progressAnima.setInterpolator(lin);
        txtLoading = (TextView) findViewById(R.id.text_loading);
		btnLoading = findViewById(R.id.btn_loading);
		btnLoading.setOnClickListener(new View.OnClickListener() {

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
        imgProgress.startAnimation(progressAnima);

	}

	/**
	 * 数据加载成功切有数据时调用
	 */
	public void onLoadingComplete() {
		setVisibility(View.GONE);
        imgProgress.clearAnimation();
	}

	public void onLoadingFail() {
		onLoadingFail("加载失败，稍后加载", R.drawable.loading_empty);
	}

	public void onLoadingFail(String failMsg) {
		onLoadingFail(failMsg, R.drawable.loading_fial1);
	}

	public void onLoadingFail(String failMsg, boolean isLoading) {
		onLoadingFail(failMsg, R.drawable.loading_empty, isLoading);
	}

	/**
	 * 加载成功但没有数据时调用此函数
	 */
	public void onDataEmpty() {
		onDataEmpty("暂无数据!", false, R.drawable.loading_empty);
	}

    public void onDataEmpty(String emptyMsg) {
        onDataEmpty(emptyMsg,false,R.drawable.loading_empty);
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
        imgProgress.clearAnimation();
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
        imgProgress.clearAnimation();
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
