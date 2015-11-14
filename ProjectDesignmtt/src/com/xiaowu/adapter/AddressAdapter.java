package com.xiaowu.adapter;

import java.util.List;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaowu.db.dao.AddressBean;
import com.xiaowu.projectdesign.R;

public class AddressAdapter extends BaseAdapter {
	
	public static final int OPERATE_ADDRESS_LONG_CLICK = 10;
	public static final int OPERATE_ADDRESS_SHORT_CLICK = 11;
	
	private Context mContext;
	private List<AddressBean> beans;
	private Handler mHandler;
	AddressBean item;
	
	
	
	

	public AddressAdapter(Context mContext, List<AddressBean> beans,Handler mHandler) {
		super();
		this.mContext = mContext;
		this.beans = beans;
		this.mHandler = mHandler;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return beans.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return beans.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_address_item, null);
		
		item = beans.get(arg0);
		
		LinearLayout infoLl = (LinearLayout) view.findViewById(R.id.aai_ll);
		TextView info = (TextView) view.findViewById(R.id.aai_info);
		
		info.setText(item.getAddr()+","+item.getPhone()+","+item.getUsername());
		
		infoLl.setOnLongClickListener(new LlLongClickLis(mContext, item.getId()));
		infoLl.setOnClickListener(new LlClickLis(mContext, item.getId()));
		return view;
	}
	private class LlClickLis implements OnClickListener{
		private Context mContext;
		private int id;
		
		public LlClickLis(Context mContext, int id) {
			super();
			this.mContext = mContext;
			this.id = id;
		}
		@Override
		public void onClick(View arg0) {
			Bundle b = new Bundle();
			b.putInt("id", id);//Message msg ;  msg.what ;msg.obj;
			mHandler.obtainMessage(OPERATE_ADDRESS_SHORT_CLICK, b).sendToTarget();
		}
	}
	private class LlLongClickLis implements OnLongClickListener{
		private Context mContext;
		private int id;
		
		public LlLongClickLis(Context mContext, int id) {
			super();
			this.mContext = mContext;
			this.id = id;
		}

		@Override
		public boolean onLongClick(View arg0) {
			Bundle b = new Bundle();
			b.putInt("id", id);
			mHandler.obtainMessage(OPERATE_ADDRESS_LONG_CLICK, b).sendToTarget();
			return true;
		}
	}

}
