package com.zxly.o2o.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.zxly.o2o.activity.FragmentListAct;
import com.zxly.o2o.activity.GuaranteeDetailAct;
import com.zxly.o2o.activity.TouchImageViewAct;
import com.zxly.o2o.dialog.LoadingDialog;
import com.zxly.o2o.dialog.VerifyDialog;
import com.zxly.o2o.model.GuaranteeInfo;
import com.zxly.o2o.model.InsureImage;
import com.zxly.o2o.model.SectionPrice;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.FileUploadRequest;
import com.zxly.o2o.request.InsureInfoCommitRequest;
import com.zxly.o2o.request.InsureRejectRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.CallBack;
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
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/25.
 */
public class InsureInfoComplementFragment extends BaseFragment implements View.OnClickListener{
    protected Uri fileUri;
    protected File photo;
    private ImageView ivUploadPhoto;
    private boolean isSelectedPhoto;
    private FileUploadRequest fileUploadRequest;
    private InsureInfoCommitRequest commitRequest;
    private EditText priceET;
    private String originImageUrl, thumImageUrl;
    private LoadingDialog loadingDialog;
    private List<SectionPrice> prices = new ArrayList<SectionPrice>();
    private boolean priceChanged;
    private Runnable runnable;
    private TextView insurePrice;
    private double _insurePrice;
    private GuaranteeInfo guaranteeInfo;
    private InsureImage imageUrl = new InsureImage();



    @Override
    protected void initView() {
        init();
    }

    @Override
    protected int layoutId() {
        return R.layout.insure_info_complement;
    }

    public void setData(GuaranteeInfo guaranteeInfo){
        this.guaranteeInfo=guaranteeInfo;
    }

    public void init() {
        if(getActivity() instanceof GuaranteeDetailAct){
            ((GuaranteeDetailAct)getActivity()).btnRight.setOnClickListener(this);
        //    ((GuaranteeDetailAct)getActivity()).btnBack.setOnClickListener(this);
        }

        prices = guaranteeInfo.getProduct().getPrices();

        ((TextView) findViewById(R.id.tv_name))
                .setText(Html.fromHtml(String.format(getString(R.string.insure_name),
                        guaranteeInfo.getOrderInfo().getUserName(),
                        " ( " + guaranteeInfo.getOrderInfo()
                                .getUserPhone() + " ) ")));


        ((TextView) findViewById(R.id.tv_brand)).setText("品牌型号 : " + guaranteeInfo.getOrderInfo().getPhoneModel());
        ((TextView) findViewById(R.id.tv_imei)).setText("IMEI         : " + guaranteeInfo.getOrderInfo().getPhoneImei());

        EaseConstant.setImage((NetworkImageView) findViewById(R.id.eg_image), guaranteeInfo.getProduct().getSampleImage(), R.drawable.insure_pic_default, null);

        priceET = (EditText) findViewById(R.id.et_price_input);

        findViewById(R.id.eg_image).setOnClickListener(this);
        insurePrice = (TextView) findViewById(R.id.et_insure_input);
        findViewById(R.id.btn_submit).setOnClickListener(this);
        findViewById(R.id.layout_user_info).setOnClickListener(this);
        ivUploadPhoto = (ImageView) findViewById(R.id.btn_photo_upload);
        ivUploadPhoto.setOnClickListener(this);

        priceET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {

                if (priceChanged) {
                    mMainHandler.removeCallbacks(runnable);
                }

                if(s.length()==0||".".equals(String.valueOf(s.toString().charAt(0)))){
                    return;
                }

                priceChanged = true;
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < prices.size(); i++) {
                            if (Double.valueOf(s.toString().trim()) >= prices.get(i).getBeginPrice()
                                    && Double
                                    .valueOf(s.toString().trim()) <= prices.get(i).getEndPrice()) {
                                insurePrice.setText("￥" + prices.get(i).getUniformPrice());
                                _insurePrice=prices.get(i).getUniformPrice();
                                break;
                            }else if(i==prices.size()-1){
                                insurePrice.setText("￥" + 0);
                            }
                        }
                        priceChanged = false;
                    }
                };

                mMainHandler.postDelayed(runnable, 1000);
            }
        });

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
            case R.id.btn_right:
                new VerifyDialog().show(new CallBack() {
                    @Override
                    public void onCall() {
                        if (Account.user != null) {
                            InsureRejectRequest insureRejectRequest = new InsureRejectRequest(guaranteeInfo
                                    .getId(), Account.user
                                    .getShopId());
                            insureRejectRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                                @Override
                                public void onOK() {
                                    ViewUtils.showToast("已拒绝");
                                    getActivity().finish();
                                }

                                @Override
                                public void onFail(int code) {
                                    ViewUtils.showToast("拒绝失败");
                                }
                            });
                            insureRejectRequest.start(this);
                        }
                    }
                },"您确定不为此用户补充延保信息吗？");
                break;
            case R.id.btn_submit:
                if (TextUtils.isEmpty(priceET.getText()
                        .toString().trim())) {
                    ViewUtils.showToast("请先输入价格!");
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
                    intent.putExtra("file_path", photo.getPath());
                    intent.putExtra("file_is_local", true);
                    startActivityForResult(intent, Constants.CHANGE_PIC);
                } else {
                    selectorPhoto();
                }
                break;
        }
    }



    private void commit() {

        if (Account.user != null && imageUrl.getIsOK()==1) {
            commitRequest =
                    new InsureInfoCommitRequest(guaranteeInfo.getId(), imageUrl.getOriginImageUrl(), priceET
                            .getText()
                            .toString().trim(), Account.user.getShopId(), imageUrl.getThumImageUrl());
            commitRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                @Override
                public void onOK() {
                    ViewUtils.showToast("延保订单提交成功!");
                    if (loadingDialog != null) {
                        loadingDialog.dismiss();
                    }

                    getActivity().finish();
                    Bundle bundle=new Bundle();
                    bundle.putDouble("price",_insurePrice);
                    bundle.putString("orderNo",guaranteeInfo.getOrderNo());
                    bundle.putByte("PayType",guaranteeInfo.getPayType());
                    bundle.putInt("Id", guaranteeInfo.getId());
                    FragmentListAct.start("完成",FragmentListAct.PAGE_GUARANTEE_PAY,bundle,null);
                }

                @Override
                public void onFail(int code) {
                    if (loadingDialog != null) {
                        loadingDialog.dismiss();
                    }
                    ViewUtils.showToast("延保订单提交失败!");
                }
            });
            commitRequest.start(this);
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
                                        100,100);
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
