package com.smilexi.sx.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.smilexi.sx.R;
import com.smilexi.sx.adapter.AnswerAdapter;
import com.smilexi.sx.adapter.DynamicPhotoAdapter;
import com.smilexi.sx.adapter.ReplyAdapter;
import com.smilexi.sx.app.BaseActivity;
import com.smilexi.sx.common.SXContext;
import com.smilexi.sx.common.SmileXiApi.IApiCallback;
import com.smilexi.sx.protocol.SxAnswers;
import com.smilexi.sx.protocol.SxAnswers.Answer;
import com.smilexi.sx.protocol.SxDynamicReply;
import com.smilexi.sx.protocol.SxDynamicReply.Reply;
import com.smilexi.sx.protocol.SxDynamics.Dynamic;
import com.smilexi.sx.protocol.SxDynamics.Dynamic.Dynamic_Photos;
import com.smilexi.sx.protocol.SxQuestions.Question;
import com.smilexi.sx.request.domain.ReplyAnswerRequest;
import com.smilexi.sx.request.domain.SubmitReplyRequest;
import com.smilexi.sx.request.domain.ZanDynamic;
import com.smilexi.sx.util.T;
import com.smilexi.sx.util.Tool;
import com.smilexi.sx.widget.CGridView;
import com.smilexi.sx.widget.ConfirmDialog;
import com.smilexi.sx.widget.LoadingDialog;

public class DynamicDetail extends BaseActivity {

	public static final int LOADTYPE_INIT = 0;
	public static final int LOADTYPE_REFRESH = 1;
	public static final int SEND_TO_SOMEONE = 10;

	public static final int DETAIL_LIFE = 0;
	public static final int DETAIL_QUESTION = 1;
	public static final int SHOW_ANSWER_REPLY = 2;

	private static final int PUBLISH_ANSWER = 1;
	private static final int REQUEST_ANSWER_DETAIL = 2;

	private boolean fromNotifacation = false;

	/*
	 * titlebar
	 */
	private RelativeLayout titleLeftRel, titleRightRelOne, titleRightRelTwo;
	private ImageView titleLeftImage, titleLeftTextImage, titleRightImageTwo;
	private LinearLayout titleLeftLl;
	private TextView titleLeftText;

	/*
	 * lifedynamic headerview
	 */
	private ImageView userImage, delImage, zanImage, replyImage;
	private TextView userName, publishTime, dynamicContent, zanText, replyText;
	private LinearLayout gridLl, zanLl, replyLl;
	private CGridView gridView;

	/*
	 * content
	 */
	private PullToRefreshListView dynamicPtrlv;
	private LinearLayout detailReplyLl;
	private EditText detailReplyEdit;
	private TextView detailReplyBtn;
	// private RelativeLayout detailNoReplyRel;
	// private TextView detailNoReplyText;

	private LoadingDialog loadingDialog;

	private ReplyAdapter adapter;
	private AnswerAdapter answerAdapter;
	List<Reply> replyList;
	List<Answer> answerList;

	Dynamic item;
	Question itemQuestion;
	int uid;

	private Reply reply;
	private String hintStr = "评论一下";

	private int detailType;
	int aid;// answer id
	int did;
	int qid;

	private Animation loadingAnim;

	private int addAnswerReplyCount = 0;

	Handler mHander = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOADTYPE_REFRESH:
				loadWebRequest(LOADTYPE_REFRESH);

				detailReplyEdit.setText("");
				break;
			case SEND_TO_SOMEONE:
				Bundle b = (Bundle) msg.obj;
				int toid = b.getInt("uid");
				String toname = b.getString("uname");
				reply.setToUserId(toid);
				reply.setToUserName(toname);
				hintStr = "回复 " + toname;
				detailReplyEdit.setText("");
				detailReplyEdit.setHint(hintStr);
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_dynamic_detail);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_ltext_rtwo_btn);

		Bundle b = getIntent().getExtras();
		if (b == null) {
			finish();
			return;
		}
		detailType = b.getInt("detailType", DETAIL_LIFE);
		if (detailType == DETAIL_LIFE) {
			item = (Dynamic) b.getSerializable("dynamic");
			// if (item == null) {
			// did = b.getInt("did");
			// fromNotifacation = true;
			// } else {
			did = item.getDid();
			// }
			reply = new Reply();
		} else if (detailType == DETAIL_QUESTION) {
			itemQuestion = (Question) b.getSerializable("dynamic");
			// if (itemQuestion == null) {
			// qid = b.getInt("qid");
			// fromNotifacation = true;
			// } else {
			qid = itemQuestion.getQid();
			// }
		} else if (detailType == SHOW_ANSWER_REPLY) {
			aid = b.getInt("aid", 0);
			uid = SXContext.getInstance().getUserInfo().getUserid();
			reply = new Reply();
		}

		initTitleBar();
		initUI();
	}

	View mHeaderView;

	private void initUI() {

		dynamicPtrlv = (PullToRefreshListView) findViewById(R.id.detail_listview);
		initIndicator();
		if (detailType == DETAIL_LIFE) {
			detailReplyLl = (LinearLayout) findViewById(R.id.detail_reply_ll);
			detailReplyEdit = (EditText) findViewById(R.id.detail_reply_edittext);
			detailReplyBtn = (TextView) findViewById(R.id.detail_reply_btn);
			detailReplyLl.setVisibility(View.VISIBLE);
			detailReplyEdit.setHint(hintStr);

			mHeaderView = getLayoutInflater().inflate(
					R.layout.act_dynamic_detail_header, null);

			initHeaderView(mHeaderView);
			// reply初始化
			reply.setFromUserId(uid);
			reply.setFromUserName(SXContext.getInstance().getUserInfo()
					.getNickname());
			reply.setToUserId(0);
			reply.setToUserName(null);
			reply.setContent(null);

			detailReplyBtn.setOnClickListener(new replyClickLis(
					DynamicDetail.this, reply, did, detailReplyEdit, mHander,
					detailType));

			dynamicPtrlv.getRefreshableView().addHeaderView(mHeaderView);
			dynamicPtrlv.setAdapter(adapter);

		} else if (detailType == DETAIL_QUESTION) {
			detailReplyLl = (LinearLayout) findViewById(R.id.detail_reply_ll);
			detailReplyLl.setVisibility(View.GONE);

			mHeaderView = getLayoutInflater().inflate(
					R.layout.act_question_detail_header, null);
			initHeaderView(mHeaderView);

			dynamicPtrlv.getRefreshableView().addHeaderView(mHeaderView);
			dynamicPtrlv.setAdapter(null);

			dynamicPtrlv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					if (arg2 == 0)
						return;
					Intent intent = new Intent(DynamicDetail.this,
							AnswerDetailActivity.class);
					Bundle b = new Bundle();
					b.putSerializable("answer", answerList.get(arg2 - 1));
					b.putString("title", itemQuestion.getQtitle());
					intent.putExtras(b);
					startActivityForResult(intent, REQUEST_ANSWER_DETAIL);
				}

			});

		} else if (detailType == SHOW_ANSWER_REPLY) {
			detailReplyLl = (LinearLayout) findViewById(R.id.detail_reply_ll);
			detailReplyEdit = (EditText) findViewById(R.id.detail_reply_edittext);
			detailReplyBtn = (TextView) findViewById(R.id.detail_reply_btn);
			detailReplyLl.setVisibility(View.VISIBLE);
			detailReplyEdit.setHint(hintStr);

			reply.setFromUserId(uid);
			reply.setFromUserName(SXContext.getInstance().getUserInfo()
					.getNickname());
			reply.setToUserId(0);
			reply.setToUserName(null);
			reply.setContent(null);

			detailReplyBtn.setOnClickListener(new replyClickLis(
					DynamicDetail.this, reply, aid, detailReplyEdit, mHander,
					detailType));

		}

		loadWebRequest(LOADTYPE_INIT);

	}

	public void loadWebRequest(int loadType) {
		if (detailType == DETAIL_LIFE)
			loadWebRequestLife(loadType);
		else if (detailType == DETAIL_QUESTION)
			loadWebRequestQuestion(loadType);
		else if (detailType == SHOW_ANSWER_REPLY)
			loadWebRequestGetAnswerReplys(loadType);

	}

	public void loadWebRequestGetAnswerReplys(final int loadType) {
		SXContext.getInstance().getmSmileXiApi()
				.getAnswerReplys(aid, new IApiCallback() {

					@Override
					public void onError(int errorCode) {

					}

					@Override
					public void onComplete(Object object) {
						lifeOrAnswerReplyFinish(loadType, object);
					}
				});
	}

	public void loadWebRequestQuestion(final int loadType) {

		SXContext.getInstance().getmSmileXiApi()
				.getQuestionAnswers(uid, qid, new IApiCallback() {

					@Override
					public void onError(int errorCode) {
						T.showShort(DynamicDetail.this, "请求失败");
						dynamicPtrlv.onRefreshComplete();
					}

					@Override
					public void onComplete(Object object) {
						SxAnswers questionAnswer = (SxAnswers) object;

						if ((questionAnswer.getAnswers().size() == 0)) {
							quesLoadingLl.setVisibility(View.GONE);
							quesNoResult.setVisibility(View.VISIBLE);
							quesNoResult.setText("目前没有回答");
							return;
						}
						// if (itemQuestion == null) {
						// itemQuestion = questionAnswer.getQuestion();
						// initHeaderView(mHeaderView);
						// }
						quesLoaingRel.setVisibility(View.GONE);
						if (answerList == null)
							answerList = new ArrayList<SxAnswers.Answer>();
						else
							answerList.clear();
						answerList.addAll(questionAnswer.getAnswers());
						if (loadType == LOADTYPE_INIT) {
							answerAdapter = new AnswerAdapter(
									DynamicDetail.this, answerList,
									AnswerAdapter.TYPE_ANSWERS);
							dynamicPtrlv.setAdapter(answerAdapter);
						} else {
							if (adapter == null) {
								answerAdapter = new AnswerAdapter(
										DynamicDetail.this, answerList,
										AnswerAdapter.TYPE_ANSWERS);
								dynamicPtrlv.setAdapter(answerAdapter);
							} else {
								answerAdapter.notifyDataSetChanged();
								dynamicPtrlv.onRefreshComplete();
							}
						}
					}
				});

	}

	public void loadWebRequestLife(final int loadType) {

		// loadingDialog = new LoadingDialog(DynamicDetail.this, "正在加载...");
		// loadingDialog.show();
		SXContext.getInstance().getmSmileXiApi()
				.getDynamicReply(Integer.valueOf(did), new IApiCallback() {

					@Override
					public void onError(int errorCode) {
						// loadingDialog.dismiss();
						if (errorCode == -1) {
							T.showShort(DynamicDetail.this, "请求失败");
							dynamicPtrlv.onRefreshComplete();
						}
					}

					@Override
					public void onComplete(Object object) {
						// loadingDialog.dismiss();
						lifeOrAnswerReplyFinish(loadType, object);
					}

				});
	}

	private void lifeOrAnswerReplyFinish(final int loadType, Object object) {
		SxDynamicReply dynamicReply = (SxDynamicReply) object;

		if (dynamicReply == null
				|| (item != null && dynamicReply.getReplys().size() == 0)) {

			// detailNoReplyRel.setVisibility(View.VISIBLE);
			// T.showShort(DynamicDetail.this, "暂无回复");
			return;
		}
		// detailNoReplyRel.setVisibility(View.GONE);
		// if (item == null) {
		// item = dynamicReply.getDynamic();
		// initHeaderView(mHeaderView);
		// }
		if (replyList == null)
			replyList = new ArrayList<SxDynamicReply.Reply>();
		else
			replyList.clear();
		replyList.addAll(dynamicReply.getReplys());
		if (loadType == LOADTYPE_INIT) {
			adapter = new ReplyAdapter(replyList, DynamicDetail.this, mHander);
			dynamicPtrlv.setAdapter(adapter);
		} else {
			if (adapter == null) {
				adapter = new ReplyAdapter(replyList, DynamicDetail.this,
						mHander);
				dynamicPtrlv.setAdapter(adapter);
			} else {
				adapter.notifyDataSetChanged();
				dynamicPtrlv.onRefreshComplete();
			}
		}
	}

	private void initHeaderView(View v) {
		if (detailType == DETAIL_LIFE) {
			// if (item != null) {
			initHeaderViewLife(v);
			// }
		} else if (detailType == DETAIL_QUESTION) {
			// if (itemQuestion != null)
			initHeaderViewQuestion(v);
		}
	}

	private TextView quesType, quesTitle, quesDescribe;
	private ImageView quesGuanzhuImg, quesAnswerImg;
	private TextView quesGuanzhuCount, quesAnswerCount;
	private TextView quesGuanzhuBtn, quesAddAnswerBtn;
	private RelativeLayout quesAddAnswerRel, quesLoaingRel;
	private ImageView quesLoaingImg;
	private TextView quesLoaingTxt;
	private TextView quesNoResult;
	private LinearLayout quesLoadingLl;

	private void initHeaderViewQuestion(View v) {
		quesType = (TextView) v.findViewById(R.id.qdh_question_type);
		quesTitle = (TextView) v.findViewById(R.id.qdh_question_title);
		quesDescribe = (TextView) v.findViewById(R.id.qdh_question_describe);
		quesGuanzhuImg = (ImageView) v.findViewById(R.id.qdh_guanzhu_image);
		quesAnswerImg = (ImageView) v.findViewById(R.id.qdh_answer_image);
		quesGuanzhuCount = (TextView) v.findViewById(R.id.qdh_guanzhu_text);
		quesAnswerCount = (TextView) v.findViewById(R.id.qdh_answer_text);
		quesGuanzhuBtn = (TextView) v.findViewById(R.id.qdh_guanzhu_btn);
		quesAddAnswerBtn = (TextView) v.findViewById(R.id.qdh_add_answer_btn);
		quesAddAnswerRel = (RelativeLayout) v
				.findViewById(R.id.qdh_add_answer_rel);
		quesLoaingRel = (RelativeLayout) v.findViewById(R.id.qdh_loading_rel);
		quesLoaingImg = (ImageView) v.findViewById(R.id.qdh_loading_image);
		quesLoaingTxt = (TextView) v.findViewById(R.id.qdh_loading_text);
		quesNoResult = (TextView) v.findViewById(R.id.qdh_no_result);
		quesLoadingLl = (LinearLayout) v.findViewById(R.id.qdh_loading_ll);

		quesLoaingRel.setVisibility(View.VISIBLE);
		quesNoResult.setVisibility(View.GONE);
		quesLoadingLl.setVisibility(View.VISIBLE);
		quesLoaingTxt.setText("正在加载answer...");

		if (loadingAnim == null) {
			loadingAnim = AnimationUtils.loadAnimation(DynamicDetail.this,
					R.anim.loading_3);
			LinearInterpolator lin = new LinearInterpolator();
			loadingAnim.setInterpolator(lin);
			if (loadingAnim != null) {
				quesLoaingImg.startAnimation(loadingAnim);
				loadingAnim.startNow();// ��ʼ����
			}
		} else {
			quesLoaingImg.startAnimation(loadingAnim);
			loadingAnim.startNow();
		}

		quesType.setText(itemQuestion.getQtypename());
		quesTitle.setText(itemQuestion.getQtitle());
		if (TextUtils.isEmpty(itemQuestion.getQcontent())) {
			quesDescribe.setVisibility(View.GONE);
		} else {
			quesDescribe.setVisibility(View.VISIBLE);
			quesDescribe.setText(itemQuestion.getQcontent());
		}
		quesGuanzhuCount.setText(itemQuestion.getAttencount() + "");
		quesAnswerCount.setText(itemQuestion.getAnswercount() + "");
		if (itemQuestion.getIsattend() == 0) {
			quesGuanzhuImg.setImageDrawable(getResources().getDrawable(
					R.drawable.guanzhu_question_gray));
			quesGuanzhuBtn
					.setBackgroundResource(R.drawable.main_no_corner_button);
			quesGuanzhuBtn.setText("关注");
		} else {
			quesGuanzhuImg.setImageDrawable(getResources().getDrawable(
					R.drawable.guanzhu_question_blue));
			quesGuanzhuBtn.setBackgroundResource(R.drawable.gray_grayer);
			quesGuanzhuBtn.setText("已关注");
		}
		uid = SXContext.getInstance().getUserInfo().getUserid();
		quesAddAnswerBtn.setOnClickListener(addAnswerClickLis);
		quesGuanzhuBtn.setOnClickListener(guanzhuCliskLis);
	}

	private OnClickListener guanzhuCliskLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {

			SXContext
					.getInstance()
					.getmSmileXiApi()
					.attentionQuestion(itemQuestion.getQid(), uid,
							new IApiCallback() {

								@Override
								public void onError(int errorCode) {

								}

								@Override
								public void onComplete(Object object) {
									if (itemQuestion.getIsattend() == 0) {
										quesGuanzhuImg
												.setImageDrawable(getResources()
														.getDrawable(
																R.drawable.guanzhu_question_blue));
										quesGuanzhuBtn
												.setBackgroundResource(R.drawable.gray_grayer);
										quesGuanzhuBtn.setText("已关注");
										itemQuestion.setAttencount(itemQuestion
												.getAttencount() + 1);
										itemQuestion.setIsattend(1);
										quesGuanzhuCount.setText(itemQuestion
												.getAttencount() + "");

									} else {
										quesGuanzhuImg
												.setImageDrawable(getResources()
														.getDrawable(
																R.drawable.guanzhu_question_gray));
										quesGuanzhuBtn
												.setBackgroundResource(R.drawable.main_no_corner_button);
										quesGuanzhuBtn.setText("关注");
										itemQuestion.setAttencount(itemQuestion
												.getAttencount() - 1);
										itemQuestion.setIsattend(0);
										quesGuanzhuCount.setText(itemQuestion
												.getAttencount() + "");
									}
								}
							});
		}
	};
	private OnClickListener addAnswerClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(DynamicDetail.this,
					PublishActivity_2.class);
			intent.putExtra("qid", itemQuestion.getQid());
			startActivityForResult(intent, PUBLISH_ANSWER);
		}
	};

	private void initHeaderViewLife(View v) {
		userImage = (ImageView) v.findViewById(R.id.detail_header_userImg);
		delImage = (ImageView) v.findViewById(R.id.detail_header_del);
		zanImage = (ImageView) v.findViewById(R.id.detail_header_zanimg);
		replyImage = (ImageView) v.findViewById(R.id.detail_header_replyimg);
		userName = (TextView) v.findViewById(R.id.detail_header_username);
		publishTime = (TextView) v.findViewById(R.id.detail_header_time);
		dynamicContent = (TextView) v.findViewById(R.id.detail_header_content);
		gridLl = (LinearLayout) v.findViewById(R.id.detail_header_grid_ll);
		zanLl = (LinearLayout) v.findViewById(R.id.detail_header_zan_ll);
		replyLl = (LinearLayout) v.findViewById(R.id.detail_header_reply_ll);
		gridView = (CGridView) v.findViewById(R.id.detail_header_gridview);
		zanText = (TextView) v.findViewById(R.id.detail_header_zantxt);
		replyText = (TextView) v.findViewById(R.id.detail_header_replytxt);

		dynamicContent.setText(item.getDcontent());
		userName.setText(item.getUname());

		long times = Tool.getTime(item.getDtime());
		String timeIntro = times > 0 ? Tool.getChatTime(DynamicDetail.this,
				times) : "";
		publishTime.setText("发布于:" + timeIntro);

		if (item.getReplysize() != 0)
			replyText.setText(item.getReplysize() + "");
		else
			replyText.setText("");
		if (item.getZanscount() != 0)
			zanText.setText(item.getZanscount() + "");
		else
			zanText.setText("");
		if (item.getIszan() == 0) {
			zanImage.setImageDrawable(getResources().getDrawable(
					R.drawable.zan_gray));
		} else {
			zanImage.setImageDrawable(getResources().getDrawable(
					R.drawable.zan_red));
		}

		String path = item.getUportrait();
		userImage.setTag(item.getUportrait());
		if (userImage.getTag() != null && userImage.getTag().equals(path))
			Tool.imageLoader(DynamicDetail.this, userImage, path, null);

		List<String> photos = new ArrayList<String>();
		for (Dynamic_Photos dp : item.getDpics()) {
			photos.add(dp.getDpic());
		}

		if (photos.size() == 0) {
			gridLl.setVisibility(View.GONE);
		} else {
			gridLl.setVisibility(View.VISIBLE);
		}
		DynamicPhotoAdapter adapter = new DynamicPhotoAdapter(
				DynamicDetail.this, gridView, photos);
		gridView.setAdapter(adapter);

		uid = SXContext.getInstance().getUserInfo().getUserid();
		if (uid == item.getUid()) {
			delImage.setVisibility(View.VISIBLE);
		} else {
			delImage.setVisibility(View.GONE);
		}
		userImage.setOnClickListener(userClickLis);
		userName.setOnClickListener(userClickLis);
		// delImage.setOnClickListener(delClickLis);
		zanLl.setOnClickListener(zanClickLis);
		replyLl.setOnClickListener(replyBtnClickLis);

	}

	private OnClickListener replyBtnClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			reply.setToUserId(0);
			reply.setToUserName(null);
			hintStr = "评论一下";
			detailReplyEdit.setText("");
			detailReplyEdit.setHint(hintStr);
		}
	};

	private static class replyClickLis implements OnClickListener {
		private Reply reply;
		private int id;
		private Context mContext;
		private TextView contentTxt;
		private Handler mHandler;
		private int detailType;

		public replyClickLis(Context context, Reply reply, int id,
				TextView contentTxt, Handler handler, int detailType) {
			super();
			this.mContext = context;
			this.reply = reply;
			this.id = id;
			this.contentTxt = contentTxt;
			this.mHandler = handler;
			this.detailType = detailType;
		}

		@Override
		public void onClick(View arg0) {
			String content = contentTxt.getText().toString().trim();
			if (TextUtils.isEmpty(content)) {
				T.showShort(mContext, "回复内容不能为空");
				return;
			}
			final LoadingDialog dialog = new LoadingDialog(mContext, "正在提交...");
			dialog.show();
			if (detailType == DETAIL_LIFE)
				submitDynamicReply(content, dialog);
			else if (detailType == SHOW_ANSWER_REPLY)
				submitAnswerReply(content, dialog);
		}

		private void submitAnswerReply(String content,
				final LoadingDialog dialog) {
			ReplyAnswerRequest params = new ReplyAnswerRequest();
			params.setAid(id);
			params.setContent(content);
			params.setFuid(reply.getFromUserId());
			params.setTuid(reply.getToUserId());

			SXContext.getInstance().getmSmileXiApi()
					.replyAnswer(params, new IApiCallback() {

						@Override
						public void onError(int errorCode) {
							dialog.dismiss();
						}

						@Override
						public void onComplete(Object object) {
							dialog.dismiss();
							mHandler.obtainMessage(LOADTYPE_REFRESH)
									.sendToTarget();
						}
					});
		}

		private void submitDynamicReply(String content,
				final LoadingDialog dialog) {
			SubmitReplyRequest params = new SubmitReplyRequest();
			params.setContent(content);
			params.setDid(id);
			params.setFuid(reply.getFromUserId());
			params.setFuname(reply.getFromUserName());
			params.setTuid(reply.getToUserId());
			params.setTuname(reply.getToUserName());

			SXContext.getInstance().getmSmileXiApi()
					.submitReply(params, new IApiCallback() {

						@Override
						public void onError(int errorCode) {
							dialog.dismiss();

						}

						@Override
						public void onComplete(Object object) {
							dialog.dismiss();
							mHandler.obtainMessage(LOADTYPE_REFRESH)
									.sendToTarget();
						}
					});
		}
	}

	private OnClickListener zanClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (item.getIszan() == 1) {
				T.showShort(DynamicDetail.this, "已经点过了");
			} else {
				ZanDynamic params = new ZanDynamic();
				params.setDid(item.getDid());
				params.setUid(uid);
				params.setOid(1);
				SXContext.getInstance().getmSmileXiApi()
						.zanDynamic(params, new IApiCallback() {

							@Override
							public void onError(int errorCode) {
								if (errorCode == -2) {
									T.showShort(DynamicDetail.this, "已经点过了");
								}
							}

							@Override
							public void onComplete(Object object) {
								item.setIszan(1);
								int zanCount = item.getZanscount();
								zanImage.setImageDrawable(getResources()
										.getDrawable(R.drawable.zan_red));
								item.setZanscount(zanCount + 1);
								if (item.getZanscount() > 0)
									zanText.setText(item.getZanscount() + "");
								// dynamicPtrlv.notifyDataSetChanged();
							}
						});
			}
		}
	};
	ConfirmDialog delDialog;
	private OnClickListener delClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			delDialog = new ConfirmDialog(DynamicDetail.this);
			delDialog.setTitle("提示");
			delDialog.setMessage("确认删除？");
			delDialog.setLeftButton("取消", new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					delDialog.dismiss();
				}
			});
			delDialog.setRightButton("确定", new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					delDialog.dismiss();
					SXContext.getInstance().getmSmileXiApi()
							.delDynamic(item.getDid(), new IApiCallback() {

								@Override
								public void onError(int errorCode) {
									T.showShort(DynamicDetail.this, "删除失败");
								}

								@Override
								public void onComplete(Object object) {
									T.showShort(DynamicDetail.this, "删除成功");
								}
							});
				}
			});
		}
	};
	private OnClickListener userClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(DynamicDetail.this,
					SelfMainPageActivity.class);
			intent.putExtra("uid", item.getUid());
			startActivity(intent);
		}
	};

	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		switch (arg0) {

		case PUBLISH_ANSWER:
			if (arg1 != RESULT_OK)
				return;
			itemQuestion.setAnswercount(itemQuestion.getAnswercount() + 1);
			loadWebRequest(LOADTYPE_INIT);
			break;
		case REQUEST_ANSWER_DETAIL:
			if (arg1 != RESULT_OK)
				return;
			Answer returnAnswer = (Answer) arg2.getSerializableExtra("answer");
			for (Answer a : answerList) {
				if (a.getId() == returnAnswer.getId()) {
					a.setIsCollect(returnAnswer.getIsCollect());
					a.setIsZan(returnAnswer.getIsZan());
					a.setZanCount(returnAnswer.getZanCount());
					break;
				}
			}
			answerAdapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
	};

	private void initIndicator() {
		ILoadingLayout startLabels = dynamicPtrlv.getLoadingLayoutProxy(true,
				false);
		startLabels.setPullLabel("                     下拉刷新");// 刚下拉时，显示的提示
		startLabels.setRefreshingLabel("                     正在刷新");// 刷新时
		startLabels.setReleaseLabel("                     释放刷新");// 下来达到一定距离时，显示的提示

		ILoadingLayout endLabels = dynamicPtrlv.getLoadingLayoutProxy(false,
				true);
		endLabels.setPullLabel("                     上拉刷新");
		endLabels.setRefreshingLabel("                     正在刷新");
		endLabels.setReleaseLabel("                     释放刷新");

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
		if (detailType == DETAIL_LIFE)
			titleLeftText.setText("动态详情");
		else if (detailType == DETAIL_QUESTION)
			titleLeftText.setText("问题详情");

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
		Intent intent = null;
		Bundle b = null;
		if (fromNotifacation == true) {
			intent = new Intent(DynamicDetail.this, MainActivity.class);
			startActivity(intent);
			finish();
			return;

		}
		if (detailType == DETAIL_LIFE) {
			if (replyList != null)
				item.setReplysize(replyList.size());
			intent = new Intent();
			b = new Bundle();
			b.putSerializable("item", item);
			intent.putExtras(b);
			setResult(RESULT_OK, intent);
		} else if (detailType == DETAIL_QUESTION) {
			intent = new Intent();
			b = new Bundle();
			b.putSerializable("item", itemQuestion);
			intent.putExtras(b);
			setResult(RESULT_OK, intent);
		} else if (detailType == SHOW_ANSWER_REPLY) {

		}
		finish();

	};
}
