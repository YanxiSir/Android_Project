package com.smilexi.sx.activity;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.smilexi.sx.R;
import com.smilexi.sx.app.BaseActivity;
import com.smilexi.sx.common.SXContext;
import com.smilexi.sx.common.SmileXiApi.IApiCallback;
import com.smilexi.sx.protocol.SxUserBaseInfo;
import com.smilexi.sx.request.domain.UserImproveInfoRequest;
import com.smilexi.sx.util.ImageUtil;
import com.smilexi.sx.util.T;
import com.smilexi.sx.widget.LoadingDialog;
import com.smilexi.sx.widget.SelectPicDialog;

public class ImproveSelfInfoActivity extends BaseActivity {

	private TextView titleCenterText;

	private ImageView userImage;
	private EditText userNameText, signWordText, emailText, realNameText;
	private RadioGroup sexGroup;
	private TextView submitBtn;

	private Bitmap currentPhotoBitmap;

	private UserImproveInfoRequest params;

	private LoadingDialog loadingDialog;
	private SelectPicDialog selectPicDialog;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_improve_self_info);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_left_center_right);
		initUI();

		userImage.setOnClickListener(imageClickLis);
		submitBtn.setOnClickListener(submitClickLis);

	}

	private OnClickListener submitClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			loadWebRequest();
		}
	};

	private OnClickListener imageClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			selectPicDialog = new SelectPicDialog(ImproveSelfInfoActivity.this);
			selectPicDialog.showPicDialog();
//			showPicDialog();
		}
	};
	
	public void loadWebRequest(){
		if (currentPhotoBitmap == null) {
			T.showShort(ImproveSelfInfoActivity.this, "请选择头像");
			return;
		}
		String username = userNameText.getText().toString();
		if (TextUtils.isEmpty(username)) {
			T.showShort(ImproveSelfInfoActivity.this, "用户名不能为空");
			return;
		}
		loadingDialog = new LoadingDialog(ImproveSelfInfoActivity.this,
				"正在提交...");
		loadingDialog.show();

		params = new UserImproveInfoRequest();
		params.setUid(SXContext.getInstance().getUserInfo().getUserid());
		params.setUsername(username);
		String signWord = signWordText.getText().toString();
		if (!TextUtils.isEmpty(signWord))
			params.setSignStr(signWord);
		String realName = realNameText.getText().toString();
		if (!TextUtils.isEmpty(realName))
			params.setRealName(realName);

		RadioButton rb = (RadioButton) findViewById(sexGroup
				.getCheckedRadioButtonId());
		if (rb != null) {
			if (rb.getText().equals("男")) {
				params.setGenderId(2);
			} else if (rb.getText().equals("女")) {
				params.setGenderId(1);
			} else {
				params.setGenderId(0);
			}
		} else {
			params.setGenderId(0);
		}
		params.setEmail(emailText.getText().toString());

		String strPortraitBase64 = ImageUtil
				.getBitmapStrBase64(currentPhotoBitmap);
		params.setPortrait(strPortraitBase64);

		SXContext.getInstance().getmSmileXiApi()
				.uploadUserImproveInfo(params, new IApiCallback() {

					@Override
					public void onError(int errorCode) {
						loadingDialog.dismiss();
						T.showShort(ImproveSelfInfoActivity.this, "fail");
					}

					@Override
					public void onComplete(Object object) {
						loadingDialog.dismiss();
						T.showShort(ImproveSelfInfoActivity.this, "success");
						
						SxUserBaseInfo userInfo = (SxUserBaseInfo) object;
						SXContext.getInstance().persistenceUserInfo(
								ImproveSelfInfoActivity.this, userInfo);
						setUserInfo();
						
						Intent intent = new Intent(
								ImproveSelfInfoActivity.this,
								MainActivity.class);
						startActivity(intent);
						finish();
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case SelectPicDialog.PHOTO_REQUEST_TAKEPHOTO:
			File tempFile = selectPicDialog.getTempFile();
			selectPicDialog.startPhotoZoom(Uri.fromFile(tempFile));

			break;
		case SelectPicDialog.PHOTO_REQUEST_GALLERY:
			if (data != null)
				selectPicDialog.startPhotoZoom(data.getData());
			break;
		case SelectPicDialog.PHOTO_REQUEST_CUT:
			if (data != null)
				setPicToView(data);
			break;
		default:
			break;
		}
	}

	private void setPicToView(Intent picdata) {
		Bundle bundle = picdata.getExtras();
		if (null != bundle) {
			Bitmap photo = bundle.getParcelable("data");
			userImage.setImageBitmap(photo);
			currentPhotoBitmap = photo;
		}
	}

	public void initUI() {
		titleCenterText = (TextView) findViewById(R.id.titlebar_lcr_center_text);
		titleCenterText.setText("完善基本信息");

		userImage = (ImageView) findViewById(R.id.info_user_image);
		userNameText = (EditText) findViewById(R.id.info_username);
		signWordText = (EditText) findViewById(R.id.info_sign_word);
		emailText = (EditText) findViewById(R.id.info_email);
		realNameText = (EditText) findViewById(R.id.info_realname);
		sexGroup = (RadioGroup) findViewById(R.id.info_radioGroup);
		submitBtn = (TextView) findViewById(R.id.info_submit);
	}
}
