package com.zxly.o2o.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.ChooseGroupPeopleAct;
import com.zxly.o2o.activity.ChooseSendAct;
import com.zxly.o2o.activity.SearchActivityAct;
import com.zxly.o2o.adapter.IMSelectArticleAdapter;
import com.zxly.o2o.application.Config;
import com.zxly.o2o.model.PromotionArticle;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.PushArticleRequest;
import com.zxly.o2o.request.SendShopArticleRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

/**
 * Created by hejun on 2016/9/5.
 *选择推送内容---店铺文章
 */
public class SendShopArticleFragment extends BaseFragment implements BaseRequest.ResponseStateListener,AdapterView.OnItemClickListener {

    private LoadingView loadingView;
    private PullToRefreshListView mListView;
    private IMSelectArticleAdapter adapter;
    private SendShopArticleRequest request;
    private int pageIndex=1;
    private EditText searchBtn;
    private int checkPosition = -1;
    private ImageView checkView;
    private LinearLayout btn_send;

    public static  SendShopArticleFragment newInstance(){
        SendShopArticleFragment f=new  SendShopArticleFragment();
        return f;
    }

    @Override
    protected void initView() {
        loadingView =(LoadingView) findViewById(R.id.view_loading11);
        setSearchBtn();
        //loadingView.startLoading();
        mListView = (PullToRefreshListView) findViewById(R.id.listview);
        btn_send = (LinearLayout) findViewById(R.id.btn_send);
        mListView.setIntercept(true);
        ViewUtils.setRefreshText(mListView);
        mListView.setMode(PullToRefreshBase.Mode.BOTH);
        mListView.setOnItemClickListener(this);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
                    loadData(1);
                } else if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
                    loadData(pageIndex);
                }
            }
        });

        loadingView.setOnAgainListener(new LoadingView.OnAgainListener() {
            @Override
            public void onLoading() {
                loadingView.startLoading();
                loadData(1);
            }
        });

        if (adapter ==null){
            adapter = new IMSelectArticleAdapter(getActivity());
        }

        mListView.setAdapter(adapter);

        if(request ==null){
            request = new SendShopArticleRequest();
            request.setOnResponseStateListener(this);
        }

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter.getCheckPosition()!=-1){
                    PromotionArticle promotionArticle = ((PromotionArticle)adapter.getItem(adapter.getCheckPosition()));
                    pushArticle(promotionArticle);
                }else{
                    ViewUtils.showToast("没有要发送的内容");
                }
            }
        });
    }

    private void pushArticle(PromotionArticle article) {
        PushArticleRequest pushArticleRequest = new PushArticleRequest(((ChooseSendAct)getActivity()).fansImeis, article.getArticleId(), Account.user.getId(), ((ChooseSendAct)getActivity()).userIds);
        pushArticleRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                ViewUtils.showToast("操作成功!");
                Config.isShouldShow=true;
                if(ChooseGroupPeopleAct.getInstance()!=null){
                    ChooseGroupPeopleAct.getInstance().finish();
                }
                getActivity().finish();
            }

            @Override
            public void onFail(int code) {
            }
        });
        pushArticleRequest.start(getActivity());
    }

    @Override
    protected void loadInitData() {
        loadData(1);
    }

    public void loadData(final int pageId) {
        pageIndex=pageId;
        if(DataUtil.listIsNull(adapter.getContent()))
            loadingView.startLoading();
        request.setPageIndex(pageId);
        request.start(getActivity());
    }

    @Override
    protected int layoutId() {
        return R.layout.win_send_product;
    }

    @Override
    public void onOK() {
        if (!DataUtil.listIsNull(request.getArticles())) {
            if(pageIndex==1)
                adapter.clear();

            adapter.addItem(request.getArticles(), true);
            request.setArticles(null);
            pageIndex++;
            loadingView.onLoadingComplete();
        } else {
            if(pageIndex==1){
                adapter.clear();
                adapter.notifyDataSetChanged();
                loadingView.onDataEmpty();
                findViewById(R.id.layout_search).setVisibility(View.GONE);
            }else{
                ViewUtils.showToast("没有数据了");
            }
        }

        if (mListView.isRefreshing())
            mListView.onRefreshComplete();

        if(request.hasNextPage){
            mListView.setMode(PullToRefreshBase.Mode.BOTH);
        } else {
            mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        }

    }

    @Override
    public void onFail(int code) {
        if(DataUtil.listIsNull(adapter.getContent()))
            loadingView.onLoadingFail();

        if (mListView.isRefreshing())
            mListView.onRefreshComplete();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (adapter.getCheckPosition() != position - 1) {
            adapter.setCheckPosition(position - 1);
            adapter.notifyDataSetChanged();
        } else {
            adapter.setCheckPosition(-1);
            adapter.notifyDataSetChanged();
        }

//        ImageView clickView = ((ImageView) view.findViewById(R.id.check_icon));
//        if (position == checkPosition) {
//            cleanCheckView(clickView);
//        } else {
//            setCheckView(clickView, position);
//        }
    }

    private void setSearchBtn() {
        searchBtn = (EditText) findViewById(R.id.edit_search);
        searchBtn.setFocusable(false);
        searchBtn.setHint("请输入文章关键字");
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivityAct.start(getActivity(), new ParameCallBack() {
                    @Override
                    public void onCall(Object object) {
                        resetSearchData(object);
                    }
                });
            }
        });
    }

    /**
     * 搜索页面返回的数据处理
     */
    public void resetSearchData(Object object){
        if (object != null) {
            removedSameItem(object);
            adapter.addItem2Head(object, false);
            adapter.setCheckPosition(0);
            adapter.notifyDataSetChanged();
        }
    }

    private void removedSameItem(Object object) {
        for (int i = 0; i < adapter.getCount(); i++) {
            if ((object).equals(adapter.getItem(i))) {
                adapter.getContent().remove(i);
                return;
            }
        }
    }



    private void cleanCheckView(ImageView checkView) {
        checkView.setImageResource(
                R.drawable.ease_check_normal);
        this.checkView = null;
        checkPosition = -1;
    }

    private void setCheckView(ImageView clickView, int position) {

        clickView.setImageResource(
                R.drawable.ease_check_press1);
        if (checkView != null && checkPosition != -1) {
            checkView.setImageResource(R.drawable.ease_check_normal);
        }
        checkPosition = position;
        checkView = clickView;
    }

    public void resetSearchData(int requestCode, int resultCode, Intent data){
        TypeToken<PromotionArticle> articleToken = new TypeToken<PromotionArticle>() {
        };
        addResultToList(articleToken, data);
    }
    private <T> void addResultToList(TypeToken<T> token, Intent data) {
        if (data != null && data.hasExtra("object")) {
            try {
                Object object = GsonParser.getInstance().fromJson(data.getStringExtra("object"),
                        token);

                if (object != null) {
                    if (checkView != null) {
                        cleanCheckView(checkView);
                    }

                    removedSameItem(object);
                    adapter.addItem2Head(object, true);
                    mListView.callItemClick(mListView, 1, 0);
                }
            } catch (AppException e) {
                e.printStackTrace();
            }
        }
    }

}
