package com.xiaowu.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;

public class FileUtils {

	public static final String NT_ROOTNAME = "Neitao";

	private static final String BITMAP_NAME = "IMAGE_";

	private static final String NT_IMAGEDIR = "neitao";

	private static final String NT_COVER = "cover";

	public static String SDPATH = Environment.getExternalStorageDirectory()
			+ "/formats/";

	public static void saveBitmap(Bitmap bm, String picName) {
		try {
			if (!isFileExist("")) {
				File tempf = createSDDir("");
			}
			File f = new File(SDPATH, picName + ".JPEG");
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File createSDDir(String dirName) throws IOException {
		File dir = new File(SDPATH + dirName);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			System.out.println("createSDDir:" + dir.getAbsolutePath());
			System.out.println("createSDDir:" + dir.mkdir());
		}
		return dir;
	}

	public static boolean isFileExist(String fileName) {
		File file = new File(SDPATH + fileName);
		file.isFile();
		return file.exists();
	}

	public static void delFile(String fileName) {
		File file = new File(SDPATH + fileName);
		if (file.isFile()) {
			file.delete();
		}
		file.exists();
	}

	public static void deleteDir() {
		File dir = new File(SDPATH);
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;

		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete();
			else if (file.isDirectory())
				deleteDir();
		}
		dir.delete();
	}

	public static boolean fileIsExists(String path) {
		try {
			File f = new File(path);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {

			return false;
		}
		return true;
	}
	
	public static Bitmap getUserCover(Context context,String userId){
		Bitmap coverBitmap = null;
		
		if(!existSDCard()){
			return null;
		}
		String coverPath = 
				Environment.getExternalStorageDirectory()+"/"+NT_ROOTNAME
				+"/"+NT_IMAGEDIR+"/"+NT_COVER+"/";
		File coverFile = new File(coverPath);
		if(!coverFile.exists()){
			return null;
		}
		String bitmapName = BITMAP_NAME+userId+".png";
		File coverPathFile = new File(coverFile,bitmapName);
		if(!coverPathFile.exists())
			return null;
		try {
			FileInputStream fis = new FileInputStream(coverPathFile);
			coverBitmap = BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return coverBitmap;
	}

	// 鏂囦欢娴佹搷浣滄椂鐢ㄥ埌鐨勪笁涓柟娉�
	public static String savaBitmap(Context context, Bitmap bitmap, Object param) {
		String userId = (String) param;
		String mSavePath = null;
		String bitmapName = "";
		File bitmapSavePath ;
		if (!existSDCard()) {
			T.showShort(context, "鏈娴嬪埌SD鍗�");
			return mSavePath;
		}
		try {// 鑾峰緱sd鍗¤矾寰�
			String sdPath = Environment.getExternalStorageDirectory() + "/";
			mSavePath = sdPath + NT_ROOTNAME;
			File rootFile = new File(mSavePath);
			// 鍒ゆ柇鏂囦欢鐩綍鏄惁瀛樺湪,涓嶅瓨鍦ㄥ氨鍒涘缓
			if (!rootFile.exists()) {
				rootFile.mkdir();
			}
			// mSavePath_nt 鏄瓨鏀惧浘鐗囩殑鏂囦欢澶笽MAGE
			String mSavePath_nt = mSavePath + "/" + NT_IMAGEDIR;
			File imageFile = new File(mSavePath_nt);
			if (!imageFile.exists())
				imageFile.mkdir();
			// 鐢熸垚瑕佸瓨鍌ㄧ殑鏂囦欢鍚�
			if (!TextUtils.isEmpty(userId)) {
				String cover_path = mSavePath_nt + "/" + NT_COVER;
				File coverFile = new File(cover_path);
				if (!coverFile.exists())
					coverFile.mkdir();
				bitmapName = BITMAP_NAME + userId + ".png";
				bitmapSavePath = new File(cover_path, bitmapName);
			} else {
				bitmapName = BITMAP_NAME
						+ String.valueOf(System.currentTimeMillis()) + ".png";
				bitmapSavePath = new File(mSavePath_nt, bitmapName);
			}
//			bitmapSavePath = new File(mSavePath_nt, bitmapName);
			if (bitmapSavePath.exists())
				bitmapSavePath.delete();
			bitmapSavePath.createNewFile();
			FileOutputStream os = new FileOutputStream(bitmapSavePath);
			boolean is = bitmap.compress(Bitmap.CompressFormat.PNG, 90, os);
			if (!is)
				mSavePath = null;
			os.flush();
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mSavePath;
	}

	public static boolean existSDCard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
			return true;
		return false;
	}
	
	

}
