package com.zxly.o2o.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.SnapScrollView.McoyScrollView;
import com.zxly.o2o.SnapScrollView.TopPage;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.ComboSelAct;
import com.zxly.o2o.activity.GalleryViewPagerAct;
import com.zxly.o2o.activity.LikeAndCollectHistoryAct;
import com.zxly.o2o.activity.LoginAct;
import com.zxly.o2o.activity.ProductInfoAct;
import com.zxly.o2o.activity.ToStorePrivilegeAct;
import com.zxly.o2o.adapter.ActiveUserAdapter;
import com.zxly.o2o.adapter.DeliveryModeAdapter;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.dialog.ProductPropertySelDialog;
import com.zxly.o2o.dialog.ShareDialog;
import com.zxly.o2o.model.NewProduct;
import com.zxly.o2o.model.Pakage;
import com.zxly.o2o.model.Skus;
import com.zxly.o2o.model.User;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.ProductCollectRequest;
import com.zxly.o2o.request.ProductInfoRequest;
import com.zxly.o2o.request.ProductLikeRequest;
import com.zxly.o2o.request.ProductShareRequest;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.ShareListener;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.viewpagerindicator.CirclePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsnx on 2016/1/13.
 */
public class TopProductInfo extends TopPage implements  View.OnClickListener {
    private ViewPager bannerView;
    private List<NetworkImageView> bannerList = new ArrayList<NetworkImageView>();
    private GridView gvDeliveryMode,gvCombo;
    private DeliveryModeAdapter dmAdapter;
    private  NewProduct product;
    private static int bannerId = -1;
    private View btnPropSelLayout, labelLayout, lineTitle, lineLable,comboLayout,labelCombo,btnTakeYh;
    private View  lineTurn;
    private ImageView imgTurn;
    private  LinearLayout gift1,gift2;
    private TextView txtHyName1,txtHyType1,txtHyName2,txtHyType2;
    private ProductPropertySelDialog proPropSelDialog;
    private TextView txtSelDesc, txtName,btnCollect,btnLike,btnShare,btnViewLike;
    private String[] imageUrls;
    private List<String> labelList;
    private HorizontalListView hListView;
    private TextView  txtPrice, txtOrigPrice;
    private McoyScrollView mcoyScrollView;
    private BannerAdapter bannerAdapter;
    private String selDesc;
    private PackageAdapter packAdapter;
    private View viewContent, buyLayout;
    private ProductInfoRequest productInfoRequest;
    private List<Pakage> packageList;
    private CirclePageIndicator indicator;
    private int isLike;//1:喜欢 2：未喜欢
    private ActiveUserAdapter userAdapter;
    private ShareDialog shareDialog;
    private int enjoyAmount,collectAmount,shareAmount;
    private ParameCallBack callBack;
    private List<User> enjoyMensList =new ArrayList<User>();
    public TopProductInfo(Context context,ProductInfoRequest productInfoRequest,NewProduct product,ParameCallBack callBack) {
        super(context);
        this.productInfoRequest=productInfoRequest;
        this.product=product;
        this.callBack=callBack;
        init();
    }
    private void init()
    {
        gvDeliveryMode = (GridView) findViewById(R.id.gridView_dm);
        hListView= (HorizontalListView) findViewById(R.id.hListView);
        btnPropSelLayout = findViewById(R.id.prop_sel_layout);
        txtSelDesc = (TextView) findViewById(R.id.txt_sel_desc);
        labelCombo=findViewById(R.id.label_combo);
        gvCombo = (GridView) findViewById(R.id.gridView_combo);
        txtPrice = (TextView) findViewById(R.id.txt_price);
        txtOrigPrice = (TextView) findViewById(R.id.txt_orig_price);
        comboLayout = findViewById(R.id.combo_layout);
        mcoyScrollView = (McoyScrollView)findViewById(R.id.product_scrollview);
        viewContent = findViewById(R.id.view_content);
        labelLayout = findViewById(R.id.label_layout);
        lineLable = findViewById(R.id.line_lable);
        lineTurn = findViewById(R.id.line_turn);
        lineTitle = findViewById(R.id.line_title);
        btnTakeYh=findViewById(R.id.btn_take_yh);
        btnViewLike= (TextView) findViewById(R.id.view_like);
        btnCollect = (TextView) findViewById(R.id.btn_collect);
        btnLike= (TextView) findViewById(R.id.btn_like);
        indicator= (CirclePageIndicator) findViewById(R.id.indicator);
        btnShare = (TextView) findViewById(R.id.btn_share);
        buyLayout = findViewById(R.id.buy_layout);
        imgTurn = (ImageView) findViewById(R.id.img_turn);
        txtName = (TextView) findViewById(R.id.txt_name);
        gift1= (LinearLayout) findViewById(R.id.gift1);
        gift2= (LinearLayout) findViewById(R.id.gift2);

        txtHyName1= (TextView) findViewById(R.id.txt_hyName1);
        txtHyType1= (TextView) findViewById(R.id.txt_hyType1);
        txtHyName2= (TextView) findViewById(R.id.txt_hyName2);
        txtHyType2= (TextView) findViewById(R.id.txt_hyType2);

        dmAdapter = new DeliveryModeAdapter(context);
        btnShare = (TextView) findViewById(R.id.btn_share);
        bannerAdapter = new BannerAdapter();
        packAdapter = new PackageAdapter(context);
        gvDeliveryMode.setAdapter(dmAdapter);
        gvCombo.setAdapter(packAdapter);
        userAdapter=new ActiveUserAdapter(context);
        hListView.setAdapter(userAdapter);
        bannerView = (ViewPager) findViewById(R.id.baner_pager);
        bannerView.setAdapter(bannerAdapter);
        bannerView.setPageMargin(10);
        bannerView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int index) {

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });


        imgTurn.setTag(false);
        lineTurn.setOnClickListener(this);
        btnPropSelLayout.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnCollect.setOnClickListener(this);
        btnLike.setOnClickListener(this);
        btnViewLike.setOnClickListener(this);
        btnTakeYh.setOnClickListener(this);
        initValue();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_top_product_info;
    }

    @Override
    public void onClick(View view) {
         if (view == lineTurn) {
            boolean isExpan = (Boolean) imgTurn.getTag();
            if (isExpan) {
                isExpan = false;
            } else {
                isExpan = true;
            }
            imgTurn.setTag(isExpan);
            if (isExpan) {
                dmAdapter.clear();
                dmAdapter.addItem(labelList);
                imgTurn.setImageResource(R.drawable.turn_up1);
            } else {
                dmAdapter.clear();
                imgTurn.setImageResource(R.drawable.turn_pull1);
                dmAdapter.addItem(labelList.subList(0, 3));
            }

            dmAdapter.notifyDataSetChanged();
        }  else if (view == btnPropSelLayout) {
            if (proPropSelDialog == null) {
                proPropSelDialog = new ProductPropertySelDialog();
            }

            proPropSelDialog.show(new ParameCallBack() {

                @Override
                public void onCall(Object object) {
                    setSelDesc((Skus) object);

                }
            }, product, 3);

        } else if (view == btnCollect) {
             collectProduct();
         }else if(view==btnLike){
             likeProduct();
         }else if(view == btnViewLike){
             LikeAndCollectHistoryAct.start(product.getId()+"",LikeAndCollectHistoryAct.TYPE_PRODUCT,"喜欢&收藏&分享");
         }else if(view==btnShare)
         {
             if(shareDialog==null)
             {
                 shareDialog=new ShareDialog();
             }
             StringBuilder desc=new StringBuilder("售价：");
             desc.append(product.getCurPriceStr()).append("\n\n").append(Account.shopInfo.getName());

             if(Account.user==null){
                 LoginAct.start(AppController.getInstance().getTopAct());
                 return;
             }

             shareDialog.show(product.getName(), desc.toString(), product.getShareUrl()+"&promotionUserId=" + Account.user.getId(), product.getHeadUrl(), new ShareListener() {
                 @Override
                 public void onComplete(Object var1) {
                     shareAmount++;
                     btnShare.setText("  分享  ( " + shareAmount + " )");
                     new ProductShareRequest(product.getId()).start();
                 }

                 @Override
                 public void onFail(int errorCode) {

                 }
             });
         }else if(view==btnTakeYh)
         {
             ToStorePrivilegeAct.start((Activity) context,product.getCurPrice());
         }

    }

    public void setSelDesc(Skus skus)
    {
        if (skus != null) {
            product.setSelSku(skus);
            ViewUtils.setText(txtSelDesc,"已选择  "+ product.getSelSku()
                    .getParamComNames());
            ViewUtils.setTextPrice(txtPrice, (product.getSelSku()
                    .getPrice() - product.getPreference()));
        } else {
            product.setSelSku(null);
            ViewUtils.setText(txtSelDesc, "请选择  "+selDesc);
            ViewUtils.setText(txtPrice, product.getCurPriceStr());
        }
    }
    private void initDdyh(View viewBg, TextView txtHyName,TextView txtHyType, String[] str)
    {
        if(str[0].equals("1"))
        {
            ViewUtils.setVisible(viewBg);
            viewBg.setBackgroundResource(R.drawable.ddyh_green_sawtooth_bg);
            txtHyType.setTextColor(Color.parseColor("#06766a"));
            ViewUtils.setText(txtHyType,"现金抵扣");
        }else
        {
            ViewUtils.setVisible(viewBg);
            viewBg.setBackgroundResource(R.drawable.ddyh_red_sawtooth_bg);
            txtHyType.setTextColor(Color.parseColor("#9a1355"));
            ViewUtils.setText(txtHyType,"礼品赠送");
        }
        ViewUtils.setText(txtHyName,str[1]);
    }
    private void initValue() {
        ViewUtils.setVisible(viewContent);
        ViewUtils.setVisible(buyLayout);
        packageList = productInfoRequest.getPackageList();
        imageUrls = productInfoRequest.getImageUrls();
        labelList = productInfoRequest.getLabelList();
        selDesc = productInfoRequest.getSelDesc();

        enjoyAmount = productInfoRequest.getEnjoyAmount();
        collectAmount = productInfoRequest.getCollectAmount();
        shareAmount = productInfoRequest.getShareAmount();
        if(productInfoRequest.getShopDiscount()!=null)
        {
            ViewUtils.setVisible(btnTakeYh);
            String[] shopDiscount=productInfoRequest.getShopDiscount();
            for(int i=0;i<shopDiscount.length;i++)
            {
                String[] str=shopDiscount[i].split(",");
                if(i==0)
                {
                    initDdyh(gift1,txtHyName1,txtHyType1,str);
                }else
                {
                    initDdyh(gift2,txtHyName2,txtHyType2,str);
                }
            }
        }

        ViewUtils.setText(btnLike, "  喜欢  ( " + enjoyAmount + " )");
        ViewUtils.setText(btnCollect, "  收藏  ( " + collectAmount + " )");
        ViewUtils.setText(btnShare, "  分享  ( " + shareAmount + " )");

        ViewUtils.setText(txtSelDesc, selDesc);
        ViewUtils.setText(txtPrice, product.getCurPriceStr());
        if (product.getPreference() > 0) {
            ViewUtils.setVisible(txtOrigPrice);
            ViewUtils.strikeThruText(txtOrigPrice,
                    product.getOrigPriceStr());

        } else {
            ViewUtils.setGone(txtOrigPrice);
        }
        ViewUtils.setText(txtName, product.getName());
        initTimeItem();
        if (packageList.isEmpty()) {
            ViewUtils.setGone(comboLayout);
            ViewUtils.setGone(labelCombo);

        } else {
            packAdapter.addItem(packageList, true);
        }
        if (labelList.size() <= 3) {//小于等于3个隐藏更多
            if (labelList.isEmpty()) {
                ViewUtils.setGone(labelLayout);
                ViewUtils.setGone(lineLable);
            } else {
                ViewUtils.setGone(imgTurn);
                dmAdapter.addItem(labelList, true);
            }

        } else {
            imgTurn.setImageResource(R.drawable.turn_pull1);
            dmAdapter.addItem(labelList.subList(0, 3), true);
        }

        int imgLength = imageUrls.length;
        for (int i = 0; i < imgLength; i++) {
            NetworkImageView imgView = new NetworkImageView(context);
            imgView.setImageUrl(imageUrls[i],
                    AppController.imageLoader);
            imgView.setTag(i);
            imgView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    GalleryViewPagerAct.start(
                            (Activity) context, imageUrls,
                            (Integer) v.getTag());
                }
            });
            bannerList.add(imgView);
        }
        enjoyMensList = productInfoRequest.getListUser();
        if(enjoyMensList.isEmpty())
        {
            ViewUtils.setGone(findViewById(R.id.view_like));
        }
        userAdapter.addItem(enjoyMensList, true);
        indicator.setViewPager(bannerView, bannerList.size(), 0);
        bannerAdapter.notifyDataSetChanged();
        changeCollectView();
        isLike = productInfoRequest.getIsLike();
        changeLikeView();

    }

    @Override
    public boolean isAtBottom() {
        int scrollY = mcoyScrollView.getScrollY();
        int height = mcoyScrollView.getHeight();
        int scrollViewMeasuredHeight = mcoyScrollView.getChildAt(0).getMeasuredHeight();

        if ((scrollY + height) >= scrollViewMeasuredHeight) {
            return true;
        }
        return false;
    }

    public void likeProduct()
    {
        if (Account.user == null) {
            LoginAct.start((Activity) context);
            return;
        }
        if(isLike==1)
        {
            ViewUtils.showToast("您已经喜欢过了");
            return;
        }
        final ProductLikeRequest request=new ProductLikeRequest(product.getId());
        request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                if(isLike==1)//喜欢
                {
                    isLike=2;
                    enjoyAmount--;
                }else
                {
                    isLike=1;
                    enjoyAmount++;
                }
                changeLikeView();

                if(callBack!=null)
                {
                    product.setEnjoyAmount(enjoyAmount);
                    callBack.onCall(product);
                }

            }

            @Override
            public void onFail(int code) {

            }
        });
        request.start(this);

    }
    public void collectProduct() {
        if (Account.user == null) {
            LoginAct.start((Activity) context);
            return;
        }
        final ProductCollectRequest request = new ProductCollectRequest(product);
        request.setTag(this);
        request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                product.setCollect(request.getCollect());
                if(request.getCollect()==1)
                {
                    collectAmount++;
                }else
                {
                    collectAmount--;
                }
                changeCollectView();

            }

            @Override
            public void onFail(int code) {

            }
        });
        request.start(this);
    }


    public void changeLikeView()
    {
        if(isLike==1)
        {
            if(!userAdapter.getContent().contains(Account.user))
            {
                ViewUtils.setVisible(findViewById(R.id.view_like));
                userAdapter.addItem(Account.user,true);
            }

            ViewUtils.setDrawableLeft(btnLike,context.getResources().getDrawable(R.drawable.like_press));
        } else {

            ViewUtils.setDrawableLeft(btnLike, context.getResources().getDrawable(R.drawable.like_normal));
        }
        ViewUtils.setText(btnLike, "  喜欢  ( " + enjoyAmount + " )");


    }
    public void changeCollectView() {
        if (product.getCollect() == 1) {
            if(!userAdapter.getContent().contains(Account.user))
            {
                ViewUtils.setVisible(findViewById(R.id.view_like));
                userAdapter.addItem(Account.user,true);
            }
            ViewUtils.setDrawableLeft(btnCollect,context.getResources().getDrawable(R.drawable.collect_press));
        } else {
            ViewUtils.setDrawableLeft(btnCollect,context.getResources().getDrawable(R.drawable.collect_normal));
        }
        ViewUtils.setText(btnCollect, "  收藏  ( " + collectAmount + " )");
    }
    private void initTimeItem() {
        final LinearLayout time_bg = (LinearLayout) findViewById(R.id.layout_time_bg);
        TimeCutDownLayout timeLayout = (TimeCutDownLayout) findViewById(R.id.layout_time);

        if (product.getTypeCode() == 1 && product.getResidueTime() > 1000) {
            time_bg.setVisibility(View.VISIBLE);
            timeLayout.setResideTime(product.getResidueTime());
            timeLayout.setOnTimeFinishCallBack(new CallBack() {
                @Override
                public void onCall() {
                    ViewUtils.setGone(time_bg);
                    product.setPreference(0);
                    ViewUtils.setText(txtPrice, product.getCurPriceStr());
                    ViewUtils.setGone(txtOrigPrice);
                }
            });
        }

    }
    public class PackageAdapter extends ObjectAdapter {

        public PackageAdapter(Context _context) {
            super(_context);
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflateConvertView();
                holder = new ViewHolder();
                holder.txtComboName = (TextView) convertView
                        .findViewById(R.id.txt_combo_name);
                holder.txtPrefPrice= (TextView) convertView.findViewById(R.id.txt_pref_price);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final Pakage productCombo = (Pakage) getItem(position);
            ViewUtils.setText(holder.txtComboName, productCombo.getName()+"，优惠");
            ViewUtils.setTextPrice(holder.txtPrefPrice, productCombo.getMaxPrefPrice());
            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
					/*
					 * FavourableComboAct.start(NewProductInfoAct.this,
					 * productCombo, product.getSelSku(), product.getId());
					 */
                    ComboSelAct.start(packageList, product, (Activity) context);
                }
            });
            return convertView;
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_combo;
        }

        class ViewHolder {
            TextView txtComboName,txtPrefPrice;
        }

    }

    private class BannerAdapter extends PagerAdapter {

        // 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的0初始化，我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            container.addView(bannerList.get(position));

            return bannerList.get(position);
        }

        @Override
        public int getCount() {
            return bannerList.size();
        }

        // PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            view.removeView(bannerList.get(position));
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }
}
