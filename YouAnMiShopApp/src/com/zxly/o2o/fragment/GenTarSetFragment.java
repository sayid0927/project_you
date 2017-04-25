package com.zxly.o2o.fragment;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxly.o2o.model.PostInfo;
import com.zxly.o2o.model.PostTask;
import com.zxly.o2o.request.SetQuotaRequest;
import com.zxly.o2o.shop.R;

import java.util.Iterator;

/**
 * Created by dsnx on 2015/7/7.
 */
@SuppressLint("ValidFragment")
public class GenTarSetFragment extends BaseFragment implements View.OnClickListener {

    private final int artGenTarget = 4;//文章推广
    private final int proGenClickTarget = 2;//商品点击
    private final int proGenTradeTarget = 3;//商品交易
    private final int shopGenTarget = 1;//本店推广
    private EditText editArtGenTar, editProGenTar, editProBuyTar, editShopGenTar;
    private TextView cbxAgree;
    private ImageView btnSel1, btnSel2;
    private PostInfo postInfo;
    private int nextContinue = 2;//1:指标适用于下月  2：不适用
    private int proSelType;//2：商品点击  3：商品交易


    public GenTarSetFragment(PostInfo postInfo) {
        this.postInfo = postInfo;
    }

    @Override
    protected void initView() {
        editArtGenTar = (EditText) findViewById(R.id.edit_art_gen_tar);
        editProGenTar = (EditText) findViewById(R.id.edit_pro_gen_tar);
        editProBuyTar = (EditText) findViewById(R.id.edit_pro_buy_tar);
        editShopGenTar = (EditText) findViewById(R.id.edit_shop_gen_tar);
        cbxAgree = (TextView) findViewById(R.id.cbx_agree);
        btnSel1 = (ImageView) findViewById(R.id.btn_sel1);
        btnSel2 = (ImageView) findViewById(R.id.btn_sel2);

        setTargetText(artGenTarget, editArtGenTar);
        setTargetText(proGenClickTarget, editProGenTar);
        setTargetText(proGenTradeTarget, editProBuyTar);
        setTargetText(shopGenTarget, editShopGenTar);

        if (postInfo.isSave()) {
            editArtGenTar.setEnabled(false);
            editProGenTar.setEnabled(false);
            editProBuyTar.setEnabled(false);
            editShopGenTar.setEnabled(false);
            btnSel1.setEnabled(false);
            btnSel2.setEnabled(false);
            cbxAgree.setEnabled(false);
        }
        btnSel1.setOnClickListener(this);
        btnSel2.setOnClickListener(this);
        cbxAgree.setOnClickListener(this);
    }

    public void reset() {
        postInfo.setSave(false);
        editArtGenTar.setEnabled(true);
        editProGenTar.setEnabled(true);
        editProBuyTar.setEnabled(true);
        editShopGenTar.setEnabled(true);
        cbxAgree.setEnabled(true);
        btnSel1.setEnabled(true);
        btnSel2.setEnabled(true);
    }

    public void save() {
        String artGenTar = editArtGenTar.getText().toString();
        String proGenTar = editProGenTar.getText().toString();
        String proBuyTar = editProBuyTar.getText().toString();
        String shopGenTar = editShopGenTar.getText().toString();

        setTargetValue(artGenTarget,artGenTar);
        switch (proSelType)
        {
            case proGenClickTarget://商品点击
                setTargetValue(proGenClickTarget, proGenTar);
                break;
            case proGenTradeTarget://商品交易
                setTargetValue(proGenTradeTarget, proBuyTar);
                break;

        }

        setTargetValue(shopGenTarget, shopGenTar);
        SetQuotaRequest sqRequest = new SetQuotaRequest(postInfo, nextContinue);
        sqRequest.start();
    }

    private void setTargetValue(int type,String target)
    {
        boolean isContains=false;

        Iterator<PostTask> iterator=postInfo.tasks.iterator();
        while (iterator.hasNext())
        {
            PostTask pt=iterator.next();
            if(pt.type==type)
            {
                isContains=true;
                pt.target=target;
            }
            switch (type)
            {
                case proGenClickTarget:
                    if(pt.type==proGenTradeTarget)
                    {
                        iterator.remove();
                    }
                    break;
                case proGenTradeTarget:
                    if(pt.type==proGenClickTarget)
                    {
                        iterator.remove();
                    }
                    break;
            }

        }

        if(!isContains)
        {

            PostTask pt=new PostTask(type,target,null);
            postInfo.tasks.add(pt);
        }
    }

    private void setTargetText(int type, EditText editText)
    {
        for(PostTask pt:postInfo.tasks)
        {
            if(pt.type==type)
            {
                switch (type)
                {
                    case proGenClickTarget:
                        proSelType = proGenClickTarget;
                        btnSel1.setSelected(true);
                        break;
                    case proGenTradeTarget:
                        proSelType = proGenTradeTarget;
                        btnSel2.setSelected(true);
                        break;
                }
                editText.setText(pt.target);
            }
        }
    }

    @Override
    protected int layoutId() {
        return R.layout.win_post_tar_set;
    }

    @Override
    public void onClick(View v) {
        if (btnSel2 == v) {
            proSelType = 3;
            btnSel2.setSelected(true);
            btnSel1.setSelected(false);
        } else if (btnSel1 == v) {
            proSelType = 2;
            btnSel2.setSelected(false);
            btnSel1.setSelected(true);
        } else if (cbxAgree == v) {
            if (nextContinue == 2) {
                Drawable drawable = getResources().getDrawable(R.drawable.danxuan2);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                nextContinue = 1;
                cbxAgree.setCompoundDrawables(drawable, null, null, null);
            } else {
                nextContinue = 2;
                Drawable drawable = getResources().getDrawable(R.drawable.danxuan1);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                cbxAgree.setCompoundDrawables(drawable, null, null, null);
            }
        }
    }
}
