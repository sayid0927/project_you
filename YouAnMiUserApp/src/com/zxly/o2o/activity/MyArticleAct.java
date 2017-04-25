package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.fragment.MyCircleArticleListFragment;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.ViewUtils;

/**
 * Created by Administrator on 2015/8/10.
 */
public class MyArticleAct extends BaseViewPageAct {
    private int pageType;

    public static void start(Activity curAct, int pageType) {
        Intent intent = new Intent(curAct, MyArticleAct.class);
        intent.putExtra("pageType", pageType);
        ViewUtils.startActivity(intent, curAct);
    }

    @Override
    protected void initView() {
        setTabTextSize(16);
        addBackBtn();
        setTabParam();
        for (int i = 0; i < 3; i++) {
            fragments.add(MyCircleArticleListFragment.newInstance(i+1));
        }
        pageType = getIntent().getIntExtra("pageType", -1);

}

    @Override
    protected void onResume() {
        super.onResume();
        if(pageType!=-1) {
            pager.setCurrentItem(pageType - 1);
        }
    }

    @Override
    protected String[] tabName() {
        return new String[]{"新手必看", "手机保养", "本店热文"};
    }

    private void addBackBtn() {
        ImageView backBtn = new ImageView(MyArticleAct.this);
        backBtn.setImageResource(R.drawable.button_back_selector);
        backBtn.setScaleType(ImageView.ScaleType.CENTER);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyArticleAct.this.finish();
            }
        });
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, AppController.displayMetrics),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48,
                        AppController.displayMetrics));
        addContentView(backBtn, layoutParams);
    }

    private void setTabParam() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, AppController.displayMetrics));
        lp.setMargins((int) TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45,
                                AppController.displayMetrics), 0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45,
                        AppController.displayMetrics), 0);
        tabs.setLayoutParams(lp);
        tabs.setIsShowDivider(false);
        setDividerColor(R.color.transparent);
        setTabBackground(R.color.transparent);
    }
}
