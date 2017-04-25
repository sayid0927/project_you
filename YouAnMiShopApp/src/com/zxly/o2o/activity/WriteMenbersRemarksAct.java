package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.adapter.TagAdapter;
import com.zxly.o2o.application.Config;
import com.zxly.o2o.dialog.AddGroupDialog;
import com.zxly.o2o.model.MenberGroupModel;
import com.zxly.o2o.model.MenberInfoModel;
import com.zxly.o2o.model.TagModel;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.MenbersTagsRequest;
import com.zxly.o2o.request.SaveMenberRemarkRequest;
import com.zxly.o2o.request.ShopTagRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.NoDoubleClickListener;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.FlowTagLayout;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by hejun on 2016/8/31.
 * 写会员备注页面
 */
public class WriteMenbersRemarksAct extends BasicAct implements View.OnClickListener {
    private static ParameCallBack _callBack;
    private static ParameCallBack _callBackRefresh;
    private TextView txt_title;
    private TextView btn_title_right;
    private TextView btn_back;
    private EditText edit_name;
    private ImageView btn_add;
    private TextView tv_group;
    private TextView txt_nick_name;
    private ImageView img_men_select;
    private ImageView img_women_select;
    private TextView text_phone;
    private CheckBox cbx_shopping;
    private EditText edit_product_name;
    private EditText edit_product_price;
    private EditText edit_user_introduce;
    private FlowTagLayout labels;
    private String userName;
    private String nickName;
    private int gender;
    private String phoneNum;
    private CheckBox cbxShopping;
    //到店购买  默认为否
    private int buyOffLine;
    private TagAdapter mTagAdapter;
    private long menberId;
    private List<TagModel> tagList;
    private List<MenberGroupModel> diffGroup;
    private ArrayList<String> presentGroup=new ArrayList<String>();
    private StringBuilder groupStr;
    //点击+ 后新加入的会员组
    private List<MenberGroupModel> chooseGroup=new ArrayList<MenberGroupModel>();
    private ArrayList<String> chooseTagIds;
    private List<String> chooseGroupIds=new ArrayList<String>();
    private List<MenberGroupModel> menberGroupModels=new ArrayList<MenberGroupModel>();
    private List<TagModel> tagModelList=new ArrayList<TagModel>();

    private int cursorPosIntroduce;
    private String inputAfterTextIntroduce;
    private boolean resetTextIntroduce;
    //输入表情前的光标位置
    private int cursorPos;
    //输入表情前EditText中的文本
    private String inputAfterText;
    //是否重置了EditText的内容
    private boolean resetText;
    private List<String> chooseGroupName;
    private ArrayList<String> chooseTagStr;
    private boolean isCheck;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.win_write_remarks);
        getIntentData();
        initViews();

    }
    private void getIntentData() {
        userName = getIntent().getStringExtra("userName");
        nickName = getIntent().getStringExtra("nickName");
        gender = getIntent().getIntExtra("gender", 0);
        phoneNum = getIntent().getStringExtra("phoneNum");
        menberId =getIntent().getLongExtra("id",0);
    }

    private void initViews() {
        chooseTagStr =new ArrayList<String>();
        //标题
        txt_title = (TextView) findViewById(R.id.txt_title);
        //标题右边保存
        btn_title_right = (TextView) findViewById(R.id.btn_title_right);
        //返回键
        btn_back = (TextView) findViewById(R.id.btn_back);
        //姓名
        edit_name = (EditText) findViewById(R.id.edit_name);
        //增加分组
        btn_add = (ImageView) findViewById(R.id.btn_add);
        //分组
        tv_group = (TextView) findViewById(R.id.tv_group);
        //昵称
        txt_nick_name = (TextView) findViewById(R.id.txt_nick_name);
        //用户为男性显示的图片
        img_men_select = (ImageView) findViewById(R.id.img_men_select);
        //用户为女性显示的图片
        img_women_select = (ImageView) findViewById(R.id.img_women_select);
        //标签容器
        labels = (FlowTagLayout) findViewById(R.id.flowlayout);
        labels.setVisibility(View.VISIBLE);
        //手机号码
        text_phone = (TextView) findViewById(R.id.text_phone);
        //到店购买
        cbx_shopping = (CheckBox) findViewById(R.id.cbx_shopping);
        //商品名称
        edit_product_name = (EditText) findViewById(R.id.edit_product_name);
        //商品价格
        edit_product_price = (EditText) findViewById(R.id.edit_product_price);
        //用户描述内容
        edit_user_introduce = (EditText) findViewById(R.id.edit_user_introduce);
        chooseTagIds =new ArrayList<String>();
        initDatas();
        getAllLables();
        txt_title.setText("写备注");
        btn_title_right.setVisibility(View.VISIBLE);
        btn_title_right.setText("保存");
        btn_back.setOnClickListener(noDoubleClickListener);
        btn_title_right.setOnClickListener(noDoubleClickListener);
        btn_add.setOnClickListener(this);

        edit_name.setText(userName);
        txt_nick_name.setText(nickName);
        text_phone.setText(phoneNum);
        //用户为男性
        if(gender==2){
            img_men_select.setImageResource(R.drawable.icon_check_normal);
            img_women_select.setImageResource(R.drawable.icon_check_dark);
        }else{
            img_men_select.setImageResource(R.drawable.icon_check_dark);
            img_women_select.setImageResource(R.drawable.icon_check_normal);
        }

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

        edit_name.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp=s;
                if (!resetText) {
                    cursorPos = edit_name.getSelectionEnd();
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
                            edit_name.setText(inputAfterText);
                            CharSequence text = edit_name.getText();
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

        mTagAdapter = new TagAdapter(this);
        labels.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_MULTI);
        labels.setAdapter(mTagAdapter);
        labels.setOnTagSelectListener(new FlowTagLayout.OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                chooseTagIds.clear();
                chooseTagStr.clear();
                if (selectedList != null && selectedList.size() > 0) {
                    for (int i : selectedList) {
                        chooseTagIds.add(((TagModel)(parent.getAdapter().getItem(i))).getId()+"");
                        chooseTagStr.add(((TagModel)(parent.getAdapter().getItem(i))).getName());
                    }
                }
            }
        });

        //限制商品价格输入为小数点前四位，小数点后两位
        edit_product_price.addTextChangedListener(new TextWatcher() {
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
     * 获取初始数据并显示
     */
    private void initDatas() {
        final MenbersTagsRequest menbersTagsRequest = new MenbersTagsRequest(menberId);
        menbersTagsRequest.start();
        menbersTagsRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                //TODO 获取到会员组和标签后初始化显示
                //初始所在的会员组
                List<String> groupIds = menbersTagsRequest.getGroupIds();
                //初始所选择的标签
                List<String> labelsIds = menbersTagsRequest.getLabelsIds();
                putData(groupIds,labelsIds);
            }

            @Override
            public void onFail(int code) {

            }
        });

    }

    /**
     * 设置初始化时 该会员所在的分组及所选的标签
     * @param groupIds
     * @param labelsIds
     */
    private void putData(List<String> groupIds, List<String> labelsIds) {
        //所有的会员分组
        List<MenberGroupModel> groupList = Config.groupList;
        //所有的标签集
        tagList = Config.tagList;
        if(!tagList.isEmpty()){
            for (int i = 0; i < tagList.size(); i++) {
                tagList.get(i).setIschoose(false);
            }
        }
        String[] groupId = new String[groupIds.size()];
        groupIds.toArray(groupId);

        //初始状态所在的组名
        presentGroup =new ArrayList<String>();
        //能添加的会员组
        diffGroup = new ArrayList<MenberGroupModel>();
        if(!groupList.isEmpty()){
            for (int i = 0; i < groupList.size(); i++) {
                if(!groupIds.contains(groupList.get(i).getId()+"")){
                    diffGroup.add(groupList.get(i));
                }else{
                    presentGroup.add(groupList.get(i).getName());
                    chooseGroupIds.add(groupList.get(i).getId()+"");
                }
            }
        }



        //将已经在的组list转成以;结尾的字符串并显示
        groupStr = new StringBuilder();
        for (int i = 0; i < presentGroup.size(); i++) {
            if (i > 0) {
                groupStr.append('；');
            }
            groupStr.append(presentGroup.get(i));
        }
        tv_group.setText(groupStr);
        if(labelsIds==null){
            labelsIds=new ArrayList<String>();
        }
        //选出选中的标签
            String[] labelId = new String[labelsIds.size()];
            labelsIds.toArray(labelId);
            chooseTagStr.clear();
            if(!tagList.isEmpty()){
                for (int i = 0; i < tagList.size(); i++) {
                    if(labelsIds.contains(tagList.get(i).getId()+"")){
                        //选中的tag
                        tagList.get(i).setIschoose(true);
                        chooseTagStr.add(tagList.get(i).getName());
                        chooseTagIds.add(tagList.get(i).getId()+"");
                    }
                }
            }
            initMobileData();
    }

    private void initMobileData() {
        List<TagModel> dataSource = new ArrayList<TagModel>();
        dataSource.addAll(tagList);
        mTagAdapter.onlyAddAll(tagList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add:
                //需要过滤掉  新会员  组
                for (int i = 0; i < diffGroup.size(); i++) {
                    if(diffGroup.get(i).getName().contains("新会员")){
                        diffGroup.remove(i);
                    }
                }
                final AddGroupDialog addGroupDialog = new AddGroupDialog(diffGroup,chooseGroup);
                addGroupDialog.show(new CallBack() {
                    @Override
                    public void onCall() {
                        StringBuilder addGroupStr = new StringBuilder();
                        //选择新增的分组名集合
                        chooseGroup = addGroupDialog.getChooseGroup();
                        if(chooseGroup.size()>0){
                            for (int i = 0; i < chooseGroup.size(); i++) {
                                if (i > 0) {
                                    addGroupStr.append('；');
                                }

                                addGroupStr.append(chooseGroup.get(i).getName());
                                chooseGroupIds.add(chooseGroup.get(i).getId()+"");
                            }
                            tv_group.setText(groupStr+"；"+addGroupStr);
                        }else{
                            tv_group.setText(groupStr);
                        }

                    }
                });
                break;
        }
    }

    private void saveRemarks() {
        final String remarkName=edit_name.getText().toString().trim();

        String productName = edit_product_name.getText().toString().trim();
        String productPrice = edit_product_price.getText().toString().trim();
        String remarkContent=edit_user_introduce.getText().toString().trim();
        if(cbxShopping.isChecked()){
            if (!StringUtil.isNull(productName) || !StringUtil.isNull(productPrice)) {
                if(TextUtils.isEmpty(productName)){
                    ViewUtils.showToast("商品名称不能为空");
                    return;
                }
                if(TextUtils.isEmpty(productPrice)){
                    ViewUtils.showToast("商品价格不能为空");
                    return;
                }
                if(cbxShopping.isChecked()&&!TextUtils.isEmpty(productName)&&!TextUtils.isEmpty(productPrice)){
                    buyOffLine=1;
                }
            }
        }

        SaveMenberRemarkRequest saveMenberRemarkRequest = new SaveMenberRemarkRequest(buyOffLine, menberId, chooseTagIds, chooseGroupIds, productName, productPrice, remarkContent, remarkName, Account.user.getShopId());
        saveMenberRemarkRequest.start();
        saveMenberRemarkRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                ViewUtils.showToast("保存成功");
                MenberInfoModel menberDtail=new MenberInfoModel();
                menberDtail.setRemarkName(remarkName);
                menberDtail.setLabels(chooseTagStr);
                menberDtail.setId(menberId);
                menberDtail.setMobilePhone(phoneNum);
                menberDtail.setNickname(nickName);
                //保存成功后将 用户所填的数据传给上一个页面
                if(_callBackRefresh!=null){
                    _callBackRefresh.onCall(menberDtail);
                }
                //客多多页面刷新改会员所在组
                if(_callBack!=null){
                    if(chooseGroup!=null){
                        menberDtail.setLocationGroupName(chooseGroup);
                    }
                    _callBack.onCall(menberDtail);
                }
                finish();
            }

            @Override
            public void onFail(int code) {
                ViewUtils.showToast("保存失败");
            }
        });

    }

    /**
     *
     * @param curAct 当前页面
     * @param userName 备注名
     * @param nickName 昵称
     * @param gender 性别
     * @param phoneNum 手机号码
     */
    public static  void start(Activity curAct, String userName,String nickName,int gender,String phoneNum,long menberId)
    {
        Intent intent = new Intent();
        intent.putExtra("userName",userName);
        intent.putExtra("nickName",nickName);
        intent.putExtra("gender",gender);
        intent.putExtra("phoneNum",phoneNum);
        intent.putExtra("id",menberId);
        intent.setClass(curAct, WriteMenbersRemarksAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

    /**
     *
     * @param curAct 当前页面
     * @param userName 备注名
     * @param nickName 昵称
     * @param gender 性别
     * @param phoneNum 手机号码
     */
    public static  void start(Activity curAct, String userName,String nickName,int gender,String phoneNum,long menberId,ParameCallBack callBack,ParameCallBack callBackRefresh)
    {
        _callBackRefresh =callBackRefresh;
        _callBack =callBack;
        Intent intent = new Intent();
        intent.putExtra("userName",userName);
        intent.putExtra("nickName",nickName);
        intent.putExtra("gender",gender);
        intent.putExtra("phoneNum",phoneNum);
        intent.putExtra("id",menberId);
        intent.setClass(curAct, WriteMenbersRemarksAct.class);
        ViewUtils.startActivityForResult(intent, curAct,100);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        _callBack=null;
        _callBackRefresh=null;
    }

    NoDoubleClickListener noDoubleClickListener=new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()){
                case R.id.btn_title_right:
                    saveRemarks();
                    break;
                case R.id.btn_back:
                    finish();
                    break;
            }
        }
    };
}
