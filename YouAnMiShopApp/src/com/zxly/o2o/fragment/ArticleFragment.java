package com.zxly.o2o.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.zxly.o2o.activity.HotArticleDetailAct;
import com.zxly.o2o.activity.NewsArticleDetailAct;
import com.zxly.o2o.adapter.ArticleListAdapter;
import com.zxly.o2o.model.ShopArticle;
import com.zxly.o2o.request.ArticleRequest;
import com.zxly.o2o.request.BaseRequest;

/**
 *     @author huangbin  @version 创建时间：2015-2-4 上午11:01:18    类说明: 
 */
public class ArticleFragment extends BaseListViewFragment {
    private ArticleRequest myRequest;
    private int fragmentPage = 1;  //1:最热 2:最新 3:行业新闻 4:业界培训

    public static ArticleFragment newInstance(int param1) {
        ArticleFragment fragment = new ArticleFragment();
        Bundle args = new Bundle();
        args.putInt("fragmentPage", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void loadData() {
        myRequest = new ArticleRequest(page, fragmentPage);
        myRequest.setOnResponseStateListener(responseStateListener);
        myRequest.start(this);
    }

    @Override
    protected void initListView(Bundle bundle) {
        fragmentPage = bundle.getInt("fragmentPage");
        objectAdapter = new ArticleListAdapter(content.getContext());

        mListView.setDivideHeight(0);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), NewsArticleDetailAct.class);
        if(fragmentPage==1||fragmentPage==2) {
            intent = new Intent(getActivity(), HotArticleDetailAct.class);
            intent.putExtra("shareUrl",
                    ((ShopArticle) objectAdapter.getContent().get(position-1)).getShareUrl());
        }
        intent.putExtra("articleId",
                ((ShopArticle) objectAdapter.getContent().get(position-1)).getId());

        startActivity(intent);
    }

    protected BaseRequest.ResponseStateListener responseStateListener = new BaseRequest.ResponseStateListener() {

        @Override
        public void onOK() {
            isTokenInvaild = false;
            if ((myRequest.articles == null || myRequest.articles.size() == 0)) {
                if (page == 1) {
                    viewContainer.setDisplayedChild(2, false);
                }
                isLastPage = true;
            } else {
                if (page == 1) {
                    objectAdapter.clear();
                    viewContainer.setDisplayedChild(3, false);
                }
                objectAdapter.addItem(myRequest.articles, true);
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
}
