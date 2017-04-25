package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.ShopTopic;
import com.zxly.o2o.model.UserTopic;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.MyTopicDeleteRequest;
import com.zxly.o2o.request.MyTopicListRequest;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fengrongjian 2015-12-26
 * @description 我的帖子页面
 */
public class UserTopicAct extends BasicAct implements View.OnClickListener, PullToRefreshBase.OnRefreshListener {
    private Context context;
    private PullToRefreshListView listView;
    private MyTopicAdapter adapter;
    private LoadingView loadingView = null;
    private long pageIndex = 1;
    private TextView btnManage;
    private CheckBox btnSelectAll;
    private View viewManage;
    private boolean isManage;
    private Map<Integer, Boolean> selectMap = new HashMap<Integer, Boolean>();
    private StringBuffer shopIds, platIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_my_topic);
        context = this;
        initViews();
        loadData(pageIndex);
    }

    public static void start(Activity curAct) {
        Intent intent = new Intent(curAct, UserTopicAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

    private void initViews() {
        loadingView = (LoadingView) findViewById(R.id.view_loading);
        findViewById(R.id.btn_back).setOnClickListener(this);
        ViewUtils.setText(findViewById(R.id.txt_title), "我的帖子");
        btnManage = (TextView) findViewById(R.id.btn_title_right);
        ViewUtils.setText(btnManage, "管理");
        btnManage.setOnClickListener(this);

        viewManage = findViewById(R.id.view_manage);
        findViewById(R.id.btn_del).setOnClickListener(this);
        btnSelectAll = (CheckBox) findViewById(R.id.btn_select_all);
        btnSelectAll.setOnClickListener(this);

        listView = (PullToRefreshListView) findViewById(R.id.list_view);
        listView.setIntercept(true);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        adapter = new MyTopicAdapter(context);
        listView.setAdapter(adapter);
        ViewUtils.setRefreshText(listView);
        listView.setOnRefreshListener(this);
    }

    private void loadData(final long pageId) {
        final MyTopicListRequest myTopicListRequest = new MyTopicListRequest(pageId, Account.user.getId());
        myTopicListRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                List<UserTopic> myTopicList = myTopicListRequest.getMyTopicList();
                if (!DataUtil.listIsNull(myTopicList)) {
                    ViewUtils.setVisible(btnManage);
                    if (pageId == 1) {
                        adapter.clear();
                    }
                    adapter.addItem(myTopicList, true);
                    adapter.setSelectMap(false);
                    pageIndex++;
                    loadingView.onLoadingComplete();
                } else {
                    if (pageId == 1) {
                        adapter.clear();
                        adapter.notifyDataSetChanged();
                        loadingView.onDataEmpty("您还没有发帖呢！");
                        ViewUtils.setGone(btnManage);
                        ViewUtils.setGone(viewManage);
                        listView.setMode(PullToRefreshBase.Mode.BOTH);
                    } else {
                        ViewUtils.showToast("最后一页了");
                        loadingView.onLoadingComplete();
                    }
                }

                if (listView.isRefreshing()) {
                    listView.onRefreshComplete();
                }
            }

            @Override
            public void onFail(int code) {
                loadingView.onLoadingFail();

                if (listView.isRefreshing()) {
                    listView.onRefreshComplete();
                }

            }
        });
        loadingView.setOnAgainListener(new LoadingView.OnAgainListener() {

            @Override
            public void onLoading() {
                loadingView.startLoading();
                myTopicListRequest.start(this);
            }
        });
        loadingView.startLoading();
        myTopicListRequest.start(this);
    }

    private void deleteMyTopic(String shopIds, String platformIds){
        MyTopicDeleteRequest myTopicDeleteRequest = new MyTopicDeleteRequest(shopIds, platformIds);
        myTopicDeleteRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                btnSelectAll.setChecked(false);
                loadData(1);
            }

            @Override
            public void onFail(int code) {

            }
        });
        myTopicDeleteRequest.start(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_title_right:
                if (isManage) {
                    ViewUtils.setGone(viewManage);
                    adapter.setManage(false);
                    adapter.notifyDataSetChanged();
                    ViewUtils.setText(btnManage, "管理");
                    isManage = false;
                    listView.setMode(PullToRefreshBase.Mode.BOTH);
                    btnSelectAll.setChecked(false);
                } else {
                    ViewUtils.setVisible(viewManage);
                    adapter.setManage(true);
                    adapter.setSelectMap(false);
                    ViewUtils.setText(btnManage, "完成");
                    isManage = true;
                    listView.setMode(PullToRefreshBase.Mode.DISABLED);
                }
                break;
            case R.id.btn_del:
                shopIds = new StringBuffer();
                platIds = new StringBuffer();
                for (int i = 0; i < adapter.getContent().size(); i++) {
                    if (selectMap.get(i)) {
                        UserTopic userTopic = (UserTopic) adapter.getContent().get(i);
                        if(userTopic.getIsShopTopic() == 1){
                            if(!"".equals(platIds.toString())) {
                                platIds.append(",");
                            }
                            platIds.append(userTopic.getId());
                        } else {
                            if(!"".equals(shopIds.toString())) {
                                shopIds.append(",");
                            }
                            shopIds.append(userTopic.getId());
                        }
                    }
                }
                if(!StringUtil.isNull(shopIds.toString()) || !StringUtil.isNull(platIds.toString())) {
                    deleteMyTopic(shopIds.toString(), platIds.toString());
                } else {
                    ViewUtils.showToast("没有选中要删除的帖子");
                }
                break;
            case R.id.btn_select_all:
                if (btnSelectAll.isChecked()) {
                    adapter.setSelectMap(true);
                } else {
                    adapter.setSelectMap(false);
                }
                break;
        }
    }

    class MyTopicAdapter extends ObjectAdapter {
        private List<String> imgThumList= new ArrayList<String>();
        private List<String> imgOriginList = new ArrayList<String>();
        private Drawable drawable;
        private boolean isManage;

        public MyTopicAdapter(Context context) {
            super(context);
        }

        public void setManage(boolean isManage){
            this.isManage = isManage;
        }

        public void setSelectMap(boolean isSelectAll) {
            for (int i = 0; i < getContent().size(); i++) {
                selectMap.put(i, isSelectAll);
            }
            notifyDataSetChanged();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflateConvertView();
                viewHolder.viewHeadLine = convertView.findViewById(R.id.view_head_line);
                viewHolder.btnSelect = (CheckBox) convertView.findViewById(R.id.btn_select);
                viewHolder.txtContent = (TextView) convertView.findViewById(R.id.txt_content);
                viewHolder.viewImage = convertView.findViewById(R.id.layout_img);
                viewHolder.imgTopic1 = (NetworkImageView) convertView.findViewById(R.id.img_topic1);
                viewHolder.imgTopic2 = (NetworkImageView) convertView.findViewById(R.id.img_topic2);
                viewHolder.imgTopic3 = (NetworkImageView) convertView.findViewById(R.id.img_topic3);
                viewHolder.txtTime = (TextView) convertView.findViewById(R.id.txt_time);
                viewHolder.txtPraise = (TextView) convertView.findViewById(R.id.txt_praise);
                viewHolder.txtReply = (TextView) convertView.findViewById(R.id.txt_reply);
                viewHolder.viewBottom = convertView.findViewById(R.id.view_last);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final UserTopic userTopic = (UserTopic) content.get(position);
            if (position == 0) {
                ViewUtils.setGone(viewHolder.viewHeadLine);
            } else {
                ViewUtils.setVisible(viewHolder.viewHeadLine);
            }
            if (position == adapter.getCount() - 1) {
                ViewUtils.setVisible(viewHolder.viewBottom);
            } else {
                ViewUtils.setGone(viewHolder.viewBottom);
            }
            ViewUtils.setText(viewHolder.txtTime, StringUtil.getShortTime(userTopic.getCreateTime()));
            ViewUtils.setText(viewHolder.txtContent, userTopic.getContent());
            ViewUtils.setText(viewHolder.txtPraise, userTopic.getPraiseAmout());
            if (userTopic.getIsPraise() == 1) {
                drawable = context.getResources().getDrawable(R.drawable.zan_press);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                viewHolder.txtPraise.setCompoundDrawables(drawable, null, null, null);
            } else if (userTopic.getIsPraise() == 0) {
                drawable = context.getResources().getDrawable(R.drawable.zan_normal);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                viewHolder.txtPraise.setCompoundDrawables(drawable, null, null, null);
            }
            ViewUtils.setText(viewHolder.txtReply, userTopic.getReplyAmout());

            imgThumList = userTopic.getThumImageList();
            if (!imgThumList.isEmpty()) {
                ViewUtils.setVisible(viewHolder.viewImage);
                showImage(imgThumList.size(), viewHolder);
//                setIconClickListener(position, viewHolder);
            } else {
                ViewUtils.setGone(viewHolder.viewImage);
            }
            if(isManage){
                ViewUtils.setVisible(viewHolder.btnSelect);
            } else{
                ViewUtils.setGone(viewHolder.btnSelect);
            }
            viewHolder.btnSelect.setChecked(selectMap.get(position));
            viewHolder.btnSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectMap.put(position, true);
                    } else {
                        selectMap.put(position, false);
                    }
                }
            });
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isManage) {
                        if(viewHolder.btnSelect.isChecked()){
                            viewHolder.btnSelect.setChecked(false);
                        } else {
                            viewHolder.btnSelect.setChecked(true);
                        }
                    } else {
                        byte isShop = (byte) userTopic.getIsShopTopic();
                        ShopTopic shopTopic = new ShopTopic();
                        shopTopic.setContent(userTopic.getContent());
                        shopTopic.setPraiseAmout(userTopic.getPraiseAmout());
                        shopTopic.setReplyAmout(userTopic.getReplyAmout());
                        ShopTopic.PublishUser publishUser = shopTopic.new PublishUser();
                        publishUser.setNickname("");
                        publishUser.setThumHeadUrl("");
                        shopTopic.setPublishUser(publishUser);
                        shopTopic.setCreateTime(userTopic.getCreateTime());
                        shopTopic.setIsPraise(userTopic.getIsPraise());
                        shopTopic.setOriginImageList(userTopic.getOriginImageList());
                        shopTopic.setThumImageList(userTopic.getThumImageList());
                        shopTopic.setId((int) userTopic.getId());
                        MyCircleThirdAct.start(UserTopicAct.this, isShop, shopTopic, "详情");
                    }
                }
            });
            return convertView;
        }

        private void showImage(int size, ViewHolder viewHolder) {
            if (size == 1) {
                ViewUtils.setVisible(viewHolder.imgTopic1);
                ViewUtils.setGone(viewHolder.imgTopic2);
                ViewUtils.setGone(viewHolder.imgTopic3);
                viewHolder.imgTopic1.setImageUrl(imgThumList.get(0),
                        AppController.imageLoader);
            } else if (size == 2) {
                ViewUtils.setVisible(viewHolder.imgTopic1);
                ViewUtils.setVisible(viewHolder.imgTopic2);
                ViewUtils.setGone(viewHolder.imgTopic3);
                viewHolder.imgTopic1.setImageUrl(imgThumList.get(0),
                        AppController.imageLoader);
                viewHolder.imgTopic2.setImageUrl(imgThumList.get(1),
                        AppController.imageLoader);
            } else {
                ViewUtils.setVisible(viewHolder.imgTopic1);
                ViewUtils.setVisible(viewHolder.imgTopic2);
                ViewUtils.setVisible(viewHolder.imgTopic3);
                viewHolder.imgTopic1.setImageUrl(imgThumList.get(0),
                        AppController.imageLoader);
                viewHolder.imgTopic2.setImageUrl(imgThumList.get(1),
                        AppController.imageLoader);
                viewHolder.imgTopic3.setImageUrl(imgThumList.get(2),
                        AppController.imageLoader);
            }
        }

        private void setIconClickListener(final int position, ViewHolder viewHolder) {
            imgOriginList.clear();
            imgOriginList = ((UserTopic) content.get(position)).getOriginImageList();
            viewHolder.imgTopic1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    showPic(imgOriginList, 0);
                }
            });
            viewHolder.imgTopic2.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    showPic(imgOriginList, 1);
                }
            });
            viewHolder.imgTopic3.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    showPic(imgOriginList, 2);
                }
            });
        }

        public void showPic(List<String> urls, int item) {
            if (urls.size() > item) {
                GalleryViewPagerAct
                        .start(AppController.getInstance().getTopAct(), urls.toArray(new String[urls.size()]),
                                item);
            }
        }

        private class ViewHolder {
            View viewHeadLine, viewBottom;
            TextView txtContent, txtTime;
            View viewImage;
            NetworkImageView imgTopic1, imgTopic2, imgTopic3;
            TextView txtPraise, txtReply;
            CheckBox btnSelect;
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_my_topic;
        }

    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
            pageIndex = 1;
            loadData(pageIndex);
        }
        if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
            loadData(pageIndex);
        }
    }
}
