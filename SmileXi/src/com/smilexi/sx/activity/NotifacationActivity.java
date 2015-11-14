package com.smilexi.sx.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.smilexi.sx.R;
import com.smilexi.sx.adapter.DynamicAdapter;
import com.smilexi.sx.adapter.NotifacationAdapter;
import com.smilexi.sx.app.BaseActivity;
import com.smilexi.sx.common.SXContext;
import com.smilexi.sx.common.SmileXiApi.IApiCallback;
import com.smilexi.sx.protocol.SxDynamics;
import com.smilexi.sx.protocol.SxNotiReply;
import com.smilexi.sx.protocol.SxNotiReply.NotiReply;
import com.smilexi.sx.protocol.SxNotiZan;
import com.smilexi.sx.protocol.SxNotiZan.NotiZan;
import com.smilexi.sx.request.domain.NotiRequest;
import com.smilexi.sx.util.T;
import com.smilexi.sx.widget.LoadingDialog;

public class NotifacationActivity extends BaseActivity {

	public static final int LOADTYPE_INIT = 0;
	public static final int LOADTYPE_REFRESH = 1;
	public static final int LOADTYPE_MORE = 2;
	public static final int LOADTYPE_REINIT = 3;

	public static final int TYPE_REPLY = 0;
	public static final int TYPE_ANSWER = 1;
	public static final int TYPE_ZAN = 2;

	/*
	 * titlebar
	 */
	private RelativeLayout titleLeftRel, titleRightRelOne, titleRightRelTwo;
	private ImageView titleLeftImage, titleLeftTextImage, titleRightImageOne,
			titleRightImageTwo;
	private LinearLayout titleLeftLl;
	private TextView titleLeftText;

	private PullToRefreshListView dynamicPtrlv;
	private List<NotiReply> notiReplyList;
	private List<NotiZan> notiZanList;

	private NotifacationAdapter adapter;

	int type = -1;
	int sid = 0;
	int count = 8;

	private LoadingDialog loadingDialog;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_notifacate);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_ltext_rtwo_btn);
		type = getIntent().getIntExtra("notifacateType", TYPE_REPLY);
		initTitleBar();
		initUI();
		loadWebRequest(LOADTYPE_INIT);
	}

	public void loadWebRequest(int otype) {
		if (type == TYPE_REPLY) {
			loadWebRequestReply(otype);
		} else if (type == TYPE_ANSWER) {
		} else if (type == TYPE_ZAN) {
			loadWebRequestZan(otype);
		}
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

	public void loadWebRequestZan(final int otype) {
		if (otype == LOADTYPE_INIT || otype == LOADTYPE_REFRESH
				|| otype == LOADTYPE_REINIT) {
			sid = 0;
			count = 8;
		} else if (otype == LOADTYPE_MORE) {
			sid += count;
		}
		if (otype == LOADTYPE_INIT) {
			loadingDialog = new LoadingDialog(NotifacationActivity.this,
					"正在加载...");
			loadingDialog.show();
		}
		NotiRequest params = new NotiRequest();
		params.setUid(SXContext.getInstance().getUserInfo().getUserid());
		params.setSid(sid);
		params.setCount(count);
		SXContext.getInstance().getmSmileXiApi()
				.getNotiZan(params, new IApiCallback() {

					@Override
					public void onError(int errorCode) {
						loadingDialog.dismiss();
					}

					@Override
					public void onComplete(Object object) {
						loadingDialog.dismiss();
						SxNotiZan sxNotiZan = (SxNotiZan) object;

						if (sxNotiZan == null
								|| sxNotiZan.getNotiZans().size() == 0) {
							T.showShort(NotifacationActivity.this, "没有数据");
							dynamicPtrlv.onRefreshComplete();
							return;
						}
						if (otype == LOADTYPE_INIT || otype == LOADTYPE_REFRESH
								|| otype == LOADTYPE_REINIT) {
							if (notiZanList == null)
								notiZanList = new ArrayList<NotiZan>();
							else
								notiZanList.clear();
						}
						notiZanList.addAll(sxNotiZan.getNotiZans());
						if (otype == LOADTYPE_INIT) {
							adapter = new NotifacationAdapter(
									NotifacationActivity.this,
									NotifacationAdapter.TYPE_DYNAMIC_ZAN,
									notiZanList);
							dynamicPtrlv.setAdapter(adapter);
						} else if (otype == LOADTYPE_REFRESH) {
							if (adapter == null) {
								adapter = new NotifacationAdapter(
										NotifacationActivity.this,
										NotifacationAdapter.TYPE_DYNAMIC_ZAN,
										notiZanList);
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

	public void loadWebRequestReply(final int otype) {
		if (otype == LOADTYPE_INIT || otype == LOADTYPE_REFRESH
				|| otype == LOADTYPE_REINIT) {
			sid = 0;
			count = 8;
		} else if (otype == LOADTYPE_MORE) {
			sid += count;
		}
		loadingDialog = new LoadingDialog(NotifacationActivity.this, "正在加载...");
		loadingDialog.show();
		NotiRequest params = new NotiRequest();
		params.setUid(SXContext.getInstance().getUserInfo().getUserid());
		params.setSid(sid);
		params.setCount(count);
		SXContext.getInstance().getmSmileXiApi()
				.getNotiReply(params, new IApiCallback() {

					@Override
					public void onError(int errorCode) {
						loadingDialog.dismiss();
					}

					@Override
					public void onComplete(Object object) {
						loadingDialog.dismiss();
						SxNotiReply sxNotiReply = (SxNotiReply) object;

						if (sxNotiReply == null
								|| sxNotiReply.getNotiReplys().size() == 0) {
							T.showShort(NotifacationActivity.this, "没有数据");
							dynamicPtrlv.onRefreshComplete();
							return;
						}
						if (otype == LOADTYPE_INIT || otype == LOADTYPE_REFRESH
								|| otype == LOADTYPE_REINIT) {
							if (notiReplyList == null)
								notiReplyList = new ArrayList<NotiReply>();
							else
								notiReplyList.clear();
						}
						notiReplyList.addAll(sxNotiReply.getNotiReplys());
						if (otype == LOADTYPE_INIT) {
							adapter = new NotifacationAdapter(
									NotifacationActivity.this, notiReplyList,
									NotifacationAdapter.TYPE_DYNAMIC_REPLY);
							dynamicPtrlv.setAdapter(adapter);
						} else if (otype == LOADTYPE_REFRESH) {
							if (adapter == null) {
								adapter = new NotifacationAdapter(
										NotifacationActivity.this,
										notiReplyList,
										NotifacationAdapter.TYPE_DYNAMIC_REPLY);
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

	private void initUI() {
		dynamicPtrlv = (PullToRefreshListView) findViewById(R.id.notifacation_ptrlv);
		initIndicator();
	}

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
		titleRightRelOne.setVisibility(View.GONE);
		titleRightRelTwo.setVisibility(View.GONE);

		if (type == TYPE_REPLY) {
			titleLeftText.setText("所有评论");
		} else if (type == TYPE_ANSWER) {
			titleLeftText.setText("所有回答");
		} else if (type == TYPE_ZAN) {
			titleLeftText.setText("赞");
		}

		titleLeftRel.setOnClickListener(titleLeftRelClickLis);

	}

	private OnClickListener titleLeftRelClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			onBackPressed();
		}
	};

}
