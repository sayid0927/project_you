package com.zxly.o2o.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.ChooseGroupPeopleAct;
import com.zxly.o2o.activity.ChooseSendAct;
import com.zxly.o2o.adapter.UserDefineDeleteGridAdapter;
import com.zxly.o2o.application.Config;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.FileUploadRequest;
import com.zxly.o2o.request.PushSelfDefineRequest;
import com.zxly.o2o.request.UploadImageTask;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.PicTools;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;
import com.zxly.o2o.view.MGridView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hejun on 2016/9/2.
 * 选择推送内容--自定义页面
 */
public class UserDefinedSendFragment extends BaseFragment{

    private EditText edit_user_sendcontent;
    private MGridView choose_pic_gridview;
    public UserDefineDeleteGridAdapter userDefineDeleteGridAdapter;
    private Uri fileUri;
    private File photo;
    private int actionType;
    public static Bitmap photoBitmap;
    //点击推送的时候 选择图片的张数
    private int imgCount;
    private StringBuffer imgUrls = new StringBuffer();
    private boolean imgUploadDone = false;
    int hasUploadNo;
    LoadingView loadingView;
    private int cursorPosIntroduce;
    private String inputAfterTextIntroduce;
    private boolean resetTextIntroduce;

    private Handler mMainHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.GET_PIC_FROM_CELLPHONE: // 解决4.4以上的版本图片url获取不到
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        // intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/jpeg");
                        startActivityForResult(intent, Constants.GET_PIC_FROM_CELLPHONE);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);// ACTION_OPEN_DOCUMENT
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/jpeg");
                        startActivityForResult(intent, Constants.GET_PIC_FROM_CELLPHONE);
                    }
                    break;

                case Constants.GET_PIC_FROM_CAMERA:
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    photo = PicTools.getOutputPhotoFile(true, "");
                    fileUri = Uri.fromFile(photo);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(intent, Constants.GET_PIC_FROM_CAMERA);
                    break;
                case Constants.MSG_SUCCEED:
                    String obj= (String) msg.obj;
                    String originImageUrl = null;
                    try {
                        JSONObject jsonObject = new JSONObject(obj);
                        String imageUrl = jsonObject.getString("imageUrl");
                        JSONObject imgObject = new JSONObject(imageUrl);
                        originImageUrl = imgObject.getString("originImageUrl");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(!"".equals(imgUrls.toString())) {
                        imgUrls.append(",");
                    }
                    imgUrls.append(originImageUrl);

                    hasUploadNo++;
                    if (imgCount == hasUploadNo) {
                        imgUploadDone = true;
                    }
                    if (imgUploadDone) {
                        push();
                    }
                    break;
            }
        }
    };
    private FileUploadRequest fileUploadRequest;

    public static UserDefinedSendFragment newInstance(){
        UserDefinedSendFragment f=new UserDefinedSendFragment();
        return f;
    }

    @Override
    protected void initView() {
        loadingView = (LoadingView) findViewById(R.id.view_loading);
        edit_user_sendcontent = (EditText) findViewById(R.id.edit_user_sendcontent);
        edit_user_sendcontent.addTextChangedListener(textWatcher);
        choose_pic_gridview = (MGridView) findViewById(R.id.choose_pic_gridview);
        userDefineDeleteGridAdapter = new UserDefineDeleteGridAdapter(getActivity());
        userDefineDeleteGridAdapter.addItem("_default", false);
        choose_pic_gridview.setAdapter(userDefineDeleteGridAdapter);
        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edit_user_sendcontent.getText().toString().trim();
                if(TextUtils.isEmpty(content)){
                    ViewUtils.showToast("内容不能为空!");
                }else{
//                    Map<String, Object> sendParams = new HashMap<String, Object>();
//                    for (int i = 0; i < userDefineDeleteGridAdapter.getContent().size(); i++) {
//                        photo = new File((String) userDefineDeleteGridAdapter.getContent().get(i));
//                    }
//                    startUploadRequest("common/image/upload", sendParams);

                    if (userDefineDeleteGridAdapter.getCount() == 1) {//纯文字推送
                        imgUrls = new StringBuffer();
                        push();
                    } else {//文字+图片推送
                        hasUploadNo = 0;
                        imgUrls = new StringBuffer();
                        imgUploadDone = false;
                        postFile();
                    }
                    loadingView.startLoading();
                }
            }
        });

    }

    private void push(){
        PushSelfDefineRequest pushSelfDefineRequest = new PushSelfDefineRequest(edit_user_sendcontent.getText().toString().trim(), imgUrls.toString(), ((ChooseSendAct)getActivity()).fansImeis, ((ChooseSendAct)getActivity()).userIds);
        pushSelfDefineRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                loadingView.onLoadingComplete();
                ViewUtils.showToast("操作成功!");
                Config.isShouldShow=true;
                if(ChooseGroupPeopleAct.getInstance()!=null){
                    ChooseGroupPeopleAct.getInstance().finish();
                }
                getActivity().finish();
            }

            @Override
            public void onFail(int code) {
                loadingView.onLoadingComplete();
            }
        });
        pushSelfDefineRequest.start(getActivity());
    }

    private void postFile() {
        Map<String, Object> params = new HashMap<String, Object>();
        imgCount=userDefineDeleteGridAdapter.getCount();
        if(userDefineDeleteGridAdapter.getContent().contains("_default")){
            imgCount = imgCount - 1;
        }
        for (int i = 0; i < imgCount; i++) {
            photo = new File((String) userDefineDeleteGridAdapter.getContent().get(i));
            fileUploadRequest = new FileUploadRequest(photo, params,
                    "common/image/upload", mMainHandler);
            fileUploadRequest.setShowLoading(false);
            fileUploadRequest.startUpload();
        }

    }

    @Override
    protected int layoutId() {
        return R.layout.win_userdifined_send;
    }

    TextWatcher textWatcher = new TextWatcher() {
        private CharSequence temp;
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp=s;
            if (!resetTextIntroduce) {
                cursorPosIntroduce = edit_user_sendcontent.getSelectionEnd();
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

                if (count >= 2&&count<500) {//表情符号的字符长度最小为2
                    CharSequence input = s.subSequence(cursorPosIntroduce, cursorPosIntroduce + count);
                    if (StringUtil.containsEmoji(input.toString())) {
                        resetTextIntroduce = true;
                        ViewUtils.showToast("暂不支持输入表情哦");
                        //是表情符号就将文本还原为输入表情符号之前的内容
                        edit_user_sendcontent.setText(inputAfterTextIntroduce);
                        CharSequence text = edit_user_sendcontent.getText();
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
                if (s.length() >= 500) {
                    ViewUtils.showToast("不能输入更多了!");
                }
                ((TextView) findViewById(R.id.txt_length)).setText( s.length() + "/500");
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        actionType = requestCode;
        switch (requestCode) {
            case Constants.GET_PIC_FROM_CAMERA:
                if (resultCode == -1) {
                    if (fileUri != null) {
//                        userDefineDeleteGridAdapter.addItem(fileUri.getPath());
//                        userDefineDeleteGridAdapter.addItem("_default");
//                        userDefineDeleteGridAdapter.notifyDataSetChanged();
                        userDefineDeleteGridAdapter.refreshView(fileUri.getPath());
                    } else if (mMainHandler != null) {
                        mMainHandler.sendEmptyMessage(Constants.DELETE_PIC);
                    }
                } else if (resultCode == 0 && mMainHandler != null) {
                    if (userDefineDeleteGridAdapter != null && !userDefineDeleteGridAdapter.getContent().contains("_default")) {
                        userDefineDeleteGridAdapter.addItem("_default");
                    }
                    mMainHandler.sendEmptyMessage(Constants.DELETE_PIC);
                } else if (resultCode == Constants.GET_PIC_FROM_CAMERA && mMainHandler != null) {  //触发拍照
                    mMainHandler.sendEmptyMessage(Constants.GET_PIC_FROM_CAMERA);
                } else {
                    if (mMainHandler != null) {
                        mMainHandler.sendEmptyMessage(Constants.DELETE_PIC);
                    }
                    ViewUtils.showToast("图片获取失败");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

//    @Override
//    protected void handleMessage(Message msg) {
//
//        switch (msg.what) {
//            case Constants.GET_PIC_FROM_CELLPHONE: // 解决4.4以上的版本图片url获取不到
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//                    Intent intent = new Intent(Intent.ACTION_PICK,
//                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    // intent.addCategory(Intent.CATEGORY_OPENABLE);
//                    intent.setType("image/jpeg");
//                    startActivityForResult(intent, Constants.GET_PIC_FROM_CELLPHONE);
//                } else {
//                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);// ACTION_OPEN_DOCUMENT
//                    intent.addCategory(Intent.CATEGORY_OPENABLE);
//                    intent.setType("image/jpeg");
//                    startActivityForResult(intent, Constants.GET_PIC_FROM_CELLPHONE);
//                }
//                break;
//
//            case Constants.GET_PIC_FROM_CAMERA:
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                photo = PicTools.getOutputPhotoFile(true, "");
//                fileUri = Uri.fromFile(photo);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//                startActivityForResult(intent, Constants.GET_PIC_FROM_CAMERA);
//                break;
//
//        }
//    }



    public void takePhoto(int requestCode, int resultCode, Intent data){
        actionType = requestCode;
        switch (requestCode) {
            case Constants.GET_PIC_FROM_CAMERA:
                if (resultCode == -1) {
                    if (fileUri != null) {
                        userDefineDeleteGridAdapter.addItem(fileUri.getPath());
                        userDefineDeleteGridAdapter.addItem("_default");
                        userDefineDeleteGridAdapter.notifyDataSetChanged();
                    } else if (mMainHandler != null) {
                        mMainHandler.sendEmptyMessage(Constants.DELETE_PIC);
                    }
                } else if (resultCode == 0 && mMainHandler != null) {
                    if (userDefineDeleteGridAdapter != null && !userDefineDeleteGridAdapter.getContent().contains("_default")) {
                        userDefineDeleteGridAdapter.addItem("_default");
                    }
                    mMainHandler.sendEmptyMessage(Constants.DELETE_PIC);
                } else if (resultCode == Constants.GET_PIC_FROM_CAMERA && mMainHandler != null) {  //触发拍照
                    mMainHandler.sendEmptyMessage(Constants.GET_PIC_FROM_CAMERA);
                } else {
                    if (mMainHandler != null) {
                        mMainHandler.sendEmptyMessage(Constants.DELETE_PIC);
                    }
                    ViewUtils.showToast("图片获取失败");
                }
                break;
        }
    }


    private void startUploadRequest( String requestUrl,  Map<String, Object> sendParams) {

        if (userDefineDeleteGridAdapter != null) {
            sendParams.put("shopId", String.valueOf(Account.user.getShopId()));

            //noinspection unchecked
            new UploadImageTask(requestUrl, mMainHandler, sendParams).execute
                    (userDefineDeleteGridAdapter.getContent());

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        photoBitmap = null;
    }
}
