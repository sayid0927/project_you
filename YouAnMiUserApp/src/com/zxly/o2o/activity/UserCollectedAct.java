package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.zxly.o2o.fragment.ArticleCollectedFragment;
import com.zxly.o2o.fragment.PaperCollectFragment;
import com.zxly.o2o.fragment.ProductCollectFragment;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.UMengAgent;
import com.zxly.o2o.util.ViewUtils;

public class UserCollectedAct extends BasicAct implements View.OnClickListener {
    private ArticleCollectedFragment articleCollectedFragment;
    private ProductCollectFragment productCollectFragment;
    private RadioButton btnTopic, btnProduct;
    private RadioButton btn_paper;
    private PaperCollectFragment paperCollectFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_my_collect);
        initView();
    }

    public static void start(Activity curAct) {
        Intent intent = new Intent(curAct, UserCollectedAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

    private void initView() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        btnTopic = (RadioButton) findViewById(R.id.btn_topic);
        btnProduct = (RadioButton) findViewById(R.id.btn_product);
        btn_paper = (RadioButton) findViewById(R.id.btn_paper);
        btn_paper.setChecked(true);
        btnTopic.setOnClickListener(this);
        btnProduct.setOnClickListener(this);
        btn_paper.setOnClickListener(this);
        //帖子
        articleCollectedFragment = new ArticleCollectedFragment();
        //商品
        productCollectFragment = new ProductCollectFragment();
        //文章
        paperCollectFragment = new PaperCollectFragment();
        replaceFragment(R.id.fragment_main, paperCollectFragment);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_topic:
                replaceFragment(R.id.fragment_main, articleCollectedFragment);
                UMengAgent.onEvent(UserCollectedAct.this, UMengAgent.collected_article_page);
                break;
            case R.id.btn_paper:
                replaceFragment(R.id.fragment_main, paperCollectFragment);
                break;
            case R.id.btn_product:
                replaceFragment(R.id.fragment_main, productCollectFragment);
                break;
        }
    }
}
