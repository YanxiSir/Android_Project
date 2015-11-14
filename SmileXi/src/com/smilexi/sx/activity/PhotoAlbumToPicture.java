package com.smilexi.sx.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.smilexi.sx.R;
import com.smilexi.sx.adapter.PhotoAlbumToPictureAdapter;
import com.smilexi.sx.adapter.PhotoAlbumToPictureAdapter.TextCallback;
import com.smilexi.sx.entity.ImageItem;
import com.smilexi.sx.util.AlbumHelper;
import com.smilexi.sx.util.Bimp;
import com.smilexi.sx.util.T;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class PhotoAlbumToPicture extends Activity {
	public static final String EXTRA_IMAGE_LIST = "imagelist";

	private RelativeLayout titleLeftRel;
	private ImageView titleLeftImage;
	private RelativeLayout titleRightRel;
	private ImageView titleRightImage;
	private TextView titleCenter;

	List<ImageItem> dataList;
	GridView picturesGrid;
	PhotoAlbumToPictureAdapter adapter;
	AlbumHelper helper;

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				T.showShort(PhotoAlbumToPicture.this, "最多选择6张图片");
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.act_photoalbumtopicture);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_left_center_right);

		// title bar
		titleLeftRel = (RelativeLayout) findViewById(R.id.titlebar_lcr_left_rel);
		titleLeftImage = (ImageView) findViewById(R.id.titlebar_lcr_left_image);
		titleCenter = (TextView) findViewById(R.id.titlebar_lcr_center_text);
		titleRightRel = (RelativeLayout) findViewById(R.id.titlebar_lcr_right_rel);
		titleRightImage = (ImageView) findViewById(R.id.titlebar_lcr_right_image);
		
		titleLeftRel.setVisibility(View.VISIBLE);
		titleRightRel.setVisibility(View.VISIBLE);

		titleLeftImage.setImageResource(R.drawable.close);
		titleCenter.setText("相册");
		titleRightImage.setImageResource(R.drawable.right);

		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		dataList = (List<ImageItem>) getIntent().getSerializableExtra(
				EXTRA_IMAGE_LIST);

		Collections.sort(dataList, new ComparatorAlbumId());

		picturesGrid = (GridView) findViewById(R.id.pictures_grid);

		picturesGrid.setSelector(new ColorDrawable(Color.TRANSPARENT));

		adapter = new PhotoAlbumToPictureAdapter(PhotoAlbumToPicture.this,
				dataList, mHandler);
		picturesGrid.setAdapter(adapter);
		adapter.setTextCallback(new TextCallback() {
			@Override
			public void onListen(int count) {
				// submitBtn.setText("完成" + "(" + count + ")");
			}
		});

		titleLeftRel.setOnClickListener(titleLeftRelClickLis);
		titleRightRel.setOnClickListener(titleRightRelClickLis);
		picturesGrid.setOnItemClickListener(gridViewItemClickLis);
	}

	private OnClickListener titleLeftRelClickLis = new OnClickListener() {

		@Override
		public void onClick(View v) {
//			PhotoAlbumToPicture.this.finish();
			Intent i = new Intent(getApplication(),PhotoAlbums.class);
			startActivityForResult(i, 10);
			finish();
		}
	};

	private OnClickListener titleRightRelClickLis = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ArrayList<String> list = new ArrayList<String>();
			Collection<String> c = adapter.map.values();
			Iterator<String> it = c.iterator();
			for (; it.hasNext();) {
				list.add(it.next());
			}

			if (Bimp.act_bool) {
				Bimp.act_bool = false;
			}
			for (int i = 0; i < list.size(); i++) {
				if (Bimp.drr.size() < 6) {
					Bimp.drr.add(list.get(i));
				}
			}
			setResult(RESULT_OK);
			finish();
			
		}
	};

	private OnItemClickListener gridViewItemClickLis = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// if(dataList.get(position).isSelected()){
			// dataList.get(position).setSelected(false);
			// }else{
			// dataList.get(position).setSelected(true);
			// }

			adapter.notifyDataSetChanged();
		}
	};

	class ComparatorAlbumId implements Comparator {

		@Override
		public int compare(Object arg0, Object arg1) {
			return Integer.valueOf(((ImageItem) arg1).imageId)
					- Integer.valueOf(((ImageItem) arg0).imageId);
		}

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent i = new Intent(getApplication(),PhotoAlbums.class);
			startActivity(i);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
