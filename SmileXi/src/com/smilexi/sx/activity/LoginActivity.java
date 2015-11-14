package com.smilexi.sx.activity;

import java.util.Set;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.smilexi.sx.R;
import com.smilexi.sx.app.BaseActivity;
import com.smilexi.sx.common.SXContext;
import com.smilexi.sx.common.SmileXiApi.IApiCallback;
import com.smilexi.sx.protocol.SxUserBaseInfo;
import com.smilexi.sx.request.domain.UserLoginRequest;
import com.smilexi.sx.util.T;
import com.smilexi.sx.widget.LoadingDialog;
import com.smilexi.sx.widget.RoundImageView;

public class LoginActivity extends BaseActivity {

	private RoundImageView headImageView;
	private EditText usernameText;
	private EditText passwordText;
	private TextView loginText;
	private TextView registerText;

	LoadingDialog dialog;

	private SharedPreferences sp;

	
	@Override
	protected void onCreate(Bundle arg0) {

		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		// setContentView(R.layout.act_login);
		// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		// R.layout.titlebar2);

		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		headImageView = (RoundImageView) findViewById(R.id.headImage);
		usernameText = (EditText) findViewById(R.id.username);
		passwordText = (EditText) findViewById(R.id.password);
		loginText = (TextView) findViewById(R.id.loginButton);
		registerText = (TextView) findViewById(R.id.registerButton);

		sp = getSharedPreferences("config", MODE_PRIVATE);

		String user = sp.getString("userPhone", "");
		String pwd = sp.getString("password", "");
		if (!TextUtils.isEmpty(user) && !TextUtils.isEmpty(pwd)) {
			usernameText.setText(user);
			passwordText.setText(pwd);
		}

		loginText.setOnClickListener(loginOnClickLis);
		registerText.setOnClickListener(registerClickLis);
	}

	private OnClickListener registerClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(LoginActivity.this,
					RegisterActivity_1.class);
			startActivity(intent);
			overridePendingTransition(R.anim.fade_medium, R.anim.hold_medium);
		}
	};

	private OnClickListener loginOnClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {

			String userPhone = usernameText.getText().toString();
			String password = passwordText.getText().toString();

			if (TextUtils.isEmpty(userPhone) || TextUtils.isEmpty(password)) {
				T.showShort(LoginActivity.this, "手机号或密码都不能为空!");
				return;
			}

			dialog = new LoadingDialog(LoginActivity.this, "正在登录...");
			dialog.show();

			UserLoginRequest params = new UserLoginRequest();
			params.setUserPhone(userPhone);
			params.setPassword(password);

			SXContext.getInstance().getmSmileXiApi()
					.getUserLoginInfo(params, new IApiCallback() {

						@Override
						public void onError(int errorCode) {
							dialog.dismiss();
							if (errorCode == -1) {
								T.showShort(LoginActivity.this, "密码错误");
							} else {
								T.showShort(LoginActivity.this, "登录失败");
							}
						}

						@Override
						public void onComplete(Object object) {
							dialog.dismiss();

							Editor editor = sp.edit();
							editor.putString("userPhone", usernameText
									.getText().toString().trim());
							editor.putString("password", passwordText.getText()
									.toString().trim());
							editor.commit();

							SxUserBaseInfo userInfo = (SxUserBaseInfo) object;
							SXContext.getInstance().persistenceUserInfo(
									LoginActivity.this, userInfo);
							setUserInfo();
							Intent intent = null;
							if (userInfo.getImproveinfo() == 0) {
								// 需要完善信息
								intent = new Intent(LoginActivity.this,
										ImproveSelfInfoActivity.class);
							} else {
								intent = new Intent(LoginActivity.this,
										MainActivity.class);
							}
							startActivity(intent);
							overridePendingTransition(R.anim.fade_short,
									R.anim.hold_medium);
						}
					});
		}
	};

	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	};

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		JPushInterface.onPause(this);
	}

}
