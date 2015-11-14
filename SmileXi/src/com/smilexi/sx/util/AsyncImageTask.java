package com.smilexi.sx.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

//异步加载图片栈类
public class AsyncImageTask {
	// 缓存图片
	private Map<String, Bitmap> imageMap;

	public AsyncImageTask() {
		super();
		this.imageMap = new HashMap<String, Bitmap>();
	}
	
	public Bitmap getBitmap(String key){
		Bitmap bitmap = null;
		bitmap = this.imageMap.get(key);
		return bitmap;
	}
	
	// ID为记录标识,标识是那条记录iamge
	public Bitmap loadImage(final int id, final String imageUrl,
			final ImageCallback imageCallback) {
		// 查看缓存内是否已经加载过此图片
		if (imageMap.containsKey(imageUrl)) {
			Bitmap bitmap = imageMap.get(imageUrl);
			if (bitmap != null) {
				return bitmap;
			}
		}
		// 更新图片UI的消息队列
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Bitmap) message.obj, id);
			}
		};
		
		// 加载图片的线程
		new Thread() {
			@Override
			public void run() {
				super.run();
				// 加载图片
				Bitmap bitmap = AsyncImageTask.loadImageByUrl(imageUrl);
				// 加入缓存集合中
				imageMap.put(imageUrl, bitmap);
				// 通知消息队列更新UI
				Message message = handler.obtainMessage(0, bitmap);
				handler.sendMessage(message);
			}
		}.start();
		return null;
	}

	// 根据图片地址加载图片，并保存为Drawable
	public static Bitmap loadImageByUrl(String imageUrl) {
		/*Bitmap bmp = null;
		URL url = null;
		InputStream inputStream = null;
		try {
			url = new URL(imageUrl);
			inputStream = (InputStream) url.getContent();
			bmp = BitmapFactory.decodeStream(inputStream);
			inputStream.close();
			return bmp;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;*/
		Bitmap bmp = null;
		try {
			URL myurl = new URL(imageUrl);
			// 获得连接
			HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
			conn.setConnectTimeout(6000);// 设置超时
			conn.setDoInput(true);
			conn.setUseCaches(false);// 不缓存
			conn.connect();
			InputStream is = conn.getInputStream();// 获得图片的数据流
			bmp = BitmapFactory.decodeStream(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bmp;
	}

	// 利用借口回调，更新图片UI
	public interface ImageCallback {
		public void imageLoaded(Bitmap image, int id);
	}
}
