package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.adapter.ShopDescAddressAdapter;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.PersonalShopInfo;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.OrderBranchsRequest;
import com.zxly.o2o.request.PersonalShopInfoRequest;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.MGridView;
import com.zxly.o2o.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author fengrongjian 2015-12-14
 * @description 关于门店
 */
public class PersonalShopDescAct extends BasicAct implements
        View.OnClickListener,PullToRefreshBase.OnRefreshListener{
    private Context context;
    private ShopLabelAdapter shopLabelAdapter;
    private ShopAdapter shopImageAdapter;
    private ViewPager pager;
    private CirclePageIndicator indicator;
    private View viewShop;
    private ViewGroup header;
    private int pageIndex=1;
    private final int AUTO_PAGER = 1;
    private PullToRefreshListView listViewAddress;
    private ShopDescAddressAdapter shopDescAddressAdapter;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case AUTO_PAGER:
                    int totalCount = pager.getChildCount();
                    int currentItem = pager.getCurrentItem();

                    int toItem = currentItem + 1 == totalCount ? 0 : currentItem + 1;

                    pager.setCurrentItem(toItem, true);

                    //每两秒钟发送一个message，用于切换viewPager中的图片
                    this.sendEmptyMessageDelayed(AUTO_PAGER, 2000);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_personal_shop_desc);
        context = this;
        initViews();
        loadData();
        loadShopAddress(pageIndex);
    }

    public static void start(Activity curAct) {
        Intent intent = new Intent(curAct, PersonalShopDescAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

    private void initViews() {
        findViewById(R.id.btn_back).setOnClickListener(this);

        header= (ViewGroup) LayoutInflater.from(this).inflate(R.layout.header_shop_info,null);
        if (Account.shopInfo != null) {
            ViewUtils.setText(header.findViewById(R.id.txt_shop_shop_name), Account.shopInfo.getName());
        }
        viewShop = header.findViewById(R.id.view_shop);
        pager = (ViewPager) header.findViewById(R.id.pager);
        ViewGroup.LayoutParams ps =  pager.getLayoutParams();
        ps.height = AppController.displayMetrics.widthPixels;
        pager.setLayoutParams(ps);
        listViewAddress= (PullToRefreshListView) findViewById(R.id.listview);
        listViewAddress.addH(header);
        listViewAddress.setDivideHeight(0);
        listViewAddress.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        listViewAddress.setIntercept(true);
        listViewAddress.setOnRefreshListener(this);
        ViewUtils.setRefreshText(listViewAddress);
        indicator = (CirclePageIndicator) header.findViewById(R.id.indicator);
        MGridView gridView = (MGridView) header.findViewById(R.id.grid_view);
        shopLabelAdapter = new ShopLabelAdapter(context);
        if(Account.shopInfo!=null)
        {
            if(Account.shopInfo.getType()==1)
            {
                ViewUtils.setVisible(header,R.id.view_shop_address);
            }else
            {
                ViewUtils.setVisible(findViewById(R.id.view_address));
                ViewUtils.setText(findViewById(R.id.txt_shop_address),Account.shopInfo.getAddress());
            }
        }

        shopDescAddressAdapter=new ShopDescAddressAdapter(context);
        listViewAddress.setAdapter(shopDescAddressAdapter);
        gridView.setAdapter(shopLabelAdapter);
    }

    private void loadData() {
        final PersonalShopInfoRequest personalShopInfoRequest = new PersonalShopInfoRequest(Config.shopId);
        personalShopInfoRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                PersonalShopInfo personalShopInfo = personalShopInfoRequest.getPersonalShopInfo();
                if (!StringUtil.isNull(personalShopInfo.getLableNames())) {
                    String[] labelNames = personalShopInfo.getLableNames().split(",");
                    List<String> labelList = new ArrayList<String>();
                    Collections.addAll(labelList, labelNames);
                    shopLabelAdapter.addItem(labelList, true);
                }
                if (!StringUtil.isNull(personalShopInfo.getImageUrls())) {
                    ViewUtils.setVisible(viewShop);
                    String[] imageUrls = personalShopInfo.getImageUrls().split(",");
                    List<String> imageList = new ArrayList<String>();
                    Collections.addAll(imageList, imageUrls);
                    shopImageAdapter = new ShopAdapter(context, imageList, pager);
                    pager.setAdapter(shopImageAdapter);
                    indicator.setViewPager(pager, imageList.size(), imageList.size() * 100);
                }
                ViewUtils.setText(header.findViewById(R.id.txt_shop_phone), personalShopInfo.getTelephone());

            }

            @Override
            public void onFail(int code) {
            }
        });
        personalShopInfoRequest.start(this);
    }
    private void loadShopAddress(final int _pageIndex)
    {
        if(Account.shopInfo!=null&&Account.shopInfo.getType()!=1)//连锁店
        {
            return ;
        }
        final OrderBranchsRequest request=new OrderBranchsRequest(_pageIndex);
        request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                listViewAddress.onRefreshComplete();
                if(request.addressList.size()>0)
                {
                    if(pageIndex==1)
                    {
                        shopDescAddressAdapter.clear();
                    }
                    shopDescAddressAdapter.addItem(request.addressList,true);
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

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.sendEmptyMessageDelayed(AUTO_PAGER, 2000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeMessages(AUTO_PAGER);
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

    class ShopAdapter extends PagerAdapter implements View.OnClickListener {
        private Context context;
        private List<String> shopImgs;
        private ViewPager mViewPager;

        public ShopAdapter(Context context, List<String> shopImgs, ViewPager mViewPager) {
            this.context = context;
            this.mViewPager = mViewPager;
            this.shopImgs = shopImgs;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public int getCount() {
            if (shopImgs.size() == 1) {
                return 1;
            }
            return shopImgs.size() * 1000;
        }

        @Override
        public Object instantiateItem(View arg0, int position) {
            if (shopImgs.size() != 1) {
                position = position % shopImgs.size();
            }

            NetworkImageView iv = new NetworkImageView(context);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setLayoutParams(pager.getLayoutParams());
            iv.setDefaultImageResId(R.drawable.icon_default);
            iv.setImageUrl(shopImgs.get(position), AppController.imageLoader);
            ((ViewPager) arg0).addView(iv, 0);
            return iv;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }

        @Override
        public void onClick(View v) {
        }

    }

    class ShopLabelAdapter extends ObjectAdapter {

        public ShopLabelAdapter(Context context) {
            super(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflateConvertView();
                holder = new ViewHolder();
                holder.txtShopLabel = (TextView) convertView
                        .findViewById(R.id.txt_shop_label);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String label = (String) getItem(position);
            ViewUtils.setText(holder.txtShopLabel, label);
            return convertView;
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_shop_label;
        }

        class ViewHolder {
            TextView txtShopLabel;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }

}
