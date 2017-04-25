package com.zxly.o2o.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.zxly.o2o.adapter.CommProductAdapter;
import com.zxly.o2o.adapter.GridImageAdapter;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.dialog.TelVerifyDialog;
import com.zxly.o2o.dialog.VerifyDialog;
import com.zxly.o2o.model.RefundmentDetail;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.BaseRequest.ResponseStateListener;
import com.zxly.o2o.request.RefundmentCancelRequest;
import com.zxly.o2o.request.RefundmentDeleteRequest;
import com.zxly.o2o.request.RefundmentDetailRequest;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.TimeUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.MListView;
import com.zxly.o2o.view.MyFlipperView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/5/20.
 */
public class RefundmentDetailActivity extends BasicAct
        implements View.OnClickListener, AdapterView.OnItemClickListener {
    private String[] imageUrls;
    private ArrayList<String> rejectPicList = new ArrayList<String>();
    private int isCancelApply;
    private long refundId;
    private String orderNo;
    private RefundmentDetailRequest detailRequest;
    protected MyFlipperView viewContainer;
    private static ParameCallBack _parameCallBack;

    private void setFlipper() {
        if (viewContainer == null) {
            viewContainer = (MyFlipperView) findViewById(R.id.list_layout);
            viewContainer.getRetryBtn().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    viewContainer.setDisplayedChild(0, true);
                    loadData();
                }
            });
        }
    }

    public static void start(Activity curAct, long refundId,
                             String orderNo, ParameCallBack parameCallBack) {
        _parameCallBack = parameCallBack;
        Intent intent = new Intent(curAct, RefundmentDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong("refundId", refundId);
        bundle.putString("orderNo", orderNo);
        intent.putExtras(bundle);
        ViewUtils.startActivity(intent, curAct);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refundment_detail_layout);
        refundId = getIntent().getLongExtra("refundId", -1);
        orderNo = getIntent().getStringExtra("orderNo");
//        refundId = mRefundmentDetail.getId();
        setUpActionBar(getActionBar());
        setFlipper();
        mMainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        }, 1000);
    }

    private void setViewClickListener() {
        findViewById(R.id.check_order).setOnClickListener(this);
        findViewById(R.id.apply_modify).setOnClickListener(this);
        findViewById(R.id.apply_cancel).setOnClickListener(this);
    }

    private void initView(RefundmentDetail mRefundmentDetail) {
        setRefundProduct(mRefundmentDetail);  //设置退款商品信息

        setViewClickListener();

        if (mRefundmentDetail.getStatus() == 6) {
            findViewById(R.id.apply_modify).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.apply_cancel)).setText("删除退款信息");
            isCancelApply = 1;
        }

        ((TextView) findViewById(R.id.refundment_detail_top_text)).setText(Html.fromHtml(
                String.format(getResources().getString(R.string.refundment_detail_top_text),
                        mRefundmentDetail.getRefundType() == 1 ? "仅退款" : "退货退款",
                        mRefundmentDetail.getRefundPrice() + "元",
                        mRefundmentDetail.getRefundReasonName(),
                        mRefundmentDetail.getRefundRemark())));

    }

    private void setRefundProduct(RefundmentDetail mRefundmentDetail) {
        ListView refundProductLV = (MListView) findViewById(R.id.listview);

        if (mRefundmentDetail.getBuyItem().getType() == Constants.PRODUCT_TYPE_PACKAGE) {
            findViewById(R.id.taocan_icon).setVisibility(View.VISIBLE);
        }
        CommProductAdapter commPrdoAdapter = new CommProductAdapter(getApplicationContext(), true);
        commPrdoAdapter.setIsShowProductInfo(true);
        refundProductLV.setAdapter(commPrdoAdapter);
        commPrdoAdapter.clear();
        commPrdoAdapter.addItem(mRefundmentDetail.getBuyItem().getProducts(), true);
        ViewUtils.setTextPrice(((TextView) findViewById(R.id.real_price)), mRefundmentDetail.getRealprice());
    }

    private void loadData() {
        detailRequest = new RefundmentDetailRequest(refundId);
        detailRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                if (detailRequest.refundmentDetail == null) {
                    viewContainer.setDisplayedChild(2, true);
                    return;
                }
                viewContainer.setDisplayedChild(3, true);
                initView(detailRequest.refundmentDetail);
                initGV(detailRequest.refundmentDetail.getImageUrls());

                int operateTimeTextId = 0;
                switch (detailRequest.refundmentDetail.getStatus()) {
                    case Constants.REFUND_ORDER_APPLY:
                        findViewById(R.id.refund_action_btn_layout).setVisibility(View.GONE);
                        operateTimeTextId = R.string.refundment_detail_bottom_text5;
                        findViewById(R.id.status1).setBackgroundResource(R.drawable.tuikuan1_press);
                    break;
                    case Constants.REFUND_ORDER_CONFIRMING:
                        findViewById(R.id.refund_action_btn_layout).setVisibility(View.GONE);
                        operateTimeTextId = R.string.refundment_detail_bottom_text3;
                        findViewById(R.id.status1).setBackgroundResource(R.drawable.tuikuan1);
                        findViewById(R.id.status2).setBackgroundResource(R.drawable.tuikuan2_press);
                        break;
                    case Constants.REFUND_ORDER_CONFIRMED:
                        findViewById(R.id.refund_action_btn_layout).setVisibility(View.GONE);
                        operateTimeTextId = R.string.refundment_detail_bottom_text;
                        findViewById(R.id.status1).setBackgroundResource(R.drawable.tuikuan1);
                        findViewById(R.id.status3).setBackgroundResource(R.drawable.tuikuan3_press);
                        break;
                    case Constants.REFUND_ORDER_REFUND_DONE:
                        operateTimeTextId = R.string.refundment_detail_bottom_text4;
                        findViewById(R.id.refund_action_btn_layout).setVisibility(View.GONE);
                        findViewById(R.id.status1).setBackgroundResource(R.drawable.tuikuan1);
                        findViewById(R.id.status4).setBackgroundResource(R.drawable.tuikuan4_press);
                        break;
                    case Constants.REFUND_ORDER_REJECT:
                        operateTimeTextId = R.string.refundment_detail_bottom_text2;
                        doRejectRefund();
                        break;
                    case Constants.REFUND_ORDER_CANCEL:
                        doCancelApply(findViewById(R.id.apply_cancel));
                        break;
                }

                if (operateTimeTextId != 0)
                    ((TextView) findViewById(R.id.refundment_detial_bottom_text))
                            .setText(Html.fromHtml(
                                    String.format(getResources().getString(operateTimeTextId),
                                            detailRequest.refundmentDetail.getRefundNO(),
                                            TimeUtil.formatOrderTime(detailRequest.refundmentDetail.getRefundDate()),
                                            TimeUtil.formatOrderTime(detailRequest.refundmentDetail.getOperateTime()))));

//                mRefundmentDetail.setRefundRemark(detailRequest.refundmentDetail.getRefundRemark());

//                rejectPicList =detailRequest.refundmentDetail.getHeadImage()
            }

            @Override
            public void onFail(int code) {
                viewContainer.setDisplayedChild(1, true);
            }
        });
        detailRequest.start(this);
    }

    private void doRejectRefund() {
        findViewById(R.id.refundment_reject_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.reject_decs).setVisibility(View.VISIBLE);
        findViewById(R.id.refund_status_layout).setVisibility(View.GONE);

        ((TextView) findViewById(R.id.reject_reason_text)).setText(Html.fromHtml(
                String.format(getResources().getString(R.string.refundment_reject_string),
                        detailRequest.refundmentDetail.getShopRefund())));

        TextView textView = ((TextView) findViewById(R.id.topbar_text));
        textView.setHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35,
                AppController.displayMetrics));
        textView.setText("退款已驳回");
        findViewById(R.id.refund_action_btn_layout).setVisibility(View.GONE);
        findViewById(R.id.btn_call).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TelVerifyDialog().show(new CallBack() {
                    @Override
                    public void onCall() {
                        Intent intent = new Intent(Intent.ACTION_DIAL,
                                Uri.parse(new StringBuffer("tel:").append("4008077067").toString()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ViewUtils.startActivity(intent, RefundmentDetailActivity.this);
                    }
                }, "4008077067");

            }
        });
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bohui, 0, 0, 0);
    }

    private void initGV(String images) {
        if (!images.equals("")) {
            //        rejectGV = (GridView) findViewById(R.id.refundment_reject_gridview);  //驳回图片GV
            GridView detailGV = (GridView) findViewById(R.id.refundment_detail_gridview);
            detailGV.setVisibility(View.VISIBLE);//退款详情图片GV

            GridImageAdapter gridImageAdapter = new GridImageAdapter(this, rejectPicList,true);
            detailGV.setAdapter(gridImageAdapter);
            imageUrls = images.split(",");
            Collections.addAll(rejectPicList, imageUrls);
            gridImageAdapter.setItems(rejectPicList, true);
            //        rejectGV.setAdapter(gridImageAdapter);

            detailGV.setOnItemClickListener(this);
        }
    }

    private void setUpActionBar(final ActionBar actionBar) {
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(R.layout.tag_title);
            ((TextView) actionBar.getCustomView().findViewById(R.id.tag_title_title_name))
                    .setText("退款详情");
            findViewById(R.id.tag_title_btn_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.apply_modify:
                Intent intent = new Intent();
                intent.setClass(this, RefundmentApplyActivity.class);
                intent.putExtra("refundId", refundId);
                intent.putExtra("price", detailRequest.refundmentDetail.getRealprice());
                intent.putExtra("refundType", detailRequest.refundmentDetail.getRefundType());
                ViewUtils.startActivity(intent, this);
                break;
            case R.id.apply_cancel:
                if (isCancelApply == 1) {
                    showVerifyDialog(v, "您确定要删除该退款信息吗?");
                } else {
                    showVerifyDialog(v, "您确定要取消退款吗?");
                }
                break;
            case R.id.check_order:  //跳到订单详情界面
                MyOrderInfoAct.start(orderNo, this, null);
                break;
        }
    }

    private void showVerifyDialog(final View v, String msg) {
        VerifyDialog verDialog = new VerifyDialog();
        verDialog.show(new CallBack() {

            @Override
            public void onCall() {
                if (isCancelApply == 1) {
                    deleteRefun(v);
                } else {
                    cancelRefund(v);
                }
            }
        }, msg);
    }

    private void deleteRefun(View v) {
        RefundmentDeleteRequest refundmentDeleteRequest = new RefundmentDeleteRequest(refundId);
        refundmentDeleteRequest.setOnResponseStateListener(new ResponseStateListener() {
            @Override
            public void onOK() {
                ViewUtils.showToast("删除成功");
                isCancelApply = 2;
                finish();
            }

            @Override
            public void onFail(int code) {
                ViewUtils.showToast("删除失败");
            }
        });
        refundmentDeleteRequest.start();
    }

    private void doCancelApply(View v) {
        isCancelApply = 1;
        findViewById(R.id.apply_modify).setVisibility(View.GONE);
        findViewById(R.id.refund_status_layout).setVisibility(View.GONE);
        TextView textView = ((TextView) findViewById(R.id.topbar_text));
        textView.setHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35,
                AppController.displayMetrics));
        textView.setText("退款已取消");
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_pay_fail, 0, 0, 0);
        ((TextView) v).setText("删除退款信息");
    }

    private void cancelRefund(final View v) {
        RefundmentCancelRequest refundmentCancel = new RefundmentCancelRequest(refundId);
        refundmentCancel.setOnResponseStateListener(new ResponseStateListener() {
            @Override
            public void onOK() {
                doCancelApply(v);
                ViewUtils.showToast("取消成功");

                //回调操作成功
                if (_parameCallBack != null) {
                    Map<String, Object> result = new HashMap<String, Object>();
                    result.put(Constants.ORDER_OPERATE_TYPE, Constants.ORDER_OPERATE_REFUND_CANCEL);
                    result.put(Constants.ORDER_NO, orderNo);
                    _parameCallBack.onCall(result);
                }
            }

            @Override
            public void onFail(int code) {
                ViewUtils.showToast("取消失败");
            }
        });
        refundmentCancel.start();
    }

    @Override
    protected void onDestroy() {
        if (isCancelApply != 0) {
            Intent intent = new Intent();
            intent.setClass(this, RefundmentListActivity.class);
            intent.putExtra("type", isCancelApply);
            startActivity(intent);
        }
//        mRefundmentDetail = null;
        _parameCallBack = null;
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GalleryViewPagerAct.start(
                this, imageUrls,
                position);
    }
}
