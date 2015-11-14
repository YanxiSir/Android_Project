package com.xiaowu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.xiaowu.projectdesign.R;

public class SelectUserTypeActivity extends Activity {
	
//	private RelativeLayout titleLeftRel,titleRightRel;
//	private TextView titleCenterText;
	
	public static final int TYPE_USER = 1;
	public static final int TYPE_SHOP = 2;

	private TextView userBtn;
	private TextView shopBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_select_user_type);
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
//				R.layout.titlebar_left_center_right);
//		titleLeftRel = (RelativeLayout) findViewById(R.id.titlebar_lcr_left_rel);
//		titleRightRel = (RelativeLayout) findViewById(R.id.titlebar_lcr_right_rel);
//		titleCenterText = (TextView) findViewById(R.id.titlebar_lcr_center_text);
		
		userBtn = (TextView) findViewById(R.id.select_type_user);
		shopBtn = (TextView) findViewById(R.id.select_type_shop);
		
		userBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(SelectUserTypeActivity.this,Login.class);
				i.putExtra("typ", TYPE_USER);
				startActivity(i); 
			}
		});
		shopBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(SelectUserTypeActivity.this,Login.class);
				i.putExtra("typ", TYPE_SHOP);
				startActivity(i); 
			}
		});
		
	}

}
