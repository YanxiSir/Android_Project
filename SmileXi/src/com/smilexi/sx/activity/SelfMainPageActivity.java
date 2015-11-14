package com.smilexi.sx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smilexi.sx.R;
import com.smilexi.sx.app.BaseActivity;
import com.smilexi.sx.common.SXContext;
import com.smilexi.sx.common.SmileXiApi.IApiCallback;
import com.smilexi.sx.protocol.SxUserMainPageInfo;
import com.smilexi.sx.util.DensityUtil;
import com.smilexi.sx.util.T;
import com.smilexi.sx.util.Tool;
import com.smilexi.sx.widget.LoadingDialog;
import com.smilexi.sx.widget.PullToZoomListView;
import com.smilexi.sx.widget.RoundImageView;

public class SelfMainPageActivity extends BaseActivity {

	private RelativeLayout titleLeftRel;
	private TextView titleLeftText;

	private RoundImageView selfImage;
	private TextView selfName;
	private TextView selfSign;

	private LinearLayout selfSendAtteLl;
	private TextView selfSendMsgBtn, selfAttendBtn;

	private PullToZoomListView mListView;
	private RelativeLayout lifeRel, contactRel,questionRel;

	SxUserMainPageInfo info;

	int uid;
	int wid;

	private LoadingDialog loadingDialog;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_self_mainpage);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_left_btn_text);

		uid = SXContext.getInstance().getUserInfo().getUserid();
		wid = getIntent().getIntExtra("uid", 0);

		initTitleBar();
		initUI();
		initHeaderView();

		loadingDialog = new LoadingDialog(SelfMainPageActivity.this, "正在加载...");
		loadingDialog.show();
		SXContext.getInstance().getmSmileXiApi()
				.getUserMainPage(wid, uid, new IApiCallback() {

					@Override
					public void onError(int errorCode) {
						loadingDialog.dismiss();
					}

					@Override
					public void onComplete(Object object) {
						loadingDialog.dismiss();
						info = (SxUserMainPageInfo) object;
						if (info == null)
							return;
						Tool.imageLoader(SelfMainPageActivity.this, selfImage,
								info.getPortrait(), null);
						selfName.setText(info.getNickname());
						if (!TextUtils.isEmpty(info.getSign())) {
							String sign = "个性签名:" + info.getSign();
							int fstart = sign.indexOf("个性签名");
							int fend = fstart + "个性签名:".length();
							SpannableStringBuilder style = new SpannableStringBuilder(
									sign);
							style.setSpan(new ForegroundColorSpan(
									R.color.main_bg_gray), fstart, fend,
									Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							selfSign.setText(style);
							if (wid != uid) {
								if (info.getAttented() == 0) {
									selfAttendBtn
											.setBackgroundResource(R.drawable.main_no_corner_button);
									selfAttendBtn.setText("关注");
								} else {
									selfAttendBtn
											.setBackgroundResource(R.drawable.gray_grayer);
									selfAttendBtn.setText("已关注");
								}
							}
						}
					}
				});
	}

	private OnClickListener titleLeftRelClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			onBackPressed();
		}
	};

	private void initTitleBar() {
		titleLeftRel = (RelativeLayout) findViewById(R.id.titlebar_lbt_btn);
		titleLeftText = (TextView) findViewById(R.id.titlebar_lbt_text);

		titleLeftRel.setOnClickListener(titleLeftRelClickLis);
	}

	private void initUI() {
		mListView = (PullToZoomListView) findViewById(R.id.self_zoom_listview);
		lifeRel = (RelativeLayout) findViewById(R.id.self_main_life);
		contactRel = (RelativeLayout) findViewById(R.id.self_main_contact);
		questionRel = (RelativeLayout) findViewById(R.id.self_main_question);

		lifeRel.setOnClickListener(lifeRelClickLis);
		contactRel.setOnClickListener(contactRelClickLis);
		questionRel.setOnClickListener(questionClickLis); 
	}
	private OnClickListener questionClickLis = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(SelfMainPageActivity.this,
					CampusLifeDynamicActivity.class);
			Bundle b = new Bundle();
			b.putInt("wid", wid);
			b.putInt("dynamicType", CampusLifeDynamicActivity.QUESTION_DYNAMIC);
			intent.putExtras(b);
			startActivity(intent);
		}
	};
	private OnClickListener contactRelClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (info == null)
				return;
			Intent intent = new Intent(SelfMainPageActivity.this,
					SelfContactWayActivity.class);
			Bundle b = new Bundle();
			b.putString("phone", info.getPhone());
			b.putString("email", info.getEmail());
			intent.putExtras(b);
			startActivity(intent);
		}
	};
	private OnClickListener lifeRelClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(SelfMainPageActivity.this,
					CampusLifeDynamicActivity.class);
			Bundle b = new Bundle();
			b.putInt("wid", wid);
			b.putInt("dynamicType", CampusLifeDynamicActivity.CAMPUS_DYNAMIC);
			intent.putExtras(b);
			startActivity(intent);
		}
	};

	private void initHeaderView() {
		/* ========================= 设置头部的图片==================================== */
		mListView.getHeaderView().setScaleType(ImageView.ScaleType.CENTER_CROP);
		mListView.getHeaderView().setImageResource(R.drawable.cover);

		/*
		 * ========================= 设置头部的高度
		 * ====================================
		 */
		mListView.setmHeaderHeight(DensityUtil.dip2px(
				SelfMainPageActivity.this, 260));

		/*
		 * =========================
		 * 设置头部的的布局====================================
		 */
		View mHeaderView = getLayoutInflater().inflate(
				R.layout.act_self_mainpage_header, null);
		mListView.getHeaderContainer().addView(mHeaderView);
		mListView.setHeaderView();
		mListView.setAdapter(null);

		selfImage = (RoundImageView) mHeaderView
				.findViewById(R.id.self_mp_headimg);
		selfName = (TextView) mHeaderView.findViewById(R.id.self_mp_name);
		selfSign = (TextView) mHeaderView.findViewById(R.id.self_mp_sign);

		selfSendAtteLl = (LinearLayout) mHeaderView
				.findViewById(R.id.self_mp_send_atte_ll);
		selfSendMsgBtn = (TextView) mHeaderView
				.findViewById(R.id.self_mp_sendmsg);
		selfAttendBtn = (TextView) mHeaderView.findViewById(R.id.self_mp_atte);

		if (wid == uid) {
			selfSendAtteLl.setVisibility(View.GONE);
		} else {
			selfSendAtteLl.setVisibility(View.VISIBLE);
		}
		selfAttendBtn.setOnClickListener(attendClickLis);
	}

	private OnClickListener attendClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {

			SXContext.getInstance().getmSmileXiApi()
					.attentionUser(uid, wid, new IApiCallback() {

						@Override
						public void onError(int errorCode) {
							T.showShort(SelfMainPageActivity.this, "操作失败");
						}

						@Override
						public void onComplete(Object object) {
							if (info.getAttented() == 0) {
								selfAttendBtn
										.setBackgroundResource(R.drawable.gray_grayer);
								selfAttendBtn.setText("已关注");
								info.setAttented(1);

							} else {
								selfAttendBtn
										.setBackgroundResource(R.drawable.main_no_corner_button);
								selfAttendBtn.setText("关注");
								info.setAttented(0);
							}
						}
					});
		}
	};
}
