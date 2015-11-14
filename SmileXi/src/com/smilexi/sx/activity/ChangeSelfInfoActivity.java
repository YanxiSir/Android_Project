package com.smilexi.sx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smilexi.sx.R;
import com.smilexi.sx.app.BaseActivity;

public class ChangeSelfInfoActivity extends BaseActivity {

	public static final int REQUEST_CHANGE_SIGN = 5;
	public static final int REQUEST_CHANGE_NICK = 2;

	/*
	 * titlebar
	 */
	private RelativeLayout titleLeftRel, titleRightRel;
	private ImageView titleLeftImage, titleRightImage;
	private TextView titleLeftText, titleRightText;

	String operate = "";
	String hintContent = "";
	int operaid;

	EditText editText;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_change_selfinfo);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_lbtn_text_rbtn_text);

		operate = getIntent().getStringExtra("operate");
		operaid = getIntent().getIntExtra("operaid", -1);
		hintContent = getIntent().getStringExtra("content");

		initUI();

	}

	private void initUI() {

		titleLeftRel = (RelativeLayout) findViewById(R.id.titlebar_lrbt_left_rel);
		titleLeftImage = (ImageView) findViewById(R.id.titlebar_lrbt_left_image);
		titleLeftText = (TextView) findViewById(R.id.titlebar_lrbt_left_text);
		titleRightRel = (RelativeLayout) findViewById(R.id.titlebar_lrbt_right_rel);
		titleRightImage = (ImageView) findViewById(R.id.titlebar_lrbt_right_image);
		titleRightText = (TextView) findViewById(R.id.titlebar_lrbt_right_text);

		titleLeftText.setText(operate);
		titleRightImage.setVisibility(View.GONE);
		titleRightText.setText("±£´æ");
		titleRightRel.setClickable(true);
		titleLeftRel.setOnClickListener(titleLeftRelClickLis);
		titleRightRel.setOnClickListener(titleRightRelClickLis);

		editText = (EditText) findViewById(R.id.change_info_et);
		editText.setText(hintContent);
	}

	private OnClickListener titleRightRelClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			String content = editText.getText().toString();
			bundle.putString("content", content);
			intent.putExtras(bundle);
			if (operaid == REQUEST_CHANGE_NICK) {
				setResult(REQUEST_CHANGE_NICK, intent);
			} else if (operaid == REQUEST_CHANGE_SIGN) {
				setResult(REQUEST_CHANGE_SIGN, intent);
			}
			finish();
		}
	};
	private OnClickListener titleLeftRelClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			onBackPressed();
		}
	};
}
