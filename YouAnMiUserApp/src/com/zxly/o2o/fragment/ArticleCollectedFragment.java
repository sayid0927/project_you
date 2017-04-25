package com.zxly.o2o.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.activity.GalleryViewPagerAct;
import com.zxly.o2o.activity.MyCircleThirdAct;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.CollectTopic;
import com.zxly.o2o.model.ShopTopic;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.ArticleCollectedRequest;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.CircleImageView;
import com.zxly.o2o.view.LoadingView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *     @author huangbin  @version 创建时间：2015-2-4 上午11:01:18    类说明: 
 */
public class ArticleCollectedFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener {
    private PullToRefreshListView listView;
    private LoadingView loadingView = null;
    private CollectTopicAdapter adapter;
    private ArticleCollectedRequest myTopicListRequest;
    private long pageIndex = 1;
    private boolean hasInit = false;
    private List<CollectTopic> myTopicList = new ArrayList<CollectTopic>();

    @Override
    protected void initView() {
        loadingView = (LoadingView) findViewById(R.id.view_loading);
        listView = (PullToRefreshListView) findViewById(R.id.list_view);
        listView.setIntercept(true);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        adapter = new CollectTopicAdapter(getActivity());
        listView.setAdapter(adapter);
        ViewUtils.setRefreshText(listView);
        listView.setOnRefreshListener(this);
        adapter.addItem(myTopicList, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hasInit) {
            loadData(pageIndex);
        }
        hasInit = true;
    }

    private void loadData(final long pageId) {
        myTopicListRequest = new ArticleCollectedRequest(pageId);
        myTopicListRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                myTopicList = myTopicListRequest.getCollectedTopicList();
                if (!DataUtil.listIsNull(myTopicList)) {
                    if (pageId == 1) {
                        adapter.clear();
                    }
                    adapter.addItem(myTopicList, true);
                    pageIndex++;
                    loadingView.onLoadingComplete();
                } else {
                    if (pageId == 1) {
                        adapter.clear();
                        adapter.notifyDataSetChanged();
                        loadingView.onDataEmpty("没有收藏的帖子！");
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

    @Override
    protected int layoutId() {
        return R.layout.win_my_topic_collect;
    }

    class CollectTopicAdapter extends ObjectAdapter {
        Drawable drawable;

        public CollectTopicAdapter(Context context) {
            super(context);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflateConvertView();
                viewHolder.viewHeadLine = convertView.findViewById(R.id.view_head_line);
                viewHolder.txtCircle = (TextView) convertView.findViewById(R.id.txt_topic_circle);
                viewHolder.txtContent = (TextView) convertView.findViewById(R.id.txt_content);
                viewHolder.viewImage = convertView.findViewById(R.id.layout_img);
                viewHolder.imgTopic1 = (NetworkImageView) convertView.findViewById(R.id.img_topic1);
                viewHolder.imgTopic2 = (NetworkImageView) convertView.findViewById(R.id.img_topic2);
                viewHolder.imgTopic3 = (NetworkImageView) convertView.findViewById(R.id.img_topic3);
                viewHolder.imgHead = (CircleImageView) convertView.findViewById(R.id.img_head);
                viewHolder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
                viewHolder.txtTime = (TextView) convertView.findViewById(R.id.txt_time);
                viewHolder.txtPraise = (TextView) convertView.findViewById(R.id.txt_praise);
                viewHolder.txtReply = (TextView) convertView.findViewById(R.id.txt_reply);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final CollectTopic collectTopic = (CollectTopic) content.get(position);
            if (position == 0) {
                ViewUtils.setGone(viewHolder.viewHeadLine);
            } else {
                ViewUtils.setVisible(viewHolder.viewHeadLine);
            }
//            if (StringUtil.isNull(collectTopic.getCircleName())) {
//                ViewUtils.setText(viewHolder.txtCircle, "本店交流");
//            } else {
//                ViewUtils.setText(viewHolder.txtCircle, collectTopic.getCircleName());
//            }
//            viewHolder.txtCircle.setBackgroundResource(getCircleBackground(position));
            ViewUtils.setText(viewHolder.txtTime, StringUtil.getShortTime(collectTopic.getCreateTime()));
            ViewUtils.setText(viewHolder.txtContent, collectTopic.getContent());
            String uName = collectTopic.getuName();
            if(StringUtil.isNull(uName)){
                if (collectTopic.getPublishMan() == 1) {
                    viewHolder.imgHead.setImageUrl(collectTopic.getUserThumHeadUrl(), R.drawable.personal_default_head);
                    ViewUtils.setText(viewHolder.txtName, "匿名");
                } else if (collectTopic.getPublishMan() == 2) {
                    viewHolder.imgHead.setImageUrl(collectTopic.getUserThumHeadUrl(), R.drawable.img_about_us);
                    ViewUtils.setText(viewHolder.txtName, "柚安米");
                }
            } else {
                viewHolder.imgHead.setImageUrl(collectTopic.getUserThumHeadUrl(), R.drawable.personal_default_head);
                ViewUtils.setText(viewHolder.txtName, uName);
            }
            ViewUtils.setText(viewHolder.txtPraise, collectTopic.getPraiseAmout());
            if (collectTopic.isPriseFlag()) {
                drawable = context.getResources().getDrawable(R.drawable.zan_press);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                viewHolder.txtPraise.setCompoundDrawables(drawable, null, null, null);
            } else {
                drawable = context.getResources().getDrawable(R.drawable.zan_normal);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                viewHolder.txtPraise.setCompoundDrawables(drawable, null, null, null);
            }
            ViewUtils.setText(viewHolder.txtReply, collectTopic.getReplyAmout());

            String thumImageString = collectTopic.getThumImageUrls();
            final ArrayList<String> imgThumList = new ArrayList<String>();
            if (thumImageString != null) {
                String[] thumImages = thumImageString.split(",");
                Collections.addAll(imgThumList, thumImages);
                if (!imgThumList.isEmpty()) {
                    ViewUtils.setVisible(viewHolder.viewImage);
                    showImage(imgThumList, viewHolder);
//                    setIconClickListener(position, viewHolder);
                } else {
                    ViewUtils.setGone(viewHolder.viewImage);
                }
            } else {
                ViewUtils.setGone(viewHolder.viewImage);
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    byte isShop;
                    if ("forum_topic".equals(collectTopic.getTablename())) {
                        isShop = 0;
                    } else {
                        isShop = 1;
                    }
                    ShopTopic shopTopic = new ShopTopic();
                    shopTopic.setContent(collectTopic.getContent());
                    shopTopic.setPraiseAmout(collectTopic.getPraiseAmout());
                    shopTopic.setReplyAmout(collectTopic.getReplyAmout());
                    ShopTopic.PublishUser publishUser = shopTopic.new PublishUser();
                    publishUser.setNickname(collectTopic.getuName());
                    publishUser.setThumHeadUrl(collectTopic.getUserThumHeadUrl());
                    shopTopic.setPublishUser(publishUser);
                    shopTopic.setCreateTime(collectTopic.getCreateTime());
                    shopTopic.setIsPraise((byte) (collectTopic.isPriseFlag() ? 1 : 2));
                    shopTopic.setThumImageList(imgThumList);
                    String originImageString = collectTopic.getOriginImageUrls();
                    final ArrayList<String> imgOriginList = new ArrayList<String>();
                    if (originImageString != null) {
                        String[] originImages = originImageString.split(",");
                        Collections.addAll(imgOriginList, originImages);
                    }
                    shopTopic.setOriginImageList(imgOriginList);
                    shopTopic.setId((int) collectTopic.getTopicId());
                    shopTopic.setIsCollect((byte)1);
                    MyCircleThirdAct.start(getActivity(), isShop, shopTopic,"详情");
                }
            });
            return convertView;
        }

        private int getCircleBackground(int position) {
            int res = R.drawable.bg_topic_circle_green;
            if (0 == position % 3) {
                res = R.drawable.bg_topic_circle_pink;
            } else if (1 == position % 3) {
                res = R.drawable.bg_topic_circle_yellow;
            } else if (2 == position % 3) {
                res = R.drawable.bg_topic_circle_green;
            }
            return res;
        }

        private void showImage(ArrayList<String> imgThumList, ViewHolder viewHolder) {
            int size = imgThumList.size();
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
            String originImageString = ((CollectTopic) content.get(position)).getOriginImageUrls();
            final ArrayList<String> imgOriginList = new ArrayList<String>();
            if (originImageString != null) {
                String[] originImages = originImageString.split(",");
                Collections.addAll(imgOriginList, originImages);
            }
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
            View viewHeadLine;
            TextView txtCircle;
            TextView txtContent;
            View viewImage;
            NetworkImageView imgTopic1;
            NetworkImageView imgTopic2;
            NetworkImageView imgTopic3;
            CircleImageView imgHead;
            TextView txtName;
            TextView txtTime;
            TextView txtPraise, txtReply;
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_collect_topic;
        }

    }

}
