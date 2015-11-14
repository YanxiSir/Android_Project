package com.xiaowu.adapter;

import java.util.List;

import com.xiaowu.projectdesign.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class SelectDialogAdapter extends BaseAdapter {
	private Context mContext;
	List<String> list;

	
	public SelectDialogAdapter(Context mContext, List<String> list) {
		super();
		this.mContext = mContext;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = View.inflate(mContext, R.layout.dialog_select_item,
				null);
		TextView t = (TextView) view
				.findViewById(R.id.dialog_select_item_text);
		t.setText(list.get(position));

		return view;
	}
}
