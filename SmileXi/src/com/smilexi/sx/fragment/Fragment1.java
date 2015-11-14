package com.smilexi.sx.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import cn.jpush.android.api.JPushInterface;

import com.jauker.widget.BadgeView;
import com.smilexi.sx.R;
import com.smilexi.sx.activity.NotifacationActivity;
import com.smilexi.sx.interfaces.RefreshFragment;
import com.smilexi.sx.receiver.NotifacationReceiver;

public class Fragment1 extends Fragment implements RefreshFragment,
		OnClickListener {

	private LinearLayout pinglunLl, answerLl, zanLl;
	private RelativeLayout pinglunRel, answerRel, zanRel;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_menu1, container,
				false);
		initUI(rootView);
		return rootView;
	}

	private void initUI(View v) {
		pinglunLl = (LinearLayout) v.findViewById(R.id.fra1_pinglun_ll);
		answerLl = (LinearLayout) v.findViewById(R.id.fra1_answer_ll);
		zanLl = (LinearLayout) v.findViewById(R.id.fra1_zan_ll);
		pinglunRel = (RelativeLayout) v.findViewById(R.id.fra1_pinglun_img_rel);
		answerRel = (RelativeLayout) v.findViewById(R.id.fra1_answer_img_rel);
		zanRel = (RelativeLayout) v.findViewById(R.id.fra1_zan_img_rel);

		pinglunLl.setOnClickListener(this);
		answerLl.setOnClickListener(this);
		zanLl.setOnClickListener(this);

		answerLl.setVisibility(View.GONE);

		// BadgeView bv = new BadgeView(getActivity());
		// bv.setTargetView(pinglunRel);
		// bv.setBadgeGravity(Gravity.TOP|Gravity.RIGHT);
		// bv.setBadgeCount(2);
	}

	@Override
	public void onClick(View arg0) {
		Intent intent = new Intent(getActivity(), NotifacationActivity.class);
		switch (arg0.getId()) {
		case R.id.fra1_pinglun_ll:
			intent.putExtra("notifacateType", NotifacationActivity.TYPE_REPLY);
			startActivity(intent);
			break;
		case R.id.fra1_answer_ll:
			intent.putExtra("notifacateType", NotifacationActivity.TYPE_ANSWER);
			startActivity(intent);
			break;
		case R.id.fra1_zan_ll:
			intent.putExtra("notifacateType", NotifacationActivity.TYPE_ZAN);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	

	@Override
	public void refreshContext() {
		BadgeView bv = new BadgeView(getActivity());
		int replyCount = NotifacationReceiver.reply_count;
		if (replyCount > 0) {

			bv.setTargetView(pinglunRel);
			bv.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);
			bv.setBadgeCount(replyCount);
		} else {
			bv.refreshDrawableState();
		}
		int zanCount = NotifacationReceiver.zan_count;
		if (zanCount > 0) {
			bv.setTargetView(zanRel);
			bv.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);
			bv.setBadgeCount(replyCount);
		} else {
			bv.refreshDrawableState();
		}
	}
}
