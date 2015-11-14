package com.smilexi.sx.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.smilexi.sx.R;
import com.smilexi.sx.adapter.PopupDialogAdapter;
import com.smilexi.sx.app.BaseActivity;
import com.smilexi.sx.common.SXContext;
import com.smilexi.sx.fragment.Fragment1;
import com.smilexi.sx.fragment.Fragment2;
import com.smilexi.sx.fragment.Fragment3;
import com.smilexi.sx.interfaces.RefreshFragment;
import com.smilexi.sx.receiver.ExampleUtil;
import com.smilexi.sx.util.T;
import com.smilexi.sx.widget.SelectDialog;
import com.smilexi.sx.widget.SelectPopup;

public class MainActivity extends BaseActivity {

	public static boolean isForeground = false;
	public static final int FROM_MAINACTIVITY = 100;
	/*
	 * titlebar 变量名
	 */
	private TextView titleLeftText;
	private RelativeLayout titleRightRelTwo;
	private ImageView titleRightImageTwo;

	private ViewPager mViewPager;
	private ArrayList<Fragment> fragmentList;
	private ImageView cursorImage;
	private TextView view1, view2, view3;
	private int curIndex;
	private int bmpW;// 横线图片宽度
	private int offset;// 图片移动的偏移量
	int uid;

	private SelectPopup selectPopup;
	private boolean popupIsShowing = false;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Bundle b;
			switch (msg.what) {
			case PopupDialogAdapter.SELECT_ID:
				b = (Bundle) msg.obj;
				int id = b.getInt("oid");
				popupIsShowing = false;

				if (id == 0) {
					Intent i = new Intent(MainActivity.this,
							SearchActivity.class);
					startActivity(i);
				}
				break;
			case SelectDialog.DIALOG_SELECT_OPERATE:
				b = (Bundle) msg.obj;
				int selectid = b.getInt("selectid");
				if (selectid == 0) {
					Intent intent = new Intent(MainActivity.this,
							PublishActivity_1.class);
					intent.putExtra("publishType",
							PublishActivity_1.PUBLISH_LIFE_DYNAMIC);
					intent.putExtra("fromId", FROM_MAINACTIVITY);
					startActivity(intent);
				} else if (selectid == 1) {
					Intent intent = new Intent(MainActivity.this,
							PublishActivity_1.class);
					intent.putExtra("publishType",
							PublishActivity_1.PUBLISH_ASK_QUESTION);
					intent.putExtra("fromId", FROM_MAINACTIVITY);
					startActivity(intent);
				}
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_ltext_rtwo_btn);

		uid = SXContext.getInstance().getUserInfo().getUserid();
		JPushInterface.setAlias(MainActivity.this, String.valueOf(uid),
				new TagAliasCallback() {

					@Override
					public void gotResult(int arg0, String arg1,
							Set<String> arg2) {
						T.showShort(MainActivity.this, arg0 + "");
					}
				});

		initTitle();
		initTextView();
		initImage();
		initViewPager();
		initUserOperate();
	}

	private void initTitle() {
		titleLeftText = (TextView) findViewById(R.id.title_tbb_left_text);
		titleRightRelTwo = (RelativeLayout) findViewById(R.id.title_tbb_right_rel_two);
		titleRightImageTwo = (ImageView) findViewById(R.id.title_tbb_right_image_two);
		titleRightImageTwo.setImageDrawable(getResources().getDrawable(
				R.drawable.add));

		titleRightRelTwo.setOnClickListener(titleRightRelTwoClickLis);

	}

	private OnClickListener titleRightRelTwoClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (!popupIsShowing)
				popupIsShowing = true;
			else {
				popupIsShowing = false;
				selectPopup.dismiss();
				return;
			}
			List<String> list = new ArrayList<String>();
			list.add("查找");
			selectPopup = new SelectPopup(MainActivity.this, handler, list,
					titleRightRelTwo);

		}
	};

	private void initTextView() {
		view1 = (TextView) findViewById(R.id.tv_guid1);
		view2 = (TextView) findViewById(R.id.tv_guid2);
		view3 = (TextView) findViewById(R.id.tv_guid3);

		setTextStyle(view1, view2, view3);

		view1.setOnClickListener(new menuClickLis(0));
		view2.setOnClickListener(new menuClickLis(1));
		view3.setOnClickListener(new menuClickLis(2));
	}

	public class menuClickLis implements OnClickListener {
		private int index = 0;

		public menuClickLis(int i) {
			index = i;
		}

		@Override
		public void onClick(View arg0) {
			mViewPager.setCurrentItem(index);
		}
	}

	/*
	 * 初始化图片的位移像素
	 */
	public void initImage() {
		cursorImage = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.cursor)
				.getWidth();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;

		offset = (screenW / 3 - bmpW) / 2;

		// imgageview设置平移，使下划线平移到初始位置（平移一个offset）
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);

		LayoutParams pa = cursorImage.getLayoutParams();
		pa.width = screenW / 3;
		pa.height = 6;
		cursorImage.setLayoutParams(pa);
		cursorImage.setImageMatrix(matrix);

	}

	public void initViewPager() {
		mViewPager = (ViewPager) findViewById(R.id.viewpager);

		fragmentList = new ArrayList<Fragment>();

		Fragment firstFragment = new Fragment1();
		Fragment secondFragment = new Fragment2();
		Fragment thirdFragment = new Fragment3();

		fragmentList.add(firstFragment);
		fragmentList.add(secondFragment);
		fragmentList.add(thirdFragment);

		mViewPager.setAdapter(new MyFragmentPagerAdapter(
				getSupportFragmentManager(), fragmentList));
		mViewPager.setCurrentItem(0);
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	public void initUserOperate() {
		final ImageView fabIconNew = new ImageView(this);
		fabIconNew.setImageDrawable(getResources().getDrawable(
				R.drawable.ic_action_new_light));
		final FloatingActionButton rightLowerButton = new FloatingActionButton.Builder(
				this).setContentView(fabIconNew).build();

		SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(this);
		ImageView rlIcon1 = new ImageView(this);
		ImageView rlIcon2 = new ImageView(this);
		ImageView rlIcon3 = new ImageView(this);
		ImageView rlIcon4 = new ImageView(this);

		rlIcon1.setImageDrawable(getResources().getDrawable(
				R.drawable.self_center));
		rlIcon2.setImageDrawable(getResources().getDrawable(
				R.drawable.fabu_airplane));
		rlIcon3.setImageDrawable(getResources().getDrawable(
				R.drawable.shoucang_xing));
		rlIcon4.setImageDrawable(getResources().getDrawable(
				R.drawable.guanzhu_main));

		SubActionButton button1 = rLSubBuilder.setContentView(rlIcon1).build();
		SubActionButton button2 = rLSubBuilder.setContentView(rlIcon2).build();
		SubActionButton button3 = rLSubBuilder.setContentView(rlIcon3).build();
		SubActionButton button4 = rLSubBuilder.setContentView(rlIcon4).build();

		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this,
						SelfInfoActivity.class);
				startActivity(intent);

			}
		});

		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				List<String> list = new ArrayList<String>();
				list.add("发布生活动态");
				list.add("提问");
				SelectDialog selectDialog = new SelectDialog(MainActivity.this,
						handler, list);
				selectDialog.setTitleText("你想发布什么？");
			}
		});
		button3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this,
						CampusLifeDynamicActivity.class);
				intent.putExtra("dynamicType",
						CampusLifeDynamicActivity.COLLECTED_ANSWER);
				startActivity(intent);
			}
		});
		button4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this,
						AttentionedUserList.class);
				startActivity(intent);
			}
		});

		// Build the menu with default options: light theme, 90 degrees, 72dp
		// radius.
		// Set 4 default SubActionButtons
		final FloatingActionMenu rightLowerMenu = new FloatingActionMenu.Builder(
				this).addSubActionView(button1).addSubActionView(button2)
				.addSubActionView(button3).addSubActionView(button4)
				.attachTo(rightLowerButton).build();

		// Listen menu open and close events to animate the button content view
		rightLowerMenu
				.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
					@Override
					public void onMenuOpened(FloatingActionMenu menu) {
						// Rotate the icon of rightLowerButton 45 degrees
						// clockwise
						fabIconNew.setRotation(0);
						PropertyValuesHolder pvhR = PropertyValuesHolder
								.ofFloat(View.ROTATION, 45);
						ObjectAnimator animation = ObjectAnimator
								.ofPropertyValuesHolder(fabIconNew, pvhR);
						animation.start();
					}

					@Override
					public void onMenuClosed(FloatingActionMenu menu) {
						// Rotate the icon of rightLowerButton 45 degrees
						// counter-clockwise
						fabIconNew.setRotation(45);
						PropertyValuesHolder pvhR = PropertyValuesHolder
								.ofFloat(View.ROTATION, 0);
						ObjectAnimator animation = ObjectAnimator
								.ofPropertyValuesHolder(fabIconNew, pvhR);
						animation.start();
					}
				});
	}


	public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

		ArrayList<Fragment> list;

		public MyFragmentPagerAdapter(FragmentManager fm,
				ArrayList<Fragment> list) {
			super(fm);
			this.list = list;
		}

		@Override
		public Fragment getItem(int arg0) {
			return list.get(arg0);
		}

		@Override
		public int getCount() {
			return list.size();
		}

	}

	public class MyOnPageChangeListener implements OnPageChangeListener {
		private int one = offset * 2 + bmpW; // 两个相邻页面的偏移量

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
				setTextStyle(view1, view2, view3);
				break;
			case 1:
				setTextStyle(view2, view1, view3);
				break;
			case 2:
				setTextStyle(view3, view2, view1);
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

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		for (Fragment f : fragmentList) {
			((RefreshFragment) f).refreshContext();
		}
	}

	// for receive customer msg from jpush server
	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";

	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}

	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
				String messge = intent.getStringExtra(KEY_MESSAGE);
				String extras = intent.getStringExtra(KEY_EXTRAS);
				StringBuilder showMsg = new StringBuilder();
				showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
				if (!ExampleUtil.isEmpty(extras)) {
					showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
				}
			}
		}
	}

	@Override
	protected void onResume() {
		isForeground = true;
		super.onResume();
	}

	@Override
	protected void onPause() {
		isForeground = false;
		super.onPause();
	}

}
