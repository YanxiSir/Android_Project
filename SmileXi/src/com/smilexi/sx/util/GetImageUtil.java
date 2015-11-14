package com.smilexi.sx.util;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

public class GetImageUtil {
	private static final int PHOTO_REQUEST_TAKEPHOTO = 1;
	private static final int PHOTO_REQUEST_GALLERY = 2;

	public static void getByPhotos(Context context) {

		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		((Activity) context).startActivityForResult(intent,
				PHOTO_REQUEST_GALLERY);
		/*
		 * onActivityResult 写法
		 */
		/*
		 * try { Uri uri = data.getData(); ContentResolver cr =
		 * this.getContentResolver(); InputStream is = cr.openInputStream(uri);
		 * 
		 * Bitmap bitmap = BitmapFactory.decodeStream(is); bitmap =
		 * ImageUtil.comp(bitmap);
		 * 
		 * userImage.setImageBitmap(bitmap); currentPhotoBitmap = bitmap; if (is
		 * != null) is.close(); } catch (Exception e) {
		 * userImage.setImageBitmap(null); currentPhotoBitmap = null;
		 * e.printStackTrace(); }
		 */

	}

	public static void getByCamera(Context context) {
		String path = null;

		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (!FileUtils.existSDCard()) {
			return;
		}
		String dir = Environment.getExternalStorageDirectory()
				+ "/SmileXi/sx/tmp/";
		File file = new File(dir);
		if (!file.exists())
			file.mkdirs();
		file = new File(dir, String.valueOf(System.currentTimeMillis())
				+ ".jpg");
		path = file.getPath();
		Uri imageUri = Uri.fromFile(file);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		openCameraIntent.putExtra("path", path);
		((Activity) context).startActivityForResult(openCameraIntent,
				PHOTO_REQUEST_TAKEPHOTO);
		/*
		 * onActivityResult 写法
		 */
		/*
		 * try {
		 * 
		 * Bitmap bitmap = BitmapFactory.decodeFile(path); bitmap =
		 * ImageUtil.comp(bitmap);
		 * 
		 * int angle = ImageUtil.getExifOrientation(path); if (angle != 0) { //
		 * 如果照片出现了 旋转 那么 就更改旋转度数 Matrix matrix = new Matrix();
		 * matrix.postRotate(angle); bitmap = Bitmap .createBitmap(bitmap, 0, 0,
		 * bitmap.getWidth(), bitmap.getHeight(), matrix, true); }
		 * 
		 * userImage.setImageBitmap(bitmap); currentPhotoBitmap = bitmap; }
		 * catch (Exception e) { userImage.setImageBitmap(null);
		 * currentPhotoBitmap = null;
		 * 
		 * e.printStackTrace(); }
		 */

	}
}
