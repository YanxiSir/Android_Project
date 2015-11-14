package com.smilexi.sx.widget;

import java.io.File;

import com.smilexi.sx.activity.ImproveSelfInfoActivity;
import com.smilexi.sx.finals.ServerFinals;
import com.smilexi.sx.util.ImageUtil;
import com.smilexi.sx.util.T;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;

public class SelectPicDialog {

	public static final int PHOTO_REQUEST_TAKEPHOTO = 101;
	public static final int PHOTO_REQUEST_GALLERY = 102;
	public static final int PHOTO_REQUEST_CUT = 103;

	private Context mContext;
	private ConfirmDialog picDialog;
	private File tempFile;

	public SelectPicDialog(Context mContext) {
		super();
		this.mContext = mContext;
	}

	public void showPicDialog() {
		picDialog = new ConfirmDialog(mContext);
		picDialog.setTitle("头像设置");
		picDialog.setMessageVisibility(View.GONE);
		picDialog.setLeftButton("拍照", new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				picDialog.dismiss();

				tempFile = ImageUtil.getPhotoFile();
				if (null != tempFile) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra(MediaStore.EXTRA_OUTPUT,
							Uri.fromFile(tempFile));
					setTempFile(tempFile); 
					((Activity) mContext).startActivityForResult(intent,
							PHOTO_REQUEST_TAKEPHOTO);
				} else {
					T.showShort(mContext, "上传图片");
				}
			}
		});
		picDialog.setRightButton("相册", new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				tempFile = ImageUtil.getPhotoFile();
				if (null != tempFile) {
					Intent intent = new Intent(Intent.ACTION_PICK, null);
					intent.setDataAndType(
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
							"image/*");
					((Activity) mContext).startActivityForResult(intent,
							PHOTO_REQUEST_GALLERY);
				} else {
					T.showShort(mContext, "获取图片失败");
				}
				picDialog.dismiss();
			}
		});
	}
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", ServerFinals.USER_BIGIMG);
		intent.putExtra("outputY", ServerFinals.USER_BIGIMG);
		intent.putExtra("return-data", true);

		((Activity) mContext).startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	public File getTempFile() {
		return tempFile;
	}

	public void setTempFile(File tempFile) {
		this.tempFile = tempFile;
	}
	
	

}
