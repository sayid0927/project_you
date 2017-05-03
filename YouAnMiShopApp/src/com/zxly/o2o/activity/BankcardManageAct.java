package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.easemob.easeui.widget.SwipeLayout;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.dialog.PaySetDialog;
import com.zxly.o2o.model.UserBankCard;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.PayMyAccountRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;
import com.zxly.o2o.view.SlideDelete;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

/**
 * @author fengrongjian 2016-5-25
 * @description 银行卡管理页面
 */
public class BankcardManageAct extends BasicAct implements View.OnClickListener {
    private Context context;
    private View contentLayout;
    private LoadingView loadingview;
    private ListView listView;
    private PayBankcardAdapter adapter;
    private ArrayList<UserBankCard> bankcardList;
    private Set<SwipeLayout> mShownLayouts = new HashSet<SwipeLayout>();
    private List<SlideDelete> slideDeleteArrayList = new ArrayList<SlideDelete>();
    private boolean showBtn;
    private PaySetDialog setDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_bankcard_manage);
        context = this;
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    public static void start(Activity curAct) {
        Intent intent = new Intent(curAct, BankcardManageAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

    private void initViews() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        ViewUtils.setText(findViewById(R.id.txt_title), "银行卡管理");
        contentLayout = findViewById(R.id.content_layout);
        loadingview = (LoadingView) findViewById(R.id.view_loading);
        listView = (ListView) findViewById(R.id.card_list);
        adapter = new PayBankcardAdapter(context);
        listView.setAdapter(adapter);
    }

    private void loadData() {
        final PayMyAccountRequest payMyAccountRequest = new PayMyAccountRequest();
        payMyAccountRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                loadingview.onLoadingComplete();
                ViewUtils.setVisible(contentLayout);
                bankcardList = payMyAccountRequest.getBankcardList();
                adapter.clear();
                adapter.addItem(bankcardList, true);
                if (bankcardList != null && !bankcardList.isEmpty()) {
                    ViewUtils.setVisible(findViewById(R.id.txt_my_bankcards));
                    UserBankCard userBankCard = bankcardList.get(0);
                    String userName = userBankCard.getUserName();
                    String idCard = userBankCard.getIdCard();
                    if (!StringUtil.isNull(userName) && !StringUtil.isNull(idCard)) {
                        ViewUtils.setText(findViewById(R.id.txt_user_info), "【" + StringUtil.getHideName(userName) + " " + StringUtil.getHiddenString(idCard) + "】");
                    }
                } else {
                    ViewUtils.setGone(contentLayout);
                    ViewUtils.setGone(findViewById(R.id.txt_my_bankcards));
                    loadingview.onDataEmpty("您还没有添加银行卡呢!",true,R.drawable.img_default_tired);
                    loadingview.setBtnText("去添加");
                    showBtn =true;
                }
                if (payMyAccountRequest.getIsUserPaw() != -1) {
                    Constants.isUserPaw = payMyAccountRequest.getIsUserPaw();
                }
            }

            @Override
            public void onFail(int code) {
                loadingview.onLoadingFail();
            }
        });
        loadingview.setOnAgainListener(new LoadingView.OnAgainListener() {

            @Override
            public void onLoading() {
                if(showBtn){
                    if (Constants.isUserPaw != 2) {
                        setUserPaw(true);
                    } else {
                        PayIdentityCheckAct.start(BankcardManageAct.this, Constants.PAY_TYPE_LIANLIAN, 0f, Constants.TYPE_JUST_ADD, null, null);
                    }
                }else {
                    loadingview.startLoading();
                    payMyAccountRequest.start(this);
                }
            }
        });
        loadingview.startLoading();
        payMyAccountRequest.start(this);
    }

    /**
     * @param isNewAdd 是否新增银行卡
     * @description 设置交易密码
     */
    private void setUserPaw(final boolean isNewAdd) {
        setDialog = new PaySetDialog(BankcardManageAct.this, new ParameCallBack() {
            @Override
            public void onCall(Object object) {
                PayIdentityCheckAct.start(BankcardManageAct.this, Constants.PAY_TYPE_LIANLIAN, 0f, Constants.TYPE_JUST_ADD, null, null);
            }
        });
        setDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }

    class PayBankcardAdapter extends ObjectAdapter {

        public PayBankcardAdapter(Context context) {
            super(context);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflateConvertView();
                holder = new ViewHolder();
                holder.viewTopLine = convertView.findViewById(R.id.view_head);
//                holder.swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipe);
//                holder.swipeLayout.addSwipeListener(new SwipeMemory());
//                mShownLayouts.add(holder.swipeLayout);
                holder.viewSlide = (SlideDelete) convertView.findViewById(R.id.view_slide);
                holder.btnDel = (TextView) convertView.findViewById(R.id.btn_del);

                holder.txtBankName = (TextView) convertView
                        .findViewById(R.id.txt_bank_name);
                holder.txtBankcardNumber = (TextView) convertView.findViewById(R.id.txt_bankcard_number);
                holder.txtBankcardType = (TextView) convertView
                        .findViewById(R.id.txt_card_type);
                holder.imgBankLogo = (NetworkImageView) convertView
                        .findViewById(R.id.img_bank_logo);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
//            holder.swipeLayout.setSwipeEnabled(false);
            holder.viewSlide.setDragEnable(true);
            holder.viewSlide.setOnSlideDeleteListener(new SlideDelete.OnSlideDeleteListener() {
                @Override
                public void onOpen(SlideDelete slideDelete) {
                    closeOtherItem();
                    slideDeleteArrayList.add(slideDelete);
                }

                @Override
                public void onClose(SlideDelete slideDelete) {
                    slideDeleteArrayList.remove(slideDelete);
                }
            });
            holder.btnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserBankCard userBankCard = (UserBankCard) getItem(position);
                    PayIdentityCheckAct.start(BankcardManageAct.this, userBankCard.getId());
                }
            });
            UserBankCard userBankCard = (UserBankCard) getItem(position);
//            if (position == 0) {
//                ViewUtils.setVisible(holder.viewTopLine);
//            } else {
//                ViewUtils.setGone(holder.viewTopLine);
//            }
            holder.imgBankLogo.setDefaultImageResId(R.drawable.img_shortcut_pay);
            holder.imgBankLogo.setImageUrl(userBankCard.getImageBanner(),
                    AppController.imageLoader);
            ViewUtils.setText(holder.txtBankName, userBankCard.getBankName());
            ViewUtils.setText(holder.txtBankcardNumber, StringUtil.getHiddenString(userBankCard.getBankNumber()));
            if (2 == userBankCard.getBankType()) {
                ViewUtils.setText(holder.txtBankcardType, "储蓄卡");
            } else if (3 == userBankCard.getBankType()) {
                ViewUtils.setText(holder.txtBankcardType, "信用卡");
            }
            return convertView;
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_pay_bankcard_slide;
        }

        class ViewHolder {
            View viewTopLine;
//            SwipeLayout swipeLayout;
            SlideDelete viewSlide;
            NetworkImageView imgBankLogo;
            TextView txtBankName;
            TextView txtBankcardNumber;
            TextView txtBankcardType;
            TextView btnDel;
        }

        private void closeOtherItem(){
            ListIterator<SlideDelete> slideDeleteListIterator = slideDeleteArrayList.listIterator();
            while(slideDeleteListIterator.hasNext()){
                SlideDelete slideDelete = slideDeleteListIterator.next();
                slideDelete.isShowDelete(false);
            }
            slideDeleteArrayList.clear();
        }

    }

    class SwipeMemory implements SwipeLayout.SwipeListener {

        @Override
        public void onStartOpen(SwipeLayout layout) {
            closeAllExcept(layout);
        }

        @Override
        public void onOpen(SwipeLayout layout) {
            closeAllExcept(layout);
        }

        @Override
        public void onStartClose(SwipeLayout layout) {
        }

        @Override
        public void onClose(SwipeLayout layout) {
        }

        @Override
        public void onUpdate(SwipeLayout layout, int leftOffset,
                             int topOffset) {
        }

        @Override
        public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
        }
    }

    public void closeAllExcept(SwipeLayout layout) {
        for (SwipeLayout s : mShownLayouts) {
            if (s != layout) {
                s.close();
            }
        }
    }
}
