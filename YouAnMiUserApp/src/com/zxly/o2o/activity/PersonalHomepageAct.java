package com.zxly.o2o.activity;

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.easeui.adapter.EaseContactAdapter;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.cropView.Crop;
import com.zxly.o2o.dialog.ChangeBirthDialog;
import com.zxly.o2o.dialog.GetPictureDialog;
import com.zxly.o2o.dialog.SettingDatePickerDialog;
import com.zxly.o2o.dialog.ShareDialog;
import com.zxly.o2o.model.User;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.BaseRequest.ResponseStateListener;
import com.zxly.o2o.request.CommonShareRequest;
import com.zxly.o2o.request.FileUploadRequest;
import com.zxly.o2o.request.IMGetShopContactsRequest;
import com.zxly.o2o.request.PersonalUserEditRequest;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.DesityUtil;
import com.zxly.o2o.util.EncodingHandler;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.PicTools;
import com.zxly.o2o.util.ShareListener;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.UMengAgent;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.CircleImageView;
import com.zxly.o2o.view.ObservableScrollView;
import com.zxly.o2o.view.OnScrollChangedCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fengrongjian 2015-12-11
 * @description 个人信息界面
 */
public class PersonalHomepageAct extends BasicAct implements OnClickListener, OnDateSetListener, OnScrollChangedCallback {
    private Context context;
    private View viewTitle;
    private ObservableScrollView scrollView;
    private CircleImageView imgUserHead;
    private TextView btnSignature;
    private TextView txtUserNick, txtUserGender, txtUserBirth, txtUserCity;
    private TextView txtPromotionCode;
    private ImageView imgQrCode;
    private Dialog dialog;
    private ShareDialog shareDialog;
    private String curBirth, curSignature, curNick;
    private byte curGender;
    private int scrollHeight, screenHeight;
    private String shareUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_personal_homepage);
        context = this;
        initViews();
        loadShareUrl();
    }

    private void loadShareUrl(){
        final CommonShareRequest commonShareRequest = new CommonShareRequest();
        commonShareRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                shareUrl = commonShareRequest.getShareUrl();
                if(!StringUtil.isNull(shareUrl)) {
                    generateQRCode(shareUrl);
                }
            }

            @Override
            public void onFail(int code) {

            }
        });
        commonShareRequest.start(this);
    }

    public static void start(Activity curAct) {
        Intent intent = new Intent(curAct, PersonalHomepageAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

    private void initViews() {
        dialog = new Dialog(this, R.style.dialog);
        findViewById(R.id.btn_back).setOnClickListener(this);
        scrollView = (ObservableScrollView) findViewById(R.id.scroll_view);
        scrollView.setOnScrollChangedCallback(this);
        viewTitle = findViewById(R.id.view_title);
        viewTitle.setOnClickListener(this);
        imgUserHead = (CircleImageView) findViewById(R.id.btn_user_head);
        imgUserHead.setOnClickListener(this);
        ViewUtils.setText(findViewById(R.id.txt_user_name), Account.user.getUserName());
        btnSignature = (TextView) findViewById(R.id.btn_user_signature);
        btnSignature.setOnClickListener(this);
        findViewById(R.id.btn_user_nick).setOnClickListener(this);
        findViewById(R.id.btn_user_gender).setOnClickListener(this);
        findViewById(R.id.btn_user_birth).setOnClickListener(this);
        findViewById(R.id.btn_user_city).setOnClickListener(this);
        findViewById(R.id.btn_promotion).setOnClickListener(this);
        txtUserNick = (TextView) findViewById(R.id.txt_nick);
        txtUserGender = (TextView) findViewById(R.id.txt_gender);
        txtUserBirth = (TextView) findViewById(R.id.txt_birth);
        txtUserCity = (TextView) findViewById(R.id.txt_city);
        txtPromotionCode = (TextView) findViewById(R.id.txt_my_promotion_code);
        imgQrCode = (ImageView) findViewById(R.id.img_qr_code);
        setUserInfo();
//        generateQRCode(Account.shareUrl + "&userId=" + Account.user.getId());
    }

    private void generateQRCode(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int size = DesityUtil.dp2px(context, 140);
                final Bitmap bitmap = EncodingHandler.createQRImage(url, size, size);
                imgQrCode.post(new Runnable() {
                    @Override
                    public void run() {
                        imgQrCode.setImageBitmap(bitmap);
                    }
                });

            }
        }).start();
    }

    private void setUserInfo() {
        setUserHead();
        if (!StringUtil.isNull(Account.user.getSignature())){
            curSignature = Account.user.getSignature();
            btnSignature.setText(curSignature);
        } else {
            curSignature = "";
            btnSignature.setText("编辑个性签名");
        }
        curNick = Account.user.getNickName();
        txtUserNick.setText(curNick);
        curGender = Account.user.getGender();
        if (curGender == 0) {
            curGender = 1;
        }
        txtUserGender.setText(curGender == 1 ? "男" : "女");
        if (Account.user.getBirthday() != 0) {
            curBirth = StringUtil.getDateByMillis(Account.user.getBirthday());
        } else {
            curBirth = "1900-01-01";
        }
        txtUserBirth.setText(curBirth);
        String provinceName = Account.user.getProvinceName();
        String cityName = Account.user.getCityName();
        if (cityName != null && !cityName.equals(provinceName)) {
            ViewUtils.setText(txtUserCity, provinceName + "  " + cityName);
        } else {
            ViewUtils.setText(txtUserCity, provinceName);
        }
        ViewUtils.setText(txtPromotionCode, "我的推广码：" + Account.user.getPromotionCode());
        ViewUtils.setText(findViewById(R.id.txt_shop_info), "扫描二维码，下载 \"" + getResources().getString(R.string.app_name) + "\"");
    }

    private void setUserHead() {
        String thumHeadUrl = Account.user.getThumHeadUrl();
        if (StringUtil.isNull(thumHeadUrl)) {
            imgUserHead.setImageResource(R.drawable.personal_default_head);
        } else {
            imgUserHead.setImageUrl(thumHeadUrl, R.drawable.personal_default_head);
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
        new FileUploadRequest(file, params,
                "user/photo/edit", mMainHandler).startUpload();
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

                    User user = Account.readLoginUser(context);
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
                break;
            case R.id.view_title:
                break;
            case R.id.btn_user_head:
                new GetPictureDialog(false).show(mMainHandler);
                break;
            case R.id.btn_user_signature:
                createSignatureDialog();
                break;
            case R.id.btn_user_nick:
                createNickDialog();
                break;
            case R.id.btn_user_gender:
                createSexDialog();
                break;
            case R.id.btn_user_birth:
//                createBirthDialog();
                changeBirthWheelDialog();
                break;
            case R.id.btn_user_city:
                SettingAddressAreaAct.start(PersonalHomepageAct.this, new ParameCallBack() {
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
            case R.id.btn_promotion:
                if (StringUtil.isNull(shareUrl)) {
                    ViewUtils.showToast("推广地址为空");
                    return;
                }
                String appName = getResources().getString(R.string.app_name);
                if (shareDialog == null)
                    shareDialog = new ShareDialog();
                String title = "客官，请您下载" + appName + "，带店回家、一键购物、轻松赚钱！下载请点击";
                title = "客官，恭喜您获得本店特权大礼包！点击下载带店回家、立享特权、一键购物、轻松赚钱！";
//                String url = Account.shareUrl + "&userId=" + Account.user.getId();
                shareDialog.show(appName, title, shareUrl, "", new ShareListener() {
                    @Override
                    public void onComplete(Object var1) {
                        ViewUtils.showToast("发送成功");
                    }

                    @Override
                    public void onFail(int errorCode) {

                    }
                });
                break;
        }
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
                .setOnClickListener(new OnClickListener() {

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
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                });
    }


    private void changeUserBirth(final long birthday) {
        final PersonalUserEditRequest request = new PersonalUserEditRequest(birthday);
        request.setOnResponseStateListener(new ResponseStateListener() {

            @Override
            public void onOK() {
                ViewUtils.showToast("修改成功");
                curBirth = StringUtil.getDateByMillis(birthday);
                txtUserBirth.setText(curBirth);

                User user = Account.readLoginUser(context);
                user.setBirthday(birthday);
                Account.user = user;
                Account.saveLoginUser(context, user);
            }

            @Override
            public void onFail(int code) {
                ViewUtils.showToast("修改失败");
            }
        });
        request.start(this);
    }

    private void changeUserGender(final byte gender) {
        final PersonalUserEditRequest request = new PersonalUserEditRequest(gender);
        request.setOnResponseStateListener(new ResponseStateListener() {

            @Override
            public void onOK() {
                ViewUtils.showToast("修改成功");
                curGender = gender;
                txtUserGender.setText(curGender == 1 ? "男" : "女");
                User user = Account.readLoginUser(context);
                user.setGender(gender);
                Account.user = user;
                Account.saveLoginUser(context, user);
            }

            @Override
            public void onFail(int code) {
                ViewUtils.showToast("修改失败");
            }
        });
        request.start(this);
    }


    private void changeUserCity(final String provinceId, final String cityId, final String provinceName, final String cityName) {
        final PersonalUserEditRequest request = new PersonalUserEditRequest(null, null, provinceId, cityId);
        request.setOnResponseStateListener(new ResponseStateListener() {

            @Override
            public void onOK() {
                if (cityName != null) {
                    txtUserCity.setText(provinceName + "  " + cityName);
                } else {
                    txtUserCity.setText(provinceName);
                }
                User user = Account.readLoginUser(context);
                user.setProvinceName(provinceName);
                user.setCityName(cityName);
                Account.user = user;
                Account.saveLoginUser(context, user);
                ViewUtils.showToast("修改成功");
            }

            @Override
            public void onFail(int code) {
                ViewUtils.showToast("修改失败");
            }
        });
        request.start(this);
    }


    private void changeUserSignature(final String signature) {
        final PersonalUserEditRequest request = new PersonalUserEditRequest(null, signature, null, null);
        request.setOnResponseStateListener(new ResponseStateListener() {

            @Override
            public void onOK() {
                curSignature = signature;
                btnSignature.setText(curSignature);
                User user = Account.readLoginUser(context);
                user.setSignature(signature);
                Account.user = user;
                Account.saveLoginUser(context, user);
                ViewUtils.showToast("修改成功");
                //更新最新联系人数据
                updateContactList();
            }

            @Override
            public void onFail(int code) {
                ViewUtils.showToast("修改失败");
            }
        });
        request.start(this);
    }

    /**
     * 由于之前修改个人信息后没有同步 故增加以下方法
     */
    private void updateContactList() {
        //刷新联系人
        EaseContactAdapter.unRegistList.clear();
        final IMGetShopContactsRequest iMGetContactListRequest= new IMGetShopContactsRequest(false);
        iMGetContactListRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                iMGetContactListRequest.isLoadingContact=false;
            }

            @Override
            public void onFail(int code) {
                iMGetContactListRequest.isLoadingContact=false;
            }
        });
        iMGetContactListRequest.start();
    }

    private void changeUserNick(final String nickName) {
        final PersonalUserEditRequest request = new PersonalUserEditRequest(nickName, null, null, null);
        request.setOnResponseStateListener(new ResponseStateListener() {

            @Override
            public void onOK() {
                curNick = nickName;
                txtUserNick.setText(curNick);
                User user = Account.readLoginUser(context);
                user.setNickName(nickName);
                Account.user = user;
                Account.saveLoginUser(context, user);
                ViewUtils.showToast("修改成功");
                //更新最新联系人数据
                updateContactList();
            }

            @Override
            public void onFail(int code) {
                ViewUtils.showToast("修改失败");
            }
        });
        request.start(this);
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
        fg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

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
                        if(curGender != sex){
                            changeUserGender(sex);
                        }
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        UMengAgent.onEvent(PersonalHomepageAct.this, UMengAgent.personal_change_sex);
                    }
                }
            }
        });
    }

    private void createBirthDialog() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        String birthday = txtUserBirth.getText().toString();
        final Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = df.parse(birthday);
            c.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dialog = new SettingDatePickerDialog(PersonalHomepageAct.this,
                this,
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.show();
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
                if(selectBirthday != currentBirth) {
                    changeUserBirth(selectBirthday);
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
        nickInput.setText(txtUserNick.getText().toString().trim());
        nickInput.setSelection(nickInput.getText().toString().length());
        dialog.findViewById(R.id.nick_done)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String nick = nickInput.getText().toString().trim();
                        if (!"".equals(nick)) {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            if(!curNick.equals(nick)) {
                                changeUserNick(nick);
                                UMengAgent.onEvent(PersonalHomepageAct.this, UMengAgent.personal_change_nick);
                            }
                        } else {
                            ViewUtils.showToast("不能为空");
                        }
                    }
                });
        dialog.findViewById(R.id.nick_cancel)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                });

    }

    @Override
    public void onDateSet(DatePicker arg0, int year, int month, int day) {
//        String date = year + "-" + (month + 1) + "-" + day;
//        changeUserBirth(StringUtil.getMillisByDate(date), year, month, day);
        UMengAgent.onEvent(this, UMengAgent.personal_change_birthday);
    }

    @Override
    public void finish() {
        super.finish();
        Account.areaList = null;
    }

    @Override
    public void onScroll(int l, int t) {
        screenHeight = Config.screenHeight - DesityUtil.dp2px(this, 25);
        scrollHeight = scrollView.getChildAt(0).getMeasuredHeight() - screenHeight;
        float alpha = (float) t / scrollHeight;
        viewTitle.setAlpha(alpha);
    }
}
