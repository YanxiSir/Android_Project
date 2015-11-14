package com.smilexi.sx.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smilexi.sx.R;
import com.smilexi.sx.app.BaseActivity;
import com.smilexi.sx.common.SXContext;
import com.smilexi.sx.common.SmileXiApi.IApiCallback;
import com.smilexi.sx.request.domain.SubmitAnswerRequest;
import com.smilexi.sx.util.T;
import com.smilexi.sx.widget.LoadingDialog;

public class PublishActivity_2 extends BaseActivity {

	private static final int PUBLISH_ANSWER = 1;
	/*
	 * titlebar
	 */
	private RelativeLayout titleLeftRel, titleRightRel;
	private ImageView titleLeftImage, titleRightImage;
	private TextView titleLeftText, titleRightText;

	private EditText answerEditText;
	private LoadingDialog loadingDialog;

	int qid;
	int uid;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_publish_2);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_lbtn_text_rbtn_text);

		qid = getIntent().getIntExtra("qid", 0);
		uid = SXContext.getInstance().getUserInfo().getUserid();

		initTitle();

		answerEditText = (EditText) findViewById(R.id.publish_2_edittext);

	}

	private void initTitle() {

		titleLeftRel = (RelativeLayout) findViewById(R.id.titlebar_lrbt_left_rel);
		titleLeftImage = (ImageView) findViewById(R.id.titlebar_lrbt_left_image);
		titleLeftText = (TextView) findViewById(R.id.titlebar_lrbt_left_text);
		titleRightRel = (RelativeLayout) findViewById(R.id.titlebar_lrbt_right_rel);
		titleRightImage = (ImageView) findViewById(R.id.titlebar_lrbt_right_image);
		titleRightText = (TextView) findViewById(R.id.titlebar_lrbt_right_text);

		titleLeftText.setText("添加回答");
		titleRightImage.setVisibility(View.GONE);
		titleRightText.setText("发布");
		titleRightRel.setClickable(true);
		titleLeftRel.setOnClickListener(titleLeftRelClickLis);
		titleRightRel.setOnClickListener(titleRightRelClickLis);

	}

	private OnClickListener titleRightRelClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			String content = answerEditText.getText().toString();
			if (TextUtils.isEmpty(content)) {
				T.showShort(PublishActivity_2.this, "还没有回答呐~");
				return;
			}
			if (loadingDialog == null)
				loadingDialog = new LoadingDialog(PublishActivity_2.this,
						"正在回答...");
			loadingDialog.show();

			SubmitAnswerRequest params = new SubmitAnswerRequest();
			params.setQid(qid);
			params.setUid(uid);
			params.setContent(content);

			SXContext.getInstance().getmSmileXiApi()
					.submitAnswer(params, new IApiCallback() {

						@Override
						public void onError(int errorCode) {
							loadingDialog.dismiss();
						}

						@Override
						public void onComplete(Object object) {
							loadingDialog.dismiss();
							setResult(RESULT_OK);
							finish();
						}
					});
		}

	};
	private OnClickListener titleLeftRelClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			onBackPressed();
		}
	};
	public void onBackPressed() {
		finish();
	};
}
