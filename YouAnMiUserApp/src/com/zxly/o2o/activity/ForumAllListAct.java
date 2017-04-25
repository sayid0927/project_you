package com.zxly.o2o.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zxly.o2o.fragment.CircleAllListFragment;
import com.zxly.o2o.fragment.MyCircleFragment;
import com.zxly.o2o.o2o_user.R;

/**
 * Created by Administrator on 2015/12/16.
 */
public class ForumAllListAct extends BasicAct {
    private CircleAllListFragment circleAllListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.easemob.chatuidemo.R.layout.em_activity_chat);
        setUpActionBar(getIntent().getStringExtra("title"));
        circleAllListFragment = new CircleAllListFragment(); //传入参数
        circleAllListFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction()
                .add(com.easemob.chatuidemo.R.id.container,
                        circleAllListFragment).commit();


    }

    protected void setUpActionBar(String title) {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(R.layout.tag_title);
            ((TextView) actionBar.getCustomView()
                    .findViewById(R.id.tag_title_title_name)).setText(title);
            findViewById(R.id.tag_title_btn_back)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
        }
    }
}
