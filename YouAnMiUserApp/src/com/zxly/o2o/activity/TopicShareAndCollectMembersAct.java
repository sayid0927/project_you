package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.easemob.chatuidemo.HXApplication;
import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.HXHelper;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.domain.EaseYAMUser;
import com.easemob.easeui.request.HXNormalRequest;
import com.easemob.easeui.ui.EaseBaseMyListPageActivity;
import com.easemob.easeui.widget.EaseMyFlipperView;
import com.easemob.easeui.widget.SwipeLayout;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.model.TopicDetailHeads;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.ShareAndUpStatusRequest;


/**
 * Created by Administrator on 2016/1/21.
 */
public class TopicShareAndCollectMembersAct extends EaseBaseMyListPageActivity {
    private MembersAdapter mMembersAdapter;
    private byte isShop;
    private long id;

    public static void start(long id, byte isShop, Activity context) {
        Intent intent = new Intent();
        intent.setClass(context, TopicShareAndCollectMembersAct.class);
        intent.putExtra("isShop", isShop);
        intent.putExtra("id", id);
        EaseConstant.startActivity(intent, context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ease_pull_to_refresh_layout);
        setUpActionBar("点赞&分享");
        setListView();
        setFlipper();
        page = 1;

        isShop = getIntent().getByteExtra("isShop", (byte) 1);
        id = getIntent().getLongExtra("id", 0);

        mMembersAdapter = new MembersAdapter(this);
        mListView.setAdapter(mMembersAdapter);

        loadData();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EaseConstant.startIMUserDetailInfo(((TopicDetailHeads) mMembersAdapter.getItem(position-1))
                                .getUser().getId(),
                        true,
                        TopicShareAndCollectMembersAct.this, "个人信息", 1,null);
            }
        });
    }

    @Override
    protected void loadData() {
        myRequest = new ShareAndUpStatusRequest(isShop, id, page);
        myRequest.setOnResponseStateListener(new HXNormalRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                if (((ShareAndUpStatusRequest) myRequest).viewHolders != null &&
                        ((ShareAndUpStatusRequest) myRequest).viewHolders.size() >
                                0) {
                    if (page == 1) {
                        mMembersAdapter.clear();
                        viewContainer.setDisplayedChild(EaseMyFlipperView.LOADSUCCESSFUL, false);
                    }
                    mMembersAdapter.addItem(((ShareAndUpStatusRequest) myRequest).viewHolders);

                } else {
                    if (page == 1) {
                        viewContainer.setDisplayedChild(EaseMyFlipperView.NODATA, true);
                    }
                    isLastPage = 1;
                }
                mListView.onRefreshComplete();
            }

            @Override
            public void onFail(int code) {
                mListView.onRefreshComplete();
                viewContainer.setDisplayedChild(EaseMyFlipperView.LOADFAIL, true);
            }
        });
        myRequest.start();
    }


    private class MembersAdapter extends ObjectAdapter {

        public MembersAdapter(Context _context) {
            super(_context);
        }

        private class ViewHolder {
            NetworkImageView avatar;
            TextView nameView;
            TextView nameRight;
            TextView signature;
            TextView actionName;
            TextView actionTime;
            SwipeLayout sLayout;
        }

        @Override
        public int getLayoutId() {
            return R.layout.like_share_list_layout;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflateConvertView();

                holder.avatar =
                        (NetworkImageView) convertView.findViewById(R.id.avatar);
                holder.nameView = (TextView) convertView.findViewById(R.id.name);
                holder.actionName = (TextView) convertView.findViewById(R.id.action_name);
                holder.actionTime = (TextView) convertView.findViewById(R.id.action_time);
                holder.sLayout = (SwipeLayout) convertView.findViewById(R.id.swipe);
                holder.sLayout.setSwipeEnabled(false);
                holder.nameRight =
                        (TextView) convertView.findViewById(R.id.name_right);
                holder.signature = (TextView) convertView.findViewById(R.id.signature);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            TopicDetailHeads item = (TopicDetailHeads) getItem(position);
            EaseConstant
                    .setImage(holder.avatar, item.getUser().getThumHeadUrl(),
                            com.easemob.chatuidemo.R.drawable
                                    .ease_default_avatar, null);
            if(!TextUtils.isEmpty(item.getUser().getSignature()))
            holder.signature.setText(item.getUser().getSignature());
            holder.nameView.setText(item.getUser().getNickname());
            holder.actionName.setText(item.getIsPraise() == 1 ? "赞了一个" : "分享了");
            holder.actionTime.setText(EaseConstant.getShortTime(item.getPraiseTime()));
            return convertView;
        }


    }
}
