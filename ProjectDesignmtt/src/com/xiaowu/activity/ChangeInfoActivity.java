package com.xiaowu.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaowu.common.RequestAPI.IApiCallback;
import com.xiaowu.common.XwContext;
import com.xiaowu.finals.ServerFinals;
import com.xiaowu.projectdesign.R;
import com.xiaowu.shop.activity.ShopMenuEditActivity;
import com.xiaowu.utils.T;
import com.xiaowu.utils.Tool;
import com.xiaowu.widget.LoadingDialog;

public class ChangeInfoActivity extends Activity {

	/*
	 * titlebar
	 */
	private RelativeLayout titleLeftRel, titleRightRel_1, titleRightRel_2;
	private LinearLayout titleLeftLl;
	private TextView titleLeftText;
	private ImageView titleRightImage_1, titleRightImage_2;

	private EditText editText;

	int operateId;
	String operateStr;

	int oid = -1;

	private LoadingDialog loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_change_info);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_ltext_rtwo_btn);

		editText = (EditText) findViewById(R.id.act_changeinfo_edit);

		InitTitle();

		operateId = getIntent().getIntExtra("operateId", -1);

		if (operateId < 0)
			return;
		if (operateId == SubmitOrderActivity.TYPE_CHANGE_TIME) {
			titleLeftText.setText("你希望什么时候送餐");
		} else if (operateId == SubmitOrderActivity.TYPE_ADD_EXTRA) {
			titleLeftText.setText("备注（你有什么要求）");
		} else if (operateId == ShopMenuEditActivity.TYPE_ADD_CLASSIFY) {
			titleLeftText.setText("添加一个分类");
			editText.setHint("分类名称");
		} else if (operateId == ShopMenuEditActivity.TYPE_UPDATE_CLASSIFY) {
			titleLeftText.setText("修改分类名称");
		} else if (operateId == OrderDetailActivity.TYPE_REVIEW_ORDER) {
			titleLeftText.setText("填写评价内容");
			oid = getIntent().getIntExtra("oid", -1);
		}
		operateStr = getIntent().getStringExtra("operateStr");

		if (!TextUtils.isEmpty(operateStr)) {
			editText.setText(operateStr);
		} else {
			if (operateId == SubmitOrderActivity.TYPE_CHANGE_TIME) {
				editText.setHint("你希望什么时候送餐");
			} else if (operateId == SubmitOrderActivity.TYPE_ADD_EXTRA) {
				editText.setHint("备注");
			}
		}
		editText.setSelection(editText.getText().toString().length());

	}

	private void InitTitle() {

		titleLeftRel = (RelativeLayout) findViewById(R.id.title_tbb_left_rel);
		titleLeftLl = (LinearLayout) findViewById(R.id.title_tbb_left_ll);
		titleLeftText = (TextView) findViewById(R.id.title_tbb_left_text);
		titleRightRel_1 = (RelativeLayout) findViewById(R.id.title_tbb_right_rel_one);
		titleRightRel_2 = (RelativeLayout) findViewById(R.id.title_tbb_right_rel_two);
		titleRightImage_1 = (ImageView) findViewById(R.id.title_tbb_right_image_one);
		titleRightImage_2 = (ImageView) findViewById(R.id.title_tbb_right_image_two);

		titleLeftRel.setVisibility(View.VISIBLE);
		titleRightRel_1.setVisibility(View.GONE);

		titleLeftLl.setVisibility(View.VISIBLE);
		titleRightRel_2.setVisibility(View.VISIBLE);

		titleRightImage_2.setImageDrawable(getResources().getDrawable(
				R.drawable.sure));
		titleLeftRel.setOnClickListener(titleLeftRelClickLis);

		titleRightRel_2.setOnClickListener(submitClickLis);
	}

	private OnClickListener submitClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			String content = editText.getText().toString().trim();
			if (TextUtils.isEmpty(content)) {
				T.showShort(ChangeInfoActivity.this, "没填写内容");
				return;
			}
			if (operateId == OrderDetailActivity.TYPE_REVIEW_ORDER) {
				loadWebRequestReviewOrder(content);
			} else {
				Intent i = new Intent();
				i.putExtra("content", content);
				setResult(RESULT_OK, i);
				finish();
			}

		}
	};
	private OnClickListener titleLeftRelClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			onBackPressed();
		}
	};

	private void loadWebRequestReviewOrder(String content) {
		loadingDialog = new LoadingDialog(ChangeInfoActivity.this, "正在提交...");
		loadingDialog.show();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("oid", String.valueOf(oid)));
		params.add(new BasicNameValuePair("content", Tool.encodeUTF_8(content)));
		params.add(new BasicNameValuePair("grade", String.valueOf(10.0)));
		XwContext
				.getInstance()
				.getmRequestAPI()
				.NoResultRequest(ServerFinals.order_review, params,
						new IApiCallback() {

							@Override
							public void onError(int errorCode) {
								loadingDialog.dismiss();
							}

							@Override
							public void onComplete(Object object) {
								loadingDialog.dismiss();
								T.showShort(ChangeInfoActivity.this, "评价成功");
								finish();
							}
						});
	}

	@Override
	public void onBackPressed() {
		finish();
	};
}
