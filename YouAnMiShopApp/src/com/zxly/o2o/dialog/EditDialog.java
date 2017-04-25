package com.zxly.o2o.dialog;

import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.CallBack;

/**
 * Created by Benjamin on 2015/7/8.
 */
public class EditDialog extends BaseDialog implements OnClickListener{

    private Button btnCancel,btnOk;
    private CallBack callBack;
    private TextView txtMsg,txtTitle;
    private EditText mEditText;
    private boolean isShowAnimation,isShowTitle=false;
    private View layoutTitle;

    @Override
    protected void initView() {
        btnCancel=(Button) findViewById(R.id.btn_cancel);
        btnOk=(Button) findViewById(R.id.btn_ok);
        txtMsg= (TextView) findViewById(R.id.txt_msg);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);

        mEditText=(EditText)findViewById(R.id.txt_edit);
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

    public String getEditText(){
        return mEditText.getText().toString().replaceAll(" ","");
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
        return R.layout.dialog_edit;
    }

    @Override
    public int getGravity() {
        return Gravity.CENTER;
    }

    public void show(CallBack callBack,Spanned msg) {
        super.show();
        this.callBack=callBack;
        txtMsg.setText(msg);
    }

    public void show(CallBack callBack,String msg) {
        super.show();
        this.callBack=callBack;
        txtMsg.setText(msg);
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
