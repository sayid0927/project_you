package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.InsuranceClauseRequest;
import com.zxly.o2o.util.ViewUtils;

public class InsuranceClauseAct extends BasicAct implements View.OnClickListener {
    private long id;
    private TextView txtClause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_insurance_clause);
        findViewById(R.id.btn_back).setOnClickListener(this);
        ViewUtils.setGone(findViewById(R.id.btn_service));
        ViewUtils.setText(findViewById(R.id.txt_title), "保障条款");
        txtClause = (TextView) findViewById(R.id.txt_clause);
        txtClause.setMovementMethod(ScrollingMovementMethod.getInstance());
        id = getIntent().getLongExtra("id", 0);
        loadData();
    }

    public static void start(Activity curAct, long id) {
        Intent intent = new Intent(curAct, InsuranceClauseAct.class);
        intent.putExtra("id", id);
        ViewUtils.startActivity(intent, curAct);
    }

    private void loadData() {
        final InsuranceClauseRequest request = new InsuranceClauseRequest(id);
        request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                String serviceTerm = request.getServiceTerm();
                ViewUtils.setText(txtClause, Html.fromHtml(serviceTerm));
            }

            @Override
            public void onFail(int code) {
            }
        });
        request.start(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_back) {
            finish();
        }
    }
}
