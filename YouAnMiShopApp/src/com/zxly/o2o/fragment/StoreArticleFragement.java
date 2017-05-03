package com.zxly.o2o.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.YamCollegeDetailAct;
import com.zxly.o2o.adapter.StoreArticelAdapter;
import com.zxly.o2o.model.StoreArticle;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.StringUtil;
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
public class StoreArticleFragement extends  BaseFragment implements PullToRefreshBase.OnRefreshListener {
    private PullToRefreshListView mListView;
    private LoadingView loadingView;
    private StoreArticelAdapter storeArticelAdapter;
    private int type;//店铺文章1，平台文章2，自定义文章3
    private String articleTypeId;
    private int pageIndex;
    private CollegeCourseView collegeCourseView;
    private CallBack callBack;
    private boolean hasCall;

    public static StoreArticleFragement newInstance(int type,String articleCode)
    {
        StoreArticleFragement sf=new StoreArticleFragement();
        Bundle bundle=new Bundle();
        bundle.putInt("type",type);
        bundle.putString("articleTypeId",articleCode);
        sf.setArguments(bundle);
        return  sf;
    }
    public static StoreArticleFragement newInstance(int type)
    {
        return newInstance(type,"");
    }
    @Override
    protected void initView(Bundle bundle) {
        type=bundle.getInt("type");
        articleTypeId =bundle.getString("articleTypeId");
        loadingView=(LoadingView) findViewById(R.id.view_loading11);
        mListView = (PullToRefreshListView) findViewById(R.id.listview);
        ViewUtils.setRefreshText(mListView);
        storeArticelAdapter=new StoreArticelAdapter(this.getActivity(),type);
        mListView.setDivideHeight(0);
        mListView.setVerticalScrollBarEnabled(false);
        mListView.getRefreshableView().setFastScrollEnabled(false);
        mListView.setIntercept(true);
        mListView.setOnRefreshListener(this);
        mListView.setAdapter(storeArticelAdapter);


    }



    @Override
    protected int layoutId() {
        return R.layout.tag_listview;
    }
    @Override
    protected void loadInitData() {
        if (DataUtil.listIsNull(storeArticelAdapter.getContent())) {
            loadData(1);
        }
    }
    private void hideCollegeCourse()
    {
        if(collegeCourseView!=null)
        {
            collegeCourseView.setVisibility(View.GONE);
        }
    }
    private void showCollegeCourse() {
        if(type==2)//平台文章
        {
            return;
        }
        if (collegeCourseView == null) {
            RelativeLayout.LayoutParams courseLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            courseLp.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics());
            courseLp.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
            courseLp.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
            courseLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            collegeCourseView = new CollegeCourseView(getActivity(),5);
            collegeCourseView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YamCollegeDetailAct.start(getActivity(),collegeCourseView.getId());
                }
            });
            content.addView(collegeCourseView, courseLp);
        }else
        {
            collegeCourseView.show();
        }

    }
    private void loadData(int _pageIndex)
    {
        if (DataUtil.listIsNull(storeArticelAdapter.getContent())) {
            loadingView.startLoading();
        }
        this.pageIndex=_pageIndex;
        final ArticlesRequest articlesRequest=new ArticlesRequest(articleTypeId,pageIndex,type);
        articlesRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {

                loadingView.onLoadingComplete();
                if (!DataUtil.listIsNull(articlesRequest.storeArticles)) {
                    if (pageIndex == 1)
                    storeArticelAdapter.clear();
                    storeArticelAdapter.addItem(articlesRequest.storeArticles, true);
                    articlesRequest.storeArticles.clear();
                    pageIndex++;
                    hideCollegeCourse();
                } else {
                    //下拉刷新的时候发现数据为空，
                    if (pageIndex == 1) {
                        storeArticelAdapter.clear();
                        storeArticelAdapter.notifyDataSetChanged();
                        switch (type)
                        {
                            case 1://店铺文章


                                if(callBack!=null&&!hasCall){
                                    callBack.onCall();
                                    hasCall=true;
                                }
                                if(Account.user.getRoleType()== Constants.USER_TYPE_ADMIN)
                                {
                                    loadingView.onDataEmpty("店铺还没有文章呢，立即到商户后台发布文章吧", R.drawable.img_default_tired);
                                }else
                                {
                                    loadingView.onDataEmpty("店铺还没有文章呢，提醒老板发布文章吧。\n或者您可以去自定义文章推广您的文章~", R.drawable.img_default_tired);
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
                if(articlesRequest.hasNext){
                    mListView.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
            }

            @Override
            public void onFail(int code) {
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
            UmengUtil.onEvent(getActivity(),new UmengUtil().FIND_REFRESH,null);
        }
        if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
            // 加载上拉数据
            loadData(pageIndex);
            UmengUtil.onEvent(getActivity(),new UmengUtil().FIND_UPLOAD,null);
        }
    }

    public void setParam(CallBack callBack) {
        this.callBack=callBack;
    }

    class  ArticlesRequest extends BaseRequest{
        public List<StoreArticle> storeArticles=new ArrayList<StoreArticle>();
        public boolean hasNext = true;
        public ArticlesRequest(String articleCode,int pageIndex,int type)
        {
            if(!StringUtil.isNull(articleCode))
            {
                addParams("codeId",articleCode);
            }
            addParams("pageIndex",pageIndex);
            addParams("type",type);
            addParams("shopId", Account.user.getShopId());
            addParams("userId",Account.user.getId());
        }

        @Override
        protected void fire(String data) throws AppException {
            try {

                    JSONArray jsonArray=new JSONArray(data);
                    int length=jsonArray.length();
                    for(int i=0;i<length;i++)
                    {
                        JSONObject saJson=jsonArray.getJSONObject(i);
                        StoreArticle storeArticle=new StoreArticle();
                        storeArticle.setArticleId(saJson.optInt("articleId"));
                        storeArticle.setHeadUrl(saJson.optString("headUrl"));
                        storeArticle.setProductArticle(saJson.optInt("isProductArticle"));
                        storeArticle.setRecomend(saJson.optInt("isRecomend")==1?true:false);
                        storeArticle.setScanCount(saJson.optInt("scanCount"));
                        storeArticle.setTitle(saJson.optString("title"));
                        storeArticle.setUrl(saJson.optString("shareUrl"));
                        storeArticle.setUserAppName(saJson.optString("userAppName"));
                        storeArticle.setDescription(saJson.optString("description"));
                        String url = saJson.optString("shareUrl");
                        if(!TextUtils.isEmpty(url)&&url.contains("shareImage=")){
                            String substring = url.replaceAll("(?is).*?shareImage=(.*?)&.*", "$1");
                            storeArticle.setShareImageUrl(substring);
                        }
                        storeArticle.setHasNewLabel(saJson.optInt("hasNewLabel"));
                        if(saJson.has("labels"))
                        {
                            JSONArray labelArray=saJson.getJSONArray("labels");
                            int k=labelArray.length();
                            String[] labels=new String[k];
                            for (int j=0;j<k;j++)
                            {
                                JSONObject labelsJson=labelArray.getJSONObject(j);
                                String label=labelsJson.optString("id")+","+labelsJson.optString("name");
                                labels[j]=label;
                            }
                            storeArticle.setLabels(labels);
                        }
                        storeArticles.add(storeArticle);
                    }

                if(storeArticles.size() < 15){
                    hasNext = false;
                }
            } catch (JSONException e) {
                throw JSONException(e);
            }
        }

        @Override
        protected String method() {
            return "/keduoduo/promote/articles";
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callBack=null;
    }
}
