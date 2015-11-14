package com.xiaowu.activity;

import java.util.ArrayList;

import com.xiaowu.baidumap.BaiduMapActivity;
import com.xiaowu.fragment.Fragment1;
import com.xiaowu.fragment.Fragment2;
import com.xiaowu.fragment.Fragment3;
import com.xiaowu.projectdesign.R;

import android.os.Bundle;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainPage extends FragmentActivity {

	/*
	 * titlebar
	 */
	private RelativeLayout titleLeftRel, titleRightRel_1, titleRightRel_2;
	private LinearLayout titleLeftLl;
	private TextView titleLeftText;
	private ImageView titleRightImage_1, titleRightImage_2;

	private TextView menulist, orderlist, myInfo;
	private ViewPager viewpager;
	private ImageView cursorImage;
	private int cursor_width;// 横线图片的宽度
	private int offset;// 图片移动的偏移量
	private ArrayList<Fragment> fragmentList;
	private int curIndex;

	// 定位相关
	private BaiduMapActivity baidu_map;

	/*
	 * Handler mHandler = new Handler(){ public void
	 * handleMessage(android.os.Message msg) { switch (msg.what) { case
	 * SelectPopup.SELECT_ID: Bundle b = (Bundle) msg.obj; int id =
	 * b.getInt("oid"); T.showShort(MainPage.this, "当前点击的第"+id+"个"); break;
	 * 
	 * default: break; } }; };
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main_page);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_ltext_rtwo_btn);
		InitTitle();

		menulist = (TextView) findViewById(R.id.menu);
		orderlist = (TextView) findViewById(R.id.order);
		myInfo = (TextView) findViewById(R.id.My);
		setTextStyle(menulist, orderlist, myInfo);
		menulist.setOnClickListener(new tabClickListener(0));
		orderlist.setOnClickListener(new tabClickListener(1));
		myInfo.setOnClickListener(new tabClickListener(2));
		initImage();
		SetViewPagerAdapter();

	}

	private void InitTitle() {
		baidu_map = new BaiduMapActivity();

		titleLeftRel = (RelativeLayout) findViewById(R.id.title_tbb_left_rel);
		titleLeftLl = (LinearLayout) findViewById(R.id.title_tbb_left_ll);
		titleLeftText = (TextView) findViewById(R.id.title_tbb_left_text);
		titleRightRel_1 = (RelativeLayout) findViewById(R.id.title_tbb_right_rel_one);
		titleRightRel_2 = (RelativeLayout) findViewById(R.id.title_tbb_right_rel_two);
		titleRightImage_1 = (ImageView) findViewById(R.id.title_tbb_right_image_one);
		titleRightImage_2 = (ImageView) findViewById(R.id.title_tbb_right_image_two);

		titleLeftRel.setVisibility(View.GONE);
		titleRightRel_1.setVisibility(View.GONE);

		titleLeftLl.setVisibility(View.VISIBLE);
		titleRightRel_2.setVisibility(View.VISIBLE);

		titleRightImage_2.setImageDrawable(getResources().getDrawable(
				R.drawable.search));
	}

	private void setTitle() {
		if (curIndex == 0) {
			titleRightRel_2.setVisibility(View.VISIBLE);
			titleLeftText.setText("定位");
		} else if (curIndex == 1) {
			titleRightRel_2.setVisibility(View.GONE);
			titleLeftText.setText("订单");
		} else if (curIndex == 2) {
			titleRightRel_2.setVisibility(View.GONE);
			titleLeftText.setText("我的");
		}
	}

	public class tabClickListener implements OnClickListener {
		private int index = 0;

		public tabClickListener(int index) {
			this.index = index;
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			viewpager.setCurrentItem(this.index);
		}

	}

	// 初始化图片的位移像素
	public void initImage() {
		cursorImage = (ImageView) findViewById(R.id.cursor);
		cursor_width = BitmapFactory.decodeResource(getResources(),
				R.drawable.cursor).getWidth();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		offset = (screenW / 3 - cursor_width) / 2;
		Matrix matrix = new Matrix();
		LayoutParams pa = cursorImage.getLayoutParams();
		pa.width = screenW / 3;
		pa.height = 6;
		cursorImage.setLayoutParams(pa);
		cursorImage.setImageMatrix(matrix);
	}

	public void SetViewPagerAdapter() {
		viewpager = (ViewPager) findViewById(R.id.viewpager);

		fragmentList = new ArrayList<Fragment>();

		Fragment firstFragment = new Fragment1();
		Fragment secondFragment = new Fragment2();
		Fragment thirdFragment = new Fragment3();

		fragmentList.add(firstFragment);
		fragmentList.add(secondFragment);
		fragmentList.add(thirdFragment);
		viewpager.setAdapter(new MyPageAdapter(getSupportFragmentManager(),
				fragmentList));
		viewpager.setCurrentItem(0);
		setTitle();
		viewpager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	public class MyPageAdapter extends FragmentPagerAdapter {
		ArrayList<Fragment> list = null;

		public MyPageAdapter(FragmentManager fm, ArrayList<Fragment> list) {
			super(fm);
			this.list = list;

			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {
		private int one = offset * 2 + cursor_width; // 两个相邻页面的偏移量

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = new TranslateAnimation(curIndex * one, arg0
					* one, 0, 0);
			curIndex = arg0;
			animation.setFillAfter(true);// 东华终止时停留在最后一帧
			animation.setDuration(200);
			cursorImage.startAnimation(animation);
			// T.showShort(getApplicationContext(), "当前 "+i+" 个页面");
			setTitle();
			switch (curIndex) {
			case 0:
				setTextStyle(menulist, orderlist, myInfo);
				break;
			case 1:

				setTextStyle(orderlist, menulist, myInfo);
				break;
			case 2:
				setTextStyle(myInfo, orderlist, menulist);
				break;

			default:
				break;
			}
		}

	}

	public void setTextStyle(TextView v1, TextView v2, TextView v3) {
		v1.getPaint().setFakeBoldText(true);
		v2.getPaint().setFakeBoldText(false);
		v3.getPaint().setFakeBoldText(false);
		v1.setTextColor(getResources().getColor(R.color.white));
		v2.setTextColor(getResources().getColor(R.color.right_color_gray));
		v3.setTextColor(getResources().getColor(R.color.right_color_gray));
	}

	// class MyReceiver extends BroadcastReceiver{
	//
	// @Override
	// public void onReceive(Context arg0, Intent intent) {
	// // TODO Auto-generated method stub
	// Bundle bundle=intent.getExtras();
	// String addr=bundle.getString("LocationAddr");
	// main_title_text.setText(addr);
	// }
	//
	// }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_page, menu);
		return true;
	}

}
