package com.zxly.o2o.activity;

import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.easemob.easeui.ui.EaseBaseViewPageAct;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.fragment.ArticleFragment;
import com.zxly.o2o.shop.R;

/**
 * Created by Benjamin on 2015/7/9.
 */
public class NewsArticleAct extends EaseBaseViewPageAct {
    @Override
    protected void initView() {
        addBackBtn();
        setTabParam();
        fragments.add(ArticleFragment.newInstance(3));
        fragments.add(ArticleFragment.newInstance(4));

    }

    private void setTabParam() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, AppController.displayMetrics));
        lp.setMargins((int) TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 55,
                                AppController.displayMetrics), 0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 55,
                        AppController.displayMetrics), 0);
        //        tabs.setTabMargin(50);
        tabs.setLayoutParams(lp);
        tabs.setIsShowDivider(false);
//        tabs.setDividerColor(getResources().getColor(R.color.transparent));
        setDividerColor(R.color.transparent);
        setTabBackground(R.color.transparent);
//                tabs.setTabBackground(getResources().getColor(R.color.transparent));
    }

    private void addBackBtn() {
        ImageView backBtn = new ImageView(NewsArticleAct.this);
        backBtn.setImageResource(R.drawable.btn_back_selector);
        backBtn.setScaleType(ImageView.ScaleType.CENTER);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsArticleAct.this.finish();
            }
        });
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, AppController.displayMetrics),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45,
                        AppController.displayMetrics));
        addContentView(backBtn, layoutParams);
    }

    @Override
    protected String[] tabName() {
        return new String[]{"行业新闻", "业界培训"};
    }
}
