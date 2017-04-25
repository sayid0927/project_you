/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p/>
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
package com.easemob.easeui.ui;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chatuidemo.HXApplication;
import com.easemob.chatuidemo.HXHelper;
import com.easemob.chatuidemo.HXModel;
import com.easemob.chatuidemo.R;
import com.easemob.chatuidemo.utils.EaseCallBack;
import com.easemob.chatuidemo.utils.PreferenceManager;
import com.easemob.easeui.AppException;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.adapter.EaseContactAdapter;
import com.easemob.easeui.domain.EaseUser;
import com.easemob.easeui.domain.EaseYAMUser;
import com.easemob.easeui.utils.EaseCommonUtils;
import com.easemob.easeui.utils.GsonParser;
import com.easemob.easeui.widget.EaseContactList;
import com.easemob.easeui.widget.EaseMyFlipperView;
import com.easemob.easeui.widget.easepullrefresh.EasePullToRefreshListView;
import com.easemob.exceptions.EaseMobException;
import com.google.gson.reflect.TypeToken;

/**
 * 联系人列表页
 */
public class EaseContactListFragment extends EaseBaseFragment {
    private static final String TAG = "EaseContactListFragment";
    protected EasePullToRefreshListView listView;
    protected boolean hidden;
    protected Handler handler = new Handler();
    protected EaseUser toBeProcessUser;
    protected String toBeProcessUsername;
    public EaseContactList contactListLayout;
    protected boolean isConflict;
    protected FrameLayout contentContainer;
    protected TextView membersCount;
    protected EaseMyFlipperView viewContainer;
    protected EaseCallBack easeCallBack;

    private void setFlipper() {
        if (viewContainer == null) {
            viewContainer = (EaseMyFlipperView) getView().findViewById(R.id.list_layout);
            viewContainer.getRetryBtn().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    viewContainer.setDisplayedChild(0, true);
                    refresh();
                }
            });
            contactListLayout.setViewContainer(viewContainer);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ease_fragment_contact_list, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
            return;
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initView() {
        contentContainer = (FrameLayout) getView().findViewById(R.id.content_container);

        contactListLayout = (EaseContactList) getView().findViewById(R.id.contact_list);
        contactListLayout.setCallBack(easeCallBack);
        listView = contactListLayout.getListView();

    }


    @Override
    protected void setUpView() {
        EMChatManager.getInstance().addConnectionListener(connectionListener);

        //        HXHelper.getInstance().getBlackList();

        setFlipper();


        //init list
        contactListLayout.init(HXHelper.yamContactList);

        // 获取设置contactlist
        refresh();

        if (listItemClickListener != null) {
            listView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (id < HXHelper.yamContactList.size()) {   //商户app有未注册的成员需要过滤掉
                        EaseYAMUser user = (EaseYAMUser) listView.getItemAtPosition(position);
                        listItemClickListener.onListItemClicked(user.getFirendsUserInfo());
                        //                    itemClickLaunchIntent.putExtra(EaseConstant.USER_ID, username);
                    }
                }
            });
        }

        listView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 隐藏软键盘
                hideSoftKeyboard();
                return false;
            }
        });
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden) {
            refresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hidden) {
            refresh();
        }
    }


    /**
     * 把user移入到黑名单
     */
    protected void moveToBlacklist(final String username) {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        String st1 = getResources().getString(R.string.Is_moved_into_blacklist);
        final String st2 = getResources().getString(R.string.Move_into_blacklist_success);
        final String st3 = getResources().getString(R.string.Move_into_blacklist_failure);
        pd.setMessage(st1);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        new Thread(new Runnable() {
            public void run() {
                try {
                    //加入到黑名单
                    EMContactManager.getInstance().addUserToBlackList(username, false);
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(getActivity(), st2, Toast.LENGTH_SHORT).show();
                            refresh();
                        }
                    });
                } catch (EaseMobException e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(getActivity(), st3, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();

    }

    // 刷新ui
    public void refresh() {
        HXHelper.getInstance().getYAMContactList();
        if (EaseContactAdapter.unRegistList == null || EaseContactAdapter.unRegistList.size() == 0) {
            EaseContactAdapter.unRegistList = new HXModel(getActivity()).getUnRegistList();
        }
        contactListLayout.refresh(membersCount);

    }


    @Override
    public void onDestroy() {

        EMChatManager.getInstance().removeConnectionListener(connectionListener);

        super.onDestroy();

        //保存联系人置顶信息
        ArrayList<Long> tops = new ArrayList<Long>();
        EaseYAMUser easeYAMUser;
        for (int i = 0; i < contactListLayout.getEaseContactAdapter().getCount(); i++) {
            easeYAMUser = contactListLayout.getEaseContactAdapter().getItem(i);
            if (easeYAMUser.getFirendsUserInfo().getIsTop() == 1) {
                tops.add(easeYAMUser.getFirendsUserInfo().getId());
            } else {
                break;
            }
        }

        if (EaseConstant.shopID < 0) {
            PreferenceManager.getInstance().saveTopContacts(GsonParser.getInstance().toJson(tops));
        }
    }


    /**
     * 获取联系人列表，并过滤掉黑名单和排序
     */


    protected EMConnectionListener connectionListener = new EMConnectionListener() {

        @Override
        public void onDisconnected(int error) {
            if (error == EMError.USER_REMOVED || error == EMError.CONNECTION_CONFLICT) {
                isConflict = true;
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        onConnectionDisconnected();
                    }

                });
            }
        }

        @Override
        public void onConnected() {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    onConnectionConnected();
                }

            });
        }
    };
    private EaseContactListItemClickListener listItemClickListener;


    protected void onConnectionDisconnected() {

    }

    protected void onConnectionConnected() {

    }

    public interface EaseContactListItemClickListener {
        /**
         * 联系人listview item点击事件
         *
         * @param user 被点击item所对应的user对象
         */
        void onListItemClicked(EaseUser user);
    }

    /**
     * 设置listview item点击事件
     *
     * @param listItemClickListener
     */
    public void setContactListItemClickListener(EaseContactListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

}
