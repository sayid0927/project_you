/*
 * 文件名：GetPictureDialog.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： GetPictureDialog.java
 * 修改人：Administrator
 * 修改时间：2015-1-16
 * 修改内容：新增
 */
package com.easemob.easeui.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.R;
import com.easemob.easeui.EaseConstant;


/**
 * TODO 添加类的一句话简单描述。
 * <p/>
 * TODO 详细描述
 * <p/>
 * TODO 示例代码
 * <p/>
 * <pre>
 * </pre>
 *
 * @author huangbin
 * @version YIBA-O2O 2015-1-16
 * @since YIBA-O2O
 */
public class SelectMultiPushDialog extends EaseBaseDialog implements OnClickListener {
    private Handler handler;
    private TextView pushTxt, pushArticle, pushProduce, pushActivity;
    private int multiPushOrSend;
    private String commission;
    private boolean isRegistMembers;
    private String registerTime;

    public SelectMultiPushDialog(Context context) {
        super(context, Gravity.CENTER);
    }

    @Override
    public int getLayoutId() {

        return R.layout.ease_dialog_select_multi_push_type;
    }

    public void show(Handler mHandler) {
        this.handler = mHandler;
        super.show();
    }

    public void show(int multiPushOrSend, String commission, boolean isRegistMembers, String registerTime) {
        //推送消息不能发文本
        if (multiPushOrSend == HXConstant.MUL_PUSH) {
            pushProduce.setText("推送商品");
            pushArticle.setText("推送文章");
            pushTxt.setVisibility(View.GONE);
            pushActivity.setVisibility(View.GONE);
        }
        this.multiPushOrSend = multiPushOrSend;
        this.commission = commission;
        this.isRegistMembers = isRegistMembers;
        this.registerTime = registerTime;
        super.show();
    }

    @Override
    protected void initView() {
        // TODO Auto-generated method stub
        pushTxt = (TextView) findViewById(R.id.push_txt);
        pushActivity = (TextView)findViewById(R.id.push_activity);
        pushArticle = (TextView)findViewById(R.id.push_article);
        pushProduce = (TextView)findViewById(R.id.push_produce);


        pushTxt.setOnClickListener(this);
        pushActivity.setOnClickListener(this);
        pushArticle.setOnClickListener(this);
        pushProduce.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        dismiss();
        int pushType = 0;
        if (v == pushTxt) {
            pushType = HXConstant.PUSH_TXT;
        } else if (v == pushActivity) {
            pushType = HXConstant.PUSH_CAMPAIGN;

        } else if (v == pushProduce) {
            pushType = HXConstant.PUSH_SHOP_INFO;

        } else if (v == pushArticle) {
            pushType = HXConstant.PUSH_ARTICLE;

        }
        dismiss();
        if (EaseConstant.shopID < 0) {  //已登录
            EaseConstant.startEaseMultiPushActivity((Activity) context, pushType, multiPushOrSend, commission, isRegistMembers, registerTime);
        }
    }
}
