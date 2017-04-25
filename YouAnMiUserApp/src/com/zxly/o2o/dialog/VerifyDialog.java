package com.zxly.o2o.dialog;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.ViewUtils;

public class VerifyDialog extends BaseDialog implements View.OnClickListener{

	private Button btnCancel,btnOk;
	private CallBack callBack;
	private TextView txtMsg,txtTitle;
    private boolean isShowAnimation,isShowTitle=false;
    private View layoutTitle;
    
	@Override
	protected void initView() {
		btnCancel=(Button) findViewById(R.id.btn_cancel);
		btnOk=(Button) findViewById(R.id.btn_ok);
		txtMsg= (TextView) findViewById(R.id.txt_msg);
		btnCancel.setOnClickListener(this);
		btnOk.setOnClickListener(this);
	
		layoutTitle=findViewById(R.id.layout_title);
		
		txtTitle=(TextView) findViewById(R.id.txt_title);		
	}

	public Button getOkbutton(){
		return btnOk;
	}
	
	public Button getCancleButton(){
		return btnCancel;
	}
	
	public TextView getContextText(){
		return txtMsg;
	}
	
	public TextView getTitle(){
		return txtTitle;
	}
	
	public void setIsShowTitle(boolean isShowTitle){
		this.isShowTitle=isShowTitle;
	}
	
	@Override
	protected boolean isShowAnimation() {
		return isShowAnimation;
	}
	
	public void setIsShowAnimation(boolean isShowAnimation){
		this.isShowAnimation=isShowAnimation;
	}
	
	@Override
	public int getLayoutId() {
		return R.layout.dialog_verify;
	}

	@Override
	public int getGravity() {	
		return Gravity.CENTER;
	}
	
	public void show(CallBack callBack,String msg) {
		super.show();
		this.callBack=callBack;
		ViewUtils.setText(txtMsg, msg);
	}

	public void show(CallBack callBack) {
		if(isShowTitle)
			layoutTitle.setVisibility(View.VISIBLE);
		super.show();
		this.callBack=callBack;
	}
	
	@Override
	public void onClick(View v) {
		 if(v==btnOk)
		{
			this.callBack.onCall();
		}
		 dismiss();
		 
	}
	

}
