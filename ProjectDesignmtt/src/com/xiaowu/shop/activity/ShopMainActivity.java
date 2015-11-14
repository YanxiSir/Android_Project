package com.xiaowu.shop.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaowu.common.XwContext;
import com.xiaowu.projectdesign.R;
import com.xiaowu.protocol.XwShopMenu;
import com.xiaowu.protocol.XwShopMenu.ClassifyMenu.ShopMenu;
import com.xiaowu.shop.fragment.ShopBasicInfoFragment;
import com.xiaowu.shop.fragment.ShopMenuInfoFragment;
import com.xiaowu.shop.fragment.ShopOrderFragment;
import com.xiaowu.shop.interfaces.RefreshFragment;
import com.xiaowu.shop.interfaces.RefreshMenu;
import com.xiaowu.shop.protocol.XwShopInfo;
import com.xiaowu.utils.Tool;
import com.xiaowu.widget.SelectDialog;

public class ShopMainActivity extends FragmentActivity {

	public static final int EDIT_MENU = 10;

	public static final int REQUESTCODE_UPDATE = 20;
	
	public static final int TYPE_LONG_CLICK_MENU = 100;

	public static final int MENU_DEL = 2;
	public static final int MENU_EDIT = 1;

	/*
	 * titlebar
	 */
	private RelativeLayout titleLeftRel, titleRightRel;
	private ImageView titleLeftImage, titleRightImage;
	private TextView titleLeftText, titleRightText;

	private TextView basicBtn, menuBtn, orderBtn;
	private ViewPager viewpager;
	private ImageView cursorImage;
	private int cursor_width;// 横线图片的宽度
	private int offset;// 图片移动的偏移量
	private ArrayList<Fragment> fragmentList;
	private int curIndex;

	private XwShopInfo shopInfo;
	private boolean isEdit = false;

	private SelectDialog selectDialog;

	int mid = -1;
	int gPosition = -1,cPosition = -1;

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Bundle b = null;
			switch (msg.what) {
			case TYPE_LONG_CLICK_MENU:
				b = (Bundle) msg.obj;
				mid = b.getInt("mid");
				gPosition = b.getInt("gPosition");
				cPosition = b.getInt("cPosition");

				List<String> list = new ArrayList<String>();
				list.add("编辑");
				list.add("删除");
				selectDialog = new SelectDialog(ShopMainActivity.this,
						mHandler, list);

				break;
			case SelectDialog.DIALOG_SELECT_OPERATE:
				b = (Bundle) msg.obj;
				int selectid = b.getInt("selectid");
				Bundle bundle = new Bundle();
				if (selectid == 0) {
					bundle.putInt("oid", MENU_EDIT);
					bundle.putInt("mid", mid);
					bundle.putInt("gPosition", gPosition);
					bundle.putInt("cPosition", cPosition);
				} else if (selectid == 1) {
					bundle.putInt("oid", MENU_DEL);
					bundle.putInt("mid", mid);
				}
				((RefreshMenu) fragmentList.get(1)).refreshMenu(bundle);
				break;
			default:
				break;
			}
		};
	};

	public Handler getmHandler() {
		return mHandler;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.shop_act_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_lbtn_text_rbtn_text);

		shopInfo = XwContext.getInstance().getShopInfo();

		InitTitle();
		initUI();
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

		fragmentList = new ArrayList<Fragment>();

		Fragment fra1 = new ShopBasicInfoFragment();
		Fragment fra2 = new ShopMenuInfoFragment();
		Fragment fra3 = new ShopOrderFragment();

		fragmentList.add(fra1);
		fragmentList.add(fra2);
		fragmentList.add(fra3);
		viewpager.setAdapter(new MyPageAdapter(getSupportFragmentManager(),
				fragmentList));
		viewpager.setCurrentItem(0);
		setTitle();
		// if (!isEdit) {
		// ((ShopBasicInfoFragment) fragmentList.get(0)).setEditEnable(false);
		// titleRightText.setText("编辑");
		// }
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
			// setTitle();
			switch (curIndex) {
			case 0:
				setTitle();
				setTextStyle(basicBtn, menuBtn, orderBtn);
				break;
			case 1:
				setTitle();
				setTextStyle(menuBtn, basicBtn, orderBtn);
				break;
			case 2:
				setTitle();
				setTextStyle(orderBtn, menuBtn, basicBtn);
				break;

			default:
				break;
			}
		}

	}

	private void setTitle() {
		if (curIndex == 0) {
			titleRightRel.setVisibility(View.VISIBLE);
			titleLeftRel.setClickable(true);
			titleRightText.setText("编辑");
		} else if (curIndex == 1) {
			titleRightRel.setVisibility(View.VISIBLE);
			titleRightRel.setClickable(true);
			titleRightText.setText("管理");
		} else {
			titleLeftRel.setClickable(false);
			titleRightRel.setVisibility(View.GONE);
		}
	}

	public void setTextStyle(TextView v1, TextView v2, TextView v3) {
		v1.getPaint().setFakeBoldText(true);
		v2.getPaint().setFakeBoldText(false);
		v2.getPaint().setFakeBoldText(false);
		v1.setTextColor(getResources().getColor(R.color.white));
		v2.setTextColor(getResources().getColor(R.color.right_color_gray));
		v3.setTextColor(getResources().getColor(R.color.right_color_gray));
	}

	private void initUI() {
		basicBtn = (TextView) findViewById(R.id.shop_tv_guid1);
		menuBtn = (TextView) findViewById(R.id.shop_tv_guid2);
		orderBtn = (TextView) findViewById(R.id.shop_tv_guid3);
		viewpager = (ViewPager) findViewById(R.id.shop_viewpager);
		cursorImage = (ImageView) findViewById(R.id.shop_cursor);

		setTextStyle(basicBtn, menuBtn, orderBtn);
		basicBtn.setOnClickListener(new tabClickListener(0));
		menuBtn.setOnClickListener(new tabClickListener(1));
		orderBtn.setOnClickListener(new tabClickListener(2));
		initImage();
		SetViewPagerAdapter();

	}

	private void InitTitle() {
		Tool.hideSoft(ShopMainActivity.this);
		titleLeftRel = (RelativeLayout) findViewById(R.id.titlebar_lrbt_left_rel);
		titleLeftImage = (ImageView) findViewById(R.id.titlebar_lrbt_left_image);
		titleLeftText = (TextView) findViewById(R.id.titlebar_lrbt_left_text);
		titleRightRel = (RelativeLayout) findViewById(R.id.titlebar_lrbt_right_rel);
		titleRightImage = (ImageView) findViewById(R.id.titlebar_lrbt_right_image);
		titleRightText = (TextView) findViewById(R.id.titlebar_lrbt_right_text);

		titleLeftRel.setVisibility(View.GONE);
		titleRightImage.setVisibility(View.GONE);
		titleRightRel.setClickable(true);

		titleLeftText.setText(shopInfo.getName());

		titleRightRel.setOnClickListener(titleRightRelClickLis);
	}

	private OnClickListener titleRightRelClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (curIndex == 0) {
				if (!isEdit) {
					((ShopBasicInfoFragment) fragmentList.get(0))
							.setEditEnable(true);
					titleRightText.setText("取消");
					isEdit = true;
				} else {
					((ShopBasicInfoFragment) fragmentList.get(0))
							.setEditEnable(false);
					((ShopBasicInfoFragment) fragmentList.get(0)).initData();
					titleRightText.setText("编辑");
					isEdit = false;

				}
			} else if (curIndex == 1) {
				XwShopMenu shopMenu = ((ShopMenuInfoFragment) fragmentList
						.get(1)).getShopMenu();

				Intent i = new Intent(ShopMainActivity.this,
						ShopMenuEditActivity.class);
				Bundle b = new Bundle();
				b.putSerializable("shopMenu", shopMenu);
				i.putExtras(b);
				startActivity(i);
			}

		}
	};

	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if(arg1!=RESULT_OK)
			return;
		switch (arg0) {
		case REQUESTCODE_UPDATE:
			break;

		default:
			break;
		}
	};
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		for (Fragment f : fragmentList) {
			((RefreshFragment) f).refreshContext(null);
		}
	}

	public void finishEdit() {
		titleRightText.setText("编辑");
		isEdit = false;
	}
}
