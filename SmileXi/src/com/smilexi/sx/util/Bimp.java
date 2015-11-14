package com.smilexi.sx.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Bimp {
	public static int max = 0;
	public static boolean act_bool = true;
	public static List<Bitmap> bmp = new ArrayList<Bitmap>();

	public static List<String> drr = new ArrayList<String>();

	public static Bitmap revitionImageSize(String path) throws IOException {
		FileInputStream fs = new FileInputStream(new File(path));
		BufferedInputStream in = new BufferedInputStream(fs);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		if (in != null)
			in.close();
		if (fs != null)
			fs.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 1000)
					&& (options.outHeight >> i <= 1000)) {
				FileInputStream fis = new FileInputStream(new File(path));
				in = new BufferedInputStream(fis);
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				if (in != null)
					in.close();
				if (fis != null)
					fis.close();
				break;
			}
			i += 1;
		}
		return bitmap;
	}

	public static void clear() {
		if (bmp != null)
			bmp = null;
		if (drr != null)
			drr = null;
		bmp = new ArrayList<Bitmap>();
		drr = new ArrayList<String>();
		max = 0;
		act_bool = true;
	}
}
