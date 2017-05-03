package com.zxly.o2o.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.zxly.o2o.application.AppController;
import com.zxly.o2o.fragment.GuaranteeListFragment;
import com.zxly.o2o.fragment.GuaranteeOrderDetailFragment;
import com.zxly.o2o.fragment.InsureInfoComplementFragment;
import com.zxly.o2o.fragment.InsureInfoModifyFragment;
import com.zxly.o2o.model.GuaranteeInfo;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.GuaranteeDetailRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;


/**
 * Created by kenwu on 2016/5/25.
 */
public class GuaranteeDetailAct extends BasicAct {
    GuaranteeListFragment.GuaranteeWarningDialog guaranteeWarningDialog;
    private GuaranteeDetailRequest guaranteeDetailRequest;
    LoadingView loadingView;
    InsureInfoComplementFragment insureInfoComplementFragment;
    InsureInfoModifyFragment insureInfoModifyFragment;
    GuaranteeOrderDetailFragment guaranteeOrderDetailFragment;
    FragmentTransaction ft;
    public TextView txtTitle,btnRight;
    public View btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.win_common);

        txtTitle=(TextView)findViewById(R.id.txt_title);
        btnRight= (TextView) findViewById(R.id.btn_right);

        btnBack= findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadingView= (LoadingView) findViewById(R.id.view_loading);

        loadingView.setOnAgainListener(new LoadingView.OnAgainListener() {
            @Override
            public void onLoading() {
                guaranteeDetailRequest.start(GuaranteeDetailAct.this);
            }
        });

        ft = getSupportFragmentManager().beginTransaction();

        String id=getIntent().getStringExtra("id");
        guaranteeDetailRequest=new GuaranteeDetailRequest(id);
        guaranteeDetailRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                loadingView.onLoadingComplete();

                int curStatus = guaranteeDetailRequest.getGuaranteeInfo().getOrderStatus();

//                int curStatus=GuaranteeInfo.STATUS_CANCELED;
                loadPage(curStatus);

            }

            @Override
            public void onFail(int code) {
                loadingView.onLoadingFail();
            }
        });



    }


    public void loadPage(int curStatus){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (curStatus){
            case GuaranteeInfo.STATUS_MODIFY:
                txtTitle.setText("订单资料修改");
                if (guaranteeOrderDetailFragment != null){
                    ft.hide(guaranteeOrderDetailFragment);
                }
                if(insureInfoModifyFragment == null){
                    insureInfoModifyFragment = new InsureInfoModifyFragment();
                }
                insureInfoModifyFragment.setData(guaranteeDetailRequest.getGuaranteeInfo());
                if (!insureInfoModifyFragment.isAdded()) {
                    ft.add(R.id.layout_content, insureInfoModifyFragment);
                    ft.show(insureInfoModifyFragment);
                    ft.commit();
                } else {
                    insureInfoModifyFragment.init();
                }
                break;
            case GuaranteeInfo.STATUS_WAIT_FOR_CONFIRN:
                txtTitle.setText("信息补充");
                btnRight.setText("拒单");
                btnRight.setVisibility(View.VISIBLE);


                if (insureInfoComplementFragment==null)
                    insureInfoComplementFragment = new InsureInfoComplementFragment();

                insureInfoComplementFragment.setData(guaranteeDetailRequest.getGuaranteeInfo());
                if (!insureInfoComplementFragment.isAdded()) {
                    ft.add(R.id.layout_content, insureInfoComplementFragment);
                    ft.show(insureInfoComplementFragment);
                    ft.commit();
                } else {
                    insureInfoComplementFragment.init();
                }
                break;

            case GuaranteeInfo.STATUS_CANCELED:
                txtTitle.setText("详情");
                guaranteeWarningDialog =new GuaranteeListFragment.GuaranteeWarningDialog();
                guaranteeWarningDialog.show("抱歉,用户已取消了购买申请。",new CallBack() {
                    @Override
                    public void onCall() {
                        GuaranteeDetailAct.this.finish();

                    }
                });
                break;

            case GuaranteeInfo.STATUS_REFUSED:
                txtTitle.setText("详情");
                guaranteeWarningDialog =new GuaranteeListFragment.GuaranteeWarningDialog();
                guaranteeWarningDialog.show("此单已被您拒绝！",new CallBack() {
                    @Override
                    public void onCall() {
                        GuaranteeDetailAct.this.finish();

                    }
                });
                break;

            default:
                txtTitle.setText("详情");

                if (guaranteeOrderDetailFragment == null)
                    guaranteeOrderDetailFragment = GuaranteeOrderDetailFragment.newInstance(getIntent().getExtras());

                guaranteeOrderDetailFragment.setData(guaranteeDetailRequest.getGuaranteeInfo());
                if (!guaranteeOrderDetailFragment.isAdded()){
                    ft.add(R.id.layout_content, guaranteeOrderDetailFragment);
                    ft.show(guaranteeOrderDetailFragment);
                    ft.commit();
                }else {
                    guaranteeOrderDetailFragment.refreshUI();
                }
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadingView.startLoading();
        guaranteeDetailRequest.start(this);
    }

    public static void start(String id){
        Intent it=new Intent();
        it.putExtra("id",id);
        it.setClass(AppController.getInstance().getTopAct(),GuaranteeDetailAct.class);
        ViewUtils.startActivity(it,AppController.getInstance().getTopAct());
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (insureInfoComplementFragment != null) {
            insureInfoComplementFragment.onActivityResult(requestCode, resultCode, data);
        } else if (insureInfoModifyFragment != null) {
            insureInfoModifyFragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
