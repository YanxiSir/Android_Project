package com.xiaowu.activity;

import java.util.ArrayList;

import com.xiaowu.fragment.ClassifyMenuFragment;
import com.xiaowu.fragment.ShopIntrodutionFragment;
import com.xiaowu.projectdesign.R;
import com.xiaowu.protocol.XwShopList.ShopInfos;
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
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShopPage extends FragmentActivity {

	private RelativeLayout titleLeftRel, titleRightRel;
	private ImageView titleLeftImage;
	private TextView titleCenterText;

	private TextView classify_menu_list, shop_introduction_list;
	private ViewPager viewpager;
	private ImageView cursorImage;
	private int cursor_width;// 横线图片的宽度
	private int offset;// 图片移动的偏移量
	private ArrayList<Fragment> fragmentList;
	private int curIndex;

	private int sid;
	private ShopInfos shopInfo;
	private String shopName = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.shop_page);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_left_center_right);

		shopInfo = (ShopInfos) getIntent().getExtras().getSerializable("shopInfo");
		sid = shopInfo.getShopId();
		shopName = shopInfo.getShopName();
		initTitlebar();

		classify_menu_list = (TextView) findViewById(R.id.classify_menu_list);
		shop_introduction_list = (TextView) findViewById(R.id.shop_introduction_list);
		setTextStyle(classify_menu_list, shop_introduction_list);
		classify_menu_list.setOnClickListener(new tabClickListener(0));
		shop_introduction_list.setOnClickListener(new tabClickListener(1));
		initImage();
		SetViewPagerAdapter();
	}

	private void initTitlebar() {
		titleLeftRel = (RelativeLayout) findViewById(R.id.titlebar_lcr_left_rel);
		titleRightRel = (RelativeLayout) findViewById(R.id.titlebar_lcr_right_rel);
		titleCenterText = (TextView) findViewById(R.id.titlebar_lcr_center_text);
		titleLeftImage = (ImageView) findViewById(R.id.titlebar_lcr_left_image);

		titleLeftRel.setVisibility(View.VISIBLE);
		titleRightRel.setVisibility(View.GONE);

		titleLeftImage.setImageDrawable(getResources().getDrawable(
				R.drawable.back));
		titleCenterText.setText(shopName);

		titleLeftRel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				onBackPressed();
			}
		});

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
		cursorImage = (ImageView) findViewById(R.id.shop_page_cursor);
		cursor_width = BitmapFactory.decodeResource(getResources(),
				R.drawable.cursor).getWidth();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		offset = (screenW / 2 - cursor_width) / 2;
		Matrix matrix = new Matrix();
		LayoutParams pa = cursorImage.getLayoutParams();
		pa.width = screenW / 2;
		pa.height = 6;
		cursorImage.setLayoutParams(pa);
		cursorImage.setImageMatrix(matrix);
	}

	public void SetViewPagerAdapter() {
		viewpager = (ViewPager) findViewById(R.id.shop_viewpager);

		fragmentList = new ArrayList<Fragment>();

		Fragment firstFragment = new ClassifyMenuFragment();
		Fragment secondFragment = new ShopIntrodutionFragment();

		fragmentList.add(firstFragment);
		fragmentList.add(secondFragment);
		viewpager.setAdapter(new MyPageAdapter(getSupportFragmentManager(),
				fragmentList));
		viewpager.setCurrentItem(0);
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
			switch (curIndex) {
			case 0:
				setTextStyle(classify_menu_list, shop_introduction_list);
				break;
			case 1:

				setTextStyle(shop_introduction_list, classify_menu_list);
				break;

			default:
				break;
			}
		}

	}

	public void setTextStyle(TextView v1, TextView v2) {
		v1.getPaint().setFakeBoldText(true);
		v2.getPaint().setFakeBoldText(false);
		v1.setTextColor(getResources().getColor(R.color.white));
		v2.setTextColor(getResources().getColor(R.color.right_color_gray));
	}

	public int getSid() {
		return sid;
	}

	public ShopInfos getShopInfo() {
		return shopInfo;
	}

	
	

}
