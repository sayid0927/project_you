/*
 * 文件名：GetPictureDialog.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： GetPictureDialog.java
 * 修改人：Administrator
 * 修改时间：2015-1-16
 * 修改内容：新增
 */
package com.zxly.o2o.dialog;

import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.Constants;

/**
 * TODO 添加类的一句话简单描述。
 * <p>
 * TODO 详细描述
 * <p>
 * TODO 示例代码
 * 
 * <pre>
 * </pre>
 * 
 * @author huangbin
 * @version YIBA-O2O 2015-1-16
 * @since YIBA-O2O
 */
public class GetPictureDialog extends BaseDialog implements OnClickListener {
	private Handler handler;


	public GetPictureDialog(boolean b) {
		if(b){
			((TextView)findViewById(R.id.get_pic_from_cellphone)).setText("重新选择图片");
			((TextView)findViewById(R.id.get_pic_from_camera)).setText("重新照相");
			(findViewById(R.id.delete_pic)).setVisibility(View.VISIBLE);
		}
	}

	@Override
	public int getLayoutId() {

		return R.layout.dialog_get_pic;
	}

	public void show(Handler mHandler) {
		this.handler = mHandler;
		super.show();
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		findViewById(R.id.get_pic_from_cellphone).setOnClickListener(this);
		findViewById(R.id.get_pic_from_camera).setOnClickListener(this);
		findViewById(R.id.get_pic_cancel).setOnClickListener(this);
		findViewById(R.id.delete_pic).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.get_pic_from_cellphone:
			dismiss();
			handler.sendEmptyMessage(Constants.GET_PIC_FROM_CELLPHONE);
			break;
		case R.id.get_pic_from_camera:
			dismiss();
			handler.sendEmptyMessage(Constants.GET_PIC_FROM_CAMERA);
			break;
		case R.id.get_pic_cancel:
			dismiss();
			handler.sendEmptyMessage(Constants.GET_PIC_FROM_CANCEL);
			break;
		case R.id.delete_pic:
			dismiss();
			handler.sendEmptyMessage(Constants.DELETE_PIC);
			break;

		default:
			break;
		}
	}
}
