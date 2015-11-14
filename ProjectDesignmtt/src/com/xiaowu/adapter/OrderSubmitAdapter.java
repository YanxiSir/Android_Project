package com.xiaowu.adapter;

import java.util.List;

import com.xiaowu.projectdesign.R;
import com.xiaowu.utils.OrderDetail.DishInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OrderSubmitAdapter extends BaseAdapter {

	private Context mContext;
	private List<DishInfo> dishList;
	LayoutInflater mInflater;
	DishInfo item;

	public OrderSubmitAdapter(Context mContext, List<DishInfo> dishList) {
		super();
		this.mContext = mContext;
		this.dishList = dishList;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return dishList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return dishList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {

		View view = mInflater.inflate(R.layout.adapter_order_item, null);

		ViewHolder holder = new ViewHolder(view);
		item = dishList.get(arg0);
		holder.dishName.setText(item.getDishName());
		int count = item.getDishCount();
		holder.dishCount.setText(count+"");
		holder.dishPrice.setText(count*item.getDishPrice()+"");
		
		
		return view;
	}

	private class ViewHolder {
		private TextView dishName, dishCount, dishPrice;

		public ViewHolder(View v) {
			dishName = (TextView) v.findViewById(R.id.aoi_name);
			dishCount = (TextView) v.findViewById(R.id.aoi_count);
			dishPrice = (TextView) v.findViewById(R.id.aoi_price);
		}

	}
}
