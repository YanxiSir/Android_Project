package com.neu.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.neu.activity.MainActivity;
import com.neu.helper.R;

public class LeftFragment extends Fragment {
	private TextView account_tv;
	private TextView range_tv;
	
	private TextView ipLogin;
	private TextView carInfo;
	private TextView aboutUs;
	private TextView forNewVersion;
	private TextView exit;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.left, null);
		
		account_tv = (TextView) view.findViewById(R.id.account_tv);
		range_tv = (TextView) view.findViewById(R.id.range_tv);
		
		ipLogin = (TextView) view.findViewById(R.id.ip_login);
		// rateOfFlow = (TextView) view.findViewById(R.id.rate_of_flow);
		carInfo = (TextView) view.findViewById(R.id.car_info);
		aboutUs = (TextView) view.findViewById(R.id.about_us);
		forNewVersion = (TextView) view.findViewById(R.id.for_new);
		exit = (TextView) view.findViewById(R.id.exit);

		ipLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
		
				// SlidingMenu mSlideMenu = new SlidingMenu(getActivity());

				/*
				 * mSlideMenu.setCenterView(getActivity().getLayoutInflater().
				 * inflate( R.layout.banche_info, null));
				 */
				FragmentTransaction ts = getActivity()
						.getSupportFragmentManager().beginTransaction();
				Fragment loginFragment = new LoginFragment();
				ts.replace(R.id.center_frame, loginFragment);
				// ts.addToBackStack(null);
				ts.commit();
				((MainActivity) getActivity()).showLeft();
			}
		});

		carInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			
				FragmentTransaction ts = getActivity()
						.getSupportFragmentManager().beginTransaction();
				Fragment bancheTimeFragment = new BancheTimeFragment();
				ts.replace(R.id.center_frame, bancheTimeFragment);
			    ts.addToBackStack(null);
				ts.commit();
				((MainActivity) getActivity()).showLeft();
			}
		});
		aboutUs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			
				AboutUsFragment aboutUsFragment = new AboutUsFragment();
				FragmentTransaction ts = getActivity()
						.getSupportFragmentManager().beginTransaction();
				ts.replace(R.id.center_frame, aboutUsFragment);
				ts.commit();
				((MainActivity) getActivity()).showLeft();
			}
		});
		forNewVersion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			
				CheckFragment fragment = new CheckFragment();
				FragmentTransaction ts = getActivity()
						.getSupportFragmentManager().beginTransaction();
				ts.replace(R.id.center_frame, fragment);
				ts.commit();
				((MainActivity) getActivity()).showLeft();
			}
		});
		exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				android.os.Process.killProcess(android.os.Process.myPid());
				
				
				/*ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE); 
				am.killBackgroundProcesses("cn.com.android123.cwj"); */
				
				/*((MainActivity) getActivity()).showLeft();
				getActivity().finish();*/
			}
		});

		return view;
	}
	public void setTopText(String account,String range){
		account_tv.setText(account);
		range_tv.setText(range);
		
		
	}
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

}
