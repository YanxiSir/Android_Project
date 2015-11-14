package com.neu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;

import com.neu.fragment.LeftFragment;
import com.neu.fragment.LoginFragment;
import com.neu.helper.R;
import com.neu.tools.NetUtils;
import com.neu.tools.UpdateManager;
import com.neu.view.SlidingMenu;

public class MainActivity extends FragmentActivity{
	SlidingMenu mSlidingMenu;
	LeftFragment leftFragment;
	LoginFragment loginFragment;
	
	private RelativeLayout showMenuButton;
	

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		getWindow().requestFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titilebar);
		
		init();
		

		
		showMenuButton = (RelativeLayout) findViewById(R.id.title_left);
		showMenuButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showLeft();
			}
		});
		
		if(NetUtils.isHaveInternet(this)){
			UpdateManager manager = new UpdateManager(this,true);
			manager.checkUpdate();
		}
	}

	private void init() {
		mSlidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);
		mSlidingMenu.setLeftView(getLayoutInflater().inflate(
				R.layout.left_frame, null));
		mSlidingMenu.setCenterView(getLayoutInflater().inflate(
				R.layout.center_frame, null));

		FragmentTransaction t = this.getSupportFragmentManager()
				.beginTransaction();
		leftFragment = new LeftFragment();
		t.replace(R.id.left_frame, leftFragment);

		loginFragment = new LoginFragment();
		// BancheTimeFragment bc = new BancheTimeFragment();
		t.replace(R.id.center_frame, loginFragment);

		t.commit();
	}

	public void showLeft() {
		mSlidingMenu.showLeftView();

	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
		
	}
	
	

}
