package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.adapter.ShopAddressInfoAdapter;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.NewProduct;
import com.zxly.o2o.model.UserAddress;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.DefaultAddressRequest;
import com.zxly.o2o.request.OrderBranchsRequest;
import com.zxly.o2o.request.PlaceOrderRequest;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsnx on 2015/12/17.
 */
public class AffirmOrderAct extends BasicAct implements View.OnClickListener,BaseRequest.ResponseStateListener,PullToRefreshBase.OnRefreshListener,AdapterView.OnItemClickListener {

    private ListView  listViewOrder;
    private PullToRefreshListView listViewAddress;
    private AffirmOrderAdapter orderAdapter;
    private ShopAddressInfoAdapter addressAdapter;
    private View btnBack,btnToBuy,checkboxShsm,checkboxDdzq;
    private static List<NewProduct> productList;
    private int channelType;
    private int logisticsType;//物流方式 1：送货上门  2：到店自取
    private UserAddress userAddress;
    private float totalPrice;
    private TextView txtPrice,txtTakeInfo,txtDeliverInfo, btnChangeSm,btnChangeZq;
    private  PlaceOrderRequest placeOrderRequest;
    private View btnDdzq,btnShsm;
    private static ParameCallBack surccedCallBack;
    private boolean isShowAddressList;
    private List<UserAddress> addressList=new ArrayList<UserAddress>();
    private int pageIndex=1;
    private ImageView imgClone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_affirm_order);
        listViewAddress = (PullToRefreshListView) findViewById(R.id.listview);
        txtPrice= (TextView) findViewById(R.id.txt_price);

        channelType=getIntent().getIntExtra("channelType", 0);
        totalPrice=getIntent().getFloatExtra("totalPrice", 0);
        btnBack=findViewById(R.id.btn_back);
        btnToBuy=findViewById(R.id.btn_ok);
        ViewGroup  header= (ViewGroup) LayoutInflater.from(this).inflate(R.layout.footer_affirm_order,null);
        listViewOrder = (ListView) header.findViewById(R.id.listview_order);
        txtDeliverInfo= (TextView) header.findViewById(R.id.txt_deliver_info);
        btnDdzq=header.findViewById(R.id.btn_ddzq);
        btnShsm=header.findViewById(R.id.btn_shsm);
        checkboxDdzq=header.findViewById(R.id.checkbox_ddzq);
        checkboxShsm=header.findViewById(R.id.checkbox_shsm);
        txtTakeInfo= (TextView) header.findViewById(R.id.txt_take_info);
        btnChangeSm = (TextView) header.findViewById(R.id.btn_change_sm);
        btnChangeZq= (TextView) header.findViewById(R.id.btn_change_zq);
        txtPrice.setText(StringUtil.getFormatPrice(totalPrice));
        imgClone= (ImageView) findViewById(R.id.img_clone);
        listViewAddress.addH(header);
        listViewAddress.setDivideHeight(0);
        listViewOrder.setDividerHeight(0);
        orderAdapter =new AffirmOrderAdapter(this);
        addressAdapter=new ShopAddressInfoAdapter(this);
        listViewOrder.setAdapter(orderAdapter);
        listViewAddress.setAdapter(addressAdapter);
        orderAdapter.addItem(productList,true);
        btnToBuy.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnShsm.setOnClickListener(this);
        btnDdzq.setOnClickListener(this);
        imgClone.setOnClickListener(this);
        btnChangeSm.setOnClickListener(this);
        ViewUtils.setRefreshText(listViewAddress);
        listViewAddress.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        listViewAddress.setIntercept(true);
        listViewAddress.setOnRefreshListener(this);
        listViewAddress.setOnItemClickListener(this);
        btnChangeZq.setVisibility(View.GONE);
        txtTakeInfo.setTextColor(getResources().getColor(R.color.blue_3987d8));
        ViewUtils.setTextPrice(txtPrice, totalPrice);
        loadUserDefaultAddress();
        loadShopAddress(pageIndex);
        listViewAddress.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                int[] location = new int[2];
                btnShsm.getLocationInWindow(location);
                if(location[1]<=0)
                {
                    ViewUtils.setVisible(imgClone);
                    imgClone.setImageBitmap(convertViewToBitmap(btnDdzq));
                }else{
                    ViewUtils.setGone(imgClone);
                }
            }
        });

    }
    public static Bitmap convertViewToBitmap(View view){
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bitmap));
      return bitmap;
    }
    private int lastSelectId=-1;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(!checkboxDdzq.isSelected())
        {
            checkboxDdzq.setSelected(true);
            checkboxShsm.setSelected(false);
        }
        logisticsType=Constants.ORDER_DELIVERY_TAKE;
        UserAddress adress= (UserAddress) addressAdapter.getItem((int) id);
        addressAdapter.setCurSelectAddress(adress);
        addressAdapter.updateSingleRow(listViewAddress.getRefreshableView(),adress);

        txtTakeInfo.setTextColor(getResources().getColor(R.color.gray_666666));
        txtTakeInfo.setText("提货地址："+adress.getAddress());
        btnChangeZq.setVisibility(View.VISIBLE);
        if(lastSelectId>-1)
        {
            addressAdapter.updateSingleRow(listViewAddress.getRefreshableView(),addressAdapter.getItem(lastSelectId));
        }
        lastSelectId= (int) id;
        changeAddress();
    }
    @Override
    public void onRefresh(PullToRefreshBase refreshView) {

        if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
            pageIndex = 1;

            loadShopAddress(pageIndex);

        } else if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
                pageIndex++;
                loadShopAddress(pageIndex);

        }
    }
    private void setUserAddress(UserAddress userAddress){
        if(userAddress!=null)
        {
            this.userAddress=userAddress;
            StringBuilder builder=new StringBuilder(userAddress.getName()).append("（");
            builder.append(userAddress.getMobilePhone()).append("）\n");
            builder.append(userAddress.getAddress());
            Account.user.setTakeoutDeliveryId(userAddress.getId());
            Account.user.setTakeDeliveryAddress(userAddress.getAddress());
            txtDeliverInfo.setTextColor(getResources().getColor(R.color.gray_666666));
            ViewUtils.setVisible(btnChangeSm);
            txtDeliverInfo.setText(builder.toString());
        }else {
            txtDeliverInfo.setTextColor(getResources().getColor(R.color.blue_3987d8));
            txtDeliverInfo.setText("编辑收货地址");
            ViewUtils.setGone(btnChangeSm);
        }

    }

    @Override
    public void onClick(View v) {
        if(v==btnBack)
        {
            finish();
        }else if(v==btnToBuy)
        {

            switch (logisticsType){
                case Constants.ORDER_DELIVERY_SEND:
                    if(userAddress!=null)
                    {
                        placeOrderRequest=new PlaceOrderRequest(Constants.ORDER_DELIVERY_SEND,userAddress.getId(),channelType,productList);
                    }else
                    {
                        ViewUtils.showToast("请设置收货地址");
                        return;
                    }
                    break;
                case Constants.ORDER_DELIVERY_TAKE:
                    if(addressAdapter.getCurSelectAddress()!=null)
                    {
                        placeOrderRequest=new PlaceOrderRequest(Constants.ORDER_DELIVERY_TAKE,addressAdapter.getCurSelectAddress().getId(),channelType, productList);
                    }

                    break;
                default:
                    ViewUtils.showToast("请选择配送方式");
                    return;
            }
            placeOrderRequest.setOnResponseStateListener(this);
            placeOrderRequest.start(this);
        }else if(v==btnShsm)
        {
            logisticsType=Constants.ORDER_DELIVERY_SEND;
            checkboxShsm.setSelected(true);
            checkboxDdzq.setSelected(false);
            isShowAddressList=true;
            changeAddress();
        }else if(v==btnDdzq||v==imgClone){
            logisticsType=Constants.ORDER_DELIVERY_TAKE;
            if(addressAdapter.getCurSelectAddress()!=null)
            {
                checkboxShsm.setSelected(false);
                checkboxDdzq.setSelected(true);
            }
            if(addressList.size()>1)
            {
                changeAddress();
            }

        }else if(v== btnChangeSm){
            SelectAddressAct.start(AffirmOrderAct.this, new ParameCallBack() {

                @Override
                public void onCall(Object object) {
                    setUserAddress((UserAddress) object);
                }
            });
        }
    }

    private void changeAddress()
    {
        if(!isShowAddressList)
        {
            isShowAddressList=true;
            addressAdapter.addItem(addressList,true);
            Drawable nav_up=getResources().getDrawable(R.drawable.turn_up2);
            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
            btnChangeZq.setCompoundDrawables(null, null, nav_up, null);
        }else
        {
            isShowAddressList=false;
            addressAdapter.clear();
            addressAdapter.notifyDataSetChanged();
            Drawable nav_up=getResources().getDrawable(R.drawable.turn_down);
            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
            btnChangeZq.setCompoundDrawables(null, null, nav_up, null);
        }
    }
    @Override
    public void onOK() {
        if(channelType==1)//购物车购买
        {
            if(Account.orderCount>=1)
            {
                Account.orderCount=Account.orderCount-productList.size();
                ProductInfoAct.refreshCartCount(2);
            }
            AffirmOrderAct.this.setResult(RESULT_OK);//下单成功通知购物车界面
        }
        if(surccedCallBack!=null)
        {
            surccedCallBack.onCall(productList);
        }
        finish();
        PayAct.start(this, placeOrderRequest.getOrderNo(), Constants.TYPE_PRODUCT_PAY);
    }

    @Override
    public void onFail(int code) {
        ViewUtils.showToast("下单失败");
    }




    private  class AffirmOrderAdapter extends ObjectAdapter{

        public AffirmOrderAdapter(Context _context) {
            super(_context);
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_affirm_order;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null)
            {
                convertView=inflateConvertView();
                holder=new ViewHolder();
                holder.imgProduct= (NetworkImageView) convertView.findViewById(R.id.img_product);
                holder.txtSku= (TextView) convertView.findViewById(R.id.txt_sku);
                holder.txtCurPrice= (TextView) convertView.findViewById(R.id.txt_curPrice);
                holder.txtOldPrice= (TextView) convertView.findViewById(R.id.txt_old_price);
                holder.txtCount= (TextView) convertView.findViewById(R.id.txt_count);
                holder.txtName= (TextView) convertView.findViewById(R.id.txt_name);
                convertView.setTag(holder);
            }else
            {
                holder= (ViewHolder) convertView.getTag();
            }
            NewProduct product = (NewProduct) getItem(position);
            holder.imgProduct.setImageUrl(product.getHeadUrl(),
                    AppController.imageLoader);
            holder.txtSku.setText(product.getRemark());
            if(product.getPreference()>0)
            {
                ViewUtils.setVisible(holder.txtOldPrice);
                ViewUtils.strikeThruText(holder.txtOldPrice, product.getPrice());
            }else
            {
                holder.txtOldPrice.setVisibility(View.INVISIBLE);
            }
            ViewUtils.setText(holder.txtName,product.getName());
            holder.txtCount.setText("x"+product.getPcs());
            ViewUtils.setTextPrice(holder.txtCurPrice,  product.getCurPrice());
            if(position==(getCount()-1))
            {
                ViewUtils.setGone(convertView,R.id.line);
            }
            return convertView;
        }
        class ViewHolder{
            NetworkImageView imgProduct;
            TextView txtName,txtSku,txtCurPrice,txtOldPrice,txtCount;
        }
    }
    private void loadUserDefaultAddress()
    {
        final DefaultAddressRequest  request=new DefaultAddressRequest();
        request.start(this);
        request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                userAddress=request.getUserAddress();
                if(userAddress!=null)
                {
                    setUserAddress(userAddress);
                }else
                {
                    txtDeliverInfo.setTextColor(getResources().getColor(R.color.blue_3987d8));
                    txtDeliverInfo.setText("编辑收货地址");
                    ViewUtils.setGone(btnChangeSm);
                    txtDeliverInfo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SelectAddressAct.start(AffirmOrderAct.this, new ParameCallBack() {

                                @Override
                                public void onCall(Object object) {

                                    setUserAddress((UserAddress) object);

                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onFail(int code) {

            }
        });
    }
    private void loadShopAddress(final int _pageIndex)
    {
        final OrderBranchsRequest request=new OrderBranchsRequest(_pageIndex);
        request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                listViewAddress.onRefreshComplete();

                if(request.addressList.size()>0)
                {
                    addressList.addAll(request.addressList);
                    if(pageIndex==1&&request.addressList.size()==1)
                    {
                        UserAddress shopAddress=addressList.get(0);
                        StringBuilder sb=new StringBuilder();
                        sb.append("提货地址：").append(shopAddress.getAddress()).append("\n");
                        sb.append( shopAddress.getMobilePhone());
                        txtTakeInfo.setTextColor(getResources().getColor(R.color.gray_666666));
                        txtTakeInfo.setText(sb.toString());
                        addressAdapter.setCurSelectAddress(shopAddress);
                    }else
                    {
                        txtTakeInfo.setText("选择提货地址");
                    }

                }else
                {
                    pageIndex--;
                }

            }

            @Override
            public void onFail(int code) {

            }
        });
        request.start();
    }
    public static void start(Activity curAct,List<NewProduct> _productList,int channelType,float totalPrice,ParameCallBack _surccedCallBack){
        surccedCallBack=_surccedCallBack;
        start(curAct, _productList, channelType, totalPrice);
    }
    /**
     *
     * @param curAct
     * @param _productList
     * @param channelType  1:购物车购买 0:直接购买不经过购物车
     * @totalPrice 总价
     */
    public static void start(Activity curAct,List<NewProduct> _productList,int channelType,float totalPrice)
    {
        if(_productList!=null&&!_productList.isEmpty())
        {
            productList=_productList;
            Intent intent;
            if(Account.user != null){
                intent = new Intent(curAct, AffirmOrderAct.class);
                intent.putExtra("channelType",channelType);
                intent.putExtra("totalPrice",totalPrice);
                ViewUtils.startActivity(intent, curAct);
            } else {
                intent = new Intent(curAct, LoginAct.class);
                ViewUtils.startActivity(intent, curAct);
            }
        }else
        {
            ViewUtils.showToast("您没有选择商品!");
        }

    }
    /**
     *
     * @param curAct
     * @param newProduct
     * @param channelType  1:购物车购买 0:直接购买不经过购物车
     * @totalPrice 总价
     */
    public static void start(Activity curAct,NewProduct newProduct,int channelType,float totalPrice)
    {
        productList=new ArrayList<NewProduct>();
        productList.add(newProduct);
        start(curAct, productList, channelType, totalPrice);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        productList=null;
    }
}
