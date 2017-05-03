package com.zxly.o2o.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.easeui.model.IMUserInfoVO;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.application.Config;
import com.zxly.o2o.cropView.Crop;
import com.zxly.o2o.dialog.ChangeBirthDialog;
import com.zxly.o2o.dialog.GetPictureDialog;
import com.zxly.o2o.dialog.ShareDialog;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.FileUploadRequest;
import com.zxly.o2o.request.IMGetContactListRequest;
import com.zxly.o2o.request.IMGetUserDetailInfoRequest;
import com.zxly.o2o.request.PersonalUserEditRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.DesityUtil;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.PicTools;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.UmengUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.CircleImageView;
import com.zxly.o2o.view.ObservableScrollView;
import com.zxly.o2o.view.OnScrollChangedCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fengrongjian 2015-12-10
 * @description 个人信息页面
 */
public class PersonalHomepageAct extends BasicAct implements
        View.OnClickListener, OnScrollChangedCallback {
    private Context context;
    private View viewTitle;
    private ObservableScrollView scrollView;
    private CircleImageView imgUserHead;
    private TextView txtNickName, txtSignature;
    private TextView txtNick, txtGender, txtBirthday, txtCity;
    private Dialog dialog;
    private PersonalUserEditRequest personalUserEditRequest;
    private FileUploadRequest fileUploadRequest;
    private ShareDialog shareDialog;
    private String curBirth, curSignature, curNick;
    private byte curGender;
    private int scrollHeight, screenHeight;
    private IMGetUserDetailInfoRequest imGetUserDetailInfoRequest;
    private IMUserInfoVO userInfoVO = new IMUserInfoVO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_personal_homepage);
        context = this;
        loadPersonalInfo();
        UmengUtil.onEvent(PersonalHomepageAct.this,new UmengUtil().INFO_ENTER,null);
    }

    private void loadPersonalInfo() {
        if (Account.user != null) {
            if (imGetUserDetailInfoRequest == null) {
                imGetUserDetailInfoRequest = new IMGetUserDetailInfoRequest(Account.user.getId());
                imGetUserDetailInfoRequest
                        .setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                            @Override
                            public void onOK() {
                                if (imGetUserDetailInfoRequest.imUserInfoVO != null) {
                                    //之前版本是只在登录时才未用户基本信息赋值一次 现改为每次进入该页面刷新数据
                                    updateUserInfo(imGetUserDetailInfoRequest.imUserInfoVO);
                                }
                            }

                            @Override
                            public void onFail(int code) {

                            }
                        });
            }
            imGetUserDetailInfoRequest.start();
        }
    }

    private void updateUserInfo(IMUserInfoVO imUserInfoVO) {
        String signature = imUserInfoVO.getSignature();
        String nickname = imUserInfoVO.getNickname();
        byte gender = imUserInfoVO.getGender();
        Long birthday = imUserInfoVO.getBirthday();
        String provinceName = imUserInfoVO.getProvinceName();
        String cityName = imUserInfoVO.getCityName();
        String thumHeadUrl = imUserInfoVO.getThumHeadUrl();
        if (!TextUtils.isEmpty(signature)) {
            Account.user.setSignature(signature);
        }
        if (!TextUtils.isEmpty(nickname)) {
            Account.user.setNickname(nickname);
        }
        Account.user.setGender(gender);
        Account.user.setBirthday(birthday);
        if (!TextUtils.isEmpty(provinceName)) {
            Account.user.setProvinceName(provinceName);
        }
        if (!TextUtils.isEmpty(cityName)) {
            Account.user.setCityName(cityName);
        }
        if (!TextUtils.isEmpty(thumHeadUrl)) {
            Account.user.setThumHeadUrl(thumHeadUrl);
        }
        initViews();
    }

    public static void start(Activity curAct) {
        Intent intent = new Intent(curAct, PersonalHomepageAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

    private void initViews() {
        dialog = new Dialog(this, R.style.dialog);
        findViewById(R.id.btn_back).setOnClickListener(this);
        ViewUtils.setText(findViewById(R.id.txt_title), "个人信息");
        scrollView = (ObservableScrollView) findViewById(R.id.scroll_view);
        scrollView.setOnScrollChangedCallback(this);

        findViewById(R.id.btn_user_head).setOnClickListener(this);
        findViewById(R.id.layout_phonenumber).setOnClickListener(this);
        imgUserHead = (CircleImageView) findViewById(R.id.img_user_head);
        ViewUtils.setGone(findViewById(R.id.view_register_time));

        txtNickName = (TextView) findViewById(R.id.txt_user_name);
        ViewUtils.setText(txtNickName, Account.user.getUserName());
        findViewById(R.id.btn_signature).setOnClickListener(this);
        txtSignature = (TextView) findViewById(R.id.txt_user_signature);

        findViewById(R.id.btn_nick).setOnClickListener(this);
        findViewById(R.id.btn_gender).setOnClickListener(this);
        findViewById(R.id.btn_birthday).setOnClickListener(this);
        findViewById(R.id.btn_city).setOnClickListener(this);
        txtNick = (TextView) findViewById(R.id.txt_nick);
        txtGender = (TextView) findViewById(R.id.txt_gender);
        txtBirthday = (TextView) findViewById(R.id.txt_birthday);
        txtCity = (TextView) findViewById(R.id.txt_city);
        setUserInfo();
    }

    private void setUserInfo() {
        setUserHead();
        if (!StringUtil.isNull(Account.user.getSignature())) {
            curSignature = Account.user.getSignature();
            txtSignature.setText(curSignature);
        } else {
            curSignature = "";
            txtSignature.setText("编辑个性签名");
        }
        curNick = Account.user.getNickname();
        txtNick.setText(curNick);
        curGender = Account.user.getGender();
        if (curGender == 0) {
            curGender = 1;
        }
        txtGender.setText(curGender == 1 ? "男" : "女");
        if (Account.user.getBirthday() != 0) {
            curBirth = StringUtil.getDateByMillis(
                    Account.user.getBirthday());
        } else {
            curBirth = "1900-01-01";
        }
        txtBirthday.setText(curBirth);
        String provinceName = Account.user.getProvinceName();
        String cityName = Account.user.getCityName();
        if (cityName != null && !cityName.equals(provinceName)) {
            ViewUtils.setText(txtCity, provinceName + "  " + cityName);
        } else {
            ViewUtils.setText(txtCity, provinceName);
        }
    }

    private void setUserHead() {
        String thumHeadUrl = Account.user.getThumHeadUrl();
        if (StringUtil.isNull(thumHeadUrl)) {
            imgUserHead.setImageResource(R.drawable.default_head_small);
        } else {
            imgUserHead.setImageUrl(thumHeadUrl, R.drawable.default_head_small);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CAMERA && resultCode == RESULT_OK) {
            beginCrop(Uri.fromFile(PicTools.getOutputPhotoFile()));
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }

    private void beginCrop(Uri source) {
        Uri outputUri = Uri.fromFile(PicTools.getOutputPhotoFile());
        new Crop(source).output(outputUri).asSquare().withMaxSize(800, 800).start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            File tmpFile = PicTools.getOutputPhotoFile();
            postFile(tmpFile);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void postFile(File file) {
        Map<String, Object> params = new HashMap<String, Object>();
        fileUploadRequest = new FileUploadRequest(file, params,
                "shop/photo/edit", mMainHandler);
        fileUploadRequest.startUpload();
    }

    @Override
    protected void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case Constants.GET_PIC_FROM_CELLPHONE:
                Crop.pickImage(this);
                break;
            case Constants.GET_PIC_FROM_CAMERA:
                Crop.cameraImage(this);
                break;
            case Constants.MSG_SUCCEED:
                ViewUtils.showToast("上传成功!");
                String data = (String) msg.obj;
                try {
                    JSONObject object = new JSONObject(data);
                    String url = object.getString("thumHeadUrl");
                    String originUrl = object.getString("originHeadUrl");

                    IMUserInfoVO user = Account.readLoginUser(context);
                    user.setThumHeadUrl(url);
                    user.setOriginHeadUrl(originUrl);
                    Account.user = user;
                    Account.saveLoginUser(context, user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setUserHead();
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                UmengUtil.onEvent(PersonalHomepageAct.this,new UmengUtil().INFO_BACK_CLICK,null);
                break;
            case R.id.btn_user_head:
                new GetPictureDialog(false).show(mMainHandler);
                UmengUtil.onEvent(PersonalHomepageAct.this,new UmengUtil().INFO_AVATAR_CLICK,null);

                break;
            case R.id.btn_signature:
                createSignatureDialog();
                UmengUtil.onEvent(PersonalHomepageAct.this,new UmengUtil().INFO_SIGN_CLICK,null);
                break;
            case R.id.btn_nick:
                createNickDialog();
                UmengUtil.onEvent(PersonalHomepageAct.this,new UmengUtil().INFO_NICKNAME_CLICK,null);
                break;
            case R.id.btn_gender:
                createSexDialog();
                UmengUtil.onEvent(PersonalHomepageAct.this,new UmengUtil().INFO_SEX_CLICK,null);
                break;
            case R.id.btn_birthday:
                changeBirthWheelDialog();
                UmengUtil.onEvent(PersonalHomepageAct.this,new UmengUtil().INFO_BIRTHDAY_CLICK,null);
                break;
            case R.id.layout_phonenumber:
                UmengUtil.onEvent(PersonalHomepageAct.this,new UmengUtil().INFO_PHONENUMBER_CLICK,null);
                break;
            case R.id.btn_city:

                UmengUtil.onEvent(PersonalHomepageAct.this,new UmengUtil().INFO_REGION_CLICK,null);
                AreaAct.start(PersonalHomepageAct.this, new ParameCallBack() {
                    @Override
                    public void onCall(Object object) {
                        Map<String, String> result = (Map<String, String>) object;
                        String provinceId = result.get("provinceId");
                        String cityId = result.get("cityId");
                        String provinceName = result.get("provinceName");
                        String cityName = result.get("cityName");
                        changeUserCity(provinceId, cityId, provinceName, cityName);
                    }
                });
                break;
        }
    }

    private void changeBirthWheelDialog() {
        ChangeBirthDialog mChangeBirthDialog = new ChangeBirthDialog(
                PersonalHomepageAct.this);
        String[] ymds = curBirth.split("-");
        int year = Integer.parseInt(ymds[0]);
        int month = Integer.parseInt(ymds[1]);
        int day = Integer.parseInt(ymds[2]);
        mChangeBirthDialog.setDate(year, month, day);
        mChangeBirthDialog.show();
        mChangeBirthDialog.setBirthdayListener(new ChangeBirthDialog.OnBirthListener() {

            @Override
            public void onClick(String year, String month, String day) {
                String date = year + "-" + month + "-" + day;
                long selectBirthday = StringUtil.getMillisByDate(date);
                long currentBirth = StringUtil.getMillisByDate(curBirth);
                if (selectBirthday != currentBirth) {
                    changeUserBirth(selectBirthday);
                }
            }
        });
    }

    private void createSignatureDialog() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog.show();
        dialog.setContentView(R.layout.dialog_edit_signature);
        showKeyboard();
        final EditText editSignature = (EditText) dialog
                .findViewById(R.id.edit_signature);
        editSignature.setText(curSignature);
        editSignature.setSelection(curSignature.length());
        dialog.findViewById(R.id.btn_done)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String signature = editSignature.getText().toString().trim();
                        if (!"".equals(signature)) {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            if (!curSignature.equals(signature)) {
                                changeUserSignature(signature);
                            }
                        } else {
                            ViewUtils.showToast("不能为空");
                        }
                    }
                });
        dialog.findViewById(R.id.btn_cancel)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                });
    }

    private void changeUserBirth(final long birthday) {
        personalUserEditRequest = new PersonalUserEditRequest(birthday);
        personalUserEditRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                ViewUtils.showToast("修改成功");
                curBirth = StringUtil.getDateByMillis(birthday);
                txtBirthday.setText(curBirth);

                IMUserInfoVO user = Account.readLoginUser(context);
                user.setBirthday(birthday);
                Account.user = user;
                Account.saveLoginUser(context, user);
                //同步已修改的数据
                updateContactList();
            }

            @Override
            public void onFail(int code) {
                ViewUtils.showToast("修改失败");
            }
        });
        personalUserEditRequest.start(this);
    }

    private void changeUserGender(final byte gender) {
        personalUserEditRequest = new PersonalUserEditRequest(gender);
        personalUserEditRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                ViewUtils.showToast("修改成功");
                curGender = gender;
                txtGender.setText(curGender == 1 ? "男" : "女");
                IMUserInfoVO user = Account.readLoginUser(context);
                user.setGender(gender);
                Account.user = user;
                Account.saveLoginUser(context, user);
                //同步已修改的数据
                updateContactList();
            }

            @Override
            public void onFail(int code) {
                ViewUtils.showToast("修改失败");
            }
        });
        personalUserEditRequest.start(this);
    }

    private void changeUserCity(final String provinceId, final String cityId, final String provinceName, final String cityName) {
        personalUserEditRequest = new PersonalUserEditRequest(null, null, provinceId, cityId);
        personalUserEditRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                if (cityName != null) {
                    txtCity.setText(provinceName + "  " + cityName);
                } else {
                    txtCity.setText(provinceName);
                }
                IMUserInfoVO user = Account.readLoginUser(context);
                user.setProvinceName(provinceName);
                user.setCityName(cityName);
                Account.user = user;
                Account.saveLoginUser(context, user);
                ViewUtils.showToast("修改成功");
                //同步已修改的数据
                updateContactList();
            }

            @Override
            public void onFail(int code) {
                ViewUtils.showToast("修改失败");
            }
        });
        personalUserEditRequest.start(this);
    }

    private void changeUserSignature(final String signature) {
        personalUserEditRequest = new PersonalUserEditRequest(null, signature, null, null);
        personalUserEditRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                curSignature = signature;
                txtSignature.setText(curSignature);
                IMUserInfoVO user = Account.readLoginUser(context);
                user.setSignature(signature);
                Account.user = user;
                Account.saveLoginUser(context, user);
                ViewUtils.showToast("修改成功");
                //同步已修改的数据
                updateContactList();
            }

            @Override
            public void onFail(int code) {
                ViewUtils.showToast("修改失败");
            }
        });
        personalUserEditRequest.start(this);
    }

    private void updateContactList() {
        final IMGetContactListRequest iMGetContactListRequest = new IMGetContactListRequest(Account.user
                .getShopId());
        iMGetContactListRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                iMGetContactListRequest.isLoadingContact = false;
            }

            @Override
            public void onFail(int code) {
                iMGetContactListRequest.isLoadingContact = false;
            }
        });
        iMGetContactListRequest.start();
    }

    private void changeUserNick(final String nickName) {
        personalUserEditRequest = new PersonalUserEditRequest(nickName, null, null, null);
        personalUserEditRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                curNick = nickName;
                txtNick.setText(curNick);
                IMUserInfoVO user = Account.readLoginUser(context);
                user.setNickname(nickName);
                Account.user = user;
                Account.saveLoginUser(context, user);
                ViewUtils.showToast("修改成功");
                updateContactList();
            }

            @Override
            public void onFail(int code) {
                ViewUtils.showToast("修改失败");
            }
        });
        personalUserEditRequest.start(this);
    }

    private void createSexDialog() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog.show();
        dialog.setContentView(R.layout.dialog_select_sex);
        final RadioGroup fg = (RadioGroup) dialog.findViewById(R.id.radioGroup);
        if (curGender == 1) {
            ((RadioButton) dialog.findViewById(R.id.select_man)).setChecked(true);
        } else if (curGender == 2) {
            ((RadioButton) dialog.findViewById(R.id.select_woman)).setChecked(true);
        }
        fg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    if (group.getChildAt(i).getId() == checkedId) {
                        byte sex = 0;
                        if (i == 0) {
                            sex = 1;
                        } else if (i == 2) {
                            sex = 2;
                        }
                        if (curGender != sex) {
                            changeUserGender(sex);
                        }
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                }
            }
        });
    }

    private void createNickDialog() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog.show();
        dialog.setContentView(R.layout.dialog_edit_name);
        showKeyboard();
        final EditText nickInput = (EditText) dialog
                .findViewById(R.id.nick_input);
        nickInput.setText(txtNick.getText().toString().trim());
        nickInput.setSelection(nickInput.getText().toString().length());
        dialog.findViewById(R.id.nick_done)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String nick = nickInput.getText().toString().trim();
                        if (!"".equals(nick)) {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            if (!curNick.equals(nick)) {
                                changeUserNick(nick);
                            }
                        } else {
                            ViewUtils.showToast("不能为空");
                        }
                    }
                });
        dialog.findViewById(R.id.nick_cancel)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                });
    }

    @Override
    public void finish() {
        super.finish();
        if (personalUserEditRequest != null) {
            personalUserEditRequest.cancel();
        }
        Config.areaList = null;
    }

    @Override
    public void onScroll(int l, int t) {
        screenHeight = Config.screenHeight - DesityUtil.dp2px(this, 25);
        scrollHeight = scrollView.getChildAt(0).getMeasuredHeight() - screenHeight;
        float alpha = (float) t / scrollHeight;
        viewTitle.setAlpha(alpha);
    }
}
