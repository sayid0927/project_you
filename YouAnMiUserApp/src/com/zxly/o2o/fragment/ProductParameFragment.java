package com.zxly.o2o.fragment;

import android.annotation.SuppressLint;
import android.widget.ListView;

import com.zxly.o2o.adapter.NewProductParamAdapter;
import com.zxly.o2o.model.ProductParam;
import com.zxly.o2o.o2o_user.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsnx on 2015/9/10.
 */
@SuppressLint("ValidFragment")
public class ProductParameFragment  extends BaseFragment {

    private List<ProductParam> productParamList = new ArrayList<ProductParam>();
    private NewProductParamAdapter productParamAdapter;
    public ProductParameFragment(){}
    private ListView paramListView;
    public ProductParameFragment(List<ProductParam> productParamList )
    {
        if(productParamList!=null)
        {
            this.productParamList.addAll(productParamList);
        }

    }

    public ListView getParamListView()
    {
        return paramListView;
    }
    @Override
    protected void initView() {
        paramListView= (ListView) findViewById(R.id.param_listview);
        productParamAdapter = new NewProductParamAdapter(this.getActivity());
        paramListView.setAdapter(productParamAdapter);
        productParamAdapter.addItem(productParamList, true);
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_parame;
    }
}
