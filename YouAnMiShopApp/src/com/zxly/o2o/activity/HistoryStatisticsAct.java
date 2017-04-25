package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.zxly.o2o.adapter.TaskInfoAdapter;
import com.zxly.o2o.dialog.DateSelectDialog;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.HistoryStatisticsRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.DateCallBack;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

/**
 * Created by dsnx on 2015/7/13.
 */
public class HistoryStatisticsAct extends BasicAct implements View.OnClickListener{
    private View btnBack,btnMore;
    private ListView mListView;
    private LoadingView loadingview;
    private View btnBeforeMonth, btnNextMonth;
    private TextView txtMonth;
    private DateSelectDialog  dateSelectDialog;
    private TaskInfoAdapter taskInfoAdapter;
    private int selYear,selMonth,curYear,curMonth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_history_statistics);
        btnBack=findViewById(R.id.btn_back);
        loadingview = (LoadingView) findViewById(R.id.view_loading);
        btnBeforeMonth =findViewById(R.id.btn_before_month);
        btnNextMonth =findViewById(R.id.btn_next_month);
        txtMonth= (TextView) findViewById(R.id.txt_month);
        btnMore=findViewById(R.id.btn_more);
        mListView = (ListView) findViewById(R.id.history_listview);
        taskInfoAdapter=new TaskInfoAdapter(this);
        mListView.setAdapter(taskInfoAdapter);
        mListView.setDividerHeight(0);
        btnBack.setOnClickListener(this);
        btnMore.setOnClickListener(this);
        btnBeforeMonth.setOnClickListener(this);
        btnNextMonth.setOnClickListener(this);
        selMonth = StringUtil.getCurrentMonth();
        selYear = StringUtil.getCurrentYear();
        curYear=selYear;curMonth=selMonth;
        txtMonth.setText(selMonth+"月");
        loadData(StringUtil.getCurrentYear() + "-" + StringUtil.getCurrentMonth());
    }
    public static void start(Activity curAct)
    {
        Intent intent=new Intent();
        intent.setClass(curAct, HistoryStatisticsAct.class);
        ViewUtils.startActivity(intent, curAct);
    }
    private void loadData(String date)
    {
        final HistoryStatisticsRequest  hsRequest=new HistoryStatisticsRequest(date);
        hsRequest.setTag(this);
        hsRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                txtMonth.setText(selMonth+"月");
                boolean isEmpty = hsRequest.getTaskInfos().isEmpty();
                if (isEmpty) {
                    loadingview.onDataEmpty("暂无历史提供统计");
                } else {
                    ViewUtils.setVisible(mListView);
                    taskInfoAdapter.clear();
                    taskInfoAdapter.addItem(hsRequest.getTaskInfos(),true);
                    loadingview.onLoadingComplete();
                }
            }

            @Override
            public void onFail(int code) {
                loadingview.onLoadingFail();
            }
        });
       // loadingview.startLoading();
        hsRequest.start();
    }

    @Override
    public void onClick(View v) {
        if(v==btnBack)
        {
            finish();
        }else if(v==btnMore)
        {
            if(dateSelectDialog==null)
            {
                dateSelectDialog=new DateSelectDialog(HistoryStatisticsAct.this,selYear,selMonth,new DateCallBack() {
                    @Override
                    public void onYearMonthCall(int year, int month) {
                        loadData(year+"-"+month);
                        selYear=year;
                        selMonth=month;

                    }
                });
            }
            dateSelectDialog.show();
        }else  if(v==btnBeforeMonth)
        {
                selMonth--;
            if(selMonth<=0)
            {
                selYear--;
                selMonth=12;
            }
            loadData(selYear+"-"+selMonth);
        }else if(v==btnNextMonth)
        {
            if(selYear<curYear)
            {
                selMonth++;
                if(selMonth>12)
                {
                    selYear++;
                    selMonth=1;
                }
                loadData(selYear+"-"+selMonth);
            }else
            {
                if(selMonth<curMonth)
                {
                    selMonth++;
                    loadData(selYear+"-"+selMonth);
                }else
                {
                    ViewUtils.showToast("未来不可以预知,请勿选择!");
                }
            }


        }
    }
}
