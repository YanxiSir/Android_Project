package com.smilexi.sx.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smilexi.sx.R;
import com.smilexi.sx.app.BaseActivity;
import com.smilexi.sx.common.SXContext;
import com.smilexi.sx.common.SmileXiApi.IApiCallback;
import com.smilexi.sx.request.domain.UserRegisterRequest;
import com.smilexi.sx.util.T;
import com.smilexi.sx.util.Tool;
import com.smilexi.sx.widget.CountDownButton;
import com.smilexi.sx.widget.LoadingDialog;

public class RegisterActivity_1 extends BaseActivity {

	private TextView titleCenterText;
	private RelativeLayout titleLeftRel;

	private EditText phoneText, codeText, pwdText;
	private TextView codeButton, registerButton;

	private CountDownButton countDownObj;
	private LoadingDialog loadingDialog;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_register_1);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_left_center_right);

		initUI();
		titleLeftRel.setOnClickListener(titleLeftRelClickLis);
		codeButton.setOnClickListener(sendVCodeClickLis);
		registerButton.setOnClickListener(registerClickLis);
	}
	private OnClickListener titleLeftRelClickLis = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			onBackPressed();
		}
	};
	private OnClickListener registerClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {

			String phone = phoneText.getText().toString();
			String vCode = codeText.getText().toString();
			String password = pwdText.getText().toString();

			if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(vCode)
					|| TextUtils.isEmpty(password)) {
				T.showShort(RegisterActivity_1.this, "请填写所有内容");
				return;
			}
			UserRegisterRequest params = new UserRegisterRequest();
			params.setUserPhone(phone);
			params.setvCode(vCode);
			params.setPassword(password);

			loadingDialog = new LoadingDialog(RegisterActivity_1.this,
					"正在注册...");
			loadingDialog.show();

			SXContext.getInstance().getmSmileXiApi()
					.userRegister(params, new IApiCallback() {

						@Override
						public void onError(int errorCode) {
							loadingDialog.dismiss();
							// -1 验证码错误，-2手机号已注册,-3服务器端数据库没找到对应手机验证码,-10初始化值
							String remindStr = "";
							if(errorCode == -1){
								remindStr = "验证码有误";
							}else if(errorCode == -2){
								remindStr = "手机号已注册";
							}else {
								remindStr = "注册失败";
							}
							T.showShort(RegisterActivity_1.this, remindStr);
						}

						@Override
						public void onComplete(Object object) {
							loadingDialog.dismiss();
							T.showShort(RegisterActivity_1.this, "注册成功");
							finish();
						}
					});
		}
	};

	private OnClickListener sendVCodeClickLis = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Tool.hideSoft(RegisterActivity_1.this);
			countDownObj.start();

			String userPhone = phoneText.getText().toString();
			if (TextUtils.isEmpty(userPhone)) {
				countDownObj.cancel();
				T.showLong(RegisterActivity_1.this, "手机号不能为空");
				return;
			}
			if (userPhone.length() != 11) {
				countDownObj.cancel();
				T.showLong(RegisterActivity_1.this, "手机号填写错误");
				return;
			}

			SXContext.getInstance().getmSmileXiApi()
					.getRegisterVCode(userPhone, new IApiCallback() {
						
						@Override
						public void onError(int errorCode) {
							T.showShort(RegisterActivity_1.this,errorCode+"");
						}
						
						@Override
						public void onComplete(Object object) {
							T.showShort(RegisterActivity_1.this,(String)object);
						}
					});

		}
	};

	public void initUI() {
		titleLeftRel = (RelativeLayout) findViewById(R.id.titlebar_lcr_left_rel);
		titleCenterText = (TextView) findViewById(R.id.titlebar_lcr_center_text);
		titleCenterText.setText("注册");
		titleLeftRel.setVisibility(View.VISIBLE); 

		phoneText = (EditText) findViewById(R.id.reg_phone_text);
		codeText = (EditText) findViewById(R.id.reg_code_text);
		pwdText = (EditText) findViewById(R.id.reg_password_text);
		codeButton = (TextView) findViewById(R.id.reg_code_button);
		registerButton = (TextView) findViewById(R.id.registerButton);

		int tickColor = getResources().getColor(R.color.right_color_gray);
		Drawable tickBackground = getResources().getDrawable(
				R.drawable.main_no_corner_button);

		countDownObj = new CountDownButton(60000, 1000);
		countDownObj.init(RegisterActivity_1.this, codeButton, "重新发送",
				tickBackground, tickColor);
	}
}
