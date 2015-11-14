package com.smilexi.sx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smilexi.sx.R;
import com.smilexi.sx.app.BaseActivity;
import com.smilexi.sx.common.SXContext;
import com.smilexi.sx.common.SmileXiApi.IApiCallback;
import com.smilexi.sx.protocol.SxAnswers.Answer;
import com.smilexi.sx.util.T;
import com.smilexi.sx.util.Tool;

public class AnswerDetailActivity extends BaseActivity {
	public final static int REQUEST_REPLY = 1; 

	/*
	 * titlebar
	 */
	private RelativeLayout titleLeftRel, titleRightRelOne, titleRightRelTwo;
	private ImageView titleLeftImage, titleLeftTextImage, titleRightImageTwo;
	private LinearLayout titleLeftLl;
	private TextView titleLeftText;

	/*
	 * UI
	 */
	private TextView questionTitle, username, school, zanCount;
	private ImageView userImage, zanImage, shoucangImage, replyImage;
	private RelativeLayout zanRel;
	private TextView answerContent, replyCount;
	private LinearLayout shoucangLl, replyLl;

	private Answer answer;
	private String title;
	int uid;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_answer_detail);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_ltext_rtwo_btn);

		answer = (Answer) getIntent().getSerializableExtra("answer");
		title = getIntent().getStringExtra("title");
		uid = SXContext.getInstance().getUserInfo().getUserid();

		initTitleBar();
		initUI();
	}

	private void initData() {
		questionTitle.setText(title);
		Tool.imageLoader(AnswerDetailActivity.this, userImage,
				answer.getUserPortrait(), null);
		username.setText(answer.getUserName());
		zanCount.setText(answer.getZanCount() + "");
		answerContent.setText(answer.getContent());
		if (answer.getIsZan() == 1) {
			zanImage.setImageDrawable(getResources().getDrawable(
					R.drawable.zan_question_blue));
		} else if (answer.getIsZan() == 0) {
			zanImage.setImageDrawable(getResources().getDrawable(
					R.drawable.zan_question_gray));
		}
		if (answer.getIsCollect() == 1) {
			shoucangImage.setImageDrawable(getResources().getDrawable(
					R.drawable.shoucang_blue));
		} else if (answer.getIsCollect() == 0) {
			shoucangImage.setImageDrawable(getResources().getDrawable(
					R.drawable.shoucang_gray));
		}
		replyCount.setText("评论(" + answer.getReplyCount() + ")");

		zanRel.setOnClickListener(zanAnswerClickLis);
		shoucangLl.setOnClickListener(collectAnswerClickLis);
		replyLl.setOnClickListener(replyClickLis);

	}

	private OnClickListener replyClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(AnswerDetailActivity.this,
					DynamicDetail.class);
			intent.putExtra("detailType", DynamicDetail.SHOW_ANSWER_REPLY);
			intent.putExtra("aid", answer.getId());
			startActivityForResult(intent,REQUEST_REPLY);
		}
	};
	private OnClickListener collectAnswerClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			SXContext.getInstance().getmSmileXiApi()
					.collectAnswer(uid, answer.getId(), new IApiCallback() {

						@Override
						public void onError(int errorCode) {

						}

						@Override
						public void onComplete(Object object) {
							if (answer.getIsCollect() == 1) {
								shoucangImage.setImageDrawable(getResources()
										.getDrawable(R.drawable.shoucang_gray));
								answer.setIsCollect(0);
								T.showShort(AnswerDetailActivity.this, "取消收藏");
							} else if (answer.getIsCollect() == 0) {
								shoucangImage.setImageDrawable(getResources()
										.getDrawable(R.drawable.shoucang_blue));
								answer.setIsCollect(1);
								T.showShort(AnswerDetailActivity.this, "收藏成功");
							}
						}
					});
		}
	};
	private OnClickListener zanAnswerClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			SXContext.getInstance().getmSmileXiApi()
					.zanAnswer(uid, answer.getId(), new IApiCallback() {

						@Override
						public void onError(int errorCode) {

						}

						@Override
						public void onComplete(Object object) {
							if (answer.getIsZan() == 1) {
								zanImage.setImageDrawable(getResources()
										.getDrawable(
												R.drawable.zan_question_gray));
								answer.setIsZan(0);
								answer.setZanCount(answer.getZanCount() - 1);
								zanCount.setText(answer.getZanCount() + "");
							} else if (answer.getIsZan() == 0) {
								zanImage.setImageDrawable(getResources()
										.getDrawable(
												R.drawable.zan_question_blue));
								answer.setIsZan(1);
								answer.setZanCount(answer.getZanCount() + 1);
								zanCount.setText(answer.getZanCount() + "");
							}
						}
					});
		}
	};

	private void initUI() {
		questionTitle = (TextView) findViewById(R.id.answer_detail_title);
		username = (TextView) findViewById(R.id.answer_detail_username);
		school = (TextView) findViewById(R.id.answer_detail_school);
		zanCount = (TextView) findViewById(R.id.answer_detail_zan_count);
		userImage = (ImageView) findViewById(R.id.answer_detail_userimage);
		zanImage = (ImageView) findViewById(R.id.answer_detail_zanimg);
		shoucangImage = (ImageView) findViewById(R.id.answer_detail_shoucang_img);
		replyImage = (ImageView) findViewById(R.id.answer_detail_reply_img);
		zanRel = (RelativeLayout) findViewById(R.id.answer_detail_zan_rel);
		answerContent = (TextView) findViewById(R.id.answer_detail_content);
		shoucangLl = (LinearLayout) findViewById(R.id.answer_detail_shoucang_ll);
		replyLl = (LinearLayout) findViewById(R.id.answer_detail_reply_ll);
		replyCount = (TextView) findViewById(R.id.answer_detail_reply_count);

		initData();
	}

	private void initTitleBar() {
		titleLeftLl = (LinearLayout) findViewById(R.id.title_tbb_left_ll);
		titleLeftRel = (RelativeLayout) findViewById(R.id.title_tbb_left_rel);
		titleRightRelOne = (RelativeLayout) findViewById(R.id.title_tbb_right_rel_one);
		titleRightRelTwo = (RelativeLayout) findViewById(R.id.title_tbb_right_rel_two);
		titleLeftImage = (ImageView) findViewById(R.id.title_tbb_left_image);
		titleLeftTextImage = (ImageView) findViewById(R.id.title_tbb_textimage);
		titleRightImageTwo = (ImageView) findViewById(R.id.title_tbb_right_image_two);
		titleLeftText = (TextView) findViewById(R.id.title_tbb_left_text);

		titleLeftRel.setVisibility(View.VISIBLE);
		titleLeftLl.setVisibility(View.VISIBLE);
		titleLeftImage.setImageDrawable(getResources().getDrawable(
				R.drawable.back));
		titleLeftTextImage.setVisibility(View.GONE);
		titleLeftText.setText("某个问题");

		titleRightRelOne.setVisibility(View.GONE);
		titleRightRelTwo.setVisibility(View.GONE);

		titleLeftRel.setOnClickListener(titleLeftRelClickLis);
	}

	private OnClickListener titleLeftRelClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			onBackPressed();
		}
	};
	public void onBackPressed() {
		Intent i = new Intent();
		Bundle b = new Bundle();
		b.putSerializable("answer", answer);
		i.putExtras(b);
		setResult(RESULT_OK,i);
		finish();
	};
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_REPLY:
			
			break;
			

		default:
			break;
		}
	};
}
