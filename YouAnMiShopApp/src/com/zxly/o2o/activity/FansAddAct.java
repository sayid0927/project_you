package com.zxly.o2o.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zxly.o2o.adapter.TagAdapter;
import com.zxly.o2o.model.FansInfo;
import com.zxly.o2o.model.TagModel;
import com.zxly.o2o.request.AddOfflineFansRequest;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.MemberLabelsRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.UmengUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.FlowTagLayout;
import com.zxly.o2o.view.LoadingView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengrongjian 2016-8-25
 * @description 新增线下粉丝
 */
public class FansAddAct extends BasicAct implements View.OnClickListener {
    private static final int REQUEST_CODE = 100;
    private static CallBack _callBack;
    private static ParameCallBack _parameCallBack;
    private Context context;
    private LoadingView loadingview;
    private TextView btnTitleRight;
    private EditText editName, editPhone, editProductName, editProductPrice, editUserIntroduce;
    private CheckBox cbxStar1, cbxStar2, cbxStar3, cbxShopping;
    private FlowTagLayout labels;
    private TagAdapter mMobileTagAdapter;
    private List<TagModel> memberLabelList = new ArrayList<TagModel>();
    private ArrayList<Long> selectLabels = new ArrayList<Long>();
    private int gender = 1;
    private EditText edit_phone_brand;
    private Dialog dialog;
    private Dialog hasSameNamedialog;
    private int buyOffline;
    private boolean shouldReturn;
    private String name;
    private String phone;
    private String phone_brand;
    private String productName;
    private String productPrice;
    private String remarkContent;
    private int buyIntention;
    private int stillSave;
    //输入表情前的光标位置
    private int cursorPos;
    //输入表情前EditText中的文本
    private String inputAfterText;
    //是否重置了EditText的内容
    private boolean resetText;
    private int cursorPosIntroduce;
    private String inputAfterTextIntroduce;
    private boolean resetTextIntroduce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_fans_add);
        context = this;
        initViews();
        loadMemberLabels();
    }

    private void loadMemberLabels() {
        final MemberLabelsRequest memberLabelsRequest = new MemberLabelsRequest();
        memberLabelsRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                memberLabelList = memberLabelsRequest.getMemberLabelList();
                mMobileTagAdapter.onlyAddAll(memberLabelList);
            }

            @Override
            public void onFail(int code) {

            }
        });
        memberLabelsRequest.start(FansAddAct.this);
    }

    public static void start(Activity curAct) {
        Intent intent = new Intent(curAct, FansAddAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

    public static void start(Activity curAct, CallBack callBack) {
        _callBack =callBack;
        Intent intent = new Intent(curAct, FansAddAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

    //录入线下粉丝界面  标题栏右侧点击新增调用
    public static void startForResult(Activity curAct, ParameCallBack callBack) {
        _parameCallBack =callBack;
        Intent intent = new Intent(curAct, FansAddAct.class);
        ViewUtils.startActivity(intent,curAct);
    }

    private void initViews() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        btnTitleRight = (TextView) findViewById(R.id.btn_title_right);
        ViewUtils.setVisible(btnTitleRight);
        ViewUtils.setText(btnTitleRight, "保存");
        btnTitleRight.setOnClickListener(this);
        ViewUtils.setText(findViewById(R.id.txt_title), "新增线下粉丝");
        loadingview = (LoadingView) findViewById(R.id.view_loading);

        editName = (EditText) findViewById(R.id.edit_name);
        editPhone = (EditText) findViewById(R.id.edit_phone);
        editProductName = (EditText) findViewById(R.id.edit_product_name);
        editProductPrice = (EditText) findViewById(R.id.edit_product_price);
        edit_phone_brand = (EditText) findViewById(R.id.edit_phone_brand);

        editUserIntroduce = (EditText) findViewById(R.id.edit_user_introduce);

        editName.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp=s;
                if (!resetText) {
                    cursorPos = editName.getSelectionEnd();
                    // 这里用s.toString()而不直接用s是因为如果用s，
                    // 那么，inputAfterText和s在内存中指向的是同一个地址，s改变了，
                    // inputAfterText也就改变了，那么表情过滤就失败了
                    inputAfterText= s.toString();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!resetText) {
                    if (before != 0) {

                        return;

                    }

                    if (count >= 2) {//表情符号的字符长度最小为2
                        CharSequence input = s.subSequence(cursorPos, cursorPos + count);
                        if (StringUtil.containsEmoji(input.toString())) {
                            resetText = true;
                            ViewUtils.showToast("暂不支持输入表情哦");
                            //是表情符号就将文本还原为输入表情符号之前的内容
                            editName.setText(inputAfterText);
                            CharSequence text = editName.getText();
                            if (text instanceof Spannable) {
                                Spannable spanText = (Spannable) text;
                                Selection.setSelection(spanText, text.length());
                            }
                        }
                    }
                } else {
                    resetText = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editUserIntroduce.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp=s;
                if (!resetTextIntroduce) {
                    cursorPosIntroduce = editUserIntroduce.getSelectionEnd();
                    // 这里用s.toString()而不直接用s是因为如果用s，
                    // 那么，inputAfterText和s在内存中指向的是同一个地址，s改变了，
                    // inputAfterText也就改变了，那么表情过滤就失败了
                    inputAfterTextIntroduce= s.toString();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!resetTextIntroduce) {
                    if (before != 0) {

                        return;

                    }

                    if (count >= 2&&count<50) {//表情符号的字符长度最小为2
                        CharSequence input = s.subSequence(cursorPosIntroduce, cursorPosIntroduce + count);
                        if (StringUtil.containsEmoji(input.toString())) {
                            resetTextIntroduce = true;
                            ViewUtils.showToast("暂不支持输入表情哦");
                            //是表情符号就将文本还原为输入表情符号之前的内容
                            editUserIntroduce.setText(inputAfterTextIntroduce);
                            CharSequence text = editUserIntroduce.getText();
                            if (text instanceof Spannable) {
                                Spannable spanText = (Spannable) text;
                                Selection.setSelection(spanText, text.length());
                            }
                        }
                    }
                } else {
                    resetTextIntroduce = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (temp.length() > 50) {
                    s.delete(50, s.length());
                    editUserIntroduce.setText(s);
                    editUserIntroduce.setSelection(s.length());
                    ViewUtils.showToast("不能输入更多了!");
                }
                ((TextView) findViewById(R.id.txt_length)).setText(s.length() + "/50");
            }
        });

        ((RadioGroup) findViewById(R.id.radio_group)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.select_man:
                        gender = 1;
                        break;
                    case R.id.select_woman:
                        gender = 2;
                        break;
                }
            }
        });
        cbxStar1 = (CheckBox) findViewById(R.id.cbx_star1);
        cbxStar2 = (CheckBox) findViewById(R.id.cbx_star2);
        cbxStar3 = (CheckBox) findViewById(R.id.cbx_star3);
        cbxStar1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                } else {
                    cbxStar2.setChecked(false);
                    cbxStar3.setChecked(false);
                }
            }
        });
        cbxStar2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbxStar1.setChecked(true);
                } else {
                    cbxStar3.setChecked(false);
                }
            }
        });
        cbxStar3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbxStar1.setChecked(true);
                    cbxStar2.setChecked(true);
                } else {
                }
            }
        });

        cbxShopping = (CheckBox) findViewById(R.id.cbx_shopping);
        cbxShopping.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ViewUtils.setVisible(findViewById(R.id.view_product_name));
                    ViewUtils.setVisible(findViewById(R.id.view_product_price));
                } else {
                    ViewUtils.setGone(findViewById(R.id.view_product_name));
                    ViewUtils.setGone(findViewById(R.id.view_product_price));
                }
            }
        });

        labels = (FlowTagLayout) findViewById(R.id.flowlayout);
        labels.setVisibility(View.VISIBLE);
        mMobileTagAdapter = new TagAdapter(this);
        labels.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_MULTI);
        labels.setAdapter(mMobileTagAdapter);
        labels.setOnTagSelectListener(new FlowTagLayout.OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    selectLabels.clear();
                    for (int i : selectedList) {
                        selectLabels.add(((TagModel) (parent.getAdapter().getItem(i))).getId());
                    }
                }
            }
        });
    }

    private void submitAdd(final int buyIntention, int buyOffline, final String name, final String phone, String productName, String productPrice, String remarkContent, final String phoneBrand) {
        final AddOfflineFansRequest addOfflineFansRequest = new AddOfflineFansRequest(buyIntention, buyOffline, gender, selectLabels, name, phone, productName, productPrice, remarkContent,phoneBrand);
        if(stillSave==1){
            addOfflineFansRequest.addParams("stillSave",stillSave);
        }
        addOfflineFansRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                ViewUtils.showToast("保存成功");
                if(_callBack!=null){
                    _callBack.onCall();
                }
                FansInfo fansInfo=new FansInfo();
                int fansId=addOfflineFansRequest.getFansId();
                fansInfo.setFansId(fansId);
                fansInfo.setName(name);
                fansInfo.setPhone(phone);
                fansInfo.setPhoneModel(phoneBrand);
                fansInfo.setBuyIntention(buyIntention);
                if(_parameCallBack!=null){
                    _parameCallBack.onCall(fansInfo);
                }
                finish();
                UmengUtil.onEvent(FansAddAct.this,new UmengUtil().FANS_FANS_SAVE_SUC,null);
            }

            @Override
            public void onFail(int code) {
                UmengUtil.onEvent(FansAddAct.this,new UmengUtil().FANS_FANS_SAVE_FAIL,null);
                if(code==20017){
                    showHasSameDialog();
                }
            }
        });
        addOfflineFansRequest.start(this);
    }

    private void showHasSameDialog() {
        if(dialog == null){
            dialog = new Dialog(this, R.style.dialog);
        }
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog.show();
        dialog.setContentView(R.layout.dialog_save_confirm);
        ((TextView)dialog.findViewById(R.id.txt_title)).setText("已存在相同姓名和手机号码的线下粉丝，是否保存？");
        ((Button)dialog.findViewById(R.id.btn_done)).setText("不,我再看看");
        ((Button)dialog.findViewById(R.id.btn_cancel)).setText("继续保存");
        dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 保存组 返回前一页
                checkInfo(name,phone,productName,productPrice);
                if (shouldReturn)return;
                stillSave =1;
                submitAdd(buyIntention, buyOffline, name, phone, productName, productPrice, remarkContent,phone_brand);
                dialog.dismiss();

            }
        });
        dialog.findViewById(R.id.btn_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        name = editName.getText().toString().trim();
        phone = editPhone.getText().toString().trim();
        phone_brand = edit_phone_brand.getText().toString().trim();
        productName = editProductName.getText().toString().trim();
        productPrice = editProductPrice.getText().toString().trim();
        remarkContent = editUserIntroduce.getText().toString().trim();
        buyIntention = getBuyIntention();
        switch (view.getId()) {
            case R.id.btn_back:
                if(!TextUtils.isEmpty(name)||!TextUtils.isEmpty(phone)
                        ||!TextUtils.isEmpty(phone_brand)||!TextUtils.isEmpty(productName)
                        ||!TextUtils.isEmpty(productPrice)||!TextUtils.isEmpty(remarkContent)||!selectLabels.isEmpty()){
                    if(dialog == null){
                        dialog = new Dialog(this, R.style.dialog);
                    }
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    dialog.show();
                    dialog.setContentView(R.layout.dialog_save_confirm);
                    ((TextView)dialog.findViewById(R.id.txt_title)).setText("该粉丝还未保存,是否保存?");
                    ((TextView)dialog.findViewById(R.id.btn_done)).setText("保存");
                    dialog.findViewById(R.id.btn_done).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //TODO 保存组 返回前一页
                            dialog.dismiss();

                            checkInfo(name, phone, productName, productPrice);
                            if (shouldReturn)return;
                            submitAdd(buyIntention, buyOffline, name, phone, productName, productPrice, remarkContent, phone_brand);
                            
                        }
                    });
                    dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                }else {
                    finish();
                }
                break;
            case R.id.btn_title_right:

                checkInfo(name, phone, productName, productPrice);
                if (shouldReturn)return;
                submitAdd(buyIntention, buyOffline, name, phone, productName, productPrice, remarkContent, phone_brand);

                UmengUtil.onEvent(FansAddAct.this,new UmengUtil().FANS_FANS_SAVE_CLICK,null);
                break;
        }
    }

    /**
     * 提取获取购买意向的方法
     * @return
     */
    private int getBuyIntention() {
        int buyIntention = 0;
        if (cbxStar1.isChecked()) {
            buyIntention++;
        }
        if (cbxStar2.isChecked()) {
            buyIntention++;
        }
        if (cbxStar3.isChecked()) {
            buyIntention++;
        }
        return buyIntention;
    }

    /**
     * 保存之前校验信息是否有填写
     * @param name
     * @param phone
     * @param productName
     * @param productPrice
     */
    private void checkInfo(String name, String phone, String productName, String productPrice) {
        if (StringUtil.isNull(name)) {
            ViewUtils.showToast("请输入姓名");
            shouldReturn =true;
            return;
        }else {
            shouldReturn =false;
        }
        if (StringUtil.isNull(phone)) {
            ViewUtils.showToast("请输入手机号");
            shouldReturn =true;
            return;
        }else {
            shouldReturn =false;
        }
//        Pattern p = Pattern.compile(Constants.PHONE_PATTERN);
//        Matcher m = p.matcher(phone);
        //此处产品说不校验手机号码真实性
        if (phone.length() < 11) {
            ViewUtils.showToast("请输入正确的电话号码");
            shouldReturn =true;
            return;
        }else {
            shouldReturn =false;
        }

        buyOffline = 0;

        if (cbxShopping.isChecked()) {
            if (!StringUtil.isNull(productName) || !StringUtil.isNull(productPrice)) {
                if (StringUtil.isNull(productName)) {
                    ViewUtils.showToast("商品名称未填写");
                    shouldReturn =true;
                    return ;
                }else {
                    shouldReturn =false;
                }
                if (StringUtil.isNull(productPrice)) {
                    ViewUtils.showToast("商品价格未填写");
                    shouldReturn =true;
                    return ;
                }else {
                    shouldReturn =false;
                }
                buyOffline = 1;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        _parameCallBack=null;
    }
}
