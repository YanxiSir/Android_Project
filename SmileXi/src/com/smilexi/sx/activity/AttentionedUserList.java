package com.smilexi.sx.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.smilexi.sx.R;
import com.smilexi.sx.adapter.AttentionAdapter;
import com.smilexi.sx.app.BaseActivity;
import com.smilexi.sx.common.SXContext;
import com.smilexi.sx.common.SmileXiApi.IApiCallback;
import com.smilexi.sx.protocol.SxAttentioned;
import com.smilexi.sx.protocol.SxAttentioned.Attentioned;
import com.smilexi.sx.request.domain.GetAttentionedRequest;
import com.smilexi.sx.util.T;
import com.smilexi.sx.widget.LoadingDialog;

public class AttentionedUserList extends BaseActivity {

	public static final int LOADTYPE_INIT = 0;
	public static final int LOADTYPE_REFRESH = 1;
	public static final int LOADTYPE_MORE = 2;
	public static final int LOADTYPE_REINIT = 3;

	private RelativeLayout titleLeftRel;
	private TextView titleLeftText;
	private LinearLayout titleCenterLl;
	private EditText titleCenterEdit;
	private RelativeLayout titleRightRel;

	private PullToRefreshListView dynamicPtrlv;
	private AttentionAdapter adapter;
	private List<Attentioned> attList;

	private LoadingDialog loadingDialog;

	int sid = 0;
	int count = 16;
	int uid;
	
	private boolean isSearching = false;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_attention);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_ltext_csearch_rbtn);
		uid = SXContext.getInstance().getUserInfo().getUserid();
		initTitleBar();
		initUI();
		loadWebRequest(LOADTYPE_INIT);
	}

	private void loadWebRequest(final int loadType) {
		if (loadType == LOADTYPE_INIT || loadType == LOADTYPE_REFRESH
				|| loadType == LOADTYPE_REINIT) {
			sid = 0;
			count = 16;
		} else if (loadType == LOADTYPE_MORE) {
			sid += count;
		}
		if (loadType == LOADTYPE_INIT) {
			loadingDialog.show();
		}
		GetAttentionedRequest params = new GetAttentionedRequest();
		params.setUid(uid);
		params.setSid(sid);
		params.setCount(count);

		SXContext.getInstance().getmSmileXiApi()
				.getAttentioned(params, new IApiCallback() {

					@Override
					public void onError(int errorCode) {
						loadingDialog.dismiss();
					}

					@Override
					public void onComplete(Object object) {
						loadingDialog.dismiss();

						SxAttentioned result = (SxAttentioned) object;
						if (loadType == LOADTYPE_INIT
								|| loadType == LOADTYPE_REFRESH) {
							if (attList == null)
								attList = new ArrayList<SxAttentioned.Attentioned>();
							else
								attList.clear();
						}
						if (result == null || result.getAtts().size() == 0) {
							dynamicPtrlv.onRefreshComplete();
							if (attList == null || attList.size() == 0)
								T.showShort(AttentionedUserList.this, "没有关注任何人");
						}
						if (result != null)
							attList.addAll(result.getAtts());
						if (loadType == LOADTYPE_INIT) {
							adapter = new AttentionAdapter(
									AttentionedUserList.this, attList);
							dynamicPtrlv.setAdapter(adapter);
						} else if (loadType == LOADTYPE_REFRESH) {
							adapter.notifyDataSetChanged();
							dynamicPtrlv.onRefreshComplete();
						} else if (loadType == LOADTYPE_MORE) {
							adapter.notifyDataSetChanged();
							dynamicPtrlv.onRefreshComplete();
						}
					}
				});
	}

	private void initUI() {
		dynamicPtrlv = (PullToRefreshListView) findViewById(R.id.attention_ptrlv);
		initIndicator();
		loadingDialog = new LoadingDialog(AttentionedUserList.this, "正在加载...");
	}

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

		dynamicPtrlv.setOnRefreshListener(refreshLis);
		dynamicPtrlv.setOnItemClickListener(attentionItemClickLis);
	}

	private OnItemClickListener attentionItemClickLis = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent(AttentionedUserList.this,
					SelfMainPageActivity.class);
			intent.putExtra("uid", attList.get(position).getUid());
			startActivity(intent);
		}
	};
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
		titleLeftRel = (RelativeLayout) findViewById(R.id.titlebar_ltcerb_left_rel);
		titleLeftText = (TextView) findViewById(R.id.titlebar_ltcerb__text);
		titleCenterLl = (LinearLayout) findViewById(R.id.titlebar_ltcerb_search_ll);
		titleCenterEdit = (EditText) findViewById(R.id.titlebar_ltcerb_edit);
		titleRightRel = (RelativeLayout) findViewById(R.id.titlebar_ltcerb_right_rel);
		

		titleLeftText.setVisibility(View.VISIBLE);
		titleCenterLl.setVisibility(View.GONE);
		titleRightRel.setVisibility(View.GONE);
		
		titleLeftText.setText("关注的人");
		titleLeftRel.setOnClickListener(titleLeftRelClickLis);
	}

	private OnClickListener titleLeftRelClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			onBackPressed();
		}
	};
}
