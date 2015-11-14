package com.xiaowu.fragment;

import com.xiaowu.activity.AddressActivity;
import com.xiaowu.activity.MyInfoPage;
import com.xiaowu.common.XwContext;
import com.xiaowu.projectdesign.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Fragment3 extends Fragment {
	private RelativeLayout manageAddressRel;
	private RelativeLayout fra3_into_myInfo;
	private TextView username,phone,leftMoney;
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment3, container, false);
		initUI(rootView);

		
		return rootView;
	}

	private OnClickListener manageAddrClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(getActivity(), AddressActivity.class);
			startActivity(intent);
		}
	};
	private OnClickListener IntoMyInfoClicklis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			 Intent intent=new Intent(getActivity(),MyInfoPage.class);
			 startActivity(intent);
			

		}
	};

	public void initUI(View v) {
		manageAddressRel = (RelativeLayout) v
				.findViewById(R.id.fra3_manage_address);
		fra3_into_myInfo = (RelativeLayout) v
				.findViewById(R.id.fra3_into_myInfo);
		username = (TextView) v.findViewById(R.id.fra3_username);
		phone = (TextView) v.findViewById(R.id.fra3_telnumber);
		leftMoney = (TextView) v.findViewById(R.id.leftmoney_amount);
		
		username.setText(XwContext.getInstance().getUserInfo().getUname());
		phone.setText(XwContext.getInstance().getUserInfo().getPhone());
		leftMoney.setText(XwContext.getInstance().getUserInfo().getLeftMoney()+"");
		
		manageAddressRel.setOnClickListener(manageAddrClickLis);
		fra3_into_myInfo.setOnClickListener(IntoMyInfoClicklis);
		
	}
}
