package com.smilexi.sx.activity;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smilexi.sx.R;
import com.smilexi.sx.adapter.PhotoPagerAdapter;
import com.smilexi.sx.app.BaseActivity;
import com.smilexi.sx.util.Bimp;
import com.smilexi.sx.util.FileUtils;
import com.smilexi.sx.widget.HackyViewPager;


public class PhotoActivity extends BaseActivity {

	private RelativeLayout titleLeftRel;
	private ImageView titleLeftImage;
	private RelativeLayout titleRightRel;
	private ImageView titleRightImage;
	private TextView titleCenter;
//	private TextView titleRight;

	private ArrayList<View> listViews = null;
	private HackyViewPager pager;
	// private MyPageAdapter adapter;
	private PhotoPagerAdapter adapter;
	private int count;

	public List<Bitmap> bmp = new ArrayList<Bitmap>();
	public List<String> drr = new ArrayList<String>();
	public List<String> del = new ArrayList<String>();
	public int max;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_photo);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_left_center_right);

		// title bar
		titleLeftRel = (RelativeLayout) findViewById(R.id.titlebar_lcr_left_rel);
		titleLeftImage = (ImageView) findViewById(R.id.titlebar_lcr_left_image);
		titleCenter = (TextView) findViewById(R.id.titlebar_lcr_center_text);
		titleRightRel = (RelativeLayout) findViewById(R.id.titlebar_lcr_right_rel);
		titleRightImage = (ImageView) findViewById(R.id.titlebar_lcr_right_image);
//		titleRight = (TextView) findViewById(R.id.title_right_tv);

		
		titleLeftRel.setVisibility(View.VISIBLE);
		titleRightRel.setVisibility(View.VISIBLE);
		titleLeftImage.setImageResource(R.drawable.back);
		titleCenter.setText("");
		titleRightImage.setImageResource(R.drawable.del);
//		titleRight.setVisibility(View.GONE);

		for (int i = 0; i < Bimp.bmp.size(); i++) {
			bmp.add(Bimp.bmp.get(i));
		}
		for (int i = 0; i < Bimp.drr.size(); i++) {
			drr.add(Bimp.drr.get(i));
		}
		max = Bimp.max;

		titleLeftRel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Bimp.bmp = bmp;
				Bimp.drr = drr;
				Bimp.max = max;
				for (int i = 0; i < del.size(); i++) {
					FileUtils.delFile(del.get(i) + ".JPEG");
				}
				finish();
			}
		});
		titleRightRel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listViews.size() == 1) {
					Bimp.bmp.clear();
					Bimp.drr.clear();
					Bimp.max = 0;
					FileUtils.deleteDir();
					finish();
				} else {
					String newStr = drr.get(count).substring(
							drr.get(count).lastIndexOf("/") + 1,
							drr.get(count).lastIndexOf("."));
					bmp.remove(count);
					drr.remove(count);
					del.add(newStr);
					max--;
					pager.removeAllViews();
					listViews.remove(count);
					adapter.setListViews(listViews);
					adapter.notifyDataSetChanged();
				}
			}
		});

		pager = (HackyViewPager) findViewById(R.id.viewpager);
		pager.setOnPageChangeListener(pageChangeListener);
		for (int i = 0; i < bmp.size(); i++) {
			initListViews(bmp.get(i));//
		}

		// adapter = new MyPageAdapter(listViews);
		adapter = new PhotoPagerAdapter(listViews);
		pager.setAdapter(adapter);
		Intent intent = getIntent();
		int id = intent.getIntExtra("ID", 0);
		pager.setCurrentItem(id);
	}

	private void initListViews(Bitmap bm) {
		if (listViews == null)
			listViews = new ArrayList<View>();
		PhotoView img = new PhotoView(this);
		img.setBackgroundColor(0xff000000);
		img.setImageBitmap(bm);
		img.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		listViews.add(img);
	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			count = arg0;
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	};

	/*
	 * class MyPageAdapter extends PagerAdapter {
	 * 
	 * private ArrayList<View> listViews;// content
	 * 
	 * private int size;
	 * 
	 * public MyPageAdapter(ArrayList<View> listViews) {
	 * 
	 * this.listViews = listViews; size = listViews == null ? 0 :
	 * listViews.size(); }
	 * 
	 * public void setListViews(ArrayList<View> listViews) { this.listViews =
	 * listViews; size = listViews == null ? 0 : listViews.size(); }
	 * 
	 * @Override public int getCount() { return size; }
	 * 
	 * @Override public int getItemPosition(Object object) { return
	 * POSITION_NONE; }
	 * 
	 * @Override public void destroyItem(View arg0, int arg1, Object arg2) {
	 * ((ViewPager) arg0).removeView(listViews.get(arg1 % size)); }
	 * 
	 * @Override public void finishUpdate(View arg0) { }
	 * 
	 * @Override public Object instantiateItem(View arg0, int arg1) { try {
	 * ((ViewPager) arg0).addView(listViews.get(arg1 % size), 0);
	 * 
	 * } catch (Exception e) { } return listViews.get(arg1 % size); }
	 * 
	 * @Override public boolean isViewFromObject(View arg0, Object arg1) {
	 * return arg0 == arg1; }
	 * 
	 * }
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Bimp.bmp = bmp;
			Bimp.drr = drr;
			Bimp.max = max;
			for (int i = 0; i < del.size(); i++) {
				FileUtils.delFile(del.get(i) + ".JPEG");
			}
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
