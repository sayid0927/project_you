package com.zxly.o2o.activity;

import android.app.ActionBar;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easemob.easeui.ui.EaseBaseViewPageAct;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.fragment.ArticleFragment;
import com.zxly.o2o.shop.R;

/**
 * Created by Benjamin on 2015/7/9.
 */
public class PublicArticleAct extends EaseBaseViewPageAct {
    @Override
    protected void initView() {
        setUpActionBar(getActionBar());
        fragments.add(ArticleFragment.newInstance(1));
        fragments.add(ArticleFragment.newInstance(2));
    }

    private void setUpActionBar(final ActionBar actionBar) {
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(R.layout.tag_title);
            ((TextView) actionBar.getCustomView().findViewById(R.id.tag_title_title_name))
                    .setText("文章推广");
            findViewById(R.id.tag_title_btn_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    @Override
    protected String[] tabName() {
        return new String[]{"最热", "最新"};
    }
}