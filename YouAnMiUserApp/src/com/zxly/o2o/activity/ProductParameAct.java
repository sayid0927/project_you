package com.zxly.o2o.activity;
import android.app.Activity;
import android.content.Intent;

import com.zxly.o2o.fragment.ProductDescFragment;
import com.zxly.o2o.fragment.ProductParameFragment;
import com.zxly.o2o.model.ProductParam;
import com.zxly.o2o.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by dsnx on 2015/9/10.
 */
public class ProductParameAct extends BaseViewPageAct {

    private static String htmlContent;
    private static List<ProductParam> productParamList = new ArrayList<ProductParam>();
    public static void start( Activity curAct,String _htmlContent,List<ProductParam> _productParamList) {
        Intent intent = new Intent(curAct, ProductParameAct.class);
        ViewUtils.startActivity(intent, curAct);
        htmlContent=_htmlContent;
        productParamList=_productParamList;
    }
    @Override
    protected void initView() {
        setUpActionBar("商品信息");
        fragments.add(new ProductDescFragment(htmlContent));
        fragments.add(new ProductParameFragment(productParamList));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        htmlContent=null;
        productParamList=null;
    }

    @Override
    protected String[] tabName() {
        return new String[]{"商品描述","商品参数"};
    }
}
