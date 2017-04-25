package com.zxly.o2o.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.StringUtil;

/**
 * @author fengrongjian 2015-3-18
 * @description 续保成功
 */
public class RenewSuccessAct extends BasicAct implements View.OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.win_renew_success);
		findViewById(R.id.tag_title_btn_back).setOnClickListener(this);
		((TextView)findViewById(R.id.tag_title_title_name)).setText("续保成功");
		((TextView) findViewById(R.id.renew_no)).setText(getIntent().getStringExtra("maintainNo"));
		((TextView) findViewById(R.id.renew_leftday)).setText(getIntent().getStringExtra("month") + "个月");
		((TextView) findViewById(R.id.renew_time)).setText(StringUtil.getDateByMillis(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss"));
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tag_title_btn_back:
			finish();
			break;
		}
	}

}
