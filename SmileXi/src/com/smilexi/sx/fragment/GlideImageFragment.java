package com.smilexi.sx.fragment;

import java.util.List;

import com.smilexi.sx.R;
import com.smilexi.sx.util.Tool;
import com.smilexi.sx.widget.HackyViewPager;
import com.squareup.picasso.Callback;

import uk.co.senab.photoview.PhotoView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class GlideImageFragment extends Fragment implements OnPageChangeListener {
	/**
	 * ViewPager
	 */
	private HackyViewPager viewPager;

	/**
	 * 装点点的ImageView数组
	 */
	private ImageView[] tips;

	/**
	 * 装ImageView数组
	 */
	private PhotoView[] mImageViews;

	private List<String> mPhotos;

	private int mCurrentIndex;

	private Animation loadingAnim;
	private ImageView loadingImage;
	private LinearLayout loadingLine;


	public void init(List<String> photoUrls, int currentSelected) {
		this.mPhotos = photoUrls;
		this.mCurrentIndex = currentSelected;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// return super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fra_viewgroup, container, false);
		ViewGroup group = (ViewGroup) view.findViewById(R.id.viewGroup);
		viewPager = (HackyViewPager) view.findViewById(R.id.viewPager);

		loadingImage = (ImageView) view.findViewById(R.id.loading_inner_image);
		loadingLine = (LinearLayout) view.findViewById(R.id.loading_line);

		if (loadingAnim == null) {
			loadingAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.loading_3);
			LinearInterpolator lin = new LinearInterpolator();
			loadingAnim.setInterpolator(lin);
			if (loadingAnim != null) {
				loadingImage.startAnimation(loadingAnim);
				loadingAnim.startNow();
			}
		} else {
			loadingImage.startAnimation(loadingAnim);
			loadingAnim.startNow();
		}

		// 将点点加入到ViewGroup�?
		tips = new ImageView[mPhotos.size()];
		for (int i = 0; i < tips.length; i++) {
			ImageView imageView = new ImageView(this.getActivity());
			imageView.setLayoutParams(new LayoutParams(8, 8));
			tips[i] = imageView;
			if (i == 0) {
				tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
			}

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			layoutParams.leftMargin = 5;
			layoutParams.rightMargin = 5;
			group.addView(imageView, layoutParams);
		}

		mImageViews = new PhotoView[mPhotos.size()];

		for (int i = 0; i < mImageViews.length; i++) {
			PhotoView imageView = new PhotoView(this.getActivity());
			mImageViews[i] = imageView;
			// imageView.setBackgroundResource(imgIdArray[i]);
		}

		// showCurrentImage();

		// 设置Adapter
		// viewPager.setAdapter(new MyAdapter());
		viewPager.setAdapter(new MyAdapter());
		// 设置监听，主要是设置点点的背�?
		viewPager.setOnPageChangeListener(this);
		// 设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑�?
		viewPager.setCurrentItem(mCurrentIndex);

		return view;
	}

	public class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mImageViews.length;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(container.getContext());
			photoView = mImageViews[position];
			if (viewPager.getCurrentItem() == position) {
				showCurrentImage(position);
			}
			// Now just add PhotoView to ViewPager and return it
			container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			photoView.setLongClickable(true);
			photoView.setClickable(true);
			photoView.setOnClickListener(onClickLis);

//			photoView.setOnLongClickListener(new SavaPicOnLongClickLis(getActivity(), photoView));
			return photoView;
		}

		private OnClickListener onClickLis = new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getActivity().finish();
				getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			}
		};

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
	}

	/*
	 * public class MyAdapter extends PagerAdapter{
	 * 
	 * @Override public int getCount() { //return Integer.MAX_VALUE; return
	 * mImageViews.length; }
	 * 
	 * @Override public boolean isViewFromObject(View arg0, Object arg1) {
	 * return arg0 == arg1; }
	 * 
	 * @Override public void destroyItem(View container, int position, Object
	 * object) { ((HackyViewPager)container).removeView(mImageViews[position %
	 * mImageViews.length]);
	 * 
	 * }
	 *//**
	 * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
	 */
	/*
	 * @Override public Object instantiateItem(View container, int position) {
	 * int index = position % mImageViews.length; if (viewPager.getCurrentItem()
	 * == position){ showCurrentImage(position); }
	 * ((ViewPager)container).addView(mImageViews[index], 0); return
	 * mImageViews[index]; }
	 * 
	 * 
	 * 
	 * }
	 */

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		mCurrentIndex = arg0 % mImageViews.length;
		showCurrentImage(arg0 % mImageViews.length);
		setImageBackground(arg0 % mImageViews.length);

	}

	/**
	 * 设置选中的tip的背�?
	 * 
	 * @param selectItems
	 */
	private void setImageBackground(int selectItems) {
		for (int i = 0; i < tips.length; i++) {
			if (i == selectItems % mImageViews.length) {
				tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
		}
	}

	private void showCurrentImage(int index) {
		loadingLine.setVisibility(View.VISIBLE);
		Tool.imageLoader(getActivity(), mImageViews[index], mPhotos.get(index), new Callback() {

			@Override
			public void onError() {
				loadingLine.setVisibility(View.GONE);
			}

			@Override
			public void onSuccess() {
				loadingLine.setVisibility(View.GONE);
			}
		});
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

}
