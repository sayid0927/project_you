/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.easemob.easeui.ui;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.easemob.chatuidemo.R;
import com.easemob.easeui.controller.EaseUI;

public class EaseBaseActivity extends FragmentActivity {
    protected Handler mMainHandler;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        mMainHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                EaseBaseActivity.this.handleMessage(msg);
                return true;
            }
        });
    }

    protected void handleMessage(Message msg) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        // onresume时，取消notification显示
        EaseUI.getInstance().getNotifier().reset();
        
    }

    protected ActionBar setUpActionBar(String title) {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(R.layout.ease_tag_title);
            ((TextView) actionBar.getCustomView().findViewById(R.id.tag_title_title_name)).setText(title);
            findViewById(R.id.tag_title_btn_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        return actionBar;
    }


    /**
     * 返回
     * 
     * @param view
     */
    public void back(View view) {
        finish();
    }
}
