package com.xiaowu.widget;


import com.xiaowu.projectdesign.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LoadingDialog {

	private Context mContext;
	private String message;

	Dialog dialog;

	public LoadingDialog(Context context, String msg) {
		this.mContext = context;
		this.message = msg;
		init();
	}

	private void init() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.loading_dialog, null);
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);
		ImageView loadingImage = (ImageView) v
				.findViewById(R.id.loading_dialog_image);
		TextView loadingText = (TextView) v
				.findViewById(R.id.loading_dialog_text);

		Animation loadingAnimation = AnimationUtils.loadAnimation(mContext,
				R.anim.loading_animation);
		loadingImage.startAnimation(loadingAnimation);
		loadingText.setText(message);

		dialog = new Dialog(mContext, R.style.loading_dialog);

		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(layout, new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
	}

	public void setCancelable(boolean flag) {
		dialog.setCancelable(flag);
	}

	public void setCanceledOnTouchOutside(boolean flag) {
		dialog.setCanceledOnTouchOutside(flag);
	}

	public void show() {
		dialog.show();
	}

	public void dismiss() {
		dialog.dismiss();
	}
}
