package com.smilexi.sx.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;





import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;

public class ImageUtil {
	public static String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss", Locale.ENGLISH);
		return dateFormat.format(date) + ".jpg";
	}

	/**
	 * 淇濆瓨鐨勫浘鐗?
	 * 
	 * @param bitmap
	 * @param path
	 * @param fileName
	 * @return
	 */
	public static String savePicToSdcard(Bitmap bitmap, String path,
			String fileName) {
		String filePath = "";
		if (bitmap == null) {
			return filePath;
		} else {
			filePath = path + fileName;
			File destFile = new File(filePath);
			OutputStream os = null;
			try {
				os = new FileOutputStream(destFile);
				bitmap.compress(CompressFormat.JPEG, 100, os);
				os.flush();
				os.close();
			} catch (IOException e) {
				filePath = "";
			}
		}
		return filePath;
	}

	public static final boolean compress(String imagePath, String descPath) {
		File file = new File(imagePath);
		int size = 1;
		size = (int) (file.length() / (1024 * 256));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = size; // 瀹介珮涓哄師鏉ヤ竴鍗?
		options.inJustDecodeBounds = false; // 鐪熷疄鍔犺浇
		FileOutputStream fos = null;
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeFile(imagePath, options);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			byte[] bytes = stream.toByteArray();
			try {
				// 鑾峰緱鏂囦欢杈撳嚭娴?
				fos = new FileOutputStream(descPath);
				// 鍐欏埌璇ユ枃浠?
				fos.write(bytes);
				// 鍏抽棴鏂囦欢娴?
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			System.gc();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
				}
				fos = null;
			}
			if (bitmap != null) {
				bitmap.recycle();
				bitmap = null;
			}
		}
		return false;
	}

	/**
	 * 淇敼鍥剧墖鐨勫ぇ灏?
	 * 
	 * @param path
	 * @param size
	 * @param context
	 * @return
	 */
	public static Bitmap getBitmapBySize(String path, int width, int height) {
		BitmapFactory.Options option = new BitmapFactory.Options();
		option.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, option);
		option.inSampleSize = computeSampleSize(option, -1, width * height);

		option.inJustDecodeBounds = false;
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeFile(path, option);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawable2Bitmap(Drawable drawable) {
		// 鍙? drawable 鐨勯暱瀹?
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();

		// 鍙? drawable 鐨勯鑹叉牸寮?
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565;
		// 寤虹珛瀵瑰簲 bitmap
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		// 寤虹珛瀵瑰簲 bitmap 鐨勭敾甯?
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		// 鎶? drawable 鍐呭鐢诲埌鐢诲竷涓?
		drawable.draw(canvas);
		return bitmap;
	}

	public static String getBitmapStrBase64(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 100, baos);
		byte[] bytes = baos.toByteArray();
		return Base64.encodeToString(bytes, Base64.DEFAULT);
	}

	public static Bitmap base64ToBitmap(String base64String) {
		byte[] bytes = Base64.decode(base64String, Base64.DEFAULT);
		Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return bitmap;
	}

	public static Bitmap pathToBitmap(Context context, String path) {
		Bitmap bitmap = null;
		try {
			if (path.startsWith("content:")) {
				Uri uri = Uri.parse(path);
				ContentResolver cr = context.getContentResolver();
				InputStream is = cr.openInputStream(uri);

				bitmap = BitmapFactory.decodeStream(is);
				bitmap = comp(bitmap);
				is.close();
			} else {
				bitmap = BitmapFactory.decodeFile(path);
				bitmap = comp(bitmap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	public static Bitmap comp(Bitmap image) {

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			if (baos.toByteArray().length / 1024 > 1024) {// 锟叫讹拷锟斤拷锟酵计拷锟斤拷锟?1M,锟斤拷锟斤拷压锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷图片锟斤拷BitmapFactory.decodeStream锟斤拷时锟斤拷锟?
				baos.reset();// 锟斤拷锟斤拷baos锟斤拷锟斤拷锟絙aos
				image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 锟斤拷锟斤拷压锟斤拷50%锟斤拷锟斤拷压锟斤拷锟斤拷锟斤拷锟斤拷荽锟脚碉拷baos锟斤拷
			}
			ByteArrayInputStream isBm = new ByteArrayInputStream(
					baos.toByteArray());
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 锟斤拷始锟斤拷锟斤拷图片锟斤拷锟斤拷时锟斤拷options.inJustDecodeBounds 锟斤拷锟絫rue锟斤拷
			newOpts.inJustDecodeBounds = true;
			Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
			newOpts.inJustDecodeBounds = false;
			int w = newOpts.outWidth;
			int h = newOpts.outHeight;
			// 锟斤拷锟斤拷锟斤拷锟斤拷锟街伙拷锟饺较讹拷锟斤拷800*480锟街憋拷锟绞ｏ拷锟斤拷锟皆高和匡拷锟斤拷锟斤拷锟斤拷锟斤拷为
			float hh = 800f;// 锟斤拷锟斤拷锟斤拷锟矫高讹拷为800f
			float ww = 480f;// 锟斤拷锟斤拷锟斤拷锟矫匡拷锟轿?480f
			// 锟斤拷锟脚比★拷锟斤拷锟斤拷锟角固讹拷锟斤拷锟斤拷锟斤拷锟脚ｏ拷只锟矫高伙拷锟竭匡拷锟斤拷锟斤拷一锟斤拷锟斤拷锟捷斤拷锟叫硷拷锟姐即锟斤拷
			int be = 1;// be=1锟斤拷示锟斤拷锟斤拷锟斤拷
			if (w > h && w > ww) {// 锟斤拷锟斤拷锟饺达拷幕锟斤拷锟斤拷菘锟饺固讹拷锟斤拷小锟斤拷锟斤拷
				be = (int) (newOpts.outWidth / ww);
			} else if (w < h && h > hh) {// 锟斤拷锟斤拷叨雀叩幕锟斤拷锟斤拷菘锟饺固讹拷锟斤拷小锟斤拷锟斤拷
				be = (int) (newOpts.outHeight / hh);
			}
			if (be <= 0)
				be = 1;
			newOpts.inSampleSize = be;// 锟斤拷锟斤拷锟斤拷锟脚憋拷锟斤拷
			// 锟斤拷锟铰讹拷锟斤拷图片锟斤拷注锟斤拷锟绞憋拷丫锟斤拷锟給ptions.inJustDecodeBounds 锟斤拷锟絝alse锟斤拷
			isBm = new ByteArrayInputStream(baos.toByteArray());
			bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
			baos.close();
			return compressImage(bitmap);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		// 压锟斤拷锟矫憋拷锟斤拷锟斤拷小锟斤拷锟劫斤拷锟斤拷锟斤拷锟斤拷压锟斤拷
	}

	public static Bitmap compressImage(Bitmap image) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 锟斤拷锟斤拷压锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷100锟斤拷示锟斤拷压锟斤拷锟斤拷锟斤拷压锟斤拷锟斤拷锟斤拷锟斤拷荽锟脚碉拷baos锟斤拷
			int options = 100;
			while (baos.toByteArray().length / 1024 > 100) { // 循锟斤拷锟叫讹拷锟斤拷锟窖癸拷锟斤拷锟酵计拷欠锟斤拷锟斤拷100kb,锟斤拷锟节硷拷锟斤拷压锟斤拷
				baos.reset();// 锟斤拷锟斤拷baos锟斤拷锟斤拷锟絙aos
				image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 锟斤拷锟斤拷压锟斤拷options%锟斤拷锟斤拷压锟斤拷锟斤拷锟斤拷锟斤拷荽锟脚碉拷baos锟斤拷
				options -= 10;// 每锟轿讹拷锟斤拷锟斤拷10
			}
			ByteArrayInputStream isBm = new ByteArrayInputStream(
					baos.toByteArray());// 锟斤拷压锟斤拷锟斤拷锟斤拷锟斤拷锟絙aos锟斤拷诺锟紹yteArrayInputStream锟斤拷
			Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 锟斤拷ByteArrayInputStream锟斤拷锟斤拷锟斤拷锟斤拷图片

			baos.close();
			return bitmap;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	private String parseToByteStr(Bitmap bitmap) {
		String result = "";
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			result = Base64.encodeToString(out.toByteArray(), Base64.DEFAULT); // 锟斤拷锟斤拷Base64锟斤拷锟斤拷
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static int getExifOrientation(String filepath) {
		int degree = 0;
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(filepath);
		} catch (IOException ex) {
			L.e("test", "cannot read exif:" + ex);
		}
		if (exif != null) {
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
			if (orientation != -1) {
				switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
				}
			}
		}
		return degree;
	}
	
	public static String parseToByteStr(File tempFile) {
		String result = "";
		FileInputStream fis = null;
		ByteArrayOutputStream baos = null;

		try {
			fis = new FileInputStream(tempFile.getAbsolutePath());
			baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[8192];
			int count = 0;
			while ((count = fis.read(buffer)) >= 0) {
				baos.write(buffer, 0, count);
			}

			result = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT); // ����Base64����
			fis.close();

			return result;
		} catch (Exception e) {
			L.e(e.getMessage());
		} finally {
			if (baos != null)
				try {
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return result;
	}
	
	public static void savaBitmap(Bitmap bitmap,File tempFile) {

		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(tempFile);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);// ��Bitmap�����������
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static File getPhotoFile() {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			String path = Environment.getExternalStorageDirectory().getPath()
					+ "/SmileXi/DCIM/";

			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"'IMG'_yyyyMMdd_HHmmss", Locale.CHINA);
			File photoFile = new File(dir, dateFormat.format(date) + ".jpg");
			if (!photoFile.exists()) {
				try {
					photoFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return photoFile;
		}
		return null;
	}
}
