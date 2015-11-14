package com.smilexi.sx.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.smilexi.sx.R;
import com.smilexi.sx.activity.ImageViewActivity;
import com.smilexi.sx.common.SXContext;
import com.smilexi.sx.util.AsyncImageTask;
import com.smilexi.sx.util.Tool;
import com.smilexi.sx.widget.CGridView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DynamicPhotoAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private CGridView mGrid;
	private List<String> mPhotos;
	private int myId;
	private int mPosition; // ��view�ı��
	private AsyncImageTask mImgTask;

	public DynamicPhotoAdapter(Context context, CGridView grid,
			List<String> photos, int position) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		this.mGrid = grid;
		this.mPosition = position;
		this.mPhotos = photos == null ? new ArrayList<String>() : photos;
		this.myId = SXContext.getInstance().getUserInfo().getUserid();
		this.mImgTask = new AsyncImageTask();
	}
	public DynamicPhotoAdapter(Context context, CGridView grid,
			List<String> photos){
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		this.mGrid = grid;
		this.mPhotos = photos == null ? new ArrayList<String>() : photos;
		this.myId = SXContext.getInstance().getUserInfo().getUserid();
		this.mImgTask = new AsyncImageTask();
	}

	@Override
	public int getCount() {
		return mPhotos.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mPhotos.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.adapter_photo_item, null);

			holder = new ViewHolder(convertView);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String path = mPhotos.get(position);
		holder.photo.setTag(path);
		holder.photo.setImageResource(R.drawable.defaultheader);
		if (holder.photo.getTag() != null && holder.photo.getTag().equals(path))
			Tool.imageLoader(mContext, holder.photo, path, null);

		holder.photo.setOnClickListener(new ImageOnClick(position));
		holder.val.setText(String.valueOf(mPosition));

		return convertView;
	}

	class ImageOnClick implements OnClickListener {
		private int mIndex;

		public ImageOnClick(int imageIndex) {
			mIndex = imageIndex;
		}

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(mContext, ImageViewActivity.class);

			Bundle bundle = new Bundle();
			bundle.putSerializable(ImageViewActivity.IMAGE_URLS_KEY,
					(Serializable) mPhotos);
			bundle.putInt(ImageViewActivity.SELECT_INDEX_KEY, mIndex);
			intent.putExtras(bundle);

			mContext.startActivity(intent);
			((Activity) mContext).overridePendingTransition(
					android.R.anim.fade_in, android.R.anim.fade_out);

		}
	}

	private static class ViewHolder {
		public ImageView photo;
		public TextView val;

		ViewHolder(View view) {
			photo = (ImageView) view.findViewById(R.id.adapter_photo_img);
			val = (TextView) view.findViewById(R.id.adapter_photo_text);
		}
	}
}
