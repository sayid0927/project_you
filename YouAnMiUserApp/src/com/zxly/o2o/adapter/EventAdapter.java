package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxly.o2o.model.Event;
import com.zxly.o2o.o2o_user.R;

import java.util.List;

public class EventAdapter extends BaseAdapter
{
    private List<Event> mDataSource;
    
    private LayoutInflater mInflater;
    
    public EventAdapter(Context context, List<Event> dataSource)
    {
        mInflater = LayoutInflater.from(context);
        mDataSource = dataSource;
    }
    
    @Override
    public int getCount()
    {
        return mDataSource.size();
    }
    
    @Override
    public Event getItem(int position)
    {
        return mDataSource.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder vh;
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.item_event, null);
            vh = new ViewHolder();
            vh.icon = (ImageView)convertView.findViewById(R.id.image);
            vh.describe = (TextView)convertView.findViewById(R.id.describe_text);
            vh.tag = (TextView)convertView.findViewById(R.id.describe_tag);
            vh.title = (TextView)convertView.findViewById(R.id.title_text);
            vh.title_layout = convertView.findViewById(R.id.title_layout);
            vh.describe_layout = convertView.findViewById(R.id.describe_layout);
            convertView.setTag(vh);
        }
        else
        {
            vh = (ViewHolder)convertView.getTag();
        }
        initView(vh, position);
        return convertView;
    }
    
    private void initView(ViewHolder vh, int position)
    {
        Event event = getItem(position);
        int action = event.getAction();
        if (action == 0)
        {
            vh.title_layout.setVisibility(View.VISIBLE);
            vh.describe_layout.setVisibility(View.GONE);
            vh.title.setText(event.getText());
        }
        else
        {
            vh.title_layout.setVisibility(View.GONE);
            vh.describe_layout.setVisibility(View.VISIBLE);
            vh.title.setText(event.getText());
        }
        
    }
    
    private class ViewHolder
    {
        ImageView icon;
        
        TextView describe;
        
        TextView tag;
        
        TextView title;
        
        View title_layout;
        
        View describe_layout;
        
    }
}
