package com.neu.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.neu.helper.R;
import com.neu.javabean.Person;

public class SelectorAdapter extends BaseAdapter {
	
	private List<Person> mList;
    private Context mContext;
    
    
	
	public SelectorAdapter(List<Person> mList, Context mContext) {
		super();
		this.mList = mList;
		this.mContext = mContext;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		View view = View.inflate(mContext, R.layout.selected_item, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.item_image);
		TextView textView = (TextView) view.findViewById(R.id.item_name);
		
		imageView.setBackgroundResource(mList.get(position).getItem_image());
		textView.setText(mList.get(position).getItem_name());
		return view;
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
}
