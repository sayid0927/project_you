package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.adapter.ActiveUserAdapter;
import com.zxly.o2o.adapter.DiscoverAdapter;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.dialog.ShareDialog;
import com.zxly.o2o.model.ProductArticles;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.AppFoundArticleInfoRequest;
import com.zxly.o2o.request.ArticelShareRequest;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.FoundArticlePraiseRequest;
import com.zxly.o2o.util.ShareListener;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.HorizontalListView;
import com.zxly.o2o.view.LoadingView;

/**
 * Created by dsnx on 2015/12/11.
 */
public class DiscoverInfoAct extends BasicAct implements View.OnClickListener{

    private ListView productListView;
    private ActiveUserAdapter activeUserAdapter;
    private DiscoverAdapter discoverAdapter;
    private long articelId;
    private NetworkImageView imgBanner;
    private TextView txtArticleTitle,txtContent,btnZan,btnShare;
    private LoadingView loadingView;
    private View btnBack,viewLike;
    private int priaseAmount;
    private int shareAmount;
    private ShareDialog shareDialog;
    private String title;
    private String imagUrl;
    private String shareUrl;
    private String content;
    private View btnToTopBtn;
    AppFoundArticleInfoRequest request;
    private int isPraise;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_discover_info);
        articelId=getIntent().getLongExtra("articelId", 0);
        productListView= (ListView) findViewById(R.id.goods_listview);
        loadingView = (LoadingView) findViewById(R.id.view_loading);
        final ViewGroup discoverHead= (ViewGroup) LayoutInflater.from(this).inflate(R.layout.discover_head, null);
        viewLike= discoverHead.findViewById(R.id.view_like);
        imgBanner= (NetworkImageView) discoverHead.findViewById(R.id.img_banner);
        txtArticleTitle= (TextView) discoverHead.findViewById(R.id.txt_article_title);
        txtContent= (TextView) discoverHead.findViewById(R.id.txt_content);
        btnToTopBtn=findViewById(R.id.btn_toTopBtn);
        btnBack=findViewById(R.id.btn_back);
        btnShare= (TextView) discoverHead.findViewById(R.id.btn_share);
        btnZan= (TextView) discoverHead.findViewById(R.id.btn_zan);
        activeUserAdapter=new ActiveUserAdapter(this);
        discoverAdapter=new DiscoverAdapter(this);
        btnShare.setOnClickListener(this);
        btnZan.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        viewLike.setOnClickListener(this);
        btnToTopBtn.setOnClickListener(this);
        HorizontalListView  hListView= (HorizontalListView) discoverHead.findViewById(R.id.hListView);
        hListView.setAdapter(activeUserAdapter);
        productListView.addHeaderView(discoverHead);
       // productListView.setDividerHeight(0);
        productListView.setAdapter(discoverAdapter);

        productListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
                        // 判断滚动到顶部
                        if (productListView.getFirstVisiblePosition() > 0) {
                            btnToTopBtn.setVisibility(View.VISIBLE);
                        }else
                        {
                            btnToTopBtn.setVisibility(View.GONE);
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            }
        });
        request=new AppFoundArticleInfoRequest(articelId);
        request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                loadingView.onLoadingComplete();
                ViewUtils.setVisible(productListView);
                imgBanner.setImageUrl(request.getImageUrl(), AppController.imageLoader);
                title=request.getTitle();
                imagUrl=request.getImageUrl();
                shareUrl=request.getShareUrl();
                if(StringUtil.isNull(shareUrl))
                {
                    ViewUtils.setGone(btnShare);
                }
                ViewUtils.setText(txtArticleTitle, request.getTitle());
                content=request.getContent();
                txtContent.setText(request.getContent());
                priaseAmount=request.getPraiseAmount();
                shareAmount=request.getShareAmount();
                btnZan.setText(" 赞一个（" + priaseAmount + "）");
                btnShare.setText(" 分享（"+shareAmount+"）");
                isPraise=request.getIsPraise();
                if(isPraise==1)//已经点赞
                {
                    btnZan.setSelected(true);
                }
                if(request.getListUser().isEmpty())
                {
                    ViewUtils.setGone(viewLike);
                }else{
                    activeUserAdapter.addItem(request.getListUser());
                }
                discoverAdapter.addItem(request.getProductArticlesList());
            }

            @Override
            public void onFail(int code) {
                loadingView.onLoadingFail();
            }
        });
        loadingView.startLoading();
        request.start(this);


    }
    public static void start(Activity curAct,long articelId)
    {
        Intent intent = new Intent(curAct, DiscoverInfoAct.class);
        intent.putExtra("articelId",articelId);
        ViewUtils.startActivity(intent, curAct);
    }

    @Override
    public void onClick(View v) {
        if(btnZan==v)
        {
            if (Account.user == null) {
                LoginAct.start(this);
                return;
            }
            if(isPraise==1)
            {
                ViewUtils.showToast("您已经点过了");
                return ;
            }
            FoundArticlePraiseRequest fapRequest=new FoundArticlePraiseRequest(articelId);
            fapRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                @Override
                public void onOK() {
                    btnZan.setText(" 赞一个（" + (priaseAmount + 1) + "）");
                    btnZan.setSelected(true);
                    ViewUtils.setVisible(viewLike);
                    if(Account.user!=null)
                    {
                        if(!activeUserAdapter.getContent().contains(Account.user))
                        {
                            activeUserAdapter.addItem(Account.user,true);
                        }
                    }

                }

                @Override
                public void onFail(int code) {
                    if(activeUserAdapter.getContent().contains(Account.user))
                    {
                        btnZan.setSelected(true);
                    }

                }
            });
            fapRequest.start(this);
        }else if (btnBack == v) {
            finish();
        }else  if(btnShare==v)
        {
            if(shareDialog==null)
            {
                shareDialog=new ShareDialog();
            }
            shareDialog.show(title,content,shareUrl,imagUrl, new ShareListener() {
                @Override
                public void onComplete(Object var1) {
                    shareAmount++;
                    btnShare.setText(" 分享（"+shareAmount+"）");
                    new ArticelShareRequest(articelId).start();
                }

                @Override
                public void onFail(int errorCode) {

                }
            });
       }else if(v==viewLike)
        {
            LikeAndCollectHistoryAct.start(articelId+"",LikeAndCollectHistoryAct.TYPE_ARTICLE_PRODUCT,"点赞&分享");
        }else if(v==btnToTopBtn)
        {
            productListView.smoothScrollToPosition(0);

        }
    }
}
