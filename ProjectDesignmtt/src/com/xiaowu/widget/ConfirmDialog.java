package com.xiaowu.widget;

import com.xiaowu.projectdesign.R;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ConfirmDialog {

	private Context context;
	private AlertDialog ad;
	private TextView titleView;
	private TextView messageView;
	private LinearLayout buttonLayout;
	private RelativeLayout dialogCancelRel;
	private ImageView cancelImage;
	private TextView confirmLeftBtn;
	private TextView confirmRightBtn;

	private EditText editText;

	public ConfirmDialog(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		ad = new android.app.AlertDialog.Builder(context).create();
		ad.show();

		Window window = ad.getWindow();
		window.setContentView(R.layout.confirm);

		titleView = (TextView) window.findViewById(R.id.title);
		messageView = (TextView) window.findViewById(R.id.message);
		buttonLayout = (LinearLayout) window.findViewById(R.id.buttonLayout);
		dialogCancelRel = (RelativeLayout) window
				.findViewById(R.id.dialog_cancel_rl);
		cancelImage = (ImageView) window.findViewById(R.id.cancel_image);

		editText = (EditText) window.findViewById(R.id.editText);
		editText.setVisibility(View.GONE);

		dialogCancelRel.setVisibility(View.GONE);
		cancelImage.setVisibility(View.GONE);

		confirmLeftBtn = (TextView) window.findViewById(R.id.confirm_left_btn);
		confirmRightBtn = (TextView) window
				.findViewById(R.id.confirm_right_btn);

	}

	public ConfirmDialog(Context context, int width, int height) {
		// TODO Auto-generated constructor stub
		this.context = context;
		ad = new android.app.AlertDialog.Builder(context).create();
		ad.show();

		Window window = ad.getWindow();
		window.setContentView(R.layout.confirm);
		window.setLayout(width, height);

		titleView = (TextView) window.findViewById(R.id.title);
		messageView = (TextView) window.findViewById(R.id.message);
		buttonLayout = (LinearLayout) window.findViewById(R.id.buttonLayout);

		editText = (EditText) window.findViewById(R.id.editText);
		editText.setVisibility(View.GONE);

		confirmLeftBtn = (TextView) window.findViewById(R.id.confirm_left_btn);
		confirmRightBtn = (TextView) window
				.findViewById(R.id.confirm_right_btn);

	}
	
	public void setEditEnable(boolean flag){
		editText.setEnabled(true);
	}

	public void setEditVisible(int flag) {
		editText.setVisibility(flag);
	}

	public void setTitle(int resId) {
		titleView.setText(resId);
	}

	public void setTitle(String title) {
		titleView.setText(title);
	}

	public void setMessage(int resId) {
		messageView.setText(resId);
	}

	public void setMessageSize(float size) {
		messageView.setTextSize(size);
	}

	public void setMessageGravity(int gravity) {
		messageView.setGravity(gravity);
	}

	public void setMessage(String message) {
		messageView.setText(message);
	}

	public void setMessageVisibility(int visible) {
		messageView.setVisibility(visible);
	}

	/**
	 * ���ð�ť
	 * 
	 * @param text
	 * @param listener
	 */
	public void setLeftButton(String text, final View.OnClickListener listener) {
		if (!TextUtils.isEmpty(text))
			confirmLeftBtn.setText(text);
		confirmLeftBtn.setOnClickListener(listener);
	}

	/**
	 * ���ð�ť
	 * 
	 * @param text
	 * @param listener
	 */
	public void setRightButton(String text, final View.OnClickListener listener) {
		if (!TextUtils.isEmpty(text))
			confirmRightBtn.setText(text);
		confirmRightBtn.setOnClickListener(listener);
	}

	/**
	 * �رնԻ���
	 */
	public void dismiss() {
		ad.dismiss();
	}

	public void clear() {
		ad.cancel();
	}
}