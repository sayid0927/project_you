package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxly.o2o.model.YieldDetail;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.TimeUtil;
import com.zxly.o2o.util.ViewUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dsnx on 2015/12/15.
 */
public class YieldDetailAdapter extends BaseExpandableListAdapter {
    protected Map<Object, List<Object>> content = new LinkedHashMap<Object, List<Object>>();
    protected Context context;

    public YieldDetailAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getGroupCount() {

        return content.keySet().size();
    }

    public void clear() {
        content.clear();
    }

    public void addContent(Map<Object, List<Object>> _content) {
        if (content.isEmpty()) {
            content.putAll(_content);
        } else {
            Iterator iter = _content.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                List<Object> srcList = (List<Object>) entry.getValue();
                if (content.containsKey(key)) {
                    List<Object> list = content.get(key);
                    list.addAll(srcList);
                } else {
                    content.put(key, srcList);
                }
            }
        }

    }

    public void addContent(Object key, Object value) {
        if (!content.containsKey(key)) {
            List<Object> list = new ArrayList<Object>();
            list.add(value);
            content.put(key, list);
        } else {
            content.get(key).add(value);
        }
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return content.get(content.keySet().toArray()[groupPosition]).size();

    }

    @Override
    public Object getGroup(int groupPosition) {
        return content.keySet().toArray()[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return content.get(content.keySet().toArray()[groupPosition]).get(
                childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflateConvertView(R.layout.item_ed_group);
        }
        TextView txtItem = (TextView) convertView.findViewById(R.id.txt_Time);
        txtItem.setText((CharSequence) getGroup(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflateConvertView(R.layout.item_yield_detail);
            holder = new ViewHolder();
            holder.imgYiedlType = (ImageView) convertView.findViewById(R.id.img_yiel_type);
            holder.txtSerialNumber = (TextView) convertView.findViewById(R.id.txt_serialNumber);
            holder.txtTime = (TextView) convertView.findViewById(R.id.txt_time);
            holder.txtMoney = (TextView) convertView.findViewById(R.id.txt_money);
            holder.txtStatus= (TextView) convertView.findViewById(R.id.txt_status);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        YieldDetail yieldDetail = (YieldDetail) getChild(groupPosition, childPosition);
        holder.txtSerialNumber.setText("流水号：" + yieldDetail.getSerialNumber());
        holder.txtTime.setText(TimeUtil.formatTimeYMDHMS(yieldDetail.getTime()));
        switch (yieldDetail.getType()) {
            case YieldDetail.YIEL_TYPE_PROUDCT://商品
                holder.imgYiedlType.setBackgroundResource(R.drawable.spyj_pic);
                break;
            case YieldDetail.YIEL_TYPE_FLOW://流量
                holder.imgYiedlType.setBackgroundResource(R.drawable.llfl_pic);
                break;
            case YieldDetail.YIEL_TYPE_YB://延保
                holder.imgYiedlType.setBackgroundResource(R.drawable.yanbao_pic);
                break;
            case YieldDetail.YIEL_TYPE_DZD://对账单收入
            case YieldDetail.YIEL_TYPE_YYFF://应用分发
                holder.imgYiedlType.setBackgroundResource(R.drawable.yingyongj_pic);
                break;
        }
        if(yieldDetail.getStatus()==1)
        {
            ViewUtils.setVisible((holder.txtStatus));
        }else
        {
            ViewUtils.setGone(holder.txtStatus);
        }
        ViewUtils.setText(holder.txtMoney, "+"+ StringUtil.getFormatPrice(yieldDetail.getMoney()));

        return convertView;
    }

    public View inflateConvertView(int layoutId) {
        return LayoutInflater.from(context).inflate(layoutId, null);
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class ViewHolder {
        ImageView imgYiedlType;
        TextView txtSerialNumber, txtTime, txtMoney,txtStatus;

    }
}
