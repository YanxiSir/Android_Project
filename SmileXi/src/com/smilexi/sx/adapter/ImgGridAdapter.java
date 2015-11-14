package com.smilexi.sx.adapter;

import java.io.IOException;

import com.smilexi.sx.R;
import com.smilexi.sx.util.Bimp;
import com.smilexi.sx.util.FileUtils;
import com.smilexi.sx.util.L;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


public class ImgGridAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private int selectedPosition = -1;
	private boolean shape;
	private int maxImageCount = 6;// �?多显�?6张图�?

	public boolean isShape() {
		return shape;
	}

	public void setShape(boolean shape) {
		this.shape = shape;
	}

	public ImgGridAdapter(Context context) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
	}

	public void update() {
		loading();
	}

	@Override
	public int getCount() {
		return (Bimp.bmp.size() + 1);
	}

	@Override
	public Object getItem(int arg0) {

		return null;
	}

	@Override
	public long getItemId(int arg0) {

		return 0;
	}

	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}

	public int getSelectedPosition() {
		return selectedPosition;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_published_grida, parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position == Bimp.bmp.size()) {
			holder.image.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_addpic_unfocused));
			if (position == maxImageCount) {
				holder.image.setVisibility(View.GONE);
			}
		} else {
			holder.image.setImageBitmap(Bimp.bmp.get(position));
		}

		return convertView;
	}

	public class ViewHolder {
		public ImageView image;
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				notifyDataSetChanged();
				break;
			}
			super.handleMessage(msg);
		}
	};

	private void loading() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (Bimp.max == Bimp.drr.size()) {
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
						break;
					} else {
						try {
							String path = Bimp.drr.get(Bimp.max);
							Bitmap bm = Bimp.revitionImageSize(path);

							int angle = getExifOrientation(path);
							if (angle != 0) { // 如果照片出现�? 旋转 那么 就更改旋转度�?
								Matrix matrix = new Matrix();
								matrix.postRotate(angle);
								bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
							}

							Bimp.bmp.add(bm);
							String newStr = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
							FileUtils.saveBitmap(bm, "" + newStr);
							Bimp.max += 1;
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						} catch (IOException e) {

							e.printStackTrace();
						}
					}
				}
			}
		}).start();
	}

	private int getExifOrientation(String filepath) {
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
}