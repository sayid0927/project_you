package com.zxly.o2o.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.TextView;

import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.DateCallBack;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

public class DateSelectDialog implements View.OnClickListener {

    private Dialog dialog;
    private Context context;
    private View contentView;
    private GridView gridView;
    private TextView btnBeforeYear, btnNextYear;
    private TextView txtYear;
    private MonthAdapter adapter;
    DateCallBack callBack;
    private int year;
    private int currentYear, currentMonth;

    public DateSelectDialog(Context context, int currentYear, int currentMonth, DateCallBack callBack) {
        this.context = context;
        this.callBack = callBack;
        this.currentYear = currentYear;
        this.currentMonth = currentMonth;
        this.year = currentYear;
        context = AppController.getInstance().getTopAct();
        dialog = new Dialog(context, R.style.dialog);
        this.dialog.getWindow().setGravity(Gravity.BOTTOM);
        Window win = dialog.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        contentView = LayoutInflater.from(context).inflate(R.layout.dialog_date_select, null);
        txtYear = (TextView) contentView.findViewById(R.id.txt_year);
        ViewUtils.setText(txtYear, currentYear + "年");
        btnBeforeYear = (TextView) contentView.findViewById(R.id.btn_before_year);
        btnBeforeYear.setOnClickListener(this);
        btnNextYear = (TextView) contentView.findViewById(R.id.btn_next_year);
        btnNextYear.setOnClickListener(this);
        gridView = (GridView) contentView.findViewById(R.id.grid_view);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new MonthAdapter(context);
        gridView.setAdapter(adapter);
    }

    public void show() {
        if (dialog.isShowing())
            return;
        dialog.show();
        dialog.getWindow().setContentView(contentView);
    }

    public void dismiss() {
        this.dialog.cancel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_before_year:
                year--;
                ViewUtils.setText(txtYear, year);
                adapter.notifyDataSetChanged();
                break;
            case R.id.btn_next_year:
                if (year != StringUtil.getCurrentYear()) {
                    year++;
                    ViewUtils.setText(txtYear, year);
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

    class MonthAdapter extends ObjectAdapter {
        private int[] months = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

        public MonthAdapter(Context _context) {
            super(_context);
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
            ViewUtils.setText(holder.txtMonth, months[position] + "月");
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.onYearMonthCall(year, months[position]);
                }
            });
            if (year == StringUtil.getCurrentYear() && months[position] > StringUtil.getCurrentMonth()) {
                holder.txtMonth.setBackgroundColor(context.getResources().getColor(R.color.white));
                holder.txtMonth.setTextColor(context.getResources().getColor(R.color.grey_aaaaaa));
                holder.txtMonth.setTextSize(17);
                convertView.setClickable(false);
            } else {
                if (year == currentYear && position == currentMonth - 1) {
                    holder.txtMonth.setBackgroundResource(R.drawable.btn1_normal);
                    holder.txtMonth.setTextColor(context.getResources().getColor(R.color.red_dd2727));
                    holder.txtMonth.setTextSize(20);
                    convertView.setClickable(false);
                } else {
                    holder.txtMonth.setBackgroundColor(context.getResources().getColor(R.color.white));
                    holder.txtMonth.setTextColor(context.getResources().getColor(R.color.grey_666666));
                    holder.txtMonth.setTextSize(17);
                    convertView.setClickable(true);
                }
            }
            return convertView;
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_month;
        }

        class ViewHolder {
            TextView txtMonth;
        }

    }

}