package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.adapter.PayBankcardAdapter;
import com.zxly.o2o.application.Config;
import com.zxly.o2o.dialog.PaySetDialog;
import com.zxly.o2o.model.UserBankCard;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.PayGetPayNoRequest;
import com.zxly.o2o.request.PayInitRequest;
import com.zxly.o2o.request.PayMyAccountRequest;
import com.zxly.o2o.request.PayParamRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.shop.wxapi.WXSecurePayer;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.PayResult;
import com.zxly.o2o.util.PayUtil;
import com.zxly.o2o.util.UmengUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

import java.util.ArrayList;

/**
 * @author fengrongjian 2015-12-22
 * @description 选择支付方式
 */
public class PayAct extends BasicAct implements View.OnClickListener {
    private Context context;
    private View contentLayout;
    private LoadingView loadingview;
    private TextView txtOrderAmount, txtUserBalance;
    private View btnUserBalance;
    private ImageView imgZhye, imgUserBalanceCheck, imgAlipayCheck, imgWxpayCheck;
    private TextView txtYezh, txtZhye;
    private Float orderMoney = null;//订单金额
    private Float userBalance = null;//用户余额
    private String orderNo = null;//订单号
    private String payType;//0:都未选中 1：连连，2：微信，3：余额，4：支付宝
    private String payNo;//流水号
    private ArrayList<UserBankCard> bankcardList = null;//快捷支付银行卡列表
    private PayInitRequest payInitRequest;
    private PayParamRequest payParamRequest;
    private PayGetPayNoRequest payGetPayNoRequest;
    private PayMyAccountRequest payMyAccountRequest;
    public static ParameCallBack parameCallBack = null;
    public static final int ALIPAY_RESULT = 1;//支付宝回调
    public static final int WXPAY_RESULT = 2;//微信回调
    private String productSubject;
    private long lastClickTime;
    private ListView listView;
    private PayBankcardAdapter adapter;
    private UserBankCard selectedBankCard = null;
    private int type;//0：流量充值，1:商品购买
    private String businessType;
    private PaySetDialog setDialog;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ALIPAY_RESULT:
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        ViewUtils.showToast("支付成功");
                        if (Constants.TYPE_PRODUCT_PAY == type || Constants.TYPE_INSURANCE_PAY == type) {
                            PaySuccessAct.start(PayAct.this, orderMoney, orderNo, type, "pay");
                        } else if (Constants.TYPE_FLOW_PAY == type) {
                            PayFlowResultAct.start(PayAct.this, payNo, Constants.PAY_TYPE_ALI, "success");
                        }
                        finish();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            ViewUtils.showToast("支付结果确认中");
                        } else if ("6001".equals(resultStatus)) {
                            //支付取消，用户手动取消
                            ViewUtils.showToast("支付取消");
                        } else if ("6002".equals(resultStatus)) {
                            ViewUtils.showToast("网络连接出错");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            ViewUtils.showToast("支付失败");
                            if (Constants.TYPE_PRODUCT_PAY == type || Constants.TYPE_INSURANCE_PAY == type) {
                                PayFailAct.start(PayAct.this, orderNo, type);
                            } else if (Constants.TYPE_FLOW_PAY == type) {
                                PayFlowResultAct.start(PayAct.this, null, Constants.PAY_TYPE_ALI, "fail");
                            }
                            finish();
                        }
                    }
                    break;
                case WXPAY_RESULT:
                    int pay_result = (Integer) msg.obj;
                    switch (pay_result) {
                        case 0://支付成功
                            ViewUtils.showToast("支付成功");
                            if (Constants.TYPE_PRODUCT_PAY == type || Constants.TYPE_INSURANCE_PAY == type) {
                                PaySuccessAct.start(PayAct.this, orderMoney, orderNo, type, "pay");
                            } else if (Constants.TYPE_FLOW_PAY == type) {
                                PayFlowResultAct.start(PayAct.this, payNo, Constants.PAY_TYPE_WEIXIN, "success");
                            }
                            finish();
                            break;
                        case -1://支付失败未知错误
                            ViewUtils.showToast("支付发生错误，请检查微信是否登陆过期");
                            if (Constants.TYPE_PRODUCT_PAY == type || Constants.TYPE_INSURANCE_PAY == type) {
                                PayFailAct.start(PayAct.this, orderNo, type);
                            } else if (Constants.TYPE_FLOW_PAY == type) {
                                PayFlowResultAct.start(PayAct.this, null, Constants.PAY_TYPE_WEIXIN, "fail");
                            }
                            finish();
                            break;
                        case -2://支付取消
                            ViewUtils.showToast("支付取消");
                            break;
                        case -3://没有安装微信
                            ViewUtils.showToast("没有检测到微信，请安装！");
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_pay_main);
        context = this;
        payType = Constants.PAY_TYPE_UNKNOWN;
        this.orderNo = getIntent().getStringExtra("orderNo");
        this.type = getIntent().getIntExtra("type", 1);//取不到默认是1，表示商品订单购买，后续要传
        if (Constants.TYPE_PRODUCT_PAY == type) {
            businessType = Constants.BUSINESS_TYPE_PRODUCT;
        } else if (Constants.TYPE_FLOW_PAY == type) {
            businessType = Constants.BUSINESS_TYPE_FLOW;
        } else if (Constants.TYPE_INSURANCE_PAY == type) {
            businessType = Constants.BUSINESS_TYPE_INSURANCE;
        }
        initViews();
        loadData();
    }

    /**
     * @param curAct
     * @param orderNo 订单号
     * @param type    0：流量充值，1:商品购买
     */
    public static void start(Activity curAct, String orderNo, int type) {
        if (orderNo != null) {
            Intent intent = new Intent(curAct, PayAct.class);
            intent.putExtra("orderNo", orderNo);
            intent.putExtra("type", type);
            ViewUtils.startActivity(intent, curAct);
        } else {
            ViewUtils.showToast("没有订单号!");
        }
    }

    /**
     * @param curAct
     * @param orderNo         订单号
     * @param _parameCallBack 延保列表页面回调刷新
     */
    public static void start(Activity curAct, String orderNo, int type, ParameCallBack _parameCallBack) {
        if (orderNo != null) {
            parameCallBack = _parameCallBack;
            Intent intent = new Intent(curAct, PayAct.class);
            intent.putExtra("orderNo", orderNo);
            intent.putExtra("type", type);
            ViewUtils.startActivity(intent, curAct);
        } else {
            ViewUtils.showToast("没有订单号!");
        }
    }

    private void initViews() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        ViewUtils.setText(findViewById(R.id.txt_title), "支付方式");
        contentLayout = findViewById(R.id.content_layout);
        loadingview = (LoadingView) findViewById(R.id.view_loading);
        txtOrderAmount = (TextView) findViewById(R.id.txt_order_amount);
        findViewById(R.id.btn_pay).setOnClickListener(this);
        listView = (ListView) findViewById(R.id.list_bankcard);
        LayoutInflater layoutInflater =
                (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View mHeaderView = layoutInflater.inflate(R.layout.pay_main_list_header, null);
        View mFooterView = layoutInflater.inflate(R.layout.pay_main_list_footer, null);
        btnUserBalance = mHeaderView.findViewById(R.id.btn_select_user_balance);
        imgUserBalanceCheck = (ImageView) mHeaderView.findViewById(R.id.img_user_balance_check);
        imgZhye = (ImageView) mHeaderView.findViewById(R.id.img_zhye);
        txtYezh = (TextView) mHeaderView.findViewById(R.id.txt_yezf);
        txtZhye = (TextView) mHeaderView.findViewById(R.id.txt_zhye);
        txtUserBalance = (TextView) mHeaderView.findViewById(R.id.txt_user_balance);
        mFooterView.findViewById(R.id.btn_add_new).setOnClickListener(this);
        imgAlipayCheck = (ImageView) mFooterView.findViewById(R.id.img_alipay_check);
        imgWxpayCheck = (ImageView) mFooterView.findViewById(R.id.img_wxpay_check);
        btnUserBalance.setOnClickListener(this);
        mFooterView.findViewById(R.id.btn_alipay).setOnClickListener(this);
        mFooterView.findViewById(R.id.btn_wxpay).setOnClickListener(this);
        adapter = new PayBankcardAdapter(context);
        adapter.setSelectedShow(true);
        listView.addHeaderView(mHeaderView, null, false);
        listView.addFooterView(mFooterView, null, false);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        WXSecurePayer.closePay();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_pay:
                if ((System.currentTimeMillis() - lastClickTime) > 2000) {
                    lastClickTime = System.currentTimeMillis();
                    if (Constants.PAY_TYPE_UNKNOWN.equals(payType)) {
                        ViewUtils.showToast("请选择支付方式");
                        return;
                    } else {
                        if (Constants.isUserPaw != 2) {
                            setUserPaw(false);
                        } else {
                            getPayNo(0);//已设置交易密码再支付时传0，表示直接支付
                        }
                    }
                }
                break;
            case R.id.btn_select_user_balance:
                if (!payType.equals(Constants.PAY_TYPE_USER_BALANCE)) {
                    setPayTypeStatus(true, false, false, true);
                    payType = Constants.PAY_TYPE_USER_BALANCE;
                }
                break;
            case R.id.btn_add_new:
                if (Constants.isUserPaw != 2) {
                    setUserPaw(true);
                } else {
                    setPayTypeStatus(false, false, false, true);
                    payType = Constants.PAY_TYPE_LIANLIAN;
                    getPayNo(2);//已设置交易密码再新增时需要验证
                }

                UmengUtil.onEvent(PayAct.this,new UmengUtil().WITHDRAW_ADDCARD_CLICK,null);

                break;
            case R.id.btn_alipay:
                if (!payType.equals(Constants.PAY_TYPE_ALI)) {
                    setPayTypeStatus(false, true, false, true);
                    payType = Constants.PAY_TYPE_ALI;
                }
                break;
            case R.id.btn_wxpay:
                if (!payType.equals(Constants.PAY_TYPE_WEIXIN)) {
                    setPayTypeStatus(false, false, true, true);
                    payType = Constants.PAY_TYPE_WEIXIN;
                }
                break;
        }
    }

    /**
     * @description 更改支付方式选中与否按钮状态
     */
    private void setPayTypeStatus(boolean isUserBalance, boolean isAlipay, boolean isWxpay, boolean isCancelBankcardSelect) {
        imgUserBalanceCheck.setBackgroundResource(isUserBalance ? R.drawable.checkbox_press : R.drawable.checkbox_normal);
        imgAlipayCheck.setBackgroundResource(isAlipay ? R.drawable.checkbox_press : R.drawable.checkbox_normal);
        imgWxpayCheck.setBackgroundResource(isWxpay ? R.drawable.checkbox_press : R.drawable.checkbox_normal);
        if (isCancelBankcardSelect) {
            selectedBankCard = null;
            adapter.setSelected(-1);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * @description 根据订单号获取订单信息
     */
    private void loadData() {
        payInitRequest = new PayInitRequest(orderNo, businessType);
        payInitRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                loadingview.onLoadingComplete();
                if (payInitRequest.getOrderPrice() != null) {
                    orderMoney = payInitRequest.getOrderPrice();
                    ViewUtils.setTextPrice(txtOrderAmount, orderMoney);
                }
                if (payInitRequest.getUserBalance() != null) {
                    userBalance = payInitRequest.getUserBalance();
                    ViewUtils.setTextPrice(txtUserBalance, userBalance);
                    if (0 == userBalance || userBalance < orderMoney) {
                        btnUserBalance.setClickable(false);
                        btnUserBalance.setBackgroundColor(getResources().getColor(R.color.gray_f9f9f9));
                        imgZhye.setBackgroundResource(R.drawable.icon_zhye_disable);
                        txtZhye.setTextColor(getResources().getColor(R.color.grey_aaaaaa));
                        txtYezh.setTextColor(getResources().getColor(R.color.grey_aaaaaa));
                        txtUserBalance.setTextColor(getResources().getColor(R.color.grey_aaaaaa));

                        setPayTypeStatus(false, true, false, true);
                        payType = Constants.PAY_TYPE_ALI;
                    } else {
                        setPayTypeStatus(true, false, false, true);
                        payType = Constants.PAY_TYPE_USER_BALANCE;
                    }
                }
                if (payInitRequest.getIsUserPaw() != -1) {
                    Constants.isUserPaw = payInitRequest.getIsUserPaw();
                }

                if (Config.payParams != null) {
                    ViewUtils.setVisible(contentLayout);
                } else {
                    getPayParams();
                }

                loadBankcardList();
            }

            @Override
            public void onFail(int code) {
                loadingview.onLoadingFail();
            }
        });
        loadingview.setOnAgainListener(new LoadingView.OnAgainListener() {

            @Override
            public void onLoading() {
                loadingview.startLoading();
                payInitRequest.start(this);
            }
        });
        loadingview.startLoading();
        payInitRequest.start(this);
    }

    /**
     * @description 获取银行卡列表
     */
    private void loadBankcardList() {
        payMyAccountRequest = new PayMyAccountRequest();
        payMyAccountRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                bankcardList = payMyAccountRequest.getBankcardList();
                if (bankcardList != null && !bankcardList.isEmpty()) {
                    adapter.clear();
                    adapter.addItem(bankcardList, true);
                    selectedBankCard = (UserBankCard) adapter.getItem(0);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                                long arg3) {
                            selectedBankCard = (UserBankCard) adapter.getItem(position - 1);
                            adapter.setSelected(position - 1);
                            adapter.notifyDataSetChanged();
                            setPayTypeStatus(false, false, false, false);
                            payType = Constants.PAY_TYPE_LIANLIAN;
                        }
                    });

                    if (payType.equals(Constants.PAY_TYPE_ALI)) {
                        selectedBankCard = (UserBankCard) adapter.getItem(0);
                        adapter.setSelected(0);
                        adapter.notifyDataSetChanged();
                        setPayTypeStatus(false, false, false, false);
                        payType = Constants.PAY_TYPE_LIANLIAN;
                    }
                }
            }

            @Override
            public void onFail(int code) {
            }
        });
        payMyAccountRequest.start(this);
    }

    /**
     * @param isNewAdd 是否新增银行卡
     * @description 设置交易密码
     */
    private void setUserPaw(final boolean isNewAdd) {
        setDialog = new PaySetDialog(PayAct.this, new ParameCallBack() {
            @Override
            public void onCall(Object object) {
                if (isNewAdd) {
                    setPayTypeStatus(false, false, false, true);
                    payType = Constants.PAY_TYPE_LIANLIAN;
                }
                getPayNo(1);//设置交易密码时下一步不再需要验证
            }
        });
        setDialog.show();
    }

    /**
     * @description 获取流水号
     * @param followType 0：直接支付 1：不需验证 2：需验证
     */
    private void getPayNo(final int followType) {
        payGetPayNoRequest = new PayGetPayNoRequest(orderNo, payType, businessType, Constants.CHANNEL_SHOP_APP, Constants.PAY_DEVICE_TYPE, Account.user.getShopId());
        payGetPayNoRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                if (payGetPayNoRequest.getPayNo() != null) {
                    payNo = payGetPayNoRequest.getPayNo();
                    if (Constants.PAY_TYPE_USER_BALANCE.equals(payType)) {
                        //余额支付
                        PayUtil.confirmPay(PayAct.this, type, payNo, payType, orderMoney, callBack);
                    } else if (Constants.PAY_TYPE_LIANLIAN.equals(payType)) {
                        //快捷支付方式
                        if (followType == 0) {
                            PayUtil.confirmPay(PayAct.this, type, payNo, payType, orderMoney, selectedBankCard, callBack, null, null, "pay");
                        } else if (followType == 1) {
                            PayAddBankcardAct.start(PayAct.this, payType, orderMoney, type, payNo, null, callBack);
                            payType = Constants.PAY_TYPE_UNKNOWN;
                        } else if (followType == 2) {
                            PayIdentityCheckAct.start(PayAct.this, payType,
                                    orderMoney, type, payNo, callBack);
                            payType = Constants.PAY_TYPE_UNKNOWN;
                        }
                    } else if (Constants.PAY_TYPE_ALI.equals(payType)) {
                        //支付宝支付
                        productSubject = Account.user.getShopName() + "-订单号" + orderNo;
                        PayUtil.doAlipay(productSubject, productSubject, orderMoney + "", payNo, mHandler, PayAct.this);
                    } else if (Constants.PAY_TYPE_WEIXIN.equals(payType)) {
                        //微信支付
                        PayUtil.wxpay(PayAct.this, orderNo, payNo, orderMoney, Config.payParams.getWxNotifyUrl(), mHandler);
                    }
                }
            }

            @Override
            public void onFail(int code) {
            }
        });
        payGetPayNoRequest.start(this);
    }

    /**
     * @description 获取支付宝, 微信支付商户参数
     */
    private void getPayParams() {
        payParamRequest = new PayParamRequest(businessType, Constants.CHANNEL_SHOP_APP, Constants.PAY_DEVICE_TYPE);
        payParamRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                loadingview.onLoadingComplete();
                ViewUtils.setVisible(contentLayout);
            }

            @Override
            public void onFail(int code) {
                loadingview.onLoadingFail();
            }
        });
        loadingview.setOnAgainListener(new LoadingView.OnAgainListener() {

            @Override
            public void onLoading() {
                loadingview.startLoading();
                payParamRequest.start(this);
            }
        });
        loadingview.startLoading();
        payParamRequest.start(this);
    }


    /**
     * @description 支付成功后回调关闭页面
     */
    private CallBack callBack = new CallBack() {
        @Override
        public void onCall() {
            parameCallBack = null;
            finish();
        }
    };

    @Override
    public void finish() {
        super.finish();
        if (payInitRequest != null) {
            payInitRequest.cancel();
        }
        if (payMyAccountRequest != null) {
            payMyAccountRequest.cancel();
        }
        if (payParamRequest != null) {
            payParamRequest.cancel();
        }
        if (payGetPayNoRequest != null) {
            payGetPayNoRequest.cancel();
        }
    }

}