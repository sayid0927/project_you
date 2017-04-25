package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxly.o2o.activity.ProductCommissionDetailAct;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.application.Config;
import com.zxly.o2o.model.CommissionRecord;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.TimeUtil;
import com.zxly.o2o.util.ViewUtils;

/**
 * Created by kenwu on 2015/12/7.
 */
public class CommissionListAdapter extends ObjectAdapter implements View.OnClickListener {
    public CommissionListAdapter(Context _context) {
        super(_context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_commission_record;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder =null;
        if(convertView==null){
            convertView=inflateConvertView();
            holder=new ViewHolder();
            holder.imgCommissionStatus= (ImageView) convertView.findViewById(R.id.img_CommissionStatus);
            holder.txtOrderNo= (TextView) convertView.findViewById(R.id.txt_orderNo);
            holder.txtCommissionArriveDate= (TextView) convertView.findViewById(R.id.txt_commissionToArriveTime);
            holder.txtCommission= (TextView) convertView.findViewById(R.id.txt_commission);
            holder.txtCommissionStatus= (TextView) convertView.findViewById(R.id.txt_commissionStatus);
            holder.imgLine=convertView.findViewById(R.id.img_line);
            convertView.setOnClickListener(this);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

        fillData(holder, (CommissionRecord) getItem(position));

        return convertView;
    }


    private void fillData(ViewHolder holder,CommissionRecord commissionRecord){
        holder.commissionRecord=commissionRecord;
        //holder.txtCommissionArriveDate.setVisibility(View.GONE);
        if(commissionRecord.getStatus()==CommissionRecord.COMMISSION_STATUS_ARRIVED){
            holder.txtCommissionArriveDate.setVisibility(View.VISIBLE);
            holder.txtCommissionArriveDate.setText("到账时间 : "+ TimeUtil.formatTimeHHMMDD(commissionRecord.getReduceTimes() + Config.serverTime()));
            holder.imgCommissionStatus.setImageResource(R.drawable.icon_finish);
            holder.txtOrderNo.setTextColor(0xff333333);
            holder.txtCommission.setTextColor(0xff449900);
            holder.txtCommissionArriveDate.setTextColor(0xff999999);
            holder.txtCommissionStatus.setText("已到账");
            holder.txtCommissionStatus.setTextColor(0xff707070);
            holder.txtCommissionStatus.setBackgroundColor(0xfff8f7f1);
            ViewUtils.setGone(holder.imgLine);
        }else if(commissionRecord.getStatus()==CommissionRecord.COMMISSION_STATUS_WILLARRIVE){
            holder.txtCommissionArriveDate.setVisibility(View.VISIBLE);
            holder.txtCommissionArriveDate.setText("预计到账时间 : "+ TimeUtil.formatTimeHHMMDD(commissionRecord.getReduceTimes() + Config.serverTime()));
            holder.imgCommissionStatus.setImageResource(R.drawable.icon_daishouru);
            holder.txtOrderNo.setTextColor(0xff333333);
            holder.txtCommission.setTextColor(0xff999999);
            holder.txtCommissionArriveDate.setTextColor(0xff999999);
            holder.txtCommissionStatus.setText("待收入");
            holder.txtCommissionStatus.setTextColor(0xff707070);
            holder.txtCommissionStatus.setBackgroundColor(0xfff8f7f1);
            ViewUtils.setGone(holder.imgLine);
        }else if(commissionRecord.getStatus()==CommissionRecord.COMMISSION_STATUS_CANCLE){
            holder.txtCommissionArriveDate.setVisibility(View.GONE);
            holder.imgCommissionStatus.setImageResource(R.drawable.icon_shibai);
            holder.txtOrderNo.setTextColor(0xffcccccc);
            holder.txtCommission.setTextColor(0xffcccccc);
            holder.txtCommissionArriveDate.setTextColor(0xffcccccc);
            holder.txtCommissionStatus.setText("已退款");
            holder.txtCommissionStatus.setTextColor(0xffd6d6d6);
            holder.txtCommissionStatus.setBackgroundColor(0xfffafafa);
            ViewUtils.setVisible(holder.imgLine);
        }

        holder.txtOrderNo.setText("流水号 : "+commissionRecord.getNumberNo());
        holder.txtCommission.setText("+ "+StringUtil.getFormatPrice(commissionRecord.getCommission()));

    }

    @Override
    public void onClick(View v) {
        ViewHolder holder= (ViewHolder) v.getTag();
        if(holder.commissionRecord.getStatus()!=CommissionRecord.COMMISSION_STATUS_CANCLE){
            ProductCommissionDetailAct.start(AppController.getInstance().getTopAct(),holder.commissionRecord.getNumberNo(),holder.commissionRecord.getId());
        }

    }


    static class ViewHolder{
        ImageView imgCommissionStatus;
        TextView txtCommission;
        TextView txtOrderNo;
        TextView txtCommissionArriveDate;
        TextView txtCommissionStatus;
        View imgLine;
        CommissionRecord commissionRecord;
    }

}
