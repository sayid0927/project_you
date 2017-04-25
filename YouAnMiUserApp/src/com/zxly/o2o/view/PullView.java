package com.zxly.o2o.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

public abstract class PullView extends LinearLayout {
	protected Animation upAnimation;
	protected Animation pullAnimation;
	protected PullView curView;
	protected long lastExecTime = 0;
	protected Object selItem;
	protected TextView txtView;
	protected ImageView imgView;
	protected boolean isExpan = false;
	protected FrameLayout content;
	protected ViewGroup spinnerContent;
	private int turnUp,turnDown, textColor,textDefColor;
	private View linV;
	private float textSize;
	

	public PullView(Context context) {
		super(context);
		init();
	}

	public PullView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PullView);
		 textSize=a.getDimension(R.styleable.PullView_textSize, getResources().getDimension(R.dimen.text_size_16sp));
		 textDefColor=getResources().getColor(R.color.orange_ff5f19);
		 textColor=a.getColor(R.styleable.PullView_textColor,textDefColor);
		 turnUp=a.getResourceId(R.styleable.PullView_turnUp, R.drawable.turn_up);
		 turnDown=a.getResourceId(R.styleable.PullView_turnDown, R.drawable.turn_pull);
		 a.recycle();
		init();

	}

	public PullView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PullView);
		 textSize=a.getDimension(R.styleable.PullView_textSize, getResources().getDimension(R.dimen.text_size_16sp));
		 textDefColor=getResources().getColor(R.color.orange_ff5f19);
		 textColor=a.getColor(R.styleable.PullView_textColor,textDefColor);
		 turnUp=a.getResourceId(R.styleable.PullView_turnUp, R.drawable.turn_up);
		 turnDown=a.getResourceId(R.styleable.PullView_turnDown, R.drawable.turn_pull);
		 a.recycle();
		init();
	}
	public Object getSelItem() {
		return selItem;
	}
	public void goneVline()
	{
		ViewUtils.setGone(linV);
	}
	protected void init()
	{
		inflate(getContext(), R.layout.view_turn, this);
		linV=findViewById(R.id.lin_v);
		spinnerContent = (ViewGroup) inflate(getContext(),spinnerContentLayoutId(), null);
		txtView = (TextView) findViewById(R.id.turn_txt);
		imgView = (ImageView) findViewById(R.id.turn_img);
		txtView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
		txtView.setTextColor(textColor);
		imgView.setBackgroundResource(turnDown);
		spinnerContent.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isExpan) {
					up();
				}

			}
		});
		upAnimation = AnimationUtils.loadAnimation(getContext(),
				R.anim.push_up_out);
		pullAnimation = AnimationUtils.loadAnimation(getContext(),
				R.anim.push_up_in);
		pullAnimation.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				if (curView != null && curView != PullView.this) {
					curView.up();
				}

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {

			}
		});
		upAnimation.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				post(new Runnable() {

					@Override
					public void run() {
						PullView view = (PullView) content.getTag();
						if (view == PullView.this) {
							content.setVisibility(View.GONE);
						}
						content.removeView(spinnerContent);

					}
				});

			}
		});
		setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				long curTime = System.currentTimeMillis();
				if (curTime - lastExecTime > 500) {
					if (isExpan) {
						up();
					} else {
						pull();
					}
					lastExecTime = curTime;
				}

			}
		});
	}

	public void setText(String str) {
		if (!StringUtil.isNull(str)) {
			txtView.setText(str);
		}
	}
	public void setContent(FrameLayout content) {
		if (content != null) {
			this.content = content;
		}
	}
	public abstract int spinnerContentLayoutId();

	public boolean getIsExpan(){
		return this.isExpan;
	}
	public void up() {
		isExpan = false;

		if(textColor!=textDefColor)
		{
			if(topSelect) {
				imgView.setBackgroundResource(turnDown);
				txtView.setTextColor(textColor);
			} else {
				imgView.setBackgroundResource(R.drawable.class_arrow_down_light);
				txtView.setTextColor(textDefColor);
			}
		}else
		{
			imgView.setBackgroundResource(turnDown);
			txtView.setTextColor(textDefColor);
		}
		
	}

	private boolean topSelect = false;
	public void setTopSelect(boolean topSelect){
		this.topSelect = topSelect;
	}

	public void pull() {
		isExpan = true;
		curView = (PullView) this.content.getTag();
		imgView.setBackgroundResource(turnUp);
		content.setTag(this);
		txtView.setTextColor(getResources().getColor(R.color.orange_ff5f19));
		this.content.setVisibility(View.VISIBLE);
		if (spinnerContent.getParent() == null) {
			this.content.addView(spinnerContent);
		} else {
			spinnerContent.setVisibility(View.VISIBLE);
		}
		
	}
	protected OnSelChangeListener onSelChangeListener;

	public void setOnSelChangeListener(OnSelChangeListener onSelChangeListener) {
		this.onSelChangeListener = onSelChangeListener;
	}

	public interface OnSelChangeListener {
		public void onSelChange();
	}
}
