package com.xiaowu.widget;

import java.util.List;




import com.xiaowu.adapter.SelectDialogAdapter;
import com.xiaowu.projectdesign.R;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SelectDialog {

	public static final int DIALOG_SELECT_OPERATE = 1;

	private Context mContext;
	private Handler mHandler;
	private List<String> list;

	private AlertDialog ad;
	private LinearLayout dialogTitleLL;
	private TextView dialogTitleText;
	private ListView listview;

	public SelectDialog(Context mContext, Handler mHandler, List<String> list) {
		super();
		this.mContext = mContext;
		this.mHandler = mHandler;
		this.list = list;

		ad = new AlertDialog.Builder(mContext).create();
		ad.show();

		Window window = ad.getWindow();
		window.setContentView(R.layout.dialog_select);

		initUI(window);
	}

	public void initUI(Window w) {
		dialogTitleLL = (LinearLayout) w
				.findViewById(R.id.dialog_select_title_ll);
		dialogTitleText = (TextView) w
				.findViewById(R.id.dialog_select_title_text);
		listview = (ListView) w.findViewById(R.id.dialog_select_listview);

		listview.setAdapter(new SelectDialogAdapter(mContext, list));
		listview.setOnItemClickListener(selectClickLis);
	}

	public void setTitleText(String title) {
		dialogTitleText.setText(title);
	}

	public void setTitleVisible(int flag) {
		dialogTitleLL.setVisibility(flag);
	}

	public void setTitleColor(int color) {
		dialogTitleText.setTextColor(color);
	}

	private OnItemClickListener selectClickLis = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Bundle b = new Bundle();
			b.putInt("selectid", position);
			mHandler.obtainMessage(DIALOG_SELECT_OPERATE, b).sendToTarget();
			ad.dismiss();
		}
	};

}
