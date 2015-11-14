package com.smilexi.sx.widget;

import java.util.List;

import com.smilexi.sx.R;
import com.smilexi.sx.adapter.PopupDialogAdapter;
import com.smilexi.sx.adapter.SelectDialogAdapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

public class SelectPopup {
	
	public static final int SELECT_ID = 0;

	private Context mContext;
	private Handler mHandler;
	private List<String> list;

	private PopupWindow popupwindow;
	
	private ListView listview;

	public SelectPopup(Context mContext, Handler mHandler, List<String> list,
			View parent) {
		super();
		this.mContext = mContext;
		this.mHandler = mHandler;
		this.list = list;

		View customView = LayoutInflater.from(mContext).inflate(
				R.layout.popup_select, null);
		popupwindow = new PopupWindow(customView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		popupwindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		popupwindow.setOutsideTouchable(true);
		popupwindow.setContentView(customView);
		
		popupwindow.showAsDropDown(parent, 0, 0);
		popupwindow.update();
		
//		customView.setOnTouchListener(viewTouchLis);
		
		initUI(customView);
	}
	public boolean isShow(){
		if(popupwindow.isShowing())
			return true;
		return false;
	}
	public void dismiss(){
		popupwindow.dismiss();
	}
	public void initUI(View v){
		
		
		listview = (ListView) v.findViewById(R.id.popup_select_listview);
		listview.setAdapter(new PopupDialogAdapter(mContext, list,mHandler,popupwindow));
//		listview.setOnItemClickListener(listItemClickLis);
	}
	/*private OnItemClickListener listItemClickLis = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Bundle b = new Bundle();
			b.putInt("oid", arg2); 
			mHandler.obtainMessage(SELECT_ID, b).sendToTarget();
			popupwindow.dismiss();
		}
	};*/
	
	private OnTouchListener viewTouchLis = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			if (popupwindow != null && popupwindow.isShowing()) {
				popupwindow.dismiss();
				popupwindow = null;
			}

			return false;
		}
	};

}
