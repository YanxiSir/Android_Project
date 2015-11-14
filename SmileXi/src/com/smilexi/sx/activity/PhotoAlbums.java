package com.smilexi.sx.activity;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;







import com.smilexi.sx.R;
import com.smilexi.sx.adapter.ImageBucketAdapter;
import com.smilexi.sx.app.BaseActivity;
import com.smilexi.sx.entity.ImageBucket;
import com.smilexi.sx.util.AlbumHelper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

/**
 * �ֻ������е����?
 * 
 */
public class PhotoAlbums extends BaseActivity {

	public static final String EXTRA_IMAGE_LIST = "imagelist";
	public static Bitmap bimap;

	private RelativeLayout titleLeftRel;
	private ImageView titleLeftImage;
	private RelativeLayout titleRightRel;
	private ImageView titleRightImage;
	private TextView titleCenter;

	List<ImageBucket> dataList;
	GridView photoAlbumsGrid;
	ImageBucketAdapter adapter;
	AlbumHelper helper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.act_photoalbums);
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
		

		titleLeftImage.setVisibility(View.GONE);
		titleCenter.setText("��Ჾ");
		titleRightImage.setImageResource(R.drawable.close);

		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		dataList = helper.getImagesBucketList(true);
				

		Collections.sort(dataList, new ComparatorImageBucket());
	
		bimap = BitmapFactory.decodeResource(getResources(),
				R.drawable.icon_addpic_unfocused);

		photoAlbumsGrid = (GridView) findViewById(R.id.photoalbums_grid);
		adapter = new ImageBucketAdapter(PhotoAlbums.this, dataList);
		photoAlbumsGrid.setAdapter(adapter);

		titleRightRel.setOnClickListener(titleRightRelClickLis);
		photoAlbumsGrid.setOnItemClickListener(gridViewItemClickLis);
	}

	private OnClickListener titleRightRelClickLis = new OnClickListener() {

		@Override
		public void onClick(View v) {
			PhotoAlbums.this.finish();
		}
	};

	private OnItemClickListener gridViewItemClickLis = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			Intent intent = new Intent(PhotoAlbums.this,
					PhotoAlbumToPicture.class);
			intent.putExtra(PhotoAlbums.EXTRA_IMAGE_LIST,
					(Serializable) dataList.get(position).imageList);
			startActivity(intent);
			finish();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			this.setResult(RESULT_OK, data); // ����������ѷ�����ѡ��ĳ���setResult��Activity
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	class ComparatorImageBucket implements Comparator {

		@Override
		public int compare(Object arg0, Object arg1) {
			return ((ImageBucket) arg1).imageList.size()
					- ((ImageBucket) arg0).imageList.size();
		}

	}
}
