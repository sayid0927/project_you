package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.DataCallBack;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kenwu on 2015/12/15.
 */
public class MonthAdapter extends ObjectAdapter implements View.OnClickListener {
    private int outYear;
    private int selectYear;
    private int selectMonth;
    private DataCallBack callBack;
    private Context context;
    public MonthAdapter(Context _context) {
        super(_context);
        outYear =StringUtil.getCurrentYear();
        selectYear= outYear;
        selectMonth=StringUtil.getCurrentMonth();
        this.context=_context;
    }

    public void setSelectYear(int selectYear){
        this.selectYear=selectYear;
    }

    public int getSelectYear() {
        return selectYear;
    }

    public void setSelectMonth(int selectMonth){
        this.selectMonth=selectMonth;
    }

    public int getSelectMonth() {
        return selectMonth;
    }

    public void setOutYear(int outYear) {
        this.outYear = outYear;
    }

    public int getOutYear() {
        return outYear;
    }

    public void setCallBack(DataCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public int getCount() {
        return 12;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflateConvertView();
            holder = new ViewHolder();
            holder.txtMonth = (TextView) convertView.findViewById(R.id.txt_month);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        int month=position+1;
        ViewUtils.setText(holder.txtMonth, (position+1) + "月");
        holder.txtMonth.setOnClickListener(this);
        holder.txtMonth.setTag((position+1));

        if(StringUtil.getCurrentYear()==outYear){
            if(month>StringUtil.getCurrentMonth()){
                holder.txtMonth.setBackgroundResource(R.color.transparent);
                holder.txtMonth.setTextColor(context.getResources().getColor(R.color.grey_aaaaaa));
                holder.txtMonth.setOnClickListener(null);
            }else {
                if(outYear ==selectYear&&month==selectMonth){
                    holder.txtMonth.setBackgroundResource(R.drawable.btn_select_normal);
                    holder.txtMonth.setTextColor(context.getResources().getColor(R.color.white));
                }else{
                    holder.txtMonth.setBackgroundResource(R.color.transparent);
                    holder.txtMonth.setTextColor(context.getResources().getColor(R.color.gray_333333));
                }
            }

        }else {
            if(outYear ==selectYear&&month==selectMonth){
                holder.txtMonth.setBackgroundResource(R.drawable.btn_select_normal);
                holder.txtMonth.setTextColor(context.getResources().getColor(R.color.white));
            }else{
                holder.txtMonth.setBackgroundResource(R.color.transparent);
                holder.txtMonth.setTextColor(context.getResources().getColor(R.color.gray_333333));
            }
        }

        return convertView;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_time;
    }

    @Override
    public void onClick(View v) {
        Map<String,Integer> data=new HashMap<String, Integer>();
        data.put("year", outYear);
        data.put("month", (Integer) v.getTag());
        if(callBack!=null)
            callBack.onCall(data);
    }

    class ViewHolder {
        TextView txtMonth;
    }

}