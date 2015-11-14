package com.smilexi.sx.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smilexi.sx.R;
import com.smilexi.sx.adapter.ImgGridAdapter;
import com.smilexi.sx.app.BaseActivity;
import com.smilexi.sx.common.SXContext;
import com.smilexi.sx.common.SmileXiApi.IApiCallback;
import com.smilexi.sx.protocol.SxQuestionType;
import com.smilexi.sx.protocol.SxQuestionType.QuestionType;
import com.smilexi.sx.request.domain.AskQuestionRequest;
import com.smilexi.sx.request.domain.UserPublishDynamic;
import com.smilexi.sx.util.Bimp;
import com.smilexi.sx.util.FileUtils;
import com.smilexi.sx.util.ImageUtil;
import com.smilexi.sx.util.T;
import com.smilexi.sx.util.Tool;
import com.smilexi.sx.widget.ImagePopup;
import com.smilexi.sx.widget.LoadingDialog;
import com.smilexi.sx.widget.SelectDialog;

public class PublishActivity_1 extends BaseActivity {

	public static final int PUBLISH_LIFE_DYNAMIC = 0;
	public static final int PUBLISH_ASK_QUESTION = 1;

	public static final int SELECT_SINGLEINFO_REQUEST = 113;
	private static final int SELECT_DEMMAND_REQUEST = 114;

	private static final int PUBLISH_FINISHED = 120;

	/*
	 * titlebar
	 */
	private RelativeLayout titleLeftRel, titleRightRel;
	private ImageView titleLeftImage, titleRightImage;
	private TextView titleLeftText, titleRightText;

	/**/
	private LinearLayout titleEditLl;
	private EditText editText, titleEdit;
	private GridView selectImgGrid;
	private ImgGridAdapter adapter;
	private ImagePopup imagePopup;
	private LinearLayout questionTypeLl;
	private TextView questionTypeText;

	private LoadingDialog loadingDialog;

	int publishType;

	int qType = -1;
	SxQuestionType questionType = null;
	SelectDialog selectDialog = null;

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SelectDialog.DIALOG_SELECT_OPERATE:
				Bundle b = (Bundle) msg.obj;
				int selectid = b.getInt("selectid", -1);
				if (selectid == -1) {
					qType = -1;
				} else {
					qType = questionType.getqTypes().get(selectid).getId();
					String typeStr = questionType.getqTypes().get(selectid)
							.getName();
					questionTypeText.setText(typeStr);
				}
				break;

			default:
				break;
			}
		};
	};

	int fromId;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_publish_1);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_lbtn_text_rbtn_text);
		publishType = getIntent().getIntExtra("publishType",
				PUBLISH_LIFE_DYNAMIC);
		fromId = getIntent().getIntExtra("fromId", -1);
		initTitle();
		initUI();
		setUserInfo();
		if (publishType == PUBLISH_ASK_QUESTION)
			loadWebRequestQuestionType();
	}

	private void initUI() {
		titleEditLl = (LinearLayout) findViewById(R.id.publish_1_edittitle_ll);
		titleEdit = (EditText) findViewById(R.id.publish_1_edittitle);
		editText = (EditText) findViewById(R.id.public_1_edittext);
		selectImgGrid = (GridView) findViewById(R.id.public_1_selectimg_grid);
		questionTypeLl = (LinearLayout) findViewById(R.id.public_1_select_question_type_ll);
		questionTypeText = (TextView) findViewById(R.id.public_1_select_question_type);

		if (publishType == PUBLISH_LIFE_DYNAMIC) {
			questionTypeLl.setVisibility(View.GONE);
			titleEditLl.setVisibility(View.GONE);
			editText.setHint("这一刻，你在想什么...");
			selectImgGrid.setVisibility(View.VISIBLE);
			selectImgGrid.setSelector(new ColorDrawable(Color.TRANSPARENT));
			adapter = new ImgGridAdapter(this);
			adapter.update();
			titleLeftText.setText("记录生活点滴");
			selectImgGrid.setAdapter(adapter);
			selectImgGrid.setOnItemClickListener(selectImgGridClickLis);
		} else if (publishType == PUBLISH_ASK_QUESTION) {
			questionTypeLl.setVisibility(View.VISIBLE);
			questionTypeLl.setOnClickListener(questionTypeClickLis);
			selectImgGrid.setVisibility(View.GONE);
			titleEditLl.setVisibility(View.VISIBLE);
			titleLeftText.setText("写出你的问题");
			titleEdit.setHint("你要提什么问题?");
			editText.setHint("详细描述一下你的问题。。。");
		}
	}

	private OnClickListener questionTypeClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (questionType == null || questionType.getqTypes().size() == 0) {
				T.showShort(PublishActivity_1.this, "没有获取到分类信息");
				return;
			}
			List<String> list = new ArrayList<String>();
			for (QuestionType qt : questionType.getqTypes()) {
				list.add(qt.getName());
			}
			selectDialog = new SelectDialog(PublishActivity_1.this, mHandler,
					list);
			selectDialog.setTitleText("选择问题类型");
		}
	};

	private void initTitle() {

		titleLeftRel = (RelativeLayout) findViewById(R.id.titlebar_lrbt_left_rel);
		titleLeftImage = (ImageView) findViewById(R.id.titlebar_lrbt_left_image);
		titleLeftText = (TextView) findViewById(R.id.titlebar_lrbt_left_text);
		titleRightRel = (RelativeLayout) findViewById(R.id.titlebar_lrbt_right_rel);
		titleRightImage = (ImageView) findViewById(R.id.titlebar_lrbt_right_image);
		titleRightText = (TextView) findViewById(R.id.titlebar_lrbt_right_text);

		titleLeftText.setText("记录生活点滴");
		titleRightImage.setVisibility(View.GONE);
		titleRightText.setText("发布");
		titleRightRel.setClickable(true);
		titleLeftRel.setOnClickListener(titleLeftRelClickLis);
		titleRightRel.setOnClickListener(titleRightRelClickLis);

	}

	public void loadWebRequestQuestion() {
		String title = titleEdit.getText().toString().trim();
		String content = editText.getText().toString();
		if (TextUtils.isEmpty(title)) {
			T.showShort(PublishActivity_1.this, "请填写问题再提交");
			return;
		}
		if (TextUtils.isEmpty(content)) {
			T.showShort(PublishActivity_1.this, "详细描述一下你的问题");
			return;
		}
		if (qType < 0) {
			T.showShort(PublishActivity_1.this, "选择问题类型");
			return;
		}
		loadingDialog = new LoadingDialog(PublishActivity_1.this, "正在发布");
		loadingDialog.show();
		AskQuestionRequest params = new AskQuestionRequest();
		params.setContent(content);
		params.setTitle(title);
		params.setKeyword(null);
		params.setType(qType);
		params.setUid(SXContext.getInstance().getUserInfo().getUserid());

		SXContext.getInstance().getmSmileXiApi()
				.askQuestion(params, new IApiCallback() {

					@Override
					public void onError(int errorCode) {

					}

					@Override
					public void onComplete(Object object) {
						T.showShort(PublishActivity_1.this, "提问成功");
						if (fromId == MainActivity.FROM_MAINACTIVITY) {
							Intent i = new Intent(PublishActivity_1.this,
									CampusLifeDynamicActivity.class);
							i.putExtra("dynamicType",
									CampusLifeDynamicActivity.QUESTION_DYNAMIC);
							startActivity(i);
						} else {
							setResult(PUBLISH_FINISHED);
						}
						finish();
					}
				});
	}

	public void loadWebRequestQuestionType() {
		loadingDialog = new LoadingDialog(PublishActivity_1.this, "正在加载...");
		loadingDialog.show();
		SXContext.getInstance().getmSmileXiApi()
				.getQuestionTypeList(null, new IApiCallback() {

					@Override
					public void onError(int errorCode) {
						loadingDialog.dismiss();
					}

					@Override
					public void onComplete(Object object) {
						loadingDialog.dismiss();
						questionType = (SxQuestionType) object;
					}
				});
	}

	public void loadWebRequestLife() {
		String content = editText.getText().toString().trim();
		if (TextUtils.isEmpty(content)) {
			T.showShort(PublishActivity_1.this, "内容不能为空");
			return;
		}

		loadingDialog = new LoadingDialog(PublishActivity_1.this, "正在发布");
		loadingDialog.show();

		UserPublishDynamic params = new UserPublishDynamic();
		params.setUid(MyId);
		params.setTyp(1);
		params.setContent(content);

		List<String> pors = new ArrayList<String>(6);
		for (Bitmap b : Bimp.bmp) {
			String bs = ImageUtil.getBitmapStrBase64(b);
			pors.add(bs);
		}
		final int size = pors.size();
		if (size == 0) {
			params.setPor(null);
		} else
			params.setPor((String[]) pors.toArray(new String[size]));

		SXContext.getInstance().getmSmileXiApi()
				.publishDynamic(params, new IApiCallback() {

					@Override
					public void onError(int errorCode) {
						loadingDialog.dismiss();

					}

					@Override
					public void onComplete(Object object) {
						loadingDialog.dismiss();
						T.showShort(PublishActivity_1.this, "发布成功");
						FileUtils.deleteDir();
						Bimp.clear();
						if (fromId == MainActivity.FROM_MAINACTIVITY) {
							Intent i = new Intent(PublishActivity_1.this,
									CampusLifeDynamicActivity.class);
							i.putExtra("dynamicType",
									CampusLifeDynamicActivity.CAMPUS_DYNAMIC);
							startActivity(i);
						} else {
							setResult(PUBLISH_FINISHED);
						}
						finish();
					}
				});
	}

	private OnItemClickListener selectImgGridClickLis = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Tool.hideSoft(PublishActivity_1.this);

			if (arg2 == Bimp.bmp.size()) {
				// new PopupWindows(DynamicPublish_2.this, selectedImgsGrid);
				imagePopup = new ImagePopup(PublishActivity_1.this,
						selectImgGrid);
			} else {
				Intent intent = new Intent(PublishActivity_1.this,
						PhotoActivity.class);
				intent.putExtra("ID", arg2);
				startActivity(intent);
			}
		}
	};

	private OnClickListener titleRightRelClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (publishType == PUBLISH_LIFE_DYNAMIC)
				loadWebRequestLife();
			else if (publishType == PUBLISH_ASK_QUESTION)
				loadWebRequestQuestion();
		}

	};
	private OnClickListener titleLeftRelClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			onBackPressed();
		}
	};

	@Override
	protected void onRestart() {
		if (adapter != null)
			adapter.update();
		super.onRestart();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ImagePopup.TAKE_PICTURE:
			if (Bimp.drr.size() < 9 && resultCode == -1
					&& !TextUtils.isEmpty(imagePopup.getTakePicPath())) {
				Bimp.drr.add(imagePopup.getTakePicPath());
			}
			break;

		}
	}
}
