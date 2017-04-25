package com.zxly.o2o.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zxly.o2o.model.SystemMessage;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.SystemMsgReadRequest;
import com.zxly.o2o.request.SystemMsgReadUnloginRequest;
import com.zxly.o2o.util.AppLog;
import com.zxly.o2o.util.StringUtil;

/**
 * @author fengrongjian 2015-1-27
 * @description 系统消息详情
 */
public class PersonalSystemMsgDetailAct extends BasicAct implements
		View.OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.win_personal_system_msg_detail);
		SystemMessage msg = (SystemMessage) getIntent().getSerializableExtra(
				"msg");
		findViewById(R.id.tag_title_btn_back).setOnClickListener(this);
		((TextView)findViewById(R.id.tag_title_title_name)).setText(msg.getTypeName());
		((TextView) findViewById(R.id.title)).setText(msg.getTitle());
		((TextView) findViewById(R.id.content)).setText(msg.getContent());
	//	((TextView) findViewById(R.id.time)).setText(StringUtil.getDateByMillis(msg.getCreateTime(), "MM-dd HH:mm"));
		if(msg.getStatus() == 1){
			markRead(msg.getId());
		} else if(msg.getStatus() == 0){
			markReadUnlogin(msg.getId());
		}
	}
	
	private void markReadUnlogin(long id) {
		final SystemMsgReadUnloginRequest request = new SystemMsgReadUnloginRequest(id, 0);
		request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

			@Override
			public void onOK() {
				AppLog.e("---msg read unlogin on ok---");
			}

			@Override
			public void onFail(int code) {
				AppLog.e("---msg read unlogin on fail---");
			}
		});
		request.start(this);
	}

	private void markRead(long id) {
		final SystemMsgReadRequest request = new SystemMsgReadRequest(id);
		request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

			@Override
			public void onOK() {
				AppLog.e("---msg read on ok---");
			}

			@Override
			public void onFail(int code) {
				AppLog.e("---msg read on fail---");
			}
		});
		request.start(this);
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
