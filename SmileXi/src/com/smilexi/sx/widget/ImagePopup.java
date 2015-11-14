package com.smilexi.sx.widget;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.smilexi.sx.R;
import com.smilexi.sx.activity.PhotoAlbums;
import com.smilexi.sx.util.FileUtils;

public class ImagePopup extends PopupWindow {

	public static final int TAKE_PICTURE = 0x000000;
	private static final int SELECT_ALBUMS_REQUEST = 10;

	private Context mContext;
	private String takePicPath;

	public ImagePopup(Context context, View parent) {

		this.mContext = context;

		View view = View.inflate(mContext, R.layout.item_popupwindows, null);
		view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_ins));
		LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_bottom_in_2));

		setWidth(LayoutParams.FILL_PARENT);
		setHeight(LayoutParams.FILL_PARENT);
		setBackgroundDrawable(new BitmapDrawable());
		setFocusable(true);
		setOutsideTouchable(true);
		setContentView(view);
		showAtLocation(parent, Gravity.BOTTOM, 0, 0);
		update();

		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);

		bt1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				photo();
				dismiss();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, PhotoAlbums.class);
				((Activity) mContext).startActivityForResult(intent, SELECT_ALBUMS_REQUEST);
				dismiss();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

	}

	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		if (!FileUtils.existSDCard()) {
			return;
		}

		String dir = Environment.getExternalStorageDirectory() + "/SmileXi/sx/tmp/";
		File file = new File(dir);
		if (!file.exists())
			file.mkdirs();
		file = new File(dir, String.valueOf(System.currentTimeMillis()) + ".jpg");

		setTakePicPath(file.getPath());
		Uri imageUri = Uri.fromFile(file);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		((Activity) mContext).startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	public String getTakePicPath() {
		return takePicPath;
	}

	public void setTakePicPath(String takePicPath) {
		this.takePicPath = takePicPath;
	}

}
