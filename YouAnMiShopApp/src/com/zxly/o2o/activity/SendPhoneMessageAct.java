package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.request.MarkFansSendMsmRequest;
import com.zxly.o2o.request.MarkSendMsmRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ViewUtils;

/**
 * Created by hejun on 2016/9/12.
 * 发短信
 */
public class SendPhoneMessageAct extends BasicAct implements View.OnClickListener {
    //粉丝发短信
    private static final int TYPE_FANS = 1;
    //会员发短信
    private static final int TYPE_MENBER = 2;
    private EditText edit_input;
    private TextView txt_length;
    private String messageContent;
    private IntentFilter mFilter;
    private long id;
    private String mobilePhone;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.win_send_message);
        findAndInitView();
    }

    private void findAndInitView() {
        id =getIntent().getLongExtra("id",0);
        mobilePhone = getIntent().getStringExtra("mobilePhone");
        type = getIntent().getIntExtra("type", 1);
        //短信默认内容为
        ((TextView)findViewById(R.id.txt_title)).setText("发短信");
        messageContent="";
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_send).setOnClickListener(this);
        txt_length = (TextView) findViewById(R.id.txt_length);
        edit_input = (EditText) findViewById(R.id.edit_input);
        edit_input.setHint("点击输入短信内容");
//        edit_input.setText("【"+Account.user.getShopName()+ "】亲，购机优惠想要吗？手机行业资讯想了解吗？更多活动想参加吗？赶紧免费注册成为本店会员，专享会员福利哟！");
        edit_input.setSelection(edit_input.getText().length());
        ((TextView) findViewById(R.id.txt_length)).setText(edit_input.getText().toString().trim().length()+ "/100");
        edit_input.addTextChangedListener(textWatcher);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
            break;
            case R.id.btn_send:
                if(!TextUtils.isEmpty(edit_input.getText().toString().trim())){
                    messageContent=edit_input.getText().toString().trim();
                    doSendSMSTo(mobilePhone,messageContent);
                }else {
                    ViewUtils.showToast("短信内容不能为空哦");
                }
                break;
        }
    }

    TextWatcher textWatcher = new TextWatcher() {
        private CharSequence temp;
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp=s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (temp.length() > 100) {
                s.delete(100, s.length());
                edit_input.setText(s);
                edit_input.setSelection(s.length());
                ViewUtils.showToast("不能输入更多了!");
            }
                ((TextView) findViewById(R.id.txt_length)).setText( s.length() + "/100");
        }
    };

    /**
     * 调起系统发短信功能
     * @param phoneNumber
     * @param message
     */
    public void doSendSMSTo(String phoneNumber,String message){
        if(!TextUtils.isEmpty(phoneNumber)){
            if(TYPE_FANS==type){
                new MarkFansSendMsmRequest(id,message).start();
            }else if(TYPE_MENBER==type){
                new MarkSendMsmRequest(id).start();
            }
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+phoneNumber));
            intent.putExtra("sms_body", message);
            startActivity(intent);
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void start(Activity curAct, long id, String mobilePhone,int type){
        Intent it=new Intent();
        it.putExtra("id",id);
        it.putExtra("type",type);
        it.putExtra("mobilePhone",mobilePhone);
        it.setClass(curAct, SendPhoneMessageAct.class);
        ViewUtils.startActivity(it, curAct);
    }
}
