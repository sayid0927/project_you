package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxly.o2o.model.TaskInfo;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.ViewUtils;

/**
 * Created by wuchenhui on 2015/7/7.
 */
public class TaskInfoAdapter extends ObjectAdapter {


    public TaskInfoAdapter(Context _context) {
        super(_context);
    }

    private boolean isShowNotask;

    @Override
    public int getLayoutId() {
        if(DataUtil.listIsNull(getContent())){
            return R.layout.item_no_task;
        }

        return R.layout.item_my_task;
    }

    @Override
    public int getCount() {
        if(DataUtil.listIsNull(getContent())&&isShowNotask){
            return 1;
        }

        return super.getCount();
    }

    public void setIsShowNotask(boolean isShowNotask) {
        this.isShowNotask = isShowNotask;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        //后面把这个逻辑搬出去
        if(true){
            convertView=inflateConvertView();
            if(!DataUtil.listIsNull(getContent())){
                holder=new ViewHolder();

                holder.txtPromotionName = (TextView) convertView.findViewById(R.id.txt_promotion_name);
                holder.txtPromotionCount=(TextView) convertView.findViewById(R.id.txt_promotion_count);
                holder.txtPromotionTarget=(TextView) convertView.findViewById(R.id.txt_promotion_target);
                holder.txtUnit=(TextView) convertView.findViewById(R.id.txt_unit);
                holder.botLine=convertView.findViewById(R.id.line_bot);
                convertView.setTag(holder);
            }

        }else{
            if(!DataUtil.listIsNull(getContent()))
               holder= (ViewHolder) convertView.getTag();
        }

        if(!DataUtil.listIsNull(getContent())){

            if(position==(getCount()-1)){
                holder.botLine.setVisibility(View.GONE);
            }else{
                holder.botLine.setVisibility(View.VISIBLE);
            }
        }

      if(!DataUtil.listIsNull(getContent())){
          fillData((TaskInfo)getItem(position),holder);

      }else{
          convertView=inflateConvertView();
      }


        return convertView;
    }

    private void fillData(TaskInfo myTask,ViewHolder holder){

        ViewUtils.setText(holder.txtPromotionName, myTask.getName());
        ViewUtils.setText(holder.txtPromotionCount,myTask.getFinishValue());
        ViewUtils.setText(holder.txtPromotionTarget,"/"+myTask.getTargetValue());
        ViewUtils.setText(holder.txtUnit, myTask.getUnitName());
    }


    class ViewHolder{

        TextView txtPromotionName,txtPromotionTarget,txtPromotionCount,txtUnit;
        View botLine;
    }
}
