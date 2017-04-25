package com.easemob.easeui.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chatuidemo.HXApplication;
import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.HXHelper;
import com.easemob.chatuidemo.R;
import com.easemob.chatuidemo.utils.EaseCallBack;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.adapter.EaseContactAdapter;
import com.easemob.easeui.domain.EaseYAMUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Administrator on 2015/12/7.
 */
public class EaseContactFilterDialog extends EaseBaseDialog
        implements DatePickerDialog.OnDateSetListener, View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, View.OnFocusChangeListener, TextWatcher {
    protected Dialog dialog;
    private RadioButton unRegist, registed;
    private EditText yjF, yjL;
    private TextView registF, registL, clickedRegist;
    private double checkedYJF, checkedYJL;
    private long checkedRegistF, checkedRegistL;
    private TextView yj1, yj2, yj3, yj4, yj5;
    private TextView regist1, regist2, regist3, regist4, regist5;
    private int multiPushOrSend;
    private TextView cleanFilterBtn;
    private TextView submit;
    private TextView title;
    private EaseCallBack mCallBack;

    public EaseContactFilterDialog(Context context, int multiPushOrSend,EaseCallBack callBack) {
        super(context);
        mCallBack=callBack;
        this.multiPushOrSend = multiPushOrSend;
        setMulPushOrSend();
    }

    private void setMulPushOrSend() {
        //初始化注册和未注册按钮
        if (multiPushOrSend == HXConstant.MUL_SEND) {
            unRegist.setVisibility(View.GONE);
            registed.setOnCheckedChangeListener(this);
            registed.setChecked(true);
            registed.setClickable(false);
            registed.setOnCheckedChangeListener(null);
            title.setText("群发消息");
        } else if (multiPushOrSend == HXConstant.MUL_PUSH) {
            unRegist.setOnCheckedChangeListener(this);
            unRegist.setChecked(true);
            registed.setOnCheckedChangeListener(this);
            title.setText("推送广告");
        } else if (multiPushOrSend == 3) {
            unRegist.setOnCheckedChangeListener(this);
            unRegist.setChecked(true);
            registed.setOnCheckedChangeListener(this);
            title.setText("筛选");
        }
    }

    @Override
    protected void initView() {

        HXHelper.getInstance().cleanCheckMembers();

        //title
        title = (TextView) findViewById(R.id.filter_title);

        //取消过滤
        cleanFilterBtn = (TextView) findViewById(R.id.clean_filter);
        submit = (TextView) findViewById(R.id.submit);

        //取消和确定按钮
        findViewById(R.id.cancel).setOnClickListener(this);
        submit.setOnClickListener(this);

        //注册和未注册按钮
        unRegist = ((RadioButton) findViewById(R.id.unregisted_btn));
        registed = ((RadioButton) findViewById(R.id.registed_btn));

        yjF = ((EditText) findViewById(R.id.yj_input_btn1));
        yjL = ((EditText) findViewById(R.id.yj_input_btn2));

        yjF.addTextChangedListener(this);
        yjL.addTextChangedListener(this);

        //注册时间范围按钮
        registF = ((TextView) findViewById(R.id.regist_time_input_btn1));
        registL = ((TextView) findViewById(R.id.regist_time_input_btn2));

        registF.setOnClickListener(this);
        registL.setOnClickListener(this);

        //佣金范围按钮
        yj1 = (TextView) findViewById(R.id.yj_btn1);
        yj2 = (TextView) findViewById(R.id.yj_btn2);
        yj3 = (TextView) findViewById(R.id.yj_btn3);
        yj4 = (TextView) findViewById(R.id.yj_btn4);

        //注册范围按钮
        regist1 = (TextView) findViewById(R.id.registed_btn1);
        regist2 = (TextView) findViewById(R.id.registed_btn2);
        regist3 = (TextView) findViewById(R.id.registed_btn3);
        regist4 = (TextView) findViewById(R.id.registed_btn4);
        //添加事件Listener
        regist1.setOnClickListener(this);
        regist2.setOnClickListener(this);
        regist3.setOnClickListener(this);
        regist4.setOnClickListener(this);
        yj1.setOnClickListener(this);
        yj2.setOnClickListener(this);
        yj3.setOnClickListener(this);
        yj4.setOnClickListener(this);
        yjF.setOnFocusChangeListener(this);
        yjL.setOnFocusChangeListener(this);
        cleanFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanFilter();
            }
        });


        checkedRegistL = System.currentTimeMillis();
        // 获取设置contactlist
        HXHelper.getInstance().getYAMContactList();
    }

    private void cleanFilter() {
        cleanYJInput();
        cleanRegistInput();

        if (regist5 != null) {
            setBtnNormal(regist5);
            setRegistFilterValue(regist5);
        }

        if (yj5 != null) {
            setBtnNormal(yj5);
            setYJFilterValue(yj5);
        }

        checkedYJF = 0.0;
        checkedYJL = 0.0;

        checkedRegistF = 0;
        checkedRegistL = System.currentTimeMillis();

        initFilterContact(); //过滤条件
    }

    private void cleanRegistInput() {
        registF.setText("");
        registL.setText("");
    }

    private void cleanYJInput() {
        if (!TextUtils.isEmpty(yjF.getText())) {
            yjF.setText("");
        }
        if (!TextUtils.isEmpty(yjL.getText())) {
            yjL.setText("");
        }
    }


    private void initFilterContact() {
        //clean push members
        if(EaseConstant.users!=null)
        EaseConstant.users.clear();

        if(EaseConstant.getuiUsers!=null)
        EaseConstant.getuiUsers.clear();

        if (unRegist.isChecked() && !registed.isChecked()) {
            EaseConstant.isRegistMembers = false;
        } else if (!unRegist.isChecked() && registed.isChecked()) {
            EaseConstant.isRegistMembers = true;
        }


        if (EaseConstant.isRegistMembers) {
            //如果佣金选项没有选中则把自定义的佣金赋值给这两货
            setCheckedYJValue();

            //添加符合过滤条件人员到发送人员列表
            if (HXHelper.yamContactList.size() > 0) {
                Double yj;
                long registTime;
                for (int i = 0; i < HXHelper.yamContactList.size(); i++) {
                    yj = EaseContactAdapter.commissionMap.get(HXHelper
                            .yamContactList.get(i).getFirendsUserInfo().getId());
                    registTime = HXHelper
                            .yamContactList.get(i).getFirendsUserInfo().getCreateTime();
                    if (yj == null) {
                        yj = 0.0;
                    }


                    if (checkedYJL == 0 || yj >= checkedYJF && yj <= checkedYJL) {
                        if (registTime >= checkedRegistF && registTime <= checkedRegistL) {
                            EaseConstant.users.add(HXApplication.getInstance().parseUserFromID(
                                    HXHelper.yamContactList.get(i).getFirendsUserInfo().getId(),
                                    HXConstant.TAG_USER));
                            EaseConstant.getuiUsers.add(HXHelper.yamContactList.get(i).getFirendsUserInfo().getId()+"");
                            HXHelper.yamContactList.get(i).setIsCheck(true); //被选中
                        }
                    } else {
                        HXHelper.yamContactList.get(i).setIsCheck(false); //没被选中
                    }
                }
            }
        } else if (EaseContactAdapter.unRegistList.size() > 0) {
//            添加未注册人员到发送人员列表
            for (EaseYAMUser user : EaseContactAdapter.unRegistList) {
                EaseConstant.users.add(HXApplication.getInstance()
                        .getUnRegistUserName(
                                user.getFirendsUserInfo().getImei()));
                EaseConstant.getuiUsers.add(user.getFirendsUserInfo().getImei());
            }
        }

        submit.setText("确认("+ EaseConstant.users.size()+")");
    }



    private void setCheckedYJValue() {
        if (checkedYJF == 0 && checkedYJL == 0) {
            if ("".equals(yjF.getText().toString())) {
                checkedYJF = 0;
            } else {
                checkedYJF = Double.valueOf(yjF.getText().toString());
            }

            if ("".equals(yjL.getText().toString())) {
                checkedYJL = 0;
            } else {
                checkedYJL = Double.valueOf(yjL.getText().toString());
            }
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.em_contact_filter_dialog_layout;
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit) {
            if (multiPushOrSend == 3) {
                if(mCallBack!=null){
                    mCallBack.onCall();
                }
                dismiss();
            } else if (multiPushOrSend == HXConstant.MUL_PUSH || multiPushOrSend == HXConstant.MUL_SEND) {
                if (EaseConstant.users.size() > 0) {
                    dismiss();
                    String commission=checkedYJF+"~"+checkedYJL;
                    String registerTime=checkedRegistF+"~"+checkedRegistL;
                   // new SendMsgStatisticsRequest(commission, EaseConstant.isRegistMembers,registerTime,multiPushOrSend).start();
                    new SelectMultiPushDialog(context).show(multiPushOrSend, commission, EaseConstant.isRegistMembers,registerTime);
                } else {
                    Toast.makeText(context, "没有要发送的人员", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (v.getId() == R.id.cancel) {
            EaseConstant.users.clear();
            EaseConstant.getuiUsers.clear();
            this.dismiss();
        } else if (v == regist1 || v == regist2 || v == regist3 || v == regist4) {
            if (v == regist5) {
                setBtnNormal(regist5);
                regist5 = null;
                checkedRegistF = 0;
                checkedRegistL = System.currentTimeMillis();
            } else {
                if (regist5 != null) {
                    setBtnNormal(regist5);
                }
                regist5 = (TextView) v;
                setBtnPress(regist5);
                setRegistFilterValue(regist5);
            }
            initFilterContact(); //过滤条件
        } else if (v == yj1 || v == yj2 || v == yj3 || v == yj4) {
            if (v == yj5) {
                setBtnNormal(yj5);
                yj5 = null;
                checkedYJF = 0;
                checkedYJL = 0;
            } else {
                if (yj5 != null) {
                    setBtnNormal(yj5);
                }
                yj5 = (TextView) v;
                setBtnPress(yj5);
                setYJFilterValue((TextView) v);
            }
            initFilterContact(); //过滤条件
            cleanYJInput();
        } else if (v == registF) {
            clickedRegist = registF;
            createBirthDialog();
        } else if (v == registL) {
            clickedRegist = registL;
            createBirthDialog();
        }


    }

    private void setBtnNormal(TextView v) {
        v.setBackgroundResource(R
                .drawable.ease_grey_btn);
        v.setTextColor(Color.parseColor("#051b28"));
    }



    private void setBtnPress(TextView v) {
        v.setBackgroundResource(R
                .drawable.ease_orange_btn);
        v.setTextColor(Color.parseColor("#ffffff"));
    }

    private void createBirthDialog() {
        long time = getLongFromDataStr(clickedRegist.getText().toString());
        Date date;
        if (time > 0) {
            date = new Date(time);
        } else {
            date = new Date(System.currentTimeMillis());
        }

        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        Dialog dialog = new EaseSettingDatePickerDialog(context,
                this,
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {
            buttonView.setBackgroundResource(R
                    .drawable.ease_orange_btn);
            buttonView.setTextColor(Color.parseColor("#ffffff"));
        } else {
            buttonView.setBackgroundResource(R
                    .drawable.ease_grey_btn);
            buttonView.setTextColor(Color.parseColor("#051b28"));
        }

        initFilterContact(); //过滤条件

    }

    private long getLongFromDataStr(String dataStr) {
        //将字符串类型转化成Date类型
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = sdf.parse(dataStr);//将String to Date类型
            return d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == yjF || v == yjL) {
            checkedYJF = 0.0;
            checkedYJL = 0.0;
            if (yj5 != null) {
                setBtnNormal(yj5);
            }
        }
    }

    public void setYJFilterValue(TextView buttonView) {
        if (buttonView == yj1) {  //btn1 check
            checkedYJF = 0.0;
            checkedYJL = 50.0;
        } else if (buttonView == yj2) {   //btn2 check
            checkedYJF = 50.0;
            checkedYJL = 100.0;
        } else if (buttonView == yj3) {   //btn3 check
            checkedYJF = 100.0;
            checkedYJL = 200.0;
        } else if (buttonView == yj4) {   //btn4 check
            checkedYJF = 200.0;
            checkedYJL = 2000000.0;   //这值越大越好
        }
    }

    public void setRegistFilterValue(TextView buttonView) {
        long curTime = System.currentTimeMillis();
        if (buttonView == regist1) {  //btn1 check
            checkedRegistF = curTime - 30 * 24 * 60 * 60 * 1000l;  //约一个月
            checkedRegistL = curTime;
            cleanRegistInput();
        } else if (buttonView == regist2) {   //btn2 check
            checkedRegistF = curTime - 90 * 24 * 60 * 60 * 1000l;  //约三个月
            checkedRegistL = curTime;
            cleanRegistInput();
        } else if (buttonView == regist3) {   //btn3 check
            checkedRegistF = curTime - 180 * 24 * 60 * 60 * 1000l; //约半年
            checkedRegistL = curTime;
            cleanRegistInput();
        } else if (buttonView == regist4) {   //btn4 check
            checkedRegistF = curTime - 365 * 24 * 60 * 60 * 1000l;  //约一年
            checkedRegistL = curTime;
            cleanRegistInput();
        }


    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        clickedRegist.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
        if (clickedRegist == registF) {
            checkedRegistF = getLongFromDataStr(clickedRegist.getText().toString());
        } else if (clickedRegist == registL) {
            checkedRegistL = getLongFromDataStr(clickedRegist.getText().toString());
            checkedRegistL = checkedRegistL + 23 * 60 * 60 * 1000l;
        }

        if (regist5 != null) {
            setBtnNormal(regist5);
        }

        initFilterContact(); //过滤条件
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        checkedYJF = 0.0;
        checkedYJL = 0.0;
        initFilterContact(); //过滤条件
    }
}
