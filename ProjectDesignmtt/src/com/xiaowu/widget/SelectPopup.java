package com.xiaowu.widget;

import java.util.List;





import com.xiaowu.adapter.SelectDialogAdapter;
import com.xiaowu.projectdesign.R;

import android.content.Context;
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
	private LinearLayout titleLl;

	public SelectPopup(Context mContext, Handler mHandler, List<String> list,
			View parent) {
		super();
		this.mContext = mContext;
		this.mHandler = mHandler;
		this.list = list;
		
		View customView = LayoutInflater.from(mContext).inflate(
				R.layout.dialog_select, null);
		popupwindow = new PopupWindow(customView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
//		popupwindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		popupwindow.setOutsideTouchable(true);
		popupwindow.setContentView(customView);
		
		popupwindow.showAsDropDown(parent, 0, 0);
		popupwindow.update();
		
		customView.setOnTouchListener(viewTouchLis);
		
		initUI(customView);
	}
	
	private void initUI(View v){
		
		titleLl = (LinearLayout) v.findViewById(R.id.dialog_select_title_ll);
		titleLl.setVisibility(View.GONE);
		listview = (ListView) v.findViewById(R.id.dialog_select_listview);
		listview.setAdapter(new SelectDialogAdapter(mContext, list));
		listview.setOnItemClickListener(listItemClickLis);
	}
	public boolean isShow(){
		if(popupwindow == null)
			return false;
		return popupwindow.isShowing();
	}
	public void dismiss(){
		popupwindow.dismiss();
	}
	private OnItemClickListener listItemClickLis = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Bundle b = new Bundle();
			b.putInt("oid", arg2); 
			mHandler.obtainMessage(SELECT_ID, b).sendToTarget();
			popupwindow.dismiss();
		}
	};
	
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
