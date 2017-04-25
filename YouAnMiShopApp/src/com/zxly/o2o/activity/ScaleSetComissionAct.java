package com.zxly.o2o.activity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.model.Product;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.ScaleSetComissionRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsnx on 2015/7/9.
 */
public class ScaleSetComissionAct extends BasicAct implements View.OnClickListener,BaseRequest.ResponseStateListener{

    private PullToRefreshListView mListView;
    private EditText editScale;
    private View btnSave,btnBack;
    private static List<Product> productList=new ArrayList<Product>();
    private int type;//1，已设置佣金的商品 0,未设置佣金的商品
    private float rate;
    private ScaleSetCommissionProductAdapter adapter;
    private static ParameCallBack callBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_scale_comission);
        mListView = (PullToRefreshListView) findViewById(R.id.goods_listview);
        editScale= (EditText) findViewById(R.id.edit_scale);
        btnBack=findViewById(R.id.btn_back);
        type=getIntent().getIntExtra("type", 0);
        adapter=new ScaleSetCommissionProductAdapter(this);
        adapter.addItem(productList);
        mListView.setAdapter(adapter);
        mListView.setDivideHeight(0);
        ViewUtils.setRefreshText(mListView);
        btnSave=findViewById(R.id.btn_save);
        editScale.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0)
                {
                    try {
                         rate=Float.valueOf(s.toString());
                        adapter.notifyDataSetChanged();
                    }catch (NumberFormatException e)
                    {

                    }



                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnSave.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    public static void start(Activity curAct,List<Product> _productList,int type,ParameCallBack _callBack)
    {
        callBack=_callBack;
        if(!_productList.isEmpty())
        {
            productList=_productList;
            Intent it=new Intent();
            it.putExtra("type",type);
            it.setClass(curAct, ScaleSetComissionAct.class);
            ViewUtils.startActivity(it, curAct);
        }else
        {
            ViewUtils.showToast("请选择需要设置的商品");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        productList=null;
        callBack=null;
    }

    @Override
    public void onClick(View v) {
        if(v==btnSave)
        {
            String rate1=editScale.getText().toString();
            if(!StringUtil.isNull(rate1))
            {
                DecimalFormat df = new DecimalFormat("#0.0");
                try {
                     rate=Float.valueOf(rate1);
                    if(rate>0&&rate<=100)
                    {
                        ScaleSetComissionRequest ssr= new ScaleSetComissionRequest(productList,df.format(rate),type);
                        ssr.setOnResponseStateListener(this);
                        ssr.start();
                    }else
                    {
                        ViewUtils.showToast("请输入1-100之间的数字");
                    }

                }catch (NumberFormatException e)
                {
                    ViewUtils.showToast("请输入有效数字");
                }


            }else
            {
                ViewUtils.showToast("请输入佣金比例");
          }
        }else if(v==btnBack)
        {
            finish();
        }
    }

    @Override
    public void onOK() {
        ScaleSetComissionAct.this.finish();
        int size=productList.size();
        for(int i=0;i<size;i++)
        {
            Product product=productList.get(i);
            product.setRate(rate);
            product.setComission(product.getPrice()*rate/100);
        }
        callBack.onCall(rate);

    }

    @Override
    public void onFail(int code) {

    }
    public class ScaleSetCommissionProductAdapter extends ObjectAdapter {
        public ScaleSetCommissionProductAdapter(Context _context) {
            super(_context);
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_scale_set_comission_product;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null)
            {
                convertView=inflateConvertView();
                holder=new ViewHolder();
                holder.itemIcon= (NetworkImageView) convertView.findViewById(R.id.img_item_icon);
                holder.imgPlotFlag= (ImageView) convertView.findViewById(R.id.img_plot_flag);
                holder.txtName= (TextView) convertView.findViewById(R.id.txt_name);
                holder.txtPrice= (TextView) convertView.findViewById(R.id.txt_price);
                holder.txtComission= (TextView) convertView.findViewById(R.id.txt_comission);
                holder.txtOldPrice= (TextView) convertView.findViewById(R.id.txt_old_price);
                holder.txtScale= (TextView) convertView.findViewById(R.id.txt_scale);
                holder.lineBottom=convertView.findViewById(R.id.line_bottom);
                convertView.setTag(holder);
            }else
            {
                holder= (ViewHolder) convertView.getTag();
            }
            final Product product= (Product) getItem(position);

            holder.itemIcon.setDefaultImageResId(R.drawable.product_def);
            holder.itemIcon.setImageUrl(product.getHeadUrl(),
                    AppController.imageLoader);
            float curPrice = product.getPrice()-product.getPreference();
            ViewUtils.setText(holder.txtName, product.getName());
            if(product.getPreference()>0)
            {
                ViewUtils.strikeThruText(holder.txtOldPrice,  product.getPrice());
            }
            String commission;
            SpannableString ss1=null;
            if(rate>0)
            {
                commission="佣金：￥"+product.getPrice()*rate/100;
                ViewUtils.setText(holder.txtScale,rate+"%");
                ss1=new SpannableString(commission);
                ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#666666")), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#EB3434")), 3, commission.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }else
            {
                if(product.getComission()>0)
                {
                    commission="佣金：￥"+product.getComission();
                    ViewUtils.setText(holder.txtScale,product.getRate()+"%");
                    ss1=new SpannableString(commission);
                    ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#666666")), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#EB3434")), 3, commission.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }else
                {
                    commission="无佣金";
                    ViewUtils.setText(holder.txtScale,"");
                    ss1=new SpannableString(commission);
                    ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#666666")), 0,  commission.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }

            holder.txtComission.setText(ss1);
            ViewUtils.setTextPrice(holder.txtPrice, curPrice);
            if (position == getCount() - 1) {
                ViewUtils.setVisible(holder.lineBottom);

            } else {
                ViewUtils.setInvisible(holder.lineBottom);
            }

            return convertView;
        }
        class ViewHolder{
            NetworkImageView itemIcon;
            ImageView imgPlotFlag;
            TextView txtName,txtPrice,txtComission,txtOldPrice,txtScale;
            View lineBottom;
        }
    }

}
