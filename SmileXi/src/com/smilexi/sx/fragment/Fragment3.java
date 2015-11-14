package com.smilexi.sx.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smilexi.sx.R;
import com.smilexi.sx.activity.AttentionedUserList;
import com.smilexi.sx.activity.CampusLifeDynamicActivity;
import com.smilexi.sx.activity.SelfInfoActivity;
import com.smilexi.sx.activity.SelfMainPageActivity;
import com.smilexi.sx.common.SXContext;
import com.smilexi.sx.interfaces.RefreshFragment;
import com.smilexi.sx.util.Tool;

public class Fragment3 extends Fragment implements RefreshFragment {
	private ImageView headImage;
	private TextView userNameText, signWordText;
	private RelativeLayout userInfoRel;

	private LinearLayout daxueLl, guanzhuLl, dongtaiLl, shezhiLl,shoucangLl,questionLl;

	View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_menu3, container, false);
		initUI(rootView);

		userInfoRel.setOnClickListener(userInfoRelClickLis);

		return rootView;
	}

	private OnClickListener userInfoRelClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(getActivity(), SelfInfoActivity.class);
			startActivity(intent);
		}
	};

	public void initUI(View v) {
		headImage = (ImageView) v.findViewById(R.id.fra3_user_image);
		userNameText = (TextView) v.findViewById(R.id.fra3_username);
		signWordText = (TextView) v.findViewById(R.id.fra3_sign_word);
		userInfoRel = (RelativeLayout) v.findViewById(R.id.fra3_userinfo_rel);

		initDate();

		daxueLl = (LinearLayout) v.findViewById(R.id.fra3_daxuell);
		guanzhuLl = (LinearLayout) v.findViewById(R.id.fra3_guanzhull);
		dongtaiLl = (LinearLayout) v.findViewById(R.id.fra3_dongtaill);
		shezhiLl = (LinearLayout) v.findViewById(R.id.fra3_shezhill);
		shoucangLl = (LinearLayout) v.findViewById(R.id.fra3_shoucangll);
		questionLl = (LinearLayout) v.findViewById(R.id.fra3_questionll);

		daxueLl.setVisibility(View.GONE);
		
		daxueLl.setOnClickListener(daxueClickLis);
		guanzhuLl.setOnClickListener(guanzhuClickLis);
		dongtaiLl.setOnClickListener(dongtaiClickLis);
		shezhiLl.setOnClickListener(shezhiClickLis);
		shoucangLl.setOnClickListener(shoucangClickLis);
		questionLl.setOnClickListener(questionClickLis); 
	}

	private OnClickListener questionClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(getActivity(),
					CampusLifeDynamicActivity.class);
			Bundle b = new Bundle();
			b.putInt("wid", SXContext.getInstance().getUserInfo().getUserid());
			b.putInt("dynamicType", CampusLifeDynamicActivity.QUESTION_DYNAMIC);
			intent.putExtras(b);
			startActivity(intent);
		}
	};
	private OnClickListener shoucangClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(getActivity(), CampusLifeDynamicActivity.class);
			intent.putExtra("dynamicType", CampusLifeDynamicActivity.COLLECTED_ANSWER);
			startActivity(intent);
		}
	};
	private OnClickListener daxueClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {

		}
	};
	private OnClickListener guanzhuClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(getActivity(), AttentionedUserList.class);
			startActivity(intent);
		}
	};
	private OnClickListener dongtaiClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(getActivity(),
					CampusLifeDynamicActivity.class);
			Bundle b = new Bundle();
			b.putInt("wid", SXContext.getInstance().getUserInfo().getUserid());
			b.putInt("dynamicType", CampusLifeDynamicActivity.CAMPUS_DYNAMIC);
			intent.putExtras(b);
			startActivity(intent);
		}
	};
	private OnClickListener shezhiClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {

		}
	};

	private void initDate() {
		String username = SXContext.getInstance().getUserInfo().getNickname();
		String portrait = SXContext.getInstance().getUserInfo().getPortrait();
		String signWord = SXContext.getInstance().getUserInfo().getSignstr();

		if (userNameText != null)
			userNameText.setText(username);
		if (TextUtils.isEmpty(signWord)) {
			signWordText.setText("Ç©Ãû£ºnull");
		} else {
			signWordText.setText(signWord);
		}

		Tool.imageLoader(getActivity(), headImage, portrait, null);
	}

	@Override
	public void refreshContext() {
		if (rootView != null)
			initUI(rootView);
	}
}
