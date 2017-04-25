package com.zxly.o2o.dialog;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.AffirmOrderAct;
import com.zxly.o2o.activity.LoginAct;
import com.zxly.o2o.adapter.ProductParamSelAdapter;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.NewProduct;
import com.zxly.o2o.model.ProductSKUParam;
import com.zxly.o2o.model.Skus;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.AddProductCartRequest;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *     @author dsnx  @version 创建时间：2015-1-20 上午11:31:39    类说明: 
 */
public class ProductPropertySelDialog extends BaseDialog implements
        View.OnClickListener {

    private ListView paramSelListView;
    private ProductParamSelAdapter ppsAdapter;
    private View btnClose, btnReduct, btnAdd, btnJoinCart;
    private TextView txtBuySum, txtPrice, txtProductName, btnOk,txtPrefPrice;
    private List<ProductSKUParam> productSKUParamList;
    private NetworkImageView productIcon;
    private List<Skus> skuList = new ArrayList<Skus>();
    private ParameCallBack callBack;
    private int buySum;
    private int maxSum;
    private NewProduct product;
    private Skus selSkus;
    private int showType;//0:立即购买 1：加入购物车  2：套餐属性选择 3:商品详情属性选择
    private  View footerView;


    public ProductPropertySelDialog() {
        super();
         footerView = LayoutInflater.from(context).inflate(
                R.layout.view_buy_sum_ctr, null);
        btnAdd = footerView.findViewById(R.id.btn_add);
        btnReduct = footerView.findViewById(R.id.btn_reduct);
        txtBuySum = (TextView) footerView.findViewById(R.id.txt_buy_sum);
        paramSelListView.addFooterView(footerView);
        btnAdd.setOnClickListener(this);
        btnReduct.setOnClickListener(this);

        ppsAdapter = new ProductParamSelAdapter(this.context,
                new CallBack() {

                    @Override
                    public void onCall() {
                        Skus sku = ppsAdapter.getSelSku();
                        if (sku != null) {
                            selSkus = sku;
                            if(product.getPreference()>0)
                            {
                                ViewUtils.setTextPrice(txtPrice, (sku.getPrice() - product.getPreference()));
                                ViewUtils.strikeThruText(txtPrefPrice, sku.getPrice());
                            }else
                            {
                                ViewUtils.setTextPrice(txtPrice, sku.getPrice());
                            }
                        } else {
                            selSkus = null;
                            ViewUtils.setText(txtPrice, product.getCurPriceStr());
                        }

                    }
                });

        paramSelListView.setAdapter(ppsAdapter);

        ViewUtils.setText(txtBuySum, buySum);

    }

    @Override
    public int getLayoutId() {

        return R.layout.dialog_property_sel;
    }

    @Override
    protected void initView() {
        paramSelListView = (ListView) findViewById(R.id.lv_param_sel);
        btnClose = findViewById(R.id.btn_close);

        txtPrice = (TextView) findViewById(R.id.txt_price);
        txtPrefPrice= (TextView) findViewById(R.id.txt_pref_price);
        txtProductName = (TextView) findViewById(R.id.txt_product_name);
        productIcon = (NetworkImageView) findViewById(R.id.img_product);
        btnOk = (TextView) findViewById(R.id.btn_ok);
        btnJoinCart = findViewById(R.id.btn_join_cart);
        buySum = 1;
        maxSum = 1;
        btnClose.setOnClickListener(this);
        btnJoinCart.setOnClickListener(this);
        btnOk.setOnClickListener(this);

    }

    public void show(ParameCallBack callBack, NewProduct _product, int _showType) {
        super.show();
        this.product = _product;
        this.skuList = _product.getSkuList();
        this.callBack = callBack;
        this.showType = _showType;
        this.productSKUParamList = _product.getProductSKUParamList();
        this.maxSum = _product.getAmount();
        switch (showType) {
            case 0:
            case 1:
            case 2:
                ViewUtils.setGone(btnJoinCart);
                ViewUtils.setVisible(btnOk);
                ViewUtils.setText(btnOk, "确定");
                if(showType==2)
                {
                    paramSelListView.removeFooterView(footerView);
                }
                break;
            case 3:
                ViewUtils.setVisible(btnOk);
                ViewUtils.setVisible(btnJoinCart);
                ViewUtils.setText(btnOk, "立即购买");
                break;
        }
        ppsAdapter.clear();
        ppsAdapter.setSelSku(this.product.getSelSku());
        ppsAdapter.addItem(productSKUParamList, true);
        ppsAdapter.setSkuList(this.skuList);

        if (this.product.getSelSku() != null) {
            selSkus=this.product.getSelSku();
            if(product.getPreference()>0)
            {
                ViewUtils.setTextPrice(txtPrice, this.product.getSelSku().getPrice() - product.getPreference());
                ViewUtils.strikeThruText(txtPrefPrice, this.product.getSelSku().getPrice());
            }else
            {
                ViewUtils.setTextPrice(txtPrice, this.product.getSelSku().getPrice());
            }

        } else {
            ViewUtils.setText(txtPrice, this.product.getCurPriceStr());
        }

        ViewUtils.setText(txtProductName, this.product.getName());
        productIcon.setImageUrl(this.product.getHeadUrl(),
                AppController.imageLoader);
        ppsAdapter.notifyDataSetChanged();
    }

    public int getBuySum() {
        return buySum;
    }

    @Override
    protected void doOnDismiss() {
        if (showType != 2) {
            this.callBack.onCall(selSkus);
        }

    }

    @Override
    public void onClick(View v) {
        if (v == btnClose) {
            if (showType != 2) {
                this.callBack.onCall(selSkus);
            }

            dismiss();
        } else if (v == btnAdd) {
            addBuySum();
        } else if (v == btnReduct) {
            reductBuySum();
        } else if (v == btnOk) {

            if (selSkus != null) {
                product.setSelSku(selSkus);
                if (showType == 0 || showType == 3) {
                    product.setPcs(buySum);
                   /* BuyItem buyItem = new BuyItem();
                    buyItem.setId(System.currentTimeMillis());
                    buyItem.setItemId(System.currentTimeMillis() + "");
                    buyItem.setPcs(product.getPcs());
                    buyItem.setPrice((selSkus.getPrice() - product.getPreference()));
                    buyItem.setType(product.getTypeCode());
                    buyItem.addProduct(product);
                    VerifyBuyAct.start((Activity) this.context, buyItem);*/
                    AffirmOrderAct.start((Activity) this.context, product, 0, (product.getSelSku().getPrice() - product.getPreference())*product.getPcs());
                } else if (showType == 1) {
                    product.setPcs(buySum);
                    AddProductCartRequest addProductCart = new AddProductCartRequest(product,new CallBack() {
                        @Override
                        public void onCall() {
                            dismiss();
                        }
                    });
                    addProductCart.start();

                } else {
                    product.setPcs(1);
                }
                dismiss();
                this.callBack.onCall(selSkus);
            } else {
                ViewUtils.showToast("请选择出产品属性");
            }

        } else if (v == btnJoinCart) {
            if (Account.user == null) {
                Intent intent = new Intent((Activity) this.context, LoginAct.class);
                ViewUtils.startActivity(intent, (Activity) this.context);
            } else {
                if (selSkus != null) {
                    product.setPcs(buySum);
                    product.setSelSku(selSkus);
                    AddProductCartRequest  addProductCart=new AddProductCartRequest(product, new CallBack() {
                        @Override
                        public void onCall() {
                            dismiss();
                        }
                    });
                    addProductCart.start();
                }else
                {
                    ViewUtils.showToast("请选择出产品属性");
                }
            }


        }

    }

    @Override
    protected boolean isLimitHeight() {

        return true;
    }

    private void addBuySum() {
        if (buySum < maxSum) {
            buySum++;
            ViewUtils.setText(txtBuySum, buySum);
        }

    }

    private void reductBuySum() {
        if (buySum >= 2) {
            buySum--;
            ViewUtils.setText(txtBuySum, buySum);
        }

    }
}
