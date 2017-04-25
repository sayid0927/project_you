package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.zxly.o2o.model.ProductSKUParam;
import com.zxly.o2o.model.ProductSKUValue;
import com.zxly.o2o.model.Skus;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *     @author dsnx  @version 创建时间：2015-1-15 下午6:04:01    类说明: 
 */
public class ProductParamSelAdapter extends ObjectAdapter {
    private List<Skus> skuList = new ArrayList<Skus>();
    private String[] selSkuValue;
    private String[] selSkuName;
    private CallBack callBack;
    private Skus selSku;
    private int curItemId;
    private int selCount;
    private boolean isOpen;
    public ProductParamSelAdapter(Context _context, CallBack callBack) {
        super(_context);
        this.callBack = callBack;
    }

    public void setSkuList(List<Skus> skuList) {
        this.skuList = skuList;
    }

    public void setSelSku(Skus selSku) {
        this.selSku = selSku;
        this.curItemId=-1;
        this.isOpen=true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflateConvertView();
            holder = new ViewHolder();
            holder.gridView = (GridView) convertView
                    .findViewById(R.id.gridView);
            holder.productName = (TextView) convertView
                    .findViewById(R.id.productName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ProductSKUParam pp = (ProductSKUParam) getItem(position);
        ParamAdapter adapter = (ParamAdapter) holder.gridView.getAdapter();
        if (adapter == null) {
            adapter = new ParamAdapter(context, position, pp.getDisplayName());
            adapter.addItemArray(pp.getProductSKUValue());
            holder.gridView.setAdapter(adapter);
        }
        if(isOpen)
        {
            isOpen=false;
            if(this.selSku!=null)
            {
                selSkuValue=this.selSku.getParamComValues().split(",");
                selSkuName=this.selSku.getParamComNames().split(",");
                selCount=selSkuValue.length;

            }else
            {
                selSkuValue=null;
                selCount=0;
            }
        }

        handlerProductParam(position,pp.getProductSKUValue());
        adapter.notifyDataSetChanged();
        ViewUtils.setText(holder.productName, pp.getDisplayName());
        return convertView;
    }

    public Skus getSelSku() {

        if(selSkuValue==null)
        {
            return null;
        }
        int length=selSkuValue.length;
        int length1=length-1;
        StringBuilder strId=new StringBuilder();
        StringBuilder strName=new StringBuilder();
        for(int i=0;i<length;i++)
        {
            String paramValue=selSkuValue[i];
            ProductSKUParam pp = (ProductSKUParam) getItem(i);
            if(!StringUtil.isNull(paramValue))
            {
                if(i<length1)
                {
                    strId.append(paramValue).append(",");
                    strName.append(selSkuName[i]).append(",");

                }else
                {
                    strId.append(paramValue);
                    strName.append(selSkuName[i]);
                }
            }else
            {

                return null;
            }


        }
        String pv=strId.toString();
        for (Skus sku : skuList) {

            String paramValue=sku.getParamComValues();
            if(paramValue.equals(pv))
            {
                sku.setParamComNames(strName.toString());
                return sku;
            }
        }
        return null;
    }

    class ViewHolder {
        GridView gridView;
        TextView productName;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_product_param_sel;
    }

    class ParamAdapter extends ObjectAdapter {
        private int itemId;
        private ProductSKUValue selValue;
        private String paramName;

        public ParamAdapter(Context _context, int itemId, String paramName) {
            super(_context);
            this.paramName = paramName;
            this.itemId = itemId;
        }

        public int getItemId() {
            return itemId;
        }

        public String getParamName() {
            return paramName;
        }

        public ProductSKUValue getSelValue() {
            return selValue;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {
            ParamHolder holder;
            if (convertView == null) {
                convertView = inflateConvertView();
                holder = new ParamHolder();
                holder.txtName = (TextView) convertView
                        .findViewById(R.id.txt_combo_name);
                convertView.setTag(holder);
            } else {
                holder = (ParamHolder) convertView.getTag();
            }
            final ProductSKUValue value = (ProductSKUValue) getItem(position);
            ViewUtils.setText(holder.txtName, value.getDisplyName());
            holder.txtName.setEnabled(true);

            if(selSkuValue!=null)
            {
                if(value.getParamValue().equals(selSkuValue[itemId]))
                {
                    if(value.getType()!=1)
                    {
                        value.setType(1);
                    }
                }else
                {
                    if(curItemId==-1&&value.getType()==1)
                    {
                        value.setType(3);
                    }
                }

            }else
            {
                if(curItemId==-1)
                {
                    value.setType(3);
                }
            }
            switch (value.getType()) {
                case 1:
                    if(selValue==null)
                    {
                        selValue=value;
                    }
                    holder.txtName.setBackgroundResource(R.drawable.button_orange);
                    holder.txtName.setTextColor(context.getResources().getColor(R.color.white));
                    break;
                case 2:
                    holder.txtName
                            .setBackgroundResource(R.drawable.huise_bg);
                    holder.txtName.setTextColor(context.getResources().getColor(R.color.gray_999999));
                    holder.txtName.setEnabled(false);
                    break;
                case 3:
                    holder.txtName.setBackgroundResource(R.drawable.huise_bg);
                    holder.txtName.setTextColor(context.getResources().getColor(R.color.gray_707070));
                    break;
            }

            holder.txtName.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    curItemId=itemId;
                    if(selSkuValue==null)
                    {
                        selSkuValue=new String[ProductParamSelAdapter.this.getCount()];
                        selSkuName=new String[ProductParamSelAdapter.this.getCount()];
                    }
                    String paramValue=selSkuValue[itemId];
                    if(!StringUtil.isNull(paramValue))
                    {
                            if(paramValue.equals(value.getParamValue()))
                            {
                                selSkuValue[itemId]="";
                                value.setType(3);
                            selValue=null;
                            selCount--;
                        }else
                        {
                            selValue.setType(3);
                            selValue=value;
                            selSkuValue[itemId]=value.getParamValue();
                            selSkuName[itemId]=value.getDisplyName();
                            value.setType(1);
                        }
                    }else
                    {
                        selValue=value;
                        selSkuValue[itemId]=value.getParamValue();
                        selSkuName[itemId]=value.getDisplyName();
                        value.setType(1);
                        selCount++;
                    }
                    ProductParamSelAdapter.this.notifyDataSetChanged();
                    if(callBack!=null)
                    {
                        callBack.onCall();
                    }
                }
            });

            return convertView;
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_sku;
        }

        class ParamHolder {
            TextView txtName;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof ParamAdapter) {
                if (((ParamAdapter) o).itemId == this.itemId) {
                    return true;
                }
            }
            return false;
        }

    }
    /***
     * 处理参数相互影响关系
     * @param itemId
     * @param psv
     */
    private  void handlerProductParam(int itemId,ProductSKUValue[] psv)
    {
        if(selSkuValue==null||itemId==curItemId)
        {
            return;
        }
        int psvLength=psv.length;
        for(int i=0;i<psvLength;i++)
        {
            ProductSKUValue p=psv[i];
            int skuSize=skuList.size();
            boolean isEnabled=false;
            for(int j=0;j<skuSize;j++)
            {
                Skus sku=skuList.get(j);
                String key=sku.getParamComValues();
                String[] comdValue=key.split(",");
                String paramValue=comdValue[itemId];
                int kLength=selSkuValue.length;
                int mateSum=0;
                if(paramValue.equals(p.getParamValue()))
                {
                    for(int k=0;k<kLength;k++)
                    {
                        if(k==itemId)
                        {
                            continue;
                        }
                        String valValue=selSkuValue[k];

                        if(!StringUtil.isNull(valValue))
                        {
                            String pv=comdValue[k];
                            if(valValue.equals(pv))
                            {
                                mateSum++;
                            }

                        }
                    }
                    String curValue=selSkuValue[itemId];
                    int t;
                    if(!StringUtil.isNull(curValue))
                    {
                        t=selCount-1;
                    }else
                    {
                        t=selCount;
                    }
                    if(mateSum==t)
                    {
                        isEnabled=true;
                        if(p.getType()==2)
                        {
                            p.setType(3);
                        }

                        break;
                    }
                }


            }
            if(!isEnabled)
            {
                p.setType(2);
            }

        }

    }

}
