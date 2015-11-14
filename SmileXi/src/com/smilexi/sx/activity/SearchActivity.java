package com.smilexi.sx.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.smilexi.sx.R;
import com.smilexi.sx.adapter.AttentionAdapter;
import com.smilexi.sx.app.BaseActivity;
import com.smilexi.sx.common.SXContext;
import com.smilexi.sx.common.SmileXiApi.IApiCallback;
import com.smilexi.sx.protocol.SxAttentioned;
import com.smilexi.sx.protocol.SxAttentioned.Attentioned;
import com.smilexi.sx.util.T;
import com.smilexi.sx.widget.LoadingDialog;

public class SearchActivity extends BaseActivity {
	
	private RelativeLayout titleLeftRel;
	private TextView titleLeftText;
	private EditText titleCenterEdit;
	private LinearLayout titleCenterLl;
	private RelativeLayout titleRightRel;
	
	private PullToRefreshListView dynamicPtrlv;
	private AttentionAdapter adapter;
	private List<Attentioned> userList;
	
	int sid = 0;
	int count = 16;
	int uid;
	
	private LoadingDialog loadingDialog;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_search);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_ltext_csearch_rbtn);
		
		uid = SXContext.getInstance().getUserInfo().getUserid();
		
		initTitleBar();
		initUI();
	}
	
	private void initUI(){
		dynamicPtrlv = (PullToRefreshListView) findViewById(R.id.search_ptrlv);
		initIndicator();
		
	}
	private void initIndicator() {
		ILoadingLayout startLabels = dynamicPtrlv.getLoadingLayoutProxy(true,
				false);
		startLabels.setPullLabel("                     ����ˢ��");// ������ʱ����ʾ����ʾ
		startLabels.setRefreshingLabel("                     ����ˢ��");// ˢ��ʱ
		startLabels.setReleaseLabel("                     �ͷ�ˢ��");// �����ﵽһ������ʱ����ʾ����ʾ

		ILoadingLayout endLabels = dynamicPtrlv.getLoadingLayoutProxy(false,
				true);
		endLabels.setPullLabel("                     ����ˢ��");
		endLabels.setRefreshingLabel("                     ����ˢ��");
		endLabels.setReleaseLabel("                     �ͷ�ˢ��");

		dynamicPtrlv.setOnItemClickListener(attentionItemClickLis);
	}
	private OnItemClickListener attentionItemClickLis = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent(SearchActivity.this,
					SelfMainPageActivity.class);
			intent.putExtra("uid", userList.get(position).getUid());
			startActivity(intent);
		}
	};
	
	private void loadWebRequest(String key){
		if(loadingDialog == null)
			loadingDialog = new LoadingDialog(SearchActivity.this, "��������...");
		loadingDialog.show();
		SXContext.getInstance().getmSmileXiApi().searchUser(uid, key, new IApiCallback() {
			
			@Override
			public void onError(int errorCode) {
				loadingDialog.dismiss();
				T.showShort(SearchActivity.this, "û�в��ҵ�����");
			}
			
			@Override
			public void onComplete(Object object) {
				loadingDialog.dismiss();
				SxAttentioned userResult = (SxAttentioned) object;
				if(userResult == null || userResult.getAtts().size() == 0){
					T.showShort(SearchActivity.this, "û�в��ҵ�����");
					return ;
				}
				if(userList == null){
					userList = new ArrayList<SxAttentioned.Attentioned>();
				}else{
					userList.clear();
				}
				userList.addAll(userResult.getAtts());
				adapter = new AttentionAdapter(
						SearchActivity.this, userList);
				dynamicPtrlv.setAdapter(adapter);
			}
		});
	}
	
	private void initTitleBar() {
		titleLeftRel = (RelativeLayout) findViewById(R.id.titlebar_ltcerb_left_rel);
		titleLeftText = (TextView) findViewById(R.id.titlebar_ltcerb__text);
		titleCenterLl = (LinearLayout) findViewById(R.id.titlebar_ltcerb_search_ll);
		titleCenterEdit = (EditText) findViewById(R.id.titlebar_ltcerb_edit);
		titleRightRel = (RelativeLayout) findViewById(R.id.titlebar_ltcerb_right_rel);
		

		titleLeftText.setVisibility(View.GONE);
		titleCenterLl.setVisibility(View.VISIBLE);
		titleRightRel.setVisibility(View.VISIBLE);
		
		titleLeftText.setText("��ע����");
		titleLeftRel.setOnClickListener(titleLeftRelClickLis);
		titleRightRel.setOnClickListener(titleRightRelClickLis); 
	}
	private OnClickListener titleRightRelClickLis = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			String key = titleCenterEdit.getText().toString().trim();
			if(TextUtils.isEmpty(key)){
				T.showShort(SearchActivity.this, "��д��Ҫ����������");
				return ;
			}
			loadWebRequest(key);
		}
	};
	private OnClickListener titleLeftRelClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			onBackPressed();
		}
	};
}
