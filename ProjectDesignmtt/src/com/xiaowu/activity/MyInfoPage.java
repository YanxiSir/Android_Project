package com.xiaowu.activity;
import com.xiaowu.projectdesign.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyInfoPage extends Activity {
	private RelativeLayout Into_changesuername_page;
	private RelativeLayout change_password_rel;
	private RelativeLayout change_titlebarRel;
	
	private TextView titlebar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.my_info_page);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.change_titlebar);
		Init();
		titlebar=(TextView) findViewById(R.id.change_text);
		titlebar.setText("Œ“µƒ’Àªß");
		
		Into_changesuername_page.setOnClickListener(Into_changesuername_pageClickLis);
		change_password_rel.setOnClickListener(change_password_relClickLis);
		change_titlebarRel.setOnClickListener(change_titlebarRelClickLis);

	}
	private void Init()
	{		
		change_password_rel=(RelativeLayout) findViewById(R.id.change_password_rel);
		Into_changesuername_page=(RelativeLayout) findViewById(R.id.Into_changeusername_page);
		change_titlebarRel=(RelativeLayout) findViewById(R.id.change_titlebar);

	}
	
	private OnClickListener change_titlebarRelClickLis=new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			onBackPressed();
		}
	};
	
	   private OnClickListener change_password_relClickLis=new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MyInfoPage.this,ChnagePasswordPage.class);
				startActivity(intent);	
			}
		};
    private OnClickListener Into_changesuername_pageClickLis=new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent=new Intent(MyInfoPage.this,ChangeUsernamePage.class);
			startActivity(intent);	
		}
	};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_info_page, menu);
		return true;
	}

}
