package com.xiaowu.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.xiaowu.common.RequestAPI.IApiCallback;
import com.xiaowu.common.XwContext;
import com.xiaowu.projectdesign.R;
import com.xiaowu.protocol.XwUserInfo;
import com.xiaowu.shop.activity.ShopMainActivity;
import com.xiaowu.shop.protocol.XwShopInfo;
import com.xiaowu.utils.T;
import com.xiaowu.widget.LoadingDialog;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Login extends Activity {

	private RelativeLayout titleLeftRel, titleRightRel;
	private TextView titleCenterText;

	private TextView loginType;
	private EditText username;
	private EditText password;
	private TextView loginButton;

	private LoadingDialog loadingDialog;

	private int typ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.login);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_left_center_right);
		typ = getIntent().getIntExtra("typ", 0);
		initTitlebar();
		InitView();
	}

	private void initTitlebar() {
		titleLeftRel = (RelativeLayout) findViewById(R.id.titlebar_lcr_left_rel);
		titleRightRel = (RelativeLayout) findViewById(R.id.titlebar_lcr_right_rel);
		titleCenterText = (TextView) findViewById(R.id.titlebar_lcr_center_text);

		titleLeftRel.setVisibility(View.GONE);
		titleRightRel.setVisibility(View.GONE);
		if (typ == SelectUserTypeActivity.TYPE_USER) {
			titleCenterText.setText("买家登录");
		} else if (typ == SelectUserTypeActivity.TYPE_SHOP) {
			titleCenterText.setText("卖家登录");
		}

	}

	private void InitView() {
		loginType = (TextView) findViewById(R.id.login_type);
		username = (EditText) findViewById(R.id.username_edit);
		password = (EditText) findViewById(R.id.password_edit);
		loginButton = (TextView) findViewById(R.id.signin_button);

		if (typ == SelectUserTypeActivity.TYPE_USER) {
			loginType.setText("我要定外卖");
		} else if (typ == SelectUserTypeActivity.TYPE_SHOP) {
			loginType.setText("我要卖快餐");
		}

		loginButton.setOnClickListener(loginBtnClickLis);

		if (typ == SelectUserTypeActivity.TYPE_USER) {
			username.setText("13998302873");
			password.setText("111");
		} else if (typ == SelectUserTypeActivity.TYPE_SHOP) {
			username.setText("yanxi");
			password.setText("111");
		}

	}

	private OnClickListener loginBtnClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {

			String phone = username.getText().toString().trim();
			String pwd = password.getText().toString().trim();

			if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(pwd)) {
				T.showShort(Login.this, "手机号和密码不能为空");
				return;
			}
			if (typ == SelectUserTypeActivity.TYPE_USER) {
				UserLoginRequest(phone, pwd);
			} else if (typ == SelectUserTypeActivity.TYPE_SHOP) {
				ShopLoginRequest(phone, pwd);
			}

		}
	};

	private void ShopLoginRequest(String username, String pwd) {
		loadingDialog = new LoadingDialog(Login.this, "正在登录...");
		loadingDialog.show();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", pwd));
		XwContext.getInstance().getmRequestAPI()
				.shopLogin(params, new IApiCallback() {

					@Override
					public void onError(int errorCode) {
						loadingDialog.dismiss();
						T.showShort(Login.this, "登录失败...");
					}

					@Override
					public void onComplete(Object object) {
						loadingDialog.dismiss();
						XwShopInfo info = (XwShopInfo) object;
						XwContext.getInstance().setShopInfo(info);
						T.showShort(Login.this, "登录成功");
						Intent intent = new Intent(Login.this,
								ShopMainActivity.class);
						startActivity(intent);
					}
				});
	}

	private void UserLoginRequest(String phone, String pwd) {
		loadingDialog = new LoadingDialog(Login.this, "正在登录...");
		loadingDialog.show();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("phone", phone));
		params.add(new BasicNameValuePair("password", pwd));
		XwContext.getInstance().getmRequestAPI()
				.login(params, new IApiCallback() {

					@Override
					public void onError(int errorCode) {
						loadingDialog.dismiss();
						T.showShort(Login.this, "登录失败...");
					}

					@Override
					public void onComplete(Object object) {
						loadingDialog.dismiss();
						XwUserInfo info = (XwUserInfo) object;
						XwContext.getInstance().setUserInfo(info);
						T.showShort(Login.this, "登录成功");
						Intent intent = new Intent(Login.this, MainPage.class);
						startActivity(intent);
					}
				});
	}

}
