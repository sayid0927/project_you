package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.easeui.widget.SwipeLayout;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.model.ShopDiscount;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.DiscountDeleteRequest;
import com.zxly.o2o.request.DiscountListRequest;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengrongjian 2016-6-2
 * @description 我的优惠
 */
public class DiscountListAct extends BasicAct implements View.OnClickListener, PullToRefreshBase.OnRefreshListener {
    private Context context;
    private PullToRefreshListView listView;
    private DiscountAdapter discountAdapter;
    private LoadingView loadingView;
    private List<ShopDiscount> shopDiscountList = new ArrayList<ShopDiscount>();
    private int pageIndex = 1;
    private DiscountDeleteRequest discountDeleteRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_discount_list);
        context = this;
        findViewById(R.id.btn_back).setOnClickListener(this);
        ((TextView) findViewById(R.id.txt_title)).setText("我的优惠");
        loadingView = (LoadingView) findViewById(R.id.view_loading);

        listView = (PullToRefreshListView) findViewById(R.id.list_view);
        listView.setIntercept(true);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        discountAdapter = new DiscountAdapter(context);
        listView.setAdapter(discountAdapter);
        ViewUtils.setRefreshText(listView);
        listView.setOnRefreshListener(this);
        discountAdapter.addItem(shopDiscountList, true);
        loadData(pageIndex);
    }

    public static void start(Activity curAct) {
        Intent intent = new Intent(curAct, DiscountListAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

    private void loadData(final int pageId) {
        final DiscountListRequest request = new DiscountListRequest(pageId);
        request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                ViewUtils.setVisible(listView);
                shopDiscountList = request.getShopDiscountList();
                if (!DataUtil.listIsNull(shopDiscountList)) {
                    if (pageId == 1) {
                        discountAdapter.clear();
                    }
                    discountAdapter.addItem(shopDiscountList, true);
                    pageIndex++;
                    loadingView.onLoadingComplete();
                } else {
                    if (pageId == 1) {
                        discountAdapter.clear();
                        discountAdapter.notifyDataSetChanged();
                        loadingView.onDataEmpty("没有优惠");
                    } else {
                        ViewUtils.showToast("最后一页了");
                        loadingView.onLoadingComplete();
                    }
                }
                if (listView.isRefreshing()) {
                    listView.onRefreshComplete();
                }
            }

            @Override
            public void onFail(int code) {
                loadingView.onLoadingFail();
                if (listView.isRefreshing()) {
                    listView.onRefreshComplete();
                }
            }
        });
        loadingView.startLoading();
        request.start(this);
    }

    private void discountDel(long id){
        discountDeleteRequest = new DiscountDeleteRequest(id);
        discountDeleteRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                pageIndex = 1;
                loadData(pageIndex);
            }

            @Override
            public void onFail(int code) {

            }
        });
        discountDeleteRequest.start(this);
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
            pageIndex = 1;
            loadData(pageIndex);
        }
        if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
            loadData(pageIndex);
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

    class DiscountAdapter extends ObjectAdapter {

        public DiscountAdapter(Context context) {
            super(context);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflateConvertView();
                holder = new ViewHolder();
                holder.swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipe);
                holder.btnDel = (TextView) convertView.findViewById(R.id.btn_del);
                holder.imgDiscount = (ImageView) convertView.findViewById(R.id.img_discount);
                holder.imgStatus = (ImageView) convertView.findViewById(R.id.img_status);
                holder.txtTitle = (TextView) convertView
                        .findViewById(R.id.txt_discount_title);
                holder.txtContent = (TextView) convertView
                        .findViewById(R.id.txt_discount_content);
                holder.txtPeriod = (TextView) convertView
                        .findViewById(R.id.txt_discount_period);
                holder.txtType = (TextView) convertView
                        .findViewById(R.id.txt_discount_type);
                holder.txtUseNow = (TextView) convertView
                        .findViewById(R.id.txt_use_now);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final ShopDiscount shopDiscount = (ShopDiscount) getItem(position);
            final long id = shopDiscount.getId();
            final int discountType = shopDiscount.getDiscountType();
            final String discountTitle = shopDiscount.getTitle();
            final int status = shopDiscount.getStatus();

            holder.btnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    discountDel(id);
                }
            });

            ViewUtils.setText(holder.txtTitle, discountTitle);
            ViewUtils.setText(holder.txtContent, shopDiscount.getDiscountInfo());
            holder.txtPeriod.setText("有效期：" + StringUtil.getDateByMillis(shopDiscount.getStartTime()) + "~" + StringUtil.getDateByMillis(shopDiscount.getEndTime()));
            if (1 == discountType) {
                ViewUtils.setText(holder.txtType, "现金抵扣");
            } else if (2 == discountType) {
                ViewUtils.setText(holder.txtType, "礼品赠送");
            }
            if (status == 1) {
                ViewUtils.setGone(holder.imgStatus);
                if (1 == discountType) {
                    holder.imgDiscount.setBackgroundResource(R.drawable.bg_discount_cash);
                    holder.txtType.setTextColor(getResources().getColor(R.color.green_06766e));
                    holder.txtUseNow.setTextColor(getResources().getColor(R.color.green_44baae));
                } else if (2 == discountType) {
                    holder.imgDiscount.setBackgroundResource(R.drawable.bg_discount_gift);
                    holder.txtType.setTextColor(getResources().getColor(R.color.purple_9a1355));
                    holder.txtUseNow.setTextColor(getResources().getColor(R.color.purple_dd4991));
                }
            } else {
                ViewUtils.setVisible(holder.imgStatus);
                holder.imgDiscount.setBackgroundResource(R.drawable.bg_discount_disable);
                holder.txtType.setTextColor(getResources().getColor(R.color.gray_b1b1b1));
                holder.txtUseNow.setTextColor(getResources().getColor(R.color.gray_dedede));
                if (status == 2) {
                    holder.imgStatus.setBackgroundResource(R.drawable.icon_discount_used);
                } else if (status == 3) {
                    holder.imgStatus.setBackgroundResource(R.drawable.icon_discount_expired);
                } else if (status == 4) {
                    holder.imgStatus.setBackgroundResource(R.drawable.icon_discount_disable);
                }
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (status == 1) {
                        DiscountDetailAct.start(DiscountListAct.this, id, discountTitle, discountType);
                    }
                }
            });
            return convertView;
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_discount_swipt;
        }

        class ViewHolder {
            SwipeLayout swipeLayout;
            TextView btnDel;
            ImageView imgDiscount, imgStatus;
            TextView txtTitle, txtContent, txtPeriod, txtType, txtUseNow;
        }

    }

}
