package com.zxly.o2o.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxly.o2o.adapter.MonthAdapter;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.DataCallBack;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.UmengUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.ExpandView;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by kenwu on 2015/12/16.
 */
public class DateListFragment extends BaseFragment implements DataCallBack,View.OnClickListener {

    private ImageView btnPreYear,btnNextYear;
    private TextView txtCurDate;
    private TextView txtPreMonth;
    private TextView txtNextMonth;
    private ExpandView viewTime ;
    ImageView imgTurn;
    private TextView txtYear;
    GridView mGridView;
    MonthAdapter monthAdapter;


    @Override
    protected void initView(Bundle bundle) {
        txtCurDate= (TextView) findViewById(R.id.btn_cur_date);
        txtPreMonth= (TextView) findViewById(R.id.btn_pre_month);
        txtNextMonth=(TextView) findViewById(R.id.btn_next_month);
        viewTime= (ExpandView) findViewById(R.id.view_time);

        imgTurn= (ImageView) findViewById(R.id.img_turn);

        View content=getActivity().getLayoutInflater().inflate(R.layout.layout_time, null);
        btnPreYear=(ImageView)content.findViewById(R.id.btn_pre_year);
        btnNextYear=(ImageView)content.findViewById(R.id.btn_next_year);
        txtYear=(TextView) content.findViewById(R.id.txt_year);
        txtYear.setText(StringUtil.getCurrentYear() + "年");
        viewTime.setContentView(content);

        mGridView= (GridView) content.findViewById(R.id.gridview);
        monthAdapter=new MonthAdapter(getActivity());
        monthAdapter.setCallBack(this);
        mGridView.setAdapter(monthAdapter);

         btnPreYear.setOnClickListener(this);
        btnNextYear.setOnClickListener(this);
        txtNextMonth.setOnClickListener(this);
        txtPreMonth.setOnClickListener(this);
        txtCurDate.setOnClickListener(this);
        viewTime.setOnClickListener(this);
        setNextMonthStatus();
        txtCurDate.setText(monthAdapter.getSelectYear() + "年" + monthAdapter.getSelectMonth() + "月");
        if(monthAdapter.getOutYear() == StringUtil.getCurrentYear()) {
            btnNextYear.setImageResource(R.drawable.turn_time_right_disable);
        }
    }


    @Override
    protected void initView() {
    }


    @Override
    protected int layoutId() {
        return R.layout.layout_date_list;
    }


    private void turnToPreYear(){
        monthAdapter.setSelectYear(monthAdapter.getSelectYear() - 1);
        monthAdapter.setSelectMonth(12);
        txtYear.setText(monthAdapter.getSelectYear() + "年");
        monthAdapter.setOutYear(monthAdapter.getSelectYear());
    }

    private void turnToNextYear(){
        //设置内部选择的年份
        monthAdapter.setSelectYear(monthAdapter.getSelectYear()+1);
        monthAdapter.setSelectMonth(1);

        //设置外部显示年份
        monthAdapter.setOutYear(monthAdapter.getSelectYear());
        txtYear.setText(monthAdapter.getOutYear() + "年");
        monthAdapter.notifyDataSetChanged();
    }

    private void turnToPreMonth(){
        int month=monthAdapter.getSelectMonth()-1;
        if(month<=0){
            //前一年
            turnToPreYear();
        }else{
            monthAdapter.setSelectMonth(monthAdapter.getSelectMonth()-1);
        }

        txtCurDate.setText(monthAdapter.getSelectYear() + "年" + monthAdapter.getSelectMonth() + "月");
        monthAdapter.notifyDataSetChanged();

        Map<String,Integer> data=new HashMap<String, Integer>();
        data.put("year", monthAdapter.getSelectYear());
        data.put("month", monthAdapter.getSelectMonth());
        onCall(data);
    }

    private void turnToNextMonth(){
        int month=monthAdapter.getSelectMonth()+1;
        if(month>12){
            turnToNextYear();
        }else{
            monthAdapter.setSelectMonth(month);
            monthAdapter.notifyDataSetChanged();
            txtCurDate.setText(monthAdapter.getSelectYear() + "年" + monthAdapter.getSelectMonth() + "月");
        }

        Map<String,Integer> data=new HashMap<String, Integer>();
        data.put("year", monthAdapter.getSelectYear());
        data.put("month",monthAdapter.getSelectMonth());
        onCall(data);
    }

    private boolean hasNextMonth(){
        //！！控制当前选择年份不能大于服务器年份
        if(monthAdapter.getSelectYear()==StringUtil.getCurrentYear()){
            if(monthAdapter.getSelectMonth()<StringUtil.getCurrentMonth()){
                return true;
            }
            return false;
        }else{
            return true;
        }
    }

    private boolean hassNextYear(){
        if((monthAdapter.getSelectYear()+1)> StringUtil.getCurrentYear()){
            ViewUtils.showToast("不能大于当前年份!");
            return false;
        }
        return true;
    }

    private void setNextMonthStatus(){
        if(!hasNextMonth()){
            txtNextMonth.setTextColor(0xffaaaaaa);
            txtNextMonth.setOnClickListener(null);
        }else{
            txtNextMonth.setTextColor(0xff333333);
            txtNextMonth.setOnClickListener(this);
        }
    }

    private void openDateChoose(){
        if(!viewTime.isExpand()){
            viewTime.expand();
            imgTurn.setImageResource(R.drawable.btn_turn_press);
        }

    }


    private void closeDateChoose(){
        if(viewTime.isExpand()){
            viewTime.collapse();
            imgTurn.setImageResource(R.drawable.btn_turn_normal);
        }
    }


    @Override
    public void onCall(Object data) {
        Map<String,Integer> result= (Map<String, Integer>) data;
        int year=result.get("year");
        int month=result.get("month");
        monthAdapter.setOutYear(year);
        monthAdapter.setSelectYear(year);
        monthAdapter.setSelectMonth(month);

        txtCurDate.setText(monthAdapter.getSelectYear() + "年" + monthAdapter.getSelectMonth() + "月");
        monthAdapter.notifyDataSetChanged();
        setNextMonthStatus();
        closeDateChoose();
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_pre_year:
                monthAdapter.setOutYear(monthAdapter.getOutYear() - 1);
                txtYear.setText(monthAdapter.getOutYear() + "年");
                monthAdapter.notifyDataSetChanged();
                if(monthAdapter.getOutYear() <  StringUtil.getCurrentYear()){
                    btnNextYear.setImageResource(R.drawable.turn_time_right);
                }
                break;

            case R.id.btn_next_year:
                if((monthAdapter.getOutYear()+1)> StringUtil.getCurrentYear()){
//                    ViewUtils.showToast("不能大于当前年份!");
                }else {
                    btnNextYear.setImageResource(R.drawable.turn_time_right);
                    monthAdapter.setOutYear(monthAdapter.getOutYear() + 1);
                    txtYear.setText(monthAdapter.getOutYear() + "年");
                    monthAdapter.notifyDataSetChanged();
                    if(monthAdapter.getOutYear() == StringUtil.getCurrentYear()) {
                        btnNextYear.setImageResource(R.drawable.turn_time_right_disable);
                    }
                }
                break;

            case R.id.btn_pre_month:
                turnToPreMonth();
                setNextMonthStatus();
                closeDateChoose();

                UmengUtil.onEvent(getActivity(),new UmengUtil().MANAGE_LASTMONTH_CLICK,null);
                break;

            case R.id.btn_next_month:
                turnToNextMonth();
                setNextMonthStatus();
                closeDateChoose();
                UmengUtil.onEvent(getActivity(),new UmengUtil().MANAGE_NEXTMONTH_CLICK,null);
                break;


            case R.id.btn_cur_date:
                if(!viewTime.isExpand()) {
                    openDateChoose();
                } else {
                    closeDateChoose();
                }

                UmengUtil.onEvent(getActivity(),new UmengUtil().MANAGE_MONTH_CLICK,null);
                break;

            case R.id.view_time:
                closeDateChoose();
                break;

            default:
                break;
        }

    }
}
