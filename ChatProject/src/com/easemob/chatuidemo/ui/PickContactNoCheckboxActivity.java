/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.easemob.chatuidemo.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.HXHelper;
import com.easemob.chatuidemo.R;
import com.easemob.easeui.adapter.EaseContactAdapter;
import com.easemob.easeui.domain.EaseUser;
import com.easemob.easeui.domain.EaseYAMUser;
import com.easemob.easeui.ui.EaseBaseActivity;
import com.easemob.easeui.widget.EaseSidebar;
import com.easemob.easeui.widget.easepullrefresh.EasePullToRefreshBase;
import com.easemob.easeui.widget.easepullrefresh.EasePullToRefreshListView;

public class PickContactNoCheckboxActivity extends EaseBaseActivity {

    private EasePullToRefreshListView listView;
    private EaseSidebar sidebar;
    protected EaseContactAdapter contactAdapter;
    private List<EaseYAMUser> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_pick_contact_no_checkbox);
        listView = (EasePullToRefreshListView) findViewById(R.id.list);
        sidebar = (EaseSidebar) findViewById(R.id.sidebar);
        sidebar.setListView(listView);
        contactList = new ArrayList<EaseYAMUser>();
        // 获取设置contactlist
        getContactList();
        // 设置adapter
        contactAdapter = new EaseContactAdapter(this, R.layout.ease_row_contact, contactList);
        listView.setAdapter(contactAdapter);
        listView.setMode(EasePullToRefreshBase.Mode.DISABLED);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onListItemClick(position-1);
            }
        });
        listView.setIntercept(true);

    }

    protected void onListItemClick(int position) {
        //		if (position != 0) {

        setResult(RESULT_OK,
                new Intent().putExtra("username", contactAdapter.getItem(position).getFirendsUserInfo()
                        .getEid()));
        finish();
        //		}
    }

    public void back(View view) {
        finish();
    }

    private void getContactList() {
        contactList.clear();
        Map<String, EaseYAMUser> users = HXHelper.getInstance().getYAMContactList();
        for (Entry<String, EaseYAMUser> entry : users.entrySet()) {
            contactList.add(entry.getValue());
        }
        // 排序
        Collections.sort(contactList, new Comparator<EaseYAMUser>() {

            @Override
            public int compare(EaseYAMUser lhs, EaseYAMUser rhs) {
                if (lhs.getFirendsUserInfo().getInitialLetter()
                        .equals(rhs.getFirendsUserInfo().getInitialLetter())) {
                    return lhs.getFirendsUserInfo().getNickname()
                            .compareTo(rhs.getFirendsUserInfo().getNickname());
                } else {
                    if ("#".equals(lhs.getFirendsUserInfo().getInitialLetter())) {
                        return 1;
                    } else if ("#".equals(rhs.getFirendsUserInfo().getInitialLetter())) {
                        return -1;
                    }
                    return lhs.getFirendsUserInfo().getInitialLetter()
                            .compareTo(rhs.getFirendsUserInfo().getInitialLetter());
                }

            }
        });
    }

}
