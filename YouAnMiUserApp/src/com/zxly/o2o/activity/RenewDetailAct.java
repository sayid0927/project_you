package com.zxly.o2o.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zxly.o2o.adapter.RenewScopeAdapter;
import com.zxly.o2o.model.UserMaintain;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.RenewDetailRequest;
import com.zxly.o2o.util.DesityUtil;
import com.zxly.o2o.util.EncodingHandler;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

/**
 * @author fengrongjian 2015-3-17
 * @description 保修详情
 */
public class RenewDetailAct extends BasicAct implements
        View.OnClickListener {
    private static final int GO_TO_RENEW = 10;
    private UserMaintain maintain = null;
    protected String maintainNo;
    private RenewScopeAdapter scopeAdapter;
    private ListView listView = null;
    private TextView txtDesc = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_renew_detail);
        maintain = (UserMaintain) getIntent().getSerializableExtra(
                "guarantee");
        initViews();
        getRenewDetail(maintain.getId(), maintain.getType());
    }

    private void initViews() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        ViewUtils.setText(findViewById(R.id.txt_title), "保修详情");
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String info = StringUtil.generateScanInfo(maintain);
                    int size = DesityUtil.dp2px(RenewDetailAct.this, 100);
                    final Bitmap bitmap = EncodingHandler.createQRImage(info, size, size);
                    findViewById(R.id.qr).post(new Runnable() {
                        @Override
                        public void run() {
                            ((ImageView) findViewById(R.id.qr)).setImageBitmap(bitmap);
                        }
                    });
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((TextView) findViewById(R.id.txt_product_name)).setText(maintain.getProductName());
        ((TextView) findViewById(R.id.txt_price)).setText("￥" + maintain.getPrice());
        if (maintain.getResidueTime() < 1) {
            findViewById(R.id.renew_left_day).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.renew_left_day_tail)).setText(" 已过期");
        } else {
            findViewById(R.id.renew_left_day).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.renew_left_day)).setText(maintain.getResidueTime() + "");
            ((TextView) findViewById(R.id.renew_left_day_tail)).setText("天");
        }
        findViewById(R.id.btn_title_right).setOnClickListener(this);
        txtDesc = (TextView) findViewById(R.id.txt_desc);
        listView = (ListView) findViewById(R.id.renew_scope_list);
        scopeAdapter = new RenewScopeAdapter(this);
        listView.setAdapter(scopeAdapter);
    }

    private void getRenewDetail(Long id, int type) {
        final RenewDetailRequest request = new RenewDetailRequest(id);
        request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                maintainNo = request.getMaintainNo();
                ((TextView) findViewById(R.id.renew_maintain_no)).setText(maintainNo);
                if (maintain.getType() == 1) {
                    listView.setVisibility(View.GONE);
                    txtDesc.setVisibility(View.VISIBLE);
                    txtDesc.setText(request.getDesc());
                } else {
                    txtDesc.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    scopeAdapter.clear();
                    scopeAdapter.addItem(request.getRenewList(), true);
                }
            }

            @Override
            public void onFail(int code) {
            }
        });
        request.start(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_title_right:
                Intent intent = new Intent(this, RenewAddAct.class);
                intent.putExtra("maintain", this.maintain);
                intent.putExtra("maintainNo", maintainNo);
                startActivityForResult(intent, GO_TO_RENEW);
                break;
        }
    }

}
