package com.zxly.o2o.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;

public class ShakeAnimation extends RelativeLayout {

	private boolean mNeedShake = false;
	private boolean mStartShake = false;

	private static final int ICON_WIDTH = 80;
	private static final int ICON_HEIGHT = 94;
	private static final float DEGREE_0 = 1.0f;
	private static final float DEGREE_1 = -1.2f;
	private static final float DEGREE_2 = 1.2f;
	private static final float DEGREE_3 = -0.7f;
	private static final float DEGREE_4 = 0.7f;
	private static final int ANIMATION_DURATION = 80;
	private PaintFlagsDrawFilter mSetfil;
	private int mCount = 0;

	float mDensity;

	/** Called when the activity is first created. */
	public ShakeAnimation(Context context) {
		super(context);
	}

	public ShakeAnimation(Context context, AttributeSet attrs) {
		super(context, attrs);
		mSetfil = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
	}

	public ShakeAnimation(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.setDrawFilter(mSetfil);
		super.onDraw(canvas);
	}

	public void startShakeAnim(float density) {
		mDensity = density;
		if (!mStartShake) {
			mStartShake = true;
			mNeedShake = true;
			shakeAnimation(this);
		}
	}

	private void shakeAnimation(final View v) {
		float rotate = 0;
		int c = mCount++ % 5;
		if (c == 0) {
			rotate = DEGREE_0;
		} else if (c == 1) {
			rotate = DEGREE_1;
		} else if (c == 2) {
			rotate = DEGREE_2;
		} else if (c == 3) {
			rotate = DEGREE_3;
		} else {
			rotate = DEGREE_4;
		}
		final RotateAnimation mra = new RotateAnimation(rotate, -rotate, ICON_WIDTH * mDensity / 2,
				ICON_HEIGHT * mDensity / 2);
		final RotateAnimation mrb = new RotateAnimation(-rotate, rotate, ICON_WIDTH * mDensity / 2,
				ICON_HEIGHT * mDensity / 2);

		mra.setDuration(ANIMATION_DURATION);
		mrb.setDuration(ANIMATION_DURATION);

		mra.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				if (mNeedShake) {
					mra.reset();
					v.startAnimation(mrb);
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationStart(Animation animation) {

			}

		});

		mrb.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				if (mNeedShake) {
					mrb.reset();
					v.startAnimation(mra);
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationStart(Animation animation) {

			}

		});
		v.startAnimation(mra);
	}

	public void stopShakeAnim() {
		mNeedShake = false;
		mCount = 0;
		mStartShake = false;
	}

}