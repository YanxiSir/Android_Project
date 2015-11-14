package com.smilexi.sx.adapter;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.smilexi.sx.R;

public class PopupDialogAdapter extends BaseAdapter {

	public static final int SELECT_ID = 0;
	private Context mContext;
	List<String> list;
	private Handler mHandler;
	private PopupWindow popupwindow;
	
	
	public PopupDialogAdapter(Context mContext, List<String> list,Handler mHandler,PopupWindow popupwindow) {
		super();
		this.mContext = mContext;
		this.list = list;
		this.mHandler = mHandler;
		this.popupwindow = popupwindow;
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

		View view = View.inflate(mContext, R.layout.popup_select_item,
				null);
		LinearLayout ll = (LinearLayout) view.findViewById(R.id.popup_select_item_ll);
		TextView t = (TextView) view
				.findViewById(R.id.popup_select_item_text);
		ll.setOnClickListener(new LlClickLis(position)); 
		t.setText(list.get(position));

		return view;
	}
	private class LlClickLis implements OnClickListener{
		private int position;
		public LlClickLis(int position) {
			this.position = position;
		}
		@Override
		public void onClick(View arg0) {
			Bundle b = new Bundle();
			b.putInt("oid", position); 
			mHandler.obtainMessage(SELECT_ID, b).sendToTarget();
			popupwindow.dismiss();
		}
	}

}
