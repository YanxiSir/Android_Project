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

//�첽����ͼƬջ��
public class AsyncImageTask {
	// ����ͼƬ
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
	
	// IDΪ��¼��ʶ,��ʶ��������¼iamge
	public Bitmap loadImage(final int id, final String imageUrl,
			final ImageCallback imageCallback) {
		// �鿴�������Ƿ��Ѿ����ع���ͼƬ
		if (imageMap.containsKey(imageUrl)) {
			Bitmap bitmap = imageMap.get(imageUrl);
			if (bitmap != null) {
				return bitmap;
			}
		}
		// ����ͼƬUI����Ϣ����
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Bitmap) message.obj, id);
			}
		};
		
		// ����ͼƬ���߳�
		new Thread() {
			@Override
			public void run() {
				super.run();
				// ����ͼƬ
				Bitmap bitmap = AsyncImageTask.loadImageByUrl(imageUrl);
				// ���뻺�漯����
				imageMap.put(imageUrl, bitmap);
				// ֪ͨ��Ϣ���и���UI
				Message message = handler.obtainMessage(0, bitmap);
				handler.sendMessage(message);
			}
		}.start();
		return null;
	}

	// ����ͼƬ��ַ����ͼƬ��������ΪDrawable
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
			// �������
			HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
			conn.setConnectTimeout(6000);// ���ó�ʱ
			conn.setDoInput(true);
			conn.setUseCaches(false);// ������
			conn.connect();
			InputStream is = conn.getInputStream();// ���ͼƬ��������
			bmp = BitmapFactory.decodeStream(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bmp;
	}

	// ���ý�ڻص�������ͼƬUI
	public interface ImageCallback {
		public void imageLoaded(Bitmap image, int id);
	}
}
