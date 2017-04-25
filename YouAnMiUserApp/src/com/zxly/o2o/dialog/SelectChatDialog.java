package com.zxly.o2o.dialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.easemob.chatuidemo.HXApplication;
import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.HXHelper;
import com.easemob.chatuidemo.request.HXBaseRequest;
import com.easemob.chatuidemo.request.HXTokenRequest;
import com.easemob.chatuidemo.request.HXUserStatusRequest;
import com.easemob.chatuidemo.ui.ChatActivity;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.controller.EaseUI;
import com.easemob.easeui.domain.EaseYAMUser;
import com.easemob.easeui.widget.SwipeLayout;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.ViewUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2015/11/16.
 */
public class SelectChatDialog extends BaseDialog {

    private ListView mListView;
    private SelectChatAdapter mSelectChatAdapter;
    private int userCount;
    protected Set<SwipeLayout> mShownLayouts = new HashSet<SwipeLayout>();

    private class SelectChatAdapter extends ObjectAdapter {

        public SelectChatAdapter(Context _context) {
            super(_context);
            /*过滤出业务员*/

            HXHelper.getInstance().getYAMContactList();
            for (EaseYAMUser user : HXHelper.yamContactList) {
                if (user.getFirendsUserInfo().getIsTop() == 1) {
                    userCount++;
                }
            }

            if (userCount > 0) {
                //获取客服的在线状态
                getUserStatus();
            }
        }

        private class ViewHolder {
            NetworkImageView avatar;
            TextView nameView;
            TextView nameRight;
            TextView signature;
        }

        @Override
        public int getLayoutId() {
            return R.layout.ease_row_contact;
        }

        @Override
        public int getCount() {
            return userCount;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater
                        .from(context).inflate(com.easemob.chatuidemo.R.layout.ease_row_contact, null);

                SwipeLayout sLayout = (SwipeLayout) convertView.findViewById(com.easemob.chatuidemo.R.id.swipe);
                sLayout.addSwipeListener(new SwipeMemory());
                mShownLayouts.add(sLayout);
                sLayout.setSwipeEnabled(false);

                holder.avatar =
                        (NetworkImageView) convertView.findViewById(com.easemob.chatuidemo.R.id.avatar);
                holder.nameView = (TextView) convertView.findViewById(com.easemob.chatuidemo.R.id.name);
                holder.nameRight =
                        (TextView) convertView.findViewById(com.easemob.chatuidemo.R.id.name_right);
                holder.signature = (TextView) convertView.findViewById(com.easemob.chatuidemo.R.id.signature);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            EaseConstant
                    .setImage(holder.avatar,
                            HXHelper.yamContactList.get(position).getFirendsUserInfo().getThumHeadUrl(),
                            com.easemob.chatuidemo.R.drawable
                                    .ease_default_avatar, null);
            holder.signature.setText(HXHelper.yamContactList.get(position).getFirendsUserInfo().isOnline()
                    ? "在线" : "离线");
            holder.nameView.setText(HXHelper.yamContactList.get(position).getFirendsUserInfo().getNickname());
            holder.nameView.setText(String.format(context.getString(R.string
                            .select_kefu_dialog_item_name),
                    HXHelper.yamContactList.get(position).getFirendsUserInfo().getGroupName(),
                    HXHelper.yamContactList.get(position)
                            .getFirendsUserInfo().getNickname()));
            return convertView;
        }


    }

    private void getUserStatus() {
        if ("".equals(HXTokenRequest.HXToken)) {
            HXTokenRequest hxTokenRequest = new HXTokenRequest();
            hxTokenRequest.setOnResponseStateListener(new HXBaseRequest.ResponseStateListener() {
                @Override
                public void onOK() {
                    HXUserStatusRequest hXUserStatusRequest = new HXUserStatusRequest();
                    hXUserStatusRequest.setOnResponseStateListener(new HXBaseRequest.ResponseStateListener() {
                        @Override
                        public void onOK() {
                            mSelectChatAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFail(int code) {

                        }
                    });
                    hXUserStatusRequest.start();
                }

                @Override
                public void onFail(int code) {

                }
            });
            hxTokenRequest.start();
        } else {
            HXUserStatusRequest hXUserStatusRequest = new HXUserStatusRequest();
            hXUserStatusRequest.setOnResponseStateListener(new HXBaseRequest.ResponseStateListener() {
                @Override
                public void onOK() {
                    mSelectChatAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFail(int code) {

                }
            });
            hXUserStatusRequest.start();
        }

    }

    @Override
    public int getGravity() {
        return Gravity.CENTER;
    }

    @Override
    public int getLayoutId() {

        return R.layout.select_chat_dialog_layout;
    }

    @Override
    protected void initView() {
        // TODO Auto-generated method stub
        mListView = (ListView) findViewById(R.id.list);
        ViewGroup.LayoutParams layoutParams = mListView.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = AppController.displayMetrics.heightPixels / 2;
        mListView.setLayoutParams(layoutParams);


        mSelectChatAdapter = new SelectChatAdapter(getContext());
        mListView.setAdapter(mSelectChatAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToHXMain(
                        HXApplication.getInstance().parseUserFromID(HXHelper.yamContactList.get(position)
                                .getFirendsUserInfo().getId(), HXConstant.TAG_SHOP));
            }
        });
    }

    private void goToHXMain(String toChatUser) {
        this.dismiss();
        Intent intent = new Intent();
        intent.setClass(AppController.getInstance().getTopAct(), ChatActivity.class);
        intent.putExtra("userId", toChatUser);
        intent.putExtra("chatType", HXConstant.CHATTYPE_SINGLE);
        ViewUtils.startActivity(intent, AppController.getInstance().getTopAct());
    }

    class SwipeMemory implements SwipeLayout.SwipeListener {

        @Override
        public void onStartOpen(SwipeLayout layout) {
            closeAllExcept(layout);
        }

        @Override
        public void onOpen(SwipeLayout layout) {
            closeAllExcept(layout);
        }

        @Override
        public void onStartClose(SwipeLayout layout) {

        }

        @Override
        public void onClose(SwipeLayout layout) {

        }

        @Override
        public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

        }

        @Override
        public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

        }
    }

    public void closeAllExcept(SwipeLayout layout) {
        for (SwipeLayout s : mShownLayouts) {
            if (s != layout) {
                s.close();
            }
        }
    }

    protected boolean isShowAnimation() {
        return false;
    }
}
