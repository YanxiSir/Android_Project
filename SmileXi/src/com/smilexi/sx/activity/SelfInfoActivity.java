package com.smilexi.sx.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smilexi.sx.R;
import com.smilexi.sx.app.BaseActivity;
import com.smilexi.sx.common.SXContext;
import com.smilexi.sx.common.SmileXiApi.IApiCallback;
import com.smilexi.sx.protocol.SxUpdateUserInfoReturn;
import com.smilexi.sx.request.domain.UserUpdateInfoRequest;
import com.smilexi.sx.util.ImageUtil;
import com.smilexi.sx.util.T;
import com.smilexi.sx.util.Tool;
import com.smilexi.sx.widget.LoadingDialog;
import com.smilexi.sx.widget.SelectDialog;
import com.smilexi.sx.widget.SelectPicDialog;

public class SelfInfoActivity extends BaseActivity implements OnClickListener {
	private static final int DIALOG_SELECT_OPERATE = 1;

	private RelativeLayout titleLeftRel;
	private TextView titleLeftText;

	private RelativeLayout headRel, nickRel, phoneRel, sexRel, signRel;
	private ImageView headImage;
	private TextView nickText, phoneText, sexText, signText;

	private SelectPicDialog selectPicDialog;
	private LoadingDialog loadingDialog;

	private Bitmap curUserHeadImgeBitmap = null;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DIALOG_SELECT_OPERATE:
				Bundle b = (Bundle) msg.obj;
				int selectid = b.getInt("selectid");
				loadWebRequest(4, String.valueOf(selectid + 1));
				// T.showShort(SelfInfoActivity.this, "ѡ�����Ϊ��" + selectid);
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_selfinfo);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_left_btn_text);
		setUserInfo();
		initUI();
		titleLeftRel.setOnClickListener(titleLeftClickLis);

	}

	private OnClickListener titleLeftClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			onBackPressed();
		}
	};

	public void initUI() {
		titleLeftRel = (RelativeLayout) findViewById(R.id.titlebar_lbt_btn);
		titleLeftText = (TextView) findViewById(R.id.titlebar_lbt_text);
		titleLeftText.setText("������Ϣ");

		headRel = (RelativeLayout) findViewById(R.id.self_head_rel);
		nickRel = (RelativeLayout) findViewById(R.id.self_nick_rel);
		phoneRel = (RelativeLayout) findViewById(R.id.self_phone_rel);
		sexRel = (RelativeLayout) findViewById(R.id.self_sex_rel);
		signRel = (RelativeLayout) findViewById(R.id.self_sign_rel);

		headImage = (ImageView) findViewById(R.id.self_head_image);
		nickText = (TextView) findViewById(R.id.self_nick_text);
		phoneText = (TextView) findViewById(R.id.self_phone_text);
		sexText = (TextView) findViewById(R.id.self_sex_text);
		signText = (TextView) findViewById(R.id.self_sign_text);

		initDate();

		headRel.setOnClickListener(this);
		nickRel.setOnClickListener(this);
		phoneRel.setOnClickListener(this);
		sexRel.setOnClickListener(this);
		signRel.setOnClickListener(this);

	}

	public void initDate() {
		Tool.imageLoader(SelfInfoActivity.this, headImage, MyPortrait, null);
		nickText.setText(MyNickName);
		phoneText.setText(MyPhone);
		if (!TextUtils.isEmpty(MySignWord)) {
			signText.setText(MySignWord);
		} else {
			signText.setText("");
		}
		if (MyGenderId == 1) {
			sexText.setText("Ů");
		} else if (MyGenderId == 2) {
			sexText.setText("��");
		} else {
			sexText.setText("");
		}
	}

	public void loadWebRequest(final int oid, final String content) {

		String message = "";
		if (oid == 1) {
			message = "�����ϴ�ͼƬ";
		} else if (oid == 2) {
			message = "�޸��ǳ�";
		} else if (oid == 3) {
			message = "�޸��ֻ���";
		} else if (oid == 4) {
			message = "�޸��Ա�";
		} else if (oid == 5) {
			message = "�޸ĸ���ǩ��";
		}
		loadingDialog = new LoadingDialog(SelfInfoActivity.this, message);
		loadingDialog.show();

		UserUpdateInfoRequest params = new UserUpdateInfoRequest();
		params.setOid(oid);
		params.setContent(content);
		params.setUid(MyId);

		SXContext.getInstance().getmSmileXiApi()
				.updateUserInfo(params, new IApiCallback() {

					@Override
					public void onError(int errorCode) {
						loadingDialog.dismiss();

						if (errorCode == -2) {
							T.showShort(SelfInfoActivity.this, "�ǳ��ѱ�ռ��");
						} else if (errorCode == -9) {
							T.showShort(SelfInfoActivity.this, "���������쳣");
						}
					}

					@Override
					public void onComplete(Object object) {
						loadingDialog.dismiss();
						SxUpdateUserInfoReturn retn = (SxUpdateUserInfoReturn) object;

						if (oid == 1) {
							SXContext.getInstance().getUserInfo()
									.setPortrait(retn.getPortrait());
						} else if (oid == 2) {
							SXContext.getInstance().getUserInfo()
									.setNickname(content);
						} else if (oid == 3) {
							SXContext.getInstance().getUserInfo()
									.setUserphone(content);
						} else if (oid == 4) {
							SXContext.getInstance().getUserInfo()
									.setGenderid(Integer.parseInt(content));
						} else if (oid == 5) {
							SXContext.getInstance().getUserInfo()
									.setSignstr(content);
						}

						setUserInfo();
						initDate();
					}
				});
	}

	Intent intent = null;
	public static final int REQUEST_CHANGE_SIGN = 5;
	public static final int REQUEST_CHANGE_NICK = 2;

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.self_head_rel:
			selectPicDialog = new SelectPicDialog(SelfInfoActivity.this);
			selectPicDialog.showPicDialog();
			break;
		case R.id.self_nick_rel:
			intent = new Intent(SelfInfoActivity.this,
					ChangeSelfInfoActivity.class);
			intent.putExtra("operate", "�޸��ǳ�");
			intent.putExtra("content", MyNickName);
			intent.putExtra("operaid", REQUEST_CHANGE_NICK);
			startActivityForResult(intent, REQUEST_CHANGE_NICK);
			break;
		case R.id.self_phone_rel:
			T.showShort(SelfInfoActivity.this, "��ʱ��֧�ָ���");
			// intent = new Intent(SelfInfoActivity.this,
			// ChangeSelfInfoActivity.class);
			// intent.putExtra("operate", "�޸��ֻ���");
			// startActivity(intent);
			break;
		case R.id.self_sex_rel:
			List<String> list = new ArrayList<String>();
			list.add("Ů");
			list.add("��");
			SelectDialog selectDialog = new SelectDialog(SelfInfoActivity.this,
					handler, list);
			selectDialog.setTitleText("ѡ���Ա�");
			break;
		case R.id.self_sign_rel:
			intent = new Intent(SelfInfoActivity.this,
					ChangeSelfInfoActivity.class);
			intent.putExtra("operate", "����ǩ��");
			intent.putExtra("content", MySignWord);
			intent.putExtra("operaid", REQUEST_CHANGE_SIGN);
			startActivityForResult(intent, REQUEST_CHANGE_SIGN);
			break;

		default:
			break;
		}
	}

	private String getPicBitmapToStr(Intent picdata) {
		Bundle bundle = picdata.getExtras();
		if (null != bundle) {
			Bitmap photo = bundle.getParcelable("data");
			curUserHeadImgeBitmap = photo;
			return ImageUtil.getBitmapStrBase64(photo);
		}
		return null;
	}

	String changeContent = "";

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
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
			if (data != null) {
				String bitToStrBase64 = getPicBitmapToStr(data);
				loadWebRequest(1, bitToStrBase64);
				// T.showShort(SelfInfoActivity.this, "�ϴ�ͷ��");
			}
			break;
		case REQUEST_CHANGE_NICK:
			if (data == null)
				return;
			changeContent = data.getStringExtra("content");
			loadWebRequest(2, changeContent);
			break;
		case REQUEST_CHANGE_SIGN:
			if (data == null)
				return;
			changeContent = data.getStringExtra("content");
			loadWebRequest(5, changeContent);
			break;
		default:
			break;
		}

	}
}
