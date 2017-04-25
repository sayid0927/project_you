package com.easemob.easeui.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.easemob.chatuidemo.R;
import com.easemob.easeui.request.SystemMsgReadRequest;


/**
 * Created by Administrator on 2015/10/9.
 */
public class SysMsgDetailActivity extends EaseBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ease_sys_msg_detail);
        setUpActionBar(getActionBar());
        initView(getIntent());

        //阅读统计
        long id = getIntent().getLongExtra("id", 0);
        if (id != 0) {
            new SystemMsgReadRequest(id).start();
        }
    }

    private void initView(Intent intent) {
        ((TextView) findViewById(R.id.tv_title)).setText(intent.getStringExtra("title"));
        ((TextView) findViewById(R.id.tv_content)).setText(intent.getStringExtra("content"));
        ((TextView) findViewById(R.id.tv_time)).setText(intent.getStringExtra("time"));
    }

    private void setUpActionBar(final ActionBar actionBar) {
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(R.layout.ease_tag_title);
            ((TextView) actionBar.getCustomView().findViewById(R.id.tag_title_title_name))
                    .setText("消息详情");
            findViewById(R.id.tag_title_btn_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
}
