package com.smilexi.sx.adapter;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class PhotoPagerAdapter extends PagerAdapter {
	ArrayList<View> listViews;
	private int size;

	public PhotoPagerAdapter(ArrayList<View> listViews) {
		this.listViews = listViews;
		size = listViews == null ? 0 : listViews.size();
	}
	public void setListViews(ArrayList<View> listViews) {
		this.listViews = listViews;
		size = listViews == null ? 0 : listViews.size();
	}

	@Override
	public int getCount() {
		return size;
	}

	@Override
	public View instantiateItem(ViewGroup container, int position) {
		PhotoView photoView = new PhotoView(container.getContext());
		photoView = (PhotoView) listViews.get(position);

		// Now just add PhotoView to ViewPager and return it
		container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		return photoView;
	}
	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return POSITION_NONE;
	}
	@Override
	public void finishUpdate(View container) {
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

}


