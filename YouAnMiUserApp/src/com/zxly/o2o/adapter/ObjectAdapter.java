package com.zxly.o2o.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public abstract class ObjectAdapter extends BaseAdapter {

	protected ArrayList<Object> content = new ArrayList<Object>();

	protected Context context;

	public ObjectAdapter(Context _context) {
		context = _context;
	}

	@Override
	public int getCount() {
		return content.size();
	}


	@Override
	public Object getItem(int position) {
		if (content == null || (content.size() == 0)) {
			Log.e("ObjectAdapter", "try to get empty content list with index:" + position);
			return null;
		}
		if (position < 0)
			position = 0;
		if (position >= content.size())
			position = content.size() - 1;
		return content.get(position);
	}

    public void updateSingleRow(ListView listView, Object obj) {

        if (listView != null) {
            int start = listView.getFirstVisiblePosition();
            for (int i = start, j = listView.getLastVisiblePosition(); i <= j; i++)
            {
                Object obj1=listView.getItemAtPosition(i);
                if (obj.equals(obj1)) {
                    View view = listView.getChildAt(i - start);
                    getView(i-listView.getHeaderViewsCount(), view, listView);
                    break;
                }
            }
            }

      }
	@Override
	public long getItemId(int position) {
		return position;
	}

	public int indexOf(Object o) {
		return content.indexOf(o);
	}

	abstract public int getLayoutId();

	public View inflateConvertView() {
		return LayoutInflater.from(context).inflate(getLayoutId(), null);
	}
	public View inflateConvertView(int layoutId) {
		return LayoutInflater.from(context).inflate(layoutId, null);
	}
	

	public void fillEmpty() {

	}

	public void addItem(Object o) {
		addItem(o, false);
	}

	public void addItem(List<?> ls) {
		addItem(ls, false);
	}

	public void addItem2Head(List<?> ls) {
		addItem2Head(ls, false);
	}

	public void addItemArray(Object[] list) {
		addItemArray(list, false);
	}

	public void addItem(Object o, boolean isUpdateUI) {
		this.content.add(o);
		if (isUpdateUI) {
			notifyDataSetChanged();
		}
	}

	public void addItem(Object o, boolean isUpdateUI,int position) {
		this.content.add(position,o);
		if (isUpdateUI) {
			notifyDataSetChanged();
		}
	}

	public void addItem(List<?> ls, boolean isUpdateUI) {
		this.content.addAll(ls);
		if (isUpdateUI) {
			notifyDataSetChanged();
		}

	}

	public void addItem2Head(List<?> ls, boolean isUpdateUI) {
		this.content.addAll(ls);
		if (isUpdateUI) {
			notifyDataSetChanged();
		}
	}
	public void addItem2Head(Object o, boolean isUpdateUI) {
		this.content.add(0, o);
		if (isUpdateUI) {
			notifyDataSetChanged();
		}
	}

	public void addItemArray(Object[] list, boolean isUpdateUI) {
		for (Object o : list) {
			this.content.add(o);
		}
		if (isUpdateUI) {
			notifyDataSetChanged();
		}
	}

	public void deleteItem(int position){
		this.content.remove(position);
	}

	public void clear() {
		this.content.clear();
	}

	public List<Object> getContent() {
		return content;
	}

}
