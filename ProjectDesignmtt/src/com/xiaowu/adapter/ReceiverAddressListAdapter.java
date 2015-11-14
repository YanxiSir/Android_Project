package com.xiaowu.adapter;

import java.util.ArrayList;

import com.xiaowu.projectdesign.R;
import com.xiaowu.request.domain.ReceiverInfo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ReceiverAddressListAdapter extends BaseAdapter{
	private ArrayList<ReceiverInfo> receivers;
	private Context mcontext;
	
	public ReceiverAddressListAdapter(ArrayList<ReceiverInfo> receivers,
			Context mcontext)
	{
		this.receivers = receivers;
		this.mcontext = mcontext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return receivers.size();
	}

	

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return  receivers.get(arg0);
	}
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}


	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View view=View.inflate(mcontext, R.layout.receiver_address_item, null);
		TextView telnumber=(TextView) view.findViewById(R.id.Receiver_telnumber);
		TextView address=(TextView) view.findViewById(R.id.Receiver_address);
		telnumber.setText(receivers.get(arg0).getTelnumber());
		telnumber.setText(receivers.get(arg0).getAddress());
		return view;
	}

}
