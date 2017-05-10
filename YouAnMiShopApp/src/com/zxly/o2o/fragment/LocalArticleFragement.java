package com.zxly.o2o.fragment;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Shop.entity.Shop;
import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.YamCollegeDetailAct;
import com.zxly.o2o.adapter.LocalArticleAdapter;
import com.zxly.o2o.dbmanager.CommonUtils;
import com.zxly.o2o.model.LocalArticle;
import com.zxly.o2o.model.LocalArticlesInfo;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.UmengUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.CollegeCourseView;
import com.zxly.o2o.view.LoadingView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsnx on 2016/9/1.
 */
public class LocalArticleFragement extends BaseFragment implements PullToRefreshBase.OnRefreshListener {
    private PullToRefreshListView mListView;
    private LoadingView loadingView;
    private LocalArticleAdapter localArticleAdapter;
    private int type;//店铺文章1，平台文章2，自定义文章3
    private String articleTypeId;
    private int pageIndex;
    private int pageSize = 10;
    private CollegeCourseView collegeCourseView;
    private CallBack callBack;
    private boolean hasCall;
    private TextView txCtys;
    private LocalArticle  localArticle=new LocalArticle();
    private List<Shop> shopDatas = new ArrayList<>();
    public CommonUtils mCommonUtils;

    public static LocalArticleFragement newInstance(int type, String articleCode) {
        LocalArticleFragement sf = new LocalArticleFragement();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putString("articleTypeId", articleCode);
        sf.setArguments(bundle);
        return sf;
    }

    public static LocalArticleFragement newInstance(int type) {
        return newInstance(type, "");
    }

    @Override
    protected void initView(Bundle bundle) {
        mCommonUtils = new CommonUtils(getActivity());
        type = bundle.getInt("type");
        articleTypeId = bundle.getString("articleTypeId");
        loadingView = (LoadingView) findViewById(R.id.view_loading11);
        mListView = (PullToRefreshListView) findViewById(R.id.listview);
        ViewUtils.setRefreshText(mListView);
        localArticleAdapter = new LocalArticleAdapter(this.getActivity(), type);
        mListView.setDivideHeight(0);
        mListView.setVerticalScrollBarEnabled(false);
        mListView.getRefreshableView().setFastScrollEnabled(false);
        mListView.setIntercept(true);
        mListView.setOnRefreshListener(this);
        ViewGroup headView = (ViewGroup) LayoutInflater.from(getActivity()).inflate(R.layout.item_custom_article_list_header, null);
        txCtys = (TextView) headView.findViewById(R.id.tx_ctiy);
        mListView.addH(headView);
        mListView.setAdapter(localArticleAdapter);


    }


    @Override
    protected int layoutId() {
        return R.layout.tag_listview;
    }

    @Override
    protected void loadInitData() {
        if (DataUtil.listIsNull(localArticleAdapter.getContent())) {
            loadData(1);
        }
    }

    private void hideCollegeCourse() {
        if (collegeCourseView != null) {
            collegeCourseView.setVisibility(View.GONE);
        }
    }

    private void showCollegeCourse() {
        if (type == 2)//平台文章
        {
            return;
        }
        if (collegeCourseView == null) {
            RelativeLayout.LayoutParams courseLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            courseLp.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics());
            courseLp.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
            courseLp.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
            courseLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            collegeCourseView = new CollegeCourseView(getActivity(), 5);
            collegeCourseView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YamCollegeDetailAct.start(getActivity(), collegeCourseView.getId());
                }
            });
            content.addView(collegeCourseView, courseLp);
        } else {
            collegeCourseView.show();
        }

    }

    private void loadData(int _pageIndex) {
        if (DataUtil.listIsNull(localArticleAdapter.getContent())) {
            loadingView.startLoading();
        }
        this.pageIndex = _pageIndex;
        final ArticlesRequest articlesRequest = new ArticlesRequest(pageIndex, pageSize, type);
        articlesRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {

                loadingView.onLoadingComplete();
                txCtys.setText(localArticle.getCityName()+localArticle.getAreaName());

                if (!DataUtil.listIsNull(articlesRequest.localArticlesInfoList)) {
                    if (pageIndex == 1)
                        localArticleAdapter.clear();
                    localArticleAdapter.addItem(articlesRequest.localArticlesInfoList, true);
                    localArticleAdapter.setLocalArticle(localArticle);
                    articlesRequest.localArticlesInfoList.clear();
                    pageIndex++;
                    hideCollegeCourse();
                } else {
                    //下拉刷新的时候发现数据为空，
                    if (pageIndex == 1) {
                        localArticleAdapter.clear();
                        localArticleAdapter.notifyDataSetChanged();
                        switch (type) {
                            case 1://店铺文章

                                if (callBack != null && !hasCall) {
                                    callBack.onCall();
                                    hasCall = true;
                                }
                                if (Account.user.getRoleType() == Constants.USER_TYPE_ADMIN) {
                                    loadingView.onDataEmpty("本地热文还没有文章呢，立即到商户后台发布文章吧", R.drawable.img_default_tired);
                                } else {
                                    loadingView.onDataEmpty("本地热文还没有文章呢，提醒老板发布文章吧。\n或者您可以去自定义文章推广您的文章~", R.drawable.img_default_tired);
                                }
                                break;
                            case 2://平台文章
                                loadingView.onDataEmpty("暂无内容", R.drawable.img_default_tired);
                                break;
                        }
                        showCollegeCourse();
                    }
                }
                if (mListView.isRefreshing())
                    mListView.onRefreshComplete();
                if (articlesRequest.hasNext) {
                    mListView.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
            }

            @Override
            public void onFail(int code) {
             hideCollegeCourse();
             loadingView.onLoadingFail();
            }
        });
        articlesRequest.start(this);

        loadingView.setOnAgainListener(new LoadingView.OnAgainListener() {
            @Override
            public void onLoading() {
                loadingView.startLoading();
                articlesRequest.start(this);
            }
        });
    }

    @Override
    protected void initView() {

    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
            // 加载下啦数据
            loadData(1);
            UmengUtil.onEvent(getActivity(), new UmengUtil().FIND_REFRESH, null);
        }
        if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
            // 加载上拉数据
            loadData(pageIndex);
            UmengUtil.onEvent(getActivity(), new UmengUtil().FIND_UPLOAD, null);
        }
    }

    public void setParam(CallBack callBack) {
        this.callBack = callBack;
    }

    class ArticlesRequest extends BaseRequest {
        List<LocalArticlesInfo> localArticlesInfoList = new ArrayList<>();

        public boolean hasNext = true;

        public ArticlesRequest(int pageIndex, int pageSize, int type) {
            addParams("pageIndex", pageIndex);
            addParams("pageSize", pageSize);
            addParams("shopId", Account.user.getShopId());
        }

        @Override
        protected void fire(String data) throws AppException {

            try {
                JSONObject json = new JSONObject(data);

                if (json.has("areaId")) {
                    localArticle.setAreaId(json.getInt("areaId"));
                }
                if (json.has("areaName")) {
                    localArticle.setAreaName(json.getString("areaName"));
                }
                if (json.has("cityId")) {
                    localArticle.setCityId(json.getLong("cityId"));
                }
                if (json.has("cityName")) {
                    localArticle.setCityName(json.getString("cityName"));
                }
                if (json.has("articles")) {

                    JSONArray articlesArray = json.getJSONArray("articles");
                    int length = articlesArray.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject artJson = articlesArray.getJSONObject(i);
                        LocalArticlesInfo localArticlesItem = new LocalArticlesInfo();
                        if (artJson.has("headImage"))
                        localArticlesItem.setHeadImage(artJson.getString("headImage"));
                        if (artJson.has("id")) {
                            long clickId= artJson.getLong("id");
                            localArticlesItem.setId(clickId);
                            if(shopDatas.size()!=0)shopDatas.clear();
                            shopDatas= mCommonUtils.queryByBuilder(String.valueOf(clickId));
                            if (shopDatas.size() == 0) {
                                Shop shop = new Shop();
                                shop.setClickid(String.valueOf(clickId));
                                shop.setIsclick(true);
                                mCommonUtils.insertShop(shop);//插入数据
                            }
                        }
                        if (artJson.has("label"))
                        localArticlesItem.setLabel(artJson.getString("label"));
                        if (artJson.has("allReadNum"))
                        localArticlesItem.setAllReadNum(artJson.getInt("allReadNum"));
                        if (artJson.has("shareNum"))
                        localArticlesItem.setShareNum(artJson.getInt("shareNum"));
                        if (artJson.has("shareUrl"))
                         localArticlesItem.setShareUrl(artJson.getString("shareUrl"));
                        if (artJson.has("title"))
                        localArticlesItem.setTitle(artJson.getString("title"));
                        if (artJson.has("publishTime"))
                        localArticlesItem.setPublishTime(artJson.getLong("publishTime"));
                        localArticlesItem.setIsclick(true);
                        //localArticlesItem.setDescription(artJson.getString("description"));
                        localArticlesInfoList.add(localArticlesItem);
                    }
                    localArticle.setArticlesInfoList(localArticlesInfoList);
                }
                localArticle.setArticlesInfoList(localArticlesInfoList);
            } catch (JSONException e) {
                throw JSONException(e);
            }
        }

        @Override
        protected String method() {
            return "/localArticle/list";
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callBack = null;
    }
}
