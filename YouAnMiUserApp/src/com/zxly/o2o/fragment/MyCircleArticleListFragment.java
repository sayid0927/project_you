package com.zxly.o2o.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.zxly.o2o.activity.MyCircleSecondAct;
import com.zxly.o2o.adapter.MyCircleListAdapter;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.ShopArticle;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.zxly.o2o.request.ArticleListRequest;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.MyCircleRequest;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.UMengAgent;

/**
 *     @author huangbin  @version 创建时间：2015-1-29 下午3:05:01    类说明: 
 */
public  class MyCircleArticleListFragment extends BaseListViewFragment implements OnItemClickListener, OnRefreshListener {

    private String pageType;

    public static MyCircleArticleListFragment newInstance(int param1) {
        MyCircleArticleListFragment fragment = new MyCircleArticleListFragment();
        Bundle args = new Bundle();
        args.putInt("fragmentPage", param1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void loadData() {
        myRequest = new ArticleListRequest(page,pageType, Config.shopId);
        myRequest.setOnResponseStateListener(responseStateListener);
        myRequest.start(this);
    }


    @Override
    protected void initListView(Bundle bundle) {
        objectAdapter = new MyCircleListAdapter(content.getContext());
        int fragmentPage = bundle.getInt("fragmentPage");
        switch (fragmentPage){
            case 1:
                pageType = "beginnersee";
                UMengAgent.onEvent(getActivity(), UMengAgent.mustlook_page);
                break;
            case 2:
                pageType = "mobilebuild";
                UMengAgent.onEvent(getActivity(), UMengAgent.phone_upkeep);
                break;
            case 3:
                pageType = "hotarticle";
                UMengAgent.onEvent(getActivity(), UMengAgent.shop_hot_article);
                break;

        }

    }


    protected BaseRequest.ResponseStateListener responseStateListener = new BaseRequest.ResponseStateListener() {

        @Override
        public void onOK() {
            isTokenInvaild = false;
            if ((((MyCircleRequest) myRequest).articles == null || ((MyCircleRequest) myRequest).articles.size() == 0)) {
                if (page == 1) {
                    viewContainer.setDisplayedChild(2, false);
                }
                isLastPage = true;
            } else {
                if (page == 1) {
                    objectAdapter.clear();
                    viewContainer.setDisplayedChild(3, false);
                    objectAdapter.addItem(((MyCircleRequest) myRequest).articles, true);
                } else {
                    objectAdapter.addItem(((MyCircleRequest) myRequest).articles, true);
                }
            }
            mListView.onRefreshComplete();
        }

        @Override
        public void onFail(int code) {
            if (code == 20101) {  //token无效
                isTokenInvaild = false;
            }
            mListView.onRefreshComplete();
            viewContainer.setDisplayedChild(1, true);
        }
    };

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        MyCircleRequest.shopArticle = (ShopArticle) objectAdapter.getItem(position - 1);
        if (MyCircleRequest.shopArticle != null) {
            MyCircleSecondAct.MyCircleLunch(getActivity(), Constants.ARTICLE_DETAIL,"");
        }
    }

}
