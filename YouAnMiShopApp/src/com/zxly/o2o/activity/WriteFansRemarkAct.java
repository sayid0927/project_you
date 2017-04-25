package com.zxly.o2o.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zxly.o2o.adapter.TagAdapter;
import com.zxly.o2o.application.Config;
import com.zxly.o2o.model.FansInfo;
import com.zxly.o2o.model.TagModel;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.GetFansDetailRequest;
import com.zxly.o2o.request.SaveFansRemarkRequest;
import com.zxly.o2o.request.ShopTagRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.NoDoubleClickListener;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.FlowTagLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejun on 2016/9/28.
 * 粉丝写备注
 */
public class WriteFansRemarkAct extends BasicAct {
    public static int phone_length = 11;
    private static ParameCallBack _callBack;
    private static ParameCallBack _fansDetailCallBack;
    private TextView btnTitleRight;
    private EditText editName;
    private EditText editPhone;
    private EditText editProductName;
    private EditText editProductPrice;
    private int gender=1;
    private CheckBox cbxStar1;
    private CheckBox cbxStar2;
    private CheckBox cbxStar3;
    private CheckBox cbxShopping;
    private FlowTagLayout labels;
    private TagAdapter mMobileTagAdapter;
    private ArrayList<Long> selectLabels = new ArrayList<Long>();
    private List<TagModel> memberLabelList;
    private long fansId;
    private FansInfo fansDtail;
    private List<TagModel> tagList;
    private ArrayList<String> chooseTagIds;
    private RadioButton select_man;
    private RadioButton select_woman;
    private int buyIntention;
    //输入表情前的光标位置
    private int cursorPos;
    //输入表情前EditText中的文本
    private String inputAfterText;
    //是否重置了EditText的内容
    private boolean resetText;
    private int cursorPosIntroduce;
    private String inputAfterTextIntroduce;
    private boolean resetTextIntroduce;
    private EditText edit_user_introduce;
    private int fansType;
    private EditText edit_phone_brand;
    private Dialog dialog;
    private String name="";
    private String phone="";
    private String productName="";
    private String productPrice="";
    private String phoneModel="";
    private String remarkContent="";
    private int buyOffline;
    private boolean infoRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_writefans_remark);
        fansId = getIntent().getLongExtra("fansId", 0);
        fansType = getIntent().getIntExtra("fansType", 0);//0  普通粉丝 1线下粉丝  字段作用：当粉丝为线下录入的时候 在填写备注的时候姓名与手机号是必填的
        findViewById(R.id.btn_back).setOnClickListener(noDoubleClickListener);
        btnTitleRight = (TextView) findViewById(R.id.btn_title_right);
        ViewUtils.setVisible(btnTitleRight);
        ViewUtils.setText(btnTitleRight, "保存");
        btnTitleRight.setOnClickListener(noDoubleClickListener);
        btnTitleRight.setEnabled(false);
        ViewUtils.setText(findViewById(R.id.txt_title), "写备注");
        getAllLables();
        findViews();
        loadData();
    }

    private void findViews() {
        editName = (EditText) findViewById(R.id.edit_name);
        editPhone = (EditText) findViewById(R.id.edit_phone);
        editProductName = (EditText) findViewById(R.id.edit_product_name);
        editProductPrice = (EditText) findViewById(R.id.edit_product_price);
        edit_phone_brand = (EditText) findViewById(R.id.edit_phone_brand);
        select_man = (RadioButton) findViewById(R.id.select_man);
        select_woman = (RadioButton) findViewById(R.id.select_woman);
        edit_user_introduce = (EditText) findViewById(R.id.edit_user_introduce);
    }

    private void getAllLables() {
        final ShopTagRequest shopTagRequest = new ShopTagRequest();
        shopTagRequest.start();
        shopTagRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                if(Config.tagList!=null){
                    Config.tagList.clear();
                }
                Config.tagList = shopTagRequest.getTagModelList();
            }

            @Override
            public void onFail(int code) {

            }
        });
    }


    /**
     * 会员基本信息获取
     */
    private void loadData() {
        final GetFansDetailRequest getMenberDetailRequest = new GetFansDetailRequest(fansId);
        getMenberDetailRequest.start();
        getMenberDetailRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                fansDtail = getMenberDetailRequest.getFansDtail();
                fansType=fansDtail.getIsOffline();
                btnTitleRight.setEnabled(true);
                fillData(fansDtail);
            }

            @Override
            public void onFail(int code) {
                btnTitleRight.setEnabled(true);
            }
        });
    }

    private void fillData(FansInfo fansDtail) {
        initViews(fansDtail);
        //初始化显示
        editName.setText(fansDtail.getName());
        if(fansDtail.getGender()==2){
            select_woman.setChecked(true);
        }else if(fansDtail.getGender()==1){
            select_man.setChecked(true);
        }
        editPhone.setText(fansDtail.getPhone());
        edit_phone_brand.setText(fansDtail.getPhoneModel());
        //所有的标签集
        tagList = Config.tagList;
        for (int i = 0; i < tagList.size(); i++) {
            tagList.get(i).setIschoose(false);
        }
        List<String> labels = fansDtail.getLabels();

        switch (fansDtail.getBuyIntention()){
            case 0:
                buyIntention=0;
                cbxStar1.setChecked(false);
                cbxStar2.setChecked(false);
                cbxStar3.setChecked(false);
                break;
            case 1:
                buyIntention=1;
                cbxStar1.setChecked(true);
                cbxStar2.setChecked(false);
                cbxStar3.setChecked(false);
                break;
            case 2:
                buyIntention=2;
                cbxStar1.setChecked(true);
                cbxStar2.setChecked(true);
                cbxStar3.setChecked(false);
                break;
            case 3:
                buyIntention=3;
                cbxStar1.setChecked(true);
                cbxStar2.setChecked(true);
                cbxStar3.setChecked(true);
                break;
        }

        //选出选中的标签
        String[] labelId = new String[labels.size()];
        labels.toArray(labelId);
        if(!tagList.isEmpty()){
            for (int i = 0; i < tagList.size(); i++) {
                if(labels.contains(tagList.get(i).getId()+"")){
                    //选中的tag
                    tagList.get(i).setIschoose(true);
                    chooseTagIds.add(tagList.get(i).getId()+"");
                }
            }
        }
        List<TagModel> dataSource = new ArrayList<TagModel>();
        dataSource.addAll(tagList);
        mMobileTagAdapter.onlyAddAll(tagList);
    }

    private void initViews(FansInfo fansDtail) {
        findViewById(R.id.layout_phonebrand).setVisibility(fansDtail.getIsOffline()==0?View.GONE:View.VISIBLE);
        edit_user_introduce.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp=s;
                if (!resetTextIntroduce) {
                    cursorPosIntroduce = edit_user_introduce.getSelectionEnd();
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
                            edit_user_introduce.setText(inputAfterTextIntroduce);
                            CharSequence text = edit_user_introduce.getText();
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
                    edit_user_introduce.setText(s);
                    edit_user_introduce.setSelection(s.length());
                    ViewUtils.showToast("不能输入更多了!");
                }
                ((TextView) findViewById(R.id.txt_length)).setText( s.length() + "/50");
            }
        });

        //限制商品价格输入为小数点前四位，小数点后两位
        editProductPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String temp = s.toString();
                int posDot = temp.indexOf(".");
                if (posDot <=0){
                    if(temp.length()<=4){
                        return;
                    }else{
                        s.delete(4, 5);
                        return;
                    }
                }
                if (temp.length() - posDot - 1 > 2)
                {
//                    ViewUtils.showToast("小数点后只能输入两位哦");
                    s.delete(posDot + 3, posDot + 4);
                }

            }
        });

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
        chooseTagIds =new ArrayList<String>();
        labels.setOnTagSelectListener(new FlowTagLayout.OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {

                chooseTagIds.clear();
                if (selectedList != null && selectedList.size() > 0) {
                    for (int i : selectedList) {
                        chooseTagIds.add(((TagModel)(parent.getAdapter().getItem(i))).getId()+"");
                    }
                }
            }
        });
    }

    private void getRemarkInfo() {
        buyIntention=0;
        name = editName.getText().toString().trim();
        phone = editPhone.getText().toString().trim();
        productName = editProductName.getText().toString().trim();
        productPrice = editProductPrice.getText().toString().trim();
        //线下粉丝 写备注时有手机品牌这一项
        phoneModel = edit_phone_brand.getText().toString().trim();
        remarkContent = edit_user_introduce.getText().toString().trim();
        if (cbxStar1.isChecked()) {
            buyIntention++;
        }
        if (cbxStar2.isChecked()) {
            buyIntention++;
        }
        if (cbxStar3.isChecked()) {
            buyIntention++;
        }

        if(cbxShopping.isChecked()&&!StringUtil.isNull(productName) && !StringUtil.isNull(productPrice)){
                    buyOffline = 1;
        }else {
            buyOffline = 0;
        }
    }

    private boolean checkInfoRight() {
        buyOffline = 0;
        infoRight =true;
        //线下粉丝 写备注时有手机品牌这一项
//        if (cbxShopping.isChecked()) {
//            buyOffline = 1;
//            if (StringUtil.isNull(productName)) {
//                ViewUtils.showToast("商品名称未填写");
//                infoRight= false;
//            }
//            if (StringUtil.isNull(productPrice)) {
//                ViewUtils.showToast("商品价格未填写");
//                infoRight= false;
//            }
//        }

        if (cbxShopping.isChecked()) {
            if (!StringUtil.isNull(productName) || !StringUtil.isNull(productPrice)) {
                if (StringUtil.isNull(productName)) {
                    ViewUtils.showToast("商品名称未填写");
                    infoRight =false;
                }
                if (StringUtil.isNull(productPrice)) {
                    ViewUtils.showToast("商品价格未填写");
                    infoRight =false;
                }
                if(!StringUtil.isNull(productName) && !StringUtil.isNull(productPrice)){
                    buyOffline = 1;
                }
            }
        }


        if(fansType==1){
            if (StringUtil.isNull(name)) {
                ViewUtils.showToast("姓名未填写");
                infoRight= false;
            }
            if (StringUtil.isNull(phone)) {
                ViewUtils.showToast("手机号未填写");
                infoRight=false;
            }
        }

        if (!StringUtil.isNull(phone)) {
//            Pattern p = Pattern.compile(Constants.PHONE_PATTERN);
//            Matcher m = p.matcher(phone);
            if (phone.length() < 11 ) {
                ViewUtils.showToast("请输入正确的电话号码");
                infoRight=false;
            }
        }
        return infoRight;
    }

    private void saveRemarks(final int buyIntention, int buyOffline, final long fansId, final int gender, final ArrayList<String> chooseTagIds,
                             final String name, final String phone, String productName, String productPrice, final String remarkContent, final String phoneModel) {
        if(checkInfoRight()){
            SaveFansRemarkRequest saveFansRemarkRequest = new SaveFansRemarkRequest(buyIntention, buyOffline, fansId,
                    gender, chooseTagIds, name, phone, productName, productPrice, remarkContent);
            if(fansDtail.getIsOffline()==1){//线下粉丝  需要传品牌（不是必填）
                saveFansRemarkRequest.addParams("phoneModel",phoneModel);
            }
            saveFansRemarkRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                @Override
                public void onOK() {
                    ViewUtils.showToast("保存成功");
                    //不是线下粉丝的回调
                    if(_callBack!=null&&fansDtail.getIsOffline()!=1){
                        FansInfo childInfo=new FansInfo();
                        childInfo.setFansId((int) fansId);
                        if(!TextUtils.isEmpty(phone)){
                            childInfo.setPhone(phone);
                        }
                        if(!TextUtils.isEmpty(name)){
                            childInfo.setRemarkName(name);
                        }
                        childInfo.setBuyIntention(buyIntention);
                        _callBack.onCall(childInfo);
                    }
                    if(_fansDetailCallBack!=null){
                        FansInfo childInfo=new FansInfo();
                        childInfo.setFansId((int) fansId);
                        childInfo.setBuyIntention(buyIntention);
                        childInfo.setRemarkContent(remarkContent);
                        if(!TextUtils.isEmpty(phone)){
                            childInfo.setPhone(phone);
                        }
                        if(!TextUtils.isEmpty(name)){
                            childInfo.setRemarkName(name);
                        }
                        if(gender!=0){
                            childInfo.setGender(gender);
                        }
                        if(!TextUtils.isEmpty(phoneModel)){
                            childInfo.setPhoneModel(phoneModel);
                        }
                        if(!chooseTagIds.isEmpty()){
                            childInfo.setLabels(chooseTagIds);
                        }
                        _fansDetailCallBack.onCall(childInfo);
                    }
                    finish();
                }

                @Override
                public void onFail(int code) {

                }
            });
            saveFansRemarkRequest.start();
        }
    }

    public static void start(Activity curAct, long fansId,int fansType) {
        Intent intent = new Intent(curAct, WriteFansRemarkAct.class);
        intent.putExtra("fansId", fansId);
        //1为普通粉丝   2 为线下粉丝
        intent.putExtra("fansType",fansType);
        ViewUtils.startActivity(intent, curAct);
    }
    public static void start(Activity curAct, long fansId, int fansType, ParameCallBack callBack,ParameCallBack fansDetailCallBack) {
        _callBack =callBack;
        _fansDetailCallBack =fansDetailCallBack;
        Intent intent = new Intent(curAct, WriteFansRemarkAct.class);
        intent.putExtra("fansId", fansId);
        //1为普通粉丝   2 为线下粉丝
        intent.putExtra("fansType",fansType);
        ViewUtils.startActivity(intent, curAct);
    }

    NoDoubleClickListener noDoubleClickListener=new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.btn_back:
                    getRemarkInfo();
                    if(!name.equals(fansDtail.getName())||!phone.equals(fansDtail.getPhone())||chooseTagIds.size()!=fansDtail.getLabels().size()
                            ||!TextUtils.isEmpty(remarkContent)||!TextUtils.isEmpty(productName)||!TextUtils.isEmpty(productPrice)||gender!=1){
                        if(dialog == null){
                            dialog = new Dialog(WriteFansRemarkAct.this, R.style.dialog);
                        }
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        if(!isFinishing()){
                            dialog.show();
                        }
                        dialog.setContentView(R.layout.dialog_save_confirm);
                        ((TextView) dialog.findViewById(R.id.txt_title)).setText("该粉丝备注还未保存,是否保存?");
                        ((TextView)dialog.findViewById(R.id.btn_done)).setText("保存");
                        dialog.findViewById(R.id.btn_done).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //TODO 保存组 返回前一页
                                dialog.dismiss();
                                saveRemarks(buyIntention,buyOffline,fansId,gender,chooseTagIds,name,phone,productName,productPrice,remarkContent,phoneModel);

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
                    getRemarkInfo();
                    if(editName.getText().toString().length()>0&&TextUtils.isEmpty(editName.getText().toString().trim())){
                        ViewUtils.showToast("姓名不能全为空");
                        return;
                    }
                    saveRemarks(buyIntention,buyOffline,fansId,gender,chooseTagIds,name,phone,productName,productPrice,remarkContent,phoneModel);
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        _callBack=null;
        _fansDetailCallBack=null;
    }
}
