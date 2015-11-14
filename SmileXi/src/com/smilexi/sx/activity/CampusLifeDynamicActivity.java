package com.smilexi.sx.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.smilexi.sx.R;
import com.smilexi.sx.adapter.AnswerAdapter;
import com.smilexi.sx.adapter.DynamicAdapter;
import com.smilexi.sx.adapter.PopupDialogAdapter;
import com.smilexi.sx.adapter.QuestionAdapter;
import com.smilexi.sx.app.BaseActivity;
import com.smilexi.sx.common.SXContext;
import com.smilexi.sx.common.SmileXiApi.IApiCallback;
import com.smilexi.sx.protocol.SxAnswers;
import com.smilexi.sx.protocol.SxAnswers.Answer;
import com.smilexi.sx.protocol.SxDynamics;
import com.smilexi.sx.protocol.SxDynamics.Dynamic;
import com.smilexi.sx.protocol.SxQuestions;
import com.smilexi.sx.protocol.SxQuestions.Question;
import com.smilexi.sx.request.domain.UserGetDynamicsRequest;
import com.smilexi.sx.util.T;
import com.smilexi.sx.widget.LoadingDialog;
import com.smilexi.sx.widget.SelectPopup;

public class CampusLifeDynamicActivity extends BaseActivity {

	public static final int REQUEST_LIFE_DYNAMIC_DETAIL = 10;
	public static final int REQUEST_QUESTION_DYNAMIC_DETAIL = 11;

	public static final int LOADTYPE_INIT = 0;
	public static final int LOADTYPE_REFRESH = 1;
	public static final int LOADTYPE_MORE = 2;
	public static final int LOADTYPE_REINIT = 3;

	public static final int CAMPUS_DYNAMIC = 0;
	public static final int QUESTION_DYNAMIC = 1;
	public static final int COLLECTED_ANSWER = 2;
	
	private static final int REQUEST_ANSWER_DETAIL = 2;

	/*
	 * titlebar
	 */
	private RelativeLayout titleLeftRel, titleRightRelOne, titleRightRelTwo;
	private ImageView titleLeftImage, titleLeftTextImage, titleRightImageOne,
			titleRightImageTwo;
	private LinearLayout titleLeftLl;
	private TextView titleLeftText;

	int sid = 0;
	int count = 8;
	int oid = 1;// 1获取所有动态,2获取指定用户wid的动态，3获取我关注的问题
	int wid = 0;// 默认为0

	int dynamicType;

	/*
	 * 布局
	 */
	private PullToRefreshListView dynamicPtrlv;
	private DynamicAdapter adapter;
	private QuestionAdapter questionAdapter;
	private AnswerAdapter answerAdapter;
	List<Dynamic> dynamicList;
	List<Question> questionList;
	List<Answer> collectAnswerList;

	private LoadingDialog loadingDialog;

	private SelectPopup selectPopup;
	private boolean popupIsShowing = false;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case PopupDialogAdapter.SELECT_ID:
				Bundle b = (Bundle) msg.obj;
				int id = b.getInt("oid");
				popupIsShowing = false;

				if (id == 0) {
					oid = 1;
				} else if (id == 1) {
					oid = 3;
				} else if (id == 2) {
					oid = 2;
					wid = SXContext.getInstance().getUserInfo().getUserid();
				}
				loadWebRequest(LOADTYPE_INIT);

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
		setContentView(R.layout.activity_campus_life);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_ltext_rtwo_btn);

		wid = getIntent().getIntExtra("wid", 0);
		dynamicType = getIntent().getIntExtra("dynamicType", CAMPUS_DYNAMIC);

		if (wid != 0) {
			oid = 2;
		}

		initTitleBar();
		initUI();

		loadWebRequest(LOADTYPE_INIT);
	}

	public void loadWebRequest(int otype) {
		if (dynamicType == CAMPUS_DYNAMIC) {
			loadWebRequestLife(otype);
		} else if (dynamicType == QUESTION_DYNAMIC) {
			loadWebRequestQuestion(otype);
		}else if(dynamicType == COLLECTED_ANSWER){
			loadWebRequestCollectedAnswers(otype); 
		}
	}

	public void loadWebRequestCollectedAnswers(final int otype) {
		if (otype == LOADTYPE_INIT || otype == LOADTYPE_REFRESH
				|| otype == LOADTYPE_REINIT) {
			sid = 0;
			count = 12;
		} else if (otype == LOADTYPE_MORE) {
			sid += count;
		}
		if (otype == LOADTYPE_INIT) {
			loadingDialog.show();
		}
		int uid = SXContext.getInstance().getUserInfo().getUserid();

		SXContext.getInstance().getmSmileXiApi()
				.getCollectedAnswers(uid, new IApiCallback() {

					@Override
					public void onError(int errorCode) {
						loadingDialog.dismiss();
						T.showShort(CampusLifeDynamicActivity.this, "请求失败");
						dynamicPtrlv.onRefreshComplete();
					}

					@Override
					public void onComplete(Object object) {
						loadingDialog.dismiss();
						SxAnswers answers = (SxAnswers) object;

						if (answers == null || answers.getAnswers().size() == 0) {
							T.showShort(CampusLifeDynamicActivity.this, "没有数据");
							dynamicPtrlv.onRefreshComplete();
							return;
						}
						if (otype == LOADTYPE_INIT || otype == LOADTYPE_REFRESH
								|| otype == LOADTYPE_REINIT) {
							if (collectAnswerList == null)
								collectAnswerList = new ArrayList<Answer>();
							else
								collectAnswerList.clear();
						}
						collectAnswerList.addAll(answers.getAnswers());
						if (otype == LOADTYPE_INIT) {
							answerAdapter = new AnswerAdapter(
									CampusLifeDynamicActivity.this,
									collectAnswerList,
									AnswerAdapter.TYPE_COLLECTED_ANSWERS);
							dynamicPtrlv.setAdapter(answerAdapter);
							
							dynamicPtrlv.setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> arg0, View arg1,
										int arg2, long arg3) {
									Intent intent = new Intent(CampusLifeDynamicActivity.this,
											AnswerDetailActivity.class);
									Bundle b = new Bundle();
									b.putSerializable("answer", collectAnswerList.get(arg2)); 
									b.putString("title", collectAnswerList.get(arg2).getqTitle());
									intent.putExtras(b);
									startActivityForResult(intent, REQUEST_ANSWER_DETAIL);
								}

							});
							
						} else if (otype == LOADTYPE_REFRESH) {
							if (answerAdapter == null) {
								answerAdapter = new AnswerAdapter(
										CampusLifeDynamicActivity.this,
										collectAnswerList,
										AnswerAdapter.TYPE_COLLECTED_ANSWERS);
								dynamicPtrlv.setAdapter(answerAdapter);
							} else {
								answerAdapter.notifyDataSetChanged();
								dynamicPtrlv.onRefreshComplete();
							}
							dynamicPtrlv.isRefreshing();
						} else if (otype == LOADTYPE_MORE) {
							answerAdapter.notifyDataSetChanged();
							dynamicPtrlv.onRefreshComplete();
						}

					}
				});
	}

	public void loadWebRequestQuestion(final int otype) {
		if (otype == LOADTYPE_INIT || otype == LOADTYPE_REFRESH
				|| otype == LOADTYPE_REINIT) {
			sid = 0;
			count = 12;
		} else if (otype == LOADTYPE_MORE) {
			sid += count;
		}
		if (otype == LOADTYPE_INIT) {
			loadingDialog.show();
		}
		UserGetDynamicsRequest params = new UserGetDynamicsRequest();
		params.setStartId(sid);
		params.setCount(count);
		params.setOid(oid);
		params.setWid(wid);
		params.setUid(SXContext.getInstance().getUserInfo().getUserid());

		SXContext.getInstance().getmSmileXiApi()
				.getQuestion(params, new IApiCallback() {

					@Override
					public void onError(int errorCode) {
						loadingDialog.dismiss();
						T.showShort(CampusLifeDynamicActivity.this, "请求失败");
						dynamicPtrlv.onRefreshComplete();
					}

					@Override
					public void onComplete(Object object) {
						loadingDialog.dismiss();
						SxQuestions questions = (SxQuestions) object;

						if (questions == null
								|| questions.getQuestions().size() == 0) {
							T.showShort(CampusLifeDynamicActivity.this, "没有数据");
							dynamicPtrlv.onRefreshComplete();
							return;
						}
						if (otype == LOADTYPE_INIT || otype == LOADTYPE_REFRESH
								|| otype == LOADTYPE_REINIT) {
							if (questionList == null)
								questionList = new ArrayList<Question>();
							else
								questionList.clear();
						}
						questionList.addAll(questions.getQuestions());
						if (otype == LOADTYPE_INIT) {
							questionAdapter = new QuestionAdapter(
									CampusLifeDynamicActivity.this,
									questionList);
							dynamicPtrlv.setAdapter(questionAdapter);
						} else if (otype == LOADTYPE_REFRESH) {
							if (questionAdapter == null) {
								questionAdapter = new QuestionAdapter(
										CampusLifeDynamicActivity.this,
										questionList);
								dynamicPtrlv.setAdapter(questionAdapter);
							} else {
								questionAdapter.notifyDataSetChanged();
								dynamicPtrlv.onRefreshComplete();
							}
							dynamicPtrlv.isRefreshing();
						} else if (otype == LOADTYPE_MORE) {
							questionAdapter.notifyDataSetChanged();
							dynamicPtrlv.onRefreshComplete();
						}

					}
				});
	}

	public void loadWebRequestLife(final int otype) {
		if (otype == LOADTYPE_INIT || otype == LOADTYPE_REFRESH
				|| otype == LOADTYPE_REINIT) {
			sid = 0;
			count = 8;
		} else if (otype == LOADTYPE_MORE) {
			sid += count;
		}
		if (otype == LOADTYPE_INIT) {
			loadingDialog.show();
		}
		UserGetDynamicsRequest params = new UserGetDynamicsRequest();
		params.setStartId(sid);
		params.setCount(count);
		params.setOid(oid);
		params.setWid(wid);
		params.setUid(SXContext.getInstance().getUserInfo().getUserid());

		SXContext.getInstance().getmSmileXiApi()
				.getDynamics(params, new IApiCallback() {

					@Override
					public void onError(int errorCode) {
						loadingDialog.dismiss();
						T.showShort(CampusLifeDynamicActivity.this, "请求失败");
						dynamicPtrlv.onRefreshComplete();
					}

					@Override
					public void onComplete(Object object) {
						loadingDialog.dismiss();
						SxDynamics dynamics = (SxDynamics) object;

						if (dynamics == null
								|| dynamics.getDynamics().size() == 0) {
							T.showShort(CampusLifeDynamicActivity.this, "没有数据");
							dynamicPtrlv.onRefreshComplete();
							return;
						}
						if (otype == LOADTYPE_INIT || otype == LOADTYPE_REFRESH
								|| otype == LOADTYPE_REINIT) {
							if (dynamicList == null)
								dynamicList = new ArrayList<SxDynamics.Dynamic>();
							else
								dynamicList.clear();
						}
						dynamicList.addAll(dynamics.getDynamics());
						if (otype == LOADTYPE_INIT) {
							adapter = new DynamicAdapter(
									CampusLifeDynamicActivity.this, dynamicList);
							dynamicPtrlv.setAdapter(adapter);
						} else if (otype == LOADTYPE_REFRESH) {
							if (adapter == null) {
								adapter = new DynamicAdapter(
										CampusLifeDynamicActivity.this,
										dynamicList);
								dynamicPtrlv.setAdapter(adapter);
							} else {
								adapter.notifyDataSetChanged();
								dynamicPtrlv.onRefreshComplete();
							}
							dynamicPtrlv.isRefreshing();
						} else if (otype == LOADTYPE_MORE) {
							adapter.notifyDataSetChanged();
							dynamicPtrlv.onRefreshComplete();
						}

					}
				});
	}

	/*
	 * 初始化代码
	 */

	private void initUI() {
		dynamicPtrlv = (PullToRefreshListView) findViewById(R.id.campuslife_ptrlv);
		initIndicator();

		loadingDialog = new LoadingDialog(CampusLifeDynamicActivity.this,
				"正在加载...");
	}

	private void initIndicator() {

		/*
		 * if (dynamicType == CAMPUS_DYNAMIC) {
		 * dynamicPtrlv.getRefreshableView().setDividerHeight(
		 * R.dimen.item_line_size); } else if (dynamicType == QUESTION_DYNAMIC)
		 * { dynamicPtrlv.getRefreshableView().setDividerHeight(
		 * R.dimen.item_line_size_1dp); }
		 */

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

		dynamicPtrlv.setOnRefreshListener(refreshLis);
	}
	private PullToRefreshBase.OnRefreshListener2<ListView> refreshLis = new PullToRefreshBase.OnRefreshListener2<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					loadWebRequest(LOADTYPE_REFRESH);
				}
			}, 300);
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			// TODO Auto-generated method stub
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					loadWebRequest(LOADTYPE_MORE);
				}
			}, 300);
		}
	};

	private void initTitleBar() {
		titleLeftLl = (LinearLayout) findViewById(R.id.title_tbb_left_ll);
		titleLeftRel = (RelativeLayout) findViewById(R.id.title_tbb_left_rel);
		titleRightRelOne = (RelativeLayout) findViewById(R.id.title_tbb_right_rel_one);
		titleRightRelTwo = (RelativeLayout) findViewById(R.id.title_tbb_right_rel_two);
		titleLeftImage = (ImageView) findViewById(R.id.title_tbb_left_image);
		titleLeftTextImage = (ImageView) findViewById(R.id.title_tbb_textimage);
		titleRightImageOne = (ImageView) findViewById(R.id.title_tbb_right_image_one);
		titleRightImageTwo = (ImageView) findViewById(R.id.title_tbb_right_image_two);
		titleLeftText = (TextView) findViewById(R.id.title_tbb_left_text);

		titleLeftRel.setVisibility(View.VISIBLE);
		titleLeftLl.setVisibility(View.VISIBLE);
		titleLeftImage.setImageDrawable(getResources().getDrawable(
				R.drawable.back));
		titleLeftTextImage.setVisibility(View.GONE);
		if (dynamicType == QUESTION_DYNAMIC) {
			titleLeftText.setText("问答");
			titleRightRelOne.setVisibility(View.VISIBLE);
			titleRightRelTwo.setVisibility(View.VISIBLE);
			titleRightImageOne.setImageDrawable(getResources().getDrawable(
					R.drawable.fabu_pencel));
			titleRightImageTwo.setImageDrawable(getResources().getDrawable(
					R.drawable.more));
			titleRightRelOne.setOnClickListener(titleRightRelClickLis);
			titleRightRelTwo.setOnClickListener(titleRightRelMoreClickLis);
		} else if (dynamicType == CAMPUS_DYNAMIC) {
			titleLeftText.setText("生活动态");
			titleRightRelOne.setVisibility(View.GONE);
			titleRightRelTwo.setVisibility(View.VISIBLE);
			titleRightImageTwo.setImageDrawable(getResources().getDrawable(
					R.drawable.fabu_pencel));
			titleRightRelTwo.setOnClickListener(titleRightRelClickLis);

		}else if(dynamicType == COLLECTED_ANSWER){
			titleLeftText.setText("收藏的答案");
			titleRightRelOne.setVisibility(View.GONE);
			titleRightRelTwo.setVisibility(View.GONE);
		}

		titleLeftRel.setOnClickListener(titleLeftRelClickLis);

	}

	private OnClickListener titleRightRelMoreClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (!popupIsShowing)
				popupIsShowing = true;
			else {
				popupIsShowing = false;
				selectPopup.dismiss();
				return;
			}
			List<String> list = new ArrayList<String>();
			list.add("全部问题");
			list.add("我关注的");
			list.add("我的问题");
			selectPopup = new SelectPopup(CampusLifeDynamicActivity.this,
					handler, list, titleRightRelTwo);

		}
	};
	public static final int PUBLISH_LIFE_DYNAMIC = 100;
	private OnClickListener titleRightRelClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(CampusLifeDynamicActivity.this,
					PublishActivity_1.class);
			if (dynamicType == CAMPUS_DYNAMIC)
				intent.putExtra("publishType",
						PublishActivity_1.PUBLISH_LIFE_DYNAMIC);
			else if (dynamicType == QUESTION_DYNAMIC)
				intent.putExtra("publishType",
						PublishActivity_1.PUBLISH_ASK_QUESTION);
			startActivityForResult(intent, PUBLISH_LIFE_DYNAMIC);
		}
	};
	private OnClickListener titleLeftRelClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			onBackPressed();
		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		int id;
		switch (requestCode) {
		case PUBLISH_LIFE_DYNAMIC:
			loadWebRequest(LOADTYPE_REFRESH);
			break;
		case REQUEST_LIFE_DYNAMIC_DETAIL:
			Dynamic dynamic = (Dynamic) data.getSerializableExtra("item");
			if (dynamic == null)
				return;
			id = dynamic.getDid();
			for (Dynamic d : dynamicList) {
				if (d.getDid() == id) {
					d.setReplysize(dynamic.getReplysize());
					d.setIszan(dynamic.getIszan());
					d.setZanscount(dynamic.getZanscount());
					break;
				}
			}
			adapter.notifyDataSetChanged();
			break;
		case REQUEST_QUESTION_DYNAMIC_DETAIL:
			Question question = (Question) data.getSerializableExtra("item");
			if (question == null)
				return;
			id = question.getQid();
			for (Question q : questionList) {
				if (q.getQid() == id) {
					q.setAttencount(question.getAttencount());
					q.setIsattend(question.getIsattend());
					q.setAnswercount(question.getAnswercount());
					break;
				}
			}
			questionAdapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
	};
}
