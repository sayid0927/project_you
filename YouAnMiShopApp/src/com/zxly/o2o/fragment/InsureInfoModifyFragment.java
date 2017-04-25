package com.zxly.o2o.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.easemob.easeui.AppException;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.AlbumActivity;
import com.zxly.o2o.activity.GuaranteeDetailAct;
import com.zxly.o2o.activity.TouchImageViewAct;
import com.zxly.o2o.dialog.LoadingDialog;
import com.zxly.o2o.model.GuaranteeInfo;
import com.zxly.o2o.model.InsureImage;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.FileUploadRequest;
import com.zxly.o2o.request.InsureInfoModifyRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.BitmapUtil;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.MyImageManager;
import com.zxly.o2o.util.ParameCallBackById;
import com.zxly.o2o.util.PicTools;
import com.zxly.o2o.util.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fengrongjian on 2016/9/5.
 */
public class InsureInfoModifyFragment extends BaseFragment implements View.OnClickListener{
    protected Uri fileUri;
    protected File photo;
    private ImageView ivUploadPhoto;
    private boolean isSelectedPhoto;
    private FileUploadRequest fileUploadRequest;
    private String originImageUrl, thumImageUrl;
    private LoadingDialog loadingDialog;
    private double _insurePrice;
    private GuaranteeInfo guaranteeInfo;
    private InsureImage imageUrl = new InsureImage();
    private EditText editName, editIMEI;

    @Override
    protected void initView() {
        init();
    }

    @Override
    protected int layoutId() {
        return R.layout.insure_info_modify;
    }

    public void setData(GuaranteeInfo guaranteeInfo){
        this.guaranteeInfo=guaranteeInfo;
    }

    public void init() {

        if(getActivity() instanceof GuaranteeDetailAct){
            ((GuaranteeDetailAct)getActivity()).btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                    GuaranteeDetailAct.start(guaranteeInfo.getId() + "");
                }
            });
            ((GuaranteeDetailAct)getActivity()).btnRight.setOnClickListener(this);
            ViewUtils.setGone(((GuaranteeDetailAct)getActivity()).btnRight);
        //    ((GuaranteeDetailAct)getActivity()).btnBack.setOnClickListener(this);
        }

        editName = (EditText) findViewById(R.id.edit_name);
        editName.setText(guaranteeInfo.getOrderInfo().getUserName());
        String phoneModel = guaranteeInfo.getOrderInfo().getPhoneModel();
        ((TextView) findViewById(R.id.tv_phone)).setText(guaranteeInfo.getOrderInfo().getUserPhone());
        ((TextView) findViewById(R.id.tv_brand)).setText(phoneModel);

        editIMEI = (EditText) findViewById(R.id.edit_imei);
        editIMEI.setText(guaranteeInfo.getOrderInfo().getPhoneImei());

        if (phoneModel.contains("iPhone")) {
            ((TextView) findViewById(R.id.tv_imei)).setTextColor(getResources().getColor(R.color.black));
            editIMEI.setTextColor(getResources().getColor(R.color.black));
            editIMEI.setEnabled(true);
        }


        ((TextView) findViewById(R.id.tv_insure)).setText(guaranteeInfo.getPrice()+"");
        ((TextView) findViewById(R.id.tv_price)).setText(guaranteeInfo.getOrderInfo().getPhonePrice()+"");

        EaseConstant.setImage((NetworkImageView) findViewById(R.id.eg_image), guaranteeInfo.getProduct().getSampleImage(), R.drawable.insure_pic_default, null);

        findViewById(R.id.eg_image).setOnClickListener(this);
        findViewById(R.id.btn_submit).setOnClickListener(this);
        findViewById(R.id.layout_user_info).setOnClickListener(this);
        ivUploadPhoto = (ImageView) findViewById(R.id.btn_photo_upload);
        ivUploadPhoto.setOnClickListener(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = new BitmapUtil().getHttpBitmap(guaranteeInfo.getOrderInfo().getThumImageUrl());
                ivUploadPhoto.post(new Runnable() {
                    @Override
                    public void run() {
                        ivUploadPhoto.setImageBitmap(bitmap);
                    }
                });
            }
        }).start();
        isSelectedPhoto = true;
        imageUrl.setIsOK(1);

    }

    private void postFile(File file) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("isThum", String.valueOf(2));  //1:no 2:yes
        fileUploadRequest = new FileUploadRequest(file, params,
                "common/image/upload", mMainHandler);
        fileUploadRequest.setShowLoading(false);
        fileUploadRequest.startUpload();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                if (TextUtils.isEmpty(editName.getText()
                        .toString().trim())) {
                    ViewUtils.showToast("姓名不能为空!");
                } else if (TextUtils.isEmpty(editIMEI.getText()
                        .toString().trim())) {
                    ViewUtils.showToast("IMEI不能为空!");
                } else if (!isSelectedPhoto) {
                    ViewUtils.showToast("请先选择上传的图片!");
                } else {
                    if (loadingDialog == null) {
                        loadingDialog = new LoadingDialog();
                    }
                    loadingDialog.show();
                    commit();
                }
                break;

            case R.id.layout_user_info:
                keyBoardCancle();
                break;

            case R.id.eg_image:
                Intent intent2 = new Intent(getActivity(), TouchImageViewAct.class);
                intent2.putExtra("file_path", guaranteeInfo.getProduct().getSampleImage());
                intent2.putExtra("file_is_local", false);
                startActivityForResult(intent2, Constants.CHANGE_PIC);
                break;

            case R.id.btn_photo_upload:
                if (isSelectedPhoto) {
                    Intent intent = new Intent(getActivity(), TouchImageViewAct.class);
                    if(photo != null) {
                        intent.putExtra("file_path", photo.getPath());
                        intent.putExtra("file_is_local", true);
                    } else {
                        intent.putExtra("file_path", guaranteeInfo.getOrderInfo().getImageUrl());
                    }
                    startActivityForResult(intent, Constants.CHANGE_PIC);
                } else {
                    selectorPhoto();
                }
                break;
        }
    }

    private void commit() {
        if (Account.user != null && imageUrl.getIsOK()==1) {
            InsureInfoModifyRequest modifyRequest =
                    new InsureInfoModifyRequest(guaranteeInfo.getId(), imageUrl.getOriginImageUrl(), Account.user.getShopId(), imageUrl.getThumImageUrl(), editName
                            .getText()
                            .toString().trim(), editIMEI.getText().toString().trim());
            modifyRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                @Override
                public void onOK() {
                    ViewUtils.showToast("延保订单修改成功!");
                    if (loadingDialog != null) {
                        loadingDialog.dismiss();
                    }
                    getActivity().finish();
                    GuaranteeDetailAct.start(guaranteeInfo.getId() + "");
                }

                @Override
                public void onFail(int code) {
                    if (loadingDialog != null) {
                        loadingDialog.dismiss();
                    }
                    if (20229 == code) {//订单状态已经变更
                        getActivity().finish();
                        GuaranteeDetailAct.start(guaranteeInfo.getId() + "");
                    } else {
                        ViewUtils.showToast("延保订单修改失败!");
                    }
                }
            });
            modifyRequest.start(this);
        }else if(imageUrl.getIsOK()==2){
            ViewUtils.showToast("图片上传失败!");
            if (loadingDialog != null) {
                loadingDialog.dismiss();
            }
        } else if (Account.user != null) {
            mMainHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    commit();
                }
            }, 3000);
        }
    }

    private void selectorPhoto() {
        Intent intent = new Intent(getActivity(), AlbumActivity.class);
        intent.putExtra("MaxSelectedSize", 1);
        AlbumActivity
                .start(getActivity(), intent, new ParameCallBackById() {
                    @Override
                    public void onCall(int id, Object object) {
                        ArrayList<String> images = (ArrayList<String>) object;
                        if(images!=null&&images.size()>0) {
                            photo = new File(images.get(0));
                            MyImageManager.from(getActivity())
                                    .displayImage(ivUploadPhoto,
                                            photo.getPath(),
                                            R.drawable.pic_normal,
                                            100,
                                            100);
                            isSelectedPhoto = true;  //标记已经选择了图片
                            postFile(photo);  //上传
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case Constants.GET_PIC_FROM_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    if (fileUri != null) {
                        MyImageManager.from(getActivity())
                                .displayImage(ivUploadPhoto,
                                        fileUri.getPath(), R.drawable.pic_normal,
                                        100,
                                        100);
                        isSelectedPhoto = true;  //标记已经选择了图片
                        postFile(photo); //上传
                    } else if (mMainHandler != null) {
                        mMainHandler.sendEmptyMessage(Constants.DELETE_PIC);
                    }
                } else if (resultCode == Activity.RESULT_CANCELED && mMainHandler != null) {
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

            case Constants.CHANGE_PIC:
                if (resultCode == Constants.CHANGE_PIC) {
                    selectorPhoto();
                }

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (getActivity().isFinishing()) {
            return;
        }
        switch (msg.what) {
            case Constants.GET_PIC_FROM_CAMERA:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                photo = PicTools.getOutputPhotoFile(true, "");
                fileUri = Uri.fromFile(photo);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent, Constants.GET_PIC_FROM_CAMERA);
                break;
            case Constants.MSG_SUCCEED:
                String data = (String) msg.obj;

                try {
                    JSONObject object = new JSONObject(data);
                    String url = object.getString("imageUrl");
                    imageUrl = GsonParser
                            .getInstance().fromJson(url, new TypeToken<InsureImage>() {
                            });

                    imageUrl.setIsOK(1);

                } catch (AppException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case Constants.MSG_FAILED:
                imageUrl.setIsOK(2);
                break;
        }
    }

}
