package com.smilexi.sx.fragment;

import com.smilexi.sx.R;
import com.smilexi.sx.activity.CampusLifeDynamicActivity;
import com.smilexi.sx.interfaces.RefreshFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class Fragment2 extends Fragment implements RefreshFragment {

	private TextView campusLife;
	private TextView question;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_menu2, container,
				false);

		initUI(rootView);

		return rootView;
	}

	public void initUI(View v) {
		campusLife = (TextView) v.findViewById(R.id.fra2_life_text);
		question = (TextView) v.findViewById(R.id.fra2_question_text);

		campusLife.setOnClickListener(campusClickLis);
		question.setOnClickListener(questionClickLis); 
	}
	private OnClickListener questionClickLis = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(getActivity(), CampusLifeDynamicActivity.class);
			intent.putExtra("dynamicType", CampusLifeDynamicActivity.QUESTION_DYNAMIC);
			startActivity(intent);
		}
	};

	private OnClickListener campusClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(getActivity(), CampusLifeDynamicActivity.class);
			intent.putExtra("dynamicType", CampusLifeDynamicActivity.CAMPUS_DYNAMIC);
			startActivity(intent);
		}
	};

	@Override
	public void refreshContext() {

	}
}
