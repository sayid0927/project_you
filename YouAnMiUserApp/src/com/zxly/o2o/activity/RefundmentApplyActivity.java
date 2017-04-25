package com.zxly.o2o.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import com.zxly.o2o.adapter.GridImageWithDeleteAdapter;
import com.zxly.o2o.dialog.GetPictureDialog;
import com.zxly.o2o.model.RefundResonVO;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest.ResponseStateListener;
import com.zxly.o2o.request.FileUploadRequest;
import com.zxly.o2o.request.RefundReasonRequest;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.ParameCallBackById;
import com.zxly.o2o.util.PicTools;
import com.zxly.o2o.util.ViewUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Benjamin on 2015/5/20.
 */
public class RefundmentApplyActivity extends BasicAct implements View.OnClickListener {
    private GridImageWithDeleteAdapter gridImageAdapter;
    private long refundId;
    private String orderNo;
    private EditText refundmentSumedit, remarkEdit;
    Map<String, Object> sendParams = new HashMap<String, Object>();
    private int refundType;  //退款类型
    private float realPrice;
    private Spinner refundmentResonSp;
    private int refundReson;
    private static ParameCallBack _parameCallBack;
    protected Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refundment_apply_layout);
        refundId = getIntent().getLongExtra("refundId", -1);  //退款单id
        orderNo = getIntent().getStringExtra("orderNo");  //订单id
        initView();
        loadRefundReason();
    }

    private void loadRefundReason() {
        final RefundReasonRequest refundReason = new RefundReasonRequest();
        refundReason.setOnResponseStateListener(new ResponseStateListener() {
            @Override
            public void onOK() {
                if (refundReason.refundResonVOs != null) {
                    ArrayList<String> arrayList = new ArrayList<String>();

                    for (RefundResonVO refundResonVO : refundReason.refundResonVOs) {
                        arrayList.add(refundResonVO.getName());
                    }
                    ArrayAdapter adapter = new ArrayAdapter<String>(RefundmentApplyActivity.this,
                            android.R.layout.simple_spinner_item,
                            arrayList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    refundmentResonSp.setAdapter(adapter);
                    refundmentResonSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            refundReson = refundReason.refundResonVOs.get(position).getCode();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onFail(int code) {

            }
        });
        refundReason.start();
    }


    private void initView() {
        setRefundType();  //设置退款类型

        realPrice = getIntent().getFloatExtra("price", 0);

        ((TextView) findViewById(R.id.refund_limit_price))
                .setText(new StringBuffer(
                        ((TextView) findViewById(R.id.refund_limit_price)).getText())
                        .append(realPrice));
        refundmentSumedit = (EditText) findViewById(R.id.refundment_sum_edit);
        remarkEdit = (EditText) findViewById(R.id.refundment_remark_edit);

        findViewById(R.id.submit_btn).setOnClickListener(this);

        setSendParams();  //设置参数

        refundmentResonSp = (Spinner) findViewById(R.id.refundment_reason_spinner);
    }

    private void setSendParams() {
        if (refundId > 0) {
            sendParams.put("id", refundId + "");
            ((Button) findViewById(R.id.submit_btn)).setText("确认修改");
            setUpActionBar(getActionBar(), "退款申请修改");
        } else {
            setUpActionBar(getActionBar(), "申请退款");
            sendParams.put("itemId", getIntent().getStringExtra("itemId"));
            sendParams.put("orderNo", orderNo);
            sendParams.put("price", String.valueOf(realPrice));
            sendParams.put("refundType", String.valueOf(refundType));
            sendParams.put("pcs", String.valueOf(getIntent().getIntExtra("pcs", -1)));
            sendParams.put("productType", String.valueOf(getIntent().getIntExtra("productType", -1)));
        }
    }

    private void setRefundType() {
        refundType = getIntent().getIntExtra("refundType", -1);
        if (refundId > 0) {  //从退款详情界面跳转过来的
            switch (refundType) {
                case Constants.REFUND_ONLYL:
                    ((TextView) findViewById(R.id.refundment_type)).setText("仅退款");
                    break;
                case Constants.REFUND_BOTH:
                    showCamera();
                    ((TextView) findViewById(R.id.refundment_type)).setText("退货退款");
                    break;
            }
        } else {  //从非退款详情界面跳转过来的
            if (refundType == Constants.ORDER_WAIT_CONFIRM) {  //交易成功表示要退货
                ((TextView) findViewById(R.id.refundment_type)).setText("退货退款");
                showCamera();
                refundType = 2;
            } else if (refundType != -1) {
                ((TextView) findViewById(R.id.refundment_type)).setText("仅退款");
                refundType = 1;
            } else {
                ViewUtils.showToast("退款类型异常");
            }
        }
    }

    private void showCamera() {
        findViewById(R.id.refundment_camera_layout).setVisibility(View.VISIBLE);
        GridView mGridView = (GridView) findViewById(R.id.refundment_pic_gridview);
        Button getPicBtn = (Button) findViewById(R.id.get_refundment_pic_btn);
        getPicBtn.setOnClickListener(this);
        gridImageAdapter = new GridImageWithDeleteAdapter(this);
        mGridView.setAdapter(gridImageAdapter);
    }

    private void setUpActionBar(final ActionBar actionBar, String title) {
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(R.layout.tag_title);
            ((TextView) actionBar.getCustomView().findViewById(R.id.tag_title_title_name))
                    .setText(title);
            findViewById(R.id.tag_title_btn_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_btn:
                if (!checkSubmitItem()) {
                    return;
                }
                if ((Float.valueOf(refundmentSumedit.getText().toString().replaceAll(" ", ""))) > realPrice) {
                    ViewUtils.showToast("退款金额不能超过商品金额");
                    return;
                }
                Map<String, File> files = new HashMap<String, File>();
                if (gridImageAdapter != null) {
                    for (int i = 0; i < gridImageAdapter.getContent().size(); i++) {
                        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
                        bmpFactoryOptions.inJustDecodeBounds = true;
                        MyCircleThirdActAssi.photoBitmap = BitmapFactory.decodeFile((String) gridImageAdapter.getContent().get(i), bmpFactoryOptions);
                        bmpFactoryOptions.inJustDecodeBounds = false;
                        long outHeight = bmpFactoryOptions.outHeight;
                        while (outHeight > 4000) {
                            bmpFactoryOptions.inSampleSize += 2;  //图片大小为原来的1/2倍
                            outHeight = outHeight / 2;
                        }
                        MyCircleThirdActAssi.photoBitmap = BitmapFactory.decodeFile((String) gridImageAdapter.getContent().get(i), bmpFactoryOptions);
                        files.put("file" + i, PicTools.savePicAndReturn(false, String.valueOf(i)));
                    }
                    //用完后回收
                    if(MyCircleThirdActAssi.photoBitmap!=null) {
                        MyCircleThirdActAssi.photoBitmap.recycle();
                        MyCircleThirdActAssi.photoBitmap = null;
                    }
                }
                sendParams.put("refundPrice", refundmentSumedit.getText().toString().replaceAll(" ", ""));
                sendParams.put("refundRemark", remarkEdit.getText().toString());
                sendParams.put("refundReason", String.valueOf(refundReson));
                new FileUploadRequest(files, sendParams,
                        refundId < 1 ? "/order/applyRefund" : "/order/modifyApplyRefund",
                        mMainHandler).startUpload();
                break;
            case R.id.get_refundment_pic_btn:
                if (gridImageAdapter.getCount() == 5) {
                    ViewUtils.showToast("图片不能超过5张");
                } else {
                    Intent  intent = new Intent(this, AlbumActivity.class);
                    intent.putExtra("dataList", (ArrayList)gridImageAdapter.getContent());
                    AlbumActivity
                            .start(this, intent, new ParameCallBackById() {
                                @Override
                                public void onCall(int id, Object object) {
                                    gridImageAdapter.clear();
                                    gridImageAdapter.addItem(((ArrayList<String>) object), false);
                                    gridImageAdapter.notifyDataSetChanged();
                                }
                            });

//                    new GetPictureDialog(false).show(this.mMainHandler);
                }
                break;
        }
    }

    private boolean checkSubmitItem() {
        if (remarkEdit.getText().toString().equals("")) {
            ViewUtils.showToast("请输入备注");
            return false;
        } else if (refundmentSumedit
                .getText().toString().equals("")) {
            ViewUtils.showToast("请输入退款金额");
            return false;
        }
        return true;
    }

    public static void start(Activity curAct, String itemId, int orderType, int productType,
                             String orderNo, float totalPrice, int pcs, ParameCallBack parameCallBack) {
        _parameCallBack = parameCallBack;
        Intent intent = new Intent(curAct, RefundmentApplyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("itemId", itemId);
        bundle.putString("orderNo", orderNo);
        bundle.putInt("refundType", orderType);
        bundle.putInt("productType", productType);
        bundle.putInt("pcs", pcs);
        bundle.putFloat("price", totalPrice);
        intent.putExtras(bundle);
        ViewUtils.startActivity(intent, curAct);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        _parameCallBack = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.GET_PIC_FROM_CAMERA) {
            if (resultCode == RESULT_OK) {
                if (fileUri != null) {
                    gridImageAdapter.addItem(fileUri.getPath());
                    gridImageAdapter.notifyDataSetChanged();
                } else if(mMainHandler!=null){
                    mMainHandler.sendEmptyMessage(Constants.DELETE_PIC);
                }
            } else if (resultCode == RESULT_CANCELED&&mMainHandler!=null) {
                mMainHandler.sendEmptyMessage(Constants.DELETE_PIC);
            } else if (resultCode == Constants.GET_PIC_FROM_CAMERA&&mMainHandler!=null) {  //触发拍照
                mMainHandler.sendEmptyMessage(Constants.GET_PIC_FROM_CAMERA);
            } else {
                if(mMainHandler!=null)
                    mMainHandler.sendEmptyMessage(Constants.DELETE_PIC);
                ViewUtils.showToast("图片获取失败");
            }





//            if (resultCode == RESULT_OK) {
//                ViewUtils.showToast("照片保存成功");
//                //                dataList.add(photoFromCamera.getAbsolutePath());
//                //只查询jpeg的图片
//                Cursor mCursor = PicTools.getCursorfromQuery(this);
//                mCursor.moveToLast();
//                gridImageAdapter.addItem(mCursor.getString(0), true);
//                mCursor.close();
//                gridImageAdapter.notifyDataSetChanged();
//            } else if (resultCode == RESULT_CANCELED) {
//                ViewUtils.showToast("拍照已取消");
//            } else {
//                ViewUtils.showToast("图片获取失败");
//            }
        } else if (resultCode == RESULT_OK && data != null) {
            gridImageAdapter.clear();
            gridImageAdapter.addItem(data.getStringArrayListExtra("dataList"), true);
            gridImageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (isFinishing()) {
            return;
        }

        switch (msg.what) {
            case Constants.GET_PIC_FROM_CELLPHONE:
                Intent intent = new Intent(this, AlbumActivity.class);
                //                Bundle bundle = new Bundle();
                //                bundle.putA("dataList", gridImageAdapter.getContent());
                //                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
                break;

            case Constants.GET_PIC_FROM_CAMERA:
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = Uri.fromFile(PicTools.getOutputPhotoFile(true, ""));
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(cameraIntent, Constants.GET_PIC_FROM_CAMERA);
//                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //                photoFromCamera = PicTools.getOutputPhotoFile(true, "");
                //                cameraUri = Uri.fromFile(photoFromCamera);
                //                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
//                startActivityForResult(cameraIntent, Constants.GET_PIC_FROM_CAMERA);
                break;
            case Constants.MSG_SUCCEED:
                Intent goToListIntent = new Intent();
                goToListIntent.setClass(this, RefundmentListActivity.class);
                if (refundId != -1) {
                    goToListIntent.putExtra("ismodify", refundmentSumedit.getText().toString().replaceAll(" ", ""));
                }

                //回调操作成功
                if (_parameCallBack != null) {
                    Map<String, Object> result = new HashMap<String, Object>();
                    result.put(Constants.ORDER_OPERATE_TYPE, Constants.ORDER_OPERATE_REFUND_APPLY);
                    result.put(Constants.ORDER_NO, orderNo);
                    _parameCallBack.onCall(result);
                }

                startActivity(goToListIntent);
                finish();


                break;
            case Constants.MSG_FAILED:
                ViewUtils.showToast((String) msg.obj);
                break;
        }
    }


}
