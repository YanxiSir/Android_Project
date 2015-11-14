package com.smilexi.sx.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.smilexi.sx.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.util.Base64;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

public class Tool {
	static final long ONE_SECOND = 1000;
	static final long ONE_MINIUS = ONE_SECOND * 60;
	static final long ONE_HOUR = ONE_MINIUS * 60;
	static final long ONE_DAY = ONE_HOUR * 60;
	static final long ONE_WEEK = ONE_HOUR * 7;
	static final long ONE_MOUTH = ONE_DAY * 30;

	public static void hideSoft(Context context) {
		try {
			InputMethodManager imm = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm.isActive()) {
				imm.hideSoftInputFromWindow(((Activity) context)
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static String base64Encode(String data) {
		byte[] encode = Base64.encode(data.getBytes(), Base64.DEFAULT);
		return new String(encode);
	}

	public static String base64Decode(String data) {
		byte[] decode = Base64.decode(data.getBytes(), Base64.DEFAULT);
		return new String(decode);
	}
	
	// ‘ÿ»ÎÕº∆¨
		public static void imageLoader(Context context, ImageView view, String imageUrl, Callback callback) {// ImageLoadingListener
																												// callback

			if (imageUrl == null) {
				return;
			}

			if (imageUrl.startsWith("http://7teb2g.com2.z0.glb.qiniucdn.com")) {
				imageUrl.replace("imageMogr/auto-orient/thumbnail/!140x140r/gravity/center/crop/!140x140/quality/80",
						"imageMogr/auto-orient/thumbnail/!140x140r/gravity/center/crop/!140x140/quality/80/format/jpg");
			}
			if (callback == null)
				Picasso.with(context).load(imageUrl).placeholder(R.drawable.defaultheader).error(R.drawable.defaultheader).into(view);
			else
				Picasso.with(context).load(imageUrl).error(R.drawable.defaultheader).into(view, callback);// .into(view);
		}
		
		
		public static long getTime(String date) {
			SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
			Date dateObj = null;
			try {
				dateObj = format.parse(String.valueOf(date));

				return dateObj.getTime();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				L.e(e.getMessage());

				return 0;
			}
		}

		public static String getTime(long time) {
			SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm", Locale.getDefault());
			return format.format(new Date(time));
		}

		public static String getHourAndMin(long time) {
			SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
			return format.format(new Date(time));
		}
		

		public static String getChatTime(Context context, long timesamp) {
			Date otherDay = new Date(timesamp);

			return RelativeDateFormat.format(otherDay);

		}


}
