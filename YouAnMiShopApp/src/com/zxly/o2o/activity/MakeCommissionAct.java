package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.zxly.o2o.fragment.PromotionProductFragment;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.UmengUtil;
import com.zxly.o2o.util.ViewUtils;

/**
 * Created by kenwu on 2015/12/17.
 */
public class MakeCommissionAct extends BasicAct implements View.OnClickListener {
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.win_common);

        TextView txtTitle= (TextView) findViewById(R.id.txt_title);
        TextView txtRight= (TextView) findViewById(R.id.btn_right);
        type = getIntent().getIntExtra("type", 0);
        if(0 == type){
            txtTitle.setText("赚佣金");
            txtRight.setVisibility(View.VISIBLE);
            txtRight.setText("佣金记录");
            txtRight.setOnClickListener(this);
        } else {
            txtTitle.setText("商品推广");
            txtRight.setVisibility(View.GONE);
        }

        View btnBack=findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);

      //  TaskTargetListFragment fragment=TaskTargetListFragment.newInstance();
     //  PromotionAcitcityFragment fragment=PromotionAcitcityFragment.newInstance();
        Fragment fragment= PromotionProductFragment.newInstance();
    // Fragment fragment= PromotionArticleFragment.newInstance();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_content,fragment);
        ft.show(fragment);
        ft.commit();

        UmengUtil.onEvent(MakeCommissionAct.this,new UmengUtil().PROMOTION_ENTER,null);
    }

    /**
     * @param curAct
     * @param type    0：赚佣金，1:商品推广
     */
    public static void start(Activity curAct, int type) {
        Intent intent = new Intent(curAct, MakeCommissionAct.class);
        intent.putExtra("type", type);
        ViewUtils.startActivity(intent, curAct);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_back:
                finish();
                UmengUtil.onEvent(MakeCommissionAct.this,new UmengUtil().PROMOTION_BACK_CLICK,null);
                break;

            case R.id.btn_right:
                ViewUtils.startActivity(new Intent(this,CommissionRecordAct.class),this);
                UmengUtil.onEvent(MakeCommissionAct.this,new UmengUtil().PROMOTION_RECORDS_CLICK,null);
                break;

            default:
                break;
        }

    }
}
