package com.smilexi.sx.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smilexi.sx.R;
import com.smilexi.sx.app.BaseActivity;

public class SelfContactWayActivity extends BaseActivity {

	private RelativeLayout titleLeftRel;
	private TextView titleLeftText;

	private TextView phone, email;

	private String phoneStr, emailStr;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_self_contact_way);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_left_btn_text);

		Bundle b = getIntent().getExtras();
		phoneStr = b.getString("phone");
		emailStr = b.getString("email");

		initTitleBar();
		initUI();
	}

	private void initUI() {
		phone = (TextView) findViewById(R.id.selfcontact_phone);
		email = (TextView) findViewById(R.id.selfcontact_email);

		if (TextUtils.isEmpty(phoneStr))
			phone.setText("");
		else
			phone.setText(phoneStr);
		if (TextUtils.isEmpty(emailStr))
			email.setText("");
		else
			email.setText(emailStr);
	}

	private void initTitleBar() {
		titleLeftRel = (RelativeLayout) findViewById(R.id.titlebar_lbt_btn);
		titleLeftText = (TextView) findViewById(R.id.titlebar_lbt_text);

		titleLeftRel.setOnClickListener(titleLeftRelClickLis);
		titleLeftText.setText("联系方式");
	}

	private OnClickListener titleLeftRelClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			onBackPressed();
		}
	};
}
