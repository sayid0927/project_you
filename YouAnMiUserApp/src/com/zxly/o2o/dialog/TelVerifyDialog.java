package com.zxly.o2o.dialog;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.ViewUtils;

/**
 * Created by Administrator on 2015/7/29.
 */
public class TelVerifyDialog extends BaseDialog implements View.OnClickListener {

    private Button btnOk;
    private CallBack callBack;
    private TextView txtMsg, txtTitle;
    private boolean isShowAnimation, isShowTitle = false;
    private View layoutTitle;

    @Override
    protected void initView() {
        btnOk = (Button) findViewById(R.id.btn_ok);
        txtMsg = (TextView) findViewById(R.id.txt_msg);
        btnOk.setOnClickListener(this);

        layoutTitle = findViewById(R.id.layout_title);

        txtTitle = (TextView) findViewById(R.id.txt_title);
    }

    public TextView getContextText() {
        return txtMsg;
    }

    public void setIsShowTitle(boolean isShowTitle) {
        this.isShowTitle = isShowTitle;
    }

    @Override
    protected boolean isShowAnimation() {
        return isShowAnimation;
    }

    public void setIsShowAnimation(boolean isShowAnimation) {
        this.isShowAnimation = isShowAnimation;
    }

    @Override
    public int getLayoutId() {
        return R.layout.tel_dialog_verify;
    }

    @Override
    public int getGravity() {
        return Gravity.CENTER;
    }

    public void show(CallBack callBack, String msg) {
        super.show();
        this.callBack = callBack;
        ViewUtils.setText(txtMsg, msg);
    }

    @Override
    public void onClick(View v) {
        this.callBack.onCall();
        dismiss();

    }


}
