package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.easemob.chatuidemo.HXApplication;
import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.HXHelper;
import com.easemob.chatuidemo.ui.ChatActivity;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.domain.EaseYAMUser;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.dialog.ProductPropertySelDialog;
import com.zxly.o2o.dialog.SelectChatDialog;
import com.zxly.o2o.model.NewProduct;
import com.zxly.o2o.model.Skus;
import com.zxly.o2o.o2o_user.R;
import  com.zxly.o2o.SnapScrollView.SnapPageLayout;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.PersonalInitRequest;
import com.zxly.o2o.request.ProductInfoRequest;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.BottomProductInfo;
import com.zxly.o2o.view.LoadingView;
import com.zxly.o2o.view.TopProductInfo;

/**
 * Created by dsnx on 2016/1/13.
 */
public class ProductInfoAct extends BasicAct implements  View.OnClickListener{

    private SnapPageLayout snapPageLayout;
    private TopProductInfo topProductInfo;
    private BottomProductInfo bottomProductInfo;
    private ProductInfoRequest productInfoRequest;
    private static ParameCallBack callBack;
    private static int bannerId = -1;
    private static NewProduct product;
    private LoadingView loadingView;
    private View btnBack, btnJoinCart, btnChat, btnCart;
    private TextView btnToBuy;
    private ProductPropertySelDialog proPropSelDialog;
    private  boolean isExistCoupon=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_product_info);
        Intent intent = this.getIntent();

        //by huangbin
        if(intent.hasExtra("produceId")) {
            NewProduct np = new NewProduct();
            product = np;
            product.setId(intent.getLongExtra("produceId", -1));
        }

        bannerId = intent.getIntExtra("bannerId", -1);
        snapPageLayout= (SnapPageLayout) findViewById(R.id.flipLayout);
        productInfoRequest=new ProductInfoRequest(product, bannerId);
        loadingView = (LoadingView) findViewById(R.id.view_loading);
        btnChat = findViewById(R.id.btn_chat);
        btnCart = findViewById(R.id.btn_cart);
        btnBack=findViewById(R.id.btn_back);
        btnToBuy= (TextView) findViewById(R.id.btn_to_buy);
        btnJoinCart=findViewById(R.id.btn_join_cart);
        btnBack.setOnClickListener(this);
        btnToBuy.setOnClickListener(this);
        btnJoinCart.setOnClickListener(this);
        btnChat.setOnClickListener(this);
        btnCart.setOnClickListener(this);

        productInfoRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                loadingView.onLoadingComplete();
                if(productInfoRequest.getShopDiscount()!=null)
                {
                    isExistCoupon=true;
                    btnToBuy.setText("到店优惠");
                }
                topProductInfo=new TopProductInfo(ProductInfoAct.this,productInfoRequest,product,callBack);
                bottomProductInfo=new BottomProductInfo(ProductInfoAct.this,productInfoRequest.content,productInfoRequest.getProductParamList());
                snapPageLayout.setSnapPages(topProductInfo,bottomProductInfo);
                ViewUtils.setVisible(findViewById(R.id.buy_layout));
            }

            @Override
            public void onFail(int code) {
                if (code == 10007||code==20109) {//下架
                    product.setStatus(3);//下架;
                    if (callBack != null) {
                        callBack.onCall(product);
                    }
                    finish();

                }
                ViewUtils.setGone(snapPageLayout);
                loadingView.onLoadingFail();
            }
        });
        loadingView.setOnAgainListener(new LoadingView.OnAgainListener() {

            @Override
            public void onLoading() {
                loadingView.startLoading();
                productInfoRequest.start(this);

            }
        });
        loadingView.startLoading();
        productInfoRequest.start(this);

    }

    public static void start(Activity curAct, NewProduct _product) {
        start(curAct, _product, null);
    }

    public static void start(Activity curAct,long productId)
    {
        start(curAct,productId,-1,null);
    }
    public static void start(Activity curAct, long productId,
                             int bannerId, ParameCallBack _callBack) {
        NewProduct np=new NewProduct();
        product = np;
        product.setId(productId);
        callBack = _callBack;
        Intent intent = new Intent(curAct, ProductInfoAct.class);
        intent.putExtra("bannerId", bannerId);
        ViewUtils.startActivity(intent, curAct);


    }

    public static void start(Activity curAct, NewProduct _product,
                             ParameCallBack _callBack) {
        product = _product;
        callBack = _callBack;
        Intent intent = new Intent(curAct, ProductInfoAct.class);
        ViewUtils.startActivity(intent, curAct);

    }
    /**
     *
     * @param type 1:增加   2:删除
     */
    public static void refreshCartCount(int type)
    {
    /*
        for(Activity act: AppController.actList)
        {
            if(act instanceof ProductInfoAct)
            {
                ProductInfoAct npia= (ProductInfoAct) act;
                npia._refreshCartCount(type);
            }
        }*/
    }
    private void _refreshCartCount(int type)
    {
        /*if(Account.orderCount>0)
        {
            ViewUtils.setVisible(txtProductCount);
        }else
        {
            ViewUtils.setGone(txtProductCount);
        }

        txtProductCount.setText(Account.orderCount + "");*/
    }
    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            finish();
        }else if (v == btnToBuy) {
            if (Account.user == null) {
                LoginAct.start(this);
                return;
            }
            if(isExistCoupon==true)
            {
                ToStorePrivilegeAct.start(this,product.getCurPrice());
            }else
            {

                if (product.getSelSku() == null) {
                    if (proPropSelDialog == null) {
                        proPropSelDialog = new ProductPropertySelDialog();
                    }
                    proPropSelDialog.show(new ParameCallBack() {

                        @Override
                        public void onCall(Object object) {
                            topProductInfo.setSelDesc((Skus) object);

                        }
                    }, product, 0);
                } else {
                    AffirmOrderAct.start(this,product,0,(product.getSelSku().getPrice() - product.getPreference())*product.getPcs());
                }

            }

        }else if (v == btnJoinCart){
            if (Account.user == null) {
                LoginAct.start(this);
                return;
            }
            if (proPropSelDialog == null) {
                proPropSelDialog = new ProductPropertySelDialog();
            }
            proPropSelDialog.show(new ParameCallBack() {

                @Override
                public void onCall(Object object) {
                    topProductInfo.setSelDesc((Skus) object);

                }
            }, product, 1);
        }else if (v == btnChat) {
            if (Account.user != null) {
                HXHelper.getInstance().getYAMContactList();

                if (Account.user.getBelongId() != 0) {
                    goToChat();

                } else {
                    PersonalInitRequest personalInitRequest = new PersonalInitRequest(Account.user
                            .getId(), Config.shopId);
                    personalInitRequest.setOnResponseStateListener(
                            new BaseRequest.ResponseStateListener() {
                                @Override
                                public void onOK() {
                                    if (Account.user.getBelongId() != 0) {
                                        goToChat();

                                    } else {
                                        new  SelectChatDialog().show();
                                    }
                                }

                                @Override
                                public void onFail(int code) {

                                }
                            });
                    personalInitRequest.start();

                }
            } else {
                LoginAct.start(this);
            }

        } else if (v == btnCart) {
            ShopCartAct.start(this);
        }
    }

    private void goToChat() {

        String chatUserId =
                HXApplication.getInstance().parseUserFromID(Account.user.getBelongId(),
                        HXConstant.TAG_SHOP);
        EaseYAMUser user = HXHelper.getInstance().getUserInfo(chatUserId);

        if(user!=null) {

            String name = user.getFirendsUserInfo().getNickname();
            if (TextUtils.isEmpty(name)) {
                name = user.getFirendsUserInfo().getUserName();
            }
            chatUserId = new StringBuffer("").append(name).append("#")
                    .append(Account.user.getBelongId()).append("#").append(chatUserId)
                    .toString();

            EaseConstant.startActivityNormalWithStringForResult(ChatActivity.class, this,
                    chatUserId,
                    EaseConstant.EXTRA_USER_ID);
        }
    }

    @Override
    public void finish() {
        super.finish();
        productInfoRequest.cancel();
        product = null;
        callBack = null;
    }
}
