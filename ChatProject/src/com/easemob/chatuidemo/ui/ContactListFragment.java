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
package com.easemob.chatuidemo.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMContactManager;
import com.easemob.chatuidemo.HXApplication;
import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.HXHelper;
import com.easemob.chatuidemo.HXHelper.DataSyncListener;
import com.easemob.chatuidemo.HXModel;
import com.easemob.chatuidemo.R;
import com.easemob.chatuidemo.db.InviteMessgeDao;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.utils.EaseCallBack;
import com.easemob.chatuidemo.widget.ContactItemView;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.adapter.EaseContactAdapter;
import com.easemob.easeui.dialog.EaseContactFilterDialog;
import com.easemob.easeui.domain.EaseUser;
import com.easemob.easeui.domain.EaseYAMUser;
import com.easemob.easeui.ui.EaseContactListFragment;
import com.easemob.util.EMLog;

/**
 * 联系人列表页
 */
public class ContactListFragment extends EaseContactListFragment implements OnClickListener {

    private static final String TAG = ContactListFragment.class.getSimpleName();
    private ContactSyncListener contactSyncListener;
    private BlackListSyncListener blackListSyncListener;
    private ContactInfoSyncListener contactInfoSyncListener;
    private View loadingView;
    private ContactItemView applicationItem;
    private InviteMessgeDao inviteMessgeDao;
    protected TextView SearchRight;
    protected EditText query;

    public static MyFilter myFilter;
    public static int blackChangeStatu;
    private boolean isCanAct;


    public void setCallBack(EaseCallBack callBack){
        easeCallBack=callBack;
    }

    private class MyFilter {
        long beforeTime;
        long afterTime;
        int belowCommission;
        int topCommission;
    }

    @Override
    public void onResume() {
        super.onResume();

        /*联系人状态有更新，重新从数据库拉取联系人列表*/
        if (blackChangeStatu > 0) {
            HXHelper.getInstance().cleanYAMContactList();
            super.setUpView();
        }
    }


    @Override
    protected void initView() {
        super.initView();
        hideTitleBar();
        View searchView =
                LayoutInflater.from(getActivity()).inflate(R.layout.ease_search_bar_with_padding, null);

        //        HeaderItemClickListener clickListener = new HeaderItemClickListener();
        //        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.em_contacts_header, null);
        //        applicationItem = (ContactItemView) headerView.findViewById(R.id.application_item);
        //        applicationItem.setOnClickListener(clickListener);
        //        headerView.findViewById(R.id.group_item).setOnClickListener(clickListener);
        //        headerView.findViewById(R.id.chat_room_item).setOnClickListener(clickListener);
        //        headerView.findViewById(R.id.robot_item).setOnClickListener(clickListener);

        //添加headerview
        if (EaseConstant.shopID < 0) {  //商户app才需要显示
            View headerView =
                    LayoutInflater.from(getActivity()).inflate(R.layout.ease_shop_contact_headview, null);
            SearchRight = (TextView) searchView.findViewById(R.id.search_right);
            membersCount = (TextView) headerView.findViewById(R.id.count_members);
            headerView.findViewById(R.id.tsgg_btn).setOnClickListener(this);
            headerView.findViewById(R.id.qfxx_btn).setOnClickListener(this);
            SearchRight.setOnClickListener(this);
            SearchRight.setVisibility(View.VISIBLE);
            //            setSearchListener();
            listView.addH(headerView);
        }
        listView.addH(searchView);

        //添加正在加载数据提示的loading view
        //        loadingView = LayoutInflater.from(getActivity()).inflate(R.layout.em_layout_loading_data, null);
        //        contentContainer.addView(loadingView);
        //注册上下文菜单
        registerForContextMenu(listView);

        //搜索框
        query = (EditText) searchView.findViewById(R.id.query);
        setSearchListener();
    }

    private void setSearchListener() {

        query.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contactListLayout.filter(s);
                //                if (s.length() > 0) {
                //                    SearchRight.setVisibility(View.VISIBLE);
                //                } else {
                //                    SearchRight.setVisibility(View.INVISIBLE);
                //
                //                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        //        SearchRight.setOnClickListener(new OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                query.getText().clear();
        //                hideSoftKeyboard();
        //            }
        //        });
    }


    @Override
    public void refresh() {
        super.refresh();
        if(!TextUtils.isEmpty(query.getText())) {
            query.setText("");
        }

        if (EaseConstant.shopID < 0 && EaseContactAdapter.unRegistList != null && EaseContactAdapter.unRegistList.size() < 1)
        {  //商户端刷新未注册
            EaseContactAdapter.unRegistList = new HXModel(HXApplication.getInstance().applicationContext).getUnRegistList();
            if(EaseContactAdapter.unRegistList.size()>0)
            contactListLayout.getEaseContactAdapter().notifyDataSetChanged();
        }
//        if (inviteMessgeDao == null) {
//            inviteMessgeDao = new InviteMessgeDao(getActivity());
//        }
        //        if(inviteMessgeDao.getUnreadMessagesCount() > 0){
        //            applicationItem.showUnreadMsgView();
        //        }else{
        //            applicationItem.hideUnreadMsgView();
        //        }
    }


    @Override
    protected void setUpView() {
        contactListLayout.setShowSiderBar(EaseConstant.shopID > 0); //设置搜索联系人的siderBar
        if (titleBar.isShown()) {
            titleBar.setRightImageResource(R.drawable.em_add);
            titleBar.setRightLayoutClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), AddContactActivity.class));
                }
            });
            // 进入添加好友页
            titleBar.getRightLayout().setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), AddContactActivity.class));
                }
            });
        }

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id < HXHelper.yamContactList.size()) {   //商户app有未注册的成员需要过滤掉

                    EaseYAMUser user = ((EaseYAMUser) listView.getItemAtPosition(position));
                    long userId = user.getFirendsUserInfo().getId();

                    // demo中直接进入聊天页面，实际一般是进入用户详情页
                    if (EaseConstant.shopID > 0) {
                        if (user.getFirendsUserInfo().getIsTop() != 1) {
                            EaseConstant.startIMUserDetailInfo(userId, false, getActivity(), "个人信息", 0,
                                    HXConstant.TAG_USER);

                        } else {
                            EaseConstant.startIMUserDetailInfo(userId, false, getActivity(), "个人信息", 0,
                                    HXConstant.TAG_SHOP );

                        }
                    } else if (EaseConstant.shopID < 0) {
                        EaseConstant.startShopIMUserDetailInfo(userId, getActivity());
                    }
                } else {
                    if(EaseConstant.shopID<0&&isCanAct){
                        Toast.makeText(HXApplication.applicationContext, "未注册用户不能操作", Toast.LENGTH_SHORT)
                                .show();
                    }else{
                        isCanAct=true;
                        refresh();
                    }

                }
            }
        });

        contactSyncListener = new ContactSyncListener();
        HXHelper.getInstance().addSyncContactListener(contactSyncListener);

        blackListSyncListener = new BlackListSyncListener();
        HXHelper.getInstance().addSyncBlackListListener(blackListSyncListener);

        contactInfoSyncListener = new ContactInfoSyncListener();
        HXHelper.getInstance().getUserProfileManager().addSyncContactInfoListener(contactInfoSyncListener);

        //        if (!HXHelper.getInstance().isContactsSyncedWithServer()) {
        //            loadingView.setVisibility(View.VISIBLE);
        //        } else {
        //            loadingView.setVisibility(View.GONE);
        //        }

        super.setUpView();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (contactSyncListener != null) {
            HXHelper.getInstance().removeSyncContactListener(contactSyncListener);
            contactSyncListener = null;
        }

        if (blackListSyncListener != null) {
            HXHelper.getInstance().removeSyncBlackListListener(blackListSyncListener);
        }

        if (contactInfoSyncListener != null) {
            HXHelper.getInstance().getUserProfileManager().removeSyncContactInfoListener(
                    contactInfoSyncListener);
        }
        if (myFilter != null) {
            myFilter = null;
        }
    }

    @Override
    public void onClick(View v) {
        myFilter = new MyFilter();
        if (v.getId() == R.id.tsgg_btn) {
            if (EaseConstant.multiMsgProgress!=0) {
               Toast.makeText(getActivity(),"有后台消息推送正在运行,请稍候!",Toast.LENGTH_SHORT).show();
            }else {
                new EaseContactFilterDialog(getActivity(), HXConstant.MUL_PUSH, null).show();
            }
        } else if (v.getId() == R.id.qfxx_btn) {
            if (EaseConstant.multiMsgProgress!=0) {
                Toast.makeText(getActivity(),"有后台消息推送正在运行,请稍候!",Toast.LENGTH_SHORT).show();
            }else {
                new EaseContactFilterDialog(getActivity(), HXConstant.MUL_SEND, null).show();
            }
        }else if(v.getId() == R.id.search_right){
            new EaseContactFilterDialog(getActivity(), 3, new EaseCallBack() {
                @Override
                public void onCall() {
                    contactListLayout.refresh(true);
                }
            }).show();
        }
    }


    protected class HeaderItemClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.application_item) {// 进入申请与通知页面
                startActivity(new Intent(getActivity(), NewFriendsMsgActivity.class));

            } else if (i == R.id.group_item) {// 进入群聊列表页面
                startActivity(new Intent(getActivity(), GroupsActivity.class));

            } else if (i == R.id.chat_room_item) {//进入聊天室列表页面
                startActivity(new Intent(getActivity(), PublicChatRoomsActivity.class));

            } else if (i == R.id.robot_item) {//进入Robot列表页面
                startActivity(new Intent(getActivity(), RobotsActivity.class));

            } else {
            }
        }

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //        toBeProcessUser =
        //                ((EaseYAMUser) listView.getItemAtPosition(((AdapterContextMenuInfo) menuInfo).position))
        //                        .getFirendsUserInfo();
        //        toBeProcessUsername = toBeProcessUser.getUsername();
        //        getActivity().getMenuInflater().inflate(R.menu.em_context_contact_list, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_contact) {
            try {
                // 删除此联系人
                deleteContact(toBeProcessUser);
                // 删除相关的邀请消息
                InviteMessgeDao dao = new InviteMessgeDao(getActivity());
                dao.deleteMessage(toBeProcessUser.getUsername());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        } else if (item.getItemId() == R.id.add_to_blacklist) {
            moveToBlacklist(toBeProcessUsername);
            return true;
        }
        return super.onContextItemSelected(item);
    }


    /**
     * 删除联系人
     */
    public void deleteContact(final EaseUser tobeDeleteUser) {
        String st1 = getResources().getString(R.string.deleting);
        final String st2 = getResources().getString(R.string.Delete_failed);
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage(st1);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        new Thread(new Runnable() {
            public void run() {
                try {
                    EMContactManager.getInstance().deleteContact(tobeDeleteUser.getUsername());
                    // 删除db和内存中此用户的数据
                    UserDao dao = new UserDao(getActivity());
                    dao.deleteYAMContact(tobeDeleteUser.getUsername());
                    HXHelper.getInstance().getYAMContactList().remove(tobeDeleteUser.getUsername());
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            HXHelper.yamContactList.remove(tobeDeleteUser);
                            contactListLayout.refresh(null);

                        }
                    });
                } catch (final Exception e) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(getActivity(), st2 + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        }).start();

    }

    class ContactSyncListener implements DataSyncListener {
        @Override
        public void onSyncComplete(final boolean success) {
            EMLog.d(TAG, "on contact list sync success:" + success);
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (success) {
                                loadingView.setVisibility(View.GONE);
                                refresh();
                            } else {
                                String s1 = getResources().getString(R.string.get_failed_please_check);
                                Toast.makeText(getActivity(), s1, Toast.LENGTH_SHORT).show();
                                loadingView.setVisibility(View.GONE);
                            }
                        }

                    });
                }
            });
        }
    }

    class BlackListSyncListener implements DataSyncListener {

        @Override
        public void onSyncComplete(boolean success) {
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    HXHelper.getInstance().getBlackList();
                    refresh();
                }

            });
        }

    }

    ;

    class ContactInfoSyncListener implements DataSyncListener {

        @Override
        public void onSyncComplete(final boolean success) {
            EMLog.d(TAG, "on contactinfo list sync success:" + success);
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    loadingView.setVisibility(View.GONE);
                    if (success) {
                        refresh();
                    }
                }
            });
        }

    }

}
